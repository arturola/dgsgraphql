package com.arturola.graphql.datasource.fake;

import com.netflix.dgs.codegen.generated.types.Address;
import com.netflix.dgs.codegen.generated.types.Author;
import com.netflix.dgs.codegen.generated.types.Book;
import com.netflix.dgs.codegen.generated.types.ReleaseHistory;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class FakeBookDataSource {
    @Autowired
    private Faker faker;
    public static final List<Book> BOOK_LIST = new ArrayList<>();

    @PostConstruct
    private void postContruct() {
      for(int i = 0; i < 20; i++) {
        var addresses = new ArrayList<Address>();
        var author = Author.newBuilder()
                .addresses(addresses)
                .name(faker.book().author())
                .originCountry(faker.country().name())
                .build();

        var released = ReleaseHistory.newBuilder()
                .printedEdition(faker.bool().bool())
                .releasedCountry(faker.country().name())
                .year(faker.number().numberBetween(2019, 2021))
                .build();

        var book = Book.newBuilder()
                .author(author)
                .title(faker.book().title())
                .released(released)
                .publisher(faker.book().publisher())
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

        BOOK_LIST.add(book);
      }

    }
}
