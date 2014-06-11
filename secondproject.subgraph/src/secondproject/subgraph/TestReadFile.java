package secondproject.subgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestReadFile {
	public static void main(String[] args) throws IOException {
		String filePath = new String("/home/hadoop/桌面/回顾.txt");
		File file = new File(filePath);
		BufferedReader buffR = new BufferedReader(new FileReader(file));
		
		String readLabel = new String("3.");
		String str = new String("");
		
//		int numCount = 0;
//		int numLabel = 0;
		while(!(str = buffR.readLine()).contains(readLabel));
		while((str = buffR.readLine()) != null){
//			System.out.println(str);
			
//			if(str.contains("hadoop")){
//				int start = str.indexOf(0);
//				int end = str.indexOf(2);
				if(str.length() > 2){
					System.out.println(str.substring(0, 1));
				}
				
//			}
			
		}
		buffR.close();
		
	}
}
