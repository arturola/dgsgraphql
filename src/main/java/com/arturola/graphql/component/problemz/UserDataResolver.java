package com.arturola.graphql.component.problemz;

import com.arturola.graphql.exception.ProblemzAuthenticationException;
import com.arturola.graphql.exception.ProblemzPermitionException;
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
import org.apache.commons.lang3.StringUtils;
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
    public UserResponse createUser(
            //@RequestHeader(name = "authToken", required = true) String authToken,
            @InputArgument(name = "user") UserCreateInput userCreateInput) {

       /* var userAuth = userzQueryService.findUserByAuthToken(authToken)
                .orElseThrow(ProblemzAuthenticationException::new);

        if(!StringUtils.equals(userAuth.getUserRole(), "ROLE_ADMIN")) {
            throw new ProblemzPermitionException();
        }*/

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
    public UserActivationResponse userActivation(
            @RequestHeader(name = "authToken", required = true) String authToken,
            @InputArgument(name="user") UserActivationInput userActivationInput) {

        var user = userzQueryService.findUserByAuthToken(authToken)
                .orElseThrow(ProblemzAuthenticationException::new);

        if(!StringUtils.equals(user.getUserRole(), "ROLE_ADMIN")) {
            throw new ProblemzPermitionException();
        }

        var userz = userzCommandService.activateUser(userActivationInput.getUsername(),userActivationInput.getActive())
                .orElseThrow(DgsEntityNotFoundException::new);

        return UserActivationResponse.newBuilder()
                .isActive(userz.isActive())
                .build();

        /*if ("ROLE_ADMIN".equals(user.getUserRole())) {
            var userz = userzCommandService.activateUser(userActivationInput.getUsername(),userActivationInput.getActive())
                        .orElseThrow(DgsEntityNotFoundException::new);

            return UserActivationResponse.newBuilder()
                    .isActive(userz.isActive())
                    .build();
        } else {
            throw new GraphQLException("You are not authorized to perform this operation");
        }*/
    }
}


