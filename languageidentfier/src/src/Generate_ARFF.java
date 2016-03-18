import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;


public class Generate_ARFF 
{
	public static TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>();
	public static TreeMap<String,Integer> sorted_map_words = new TreeMap<String,Integer>();

	
    public static void read()
    {
    	try
        {
            //FileInputStream fis = new FileInputStream("Trigram_1000.txt");
    		FileInputStream fis = new FileInputStream("Trigram_20.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String line;
            while((line = br.readLine())!=null)
            {
            	sorted_map.put(line.trim().substring(0,line.indexOf("\t")).trim(),0);
            }
            fis.close();
        }catch(IOException f){}  
    }
    public static void read_words()
    {
    	int k=0;
    	try
        {
    		FileInputStream fis = new FileInputStream("Words.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String line;
            while((line = br.readLine())!=null)
            {
            	String words[]=line.split("\t");
            	sorted_map_words.put(words[0].trim(),k++);
            }
            fis.close();
        }catch(IOException f){}  
    }
    public static String vector_represent(String word)
    {
    	String vector="";
    	
    	TreeMap<String,Integer> vctr = new TreeMap<String,Integer>();
    	vctr.putAll(sorted_map);

    	
    	char[] y=word.toCharArray();
        
        //System.out.println(word+"\t"+tag);

        for(int j=0;j<y.length-2;j++)
        {
       	 	String x=y[j]+""+y[j+1]+""+y[j+2]+"";
       	 	
       	 	if(vctr.containsKey(x))
       	 	{
       	 		int k=vctr.get(x);
       	 		if(k==1)
       	 		{
       	 		}
       	 		else
       	 		{
	       	 		k=k+1;
	       	 		vctr.put(x, k);
       	 		}
       	 	}
        }  
        
		for (Map.Entry entry : vctr.entrySet()) 
		{
			vector+=entry.getValue()+",";
		}
		
		System.out.println(vector);

    	return vector;
    }
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		int l=0,cntr=0;;
		read();
		read_words();
		Global.file_append("Vector.arff","@relation Language");
		

		for (Map.Entry entry : sorted_map.entrySet()) 
		{
			//System.out.println(entry.getKey());
			Global.file_append("Vector.arff","@ATTRIBUTE WORD"+l+"  NUMERIC");
			l++;
		}
		Global.file_append("Vector.arff","@ATTRIBUTE WORD_ORI NUMERIC");

		
		StringBuffer str=new StringBuffer();
		
		Global.file_append("Vector.arff","@ATTRIBUTE class        {acro,univ,ne,en,hi,undef}");

		Global.file_append("Vector.arff","@DATA");
		try
        {
            FileInputStream fis = new FileInputStream("English_Hindi.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String line;
            while((line = br.readLine())!=null)
            {
            	cntr++;
            	if(cntr==100)
            	{
            		break;
            	}
                String words[]=line.toLowerCase().split(" ");
                
                for(int i=0;i<words.length;i++)
                {
	                 String word=words[i].substring(0,words[i].indexOf("£"));
	                 String tag=words[i].substring(words[i].indexOf("£")+1);
	                 
	                 if(tag.equals("hi+en_suffix"))
	                	 tag="hi";
	                 else if(tag.equals("acro+en_suffix"))
	                	 tag="acro";
	                 else if(tag.equals("ne+en_suffix"))
	                	 tag="ne";
	                 else if(tag.equals("en+hi_suffix"))
	                	 tag="en";
	                 else if(tag.equals("ne+hi_suffix"))
	                	 tag="ne";
	                 else if(tag.trim().length()==0)
	                	 tag="undef";
	                 
	                 int x=0;
	                 
	                 if(sorted_map_words.containsKey(word))
	                	 x=sorted_map_words.get(word);
	                 //System.out.println(x+"######"+word);
	                 
	                 Global.file_append("Vector.arff",vector_represent(word)+","+x+","+tag);
	                 //Global.file_append("Vector.arff",vector_represent(word)+","+tag);
	            }
	            //System.out.println("==================================");
            }
            fis.close();		
        }catch(IOException f){} 
		
		//Global.file_update("Vector.txt", str.toString());

	}

}
