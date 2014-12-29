package com.bigdata.servlets;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class XlsParse {

	String[][] readXls(String fileName) throws IOException{
		InputStream is = new FileInputStream( fileName);  
		String[][] poiRes = new String[667][6];
	    @SuppressWarnings("resource")
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook( is);   
	      
	    for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++){  
	      HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);  
	      if(hssfSheet == null){  
	        continue;  
	      }
	      
	      for(int rowNum = 0; rowNum < 667/*hssfSheet.getLastRowNum()*/; rowNum++){  
	        HSSFRow hssfRow = hssfSheet.getRow( rowNum);  
	        if(hssfRow == null){  
	          continue;  
	        }  
	          
	        for(int cellNum = 0; cellNum < hssfRow.getLastCellNum(); cellNum++){  
	          HSSFCell hssfCell = hssfRow.getCell( cellNum);  
	          if(hssfCell == null){
	            continue;  
	          }
	          
	          if (getValue( hssfCell) == ""){
	        	  int offset = 1;
	        	  while(getValue(hssfSheet.getRow(rowNum-offset).getCell(cellNum)) == "")
	        	  {
	        		  offset ++;
	        		 
	        	  }
	        	  //System.out.print(getValue(hssfSheet.getRow(rowNum-offset).getCell(cellNum))+"    ");
	        	  poiRes[rowNum][cellNum] = getValue(hssfSheet.getRow(rowNum-offset).getCell(cellNum)).toString();
	          }else{
	          
	        	 // System.out.print(getValue( hssfCell)+"    "); 
	        	  poiRes[rowNum][cellNum] = getValue( hssfCell).toString();
	          }
	          
	        }
	        
	        //System.out.println("\n--------------------");  
	      }  
	    }
	    return poiRes;
	}
	
    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
        	return String.valueOf(hssfCell.getStringCellValue());
        }
    }
    
    void printRes(String[][] res){
    	for (int i=0; i< res.length; i++){
    		for (int j =0; j< 6; j++){
    			System.out.println(res[i][j]);
    		}
    		System.out.print("------------------\n");
    	}
    }

}
