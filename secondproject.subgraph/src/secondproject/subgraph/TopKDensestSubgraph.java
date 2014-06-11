package secondproject.subgraph;

/*
 * 先进行社团划分，接着对每个社团求最大密度子图。先community structure, 再 densest subgraph
 * 将图文件 放在  ～/denseSubgraph 中，然后就需要处理
 * 测试程序，测试需要进行7步：
 * 1. 转换文件格式，对文件进行改写，是为了符合.net文件风格，开始节点从1开始
 * 2. 得出与读入文件对应的 graphMap
 * 3. 得出与 graphMap 对应的文件的 .net文件，是为了计算给定的初始图的社团个数
 * 4. 对每个社团，列举其密度子图，加入到这样的数据结构中：
 *  HashMap(Integer, HashMap<Integer,ArrayList<Integer>>),其中HashMap(Integer, HashMap)其中integer表示的是社团号，HashMap存储的
 * （1）将得到的密度子图加入fibnacci堆中，
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
import java.util.TreeMap;

public class TopKDensestSubgraph {
	public static void main(String[] args) throws IOException {

		long startTime = System.currentTimeMillis();
		String fileDir = "/home/hadoop/denseSubgraph/test1/";
		// 修改文件目录时，只需要修改 以下两个文件的名字。
		String originalDir = "originalFile/";
		String originalPath = "Email-Enron.txt";
		
//		String graphPath = "/home/hadoop/denseSubgraph/originalFile/Email-Enron.txt";
		// 源图文件存放位置
		String graphPath = fileDir + originalDir + originalPath;
		System.out.println("graphPath: " + graphPath);
		// String graphPath = "/home/hadoop/桌面/012";
		String convertPath = "pre" + originalPath;
//		String postProfilePath = "/home/hadoop/denseSubgraph/originalFile/preEmail-Enron.txt";
		
		//转换之后文件文件存放位置
		String postProfilePath = fileDir + originalDir + convertPath;
		System.out.println("postProfilePath: " + postProfilePath);
		PreProcessFile ppf = new PreProcessFile();
		
		// 1.转换文件格式
		ppf.changeFile(graphPath, postProfilePath);

		long firstTime = System.currentTimeMillis();
		long changeFT = firstTime - startTime;
		System.out.println(" change file time: " + changeFT);

		// 2.得出与读入文件对应的 graphMap
		HashMap<Integer, HashSet<Integer>> graphMap = ppf.symUndirGraph(postProfilePath);

		long secondTime = System.currentTimeMillis();
		long getGraphT = secondTime - firstTime;
		System.out.println(" get graph time: " + getGraphT);

		// 3.得出与 graphMap 对应的文件的 .net文件
		// 这个转换是针对文件后缀为.txt格式的，如果不是这种格式，还需要再写
		String graphDir = "graphmap/";
		String netgraphFile = originalPath.substring(0, (originalPath.length() - 4)) + ".net";
		// .net文件存放位置
		String writePath = fileDir + graphDir + netgraphFile;
		
		System.out.println("writePath: " + writePath);
		ppf.writeNetFile(graphMap, writePath);

		long thirdTime = System.currentTimeMillis();
		long writeTime = thirdTime - secondTime;
		System.out.println("write graphmap to .net file time: " + writeTime);
		
		// 4. 使用infomap算法得出社团结构
		// 这步，处理.net文件，将会得出.map文件，map文件中存放的是划分好的社团结果，对与越大的数据集，想必其得出的社团数量越多。
		CommunityStructure.detectCommunity(writePath);
		
		long forthTime = System.currentTimeMillis();
		long commST = forthTime - thirdTime;
		System.out.println("use infomap algorithm, get community structure time: " + commST);
		
		// 5.读取生成的.map文件，得出原图的社团。
		String mapfilePath = fileDir + graphDir + originalPath.substring(0, (originalPath.length() - 4)) + ".map";
		System.out.println("mapfilePath: " + mapfilePath);
		KDensest kd = new KDensest();  // 这个类是对得出的社团计算其密度的。
		HashMap<Integer,ArrayList<Integer>> graphCommunity = new HashMap<Integer,ArrayList<Integer>>(); // 键是社团编号，值是社团中的元素。
		graphCommunity = kd.getSubCommunity(mapfilePath);
		
		long fifthTime = System.currentTimeMillis();
		long graphCTime = fifthTime - forthTime;
		System.out.println("use map file genarate graphCommunity time: " + graphCTime);
		
		// 6. 根据社团得出每个社团相应的图.
		HashMap<Integer, HashMap<Integer,HashSet<Integer>>> mapToCommunity = new HashMap<Integer, HashMap<Integer,HashSet<Integer>>>();
		mapToCommunity = kd.getCommunityGraph(graphCommunity, graphMap);
//		System.out.println("mapToCommunity: " + mapToCommunity);
		
		long sixthTime = System.currentTimeMillis();
		long mtpTime = sixthTime - fifthTime;
		System.out.println("get every community's correspodent graph time: " + mtpTime);

		
		// 7. 对每个社团求其相应的密度子图，并得出相应的密度,然后将密度排序，得出前K大密度
		ArrayList<Double> densityList = MapDensity.getcomMapDensity( mapToCommunity);
		System.out.println("densityList: " + densityList);
		long seventhTime = System.currentTimeMillis();
		long densityTime = seventhTime - sixthTime;
		System.out.println("get density time: " + densityTime);
		long runTime = sixthTime - startTime;

		System.out.println("run time: " + runTime);

	}
}
