package com.arturola.graphql.component.fake;

import com.arturola.graphql.datasource.fake.FakePetDataSource;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.*;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class PetDataResolver {
    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.Pets
    )
    public List<Pet> getPets(
            @InputArgument(name = "petFilter", collectionType = PetFilter.class)
            Optional<PetFilter> petFilter) {

        if(petFilter.isEmpty()) {
            return FakePetDataSource.PET_LIST;
        }

       /* return FakePetDataSource.PET_LIST.stream()
                .filter(pet -> petFilter.get().getPetType().equals(pet.getName())).collect(Collectors.toList());
*/
        return FakePetDataSource.PET_LIST.stream()
                .filter(pet -> this.matchFilter(petFilter.get(), pet)).collect(Collectors.toList());
    }

    private boolean matchFilter(PetFilter petFilter, Pet pet) {
       /* if (petFilter.getPetType().isEmpty())
            return true;*/

        if(StringUtils.isBlank(petFilter.getPetType()))
            return true;

        if(petFilter.getPetType().equalsIgnoreCase(Dog.class.getSimpleName()))
            return pet instanceof Dog;
        else if (petFilter.getPetType().equalsIgnoreCase(Cat.class.getSimpleName())) {
            return pet instanceof Cat;
        } else
            return false;
    }


}
