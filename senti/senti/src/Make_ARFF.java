import java.io.*;
import java.util.*;


public class Make_ARFF 
{
	public static HashMap<String,Integer> list=new HashMap<String,Integer>();
	public static void read_list()
	{
		try
        {
            FileInputStream fis = new FileInputStream("HI/BI.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String line;
            while((line = br.readLine())!=null)
            {
            	String words[]=line.split("\t");
            	list.put(words[0], 0);
            }
            fis.close();
        }catch(IOException f){}    
	}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		read_list();
		Global.file_append("Vector.arff","@relation Sentiment");
		int l=0;
		
		for (Map.Entry entry : list.entrySet()) 
		{
			//System.out.println(entry.getKey());
			Global.file_append("Vector.arff","@ATTRIBUTE BI"+l+"  NUMERIC");
			l++;
		}
		Global.file_append("Vector.arff","@ATTRIBUTE class        {Positive,Negative,Neutral}");

		Global.file_append("Vector.arff","@DATA");
		
		try
        {
            FileInputStream fis = new FileInputStream("HI/HI_SENTI.TXT");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String line,tag="";
            while((line = br.readLine())!=null)
            {
            	HashMap<String,Integer> feature=new HashMap<String,Integer>();
            	feature.putAll(list);

            	if(!line.startsWith("================="))
            	{
	            	String words[]=line.split("\t");
	            	
	            	String x=Twokenize.tokenized(words[1]);
	            	
	            	String x_words[]=x.split(" ");
	            	tag=x_words[x_words.length-1].replaceAll("#", "").replaceAll("@", "");
	            	
	            	for(int i=0;i<x_words.length-2;i++)
	            	{
	            		String y=x_words[i]+"_"+x_words[i+1];
	            		if(feature.containsKey(y))
	            		{
	            			feature.put(y, 1);
	            		}
	            	}
	            	
	            	StringBuffer str=new StringBuffer();
	            	
	        		for (Map.Entry entry : feature.entrySet()) 
	        		{
	        		    System.out.print(entry.getValue()+",");
	        		    str.append(entry.getValue()+",");
	        		}
        		    str.append(tag);
	        		System.out.println(tag);
	        		Global.file_append("Vector.arff",str.toString());
            	}
            }
            fis.close();
        }catch(IOException f){}    
	}
}
