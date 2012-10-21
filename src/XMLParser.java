import javax.xml.*;
import javax.xml.parsers.*;
import org.w3c.dom.*; // auto-import
import org.xml.sax.*;
import java.io.*;
import java.util.*;
/**
 * Contains a main method which parses all the XML files in a hardcoded subdirectory
 * and contains a bunch of static methods which processes this data.
 * 
 * Things to Analyze:
 * 1. In the (842) objects labeled 3 or more times, what kind of agreement to they have as a percentage?
 * 2. Graph where x axis is different object names and bars separated according to material, with heirarchies?
 * 3. Graph where x axis shows different objects of same names with the same graphing scheme as above.
 * 4. Take a look at Visipedia and LabelMe Analytics.
 */
public class XMLParser {
	
	public static ArrayList<ImageObject> images=new ArrayList<ImageObject>();
	
    public static void main (String args[]) {
        XMLParser.parseXmlFile("kitchenedit/");
    	writeIndividualObjectMaterialsWithEntropyToCSVFile();
    	writeCleansedXMLs();
    }
    
    /**
     * Parses all of the XML files in the subdirectory given
     * and stores the data
     */
    private static void parseXmlFile(String subdir){
    	File dir = new File(subdir);
    	//Getting all files in the directory dir
    	String[] children = dir.list();
    	//Storing only the xml files from this directory in another arraylist
    	ArrayList<String> xmls=new ArrayList<String>();
    	for (int i=0;i<children.length;i++) {
    		if (children[i].lastIndexOf('.')!=-1 && children[i].substring(children[i].lastIndexOf('.')+1).equals("xml")) {
    			xmls.add(children[i]);
    		}
    	}
    	//Parsing the xml files from the dir and printing output to console.
    	for (int j=0;j<xmls.size();j++) {
    		System.out.println(xmls.get(j));
         	images.add(new ImageObject(xmls.get(j),dir.getAbsolutePath()));
        	System.out.println("Done");
    	}
   }
    
    
    /** 
     * Writes the Cleansed form of the XMLs read
     * the cleansed form deletes redundant spacing, and numeric material or 
     * attribute labels
     */
    public static void writeCleansedXMLs () {
    	for (ImageObject i: images) {
    		i.makeFile();
    	}
    }
    
