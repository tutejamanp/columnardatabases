package org.dm.columnar;

import java.io.*;
import java.util.*;

import javax.ws.rs.Path;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

@Path("/TableManagerController")
public class TableManager implements Serializable{
    //We Store name of column, with the instance of column manager for that column
	private static final long serialVersionUID = 2L;
    public LinkedHashMap<String, ColumnManager> columnManagers = new LinkedHashMap<String, ColumnManager>();
    public int rowCount;
    public String tableName;
 

	public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Create A Empty Table Manager Class
     * @param tableName
     */
    public TableManager(String tableName){
        this.tableName = tableName;
        this.rowCount = 0;
        System.out.println("Table Created with Name : "+tableName);
    }

    /**
     * Adds Columns To The Table
     * @param columnName name of column to be added
     * @param columnInfo all info about the column to be added.
     * @return
     */
    public boolean addColumnInfo(String columnName, ColumnManager columnInfo) {
        //Storing the Column Name with It's Manager Object
        columnManagers.put(columnName, new ColumnManager(columnInfo.dataType, columnInfo.elementSize, columnInfo.serializeSize, columnInfo.columnName));
        System.out.println("Data Inserted Successfully");
        System.out.println(toString());
        return true;
    }

    /**
     * Inserting Data Into Columns
     * @param data Hash Map of Column Name, Column Value
     */
    public void insert(HashMap<String, String> data) {

        //Check if proper keys are given
        Set inputKeys = data.keySet();
        System.out.println(inputKeys);
        System.out.println(columnManagers.keySet());
        Set requiredKeys = columnManagers.keySet();

        //If both key sets have same value then the keys are valid
        if(!inputKeys.equals(requiredKeys)) {
            System.out.println("Invalid Column Names Given");
            return;
        }

        //Iterate through each column.
        for (Map.Entry<String, String> entry : data.entrySet()) {
            //Insert Data in respective Column
            ColumnManager column = columnManagers.get(entry.getKey());
            column.insert(entry.getValue());
        }
    }

    
    public void bulkinsert(HashMap<Integer, List<String>> mapdata, List<String> columnnames) {        
        
    	System.out.println("for check sake : "+mapdata.keySet());
    	
    	for (Integer i : mapdata.keySet()) {
        	ColumnManager column = columnManagers.get(columnnames.get(i));
        	 column.bulkinsert(mapdata.get(i));
        }     
    }
 
    /**
     * Selecting all column Ids of for a particular query of operations given
     * @param operations
     * @return Array of IDs
     */
    public Integer[] select(ArrayList<OperationPair> operations ) {
        //Stores current Result Set
        Set<Integer> currentResult = new HashSet<Integer>();
        int operationCount = 0;
        String operation = "";

        //For each operation pair calculate the intersection or the union of the sets nlogn
        
        System.out.println("size of the operations thus created is :"+operations.size());
        
        for(OperationPair currentOp : operations) {
            ColumnManager currentColumn = columnManagers.get(currentOp.getQuery().columnName);

            //Set of All Selected Columns
            
            Set<Integer> tempResult = new HashSet<>(currentColumn.conditionalSelect(currentOp.getQuery().value, currentOp.getQuery().operation));
            
            System.out.println("value is : "+currentOp.getQuery().value);
            System.out.println("value is : "+currentOp.getQuery().operation);
            
            System.out.println("Temp Result Table Manager" +tempResult);
            if(operationCount == 0) {
                currentResult.addAll(tempResult);
                operation = currentOp.nextOperation;
                operationCount++;
            } else if(operation.equals("AND")){
            	
                currentResult.retainAll(tempResult);
                operation = currentOp.nextOperation;
            
            } else if(operation.equals("OR")){
                currentResult.addAll(tempResult);
                operation = currentOp.nextOperation;
            } else {
                System.out.println(currentResult);
                //Returning current result if there's no next operation
                return currentResult.toArray(new Integer[currentResult.size()]);

            }
            System.out.println(currentResult);
        }
        System.out.println("Final Result IS :");
        System.out.println(currentResult);
        return currentResult.toArray(new Integer[currentResult.size()]);
    }
    
    
    
    
    

    /**
     * Retrives the rows containing the values.
     * @param selectedIds
     * @param columnNames
     */
    public Map<String,List<String>> getSelectedValues(int[] selectedIds, ArrayList<String> columnNames) {
        Arrays.sort(selectedIds);
        //Check if proper keys are given
        Set<String> requiredKeys = columnManagers.keySet();
        System.out.println(requiredKeys);

        HashMap<String, List<String>> columnValues = new HashMap<String, List<String>>();
        //Get List Of Column Names
        for(String columnName : columnNames) {
            ColumnManager column = columnManagers.get(columnName);

            //Store the values in temp Map
            System.out.println(columnName + "------" + selectedIds.toString());
            columnValues.put(columnName, column.getColumnValues(selectedIds));
        }
        System.out.println("Selected Column Values Are :");
        System.out.println(columnValues);
    
        return columnValues;
    }

    
    public Map<String,List<String>> getAllSelectedValues(ArrayList<String> columnNames) {
      
    	Set<String> requiredKeys = columnManagers.keySet();
        System.out.println(requiredKeys);

       Map<String, List<String>> columnValues = new HashMap<String, List<String>>();
        //Get List Of Column Names
        for(String columnName : columnNames) {
            ColumnManager column = columnManagers.get(columnName);
            columnValues.put(columnName, column.getAllColumnValues());
        }
        System.out.println("Selected Column Values Are :");
        System.out.println(columnValues);
        return columnValues;
        
    }
    
    

    public static void serializer(TableManager obj, String columnFileName) throws IOException {

        //Setting Filename as "Column Name" + "Total No. Of Files"
        String fileName = columnFileName;

        //Serializing The File
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream("C:\\Users\\Manpreet\\eclipse-workspace\\columnar\\" + fileName);
            FSTObjectOutput out = new FSTObjectOutput(file);

            // Method for serialization of object
            out.writeObject(obj);

            out.close();
            file.close();

            //Adding the filename to list of filenames.
            //System.out.println("Object has been serialized");
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
    public static TableManager deserializer(String filename) {
    	System.out.println(filename + " ");
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("C:\\Users\\Manpreet\\eclipse-workspace\\columnar\\" + filename);
            FSTObjectInput in = new FSTObjectInput(file);

            TableManager obj = null;
            // Method for deserialization of object
            obj = (TableManager) in.readObject();

            in.close();
            file.close();

        //    System.out.println("Object has been deserialized ");
            System.out.println(obj.toString());
            return obj;
        }

        catch(IOException ex)
        {
            System.out.println("helo IOException is caught" + ex.toString());
            return null;
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            return null;
        }
    }


    @Override
    public String toString() {
        return "TableManager{" +
                "columnManagers=" + columnManagers +
                ", rowCount=" + rowCount +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
