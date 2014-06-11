package secondproject.subgraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import secondPac.*;
/*
* 1. 转换文件格式，对文件进行改写，是为了符合.net文件风格，开始节点从1开始
* 2. 得出与读入文件对应的 graphMap
* 3. 得出与 graphMap 对应的文件的 .net文件，是为了计算给定的初始图的社团个数
* 
* 4. 对graphMap应用高密度子图算法，得出密度子图 resultMap
*/
public class MainTest {
	public static void main(String[] args) throws IOException {
		optimalDenseSubG();
//		long startTime = System.currentTimeMillis();
//		String graphPath = "D:\\densestSubgraph\\论文所用到的数据集\\roadNet\\roadNet.txt";
////		String postProfilePath = graphPath.substring(0, graphPath.length() - 4) + "post.txt";
//////		//转换文件格式
//		PreProcessFile ppf = new PreProcessFile();
////		ppf.changeFile(graphPath, postProfilePath);
////		HashMap<Integer,HashSet<Integer>> graphMap = ppf.symUndirGraph(postProfilePath); //根据输入文件得出图文件
//		
//		HashMap<Integer,HashSet<Integer>> graphMap = ppf.symUndirGraph(graphPath); //根据输入文件得出图文件，第一行就从边开始
//		HashMap<Integer,HashSet<Integer>> copygraphMap = new HashMap<Integer,HashSet<Integer>>(); // 复制图
//		copyGraph(graphMap, copygraphMap);  // 前者是srcMap， 后者是dstMap
//		System.out.println("graphMap.size: " + graphMap.size() + "\tcopygraphMap.size: " + copygraphMap.size());
//		
//		// 3.得出与 graphMap 对应的文件的 .net文件
////		String writePath = "C:\\Users\\wp\\Desktop\\dense\\as-735.net";
////		ppf.writeNetFile(graphMap, writePath);
//		
//		// 4. 对graphMap应用高密度子图算法，得出密度子图 resultMap
////		HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.myWithoutRemainDelete(graphMap);
//		
//		// 对应原始方法，如果需要检测，还是使用这个方法
//		int K = 0;
//		while( (graphMap.size() != 0) && (K < 1)){
//			K = K+1;
//			System.out.println("Graph " + K );
////			HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.myWithoutRemainDelete(copygraphMap);
//			HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.withoutRemainDelete(copygraphMap);
//
//			//			System.out.println("testing copygraphMap: " + copygraphMap.size());
//			//写进文件
//			String resultMapPath = graphPath.substring(0, graphPath.length() - 4) + "result" + K + ".txt";
//			
//			System.out.println("write finish!");
//			
//			printScore(resultMap,resultMapPath);
//			
//			deleteSubmap(graphMap,resultMap);
//			
////			System.out.println("graphMap.szie:" + graphMap.size());
//			copyGraph(graphMap, copygraphMap);
////			System.out.println("graphMap.szie:" + graphMap.size() + "\tcopygraphMap.szie:　" + copygraphMap.size());
//			System.out.println();
//		}
//		
//		long endTime = System.currentTimeMillis();
//		long runTime = endTime - startTime;
//		
//		System.out.println("runTime: " + runTime);
////		HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.withRemainLastDelete(graphMap); // 保留最后一个密度子图
////		HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.withRemainDelete(graphMap);  //保留倒数第二密度子图
//		
//		
//		// 5. 将resultMap写进文件，为了计算直径方便。
////		System.exit(0);
	}
	
	
//	public static void Score(HashMap<Integer,HashSet<Integer>> graphMap){
//		
//	}
	
	
	public static void optimalDenseSubG() throws IOException {

		long startTime = System.currentTimeMillis();
		String graphPath = "D:\\200\\shard0as-skitter.txt";
		PreProcessFile ppf = new PreProcessFile();

		HashMap<Integer, HashSet<Integer>> graphMap = ppf.symUndirGraph(graphPath); // 根据输入文件得出图文件，第一行就从边开始

		HashMap<Integer, HashSet<Integer>> resultMap = RemainDensestSubgraph.withoutRemainDelete(graphMap);
//		HashMap<Integer, HashSet<Integer>> resultMap = RemainDensestSubgraph.secwithoutRemainDelete(graphMap);
		String resultMapPath = graphPath.substring(0, graphPath.length() - 4)
				+ "result" + ".txt";

		System.out.println("write finish!");

		printScore(resultMap, resultMapPath);
		System.out.println();

		long endTime = System.currentTimeMillis();
		long runTime = endTime - startTime;

		System.out.println("runTime: " + runTime);
	}
	
