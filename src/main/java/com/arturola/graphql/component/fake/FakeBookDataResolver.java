package com.arturola.graphql.component.fake;

import com.arturola.graphql.datasource.fake.FakeBookDataSource;
import com.arturola.graphql.datasource.fake.FakeHelloDataSource;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.Book;
import com.netflix.dgs.codegen.generated.types.Hello;
import com.netflix.dgs.codegen.generated.types.ReleaseHistory;
import com.netflix.dgs.codegen.generated.types.ReleaseHistoryInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@DgsComponent
public class FakeBookDataResolver {

    @DgsData(parentType = "Query", field = "books")
    public List<Book> booksWrittenBy(@InputArgument(name = "author") String authorName) {
        if(StringUtils.isBlank(authorName)) {
            return FakeBookDataSource.BOOK_LIST;
        }
        return FakeBookDataSource.BOOK_LIST.stream()
                //.filter(book -> authorName.equals(book.getAuthor().getName())).collect(Collectors.toList());
                .filter(book -> StringUtils.containsAnyIgnoreCase( // la diferencia con la linea de arriba, es que asi no hace falta que escriba el nombre entero del autor en la query, basta con algunas letras de su nombre o apellido.
                book.getAuthor().getName(), authorName)).collect(Collectors.toList());

    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.BooksByReleased
    )
    public List<Book> getBooksByReleased(DataFetchingEnvironment dataFetchingEnvironment) {
        var releaseMap = (Map<String, Object>) dataFetchingEnvironment.getArgument("releasedInput");
        var releasedInput = ReleaseHistoryInput.newBuilder()
                .printedEdition((boolean) releaseMap.get(DgsConstants.RELEASEHISTORYINPUT.PrintedEdition))
                .year((int) releaseMap.get(DgsConstants.RELEASEHISTORYINPUT.Year))
                .build();
        return FakeBookDataSource.BOOK_LIST.stream()
                .filter(book -> this.matchReleaseHistory(releasedInput, book.getReleased()))
                .collect(Collectors.toList());
    }

    private boolean matchReleaseHistory(ReleaseHistoryInput input, ReleaseHistory element) {
        return input.getPrintedEdition().equals(element.getPrintedEdition())
                && input.getYear() == element.getYear();
    }


}
