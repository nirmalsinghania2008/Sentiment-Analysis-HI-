import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.classifiers.functions.SMO;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Lang_Detect {
	
	public static TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>();
	public static TreeMap<String,Integer> sorted_map_words = new TreeMap<String,Integer>();

	public static weka.classifiers.Classifier c;
	
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
    public static RandomForest loadModel(String path) throws Exception 
	{

	    /*SMO classifier;

	    FileInputStream fis = new FileInputStream(path);
	    ObjectInputStream ois = new ObjectInputStream(fis);

	    classifier = (SMO) ois.readObject();
	    ois.close();

	    return classifier;*/
    	RandomForest classifier;

	    FileInputStream fis = new FileInputStream(path);
	    ObjectInputStream ois = new ObjectInputStream(fis);

	    classifier = (RandomForest) ois.readObject();
	    ois.close();

	    return classifier;
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
		
		//System.out.println(vector);
		//vector=word+","+vector;

    	return vector;
    }

    public static void initiate() throws Exception
    {
		read();
		read_words();
	    c = loadModel("Model/RF.model"); // loads smo model
    }
    public static String lang_tag(String sentence) throws Exception
    {
    	sentence=Twokenize.tokenized(sentence);
    	
    	String tagged_sentence="";
		int l=0,cntr=0;;
        String words[]=sentence.toLowerCase().split(" ");
		StringBuffer str=new StringBuffer();

        str.append("@relation Language\n");
		

		for (Map.Entry entry : sorted_map.entrySet()) 
		{
			//System.out.println(entry.getKey());
			str.append("@ATTRIBUTE WORD"+l+"  NUMERIC\n");
			l++;
		}
		
		str.append("@ATTRIBUTE WORD_ORI NUMERIC\n");

		str.append("@ATTRIBUTE class        {acro,univ,ne,en,hi,undef}\n");

		str.append("@DATA\n");
        
        for(int i=0;i<words.length;i++)
        {
        	int x=0;
            
            if(sorted_map_words.containsKey(words[i]))
           	 x=sorted_map_words.get(words[i]);
            
            str.append(vector_represent(words[i])+","+x+",?\n");
        }
        Global.file_update("unlabeled.arff",str.toString());
        
	    Instances unlabeled = new Instances(
                new BufferedReader(
                  new FileReader("unlabeled.arff")));

	    // set class attribute
	    unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

	    Instances labeled = new Instances(unlabeled);
	    
	    // label instances
	    for (int i = 0; i < unlabeled.numInstances(); i++) 
	    {
	      double clsLabel = c.classifyInstance(unlabeled.instance(i));
	      String tag="";
	      
	      if(clsLabel==0.0)
	    	  tag="acro";
	      else if(clsLabel==1.0)
	    	  tag="univ";
	      else if(clsLabel==2.0)
	    	  tag="ne";
	      else if(clsLabel==3.0)
	    	  tag="en";
	      else if(clsLabel==4.0)
	    	  tag="hi";
	      else if(clsLabel==5.0)
	    	  tag="undef";
	      //System.out.println(words[i]+"/"+tag);
	      
	      //if(words[i].contains(".") || )
	      Pattern pat = Pattern.compile("[:?!@#$%^&*().]");
	      Matcher m = pat.matcher(words[i]);
	      if (m.find()) 
	      {
		      tagged_sentence+=words[i]+"/"+"univ ";
	      }
	      else
	      {
	    	  tagged_sentence+=words[i]+"/"+tag+" ";
	      }
	      //labeled.instance(i).setClassValue(clsLabel);
	    }
    	return tagged_sentence.trim();
    }
	public static void main(String[] args) throws Exception 
	{
		initiate();
		System.out.println(lang_tag("love u all khud ne kha nd THARKI doosre ko keh rha hai :D"));
	}
}
