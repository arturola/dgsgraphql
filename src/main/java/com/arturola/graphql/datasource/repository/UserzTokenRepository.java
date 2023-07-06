package com.arturola.graphql.datasource.repository;

import com.arturola.graphql.datasource.entity.UserzToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserzTokenRepository extends CrudRepository<UserzToken, String> {
}
