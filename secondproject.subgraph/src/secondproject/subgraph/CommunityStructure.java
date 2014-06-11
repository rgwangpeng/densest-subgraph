package secondproject.subgraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommunityStructure {
	public static void detectCommunity(String mapfilePath){
		String cmdStr = "~/infomap 345234 " + mapfilePath + " 3"; // 其中infomap是.exe执行文件，345234是一个随机种子，接着是.net文件名，接着是迭代次数
		String[] cmd = new String[]{"/bin/sh","-c",cmdStr};
		try {
			Process child = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(child.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
