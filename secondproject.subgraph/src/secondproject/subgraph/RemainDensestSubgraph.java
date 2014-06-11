package secondproject.subgraph;
/*
 * 处理无向图时，有两种策略： 无保留删除 和 有保留删除
 * 对于无保留删除，使用函数：  withoutRemainDelete()
 * 对于有保留删除，使用函数：  withRemainDelete()
 * 
 * 尽量使用 HashMap
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class RemainDensestSubgraph {
	
	// 得出 triangleMap之后，就可以根据triangleMap 建立一个 treeTriMap<Integer,Integer>,根据三角数，排好序。
	// 得到的结果构造是： 键： 三角数，值：节点编号。
	public TreeMap<Integer,HashSet<Integer>> getSortedTriMap(HashMap<Integer,Integer> triangleMap){
		TreeMap<Integer,HashSet<Integer>> resultMap = new TreeMap<Integer,HashSet<Integer>>();
		
		Set<Integer> keyset = triangleMap.keySet();
		Iterator tmit = keyset.iterator();
		while(tmit.hasNext()){
			int tmkey = (Integer) tmit.next();
			int triCount = triangleMap.get(tmkey);
			
			if(resultMap.keySet().contains(triCount)){
				resultMap.get(resultMap).add(tmkey);
			}else{
				HashSet<Integer> valueSet = new HashSet<Integer>();
				valueSet.add(tmkey);
				resultMap.put(triCount, valueSet);
			}
		}
		return resultMap;
	}
	
	// 得出graphMap之后，就可以根据graphMap 建立一个 sortMap<nerborNum，vertexList>，根据节点的邻居数，排好序，这个函数是为了测试之用
	public static TreeMap<Integer, ArrayList<Integer>>  getSortMap(HashMap<Integer, HashSet<Integer>> graphMap) {
		
		TreeMap<Integer, ArrayList<Integer>> sortMap = new TreeMap<Integer, ArrayList<Integer>>();
		List<Integer> keyList = new ArrayList<Integer>(); // 存儲关键字
		
		keyList.addAll(graphMap.keySet());
		for (int i = 0; i < graphMap.size(); i++) {
			ArrayList<Integer> vertexList = new ArrayList<Integer>();
			if (sortMap.keySet().contains(graphMap.get(keyList.get(i)).size())) {
				sortMap.get(graphMap.get(keyList.get(i)).size()).add(keyList.get(i));
			} else {
				vertexList.add(keyList.get(i));
				sortMap.put(graphMap.get(keyList.get(i)).size(), vertexList);
			}
		}
		
		return sortMap;
	}
	
	// 计算 HashMap<Integer, HashSet<Integer>> graphMap 图密度
	public static double computeGraphDensity(HashMap<Integer, HashSet<Integer>> graphMap){
		double density = 0.0;
		int totalDegree = 0;
		Set<Integer> keySet = graphMap.keySet();
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			Integer key = (Integer) it.next();
			totalDegree += graphMap.get(key).size();
		}
		
		density = (double)totalDegree / (graphMap.size()) / 2;
		
		return density;
	}
	
	// 计算 TreeMap<Integer, ArrayList<Integer>> sortMap 的密度
	public double computeTreeMapDensity(TreeMap<Integer, ArrayList<Integer>> treeMap){
		double density = 0.0;
		int totalDegree = 0;
		int totalVertex = 0;
		Set<Integer> keySet = treeMap.keySet();
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			int key = (Integer) it.next();
			
			totalDegree = treeMap.get(key).size() * key;
			totalVertex += treeMap.get(key).size();
		}
		density = totalDegree / totalVertex / 2;
		return density;
	}
	
	
	
	//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中，这是不保留的删除
	public static List<Integer> getDeleteList(HashMap<Integer,HashSet<Integer>> graphMap, double threshold){
		
		List<Integer> deleteList = new ArrayList<Integer>();  //用于记录每次被删除的节点
		List<Integer> belowList = new ArrayList<Integer>();
		belowList.addAll(graphMap.keySet());
		
		for(int i = 0; i < graphMap.size(); i++){
			if( graphMap.get( belowList.get(i) ).size() < threshold ){
				deleteList.add( belowList.get(i) );
			}
		}
		return deleteList;
	}
	
	
	//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中，这是不保留的删除
		public static List<Integer> secgetDeleteList(HashMap<Integer,HashSet<Integer>> graphMap, double threshold){
			
			List<Integer> deleteList = new ArrayList<Integer>();  //用于记录每次被删除的节点
//			List<Integer> belowList = new ArrayList<Integer>();
//			belowList.addAll(graphMap.keySet());
			
			for(int i = 1; i <= graphMap.size(); i++){
				if( graphMap.get( graphMap.get(i) ).size() < threshold ){
					deleteList.add( i );
				}
			}
			return deleteList;
		}
	
	// 这一步是将得分低的节点加入删除队列
//	public static List<Integer> myGetDeleteList(HashMap<Integer,HashSet<Integer>> graphMap, double threshold){
//		
//		List<Integer> deleteList = new ArrayList<Integer>();  //用于记录每次被删除的节点
//		List<Integer> belowList = new ArrayList<Integer>();
//		belowList.addAll(graphMap.keySet());
//		
//		for(int i = 0; i < graphMap.size(); i++){
//			if( graphMap.get( belowList.get(i) ).size() < threshold ){
//				deleteList.add( belowList.get(i) );
//			}
//		}
//		return deleteList;
//	}
	
	public static double copmuteGraphScore(HashMap<Integer,HashSet<Integer>> graphMap){
		
		
		
		double result = 0.0;
		double epsilion = (double)1 / 3;
		int verSize = graphMap.size();
		int edges = getEdgeCount(graphMap);
		
		result = edges - epsilion * (verSize - 1) * verSize / 2 ;    // 计算的是  fscore
//		result = (double) edges  / ( (verSize - 1) * verSize / 2);   // 计算的是 相对边密度
		return result;
		
		
		
	}
	
	
//	// 每次删除最小度数的节点
//	public static HashMap<Integer,HashSet<Integer>> delMinDeg(HashMap<Integer,HashSet<Integer>> graphMap){
//		HashMap<Integer,HashSet<Integer>> resultMap = new HashMap<Integer,HashSet<Integer>>();
//		
//		TreeMap<Integer, ArrayList<Integer>>  sortNebnumMap = getSortMap(graphMap);  // 度数对应相应节点，
//		Set<Integer> smKeyset = sortNebnumMap.keySet();
//		
//		
//		
//		while(graphMap.size() != 0){
//			// 首先找出度数最小的节点
//			
//			
//		}
//		
//		
//		
//		return resultMap;
//	}
	
	
	
	// 处理无向图，这次使用的比较方法有些差异
	public static HashMap<Integer,HashSet<Integer>> myWithoutRemainDelete(HashMap<Integer,HashSet<Integer>> graphMap){
		int index = 0;
		double fi = 0.001; // 删除系数
		HashMap<Integer,HashSet<Integer>> remainMap = new HashMap<Integer,HashSet<Integer>>();
		int count = 0;
		while( graphMap.size() != 0 && count <= 100){
			count ++;
			List<Integer> belowList = new ArrayList<Integer>();
			belowList.addAll(graphMap.keySet());
			
			// graphScore 用于比较整体图
			double graphScore = copmuteGraphScore(graphMap);
			
			// graphDensity 用于得出需要删除的节点集合
			double graphDensity = computeGraphDensity(graphMap);
			double threshold = (double)(1 + fi) * graphDensity;                   //deg：每次迭代时，最小度数
//			double threshold = (double)(1 + fi) * graphScore;
			
//			System.out.println("迭代 " + index + " ： ");
//			System.out.println(" 边密度： " + graphScore + " ， 度数阈值： " + threshold );
			
			//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中
			
			List<Integer> deleteVerL = getDeleteList(graphMap, threshold);
			
//			System.out.println( " deleteVerL.size() : " + deleteVerL.size() +  " \n deleteVerL : " + deleteVerL  );
			//这一步 功能 将deleteVerL 中的节点从图中移除
			//分两步，1. 从该节点的 邻居节点的 邻居数组集合中移除 该节点； 2. 从图中移除该节点，也就是 vertexMap
			for( int j = 0; j < deleteVerL.size(); j ++){
				List<Integer> tempL = new ArrayList<Integer>();
				tempL.addAll( graphMap.get( deleteVerL.get(j) ) );
				
				//System.out.println("j = " + j +  "  deleteVerL.get(j): " + deleteVerL.get(j) + " tempL :" + tempL + "\n + tempL.size():" + tempL.size());
				//1.满足上边条件的节点，其邻居节点肯定也和它有连边，移除该节点邻居节点连接到此节点的边，也就是从它邻居节点的 邻居队列里移除该节点
				for(int k = 0; k < tempL.size(); k ++){
					graphMap.get( tempL.get(k) ).remove( deleteVerL.get(j) );  
				}
				//2. vertexMap 移除该节点
				graphMap.remove( deleteVerL.get(j) );
			}
			
			index ++;
			
			//计算 经过删除度数小 后的 子图的密度
			if(graphMap.size() == 0){
				return remainMap;
			}
			double graphNewScore = copmuteGraphScore(graphMap);
//			double graphNewDensity = computeGraphDensity(graphMap);
//			System.out.println("graphScore: " + graphScore + " , graphNewScore: " + graphNewScore);
			
			if(graphNewScore > graphScore){
				remainMap.clear();
				remainMap = copyAllMap(graphMap);
				graphScore = graphNewScore;
			}
//			else{
//				break;
//			}
//			System.out.println(" remainMap -> vertex: " + remainMap.size());
//			System.out.println();
		}//while
		
//		System.out.println("graph process result");
//		System.out.println(" remainMap.size(): " + remainMap.size() );
//		System.out.println(" remainMap.density: " +  computeGraphDensity(remainMap));
		int mapEdge = getEdgeCount(remainMap);
		System.out.println(" remainMap -> vertex: " + remainMap.size() + "\tremainMap -> edge: " + mapEdge + "\tremainMap ->graphScore: " + copmuteGraphScore(remainMap));
		return remainMap;
	}
	
	// 处理无向图,无保留删除图中元素
		public static HashMap<Integer,HashSet<Integer>> secwithoutRemainDelete(HashMap<Integer,HashSet<Integer>> graphMap){
			int index = 0;
			double fi = 0.001; // 删除系数
			HashMap<Integer,HashSet<Integer>> remainMap = new HashMap<Integer,HashSet<Integer>>();
			
//			while( graphMap.size() != 0){
				
//				List<Integer> belowList = new ArrayList<Integer>();
//				belowList.addAll(graphMap.keySet());
				
				double graphDensity = computeGraphDensity(graphMap);
				double threshold = (double)(1 + fi) * graphDensity;                   //deg：每次迭代时，最小度数
				
//				System.out.println("迭代 " + index + " ： ");
//				System.out.println(" 图密度： " + graphDensity + " ， 度数阈值： " + threshold );
				
				//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中
				
				List<Integer> deleteVerL = getDeleteList(graphMap, threshold);
				
				System.out.println("deleteVerL.size: " + deleteVerL.size());
//				System.out.println( " deleteVerL.size() : " + deleteVerL.size() +  " \n deleteVerL : " + deleteVerL  );
				//这一步 功能 将deleteVerL 中的节点从图中移除
				//分两步，1. 从该节点的 邻居节点的 邻居数组集合中移除 该节点； 2. 从图中移除该节点，也就是 vertexMap
				for( int j = 0; j < deleteVerL.size(); j ++){
					List<Integer> tempL = new ArrayList<Integer>();
					tempL.addAll( graphMap.get( deleteVerL.get(j) ) );
					
					//System.out.println("j = " + j +  "  deleteVerL.get(j): " + deleteVerL.get(j) + " tempL :" + tempL + "\n + tempL.size():" + tempL.size());
					//1.满足上边条件的节点，其邻居节点肯定也和它有连边，移除该节点邻居节点连接到此节点的边，也就是从它邻居节点的 邻居队列里移除该节点
					for(int k = 0; k < tempL.size(); k ++){
						graphMap.get( tempL.get(k) ).remove( deleteVerL.get(j) );  
					}
					//2. vertexMap 移除该节点
					graphMap.remove( deleteVerL.get(j) );
				}
				
//				index ++;
				
				//计算 经过删除度数小 后的 子图的密度
//				if(graphMap.size() == 0){
//					return remainMap;
//				}
//				double graphNewDensity = computeGraphDensity(graphMap);
////				System.out.println("graphDensity: " + graphDensity + " , graphNewDensity: " + graphNewDensity);
//				
//				if(graphNewDensity > graphDensity){
//					remainMap.clear();
//					remainMap = copyAllMap(graphMap);
//					graphDensity = graphNewDensity;
//				}else{
//					break;
//				}
//				System.out.println(" remainMap -> vertex: " + remainMap.size());
//				System.out.println();
//			}//while
			
//			System.out.println("graph process result");
//			System.out.println(" remainMap.size(): " + remainMap.size() );
//			System.out.println(" remainMap.density: " +  computeGraphDensity(remainMap));
			int mapEdge = getEdgeCount(graphMap);
			System.out.println(" remainMap -> vertex: " + graphMap.size() + "\tremainMap -> edge: " + mapEdge);
//			return remainMap;
			
			return graphMap;
		}
	
	// 处理无向图,无保留删除图中元素
	public static HashMap<Integer,HashSet<Integer>> withoutRemainDelete(HashMap<Integer,HashSet<Integer>> graphMap){
		int index = 0;
		double fi = 0.001; // 删除系数
		HashMap<Integer,HashSet<Integer>> remainMap = new HashMap<Integer,HashSet<Integer>>();
		
		while( graphMap.size() != 0){
			
//			List<Integer> belowList = new ArrayList<Integer>();
//			belowList.addAll(graphMap.keySet());
			
			double graphDensity = computeGraphDensity(graphMap);
			System.out.println("at first, graphMap.size(): " + graphMap.size() + "\tgraphDensity: " + graphDensity);
			double threshold = (double)(1 + fi) * graphDensity ;                   //deg：每次迭代时，最小度数
//			double threshold = 1.1;
//			System.out.println("迭代 " + index + " ： ");
//			System.out.println(" 图密度： " + graphDensity + " ， 度数阈值： " + threshold );
			
			//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中
			
			List<Integer> deleteVerL = getDeleteList(graphMap, threshold);
			
			System.out.println("deleteVerL.size: " + deleteVerL.size());
//			System.out.println( " deleteVerL.size() : " + deleteVerL.size() +  " \n deleteVerL : " + deleteVerL  );
			//这一步 功能 将deleteVerL 中的节点从图中移除
			//分两步，1. 从该节点的 邻居节点的 邻居数组集合中移除 该节点； 2. 从图中移除该节点，也就是 vertexMap
			for( int j = 0; j < deleteVerL.size(); j ++){
				List<Integer> tempL = new ArrayList<Integer>();
				tempL.addAll( graphMap.get( deleteVerL.get(j) ) );
				
				//System.out.println("j = " + j +  "  deleteVerL.get(j): " + deleteVerL.get(j) + " tempL :" + tempL + "\n + tempL.size():" + tempL.size());
				//1.满足上边条件的节点，其邻居节点肯定也和它有连边，移除该节点邻居节点连接到此节点的边，也就是从它邻居节点的 邻居队列里移除该节点
				for(int k = 0; k < tempL.size(); k ++){
					graphMap.get( tempL.get(k) ).remove( deleteVerL.get(j) );  
				}
				//2. vertexMap 移除该节点
				graphMap.remove( deleteVerL.get(j) );
			}
			System.out.println("at second, graphMap.size(): " + graphMap.size() + "\tgraphDensity: " + computeGraphDensity(graphMap));
			
			index ++;
			
			//计算 经过删除度数小 后的 子图的密度
			if(graphMap.size() == 0){
				return remainMap;
			}
			double graphNewDensity = computeGraphDensity(graphMap);
			
//			System.out.println("graphMap.size(): " + + "\tgraphNewDensity: " + graphNewDensity);
//			System.out.println("graphDensity: " + graphDensity + " , graphNewDensity: " + graphNewDensity);
			
			if((index > 1)&&(graphNewDensity > graphDensity)){
				remainMap.clear();
				remainMap = copyAllMap(graphMap);
				graphDensity = graphNewDensity;
			}else{
				break;
			}
//			System.out.println(" remainMap -> vertex: " + remainMap.size());
//			System.out.println();
		}//while
		
//		System.out.println("graph process result");
//		System.out.println(" remainMap.size(): " + remainMap.size() );
//		System.out.println(" remainMap.density: " +  computeGraphDensity(remainMap));
		int mapEdge = getEdgeCount(remainMap);
		System.out.println(" remainMap -> vertex: " + remainMap.size() + "\tremainMap -> edge: " + mapEdge);
		return remainMap;
	}
	
	// 得出 selectDelList,是基于之前deleteList的基础上，增加删除策略,得出需要删除节点list
	// 处理deleteList,按照加权排序决定需要删除的节点， 节点度数degree 与 节点邻居度数平均值 avgNeborDegree  w1*degree + w2*avgNeborDegree
	// 首先使用 weightMap 存放相应的 <权值，节点>， 接着安权值将节点排序存放在sortedList,最后根据deleteCount的出需要删除的相应节点
	public static List<Integer> getSelectDelList(HashMap<Integer,HashSet<Integer>> graphMap, double threshold,double fi){
		List<Integer> resultList = new ArrayList<Integer>(); // 用于记录最终结果
		List<Integer> deleteList = new ArrayList<Integer>();  //用于记录每次被删除的节点
		List<Integer> keyList = new ArrayList<Integer>();
		keyList.addAll(graphMap.keySet());
		
		for(int i = 0; i < graphMap.size(); i++){
			if( graphMap.get( keyList.get(i) ).size() < threshold ){
				deleteList.add( keyList.get(i) );
			}
		}
		
		double selectFi = (double)fi/(fi + 1);
		int deleteCount = (int)(selectFi * deleteList.size());
		
		System.out.println("deleteVerList.size( original to delete size): " + deleteList.size() + " , deleteCount( factly to delete): " + deleteCount);
		
		double w1 = 0.9;  // 度数权值系数
		double w2 = 0.1;  // 邻居节点平均密度权值系数
//		ArrayList<Double> wlist = new ArrayList<Double>();     // 记录deleteList中每个节点的权值
		TreeMap<Double,ArrayList<Integer>> weightMap = new TreeMap<Double,ArrayList<Integer>>(); // 记录权重系数， key:权重， value:节点编号
		for(int i = 0; i < deleteList.size(); i++){
			Set<Integer> keySet = graphMap.get(deleteList.get(i));
			
			int verDegree = keySet.size();
			if(verDegree == 0){
				resultList.add(deleteList.get(i));
				continue;
			}
			int nerborDegree = 0;
			Iterator it = keySet.iterator();
			while(it.hasNext()){
				int keyValue = (Integer) it.next();
				nerborDegree += graphMap.get(keyValue).size();
			}
			
			double avgNeborDeg = nerborDegree / verDegree;
			double w = w1*verDegree + w2*avgNeborDeg;
			
			if(weightMap.keySet().contains(w)){
				weightMap.get(w).add(deleteList.get(i));
			}
			else{
				ArrayList<Integer> valueList = new ArrayList<Integer>();
				valueList.add(deleteList.get(i));
				weightMap.put(w, valueList);
			}
		}
		ArrayList<Double> wmapKeyL = new ArrayList<Double>();      // 用于存放weightMap的键值
		wmapKeyL.addAll(weightMap.keySet());
		
		ArrayList<Integer> sortedList = new ArrayList<Integer>(); // 用于存放根据权值排好序的节点
		
		for(int i = 0; i < wmapKeyL.size(); i ++){
			sortedList.addAll(weightMap.get(wmapKeyL.get(i)));
		}
		int zeroSize = resultList.size();
		// 由于权重是有效到大排列的，因此，需要删除的节点肯定是排在前边的。
		for(int i = 0; i < deleteCount - zeroSize; i ++ ){
			resultList.add(sortedList.get(i));
		}
		return resultList;
	}
	
	// 这个是根据论文里提到的方法，实现的，也就是  A(s) = |S|*fi/(1 + fi)
	public static List<Integer> scdSelectDelList(HashMap<Integer,HashSet<Integer>> graphMap, double threshold, double fi){
		List<Integer> resultList = new ArrayList<Integer>();  //用于记录最终被删除节点
		
		List<Integer> deleteList = new ArrayList<Integer>();  //用于记录每次被删除的节点
		List<Integer> keyList = new ArrayList<Integer>();
		keyList.addAll(graphMap.keySet());
		
		for(int i = 0; i < graphMap.size(); i++){
			if( graphMap.get( keyList.get(i) ).size() < threshold ){
				deleteList.add( keyList.get(i) );
			}
		}
		
		double selectFi = (double)fi/(fi + 1);
		int deleteCount = (int)(selectFi * graphMap.size());
		
		System.out.println("deleteVerList.size( original to delete size): " + deleteList.size() + " , deleteCount( factly to delete): " + deleteCount);
		
		TreeMap<Integer,ArrayList<Integer>> treeMap = new TreeMap<Integer,ArrayList<Integer>>();
		// treeMap 作用： 将节点 按度数值 从小到大排列
		for(int i = 0; i < deleteList.size(); i++){
			
			Set<Integer> keySet = graphMap.get(deleteList.get(i));
			int verDegree = keySet.size();
			if(verDegree == 0){
				resultList.add(deleteList.get(i));
				continue;
			}
			
			ArrayList<Integer> mapList = new ArrayList<Integer>();   //
			if(treeMap.keySet().contains( keySet.size() ) ){
				treeMap.get(keySet.size() ).add( deleteList.get(i) );
			}
			else{
				mapList.add(deleteList.get(i));
				treeMap.put( keySet.size(),mapList );
//				mapList.clear();
			}
		}
		
		ArrayList<Integer> wmapKeyL = new ArrayList<Integer>();      // 用于存放treeMap的键值
		wmapKeyL.addAll(treeMap.keySet());
		
		ArrayList<Integer> sortedList = new ArrayList<Integer>(); // 用于存放根据权值排好序的节点
		
		for(int i = 0; i < wmapKeyL.size(); i ++){
			sortedList.addAll(treeMap.get(wmapKeyL.get(i)));
		}
		
		int zeroSize = resultList.size();
		
//		System.out.println("sortedList.size: " + sortedList.size());
		// 由于权重是有效到大排列的，因此，需要删除的节点肯定是排在前边的。
		for(int i = 0; i < deleteCount - zeroSize; i ++ ){
			resultList.add(sortedList.get(i));
		}
		return resultList;
	}
	
	// 有保留地删除，不过这次与论文所说删除不一样，只是删除元素少，也就是保留的元素多一些。 也就是  A‘(s) = |A（S）|*fi/(1 + fi)
	public static List<Integer> mySelectDelList(HashMap<Integer,HashSet<Integer>> graphMap, double threshold, double fi){
		List<Integer> resultList = new ArrayList<Integer>();  //用于记录最终被删除节点
		
		List<Integer> deleteList = new ArrayList<Integer>();  //用于记录每次被删除的节点
		List<Integer> keyList = new ArrayList<Integer>();
		keyList.addAll(graphMap.keySet());
		
		for(int i = 0; i < graphMap.size(); i++){
			if( graphMap.get( keyList.get(i) ).size() < threshold ){
				deleteList.add( keyList.get(i) );
			}
		}
		
		double selectFi = (double)fi/(fi + 1);
		int deleteCount = (int)(selectFi * deleteList.size());  // 这个函数与上个函数就是这一条语句不一样。
		
		System.out.println("deleteVerList.size( original to delete size): " + deleteList.size() + " , deleteCount( factly to delete): " + deleteCount);
		
		TreeMap<Integer,ArrayList<Integer>> treeMap = new TreeMap<Integer,ArrayList<Integer>>();
		// treeMap 作用： 将节点 按度数值 从小到大排列
		for(int i = 0; i < deleteList.size(); i++){
			
			Set<Integer> keySet = graphMap.get(deleteList.get(i));
			int verDegree = keySet.size();
			if(verDegree == 0){
				resultList.add(deleteList.get(i));
				continue;
			}
			
			ArrayList<Integer> mapList = new ArrayList<Integer>();   //
			if(treeMap.keySet().contains( keySet.size() ) ){
				treeMap.get(keySet.size() ).add( deleteList.get(i) );
			}
			else{
				mapList.add(deleteList.get(i));
				treeMap.put( keySet.size(),mapList );
			}
		}
		ArrayList<Integer> wmapKeyL = new ArrayList<Integer>();      // 用于存放treeMap的键值
		wmapKeyL.addAll(treeMap.keySet());
		ArrayList<Integer> sortedList = new ArrayList<Integer>(); // 用于存放根据权值排好序的节点
		
		for(int i = 0; i < wmapKeyL.size(); i ++){
			sortedList.addAll(treeMap.get(wmapKeyL.get(i)));
		}
		int zeroSize = resultList.size();
//		System.out.println("sortedList.size: " + sortedList.size());
		// 由于权重是有效到大排列的，因此，需要删除的节点肯定是排在前边的。
		for(int i = 0; i < deleteCount - zeroSize; i ++ ){
			resultList.add(sortedList.get(i));
		}
		return resultList;
	}
	
	// 进行有保留删除，保留密度最大的子图
public static HashMap<Integer,HashSet<Integer>> withRemainLastDelete(HashMap<Integer,HashSet<Integer>> graphMap){
		
		HashMap<Integer,HashSet<Integer>> remainMap = new HashMap<Integer,HashSet<Integer>>(); //用于记录每次得到密度比较大的子图
		// 用于记录每次得到的remainMap,设置该图最大容量为2，也就是最终是为了保留倒数第二个密度子图
		double fi = (double)1/3; // 0.5, 1, 2
		int index = 0;  // 用于统计迭代迭代次数
		// 计算初始图的密度
		double graphDensity = computeGraphDensity(graphMap);  // 应该把这条语句放在while循环外边，这样才是对的。
		
		while( graphMap.size() != 0){
			
			double threshold = (double)2 * (1 + fi) * graphDensity;                   //deg：每次迭代时，最小度数
			
			System.out.println("迭代 " + index + " ： ");
			System.out.println(" 图密度： " + graphDensity + " ， 度数阈值： " + threshold );
			
			//这一步 功能 将 度数小于阈值的节点存入 deleteVerL 数组集合中
			List<Integer> deleteVerList = mySelectDelList(graphMap,  threshold, fi);
//			System.out.println( " deleteVe.size() : " + deleteVe.size() +  " \n deleteVe : " + deleteVe  );
			
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
			
			System.out.println(" graphMap.size(): " + graphMap.size() + "\tgraphMap.edge: " + getEdgeCount(graphMap) +"\tgraphMap.density: " + computeGraphDensity(graphMap));
			
			// 如果得出的新图密度大于之前的密度，那么更新图密度，同时将新图存放在 remainMap中
			if(graphNewDensity > graphDensity){
				remainMap.clear();
				remainMap = copyAllMap(graphMap);
				graphDensity = graphNewDensity;
			}else{
				break;
			}
			System.out.println(" remainMap.size(): " + remainMap.size() + "\tremainMap.edge: " + getEdgeCount(remainMap) +"\tremainMap.density: " + computeGraphDensity(remainMap));
		}//while
		
		int mapEdge = getEdgeCount(remainMap);
		System.out.println(" remainMap -> vertex: " + remainMap.size() + "\tremainMap -> edge: " + mapEdge
				+ "\tremainMap -> density: " + computeGraphDensity(remainMap));
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
	
	// 进行有保留的删除，每次迭代删除 |A（S）|*fi/(1 + fi) 个元素,而且保留的是倒数第二个产生的密度子图。
	public static HashMap<Integer,HashSet<Integer>> withRemainDelete(HashMap<Integer,HashSet<Integer>> graphMap){
		
//		HashMap<Integer,HashSet<Integer>> resultMap = new HashMap<Integer,HashSet<Integer>>(); // 用于记录最终结果
		
		HashMap<Integer,HashSet<Integer>> remainMap = new HashMap<Integer,HashSet<Integer>>(); //用于记录每次得到密度比较大的子图
		TreeMap< Integer,HashMap<Integer,HashSet<Integer>> > saveMap = new TreeMap< Integer,HashMap<Integer,HashSet<Integer>> > ();  
		// 用于记录每次得到的remainMap,设置该图最大容量为2，也就是最终是为了保留倒数第二个密度子图
		double fi = (double)1/3; // 0.5, 1, 2
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
//			System.out.println( " deleteVe.size() : " + deleteVe.size() +  " \n deleteVe : " + deleteVe  );
			
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
				HashMap<Integer,HashSet<Integer>> tmpMap = new HashMap<Integer,HashSet<Integer>>(); //用于临时记录remainMap
				tmpMap = copyAllMap(remainMap);
				graphDensity = graphNewDensity;
				saveMap.put(saveMapKey, tmpMap);
				saveMapKey ++;
				
				if(saveMap.size() > 2){
					ArrayList<Integer> saveList = new ArrayList<Integer>();
					saveList.addAll(saveMap.keySet());
					saveMap.remove(saveList.get(0));
				}
			}else{
				break;
			}
		}//while
		
		if(saveMapKey > 2){
			System.out.println("saveMap.size: " + saveMap.size() + "\t saveMapKey: " + saveMapKey);
			System.out.println("saveMapKey - 2: " + (saveMapKey - 2) + "\t saveMap.get(saveMapKey - 2).size: " + saveMap.get(saveMapKey - 2).size() );
			System.out.println("saveMapKey - 1: " + (saveMapKey - 1) + "\t saveMap.get(saveMapKey - 1).size: " + saveMap.get(saveMapKey - 1).size() );
			remainMap.clear();
			remainMap = copyAllMap(saveMap.get(saveMapKey - 2));
		}
		else{
			System.out.println("saveMap.size: " + saveMap.size() + "\t saveMapKey: " + saveMapKey);
			remainMap.clear();
			remainMap = copyAllMap(saveMap.get(saveMapKey));
		}
		
//		System.out.println("saveMapKey - 2: " + (saveMapKey - 2) + "\t saveMap.get(saveMapKey - 2).size: " + saveMap.get(saveMapKey - 2).size() );
//		System.out.println("saveMapKey - 1: " + (saveMapKey - 1) + "\t saveMap.get(saveMapKey - 1).size: " + saveMap.get(saveMapKey - 1).size() );
		
		
		int mapEdge = getEdgeCount(remainMap);
		System.out.println(" remainMap -> vertex: " + remainMap.size() + "\tremainMap -> edge: " + mapEdge);
		return remainMap;
	}
	
	// 得出给定图的边数
		public static int getEdgeCount(HashMap<Integer, HashSet<Integer>> graphMap){
			int edgeCount = 0;
			Set<Integer> keySet = graphMap.keySet();
			
			Iterator it = keySet.iterator();
			while(it.hasNext()){
				int key = (Integer) it.next();
				HashSet<Integer> valueSet = graphMap.get(key);
				edgeCount += valueSet.size();
			}
			
			return edgeCount / 2;
		}
		
		
		// 这步处理的是  根据社团得出每个社团相应的图.
		/*
		 * HashMap<Integer,ArrayList<Integer>> graphCommunity = new HashMap<Integer,ArrayList<Integer>>(); // 键是社团编号，值是社团中的元素。
		graphCommunity = kd.getSubCommunity(mapfilePath);
		 */
		
}
