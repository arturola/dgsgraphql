package com.arturola.graphql.component.fake;

import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FakeHelloDataResolverTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void allHellos() {
        var graphqlQuery = """
                   {
                       allHellos {
                           text
                           randomNumber   
                         }                   
                   } 
                """;

        List<String> texts = dgsQueryExecutor.executeAndExtractJsonPath(
                graphqlQuery, "data.allHellos[*].text"
        );
        List<Integer> randomNumbers = dgsQueryExecutor.executeAndExtractJsonPath(
                graphqlQuery, "data.allHellos[*].randomNumber"
        );

        assertFalse(texts.isEmpty());
        assertNotNull(texts);
        assertNotNull(randomNumbers);
        assertEquals(texts.size(), randomNumbers.size());
    }

    @Test
    void oneHello() {
        var graphqlQuery = """
                   {
                       oneHello {
                           text
                           randomNumber   
                         }                   
                   } 
                """;

        String text = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQuery, "data.oneHello.text");
        Integer randomNumber = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQuery, "data.oneHello.randomNumber");

        assertFalse(StringUtils.isBlank(text));
        assertNotNull(randomNumber);
    }
}