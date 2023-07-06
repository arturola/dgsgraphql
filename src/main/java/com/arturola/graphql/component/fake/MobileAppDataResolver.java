package com.arturola.graphql.component.fake;

import com.arturola.graphql.datasource.fake.FakeMobileAppDataSource;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.MobileApp;
import com.netflix.dgs.codegen.generated.types.MobileAppFilter;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class MobileAppDataResolver {
    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.MobileApps
    )
    public List<MobileApp> getMobileApps(@InputArgument(name = "mobileAppFilter", collectionType = MobileAppFilter.class)
            Optional<MobileAppFilter> filter) {

        if(filter.isEmpty())
            return FakeMobileAppDataSource.MOBILE_APP_LIST;


        return FakeMobileAppDataSource.MOBILE_APP_LIST.stream()
                .filter(mobileApp -> this.matchFilter(filter.get(), mobileApp)).collect(Collectors.toList());
    }

    private boolean matchFilter(MobileAppFilter mobileAppFilter, MobileApp mobileApp) {
        var isAppMatch = StringUtils.containsAnyIgnoreCase(mobileApp.getName(),
                StringUtils.defaultIfBlank(mobileAppFilter.getName(), StringUtils.EMPTY))
        && StringUtils.containsAnyIgnoreCase(mobileApp.getVersion(),
                StringUtils.defaultIfBlank(mobileAppFilter.getVersion(), StringUtils.EMPTY))
                && mobileApp.getReleaseDate().isAfter(
                        Optional.ofNullable(mobileAppFilter.getReleasedAfter()).orElse(LocalDate.MIN))
                && mobileApp.getReleaseDate().isBefore(
                    Optional.ofNullable(mobileAppFilter.getReleasedBefore()).orElse(LocalDate.MIN))
                && mobileApp.getDownloaded() >=
                        Optional.ofNullable(mobileAppFilter.getMinimunDownload()).orElse(0);

        if(!isAppMatch)
            return false;

        if(StringUtils.isNotBlank(mobileAppFilter.getPlatform())
        && !mobileApp.getPlatform().contains(mobileAppFilter.getPlatform().toLowerCase()))
            return false;

        if(mobileAppFilter.getAuthor() != null
                && !StringUtils.containsAnyIgnoreCase(mobileApp.getAuthor().getName(),
                StringUtils.defaultIfBlank(mobileAppFilter.getAuthor().getName(), StringUtils.EMPTY)))
            return false;

        if(mobileAppFilter.getCategory() != null
                && !mobileApp.getCategory().equals(mobileAppFilter.getCategory()))
            return false;

        return true;
    }
}
