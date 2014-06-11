package secondproject.subgraph;

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
import java.util.Set;

public class CountFileLine {
	
	public static void main(String[] args) throws IOException {
		
//		String srcPath = "D:\\densestSubgraph\\论文所用到的数据集\\liveJ\\com-lj.ungraph.txt";
//		String srcPath = "D:\\densestSubgraph\\论文所用到的数据集\\liveJournal\\soc-LiveJournal1.txt";
//		String dstPath = "D:\\densestSubgraph\\论文所用到的数据集\\liveJournal\\dstLiveJ.txt";
		String srcPath = "C:\\Users\\wp\\Desktop\\vlbd\\dt4.txt";
		String dstPath = "C:\\Users\\wp\\Desktop\\vlbd\\dt4result.txt";
		
		
		filterFile(srcPath, dstPath);
		
//		getSameName();
//		 countLine();
//		overWriteFile();
//		rewriteFile();
//		long strartTime = System.currentTimeMillis();
//		removeDescribe(srcPath, dstPath);
//		long endTime =  System.currentTimeMillis() - strartTime;
//		System.out.println("endTime: " + endTime);
//		testRead();
	}
	
	// 
	public static void filterFile(String srcPath, String dstPath) throws IOException{
		File sffile = new File(srcPath);
		File dsfile = new File(dstPath);
		BufferedReader buffR = new BufferedReader(new FileReader(sffile));
		BufferedWriter buffW = new BufferedWriter(new FileWriter(dsfile));
		
		int count = 0;
		int max = 0;
		String str = new String("");
		while( (str = buffR.readLine()) != null ){
			if(!str.contains("N")){
				continue;
			}
			String[] arrStr = str.split("N");
			String value1 = arrStr[1].substring(0,arrStr[1].indexOf("\"") );
			String value2 = arrStr[2].substring(0,arrStr[2].indexOf("\"") );
			
			System.out.println("value1: " + value1 + " , value2: " + value2);
			
			value1 = new String( String.valueOf(Integer.parseInt(value1) + 1) );
			value2 = new String( String.valueOf(Integer.parseInt(value2) + 1) );
			
			count ++;
			String strS = value1 + "\t" + value2;
			
			buffW.write(strS);
			buffW.newLine();
		}
		buffR.close();
		buffW.close();
		
		System.out.println("count: " + count);
	}
	
	
	public static void testRead() throws IOException{
		String srcPath = "D:\\densestSubgraph\\论文所用到的数据集\\liveJournal\\soc-LiveJournal1.txt";
		File sffile = new File(srcPath);
		BufferedReader buffR = new BufferedReader(new FileReader(sffile));
		
		int count = 0;
		int max = 0;
		String str = new String("");
		while( (str = buffR.readLine()) != null ){
			System.out.println("str:　" + str);
			count ++;
			
			String[] arrStr = str.split(" |\t");
			if(arrStr.length == 2){
				System.out.println("count: " + count);
				break;
			}
		}
		buffR.close();
		
		System.out.println("count: " + count + "\tmax:" + max);
	}
	
	// 为了避免中间空缺，或者开始结点标号过大
	public static void rewriteFile() throws IOException{
		String srcPath = "D:\\densestSubgraph\\论文所用到的数据集\\liveJ\\ljungraph.txt";
		String dstPath = "D:\\densestSubgraph\\论文所用到的数据集\\liveJ\\rewljungraph.txt";
		
		PreProcessFile ppf = new PreProcessFile();
		HashMap<Integer, HashSet<Integer>> graphMap = ppf.symUndirGraph(srcPath); // 得出与相对应的HashMap
		
		// 设置每个结点对应的结点名称
		Set<Integer> keySet = graphMap.keySet(); 
		int VAL = 1; 
        HashMap<Integer,Integer> hm = new HashMap<Integer,Integer>();
        for(int i:keySet)
        {
            hm.put(i, VAL);
            VAL++;
        }
		
		int vertexNum = graphMap.size();
		int edgeNum =  ppf.getEdgeCount(graphMap)/2;
		String fristLine = vertexNum + "\t" + edgeNum;
		System.out.println("fristLine: " + fristLine);
		File dsfile = new File(dstPath);
		BufferedWriter buffW = new BufferedWriter(new FileWriter(dsfile));
		
		buffW.write(fristLine);
		buffW.newLine();
		
		String writeLine = new String("");
        for(int i:keySet)
        {
        	int iname = hm.get(i);
            for(int id:graphMap.get(i))
            {
            	
            	int idname = hm.get(id);
            	writeLine = iname + "\t" + idname;
            	buffW.write(writeLine);
        		buffW.newLine();
            }
        }
		
		buffW.close();
	}
	
