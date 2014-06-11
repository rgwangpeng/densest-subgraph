package secondproject.subgraph;
/*
 * 程序中最主要的两个函数就是: getGraph getDirGraph,
 * 为了处理文件，生成.net文件方便，增加两个函数： changeFile， writeNetFile
 * public HashMap<Integer,HashSet<Integer>> symUndirGraph( String filePath),根据无向图文件名，得出无向图
 * public HashMap<Integer, HashSet<Integer>>  asymGraph(String filePath) ，根据有向图文件名，得出有向图
 * public void changeFile(String sfpath, String dfpath)  为了符合.net文件的特点，就必须节点排好序，而且节点开始不能为0，在此对文件进行预处理
 * public void writeNetFile(HashMap<Integer, HashSet<Integer>> graphMap, String filePath) 将 HashMap<Integer, HashSet<Integer>> graphMap 中的节点及其邻居写进文件，并且文件格式为.net
 * 这个程序与项目中的 project.densestSubgraph 中的包： project.densestSubgraph 类： ProcessGraphFile
 * 这个只有其中两个函数，经过测试，源程序正确，如果需要写文件，就需要到 ProcessGraphFile 中找 writeFile();
 * 
 * 另外，增加了两个函数，是为了读写resultMap: readResultMap, writeResultMap
 * public void writeResultMap(String writeMapPath, HashMap<Integer,HashSet<Integer>> resultMap)
 * public HashMap<Integer,HashSet<Integer>> readResultMap(String writeMapPath)
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PreProcessFile {
		// 只需要使用一个graphMap就能存储所有信息，适用于 节点对重复的无向图symmetric，适用与两种情况的无向图，也就是无重复和重复出现的边，总之，是无向图，就可以使用此方法处理
		public HashMap<Integer, HashSet<Integer>> symUndirGraph(String filePath)
				throws IOException {
			HashMap<Integer, HashSet<Integer>> graphMap = new HashMap<Integer, HashSet<Integer>>();

			File srcFile = new File(filePath);
			System.out.println("file path: " + filePath + " ,\tlength: " + srcFile.length());
			BufferedReader readBuff = new BufferedReader(new FileReader(srcFile));

			int edgeCount = 0;

			String str;
			while ((str = readBuff.readLine()) != null) {
				edgeCount++;
				// 这句是为了防止能读到下一行，但是下一行又没有数据。
				if(str.length() < 3 ){
					break;
				}
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
				
//				System.out.println("graphMap.size:　" + graphMap.size());
			}
			readBuff.close();
			System.out.println("graph.edge: edgeCount: " + edgeCount);

			int vertexNum = graphMap.size();
			int edgeNum = getEdgeCount(graphMap);
			System.out.println("graph -> vertex: " + vertexNum + "\tgraph -> edge: " + edgeNum/2);
			return graphMap;
		}
	
	
		// 处理无向图，针对的是文件中不重复的节点对 asymmetric
		// 也可以处理有向图，
		// 因为有向无向图文件，但是这个无向图必须是 0 1， 1 0 这种对称的，都是只需要处理前一个key值，第二个值只需要加入到该键值的邻居集合中就行。
		public HashMap<Integer, HashSet<Integer>> asymGraph(String filePath)
				throws IOException {
			HashMap<Integer, HashSet<Integer>> graphMap = new HashMap<Integer, HashSet<Integer>>();

			File srcFile = new File(filePath);
			System.out.println("file path: " + filePath + " ,length: " + srcFile.length());
			BufferedReader readBuff = new BufferedReader(new FileReader(srcFile));

			int edgeCount = 0;

			String str = new String("");
			while ((str = readBuff.readLine()) != null) {
				edgeCount++;
				// 这句是为了防止能读到下一行，但是下一行又没有数据。
				if(str.length() < 3 ){
					break;
				}
				String[] strArr = str.split(" |\t");
				int firstVertex = Integer.parseInt(strArr[0]);
				int secondVertex = Integer.parseInt(strArr[1]);

				if ( graphMap.keySet().contains(firstVertex) ){
					// 图的键集合值包含第一个元素，分两步进行： 1.将第二个元素加入第一个元素的邻居节点集合中；
					graphMap.get(firstVertex).add(secondVertex);

				}else {
					// 图的键集合值均不包含两个元素，因此处理方式是：
					// 将第一个元素加入图的键集合，而且将第二个元素加入第二个元素的邻居节点集合中
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

	
	// 为了符合.net文件的特点，就必须节点排好序，而且节点开始不能为0，在此对文件进行预处理
	public void changeFile(String sfpath, String dfpath) throws IOException{
		File sffile = new File(sfpath);
		File dsfile = new File(dfpath);
		BufferedReader buffR = new BufferedReader(new FileReader(sffile));
		BufferedWriter buffW = new BufferedWriter(new FileWriter(dsfile));
		
		String str = new String("");
		while( (str = buffR.readLine()) != null ){
			String[] arrStr = str.split(" |\t");
			for(int i = 0; i < arrStr.length; i ++){
				int key = Integer.parseInt(arrStr[i]) + 1;
				buffW.write(key + " ");
			}
			buffW.newLine();
		}
		buffR.close();
		buffW.close();
	}
	
	// 得出给定图的边数,也就是统计每个键所对应的邻居节点集合中元素的数量。
	public int getEdgeCount(HashMap<Integer, HashSet<Integer>> graphMap){
		int edgeCount = 0;
		Set<Integer> keySet = graphMap.keySet();
		
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			Integer key = (Integer) it.next();
			HashSet<Integer> valueSet = graphMap.get(key);
			edgeCount += valueSet.size();
		}
		
		return edgeCount;
	}
	
	// 将 HashMap<Integer, HashSet<Integer>> graphMap 中的节点及其邻居写进文件，并且文件格式为.net
	public void writeNetFile(HashMap<Integer, HashSet<Integer>> graphMap, String filePath) throws IOException{
		File file = new File(filePath);               //记录文件
		TreeSet<Integer> sortKeySet = new TreeSet<Integer>(); // sortKeySet 存放 graphMap的键,因为.net文件的特点，就必须节点排好序，而且节点开始不能为0
		sortKeySet.addAll(graphMap.keySet());                 // 为了让键值排序
		int graphSize = graphMap.size();              // 记录graphMap.size();  
		BufferedWriter buffW = new BufferedWriter(new FileWriter(file));
		String str = new String("");
		str = "*Vertices " +  graphSize;
		buffW.write(str);
		buffW.newLine();
		
		Iterator firIt = sortKeySet.iterator();
		while(firIt.hasNext()){
			Integer key = (Integer)firIt.next();
			str = key + " \"" + key + "\"";
			buffW.write(str);
			buffW.newLine();
		}
		
		int edgeCount = getEdgeCount(graphMap);
		
		str = "*Edges " + edgeCount;
		buffW.write(str);
		buffW.newLine();
		
		Iterator secIt = sortKeySet.iterator();
		while(secIt.hasNext()){
			Integer key = (Integer)secIt.next();
			ArrayList<Integer> valueList = new ArrayList<Integer>();
			valueList.addAll(graphMap.get(key));
			for(int j = 0; j < valueList.size(); j ++){
				str = key + " " + valueList.get(j);
				buffW.write(str);
				buffW.newLine();
			}
		}
		
		buffW.close();
	}
	
	// 为了适应.net文件格式，将resultMap进行转换，得到一个新的resultMap，将键转换为 1 2 3 的形式
	public HashMap<Integer,HashSet<Integer>> convertResultMap(HashMap<Integer,HashSet<Integer>> resultMap){
		HashMap<Integer,HashSet<Integer>> convertMap = new HashMap<Integer,HashSet<Integer>>();
		
		HashMap<Integer, Integer> keyLabelMap = new HashMap<Integer, Integer>(); // 存放 <key,label>
		Set<Integer> resultKeySet =  resultMap.keySet();
		Iterator it = resultKeySet.iterator();
		int vertexLabel = 0;
		while(it.hasNext()){
			vertexLabel ++;
			Integer itKey = (Integer) it.next();
			keyLabelMap.put(itKey, vertexLabel);
		}
		
		Iterator its = resultKeySet.iterator();
		while(its.hasNext()){
			ArrayList<Integer> valueList = new ArrayList<Integer>();
			HashSet<Integer> convertSet = new HashSet<Integer>();
			Integer itsKey = (Integer) its.next();      // resultMap key
			valueList.addAll(resultMap.get(itsKey));
			
			// 遍历原图 result 中每个 键 对应的 值集合，得出 对应值的 标签
			for(int i = 0; i < valueList.size(); i ++){
				int tmpValue = keyLabelMap.get(valueList.get(i));
				convertSet.add(tmpValue);       // convertMap valueSet
			}
			int convertKey = keyLabelMap.get(itsKey);
			convertMap.put(convertKey, convertSet);
		}
		
		return convertMap;
	}
	
	// 将resultMap写进文件，格式为： 节点数， 边数，接下来每行都是一条边
	public void writeDiaResultMap(String writeMapPath, HashMap<Integer,HashSet<Integer>> resultMap) throws IOException{
		File file = new File(writeMapPath);
		Set<Integer> keySet = resultMap.keySet();
		int verNum = resultMap.size();
		Iterator sit = keySet.iterator();
		int edges = 0;
		while(sit.hasNext()){
			Integer svalue = (Integer)sit.next();
			edges += resultMap.get(svalue).size();
		}
		edges = edges / 2;
		BufferedWriter buffW = new BufferedWriter(new FileWriter(file));
		String firstLine = verNum + "\t" + edges;
		buffW.write(firstLine);
		buffW.newLine();
		
		Iterator firIt = keySet.iterator();
		while(firIt.hasNext()){
			String str = new String("");
			int key = (Integer)firIt.next();
			
			HashSet<Integer> hashSet = resultMap.get(key);
			Iterator secIt = hashSet.iterator();
			while(secIt.hasNext()){
				int value = (Integer) secIt.next();
//				str += value + " ";
				str = key + "\t" + value ;
				buffW.write(str);
				buffW.newLine();
			}
			
		}
		buffW.close();
	}
	
	
	
	// 将 resultMap写进文件中，为了下一步恢复resultMap做准备
	// * public void writeResultMap(String writeMapPath, HashMap<Integer,HashSet<Integer>> resultMap)
	public void writeResultMap(String writeMapPath, HashMap<Integer,HashSet<Integer>> resultMap) throws IOException{
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
		buffW.close();
	}
	
	// 与上个函数相对应，该函数是为了将图文件恢复成图数据结构HashMap<Integer,HashSet<Integer>> 
	// * public HashMap<Integer,HashSet<Integer>> readResultMap(String writeMapPath)
	public HashMap<Integer,HashSet<Integer>> readResultMap(String writeMapPath) throws IOException{
		HashMap<Integer,HashSet<Integer>> resultMap = new HashMap<Integer,HashSet<Integer>>();
		
		File file = new File(writeMapPath);
		BufferedReader buffR = new BufferedReader(new FileReader(file));
		
		String str = "";
		while( (str = buffR.readLine()) != null ){
			
			HashSet<Integer> valueSet = new HashSet<Integer>();
			String tmpStr = str.trim();
			if(tmpStr.length() <= 2){
				continue;
			}
			String[] arrStr = tmpStr.split(" ");
			
			int key = Integer.parseInt(arrStr[0]);
			for(int i = 1; i < arrStr.length; i ++){
				valueSet.add( Integer.parseInt(arrStr[i]) );
			}
			resultMap.put(key, valueSet);
		}
		buffR.close();
		return resultMap;
	}
}

