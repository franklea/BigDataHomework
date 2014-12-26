package com.bigdata.servlets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHandle {
	 private int LEN;
	 private int WID;
	 private int X;
	 private int Y;
	 
	 FileHandle(int len, int wid, int x, int y){
		 this.LEN = len;
		 this.WID = wid;
		 this.X = x;
		 this.Y = y;
	 }
	
    public double[][] readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        double[][] data = new double[LEN][WID];
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int index = 0;
            while ((tempString = reader.readLine()) != null) {
            	String[] dataString = tempString.split(",");
            	if (index < LEN)
            	{
            		data[index][0] = Double.parseDouble(dataString[X]);
            		data[index][1] = Double.parseDouble(dataString[Y]);
            		index++;
            	}
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
		return data;
    }
}
