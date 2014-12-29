package com.bigdata.servlets;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author lea
 *
 */
public class Test {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "data.txt";
		String poiFileName = "POIYP.xls";
		
		//Type data stored in array resPoi;
		XlsParse parser = new XlsParse();
		String[][] resPoi = null;
		try {
			resPoi = parser.readXls(poiFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		parser.printRes(resPoi);
		
		//poi data stored in list res;
		ArrayList<Data> dataList = new ArrayList<Data>();
		Data data = new Data();
		dataList = data.readFromFile(fileName);

// test buildTree		
		SimpleKdTree kdTree = new SimpleKdTree();
		int first_dim = kdTree.firstDim(dataList);
		KdNode root = new KdNode();
		root = kdTree.buildTree(dataList);
		System.out.println(root.data.x+" "+root.data.y);

		
// test NNQ
//		KdNode nearest = new KdNode();
//		nearest = kdTree.NNQ(root, 2, 4.5);
//		nearest.data.printData();
		
	}
}
