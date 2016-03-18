import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class Extract {

	public static void main(String[] args) 
	{
		HashMap<String,Integer> tri=new HashMap<String,Integer>();
        ValueComparator bvc =  new ValueComparator(tri);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        
		HashMap<String,Integer> word_list=new HashMap<String,Integer>();
        ValueComparator bvc1 =  new ValueComparator(word_list);
        TreeMap<String,Integer> sorted_map1 = new TreeMap<String,Integer>(bvc1);

		HashMap<String,String> tag_list=new HashMap<String,String>();


		 try
         {
             FileInputStream fis = new FileInputStream("English_Hindi.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
             String line;
             while((line = br.readLine())!=null)
             {
                 String words[]=line.toLowerCase().split(" ");
                 
                 for(int i=0;i<words.length;i++)
                 {
	                 String word=words[i].substring(0,words[i].indexOf("£"));
	                 String tag=words[i].substring(words[i].indexOf("£")+1);
	                 
	                 tag_list.put(tag, "");
	                 if(word_list.containsKey(word))
	                 {
	                	 int k=word_list.get(word);
	                	 k=k+1;
	                	 word_list.put(word, k);
	                 }
	                 else
	                 {
	                	 word_list.put(word, 1);
	                 }
	                 
	                 char[] y=word.toCharArray();
	                 
	                 //System.out.println(word+"\t"+tag);

	                 for(int j=0;j<y.length-2;j++)
	                 {
	                	 String x=y[j]+""+y[j+1]+""+y[j+2]+"";
	                	 //System.out.println(word.charAt(j)+word.charAt(j+1)+word.charAt(j+2));
	                	 //System.out.println(x);
	                	 
	                	 if(tri.containsKey(x))
	                	 {
	                		int k=tri.get(x);
	                		k=k+1;
	                		tri.put(x, k);
	                	 }
	                	 else
	                	 {
	                		 tri.put(x, 1);
	                	 }
	                 }
	                 //System.out.println("==================================");
                 }
             }
             fis.close();		
         }catch(IOException f){} 
		 
		 sorted_map.putAll(tri);
		 sorted_map1.putAll(word_list);

		 
		 StringBuffer str=new StringBuffer();
		 
		 for (Map.Entry entry : sorted_map.entrySet()) 
		 {
			    System.out.println(entry.getKey() + "\t " + entry.getValue());
			    str.append(entry.getKey() + "\t " + entry.getValue()+"\n");
		}
		 for (Map.Entry entry : tag_list.entrySet()) 
		 {
			 Global.file_append("Tags.txt", entry.getKey()+"");
		 }
		 for (Map.Entry entry : sorted_map1.entrySet()) 
		 {
			 Global.file_append("Words.txt", entry.getKey()+"\t"+entry.getValue());
		 }
		 Global.file_update("Trigram.txt", str.toString());
	}

}

class ValueComparator implements Comparator<String> 
{

    Map<String, Integer> base;
    public ValueComparator(HashMap<String, Integer> base) 
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
