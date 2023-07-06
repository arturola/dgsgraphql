package com.arturola.graphql.component.fake;

import com.arturola.graphql.datasource.fake.FakeHelloDataSource;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.Hello;
import com.netflix.dgs.codegen.generated.types.HelloInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import java.time.OffsetDateTime;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class HelloMutation {

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.AddHello
    )
    // Tambien podemos usar @DgsMutation
    public int addHello(@InputArgument(name = "helloInput") HelloInput input) {
       var hello = Hello.newBuilder()
               .text(input.getText())
               .randomNumber(input.getRandomNumber())
               .build();

        FakeHelloDataSource.HELLO_LIST.add(hello);

       return  FakeHelloDataSource.HELLO_LIST.size();
    }

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.ReplaceHello)
    public List<Hello> replaceHello(@InputArgument(name = "helloInput") HelloInput input) {
        FakeHelloDataSource.HELLO_LIST.stream()
                .filter(hello -> hello.getRandomNumber() == input.getRandomNumber())
                .forEach(hello -> hello.setText(input.getText()));

        return FakeHelloDataSource.HELLO_LIST;
    }

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.DeleteHello)
    public int deleteHello(int number) {
      /*  var helloABorrar = FakeHelloDataSource.HELLO_LIST.stream()
                .filter(hello -> hello.getRandomNumber() == number);

        FakeHelloDataSource.HELLO_LIST.remove(helloABorrar); */

        FakeHelloDataSource.HELLO_LIST.removeIf(hello -> hello.getRandomNumber() == number);

        return FakeHelloDataSource.HELLO_LIST.size();
    }
}
