package secondproject.subgraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestReadGraph {
	
	public static void main(String[] args) throws IOException {
		String filePath = new String("/home/hadoop/桌面/012");
		HashMap<Integer, HashSet<Integer>> graphMap = symUndirGraph(filePath);
		 withRemainDelete(graphMap);
		
	}
	
	// 只需要使用一个graphMap就能存储所有信息
	public static HashMap<Integer, HashSet<Integer>> getsUndirGraph(String filePath)
			throws IOException {
		HashMap<Integer, HashSet<Integer>> graphMap = new HashMap<Integer, HashSet<Integer>>();

		File srcFile = new File(filePath);
		System.out.println("file length: " + srcFile.length());
		BufferedReader readBuff = new BufferedReader(new FileReader(srcFile));

		int edgeCount = 0;

		String str = new String("");
		while ((str = readBuff.readLine()) != null) {
			edgeCount++;

			String[] strArr = str.split(" |\t");
			int firstVertex = Integer.parseInt(strArr[0]);
			int secondVertex = Integer.parseInt(strArr[1]);

			if ( graphMap.keySet().contains(firstVertex) ){
				// 图的键集合值包含第一个元素，分两步进行： 1.将第二个元素加入第一个元素的邻居节点集合中；
				graphMap.get(firstVertex).add(secondVertex);

			}else {
				// 图的键集合值均不包含两个元素，因此处理方式是：
				// 1.将第一个元素加入图的键集合，而且将第二个元素加入第二个元素的邻居节点集合中
				HashSet<Integer> snerborSet = new HashSet<Integer>();
				snerborSet.add(secondVertex);
				graphMap.put(firstVertex, snerborSet);
			}
		}

		System.out.println("graph.edge: edgeCount: " + edgeCount);

		int vertexNum = graphMap.size();
		int edgeNum = getEdgeCount(graphMap);
		System.out.println("graph -> vertex: " + vertexNum
				+ "\tgraph -> edge: " + edgeNum);
		return graphMap;
	}
	
	
	
	
	
	// 得出给定图的边数
	public static int getEdgeCount(HashMap<Integer, HashSet<Integer>> graphMap) {
		int edgeCount = 0;
		Set<Integer> keySet = graphMap.keySet();

		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			int key = (Integer) it.next();
			HashSet<Integer> valueSet = graphMap.get(key);
			edgeCount += valueSet.size();
		}

		return edgeCount;
	}
	
	// 将 resultMap写进文件中，为了下一步恢复resultMap做准备
		// * public void writeResultMap(String writeMapPath, HashMap<Integer,HashSet<Integer>> resultMap)
		public static void writeResultMap(String writeMapPath, HashMap<Integer,HashSet<Integer>> resultMap) throws IOException{
			File file = new File(writeMapPath);
			Set<Integer> keySet = resultMap.keySet();
			
			BufferedWriter buffW = new BufferedWriter(new FileWriter(file));
			Iterator firIt = keySet.iterator();
			
			while(firIt.hasNext()){
				String str = new String("");
				int key = (Integer)firIt.next();
				
				HashSet<Integer> hashSet = resultMap.get(key);
				Iterator secIt = hashSet.iterator();
				while(secIt.hasNext()){
					int value = (Integer) secIt.next();
					str += value + " ";
				}
				
				str = key + " " + str ;
				buffW.write(str);
				buffW.newLine();
			}
			
		}
		
		// 只需要使用一个graphMap就能存储所有信息，适用于 节点对重复的无向图symmetric
		public static HashMap<Integer, HashSet<Integer>> symUndirGraph(String filePath)
				throws IOException {
			HashMap<Integer, HashSet<Integer>> graphMap = new HashMap<Integer, HashSet<Integer>>();

			File srcFile = new File(filePath);
			System.out.println("file path: " + filePath + " ,length: " + srcFile.length());
			BufferedReader readBuff = new BufferedReader(new FileReader(srcFile));

			int edgeCount = 0;

			String str = new String("");
			while ((str = readBuff.readLine()) != null) {
				edgeCount++;

				String[] strArr = str.split(" |\t");
				int firstVertex = Integer.parseInt(strArr[0]);
				int secondVertex = Integer.parseInt(strArr[1]);

				if ((graphMap.keySet().contains(firstVertex))
						&& (!(graphMap.keySet().contains(secondVertex)))) {
					// 图的键集合值包含第一个元素，分两步进行： 1.将第二个元素加入第一个元素的邻居节点集合中；
					// 2.将第二个元素加入图的键集合，而且将第一个元素加入第二个元素的邻居节点集合中
					// 处理第一个元素，firstVertex
					graphMap.get(firstVertex).add(secondVertex);
					// 处理第二个元素，secondVertex
					HashSet<Integer> nerborSet = new HashSet<Integer>();
					nerborSet.add(firstVertex);
					graphMap.put(secondVertex, nerborSet);

				} else if (graphMap.keySet().contains(secondVertex)
						&& (!(graphMap.keySet().contains(firstVertex)))) {
					// 图的键集合值包含第二个元素，分两步进行： 1.将第一个元素加入第二个元素的邻居节点集合中；
					// 2.将第一个元素加入图的键集合，而且将第二个元素加入第二个元素的邻居节点集合中
					// 处理第二个元素，secondVertex
					graphMap.get(secondVertex).add(firstVertex);
					// 处理第一个元素，firstVertex
					HashSet<Integer> nerborSet = new HashSet<Integer>();
					nerborSet.add(secondVertex);
					graphMap.put(firstVertex, nerborSet);
				} else if (graphMap.keySet().contains(secondVertex)
						&& (graphMap.keySet().contains(firstVertex))) {
					// 图的键集合值包含有两个元素，因此处理方式是：
					// 1.将第二个元素加入第一个元素的邻居节点集合中；2.将第一个元素加入第二个元素的邻居节点集合中；
					// 处理第一个元素，firstVertex
					graphMap.get(firstVertex).add(secondVertex);
					// 处理第二个元素，secondVertex
					graphMap.get(secondVertex).add(firstVertex);
				} else {
					// 图的键集合值均不包含两个元素，因此处理方式是：
					// 1.将第二个元素加入图的键集合，而且将第一个元素加入第二个元素的邻居节点集合中；
					// 2.将第一个元素加入图的键集合，而且将第二个元素加入第二个元素的邻居节点集合中
					// 处理第一个元素，firstVertex
					HashSet<Integer> fnerborSet = new HashSet<Integer>();
					fnerborSet.add(firstVertex);
					graphMap.put(secondVertex, fnerborSet);
					// 处理第二个元素，secondVertex
					HashSet<Integer> snerborSet = new HashSet<Integer>();
					snerborSet.add(secondVertex);
					graphMap.put(firstVertex, snerborSet);
				}
			}
			readBuff.close();
			System.out.println("graph.edge: edgeCount: " + edgeCount);

			int vertexNum = graphMap.size();
			int edgeNum = getEdgeCount(graphMap);
			System.out.println("graph -> vertex: " + vertexNum
					+ "\tgraph -> edge: " + edgeNum);
			return graphMap;
		}
		
		
		
		
		
		public static void withRemainDelete(HashMap<Integer,HashSet<Integer>> graphMap){
				//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中
				List<Integer> deleteVerList = new ArrayList<Integer>();
				deleteVerList.add(1);
				deleteVerList.add(2);
				deleteVerList.add(3);
//				System.out.println( " deleteVe.size() : " + deleteVe.size() +  " \n deleteVe : " + deleteVe  );
				
				//这一步 功能 将deleteVerList 中的节点从图中移除
				//分两步，1. 从该节点的 邻居节点的 邻居数组集合中移除 该节点； 2. 从图中移除该节点，也就是 vertexMap
				for( int j = 0; j < deleteVerList.size(); j ++){
					List<Integer> tempL = new ArrayList<Integer>();
					tempL.addAll( graphMap.get( deleteVerList.get(j) ) );
					
					//System.out.println("j = " + j +  "  deleteVerL.get(j): " + deleteVerL.get(j) + " tempL :" + tempL + "\n + tempL.size():" + tempL.size());
					//1.满足上边条件的节点，其邻居节点肯定也和它有连边，移除该节点邻居节点连接到此节点的边，也就是从它邻居节点的 邻居队列里移除该节点
					for(int k = 0; k < tempL.size(); k ++){
						graphMap.get( tempL.get(k) ).remove( deleteVerList.get(j) );  
					}
					//2. vertexMap 移除该节点
					graphMap.remove( deleteVerList.get(j) );
				}
				//计算 经过删除度数小的节点后的子图的密度
				System.out.println(" graphMap.size(): " + graphMap.size() );
				System.out.println("graphMap: " + graphMap);
				
		}
	
}
