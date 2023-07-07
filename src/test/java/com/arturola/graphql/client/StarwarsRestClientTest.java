package com.arturola.graphql.client;

import com.arturola.graphql.client.response.PlanetResponse;
import com.netflix.dgs.codegen.generated.types.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StarwarsRestClientTest {
    @Autowired
    private StarwarsRestClient starwarsRestClient;
    @Test
    void asJson() throws Exception {
        var query = """
                query allPlanets {
                  allPlanets {
                    planets {
                      name
                      climates
                      terrains
                      importantPeople: residentConnection {
                        people: residents {
                          name
                          gender
                        }
                      }
                    }
                  }
                }
                """;

        var body = new com.arturola.graphql.client.request.GraphqlRequest();

        body.setQuery(query);

        var response = starwarsRestClient.asJson(body, null);

        assertNotNull(response);
    }
    @Test
    void asJsonException() throws Exception {
        var query = """
                query allPlanets {
                  allPlanetsxxx {
                    planets {
                      name
                      climates
                      terrains
                      importantPeople: residentConnection {
                        people: residents {
                          name
                          gender
                        }
                      }
                    }
                  }
                }
                """;

        var body = new com.arturola.graphql.client.request.GraphqlRequest();

        body.setQuery(query);


        assertThrows(RuntimeException.class, () -> {
            var response = starwarsRestClient.asJson(body, null);
        });
    }


    @Test
    void allPlanets() throws Exception {

        var planets = starwarsRestClient.allPlanets();

        assertNotNull(planets);
        assertFalse(planets.isEmpty());
    }

    @Test
    void getOneStarship() throws Exception {

        var starshipResponse = starwarsRestClient.getOneStarshipFixed();

        assertNotNull(starshipResponse);
        assertNotNull(starshipResponse.getModel());
    }

    @Test
    void testOneFilm_Right() throws Exception {
        var result = starwarsRestClient.onFilm("1");
        assertNotNull(result);
        assertNotNull(result.getTitle());
    }

    @Test
    void testOneFilm_Invalid() throws Exception {
        var errors = starwarsRestClient.oneInvalidFilmId();

        assertNotNull(errors);
        assertFalse(errors.isEmpty());
    }

}