    /**
     * Writes the Raw form of the XMLs read
     * The raw form contains no material annotations or attribute annotation data
     * and just mere object labels
     */
    public static void writeRawXMLs () {
    	for (ImageObject i: images) {
    		i.makeFile();
    	}
    }
    
    
    /**
     * Writes All The Individual Objects with more than 10 material labels
     * to a csv file in the form
     * <No of Material Labels> <No of Attribute Labels> <No of Individual Polygons> <Entropy> <Object Name> <Details>
     */
    public static void writeIndividualObjectMaterialsWithEntropyToCSVFile () {
    	try {
    		//Initializing File Writer
			File file = new File("IndividualObjectsMaterialsWEntropy.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Writing File Header
			bw.write("Material,Attribute,Object,Entropy,Details\n");
			//Getting arraylis of unique object names
			ArrayList<LabelStuffObject> a=ImageCluster.getArrayListofObjectsClassifiedByName ();;
			int matsizetolerance=10;
			int othermatcount=0;
			int otherattribcount=0;
			int otherobjectcount=0;
			//Scanning through list of unique objects
	    	for (int i=0;i<a.size();i++) {
	    		//Processing unique object if they satisfy the material annotation number tolerance
	    		if (a.get(i).getMatSize()>matsizetolerance) {
	    			//Writing number of materials annotated, number of attributes annotated and number of polygons
		    		bw.write(a.get(i).getMatSize()+","+a.get(i).getAttSize()+","+a.get(i).getCount()+",");
		    		//Get an arraylist of the materials this object has been annotated with and their counts
		    		ArrayList<MaterialCount> mc=a.get(i).getOrderedMaterialCounts();
		    		//Printing the different materials, their counts and calculating entropy
		    		int othercount=0;
		    		int tolerance=Math.max(2,a.get(i).getMatSize()/50);
		    		double entropy=0;
		    		String tmp=a.get(i).getObjName()+",";
		    		for (int j=0;j<mc.size();j++) {
		    			if (mc.get(j).getCount()>tolerance) {
		    				
		    				tmp+=(mc.get(j).getCount()+" "+mc.get(j).getMaterial());
		    				tmp+=(",");
		    			}
		    			else {
		    				othercount+=mc.get(j).getCount();
		    			}
		    			double px=((double)mc.get(j).getCount())/a.get(i).getMatSize();
		    			entropy+=px*(Math.log(px)/Math.log(2));
		    			
		    		}
		    		tmp+=(othercount+" other");
		    		entropy=-entropy;
		    		bw.write((double)Math.round(entropy*1000)/1000+",\t"+tmp+"\n");
	    		}
	    		else {
	    			//Accumulating the statistics of objects that do not meet the tolerance criteria
	    			othermatcount+=a.get(i).getMatSize();
	    			otherattribcount+=a.get(i).getAttSize();
	    			otherobjectcount+=a.get(i).getCount();
	    		}
	    	}
	    	bw.write(othermatcount+","+otherattribcount+","+otherobjectcount+",Other Objects\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Writes All The Individual Objects with more than 10 material labels
     * to a csv file in the form
     * <No of Material Labels> <No of Attribute Labels> <No of Individual Polygons> <Object Name> <Material Details>
     */
    public static void writeIndividualObjectMaterialsToCSVFile () {
    	try {
    		//Initializing File Writer
			File file = new File("IndividualObjectsMaterials.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Writing File Header
			bw.write("Material,Attribute,Object,Name,Details\n");
			//Get individual objects in an arraylist
			ArrayList<LabelStuffObject> a=ImageCluster.getArrayListofObjectsClassifiedByName ();;
			int matsizetolerance=10;
			int othermatcount=0;
			int otherattribcount=0;
			int otherobjectcount=0;
			//Scanning through individual objects
	    	for (int i=0;i<a.size();i++) {
	    		//Checking for whether the material size satisfies the tolerance limit
	    		if (a.get(i).getMatSize()>matsizetolerance) {
	    			//Writing the material number, attribute number, polygon number and object name 
		    		bw.write(a.get(i).getMatSize()+","+a.get(i).getAttSize()+","+a.get(i).getCount()+","+a.get(i).getObjName()+",");
		    		//Get an ordered list of materials and counts for each object
		    		ArrayList<MaterialCount> mc=a.get(i).getOrderedMaterialCounts();
		    		//Writing the material counts for this object
		    		int othercount=0;
		    		int tolerance=Math.max(2,a.get(i).getMatSize()/50);
		    		for (int j=0;j<mc.size();j++) {
		    			if (mc.get(j).getCount()>tolerance) {
		    				
		    				bw.write(mc.get(j).getCount()+" "+mc.get(j).getMaterial());
		    				bw.write(",");
		    			}
		    			else {
		    				othercount+=mc.get(j).getCount();
		    			}
		    			
		    		}
		    		bw.write(othercount+" other");
		    		bw.write("\n");
	    		}
	    		else {
	    			othermatcount+=a.get(i).getMatSize();
	    			otherattribcount+=a.get(i).getAttSize();
	    			otherobjectcount+=a.get(i).getCount();
	    		}
	    	}
	    	bw.write(othermatcount+","+otherattribcount+","+otherobjectcount+",Other Objects\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Writes All The Individual Objects with more than 10 material labels
     * to a csv file in the form
     * <No of Material Labels> <No of Attribute Labels> <No of Individual Polygons> <Object Material Name> <Details>
     * So that it can be made a Stacked Graph in Excel with appropriate color coding scheme
     */
    public static void writeIndividualObjectsToCSVFileToEnableStackedBarGraphCreationWithProperColors() {
    	try {
    		//Initializes the File Writer
			File file = new File("IndividualObjectsExcelBarGraphProperColorCoding.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Writes the File Header
			bw.write("Material,Attribute,Object,Name");
			//Get arraylist of individual objects, ie, objects with distinct names
			ArrayList<LabelStuffObject> a=ImageCluster.getArrayListofObjectsClassifiedByName ();;
			int matsizetolerance=10;
			int othermatcount=0;
			int otherattribcount=0;
			int otherobjectcount=0;
			//Collecting all the material names that exist throughout the objects that fulfill the tolerance limit
			ArrayList<String> material=new ArrayList<String>();
			for (int i=0;i<a.size();i++) {
				ArrayList<MaterialCount> mc=a.get(i).getUnorderedMaterialCounts();
	    		if (a.get(i).getMatSize()>matsizetolerance) {
	    			int tolerance=Math.max(2,a.get(i).getMatSize()/50);
	    			for (int j=0;j<mc.size();j++) {
		    			if (mc.get(j).getCount()>tolerance && !material.contains(mc.get(j).getMaterial()))
		    				material.add(mc.get(j).getMaterial());
		    		}
	    		}
			}	
			//Sorting the materials collected
			Collections.sort(material);
			//Writing the header full of materials
			for (int i=0;i<material.size();i++) {
				bw.write(","+material.get(i));
			}
			bw.write(",other\n");
			//Scanning through all the ImageClusters, ie, images with distinct primary filenames
	    	for (int i=0;i<a.size();i++) {
	    		//Check whether material size exceeds the tolerance stated
	    		if (a.get(i).getMatSize()>matsizetolerance) {
	    			//Printing the Material annotation number, attribute annotation number, number of distinct polygons, object name
		    		bw.write(a.get(i).getMatSize()+","+a.get(i).getAttSize()+","+a.get(i).getCount()+","+a.get(i).getObjName()+",");
					//Putting the material frequencies under the correct header so they 
		    		// color code properly in Excel
		    		ArrayList<MaterialCount> mc=a.get(i).getUnorderedMaterialCounts();
					int tmp[]=new int[material.size()];
		    		int othercount=0;
		    		int tolerance=Math.max(2,a.get(i).getMatSize()/50);
		    		for (int j=0;j<mc.size();j++) {
		    			if (mc.get(j).getCount()>tolerance) {
		    				tmp[material.indexOf(mc.get(j).getMaterial())]=mc.get(j).getCount();
		    			}
		    			else {
		    				othercount+=mc.get(j).getCount();
		    			}
		    		}
		    		for (int j=0;j<tmp.length;j++) {
		    			if (tmp[j]!=0)
		    				bw.write(tmp[j]+"");
		    			bw.write(",");
		    		}
		    		if (othercount!=0)
		    			bw.write(""+othercount);
		    		bw.write("\n");
	    		}
	    		else {
	    			othermatcount+=a.get(i).getMatSize();
	    			otherattribcount+=a.get(i).getAttSize();
	    			otherobjectcount+=a.get(i).getCount();
	    		}
	    	}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Writes All The Individual Objects with more than 10 material labels
     * to a csv file in the form
     * <No of Material Labels> <No of Attribute Labels> <No of Individual Polygons> <Object Name> <Details>
     * So it can be made a stacked graph in Excel with the most common label first
     */
    public static void writeIndividualObjectsToCSVFileToEnableStackedBarGraphCreationWithHighestFirst () {
    	try {
    		//Initializing File Writer
			File file = new File("IndividualObjectsExcelBarGraphHighestFirst.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Writing Header
			bw.write("Material,Attribute,Object,Name,Details\n");
			//Getting an arraylist of individual objects, ie, objects with distinct names
			ArrayList<LabelStuffObject> a=ImageCluster.getArrayListofObjectsClassifiedByName ();;
			int matsizetolerance=10; //Only objects with more than this number of material labels will be printed
			int othermatcount=0;
			int otherattribcount=0;
			int otherobjectcount=0;
			// Scanning through each individual object
	    	for (int i=0;i<a.size();i++) {
	    		//Processes the object only if it satisfies the no of materials labelled tolerance limit
	    		if (a.get(i).getMatSize()>matsizetolerance) {
	    			//Writes the no of times material labelled, attributes labelled, number of polygons and object name
		    		bw.write(a.get(i).getMatSize()+","+a.get(i).getAttSize()+","+a.get(i).getCount()+","+a.get(i).getObjName()+",");
		    		//Writes the details of the different materials that this object has been labelled
		    		ArrayList<MaterialCount> mc=a.get(i).getOrderedMaterialCounts();
		    		int othercount=0;
		    		int tolerance=Math.max(2,a.get(i).getMatSize()/50);
		    		for (int j=0;j<mc.size();j++) {
		    			if (mc.get(j).getCount()>tolerance) {
		    				bw.write(mc.get(j).getCount()+"");
		    				bw.write(",");
		    			}
		    			else {
		    				othercount+=mc.get(j).getCount();
		    			}
		    			
		    		}
		    		bw.write(othercount+"");
		    		bw.write("\n");
	    		}
	    		else {
	    			//Accumulates statistics of objects that did not meet the tolerance criteria
	    			othermatcount+=a.get(i).getMatSize();
	    			otherattribcount+=a.get(i).getAttSize();
	    			otherobjectcount+=a.get(i).getCount();
	    		}
	    	}
	    	bw.write(othermatcount+","+otherattribcount+","+otherobjectcount+",Other Objects\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    
    /**
     * Writes All The Individual Polygons with more than or equal to 3 material labels
     * to a csv file in the form
     * <No of Mat Labels> <Object Name> <Image Name> <Entropy> <Detailed Material Labels List>
     */
    public static void writeIndividualPolygonMaterialsWithEntropyToCSVFile () {
    	try {
    		//Creating File and Initializing writer
			File file = new File("IndividualPolygonsMaterialsWEntropy.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Writing Header
			bw.write("Labels,Object,Image,Entropy,Details\n");
			//Scanning through each ImageCluster, i.e., images with distinct primarily filename
	    	for (int i=0;i<ImageCluster.clusters.size();i++) {
	    		//Scanning through each object in the ImageCluster
	    		for (int j=0;j<ImageCluster.clusters.get(i).labelobjects.size();j++) {
	    			int matno=ImageCluster.clusters.get(i).labelobjects.get(j).getMatSize();
					if (matno>=3) {
						//Gets an ordered list of materials of the object
						ArrayList<MaterialCount> mc=ImageCluster.clusters.get(i).labelobjects.get(j).getOrderedMaterialCounts();
						//Writes No. of materials and the object name
						bw.write(matno+","+ImageCluster.clusters.get(i).labelobjects.get(j).getObjName()+",");
						//Calculates Entropies and Accumulates Count and list of Materials
						String tmp="";
						double entropy=0;
						for (int k=0;k<mc.size();k++) {
							tmp+=(mc.get(k).getCount()+" "+mc.get(k).getMaterial()+",");
							double px=((double)mc.get(k).getCount()/matno);
							entropy+=px*(Math.log(px)/Math.log(2));
						}
						entropy=-entropy;
						tmp=tmp.substring(0,tmp.length()-1);
						bw.write(ImageCluster.clusters.get(i).getBasename()+","+(double)Math.round(entropy*1000)/1000+","+tmp+"\n");
					}
				}
			}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Writes All The Individual Objects with more than 5 attribute labels
     * to a csv file in the form
     * <No of Material Labels> <No of Attribute Labels> <No of Individual Polygons> <Entropy> <Object Name> <Details>
     */
    public static void writeIndividualObjectAttributesWithEntropyToCSVFile () {
    	try {
    		//Creating File and Initializing writer
			File file = new File("IndividualObjectsAttributesWEntropy.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Writing Header
			bw.write("Material,Attribute,Polygon,Entropy,Object Name,Details\n");
			//Retrieve an ArrayList of objects with distinct names
			ArrayList<LabelStuffObject> a=ImageCluster.getArrayListofObjectsClassifiedByName ();;
			int attsizetolerance=5; //Prints only Objects with no of attributes beyond this value
			int othermatcount=0;
			int otherattribcount=0;
			int otherobjectcount=0;
	    	//Scans through all the individual objects
			for (int i=0;i<a.size();i++) {
				//Checks whether the object has more attributes than tolerated
	    		if (a.get(i).getAttSize()>attsizetolerance) {
	    			// Writes Details about the object
		    		bw.write(a.get(i).getMatSize()+","+a.get(i).getAttSize()+","+a.get(i).getCount()+",");
		    		// Get an arraylist of counts of the attributes of this object
		    		ArrayList<AttributeCount> ac=a.get(i).getOrderedAttributeCounts();
		    		int othercount=0;
		    		int tolerance=Math.max(2,a.get(i).getAttSize()/50);
		    		double entropy=0;
		    		String tmp=a.get(i).getObjName()+",";
		    		//Accumulates the attribute details of the object and calculates entropy
		    		for (int j=0;j<ac.size();j++) {
		    			if (ac.get(j).getCount()>tolerance) {
		    				tmp+=(ac.get(j).getCount()+" "+ac.get(j).getAttribute());
		    				tmp+=(",");
		    			}
		    			else {
		    				othercount+=ac.get(j).getCount();
		    			}
		    			double px=((double)ac.get(j).getCount())/a.get(i).getMatSize();
		    			entropy+=px*(Math.log(px)/Math.log(2));
		    			
		    		}
		    		tmp+=(othercount+" other");
		    		entropy=-entropy;
		    		// Writes entropy and the object's attribute details
		    		bw.write((double)Math.round(entropy*1000)/1000+","+tmp+"\n");
	    		}
	    		else {
	    			othermatcount+=a.get(i).getMatSize();
	    			otherattribcount+=a.get(i).getAttSize();
	    			otherobjectcount+=a.get(i).getCount();
	    		}
	    	}
			//Writes the details of object that fell under the tolerane
	    	bw.write(othermatcount+","+otherattribcount+","+otherobjectcount+",Other Objects\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * Writes All The Individual Polygons with more than or equal to 3 attribute labels
     * to a csv file in the form
     * <No of Att Labels> <Object Name> <Image Name> <Entropy> <Detailed Attribute Labels List>
     */
    public static void writeIndividualPolygonAttributesWithEntropyToCSVFile () {
    	try {
    		//Creating File and Initializing writer
			File file = new File("IndividualPolygonsAttributesWEntropy.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Header
			bw.write("Labels,Object,Image,Entropy,Details\n");
			//Loops through all the ImageClusters
	    	for (int i=0;i<ImageCluster.clusters.size();i++) {
	    		// Loops through each object in each ImageCluster
	    		for (int j=0;j<ImageCluster.clusters.get(i).labelobjects.size();j++) {
	    			//Process if number of attributes of the object is more than 3
					int attno=ImageCluster.clusters.get(i).labelobjects.get(j).getAttSize();
					if (attno>=3) {
						//Gets an ordered list of attributes of the object
						ArrayList<AttributeCount> ac=ImageCluster.clusters.get(i).labelobjects.get(j).getOrderedAttributeCounts();
						//Writes No. of attributes and the object name
						bw.write(attno+","+ImageCluster.clusters.get(i).labelobjects.get(j).getObjName()+",");
						//Calculates Entropies and Accumulates Count and list of Attributes
						String tmp="";
						double entropy=0;
						for (int k=0;k<ac.size();k++) {
							tmp+=(ac.get(k).getCount()+" "+ac.get(k).getAttribute()+",");
							double px=((double)ac.get(k).getCount()/attno);
							entropy+=px*(Math.log(px)/Math.log(2));
						}
						entropy=-entropy;
						tmp=tmp.substring(0,tmp.length()-1);
						bw.write(ImageCluster.clusters.get(i).getBasename()+","+(double)Math.round(entropy*1000)/1000+","+tmp+"\n");
					}
				}
			}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		
    	}
    }
    

    /**
     * Prints a list of all Mechanical Turk usernames associated with any of the XML Files
     */
    public static void printAllMTurkUsers () {
    	try {
    		//Initializing Writer
			File file = new File("AllMTurkUsernamesList.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			HashMap<String,Integer> hasher=new HashMap<String,Integer>();
			//Scanning through each ImageCluster
			for (int i=0;i<ImageCluster.clusters.size();i++) {
				//Scanning through each object in each ImageCluste
				for (int j=0;j<ImageCluster.clusters.get(i).labelobjects.size();j++) {
					String username="";
					//Scanning through usernames who labelled materials in that ImageCluster
					for (int k=0;k<ImageCluster.clusters.get(i).labelobjects.get(j).getMatSize();k++) {
						username=ImageCluster.clusters.get(i).labelobjects.get(j).getMatLabel(k).getUserName();
						if (username.substring(0,2).equals("MT")) {
							if (hasher.containsKey(username)) 
								hasher.put(username,hasher.get(username)+1);
							else 
								hasher.put(username,1);
						}
					}
					//Scanning through usernames who labelled attributes in that ImageCluster
					for (int k=0;k<ImageCluster.clusters.get(i).labelobjects.get(j).getAttSize();k++) {
						username=ImageCluster.clusters.get(i).labelobjects.get(j).getMatLabel(k).getUserName();
						if (username.substring(0,2).equals("MT")) {
							if (hasher.containsKey(username)) 
								hasher.put(username,hasher.get(username)+1);
							else 
								hasher.put(username,1);
						}
					}
				}
			}
			//Writing all the Map Entries
			for (Map.Entry<String,Integer> i: hasher.entrySet()) {
				bw.write(i.getValue()+" "+i.getKey()+"\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

    
    /**
     * Scans through all the clusters and prints each object and the number of 
     * times the material has been annotated and then prints the average 
     * number of times each object has been labelled in the dataset
     */
    public static void printObjectsInAllClustersAndNumberOfTimesAnnotated () {
    	int count=0;
    	int tally=0;
    	int threeplus=0;
    	for (int i=0;i<ImageCluster.clusters.size();i++) {
    		System.out.println(ImageCluster.clusters.get(i).getBasename()+" "+ImageCluster.clusters.get(i).images.size());
			for (int j=0;j<ImageCluster.clusters.get(i).labelobjects.size();j++) {
				int k=(ImageCluster.clusters.get(i).labelobjects.get(j).getMatSize());
				System.out.println(ImageCluster.clusters.get(i).labelobjects.get(j).getObjName()+" "+k);
				if (k>=3) {
					threeplus++;
				}
				tally++;
				count+=k;
			}
		}
    	System.out.println("Average Material Labels per object: "+(double)count/tally);
    }

    
}