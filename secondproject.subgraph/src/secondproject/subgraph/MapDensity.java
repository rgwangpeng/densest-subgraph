package secondproject.subgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MapDensity {
	
	// 有保留地删除，不过这次与论文所说删除不一样，只是删除元素少，也就是保留的元素多一些。 也就是 A‘(s) = |A（S）|*fi/(1 + fi)
	public static List<Integer> mySelectDelList(
			HashMap<Integer, HashSet<Integer>> graphMap, double threshold,
			double fi) {
		List<Integer> resultList = new ArrayList<Integer>(); // 用于记录最终被删除节点

		List<Integer> deleteList = new ArrayList<Integer>(); // 用于记录每次被删除的节点
		List<Integer> keyList = new ArrayList<Integer>();
		keyList.addAll(graphMap.keySet());

		for (int i = 0; i < graphMap.size(); i++) {
			if (graphMap.get(keyList.get(i)).size() < threshold) {
				deleteList.add(keyList.get(i));
			}
		}

		double selectFi = (double) fi / (fi + 1);
		int deleteCount = (int) (selectFi * deleteList.size()); // 这个函数与上个函数就是这一条语句不一样。

//		System.out.println("deleteVerList.size( original to delete size): "
//				+ deleteList.size() + " , deleteCount( factly to delete): "
//				+ deleteCount);

		TreeMap<Integer, ArrayList<Integer>> treeMap = new TreeMap<Integer, ArrayList<Integer>>();
		// treeMap 作用： 将节点 按度数值 从小到大排列
		for (int i = 0; i < deleteList.size(); i++) {

			Set<Integer> keySet = graphMap.get(deleteList.get(i));
			int verDegree = keySet.size();
			if (verDegree == 0) {
				resultList.add(deleteList.get(i));
				continue;
			}

			ArrayList<Integer> mapList = new ArrayList<Integer>(); //
			if (treeMap.keySet().contains(keySet.size())) {
				treeMap.get(keySet.size()).add(deleteList.get(i));
			} else {
				mapList.add(deleteList.get(i));
				treeMap.put(keySet.size(), mapList);
			}
		}
		ArrayList<Integer> wmapKeyL = new ArrayList<Integer>(); // 用于存放treeMap的键值
		wmapKeyL.addAll(treeMap.keySet());
		ArrayList<Integer> sortedList = new ArrayList<Integer>(); // 用于存放根据权值排好序的节点

		for (int i = 0; i < wmapKeyL.size(); i++) {
			sortedList.addAll(treeMap.get(wmapKeyL.get(i)));
		}
		int zeroSize = resultList.size();
		// System.out.println("sortedList.size: " + sortedList.size());
		// 由于权重是有效到大排列的，因此，需要删除的节点肯定是排在前边的。
		for (int i = 0; i < deleteCount - zeroSize; i++) {
			resultList.add(sortedList.get(i));
		}
		return resultList;
	}
	
	
	// 得出前k大密度子图
	public static ArrayList<Double> getTopKDensity(HashMap<Integer,Double> densityMap){
		ArrayList<Double> resultList = new ArrayList<Double>();
		
		int K = densityMap.size();
//		int K = (int) (densityMap.size() * 0.01);
		TreeSet<Double> sortedSet = new TreeSet<Double>();
		Set<Integer> keySet = densityMap.keySet();
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			Integer itKey = (Integer) it.next();
			sortedSet.add(densityMap.get(itKey));
		}
		
		ArrayList<Double> sortedList = new ArrayList<Double>();
		sortedList.addAll(sortedSet);
		
		for(int i = 0; i < sortedList.size(); i ++){
			resultList.add(sortedList.get(i));
		}
		
		for(int i = sortedList.size() - 1; i > sortedList.size() - K; i --){
			resultList.add(sortedList.get(i));
		}
		return resultList;
	}
	
	// 得出前k大密度子图
	/*
	 * 首先得出每个图的密度子图，以及相应的密度，根据子图的数量确定K,得出前K大密度
	 */
	public static ArrayList<Double> getcomMapDensity(HashMap<Integer, HashMap<Integer,HashSet<Integer>>> mapToCommunity){
		ArrayList<Double> resultList  = new ArrayList<Double>();
		HashMap<Integer,Double> resultMap = new HashMap<Integer,Double>();
		
		Set<Integer> mckeySet = mapToCommunity.keySet();
		Iterator mcit = mckeySet.iterator();
		while(mcit.hasNext()){
			Integer mcValue = (Integer) mcit.next();
			HashMap<Integer,HashSet<Integer>> mcvalueMap = mapToCommunity.get(mcValue);
			double rmDensity =  withRDelete(mcvalueMap);
			resultMap.put(mcValue, rmDensity);
		}
//		System.out.println("getcomMapDensity, resultMap.size: " + resultMap.size());
		
		resultList = getTopKDensity(resultMap);
		return resultList;
	}
	
	// 进行有保留的删除，每次迭代删除 |A（S）|*fi/(1 + fi) 个元素,得出 remainMap
		public static HashMap<Integer,HashSet<Integer>> withRemainDelete(HashMap<Integer,HashSet<Integer>> graphMap){
			
			HashMap<Integer,HashSet<Integer>> remainMap = new HashMap<Integer,HashSet<Integer>>(); //用于记录每次得到密度比较大的子图
			TreeMap< Integer,HashMap<Integer,HashSet<Integer>> > saveMap = new TreeMap< Integer,HashMap<Integer,HashSet<Integer>> > ();  
			// 用于记录每次得到的remainMap,设置该图最大容量为2，也就是最终是为了保留倒数第二个密度子图
			double fi = 1.0; // 0.5, 1, 2
			int index = 0;  // 用于统计迭代迭代次数
			int saveMapKey = 0; // saveMap 每次存放进一个remainMap，其key值自动加1
			// 计算初始图的密度
			double graphDensity = computeGraphDensity(graphMap);  // 应该把这条语句放在while循环外边，这样才是对的。
			
			while( graphMap.size() != 0){
				
				double threshold = (double)2 * (1 + fi) * graphDensity;                   //deg：每次迭代时，最小度数
				
				System.out.println("迭代 " + index + " ： ");
				System.out.println(" 图密度： " + graphDensity + " ， 度数阈值： " + threshold );
				
				//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中
				List<Integer> deleteVerList = mySelectDelList(graphMap,  threshold, fi);
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
				index ++;
				//计算 经过删除度数小的节点后的子图的密度
				double graphNewDensity = computeGraphDensity(graphMap);
				System.out.println("graphDensity: " + graphDensity + " , graphNewDensity: " + graphNewDensity);
				System.out.println(" remainMap.size(): " + remainMap.size() );
				
				// 如果得出的新图密度大于之前的密度，那么更新图密度，同时将新图存放在 remainMap中
				if(graphNewDensity > graphDensity){
					remainMap.clear();
					remainMap = copyAllMap(graphMap);
					graphDensity = graphNewDensity;
					
				}else{
					break;
				}
			}//while
			
			int mapEdge = getEdgeCount(remainMap);
			System.out.println("graphDensity: " + graphDensity);
			System.out.println(" remainMap -> vertex: " + remainMap.size() + "\tremainMap -> edge: " + mapEdge);
			System.out.println("remainMap -> density: " + computeGraphDensity(remainMap));
			return remainMap;
		}
		
		// 复制图，remainMap， graphMap,将graphMap整个复制到 remainMap
		public static HashMap<Integer,HashSet<Integer>> copyAllMap( HashMap<Integer,HashSet<Integer>> graphMap){
			HashMap<Integer,HashSet<Integer>> resultMap = new HashMap<Integer,HashSet<Integer>>();
			
			Set<Integer> gmkeySet = graphMap.keySet();
			Iterator gmit = gmkeySet.iterator();
			while(gmit.hasNext()){
				Integer key = (Integer) gmit.next();
				HashSet<Integer> gmkeyValueSet = graphMap.get(key);
				HashSet<Integer> mapKeyset = new HashSet<Integer>();
				Iterator it = gmkeyValueSet.iterator();
				while(it.hasNext()){
					Integer itkey = (Integer) it.next();
					mapKeyset.add(itkey);
				}
				resultMap.put(key, mapKeyset);
			}
			
			return resultMap;
		}
	
	
	// 进行有保留删除，最终得出密度值,这个函数在使用时，需要进行修改
	public static double withRDelete(HashMap<Integer, HashSet<Integer>> graphMap) {
		
//		System.out.println("graphMap -> vertex: " + graphMap.size() + "graphMap -> edge: " + getEdgeCount(graphMap));
//		System.out.println("graphMap -> density: " + computeGraphDensity(graphMap));
		
		ArrayList<Double> resultList = new ArrayList<Double>();
		// 用于记录每次得到的remainMap,设置该图最大容量为2，也就是最终是为了保留倒数第二个密度子图
		double fi = 0.33; // 0.5, 1, 2
		int index = 0; // 用于统计迭代迭代次数
		int saveMapKey = 0; // saveMap 每次存放进一个remainMap，其key值自动加1
		// 计算初始图的密度
		double graphDensity = computeGraphDensity(graphMap); // 应该把这条语句放在while循环外边，这样才是对的。
		resultList.add(graphDensity);
		while (graphMap.size() != 0) {

			double threshold = (double) 2 * (1 + fi) * graphDensity; // deg：每次迭代时，最小度数
 			// 这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中
			List<Integer> deleteVerList = mySelectDelList(graphMap, threshold,fi);
			
//			System.out.println("test deleteVerList, delete list size: " + deleteVerList.size());
			
			// 这一步 功能 将deleteVerList 中的节点从图中移除
			// 分两步，1. 从该节点的 邻居节点的 邻居数组集合中移除 该节点； 2. 从图中移除该节点，也就是 vertexMap
			
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
			index++;
			// 计算 经过删除度数小的节点后的子图的密度
			double graphNewDensity = computeGraphDensity(graphMap);
			// 如果得出的新图密度大于之前的密度，那么更新图密度，同时将新图存放在 remainMap中
			if (graphNewDensity > graphDensity) {
				resultList.add(graphNewDensity);
				graphDensity = graphNewDensity;
			} else {
				break;
			}
		}// while
		
		System.out.println("resultList.get(resultList.size() - 1): " + resultList.get(resultList.size() - 1));
		
		return resultList.get(resultList.size() - 1);
	}

	// 得出给定图的边数
	public static int getEdgeCount(HashMap<Integer, HashSet<Integer>> graphMap) {
		int edgeCount = 0;
		Set<Integer> keySet = graphMap.keySet();

		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			HashSet<Integer> valueSet = graphMap.get(key);
			edgeCount += valueSet.size();
		}

		return edgeCount;
	}

	// 计算 HashMap<Integer, HashSet<Integer>> graphMap 图密度
	public static double computeGraphDensity(HashMap<Integer, HashSet<Integer>> graphMap) {
		double density = 0.0;
		int totalDegree = 0;
		Set<Integer> keySet = graphMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			totalDegree += graphMap.get(key).size();
		}

		density = (double) totalDegree / (graphMap.size()) / 2;

		return density;
	}


}
