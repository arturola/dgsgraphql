package com.arturola.graphql.util;

import com.arturola.graphql.datasource.entity.Problemz;
import com.arturola.graphql.datasource.entity.Solutionz;
import com.arturola.graphql.datasource.entity.Userz;
import com.arturola.graphql.datasource.entity.UserzToken;
import com.arturola.graphql.datasource.repository.UserzRepository;
import com.netflix.dgs.codegen.generated.types.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.type.descriptor.java.ZoneOffsetJavaType;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GraphBeanMapper {
    private static final PrettyTime PRETTY_TIME = new PrettyTime();
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(1);

    public static User mapToGraphql(Userz original) {
        var result = new User();
        var createDateTime = original.getCreationTimestamp().atOffset(ZONE_OFFSET);

        result.setAvatar(original.getAvatar());
        result.setCreateDateTime(createDateTime);
        result.setDisplayName(original.getDisplayName());
        result.setEmail(original.getEmail());
        result.setId(original.getId().toString());
        result.setUsername(original.getUsername());

        return result;
    }

    public static Problem mapToGraphql(Problemz original) {
        var result = new Problem();
        var author = mapToGraphql(original.getCreatedBy());
        var solutions = original.getSolutions().stream()
                .sorted(Comparator.comparing(Solutionz::getCreationTimestamp).reversed()) // este sorted orderna las soluciones by newest first.
                .map(GraphBeanMapper::mapToGraphql)
                .collect(Collectors.toList());
        var createDateTime = original.getCreationTimestamp().atOffset(ZONE_OFFSET);
        var tagList = List.of(original.getTags().split(","));

        result.setId(original.getId().toString());
        result.setAuthor(author);
        result.setTitle(original.getTitle());
        result.setContent(original.getContent());
        result.setSolutionCount(original.getSolutions().size());
        result.setSolutions(solutions);
        result.setCreateDateTime(createDateTime);
        result.setPrettyCreateDateTime(PRETTY_TIME.format(createDateTime));
        result.setTags(tagList);

        return result;
    }
    public static Solution mapToGraphql(Solutionz original) {
        var result = new Solution();
        var createDateTime = original.getCreationTimestamp().atOffset(ZONE_OFFSET);
        var author = mapToGraphql(original.getCreatedBy());
        var category = StringUtils
                .containsAnyIgnoreCase(original.getCategory(), SolutionCategory.EXPLANATION.toString()) ?
                SolutionCategory.EXPLANATION : SolutionCategory.REFERENCE;

        result.setId(original.getId().toString());
        result.setContent(original.getContent());
        result.setVoteAsBadCount(original.getVoteBadCount());
        result.setVoteAsGoodCount(original.getVoteGoodCount());
        result.setCreateDateTime(createDateTime);
        result.setAuthor(author);
        result.setCategory(category);
        result.setPrettyCreateDateTime(PRETTY_TIME.format(createDateTime));

        return result;
    }
    public static UserAuthToken mapToGraphql(UserzToken original) {
        var result = new UserAuthToken();

        result.setAuthToken(original.getAuthToken());
        result.setExpiryTime(original.getExpiryTimestamp().atOffset(ZONE_OFFSET));

        return result;
    }

    public static Problemz mapToEntity(ProblemCreateInput original, Userz userz) {
        var result = new Problemz();

        result.setId(RandomStringUtils.randomAlphanumeric(36));
        result.setCreatedBy(userz);
        result.setTitle(original.getTitle());
        result.setContent(original.getContent());
        result.setTags(String.join(",", original.getTags()));
        result.setSolutions(Collections.emptyList());

        return result;
    }

    public static Solutionz mapToEntity(SolutionCreateInput solutionCreateInput, Userz userz, Problemz problemz) {
        var result = new Solutionz();

        result.setId(RandomStringUtils.randomAlphanumeric(36));
        result.setCreatedBy(userz);
        result.setContent(solutionCreateInput.getContent());
        result.setCategory(solutionCreateInput.getCategory().name());
        result.setProblemz(problemz);

        return result;
    }

    public static Userz mapToEntity(UserCreateInput userCreateInput) {
        var result = new Userz();

        result.setId(RandomStringUtils.randomAlphanumeric(36));
        result.setUsername(userCreateInput.getUsername());
        result.setDisplayName(userCreateInput.getDisplayName());
        result.setEmail(userCreateInput.getEmail());
        result.setAvatar(userCreateInput.getAvatar());
        result.setHashedPassword(HashUtil.getBcryptHash(userCreateInput.getPassword()));
        result.setActive(true);

        return result;
    }
}
