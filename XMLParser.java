import javax.xml.*;
import javax.xml.parsers.*;
import org.w3c.dom.*; // auto-import
import org.xml.sax.*;
import java.io.*;
public class XMLParser {
    public static void main (String args[]) {
        XMLParser.parseXmlFile();
    }
    private static void parseXmlFile(){
  //get the factory
  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

  try {

   //Using factory get an instance of document builder
   DocumentBuilder db = dbf.newDocumentBuilder();

   //parse using builder to get DOM representation of the XML file
   Document dom = db.parse("xmlfile.xml");
   
   


  }catch(ParserConfigurationException pce) {
   pce.printStackTrace();
  }catch(SAXException se) {
   se.printStackTrace();
  }catch(IOException ioe) {
   ioe.printStackTrace();
  }
 }
}