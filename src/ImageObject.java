import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class ImageObject {
	//public static ArrayList<ImageObject> imageobjects=new ArrayList<ImageObject>();
	public ArrayList<LabelStuffObject> labelobjects=new ArrayList<LabelStuffObject>();
	private String imagefilename;
	private String folder;
	private String sourceImage;
	private String sourceAnnotation;
	private String filename;
	private String basefilename;
	private int index;
	public ImageObject (String filename,String path) {
		  this.filename=filename;
		  if (path.length()!=0)
			  path+="/";
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  try {
		   DocumentBuilder db = dbf.newDocumentBuilder();
		   Document dom = db.parse(path+filename);
		   parseDocument(dom);
		   //imageobjects.add(this);
		   ImageCluster.addImageObject(this);
		  }catch(ParserConfigurationException pce) {
		   pce.printStackTrace();
		  }catch(SAXException se) {
		   se.printStackTrace();
		  }catch(IOException ioe) {
		   ioe.printStackTrace();
		  }
	}
	public String getBaseFileName() {
		return this.basefilename;
	}
	public int getIndex() {
		return this.index;
	}
    private void parseDocument(Document dom){
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("object");
		this.imagefilename=docEle.getElementsByTagName("filename").item(0).getTextContent();
		this.basefilename=this.imagefilename.substring(0,this.imagefilename.lastIndexOf('_'));
		this.index=Integer.parseInt(this.imagefilename.substring(this.imagefilename.lastIndexOf('_')+1, this.imagefilename.lastIndexOf('.')));
		this.folder=docEle.getElementsByTagName("folder").item(0).getTextContent();
		this.sourceImage=docEle.getElementsByTagName("sourceImage").item(0).getTextContent();
		this.sourceAnnotation=docEle.getElementsByTagName("sourceAnnotation").item(0).getTextContent();
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Node tmp=nl.item(i);
				LabelStuffObject a=new LabelStuffObject(this.filename,tmp);
				labelobjects.add(a);
			}
		}
    }
    public String toXMLString () {
    	String s="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><annotation>";
    	s+="<filename>"+this.imagefilename+"</filename>";
    	s+="<folder>"+this.folder+"</folder>";
    	s+="<source><sourceImage>"+this.sourceImage+"</sourceImage>";
    	s+="<sourceAnnotation>"+this.sourceAnnotation+"</sourceAnnotation></source>";
    	for (int i=0;i<labelobjects.size();i++) {
    		s+=labelobjects.get(i).toXMLString();
    	}
    	s+="</annotation>";
    	return s;
    }
    public void makeFile() {
    	try {
			File file = new File(this.filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.toXMLString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public void makeRawFile() {
    	try {
			File file = new File(this.basefilename+"_1.xml");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.toRawXMLString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public String toRawXMLString () {
    	String s="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><annotation>";
    	s+="<filename>"+this.imagefilename+"</filename>";
    	s+="<folder>"+this.folder+"</folder>";
    	s+="<source><sourceImage>"+this.sourceImage+"</sourceImage>";
    	s+="<sourceAnnotation>"+this.sourceAnnotation+"</sourceAnnotation></source>";
    	for (int i=0;i<labelobjects.size();i++) {
    		s+=labelobjects.get(i).toRawXMLwoMat();
    	}
    	s+="</annotation>";
    	return s;
    }
}
