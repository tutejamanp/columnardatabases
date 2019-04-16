package org.dm.columnar;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;

@Path("/TestingController")
public class Testing {

    // Generic function to convert an Array to Set

    public static void main(String[] args){
        Integer[] array = {5,6,7,8};
        Integer[] array2 = {5,7, 11, 12};
        Set<Integer>   set = new HashSet<>(Arrays.asList(array));
        Set<Integer>   set2 = new HashSet<>(Arrays.asList(array2));
        set.addAll(set2);
        System.out.println(set);
    }
}
