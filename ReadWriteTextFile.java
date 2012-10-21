import java.io.*;

public class ReadWriteTextFile {

  /**
  * Fetch the entire contents of a text file, and return it in a String.
  * This style of implementation does not throw Exceptions to the caller.
  *
  * @param aFile is a file which already exists and can be read.
  */
  static public String getContents(File aFile) {
    //...checks on aFile are elided
    StringBuilder contents = new StringBuilder();
    int count=0;
    try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input =  new BufferedReader(new FileReader(aFile));
      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        
        while (( line = input.readLine()) != null){
//          line=line.replaceAll(">", (">"+System.getProperty("line.separator"))).trim();
          line=line.replaceAll(System.getProperty("line.separator"),"").trim();
          contents.append(line);
         //contents.append(System.getProperty("line.separator"));
          count++;
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    
    
    return contents.toString();
  }

  /**
  * Change the contents of text file in its entirety, overwriting any
  * existing text.
  *
  * This style of implementation throws all exceptions to the caller.
  *
  * @param aFile is an existing file which can be written to.
  * @throws IllegalArgumentException if param does not comply.
  * @throws FileNotFoundException if the file does not exist.
  * @throws IOException if problem encountered during write.
  */
  static public void setContents(File aFile, String aContents)
                                 throws FileNotFoundException, IOException {
    

    //use buffering
    Writer output = new BufferedWriter(new FileWriter(aFile));
    try {
      //FileWriter always assumes default encoding is OK!
      output.write( aContents );
    }
    finally {
      output.close();
    }
  }

  /** Simple test harness.   */
  public static void main (String... aArguments) throws IOException {
 
    File directory = new File("LabelStuff/");  
    File[] files = directory.listFiles();  
    for (int index = 0; index < files.length; index++)  
    {  
        setContents( new File(files[index].toString().substring(files[index].toString().lastIndexOf('/')+1)),getContents(files[index]));
        //Print out the name of files in the directory  
        //System.out.println(files[index].toString());  
    }  
  }
} 
