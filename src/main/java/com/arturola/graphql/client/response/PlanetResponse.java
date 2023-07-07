package com.arturola.graphql.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanetResponse {
    private String name;
    private List<String> climates;
    private List<String> terrains;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getClimates() {
        return climates;
    }

    public void setClimates(List<String> climates) {
        this.climates = climates;
    }

    public List<String> getTerrains() {
        return terrains;
    }

    public void setTerrains(List<String> terrains) {
        this.terrains = terrains;
    }
}
