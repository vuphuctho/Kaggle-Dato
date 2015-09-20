import java.util.*;
import java.io.*;
import java.math.*;

//Java program to create a number of sample training data for classifier (initial value: 2000)

public class SampleCreator {

	public static final String COMMA = ",";
	public static final String NEWLINE = "\n";
	public static final int SampleNum = 2000;

	public static void main(String[] args) {

		FileWriter fw = null;

		try {
			fw = new FileWriter("ClassifierInput.csv");

			try {

				Scanner sc = new Scanner(new File("..//Data//train.csv")); //directory of train.csv
				sc.nextLine(); // jump first line

				//assume computer can store about 500kB in memory, store train.csv in train
				List<String[]> train = new ArrayList<String[]>();
				while (sc.hasNextLine()) {
					String[] in = sc.nextLine().split(COMMA);					
					train.add(in);
				}

				//write to SampleNum (2000, for example) examples
				Random r = new Random();				
								
				//format for one data item: name_dir, all features, result (0 or 1)
				for (int i = 0; i < SampleNum; i++) {

					String[] random_example = train.get(r.nextInt(train.size()));
					String name_dir = random_example[0];
					String result = random_example[1];

					fw.append(name_dir);
					fw.append(COMMA);	

					List<Double> features = new ArrayList<Double>();
					String dir = "..//data//htmls//" + name_dir;
					File f = new File(dir);
					if (f.exists()) {
						features = getFeatures.getFeatures(dir); //directory of the html files				
						for (int j = 0; j < features.size(); j++) {
							fw.append(Double.toString(features.get(j)));
							fw.append(COMMA);
						}
					
						fw.append(result);
						fw.append(NEWLINE);
					}
				}		
				
			}

			catch (Exception e) { //catch of scanner
				e.printStackTrace();
			}

		}

		catch (Exception e) { //cath of File Writer
			e.printStackTrace();
		}

		finally{
         		try {fw.flush(); fw.close();}     
          		catch (IOException e) { System.out.println("Error while closing file writer");}
      		}	

	}

}

