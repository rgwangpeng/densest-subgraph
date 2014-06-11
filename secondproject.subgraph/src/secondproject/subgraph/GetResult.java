package secondproject.subgraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/*
 *  7. 得出前k个密度子图
 *  
 *  该类主要使用 KDensest PreProcessFile两个类
 */
public class GetResult {
	public static void main(String[] args) throws IOException {
		
		long startTime = System.currentTimeMillis(); 
		KDensest kd = new KDensest();
		
		// mapFilePath 必须是以.map结尾的文件，而且是根据上步infomap运算,得出的.map
		String mapFilePath = "/home/hadoop/桌面/test/a.map";
		
		 HashMap<Integer,ArrayList<Integer>> subComMap = new HashMap<Integer,ArrayList<Integer>>();
		// 1. 得出 各个社团
		 subComMap = kd.getSubCommunity(mapFilePath);
		System.out.println("subComMap.size: " + subComMap.size());
		 
		long secondTime = System.currentTimeMillis();
		long getsubTime = secondTime - startTime;
		System.out.println("getSubCommunity time: " + getsubTime);
		
		//2. 得出上步计算的 resuleMap
		PreProcessFile ppf = new PreProcessFile();
		// writeMapPath 的路径必须和之前写的文件路径对应
		String writeMapPath = "/home/hadoop/桌面//test/a.txt";
		HashMap<Integer,HashSet<Integer>> resultMap = ppf.readResultMap(writeMapPath);
		
		System.out.println("resultMap.size: " + resultMap.size());
		
		kd.SubComsDen(subComMap, resultMap);
		
		long thirdTime = System.currentTimeMillis();
		long getDenTime = thirdTime - secondTime;
		
		long runTime = thirdTime - startTime;
		System.out.println(" get density time: " + getDenTime);
		System.out.println("run time: " + runTime);
		
	}
}
