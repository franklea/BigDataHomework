package com.bigdata.servlets;
class KdNode {
		public KdNode left;
		public KdNode right;
		public Data data;
		public int dim;

		public KdNode(){
			this.data = null;
			this.left = null;
			this.right = null;
			this.dim = -1;
		}
		
		public KdNode(Data data) {
			this.data = data;
			this.left = null;
			this.right= null;
			this.dim = -1;
		}
	}