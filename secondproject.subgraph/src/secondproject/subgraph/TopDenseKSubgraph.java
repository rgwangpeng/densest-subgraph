package secondproject.subgraph;

/*
 * 先求 densest subgraph,再求top k densest subgraph.先求密度子图，再从密度子图中求解前k大
 * 测试程序，测试需要进行7步：
 * 1. 转换文件格式，对文件进行改写，是为了符合.net文件风格，开始节点从1开始
 * 2. 得出与读入文件对应的 graphMap
 * 3. 得出与 graphMap 对应的文件的 .net文件，是为了计算给定的初始图的社团个数
 * 
 * 4. 对graphMap应用高密度子图算法，得出密度子图 resultMap
 * 5. 得出与 resultMap 对应的文件的 .net文件，是为了计算图中所包含的子社团个数
 * 6. 使用 infomap 算法计算 得出社团结构，对每个社团结构计算其密度，得出的所有密度与之前的社团密度进行比较。
 * 7. 得出前k个密度子图
 * 
 * 因为需要用到 infomap算法，此算法需要输入数据文件格式为.net,因此需要进行处理。
 * .net文件要求有： 节点必须按照顺序存储，且节点开始序号为1，不得有间断； 边没有条件限制，对称非对称都行
 * 
 * TestSub主要用到两个类： PreProcessFile RemainDensestSubgraph 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TopDenseKSubgraph {
	public static void main(String[] args) throws IOException {
		
		long startTime = System.currentTimeMillis();
		String graphPath = "C:\\Users\\wp\\Desktop\\dense\\as-735.txt";
		PreProcessFile ppf = new PreProcessFile();
		HashMap<Integer,HashSet<Integer>> graphMap = ppf.symUndirGraph(graphPath);
		
		// 3.得出与 graphMap 对应的文件的 .net文件
		String writePath = "C:\\Users\\wp\\Desktop\\dense\\as-735.net";
		ppf.writeNetFile(graphMap, writePath);
		
		// 4. 对graphMap应用高密度子图算法，得出密度子图 resultMap
		HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.withRemainLastDelete(graphMap); // 保留最后一个密度子图
//		HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.withRemainDelete(graphMap);  //保留倒数第二密度子图
		System.out.println("resultMap.size: " + resultMap.size() );
		
		// 5. 将convertMap -- resultMap写进文件，因为不能使用程序调用infomap，因此采用先写进文件，再读文件的方式。
		String resultMapPath = "C:\\Users\\wp\\Desktop\\dense\\result.txt";
		ppf.writeResultMap(resultMapPath, resultMap);
		
		long runTime = System.currentTimeMillis() - startTime;
		
		System.out.println("run time: " + runTime);
		System.exit(0);
//		// 6. 转换resultMap格式
		HashMap<Integer,HashSet<Integer>> convertMap = ppf.convertResultMap(resultMap);
		
		// 7. 得出与 convertMap -- resultMap 对应的文件的 .net文件
		String wnetPath = "/home/hadoop/denseSubgraph/test3/graphmap/resultEmail-Enron.net";
		ppf.writeNetFile(convertMap, wnetPath);
		
		// 8. infomap划分社团，求各社团密度。
		CommunityStructure.detectCommunity(wnetPath);

		// 9.读取生成的.map文件，得出原图的社团。
		String mapfilePath = "/home/hadoop/denseSubgraph/test3/graphmap/resultEmail-Enron.map";
		KDensest kd = new KDensest(); // 这个类是对得出的社团计算其密度的。
		HashMap<Integer, ArrayList<Integer>> graphCommunity = new HashMap<Integer, ArrayList<Integer>>(); // 键是社团编号，值是社团中的元素。
		graphCommunity = kd.getSubCommunity(mapfilePath);
		
		// 10. 根据社团得出每个社团相应的图.
		HashMap<Integer, HashMap<Integer, HashSet<Integer>>> mapToCommunity = new HashMap<Integer, HashMap<Integer, HashSet<Integer>>>();
		mapToCommunity = kd.getCommunityGraph(graphCommunity, convertMap);

		// 11. 对每个社团求其相应的密度子图，并得出相应的密度,然后将密度排序，得出前K大密度
		ArrayList<Double> densityList = MapDensity.getcomMapDensity(mapToCommunity);
		System.out.println("densityList: " + densityList);
		
//		long runTime = System.currentTimeMillis() - startTime;
		
		System.out.println("run time: " + runTime);
		
	}
}
