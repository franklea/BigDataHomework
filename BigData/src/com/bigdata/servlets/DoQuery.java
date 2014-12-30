package com.bigdata.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DoQuery
 */
public class DoQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DoQuery() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	int xmlParsed = 0;
	int poiParsed = 0;
	String POI1 = "";
	String POI2 = "";
	String POI3 = "";
	String[][] resPoi = null;
	ArrayList<Data> dataList = new ArrayList<Data>();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("in do get");
		if (poiParsed == 0) {
			String poi = "F:\\git\\BigData\\data.txt";
			Data data = new Data();
			dataList = data.readFromFile(poi);
			poiParsed = 1;
		}
		if (xmlParsed == 0) {
			String poiFileName = "F:\\git\\BigData\\POIYP.xls";
			// Type data stored in array resPoi;
			XlsParse parser = new XlsParse();
			try {
				resPoi = parser.readXls(poiFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			xmlParsed = 1;
			System.out.println("xmlFile parsed!");

		}

		String queryType = request.getParameter("type");
		String wclass = new String(request.getParameter("value").getBytes(
				"ISO-8859-1"), "GBK");
		String action = new String(request.getParameter("act"));
		String parm = new String(request.getParameter("parm"));

		// System.out.println(queryType+" "+wclass);
		ArrayList<Data> useful = new ArrayList<Data>();
		StringBuffer sb = new StringBuffer("<wclass>");
		
		if (queryType.equals("L")) {
			for (int i = 0; i < resPoi.length; i++) {
				if (resPoi[i][0].equals(wclass)) {
					int index = 0;
					String tmp = resPoi[i][0];
					POI1 = resPoi[i][5].substring(0, 2);
					POI2="";
					POI3="";
					
					String tmp2 = "";
					while (resPoi[i + index][0].equals(tmp)) {
						if (resPoi[i + index][2].equals(tmp2)) {
							index++;
							continue;
						}
						sb.append("<tclass>" + resPoi[i + index][2]
								+ "</tclass>");
						tmp2 = resPoi[i + index][2];
						index++;
					}
					break;
				}
			}
		} else if (queryType.equals("M")) {
			// TODO
			for (int i = 0; i < resPoi.length; i++) {
				if (resPoi[i][2].equals(wclass)) {
					POI2 = resPoi[i][1];
					POI3="";
					sb.append("<tclass>" + resPoi[i][4] + "</tclass>");
				}
			}
		} else if (queryType.equals("S")) {
			for (int i = 0; i < resPoi.length; i++) {
				if (resPoi[i][4].equals(wclass)) {
					POI3 = resPoi[i][3];
				}
			}
		}
		
		//System.out.println("poi:" + POI1 + POI2 + POI3);
		if (!action.equals("xxx")) {
			// get useful data
			System.out.println("poi:" + POI1 + POI2 + POI3);
			if (!POI3.equals("")) {
				System.out.println("poi3");
				for (Data d : dataList) {
					if (d.poi.equals(POI1+POI2+POI3)) {
						useful.add(d);
						d.printData();
					}
				}
			} else if (POI3.equals("") && !POI2.equals("")) {
				System.out.println("poi2");
				for (Data d : dataList) {
					if (d.poi.substring(0, 4).equals(POI1 + POI2)) {
						useful.add(d);
					}
				}

			} else if (POI3.equals("") && POI2.equals("") && !POI1.equals("")) {
				System.out.println("poi1");
				for (Data d : dataList) {
					if (d.poi.substring(0, 2).equals(POI1)) {
						useful.add(d);
					}
				}
			}
			System.out.println("getData");
			
			for (Data d : useful) {
				System.out.println("================");
				d.printData();
				System.out.println("================");
			}
			
			if (useful.isEmpty()){
				sb.append("<name>нч╫А╧Ш</name>");
				sb.append("</wclass>");
				System.out.println(sb);
				response.setContentType("text/xml;charset=utf-8");
				// response. setCharacterEncoding("GB2312");
				// response.setHeader("Content-Type", "text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.write(sb.toString());
				out.flush();
				out.close();
			}
			// buildTree
			SimpleKdTree kdTree = new SimpleKdTree();
			KdNode root = new KdNode();
			root = kdTree.buildTree(useful);
			if (action.equals("NNQ")){
				System.out.println("do NNQ");
				KdNode nearest = new KdNode();
				nearest = kdTree.NNQ(root, 121.429, 31.031);
				sb.append("<locate>" + nearest.data.x + "</locate>");
				sb.append("<locate>" + nearest.data.y + "</locate>");
				sb.append("<name>" + nearest.data.name + "</name>");
			}else if(action.equals("SRQ")){
				//TODO
				System.out.println("do SRQ");
				
				System.out.println("do SRQ");
				ArrayList<KdNode> res = new ArrayList<KdNode>();
				double range = Double.parseDouble(parm);
				System.out.println("range:"+range);
				res = kdTree.SRQ(root,121.429,31.031,range);
				for (KdNode d : res){
					d.data.printData();
					sb.append("<locate>" + d.data.x + "</locate>");
					sb.append("<locate>" + d.data.y + "</locate>");
					double dis = kdTree.getDistance(d.data.x, d.data.y, 121.429, 31.031);
					System.out.println("dis: "+dis);
				}
				
			}
			
		}

		sb.append("</wclass>");
		System.out.println(sb);
		response.setContentType("text/xml;charset=utf-8");
		// response. setCharacterEncoding("GB2312");
		// response.setHeader("Content-Type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(sb.toString());
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
