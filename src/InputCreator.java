import java.util.*;
import java.io.*;

//a new java program that reads from train.csv and "0.1.2.3" and write to input for classifier
//assume: all the data + train.csv is in one folder Data
//file listed in train.csv is training file, the rest is test file

public class InputCreator {

	public static final String COMMA = ",";
	public static final String NEWLINE = "\n";

	public static void main(String[] args) {

		FileWriter fw = null;

		try {
			fw = new FileWriter("ClassifierInput.csv");

			try {

				Scanner sc = new Scanner("Data//train.csv"); //directory of train.csv
				sc.nextLine(); // jump first line

				//format for one data item: name_dir, all features, result (0 or 1)
				while (sc.hasNextLine()) {

					String[] in = sc.nextLine().split(COMMA);
					String name_dir = in[0];
					String result = in[1];				
	
					fw.append(name_dir);
					fw.append(COMMA);	

					List<Double> features = new ArrayList<Double>();
					features = getFeatures.getFeatures(HTMLparser.readFile("Data//htmls//" + name_dir)); //directory of the html files
				
					for (int i = 0; i < features.size(); i++) {
						fw.append(Double.toString(features.get(i)));
						fw.append(COMMA);
					}
					
					fw.append(result);
					fw.append(NEWLINE);
		
				}
			}

			catch (Exception e) { //catch of scanner
				System.out.println(e); 
			}

		}

		catch (Exception e) { //cath of File Writer
			System.out.println(e);
		}

		finally{
         		try {fw.flush(); fw.close();}     
          		catch (IOException e) { System.out.println("Error while closing file writer");}
      		}	

	}

}


