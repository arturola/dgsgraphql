package com.arturola.graphql.component.problemz;

import com.arturola.graphql.service.command.UserzCommandService;
import com.arturola.graphql.service.query.UserzQueryService;
import com.arturola.graphql.util.GraphBeanMapper;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.*;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import graphql.GraphQLException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
public class UserDataResolver {

    @Autowired
    private UserzCommandService userzCommandService;
    @Autowired
    private UserzQueryService userzQueryService;

    @DgsQuery(field = "me")
    public User getAccountInfo(@RequestHeader(name = "authToken", required = true) String authToken) {
      /*  var userzOpt = userzQueryService.findUserByAuthToken(authToken);
        var userz = userzOpt.get();*/
        var userz = userzQueryService.findUserByAuthToken(authToken).orElseThrow(DgsEntityNotFoundException::new);

        return GraphBeanMapper.mapToGraphql(userz);
    }

    @DgsMutation(field = DgsConstants.MUTATION.UserCreate)
    public UserResponse createUser(@InputArgument(name = "user") UserCreateInput userCreateInput) {
        var userz = GraphBeanMapper.mapToEntity(userCreateInput);

        var newUserz = userzCommandService.createUserz(userz);

        var user = GraphBeanMapper.mapToGraphql(newUserz);

        return UserResponse.newBuilder()
                .user(user)
                .build();
    }

    @DgsMutation
    public UserResponse userLogin(@NotNull @InputArgument(name="user") UserLoginInput userLoginInput) {
        var authTokenz = userzCommandService.login(userLoginInput.getUsername(), userLoginInput.getPassword());

        var authToken = GraphBeanMapper.mapToGraphql(authTokenz);

        var user = getAccountInfo(authToken.getAuthToken());

        return UserResponse.newBuilder()
                .authToken(authToken)
                .user(user)
                .build();
    }

    @DgsMutation(field = DgsConstants.MUTATION.UserActivate)
    public UserActivationResponse userActivation(@InputArgument(name="user") UserActivationInput userActivationInput) {
        var userz = userzCommandService.activateUser(userActivationInput.getUsername(),userActivationInput.getActive())
                        .orElseThrow(DgsEntityNotFoundException::new);

        return UserActivationResponse.newBuilder()
                .isActive(userz.isActive())
                .build();
    }
}


 /* return userzCommandService.activateUser(userActivationInput.getUsername(), userActivationInput.getIsActive())
                .map(userz -> {
                    var user = GraphBeanMapper.mapToGraphql(userz);

                    return UserActivationResponse.newBuilder()
                            .user(user)
                            .build();
                })
                .orElseThrow(DgsEntityNotFoundException::new);*/