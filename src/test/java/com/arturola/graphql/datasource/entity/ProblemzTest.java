package com.arturola.graphql.datasource.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ProblemzTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link Problemz}
     *   <li>{@link Problemz#setContent(String)}
     *   <li>{@link Problemz#setCreatedBy(Userz)}
     *   <li>{@link Problemz#setCreationTimestamp(LocalDateTime)}
     *   <li>{@link Problemz#setId(String)}
     *   <li>{@link Problemz#setSolutions(List)}
     *   <li>{@link Problemz#setTags(String)}
     *   <li>{@link Problemz#setTitle(String)}
     *   <li>{@link Problemz#getContent()}
     *   <li>{@link Problemz#getCreatedBy()}
     *   <li>{@link Problemz#getCreationTimestamp()}
     *   <li>{@link Problemz#getId()}
     *   <li>{@link Problemz#getSolutions()}
     *   <li>{@link Problemz#getTags()}
     *   <li>{@link Problemz#getTitle()}
     * </ul>
     */
    @Test
    void testConstructor() throws MalformedURLException {
        Problemz actualProblemz = new Problemz();

        actualProblemz.setContent("Not all who wander are lost");

        Userz createdBy = new Userz();

        createdBy.setActive(true);
        createdBy.setAvatar(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL());
        createdBy.setCreationTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        createdBy.setDisplayName("Display Name");
        createdBy.setEmail("jane.doe@example.org");
        createdBy.setHashedPassword("iloveyou");
        createdBy.setId("42");
        createdBy.setUsername("janedoe");

        actualProblemz.setCreatedBy(createdBy);

        LocalDateTime creationTimestamp = LocalDate.of(1970, 1, 1).atStartOfDay();

        actualProblemz.setCreationTimestamp(creationTimestamp);
        actualProblemz.setId("42");

        ArrayList<Solutionz> solutions = new ArrayList<>();

        actualProblemz.setSolutions(solutions);
        actualProblemz.setTags("Tags");
        actualProblemz.setTitle("Dr");

        assertEquals("Not all who wander are lost", actualProblemz.getContent());
        assertSame(createdBy, actualProblemz.getCreatedBy());
        assertSame(creationTimestamp, actualProblemz.getCreationTimestamp());
        assertEquals("42", actualProblemz.getId());
        assertSame(solutions, actualProblemz.getSolutions());
        assertEquals("Tags", actualProblemz.getTags());
        assertEquals("Dr", actualProblemz.getTitle());
    }
}

