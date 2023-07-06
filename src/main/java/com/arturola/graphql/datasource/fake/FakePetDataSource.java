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
public class FakePetDataSource {
    @Autowired
    private Faker faker;
    public static final List<Pet> PET_LIST = new ArrayList<>();

    @PostConstruct
    private void postContruct() throws MalformedURLException {
        for(int i = 0; i < 20; i++) {
          Pet animal = switch (i % 2) {
              case 0:
                  yield Dog.newBuilder()
                      .name(faker.dog().name())
                      /* .author(author).version(faker.app().version())
                       .platform(randomMobileAppPlatform())
                       .appId(UUID.randomUUID().toString())
                       .releaseDate(LocalDate.now().minusDays(faker.random().nextInt(365)))
                       .downloaded(faker.number().numberBetween(1, 1_500_000))
                       .homepage(new URL("https://" + faker.internet().url()))
                       //.category(MobileAppCategory.values()[faker.random().nextInt(MobileAppCategory.values().length)])
                       .category(MobileAppCategory.values()[
                               faker.random().nextInt(MobileAppCategory.values().length)])*/
                      .build();

              default:
                  yield Cat.newBuilder()
                          .name(faker.cat().name())
                          .build();
          };

          PET_LIST.add(animal);
        }
    }
}
