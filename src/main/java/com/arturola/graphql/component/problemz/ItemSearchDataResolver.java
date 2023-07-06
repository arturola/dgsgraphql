package com.arturola.graphql.component.problemz;

import com.arturola.graphql.service.query.ProblemzQueryService;
import com.arturola.graphql.service.query.SolutionzQueryService;
import com.arturola.graphql.util.GraphBeanMapper;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.MobileAppFilter;
import com.netflix.dgs.codegen.generated.types.SearchItemFilter;
import com.netflix.dgs.codegen.generated.types.SearcheableItem;
import com.netflix.dgs.codegen.generated.types.Solution;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class ItemSearchDataResolver {
    @Autowired
    private ProblemzQueryService problemzQueryService;
    @Autowired
    private SolutionzQueryService solutionzQueryService;

    @DgsQuery(field = DgsConstants.QUERY.ItemSearch)
    public List<SearcheableItem> searchItems(
        @InputArgument(name = "filter", collectionType = SearchItemFilter.class )
        SearchItemFilter filter
    ) {
        var result = new ArrayList<SearcheableItem>();
        var keyword = filter.getKeyword();
        var listProblemsByKeyword = problemzQueryService.problemzByKeyword(keyword).stream()
                .map(GraphBeanMapper::mapToGraphql).collect(Collectors.toList());

        result.addAll(listProblemsByKeyword);

        var listSolutionsByKeyword = solutionzQueryService.problemzByKeyword(keyword).stream()
                .map(GraphBeanMapper::mapToGraphql).collect(Collectors.toList());

        result.addAll(listSolutionsByKeyword);

        if(result.isEmpty()) {
            throw new DgsEntityNotFoundException(("No hay items que coincidan con el criterio de busqueda: " + keyword));
        }

        result.sort(Comparator.comparing(SearcheableItem::getCreateDateTime).reversed());

        return result;
    }
}
