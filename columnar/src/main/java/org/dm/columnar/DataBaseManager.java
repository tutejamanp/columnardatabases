package org.dm.columnar;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/DataBaseManagerController")
public class DataBaseManager implements Serializable{
    //An Array List Of Tables
    ArrayList<String> tableNames = new ArrayList<String>();
    public String databaseName;
    private static final long serialVersionUID = 1L;

    public DataBaseManager(String name) {
        databaseName = name;
    }

    public DataBaseManager() {
    }
    
    public TableManager loadTable(String tableName) {
        return TableManager.deserializer(tableName);
    }

    public static void serializer(DataBaseManager obj, String dataBaseName) throws IOException {


        String fileName = dataBaseName;

        //Serializing The File
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream("C:\\Users\\Manpreet\\eclipse-workspace\\columnar\\" + fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(obj);

            out.close();
            file.close();

            //Adding the filename to list of filenames.
            System.out.println("Object has been serialized");
            System.out.println(obj.toString());

        } catch (IOException ex) {

            System.out.println("Error Has Occured During Serialization");
        }
    }


    /**
     * De-Serializes The Given Column instance
     * @param filename name of the file to be de-serialized
     * @return persisted object, if false "null"
     */
    public static DataBaseManager deserializer(String filename) {
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("C:\\Users\\Manpreet\\eclipse-workspace\\columnar\\" + filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            DataBaseManager obj = (DataBaseManager) in.readObject();

            in.close();
            file.close();

            System.out.println("Data Base has been deserialized ");
            System.out.println(obj.toString());
            return obj;
        }

        catch(IOException ex)
        {
            ex.printStackTrace();
            System.out.println("IOException is caught");
            return new DataBaseManager("CollegeManagement.ser");
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            return new DataBaseManager("CollegeManagement.ser");
        }
        
        catch(Exception ex)
        {
            System.out.println("Empty database");
            return new DataBaseManager("CollegeManagement.ser");
        }
        
    }


	@POST 
	@Path("/createtable")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
    public static Response createtable(@FormDataParam("tableName") String tableName, @FormDataParam("colcount")  int columnCount, @FormDataParam("sersize") int serializationSize, 
    		@FormDataParam("coldatatype") String listdata,
    		@FormDataParam("colsize")  String listsizer,
    		 @FormDataParam("colname") String columnnames ) 
    		throws IOException {

		List<String> listdatatype = new ArrayList<String> ();
		
		for (String s : listdata.split(",")) {
			if(!s.isEmpty())
			listdatatype.add(s.trim());
		}
		
       List<Integer> listsize = new ArrayList<Integer> ();
		
		for (String s : listsizer.split(",")) {
			if(!s.isEmpty())
			listsize.add(Integer.parseInt(s.trim()));
		}
	
		   List<String> listcolumnnames = new ArrayList<String> ();
			
			for (String s : columnnames.split(",")) {
				if(!s.isEmpty())
				listcolumnnames.add(s.trim());
			}
		
        //Creating The Table
		try {
        System.out.println(tableName + " " + columnCount + " " + serializationSize);
        for(String s: listdatatype)
        	System.out.println(s);
        for(int i : listsize)
        	System.out.println(i + " ");
        for(String s: listcolumnnames)
        	System.out.println(s);
        
        
		String DataBaseName = "CollegeManagement.ser";
        //Object has been serialized previous and2 stored. Only one Database for now.
        DataBaseManager dbManager = deserializer(DataBaseName);

        TableManager table = new TableManager(tableName);

        List<String[]> columnInfo = new ArrayList<String[]>();

        //Insert Information Of Each Column

        for (int i = 0; i < columnCount; i++) {

            String dataType = listdatatype.get(i);
            int elementSize = listsize.get(i);
            String columnName = listcolumnnames.get(i);
            table.addColumnInfo(columnName,new ColumnManager(dataType, elementSize, serializationSize, columnName));
        }

        TableManager.serializer(table, tableName+".ser");
        //Here we've info of the table, we then attached this table to a particular DB
   
        dbManager.tableNames.add(tableName+".ser");
        serializer(dbManager, DataBaseName);
        
        
        return Response.status(404).entity("Table Created").build();
		
		}
		
		catch (Exception e) {
		return Response.status(200).entity("Error in Table Creation").build();
		}
        
    }

	
	 @Path("/getalltables")
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 public static Response showtables () throws IOException {
		 String DataBaseName = "CollegeManagement.ser";
		 
		 GenericEntity<List<String>> tables;
		 DataBaseManager dbManager = deserializer(DataBaseName);
			
		 tables  = new GenericEntity<List<String>>(dbManager.tableNames) { };
		 return Response.ok(tables).build();
		 
		 
	    
	     //return Response.ok(dbManager.tableNames).build();
	}
	
	
	 @Path("/getallcolumns/{tablename}")
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 public static Response getcolumns (@PathParam("tablename") String tablename) throws IOException {
		 TableManager tableManager = TableManager.deserializer(tablename + ".ser");
		 GenericEntity<Set<String>> columns;
		 columns  = new GenericEntity<Set<String>>(tableManager.columnManagers.keySet()) { };
		 return Response.ok(columns).build();
	     //return Response.ok(dbManager.tableNames).build();
	}
	 
