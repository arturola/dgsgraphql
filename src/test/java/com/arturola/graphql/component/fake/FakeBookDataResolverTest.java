package com.arturola.graphql.component.fake;

import com.jayway.jsonpath.TypeRef;
import com.netflix.dgs.codegen.generated.client.BooksByReleasedGraphQLQuery;
import com.netflix.dgs.codegen.generated.client.BooksByReleasedProjectionRoot;
import com.netflix.dgs.codegen.generated.client.BooksGraphQLQuery;
import com.netflix.dgs.codegen.generated.client.BooksProjectionRoot;
import com.netflix.dgs.codegen.generated.types.Author;
import com.netflix.dgs.codegen.generated.types.ReleaseHistory;
import com.netflix.dgs.codegen.generated.types.ReleaseHistoryInput;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQuery;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FakeBookDataResolverTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    Faker faker;


    @Test
    void booksWrittenBy() {
        var graphqlQuery = new BooksGraphQLQuery.Builder().build();
        var projectionRoot = new BooksProjectionRoot()
                .title()
                .author().name()
                .originCountry()
                .getRoot().released().year(); // Query result
        var grapqlQueryRequest = new GraphQLQueryRequest(graphqlQuery, projectionRoot).serialize();

        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
                grapqlQueryRequest, "data.books[*].title");

        assertNotNull(titles);
        assertFalse(titles.isEmpty());

        List<Author> authors = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                grapqlQueryRequest,
                "data.books[*].author",
                new TypeRef<List<Author>>() {
                }
        );

        assertNotNull(authors);
        assertEquals(titles.size(), authors.size());

        List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                grapqlQueryRequest,
                "data.books[*].released.year",
                new TypeRef<List<Integer>>() {
                }
        );

        assertNotNull(releaseYears);
        assertEquals(titles.size(), releaseYears.size());

    }

    @Test
    void getBooksByReleased() {
        var graphqlQuery = new BooksGraphQLQuery.Builder().build();
        var projectionRoot = new BooksProjectionRoot()
                .title()
                .author().name()
                .originCountry()
                .getRoot().released().year(); // Query result
        var grapqlQueryRequest = new GraphQLQueryRequest(graphqlQuery, projectionRoot).serialize();

        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
                grapqlQueryRequest, "data.books[*].title");

        List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                grapqlQueryRequest,
                "data.books[*].released.year",
                new TypeRef<List<Integer>>() {
                }
        );

        assertNotNull(releaseYears);
        assertEquals(titles.size(), releaseYears.size());
    }

    @Test
    void testBooksWithInput() {
        int expectedYear = faker.number().numberBetween(2019, 2021);
        boolean expectedPrintEdition = faker.bool().bool();

        var releaseHistoryInput = ReleaseHistoryInput.newBuilder()
                .year(expectedYear)
                .printedEdition(expectedPrintEdition)
                .build();

        var graphqlQuery = BooksByReleasedGraphQLQuery.newRequest()
                .releasedInput(releaseHistoryInput)
                .build();
        var projectionRoot = new BooksProjectionRoot().released().year().printedEdition();

        var graphQueryRequest = new GraphQLQueryRequest(
                graphqlQuery, projectionRoot
        ).serialize();

        List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPath(
                graphQueryRequest, "data.booksByReleased[*].released.year"
        );

        var uniqueReleaseYears = new HashSet<>(releaseYears);

        assertNotNull(uniqueReleaseYears);
        assertTrue(uniqueReleaseYears.size() <= 1);

        if(!uniqueReleaseYears.isEmpty()) {
            assertTrue(uniqueReleaseYears.contains(expectedYear));
        }

        List<Boolean> releasePrintedEditions = dgsQueryExecutor.executeAndExtractJsonPath(
                graphQueryRequest, "data.booksByReleased[*].released.printedEdition"
        );

        var uniqueReleasePrintedEditions = new HashSet<>(releasePrintedEditions);

        assertNotNull(uniqueReleasePrintedEditions);
        assertTrue(uniqueReleasePrintedEditions.size() <= 1);

        if(!uniqueReleasePrintedEditions.isEmpty()) {
            assertTrue(uniqueReleasePrintedEditions.contains(expectedPrintEdition));
        }
    }
}