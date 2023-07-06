package com.arturola.graphql.datasource.fake;

import com.netflix.dgs.codegen.generated.types.*;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class FakeMobileAppDataSource {
    @Autowired
    private Faker faker;
    public static final List<MobileApp> MOBILE_APP_LIST = new ArrayList<>();

    @PostConstruct
    private void postContruct() throws MalformedURLException {
        for(int i = 0; i < 20; i++) {
            var addresses = new ArrayList<Address>();
            var author = Author.newBuilder()
                    .addresses(addresses)
                    .name(faker.book().author())
                    .originCountry(faker.country().name())
                    .build();

            /*var released = ReleaseHistory.newBuilder()
                    .printedEdition(faker.bool().bool())
                    .releasedCountry(faker.country().name())
                    .year(faker.number().numberBetween(2019, 2021))
                    .build();*/

            var mobileApp = MobileApp.newBuilder()
                    .name(faker.app().name())
                    .author(author).version(faker.app().version())
                    .platform(randomMobileAppPlatform())
                    .appId(UUID.randomUUID().toString())
                    .releaseDate(LocalDate.now().minusDays(faker.random().nextInt(365)))
                    .downloaded(faker.number().numberBetween(1, 1_500_000))
                    .homepage(new URL("https://" + faker.internet().url()))
                    //.category(MobileAppCategory.values()[faker.random().nextInt(MobileAppCategory.values().length)])
                    .category(MobileAppCategory.values()[
                            faker.random().nextInt(MobileAppCategory.values().length)])
                    .build();

            for(int j = 0; j < ThreadLocalRandom.current().nextInt(1, 3); j++) { // Up to 3 addresses for each author
                var address = Address.newBuilder()
                        .country(faker.address().country())
                        .city(faker.address().city())
                        .zipCode(faker.address().zipCode())
                        .street(faker.address().streetAddress())
                        .build();

                addresses.add(address);
            }

            MOBILE_APP_LIST.add(mobileApp);
        }
    }

    private List<String> randomMobileAppPlatform() {
        switch (ThreadLocalRandom.current().nextInt(10) % 3) {
            case 0:
                return List.of("android");
            case 1:
                return List.of("ios");
            default:
                return List.of("ios", "android");
        }
    }
}
