package com.bigdata.servlets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 * Implemention of a 2-d KdTree
 * 
 * @author lea
 *
 */

public class SimpleKdTree {	
	
	KdNode root;


	public int firstDim(ArrayList<Data> list) {
		// x_avg;
		double x_avg = 0;
		for (Data d : list) {
			x_avg += d.x / list.size();
		}
		// x_var
		double x_var = 0;
		for (Data d : list) {
			x_var += ((d.x - x_avg) * (d.x - x_avg)) / list.size();
		}

		// y_avg;
		double y_avg = 0;
		for (Data d : list) {
			y_avg += d.y / list.size();
		}
		// y_var
		double y_var = 0;
		for (Data d : list) {
			y_var += ((d.y - y_avg) * (d.y - y_avg)) / list.size();
		}
		
		return x_var >= y_var?0:1;
	}

	Comparator<Data> comparator_x = new Comparator<Data>(){
		public int compare(Data d1, Data d2){
			if (d1.x >= d2.x){
				return 1;
			}else{
				return 0;
			}
		}
	};
	
	Comparator<Data> comparator_y = new Comparator<Data>(){
		public int compare(Data d1, Data d2){
			if (d1.y >= d2.y){
				return 1;
			}else{
				return 0;
			}
		}
	};
	
	
	public ArrayList<Data> sortList(ArrayList<Data> list, int dim){
		if (dim == 0 /*x_dim*/){
			Collections.sort(list, comparator_x);
		}else if (dim == 1 /*y_dim*/){
			Collections.sort(list, comparator_y);
		}
		return list;
	}
	
	public KdNode buildTree(ArrayList<Data> list/*, int first_dim*/) {
		int first_dim = firstDim(list);
		
		if (list.isEmpty()){
			return null;
		}
		
		KdNode root = new KdNode();
		if(list.size() == 1){
			root.data = list.get(0);
			root.dim = first_dim;
			return root;
		}
		
		ArrayList<Data> sortedList = sortList(list,first_dim);
		//printList(sortedList);
		int median = sortedList.size()/2;
		root.data = sortedList.get(median);
		root.dim = first_dim;
		
		ArrayList<Data> left = new ArrayList();
		ArrayList<Data> right = new ArrayList();
		for (int i = 0; i< median; i++)
		{
			left.add(sortedList.get(i));
		}
		for(int j = median+1; j< sortedList.size(); j++){
			right.add(sortedList.get(j));
		}
		
		root.left = buildTree(left);
		root.right = buildTree(right);
		return root;
	}


	public KdNode findNode(KdNode root ,double x, double y) {
		//dfs
		if (root.data.x == x && root.data.y == y){
			root.data.printData();
			return root;
		}else{
			findNode(root.left,x,y);
			findNode(root.right,x,y);
		}
		System.out.println("Not Found");
		return null;
	}

	/*
	 * Nearedt Neighbour Query
	 */
	public KdNode NNQ(KdNode root, double x, double y) {
		double dis = 0;
		int currentDim = 0;
		KdNode nearest = new KdNode();
		Stack<KdNode> path = new Stack<KdNode>();
		
		if (root == null){
			System.out.println("Tree is empty!");
			return null;
		}
		
		nearest = root;
		while(root != null){
			path.push(root);
			currentDim = root.dim;
			if (currentDim == 0){
				if (x <= root.data.x){
					root = root.left;
				}else{
					root = root.right;
				}
			}else if(currentDim == 1){
				if (y <= root.data.y){
					root = root.left;
				}else{
					root = root.right;
				}
			}
		}
		
		nearest = path.pop();	
	//	nearest.data.printData();
		double tmpMinDis = distance(nearest,x,y);
		double minDis = 0;
	//	System.out.println(Max_dis);
		
		//backtrack
		KdNode backNode = new KdNode();
		while (!path.isEmpty()){
			backNode = path.pop();
			currentDim = backNode.dim;
			if (distance(backNode,x,y) < tmpMinDis){
				if(currentDim == 0){
					if (x <= backNode.data.x){
						root = backNode.right;
					}else {
						root = backNode.left;
					}
				}else if (currentDim == 1){
					if (y <= backNode.data.y){
						root = backNode.right;
					}else{
						root = backNode.left;
					}
				}
				path.push(root);
			
				if(distance(nearest,x,y) > distance(root,x,y)){
					nearest = root;
					tmpMinDis = distance(root,x,y);
				}
			}
			
		}
		
		return nearest;
	}

	/*
	 * Special Range Query
	 */
	double EARTH_RADIUS = 6378137;

	private double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public double getDistance(double lng1, double lat1, double lng2, double lat2) {
		//to meters
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public ArrayList<KdNode> SRQ(KdNode root, double x, double y ,double range) {
		ArrayList<KdNode> res = new ArrayList<KdNode>();
		
		Stack<KdNode> node = new Stack<KdNode>();
		node.push(root);
		
		while(node.size() != 0){
			KdNode tmpNode = node.pop();
			double dis = getDistance(tmpNode.data.x,tmpNode.data.y,x,y);
			System.out.println("dis: "+dis);
			if (dis <= range){
				res.add(tmpNode);
			}
			
			if (tmpNode.left != null){
				node.push(tmpNode.left);
			}
			if (tmpNode.right != null){
				node.push(tmpNode.right);
			}
		}
		return res;
	}
	
	Stack<KdNode> node = new Stack<KdNode>();
	public void getNode(KdNode root){
		if (root != null){
			node.push(root);
			getNode(root.left);
			getNode(root.right);
		}
	}
	
	
	/*
	 * use Euclidean distance
	 */
	public double distance(KdNode node1,double x, double y){
		double dis = 0;
		dis = Math.sqrt(Math.pow(node1.data.x-x,2)+Math.pow(node1.data.y-y, 2));
		return dis;
	}
	
	public void printList(ArrayList<Data> list){
		for (Data d:list){
			d.printData();
		}
	}
	
	public void printKdTree(KdNode root){
		if (root != null){
			root.data.printData();
			System.out.println(root.dim);
			System.out.println("=================");
			printKdTree(root.left);
			printKdTree(root.right);
		}
	}
}