	 @Path("/getallcolumndatatypes/{tablename}")
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 public static Response getcolumnsdatatypes (@PathParam("tablename") String tablename) throws IOException {
		 TableManager tableManager = TableManager.deserializer(tablename + ".ser");
		 GenericEntity<HashMap<String,ColumnManager>> columns;
		 columns  = new GenericEntity<HashMap<String,ColumnManager>>(tableManager.columnManagers) { };
		 return Response.ok(columns).build();
	     //return Response.ok(dbManager.tableNames).build();
	}
	 
	 
	 
	 
	 
	
	@POST 
	@Path("/insertintotable/{tablename}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	
    public static void insertintotable (@PathParam("tablename") String loadTable, @FormDataParam("valuelist") String valuelist) throws IOException {

		System.out.println(loadTable + " " + valuelist);
        String DataBaseName = "CollegeManagement.ser";
        //Object has been serialized previous and2 stored. Only one Database for now.
        DataBaseManager dbManager = deserializer(DataBaseName);
        
        List<String> listinp = new ArrayList<String> ();
		
		for (String s : valuelist.split(",")) {
			if(!s.isEmpty())
				listinp.add(s.trim());
		}
	
        
        
        
        HashMap<String, String> insertData = new HashMap<String, String>();
        TableManager tableManager = TableManager.deserializer(loadTable + ".ser");
        int i=0 ;
        for(String keyNames : tableManager.columnManagers.keySet()) {
            System.out.println(keyNames + " :");
            String inputData = listinp.get(i);
            System.out.println("input data:" + inputData);
            insertData.put(keyNames, inputData);
        i++;
        }
        tableManager.insert(insertData);
        //Storing the info of the table
        TableManager.serializer(tableManager, loadTable+".ser");
        System.out.println(dbManager);

    }


	@POST 
	@Path("/seachintotable/{tablename}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
    public static Response searchintable (@PathParam("tablename") String loadTable,@FormDataParam("selectedcols")  String selectedcols,  @FormDataParam("noOps") int noOps, @FormDataParam("columns") String columns,
    		@FormDataParam("values") String values, @FormDataParam("comparators") String comparators, @FormDataParam("operations") String operations) throws IOException {
        ArrayList<OperationPair> operationPairs = new ArrayList<OperationPair>();
        
       String listcolumnName [] = columns.split(",");
       String listValue [] = values.split(",");
       String listComparator [] = comparators.split(",");
       String listOperation [] = operations.split(",");
       String listcolumnstodisplay [] = selectedcols.split(",");
       
       System.out.println(loadTable + " " + noOps + " " +listcolumnName.toString() + " " + listValue.toString() + " " + listComparator.toString() + " " + listOperation.toString());
        
       
       System.out.println("loadtable is :"+loadTable);
       System.out.println("nOps is :"+noOps);
       System.out.println("columns are :"+columns);
       System.out.println("comparators are :"+comparators);
       System.out.println("values are :"+values);
       System.out.println("operations are :"+operations);
       
       
       ArrayList<String> columnli = new ArrayList<String>();
       
       
       for (String col : listcolumnstodisplay ){
    	   columnli.add(col);
       }
       
       
        for(int i = 1; i < noOps; i++) {
        	
            //System.out.println("Enter Column :");
            String columnName = listcolumnName[i];
            //System.out.println("Enter Value :");
            String value = listValue[i];
            //System.out.println("Enter Comparator :");
            String operation = listComparator[i];
            //System.out.println("Next Operation");
            String nextOp = listOperation[i];

            QueryContainer queryPart = new QueryContainer(columnName,operation,value);
            OperationPair ops = new OperationPair(queryPart,nextOp);
            operationPairs.add(ops);
            
        }
        
        
        TableManager selectTable = TableManager.deserializer(loadTable + ".ser");
        Integer[] idf = selectTable.select(operationPairs);
        int selectedidf []  = new int [idf.length]; 
        int k =0 ; 
        
        for (Integer i : idf) {
        	selectedidf [k] = i;
        	k++;
        }
     
        System.out.println("selected values are : ");
        for (Integer i : idf) {
        	System.out.print(i+" --- ");
        }
        System.out.println();
        
        
        TableManager.serializer(selectTable, loadTable+ ".ser");
        return Response.ok(selectTable.getSelectedValues(selectedidf,columnli)).build();
    }
	
    
     @Path("/getallvalues/{tablename}")
     @POST 
	 @Produces(MediaType.APPLICATION_JSON)
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public static Response getvalues (@PathParam("tablename") String loadTable, @FormDataParam("collist") String collist) throws IOException {
    	 
    	 System.out.println("-----" + loadTable + " " + collist);
    	 ArrayList<String> columnNames = new ArrayList<String> ();
 		
 		for (String s : collist.split(",")) {
 			if(!s.isEmpty())
 				columnNames.add(s.trim());
 		}
    	 TableManager getTableValues = TableManager.deserializer(loadTable + ".ser");
         GenericEntity<Map<String,List<String>>> columns;
		 columns  = new GenericEntity<Map<String,List<String>>>( getTableValues.getAllSelectedValues(columnNames)) { };
		 return Response.ok(columns).build();

     }
    
    
  


}
