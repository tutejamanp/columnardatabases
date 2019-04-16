package org.dm.columnar;

import javax.ws.rs.Path;

/**
 * Used to Store Sub Query
 * Example :-
 * "UserID" , "<", "20"
 * "Name" , "=" , "Tushar"
 */
@Path("/QueryContainerController")
public class QueryContainer {
    public String columnName;
    public String operation;
    public String value;

    public QueryContainer(String name, String op, String val) {
        columnName = name;
        operation = op;
        value = val;
    }
}
