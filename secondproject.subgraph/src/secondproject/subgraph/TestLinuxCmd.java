package secondproject.subgraph;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestLinuxCmd {
	public static void main(String[] args) {
		
		try{	
			long startTime = System.currentTimeMillis();
//			String cmdStrs[] = new String[]{"/bin/bash","-c","ll","-a"};  //"/bin/bash","-c",
			String graphPath = "/home/hadoop/桌面/graphmap/Email-Enron.net";
			graphPath = graphPath + " 5";
			String strcmd = "~/infomap 345234 " + graphPath ;
			
			String cmdStrs[] = new String[]{"/bin/sh","-c",strcmd};
			Process child = Runtime.getRuntime().exec(cmdStrs);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(child.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			br.close();
//			String result = sb.toString();
//			System.out.println(result);
			long runTime = System.currentTimeMillis() - startTime;
			System.out.println("run time:" + runTime);
			}catch(Exception e){
//			LogUtil.LogCommInfo("修改权限异常 = "+e);
			e.printStackTrace();
			}
		
	}
}
