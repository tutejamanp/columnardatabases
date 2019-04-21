package org.dm.columnar;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;
import javax.ws.rs.Path;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

@Path("/ColumnManagerController")
public class ColumnManager implements Serializable{
	private static final long serialVersionUID = 3L;
    //Total Number Of Elements Present In The Column
    private int elementCount;
    //List Of All Serialized Object File Names
    ArrayList<String> fileNames = new ArrayList<String>();
    //Data Type of Element
    String dataType;
    //Size Of Each Element
    int elementSize;
    //Serialized Object Size Limit
    int serializeSize;
    //Name Of The Column
    String columnName;
    //Latest File Structure Index
    int fileIndex;
    
    static int calltimer=0;
    static int selectcalltimer=0;
    
    

    public ColumnManager(String dataType, int elementSize, int serializeSize, String name) {
        elementCount = 0;
        this.dataType = dataType;
        this.elementSize = elementSize;
        this.serializeSize = serializeSize;
        columnName = name;
        fileIndex = 0;
    }

    /**
     * Serializes A Column Store Instance
     * @param obj instance of column Serialzied Object
     * @return true if successful, false if not.
     * @throws IOException
     */
    public boolean serializer(ColumnSerialized obj, String columnFileName) throws IOException {

        //Setting Filename as "Column Name" + "Total No. Of Files"
        String fileName =  columnFileName;

        //Serializing The File
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(fileName);
            FSTObjectOutput out = new FSTObjectOutput(file);

            // Method for serialization of object
            out.writeObject(obj);

            out.close();
            file.close();

            //Adding the filename to list of filenames.
            System.out.println("Object has been serialized");
            System.out.println(obj.toString());
            return true;
        } catch (IOException ex) {
            System.out.println("Error Has Occured During Serialization");
            return false;
        }
    }

    /**
     * De-Serializes The Given Column instance
     * @param filename name of the file to be de-serialized
     * @return persisted object, if false "null"
     */
    public ColumnSerialized deserializer(String filename) {
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            FSTObjectInput in = new FSTObjectInput(file);

            ColumnSerialized obj = null;
            // Method for deserialization of object
            obj = (ColumnSerialized) in.readObject();

            in.close();
            file.close();

            
            System.out.println(++calltimer);
            
            //System.out.println("hi look at this : manp3");
            //System.out.println("Object has been deserialized lodu ");
            //System.out.println(obj.toString());
            return obj;
        }

        catch(IOException ex)
        {
            System.out.println("deserialise IOException is caught");
            ex.printStackTrace();
            return null;
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            return null;
        }
    }

    /**
     * Insert a new Element To Column
     * @param input A String input
     */
    public void insert(String input){
        boolean createNewFile = true;
        ColumnSerialized column = null;
        String selectedFileName = "";

        //Check which file has empty space and select that file.
        System.out.println(fileNames.toString());
        
        if(fileNames.size()!=0) {
        String file = fileNames.get(fileNames.size()-1);
        System.out.println("file:" + file);
        column = deserializer(file);
        if(column.getElementCount() < serializeSize) {
            selectedFileName = file;
            createNewFile = false;
        }
        }
        
        //if no file is empty, create a new file and Store Data
        if(createNewFile) {
            System.out.println("Creating New File");
            column = new ColumnSerialized(serializeSize);
            selectedFileName ="C:\\Users\\Manpreet\\eclipse-workspace\\columnar\\" + columnName+fileNames.size()+".ser";

        }
        //insert into the file.
        column.insert(input);
        elementCount++;

        //serialize the file
        try {
            serializer(column, selectedFileName);
            //Adding the new file to file list if it's created
            if(createNewFile){
                fileNames.add(selectedFileName);
                System.out.println(fileNames.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void bulkinsert(List<String> listinputs){
        boolean createNewFile = true;
        ColumnSerialized column = null;
        String selectedFileName = "";
        System.out.println("Creating New File");
        column = new ColumnSerialized(serializeSize);
        selectedFileName ="C:\\Users\\Manpreet\\eclipse-workspace\\columnar\\" + columnName+fileNames.size()+".ser";

        
        //insert into the file.
        column.insertbulk(listinputs);
        elementCount++;

        //serialize the file
        try {
            serializer(column, selectedFileName);
            //Adding the new file to file list if it's created
            if(createNewFile){
                fileNames.add(selectedFileName);
                System.out.println(fileNames.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    /**
     * Selecting list of required elements
     * @param input String value to be searched for
     * @param count number of elements to search
     * @return ArrayList Of Found Values.
     */
    public ArrayList<Integer> select(String input, int count) {
    	
    	
    	
    	
        ArrayList<Integer> index = new ArrayList<Integer>();
        int countSoFar = 0;
        int countOfSerialized = 0;
        if(count == 0) {
            count = Integer.MAX_VALUE;
        }

        //Search File By File
        for(String file: fileNames) {
            ColumnSerialized column = deserializer(file);

            //Array Of Selected Elements
            int[] store = column.select(input);
            if(store != null) {
                //Store all the returned elements with appropriate index
                for(int item : store) {
                    index.add(item + serializeSize*countOfSerialized);
                }
            }
            //Incrementing countOfSerialized to show that we've moved to next file
            countOfSerialized++;
        }
        //Check If Input Matches
        if(index.isEmpty()) {
            System.out.println("Result Not Found.");
        } else {
            System.out.println("Result Found");
            System.out.println(index);
        }
        //Return Index Of Item
        return index;

    }

    /**
     * Selecting through entire column for given operation
     * @param input string to be searched for
     * @param operation operation to be performed ( supports <, <=, >, >=, =, !=)
     * @return Array list of selected IDs
     */
    public ArrayList<Integer> conditionalSelect(String input, String operation) {
    	
    	System.out.println("mere khud k :"+ ++selectcalltimer);
    	
        ArrayList<Integer> index = new ArrayList<Integer>();
        int countSoFar = 0;
        int countOfSerialized = 0;

        //Search File By File
        for(String file: fileNames) {
        	
        	System.out.println("hi there see this call manp1:");
        	
            ColumnSerialized column = deserializer(file);
 
            System.out.println("hi there see this call manp2:");
            
            //Array Of Selected Elements
            Integer[] store = column.conditionalSelect(input, operation);
            if(store != null) {
                //Store all the returned elements with appropriate index
                for(int item : store) {
                    index.add(item + serializeSize*countOfSerialized);
                }
            }
            //Incrementing countOfSerialized to show that we've moved to next file
            countOfSerialized++;
        }
        //Check If Input Matches
        if(index.isEmpty()) {
            System.out.println("Result Not Found.");
        } else {
            System.out.println("Result Found");
            System.out.println("Column Name : "+columnName);
            System.out.println(index);
        }
        //Return Index Of Item
        return index;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getElementSize() {
        return elementSize;
    }

    public void setElementSize(int elementSize) {
        this.elementSize = elementSize;
    }

    public int getSerializeSize() {
        return serializeSize;
    }

    public void setSerializeSize(int serializeSize) {
        this.serializeSize = serializeSize;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
         * Get an Array list of values corresponding to ids provided.
         * @param ids ids of elements to be selected
         * @return
         */
    public ArrayList<String> getColumnValues(int[] ids) {
        //just to check if the answers are still correct
    	//Arrays.sort(ids);
        ArrayList<String> values = new ArrayList<String>();

        //If no ids are selected, then we return null.
        if(ids == null) {
            return null;
        }
        int size = ids.length;
        int countSoFar = 0; //How many items have you visited so far
        int countOfSerialized = 0; //Which file are you on
        //Search File By File
        for(String file: fileNames) {
            //shifted it above as it was unneccesary to call it under while opeartion
        	ColumnSerialized column = deserializer(file); 
        	
            //Checking if the data at given index actually belongs to the current file
            while(ids[countSoFar] < (countOfSerialized + 1)*serializeSize) {
                //Get the selected column
                
                String value = column.getSelectedValues(ids[countSoFar] - countOfSerialized*serializeSize);
                if(value != null) {
                    values.add(value);
                }
                countSoFar++;
                if (countSoFar == size) {
                    System.out.println("Count So Far" + countSoFar+"size "+size);
                    break;
                }
            }
            if (countSoFar == size) {
                System.out.println("Count So Far" + countSoFar+"size "+size);
                break;
            }

            //Moving to next file
            countOfSerialized++;
        }
        System.out.println("Selected Elements Are");
        System.out.println(values.toString());
        return values;
    }
    
    
    
    public ArrayList<String> getAllColumnValues() {

    	ArrayList<String> values = new ArrayList<String>();
       
        //Search File By File
        for(String file: fileNames) {
            System.out.println("name of file is :"+file);
        	ColumnSerialized column = deserializer(file);
        	System.out.println("file to be checked is : "+file);
        	values.addAll(column.getData());
        }
        
        System.out.println("Selected Elements Are");
        System.out.println(values.toString());
        
        return values;
    }
    
    
    

    /**
     * Update data of given ids
     * @param id Array of ids to be updated
     * @param input input string where you want to make an update
     * @return true if element is found, false if not
     */
    public boolean update(int []id, String input) {
        //Get Count Of All Elements
        int countOfSerialized = 0;
        Arrays.sort(id);
        //Open File
        int currentIndex = 0;
        boolean completed = false;
        //Iterate through files, To find the IDs.
        for(String file : fileNames) {
            ColumnSerialized column = deserializer(file);
            System.out.println(file);
            //Check if any changes have been made to the file
            boolean fileSelected = false;

            //While Current Index Lies Within The File
            while (id[currentIndex] < (countOfSerialized + 1)*serializeSize) {
                column.update(id[currentIndex] - (countOfSerialized*serializeSize),input);
                fileSelected = true;
                currentIndex++;
                //Get Out Loop If We've Found All Elements
                if((id.length) == currentIndex) {
                    System.out.println("Successfully Updated");
                    completed = true;
                    break;
                }

            }
            //Moving on to next file
            countOfSerialized++;
            //If file is selected, Serialize the object
            if(fileSelected) {
                try {
                    serializer(column, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(completed) return true;

        }

        //Update The File At that Relative Index
        return false;
    }

    /**
     * Deleted The Selected Elements Provided With ID.
     * @param id Array of IDs to be deleted
     * @return true if deleted, false if not.
     */
    public boolean delete(int[] id) {
        //Get Count Of All Elements
        int countOfSerialized = 0;
        Arrays.sort(id);
        //Open File
        int currentIndex = 0;
        boolean completed = false;
        //Iterate through files, To find the IDs.
        for(String file : fileNames) {
            ColumnSerialized column = deserializer(file);
            System.out.println(file);
            //Check if any changes have been made to the file
            boolean fileSelected = false;
            int elementsDeleted = 0;
            //While Current Index Lies Within The File
            while (id[currentIndex] < (countOfSerialized + 1)*serializeSize) {
                column.delete(id[currentIndex] - (countOfSerialized*serializeSize + elementsDeleted));
                elementCount--;
                elementsDeleted++;
                fileSelected = true;
                currentIndex++;
                //Get Out Loop If We've Found All Elements
                if((id.length) == currentIndex) {
                    System.out.println("Successfully Deleted Selected Elements");
                    completed = true;
                    break;
                }

            }
            //Moving on to next file
            countOfSerialized++;
            //If file is selected, Serialize the object
            if(fileSelected) {
                try {
                    serializer(column, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(completed) return true;

        }

        //Update The File At that Relative Index
        return false;
    }

    @Override
    public String toString() {
        return "ColumnManager{" +
                "elementCount=" + elementCount +
                ", fileNames=" + fileNames +
                ", dataType='" + dataType + '\'' +
                ", elementSize=" + elementSize +
                ", serializeSize=" + serializeSize +
                ", columnName='" + columnName + '\'' +
                ", fileIndex=" + fileIndex +
                '}';
    }

    /**
     * Testing
     * @param args
     */
}
