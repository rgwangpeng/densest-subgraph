package secondproject.subgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * 实现类中所提到的方法
 * 5. 得出与 resultMap 对应的文件的 .net文件，是为了计算图中所包含的子社团个数
 * 6. 使用 infomap 算法计算 得出社团结构，对每个社团结构计算其密度，得出的所有密度与之前的社团密度进行比较。
 */
public class KDensest {
	
//	public static void main(String[] args) throws IOException {
////		String filePath = "/home/hadoop/桌面/123456/Email-Enron.map";
//		String filePath = "/home/hadoop/桌面/fgh/testgraph.map";
//		getSubCommunity(filePath);
//		
//	}
	
	// 根据mapinfo得出的结果，主要处理.map文件，得出 HashMap<Integer,ArrayList<Integer>>。键：社团编号，为了有明确标识，我们取 ArrayList的第一个元素， ArrayList存放对应社团的元素
	public HashMap<Integer,ArrayList<Integer>> getSubCommunity(String mapFilePath) throws IOException{
		HashMap<Integer,ArrayList<Integer>> subComMap = new HashMap<Integer,ArrayList<Integer>>();
		File mapFile = new File(mapFilePath);
		BufferedReader buffR = new BufferedReader(new FileReader(mapFile));
		String readLabel = new String("*Nodes");
		String str = new String("");
		while(!(str = buffR.readLine()).contains(readLabel));
//		System.out.println("str++: " + str);
		while((str = buffR.readLine()) != null){
			if(str.length() < 3){
				continue;
			}
			if(str.contains("*Links")){
				break;
			}
			
			int endkLabel = str.indexOf(':');
			
			int key = Integer.parseInt( str.substring(0, endkLabel) );
			
			int start = str.indexOf('"');
			int end = str.lastIndexOf('"');
			int value = Integer.parseInt( str.substring(start+1,end) );
			
			if(subComMap.keySet().contains(key)){
				subComMap.get(key).add(value);
			}else{
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(value);
				subComMap.put(key,list);
			}
		}
		buffR.close();
		
		ArrayList<Integer> keyList = new ArrayList<Integer>();
		keyList.addAll(subComMap.keySet());
		
		for(int i = 0; i < keyList.size(); i ++){
			
			ArrayList<Integer> ali = new ArrayList<Integer>();
			ali.addAll(subComMap.get( keyList.get(i)) );
		}
		
		System.out.println("get community size: " + subComMap.size() );
		return subComMap;
	}
	
	// 计算各子图的密度
	public HashMap<Integer,Double> SubComsDen(HashMap<Integer,ArrayList<Integer>> subComMap, HashMap<Integer,HashSet<Integer>> resuleMap){
		
		HashMap<Integer,Double> densityMap = new HashMap<Integer,Double>();
		
		ArrayList<Integer> keyList = new ArrayList<Integer>(); // 用于记录 subComMap的键
		keyList.addAll(subComMap.keySet());
		
		ArrayList<Double> densityList = new ArrayList<Double>();
		
		for(int i = 0; i < keyList.size(); i ++){
			
			ArrayList<Integer> valueList = new ArrayList<Integer>();
			valueList.addAll(subComMap.get( keyList.get(i)) );
			
//			System.out.println("test valueList.size: " + valueList.size());
			
			double density = cmpSubDensity(valueList, resuleMap);
			
//			System.out.println( "key: " + keyList.get(i) + "  density: " + density);
			
			densityList.add(density);
			densityMap.put(i + 1, density);
		}
		
		System.out.println(" density list :");
		for(int i = 0; i < densityList.size(); i ++){
			System.out.print(densityList.get(i) + " ");
		}
		System.out.println();
		
		return densityMap;
	}
	
	// 对密度进行排序，使用快速排序算法，等会使用fibonacci堆，算是对fibonacci堆的理解了
	
	// 每个小的社团（其实就是小的密度子图），就相当于一个图一样，计算图的密度
	public double cmpSubDensity(ArrayList<Integer> valueList, HashMap<Integer,HashSet<Integer>> resuleMap){
		double density = 0.0;
		int vertexNum = valueList.size();
		int edgeNum = 0;
		
		for(int i = 0; i < valueList.size(); i ++){
			int key = valueList.get(i);
			HashSet<Integer> valueSet = resuleMap.get(key);
			if(valueSet == null){
				continue;
			}
//			System.out.println("testing valueSet: " + valueSet);
			// 在valueSet 中遍历 valueList，如果 valueSet 中包含 valueList的值，那么就将边的计数增加1
			for(int j = 0; j < valueList.size(); j ++){
				if(j == i){
					continue;
				}
				if(valueSet.contains( valueList.get(j) )){
					edgeNum ++;
				}
			}
		}
		density = (double)edgeNum / vertexNum / 2;
		return density;
	}
	
	// 根据社团得出每个社团相应的图.
	public HashMap<Integer, HashMap<Integer,HashSet<Integer>>> getCommunityGraph(HashMap<Integer,ArrayList<Integer>> graphCommunity, HashMap<Integer,HashSet<Integer>> graphMap){
		 HashMap<Integer, HashMap<Integer,HashSet<Integer>>> commToGraph = new  HashMap<Integer, HashMap<Integer,HashSet<Integer>>>();
		 
		 Set<Integer> keySet = graphCommunity.keySet();
		 Iterator it = keySet.iterator();
		 
		 while(it.hasNext()){
			Integer gckey = (Integer) it.next();
			ArrayList valueList = graphCommunity.get(gckey); // graphCommunity的键值列表
			
			HashMap<Integer,HashSet<Integer>> valueMap = new HashMap<Integer,HashSet<Integer>>();
			valueMap = genrateSubMap(valueList,graphMap);    // 根据graphCommunity的键值列表，生成相对于graphMap的子图
			commToGraph.put(gckey, valueMap);
			
		 }
		 
		 System.out.println("community size: " + graphCommunity.size());
		 
		 return commToGraph;
	}
	
	// 根据给定的 valueList，与相应的 graphMap 进行匹配，得出相应的图
	public HashMap<Integer,HashSet<Integer>> genrateSubMap(ArrayList<Integer> valueList, HashMap<Integer,HashSet<Integer>> graphMap){
		HashMap<Integer,HashSet<Integer>> genrateMap = new HashMap<Integer,HashSet<Integer>>();
		
		for(int i = 0; i < valueList.size(); i ++){
			int key = valueList.get(i);
			HashSet<Integer> valueSet = graphMap.get(key);
			if(valueSet == null){
				continue;
			}
//			System.out.println("testing valueSet: " + valueSet);
			// 在valueSet 中遍历 valueList，如果 valueSet 中包含 valueList的值，那么就将边的计数增加1
			HashSet<Integer> neborSet = new HashSet<Integer>();
			for(int j = 0; j < valueList.size(); j ++){
				
				if(j == i){
					continue;
				}
				if(valueSet.contains( valueList.get(j) )){
					neborSet.add(valueList.get(j));
				}
			}
			
			genrateMap.put(key, neborSet);
		}
		
		return genrateMap;
	}
	
}
