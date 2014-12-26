package com.bigdata.servlets;

public class Rectangle {
	double x1;
	double y1;
	double x2;
	double y2;
	Rectangle(double x1, double y1, double x2, double y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	//(x,y) is in rec ?
	boolean isInRec(Rectangle rec, double x, double y){
		if ((x1 <= x && x <= x2)&&(y1 <= y && y <= y2)){
			return true;
		}else{
			return false;
		}	
	}
	
	//rec1 is in rec2 ?
	boolean recInRec(Rectangle rec1, Rectangle rec2){
		if (isInRec(rec2,rec1.x1,rec1.y1) && isInRec(rec2,rec1.x2,rec1.y2)){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	
}
