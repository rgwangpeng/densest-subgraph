package secondproject.subgraph;
/*
 * 测试程序，测试需要进行7步：
 * 1. 转换文件格式，对文件进行改写，切实是为了符合.net文件风格，开始节点从0开始
 * 2. 得出与读入文件对应的 graphMap
 * 3. 得出与 graphMap 对应的文件的 .net文件，是为了计算给定的初始图的社团个数
 * 4. 对graphMap应用高密度子图算法，得出密度子图 resultMap
 * 5. 得出与 resultMap 对应的文件的 .net文件，是为了计算图中所包含的子社团个数
 * 6. 使用 infomap 算法计算 得出社团结构，对每个社团结构计算其密度，得出的所有密度与之前的社团密度进行比较。
 * 7. 得出前k个密度子图
 * 
 * 因为暂时不能调用 infomap 算法，因此，必须手动进行，所以该算法先执行到前5步
 * 
 * TestSub主要用到两个类： PreProcessFile RemainDensestSubgraph 
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


public class TestSub {
	public static void main(String[] args) throws IOException {
		
		long startTime = System.currentTimeMillis();
		String graphPath = "/home/hadoop/数据集/Email-Enron.txt";
//		String graphPath = "/home/hadoop/桌面/012";
		String postProfilePath = "/home/hadoop/桌面/preEmail-Enron.txt";
		PreProcessFile ppf = new PreProcessFile();
		//  1.转换文件格式
		ppf.changeFile(graphPath, postProfilePath);
		
		long firstTime = System.currentTimeMillis();
		long changeFT = firstTime -startTime;
		System.out.println(" change file time: " + changeFT);
		
		//  2.得出与读入文件对应的 graphMap
		HashMap<Integer,HashSet<Integer>> graphMap = ppf.symUndirGraph(postProfilePath);
		
		long secondTime = System.currentTimeMillis();
		long getGraphT = secondTime - firstTime;
		System.out.println(" get graph time: " + getGraphT);
		
		// 3.得出与 graphMap 对应的文件的 .net文件
		String writePath = "/home/hadoop/桌面/graphmap/Email-Enron.net";
		ppf.writeNetFile(graphMap, writePath);
		
		long thirdTime = System.currentTimeMillis();
		long writeTime = thirdTime - secondTime;
		System.out.println("write graphmap to .net file time: " + writeTime);
		
		// 4. 对graphMap应用高密度子图算法，得出密度子图 resultMap
		HashMap<Integer,HashSet<Integer>> resultMap = RemainDensestSubgraph.withRemainDelete(graphMap);
		System.out.println("resuleMap.size: " + resultMap.size());
		
		long forthTime = System.currentTimeMillis();
		long getResultT = forthTime - thirdTime;
		System.out.println("get resultMap time,this time span is the algorithm time: " + getResultT);
		
		// 5. 转换resultMap格式
		HashMap<Integer,HashSet<Integer>> convertMap = ppf.convertResultMap(resultMap);
		
		// 6. 得出与 convertMap -- resultMap 对应的文件的 .net文件
		String wPath = "/home/hadoop/桌面/resultmap/resultEmail-Enron.net";
		ppf.writeNetFile(convertMap, wPath);
		
		long fifthTime = System.currentTimeMillis();
		long wTime = fifthTime - forthTime;
		System.out.println("write resultMap to .net fie time: " + wTime);
		
		// 7. 将convertMap -- resultMap写进文件，因为不能使用程序调用infomap，因此采用先写进文件，再读文件的方式。
		String resultMapPath = "/home/hadoop/桌面/resultmap/resultEmail-Enron.txt";
		ppf.writeResultMap(resultMapPath, convertMap);
		
		long sixthTime = System.currentTimeMillis();
		long writeRMT = sixthTime - fifthTime;
		System.out.println("write resultMap to resultMapPath time: " + writeRMT);
		
		// 这个类运行到这，就算结束了。
		// 6. 使用 infomap 算法计算 得出社团结构，对每个社团结构计算其密度，得出的所有密度与之前的社团密度进行比较。
		// 这个需要使用java 调用 c++ 算法，得出社团结构文件，然后提取文件
		
		// 7. 得出前k个密度子图。
		
		
		long runTime = sixthTime - startTime;
		
		System.out.println(" change file time: " + changeFT);
		System.out.println("get graphMap tmie: " + getGraphT);
		System.out.println("write net file time: " + writeTime);
		System.out.println("get resultMap time,this time span is the algorithm time: " + getResultT);
		System.out.println("second write file time: " + wTime);
		System.out.println("write resultMap to resultMapPath time: " + writeRMT);
		System.out.println("run time: " + runTime);
		System.out.println("finish! ");
	}
}
