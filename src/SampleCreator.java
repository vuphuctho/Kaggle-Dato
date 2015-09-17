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
				File training = new File("Data//train_v2.csv");
				Scanner sc = new Scanner(training); //directory of train.csv
				sc.nextLine(); // jump first line

				//assume computer can store about 500kB in memory, store train.csv in train
				List<String[]> train = new ArrayList<String[]>();
				while (sc.hasNextLine()) {
					String[] in = sc.nextLine().split(COMMA);					
					train.add(in);
				}
				
				// Ensure all data item is selected once only.
				List<Integer> selected = new ArrayList<Integer>();
				//write to SampleNum (2000, for example) examples
				Random r = new Random();				
				//format for one data item: name_dir, all features, result (0 or 1)
				while (selected.size() < SampleNum) {
					int index = r.nextInt(train.size());
					if (!selected.contains(index)) {
						selected.add(index);
						String[] random_example = train.get(index);
						String name_dir = random_example[0];
						String result = random_example[1];

						fw.append(name_dir);
						fw.append(COMMA);	

						List<Double> features = new ArrayList<Double>();
						features = getFeatures.getFeatures(HTMLparser.readFile("Data//htmls//" + name_dir)); //directory of the html files				
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

