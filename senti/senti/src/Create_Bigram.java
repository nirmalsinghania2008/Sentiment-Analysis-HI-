import java.io.*;
import java.util.*;


public class Create_Bigram 
{
	public static void main(String args[])
	{
		HashMap<String,Integer> list=new HashMap<String,Integer>();
        ValueComparator bvc =  new ValueComparator(list);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

        
		try
        {
            FileInputStream fis = new FileInputStream("HI/neu.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String line;
            while((line = br.readLine())!=null)
            {
            	String words[]=line.split("\t");
            	String x=Twokenize.tokenized(words[1]);
            	
            	String x_words[]=x.split(" ");
            	
            	for(int i=0;i<x_words.length-2;i++)
            	{
            		String y=x_words[i]+"_"+x_words[i+1];
            		if(list.containsKey(y))
            		{
            			int k=list.get(y);
            			k+=1;
            			list.put(y, k);
            		}
            		else
            		{
            			list.put(y, 1);
            		}
            	}
            }
            fis.close();
        }catch(IOException f){} 
		
        sorted_map.putAll(list);
		
		for (Map.Entry entry : sorted_map.entrySet()) 
		{
		    System.out.println(entry.getKey() + ", " + entry.getValue());
		    Global.file_append("HI/neu_bi.txt",entry.getKey() + "\t" + entry.getValue());
		}
	}
}


class ValueComparator implements Comparator<String> 
{

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) 
    {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) 
    {
        if (base.get(a) >= base.get(b)) 
        {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

