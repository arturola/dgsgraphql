package com.arturola.graphql.client.request;

import java.util.Map;

public class GraphqlRequest {

    private String query;
    private Map<String, ? extends  Object> variables;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, ? extends Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, ? extends Object> variables) {
        this.variables = variables;
    }
}
/*
*
*
*   private Map<String, Object> variables;
*
*   VS
*
*   private Map<String, ? extends  Object> variables;
*
*
* If you need to put values into the map, you should use Map<String, Object>.
* If you only need to get values from the map and you don't care about the specific type of the values,
* you can use Map<String, ? extends Object>.
*
*
* */