	public static void topKDenseSubG() throws IOException{

		long startTime = System.currentTimeMillis();
		String graphPath = "D:\\densestSubgraph\\论文所用到的数据集\\roadNet\\roadNet.txt";
//		String postProfilePath = graphPath.substring(0, graphPath.length() - 4) + "post.txt";
////		//转换文件格式
		PreProcessFile ppf = new PreProcessFile();
//		ppf.changeFile(graphPath, postProfilePath);
//		HashMap<Integer,HashSet<Integer>> graphMap = ppf.symUndirGraph(postProfilePath); //根据输入文件得出图文件
		
		HashMap<Integer,HashSet<Integer>> graphMap = ppf.symUndirGraph(graphPath); //根据输入文件得出图文件，第一行就从边开始
		HashMap<Integer,HashSet<Integer>> copygraphMap = new HashMap<Integer,HashSet<Integer>>(); // 复制图
		copyGraph(graphMap, copygraphMap);  // 前者是srcMap， 后者是dstMap
		System.out.println("graphMap.size: " + graphMap.size() + "\tcopygraphMap.size: " + copygraphMap.size());
		
		// 3.得出与 graphMap 对应的文件的 .net文件
//		String writePath = "C:\\Users\\wp\\Desktop\\dense\\as-735.net";
//		ppf.writeNetFile(graphMap, writePath);
		
		// 4. 对graphMap应用高密度子图算法，得出密度子图 resultMap
//		HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.myWithoutRemainDelete(graphMap);
		
		// 对应原始方法，如果需要检测，还是使用这个方法
		int K = 0;
		while( (graphMap.size() != 0) && (K < 1)){
			K = K+1;
			System.out.println("Graph " + K );
//			HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.myWithoutRemainDelete(copygraphMap);
			HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.withoutRemainDelete(copygraphMap);

			//			System.out.println("testing copygraphMap: " + copygraphMap.size());
			//写进文件
			String resultMapPath = graphPath.substring(0, graphPath.length() - 4) + "result" + K + ".txt";
			
			System.out.println("write finish!");
			
			printScore(resultMap,resultMapPath);
			
			deleteSubmap(graphMap,resultMap);
			
//			System.out.println("graphMap.szie:" + graphMap.size());
			copyGraph(graphMap, copygraphMap);
//			System.out.println("graphMap.szie:" + graphMap.size() + "\tcopygraphMap.szie:　" + copygraphMap.size());
			System.out.println();
		}
		
		long endTime = System.currentTimeMillis();
		long runTime = endTime - startTime;
		
		System.out.println("runTime: " + runTime);
	}
	
	
	public static void copyGraph(HashMap<Integer,HashSet<Integer>> srcMap, HashMap<Integer,HashSet<Integer>> dstMap){
		dstMap.clear();
		Set<Integer> srcKeyset = srcMap.keySet();
		Iterator srcit = srcKeyset.iterator();
		while(srcit.hasNext()){
			Integer value = (Integer) srcit.next();
			
			HashSet<Integer> tmpSet = srcMap.get(value);
			HashSet<Integer> dstValueset = new HashSet<Integer>();
			for(int item : tmpSet){
				dstValueset.add(item);
			}
			dstMap.put(value, dstValueset);
		}
	}
	
	public static void deleteSubmap(HashMap<Integer,HashSet<Integer>> srcMap,HashMap<Integer,HashSet<Integer>> subMap){
		Set<Integer> subKeyset = subMap.keySet();
		
		Iterator subit = subKeyset.iterator();
		while(subit.hasNext()){
			Integer subkey = (Integer) subit.next();
			List<Integer> tempL = new ArrayList<Integer>();
			if(srcMap.get(subkey).size() == 0){
				continue;
			}
			tempL.addAll( srcMap.get(subkey) );
			for(int k = 0; k < tempL.size(); k ++){
				//1.满足上边条件的节点，其邻居节点肯定也和它有连边，移除该节点邻居节点连接到此节点的边，也就是从它邻居节点的 邻居队列里移除该节点
					srcMap.get( tempL.get(k) ).remove( subkey );  
				//2. vertexMap 移除该节点
					srcMap.remove( subkey );
			}
		}
	}
	
	public static void printScore(HashMap<Integer,HashSet<Integer>> resultMap,String resultMapPath) throws IOException{
		PreProcessFile ppf = new PreProcessFile();
		ppf.writeDiaResultMap(resultMapPath, resultMap);
		
		Set<Integer> rmkeySet = resultMap.keySet();
		int vertexSize = rmkeySet.size();
		
		int inducedEdges = RemainDensestSubgraph.getEdgeCount(resultMap);
		TriangleCounting tc = new TriangleCounting();
		System.out.println("start count triganle");
		
		long startT = System.currentTimeMillis();
		int triNum = tc.countgMapTriangles(resultMap);
		System.out.println("triNum: " + triNum);
		long runT = System.currentTimeMillis() - startT;
		System.out.println("count triganle spend time: " + runT);
		
		// fe: 边密度，	fd:平均度数	ft:三角数比 
		double fd = (double) inducedEdges / vertexSize;
		double fe = (double) inducedEdges * 2 / (vertexSize * (vertexSize - 1));
		
		System.out.println("count ft, triNum: " + triNum + " ,\tvertexSize: " + vertexSize);
		double ft = (double) triNum * 6 / ( vertexSize * (vertexSize - 1) * (vertexSize - 2) );
		System.out.println("fd: " + fd + " ,\t fe: " + fe + " ,\tft: " + ft);
		
//		System.out.println("fd: " + fd + " ,\t fe: " + fe);
	}
	
}
