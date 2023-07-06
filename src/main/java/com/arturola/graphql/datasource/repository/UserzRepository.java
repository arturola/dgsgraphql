package com.arturola.graphql.datasource.repository;

import com.arturola.graphql.datasource.entity.Userz;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserzRepository extends CrudRepository<Userz, String> {
    Optional<Userz> findByUsernameIgnoreCase(String username);

    @Query(
            nativeQuery = true,
            value =   "SELECT U.* FROM USERZ U "
                    + "INNER JOIN USERZ_TOKEN UT "
                    +   "ON U.ID = UT.USER_ID "
                    + "WHERE UT.AUTH_TOKEN = ?"
    )
    Optional<Userz> findUserByToken(String token);


    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value =  "UPDATE USERZ U "
                    + "SET U.ACTIVE = :isActive "
                    + "WHERE UPPER(U.USERNAME) = UPPER(:username)"
    )
    void activateUserz(@Param("username") String username, @Param("isActive") boolean isActive);
}


/*
 *
 * select * from userz u
 * where ut.auth_token = ? && ut.expriy_timestamp > current_timestamp
 * inner join userz_token ut
 *   on u.id = ut.user_id
 *
 * */