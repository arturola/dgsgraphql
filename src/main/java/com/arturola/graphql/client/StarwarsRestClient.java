package com.arturola.graphql.client;

import com.arturola.graphql.client.request.GraphqlRequest;
import com.arturola.graphql.client.response.FilmResponse;
import com.arturola.graphql.client.response.GraphqlErrorResponse;
import com.arturola.graphql.client.response.PlanetResponse;
import com.arturola.graphql.client.response.StarshipResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.dgs.codegen.generated.types.Planet;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
@Component
public class StarwarsRestClient {
    private static final String BASE_URL = "https://swapi-graphql.netlify.app/.netlify/functions/index";

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper(); // para convertir de JSON a POJO


    public String asJson(GraphqlRequest body, Map<String, List<String>> headersMap) {

        var requestHeaders = new org.springframework.http.HttpHeaders();

        if(headersMap != null) {
            headersMap.forEach(requestHeaders::addAll);
        }

        var responseEntity = restTemplate.postForEntity(
                BASE_URL,
                new org.springframework.http.HttpEntity<>(body, requestHeaders), String.class);

        return responseEntity.getBody();
    }

    public List<PlanetResponse> allPlanets() throws JsonProcessingException {
        var query = """
                query allPlanets {
                  allPlanets {
                    planets {
                      name
                      climates
                      terrains
                    }
                  }
                }
                """;

        var body = new GraphqlRequest();

        body.setQuery(query);

        var response = asJson(body, null);

        var jsonNode =objectMapper.readTree(response);

        var data = jsonNode.at("/data/allPlanets/planets");

       return objectMapper.readValue(data.toString(),
               new TypeReference<List<PlanetResponse>>() {}
               //objectMapper.getTypeFactory().constructCollectionType(List.class, Planet.class)
            );
    }

    public StarshipResponse getOneStarshipFixed() throws JsonProcessingException {
        var query = """
                query oneStarshipFixed {
                  starship(id: "c3RhcnNoaXBzOjU=") {
                    model
                    name
                    manufacturers
                  }
                }                
                """;
        var body = new GraphqlRequest();

        body.setQuery(query);

        var response = asJson(body, null);
        var jsonNode =objectMapper.readTree(response);
        var data = jsonNode.at("/data/starship");

        return objectMapper.readValue(data.toString(), StarshipResponse.class);
    }

    public FilmResponse onFilm(String filmId) throws JsonProcessingException {
        var query = """
                query oneFilm($filmId: ID!) {
                  film(filmID: $filmId) {
                    title                
                    director
                    producers
                    releaseDate  
                  }
                }
                """;

        var body = new GraphqlRequest();
        body.setQuery(query);

        var variables = Map.of("filmId", filmId);
        body.setVariables(variables);

        var response = asJson(body, null);
        var jsonNode =objectMapper.readTree(response);
        var data = jsonNode.at("/data/film");

        return objectMapper.readValue(data.toString(), FilmResponse.class);
    }

     public List<GraphqlErrorResponse> oneInvalidFilmId() throws JsonProcessingException {
         var query = """
                query oneFilm($filmId: ID!) {
                  film(filmID: $filmId) {
                    title                
                    director
                    producers
                    releaseDate  
                  }
                }
                """;

         var body = new GraphqlRequest();
         body.setQuery(query);

         var variables = Map.of("filmId", "xxxxx");
         body.setVariables(variables);

         var response = asJson(body, null);
         var jsonNode =objectMapper.readTree(response);
         var errors = jsonNode.at("/errors");

         if(errors != null) {
             return objectMapper.readValue(errors.toString(),
                     new TypeReference<List<GraphqlErrorResponse>>() {}
             );
         }

         return null;
     }
}