	// 不用打开文件，直接去除那些数据文件的描述内容
	public static void  removeDescribe(String srcPath, String dstPath) throws IOException{

		File sffile = new File(srcPath);
		File dsfile = new File(dstPath);
		BufferedReader buffR = new BufferedReader(new FileReader(sffile));
		BufferedWriter buffW = new BufferedWriter(new FileWriter(dsfile));
		
		int count = 0;
		int max = 0;
		String str = new String("");
		while( (str = buffR.readLine()) != null ){
			count ++;
			String[] arrStr = str.split(" |\t");
			if( (arrStr.length != 2) || (str.contains("#")) ){
				continue;
			}
			String strS = "";
			for(int i = 0; i < arrStr.length; i ++){
				int key = Integer.parseInt(arrStr[i]);
				if(key > max){
					max = key;
				}
				
				strS += key + " ";
			}
			buffW.write(strS.substring(0,strS.length() - 1));
			buffW.newLine();
		}
		buffR.close();
		buffW.close();
		
		System.out.println("count: " + count + "\tmax:" + max);
	}
	
	public static void overWriteFile() throws IOException{
		String srcPath = "D:\\densestSubgraph\\论文所用到的数据集\\ca-AstroPh\\CA-AstroPh.txt";
		String dstPath = "D:\\densestSubgraph\\论文所用到的数据集\\ca-AstroPh\\dstCA-AstroPh.txt";
		File sffile = new File(srcPath);
		File dsfile = new File(dstPath);
		BufferedReader buffR = new BufferedReader(new FileReader(sffile));
		BufferedWriter buffW = new BufferedWriter(new FileWriter(dsfile));
		
		String tmpwStr = "425876 198080";
		
		buffW.write(tmpwStr);
		buffW.newLine();
		
		int count = 0;
		int max = 0;
		String str = new String("");
		while( (str = buffR.readLine()) != null ){
			count ++;
			String[] arrStr = str.split(" |\t");
			String strS = "";
			for(int i = 0; i < arrStr.length; i ++){
				int key = Integer.parseInt(arrStr[i]) + 1;
				if(key > max){
					max = key;
				}
				
				strS += key + " ";
			}
			buffW.write(strS.substring(0,strS.length() - 1));
			buffW.newLine();
		}
		buffR.close();
		buffW.close();
		
		System.out.println("count: " + count + "\tmax:" + max);
		
	}
	
	
	
	
	
	public static void countLine() throws IOException{
		String srcPath = "D:\\densestSubgraph\\论文所用到的数据集\\football\\football.txt";
		File srcFile = new File(srcPath);
		BufferedReader buffR = new BufferedReader(new FileReader(srcFile));
		
		
//		String dstPath = "C:\\Users\\wp\\Desktop\\dstjkmx.txt";
//		File dstFile = new File(dstPath);
//		BufferedWriter buffW = new BufferedWriter(new FileWriter(dstFile));
		
		String readStr = new String("");
		int count = 0;
		while( (readStr = buffR.readLine()) != null ){
			
			count ++;
			
//			String writeStr = new String("");
//			String[] strArr = readStr.split(" +|\t");
//			for(int i = 0; i < strArr.length - 1; i ++){
//				writeStr += strArr[i] + " ";
//			}
//			writeStr += strArr[strArr.length - 1];
//			buffW.write(writeStr);
//			buffW.newLine();
		}
		
		System.out.println("count : " + count);
	
//		buffR.close();
//		buffW.close();
	}
	
	public static void getSameName() throws IOException{
		String filePath1 = new String("C:\\Users\\wp\\Desktop\\tmpTxt2.txt");
		String filePath2 = new String("C:\\Users\\wp\\Desktop\\tmpTxt.txt");
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		
		BufferedReader buffR1 = new BufferedReader(new FileReader(file1));
		BufferedReader buffR2 = new BufferedReader(new FileReader(file2));
		
		ArrayList<String> nameL1 = new ArrayList<String>();
		ArrayList<String> nameL2 = new ArrayList<String>();
		
		String strName1 = new String("");
		while( (strName1 = buffR1.readLine()) != null ){
			nameL1.add(strName1);
		}
		buffR1.close();
		
		String strName2 = new String("");
		while( (strName2 = buffR2.readLine()) != null ){
			nameL2.add(strName2);
		}
		buffR2.close();
		
		for(int i = 0; i < nameL1.size(); i ++){
			if(nameL2.contains(nameL1.get(i))){
				System.out.println("same name: " + nameL1.get(i));
			}
		}
		
	}
}
