package com.bigdata.servlets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author lea
 *
 */
public class Data {
	String name;
	String poi;
	double x;
	double y;
	
	Data(){};
	
	Data(String name, String poi, double x, double y){
		this.name = name;
		this.poi = poi;
		this.x = x;
		this.y = y;
	}
	

	
	public ArrayList<Data> readFromFile(String fileName){
		ArrayList<Data> dataArray = new ArrayList<Data>();
		File file = new File(fileName);
        BufferedReader reader = null;
        String name = null;
        
        try {
            reader = new BufferedReader(new FileReader(file));
            String item = null;
            
            while ((item = reader.readLine()) != null) {
            	//System.out.println(item);
            	String[] dataString = item.split(",");
            	String item_name = dataString[1];
            	String item_poi = dataString[9];
            	double item_x_coord = Double.parseDouble(dataString[14]);
            	double item_y_coord = Double.parseDouble(dataString[15]);
            	Data data = new Data(item_name,item_poi,item_x_coord,item_y_coord);	
            	dataArray.add(data);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return dataArray;
	}

	
	public void printData(){
		System.out.println(name);
		System.out.println(poi);
		System.out.println(x+" "+y);
	}
	
}
