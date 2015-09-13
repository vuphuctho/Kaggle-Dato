import java.io.File;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/*
 * This class is responsible to evaluate classification model
 * with given default input file.
 * We apply 10-fold cross-validation to test with training data
 * Command line parameter:
 * 		"- M " + classifier information: classifier name and options,
 * 		enclosed by double quoted
 * 		-	Example: -M "NaiveBayes -C 0.12": choose a NaiveBayes model and 
 * 			set C equal 0.12
 * 
 *  Example command line:
 *  	java WekaEvaluator -M "NaiveBayes -C 0.12"
 */

public class WekaEvaluator {
	public final String DEFAULT_INPUT = "ClassifierInput.csv";
	public Instances data;
	public Classifier model;

	public void setInput() throws Exception {
		setInput(DEFAULT_INPUT);
	}
	
	public void setInput(String file) throws Exception {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(file));
		this.data = loader.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		// remove first column (name of file)
		String[] options = {"-R", "1"};
		Remove remove = new Remove();
		remove.setOptions(options);
		data = Filter.useFilter(data, remove);
	}
	
	public void setClassifier(String arg) throws Exception {
		String[] options = Utils.splitOptions(arg);
		String className = options[0]; 
		options[0] = "";
		model = (Classifier) Utils.forName(Classifier.class, className, options);
	}
	
	public void execute() throws Exception {
		Evaluation eval = new Evaluation(data);
		// cross-validation: 10 folds
		eval.crossValidateModel(model, data, 10, new Random(1));
		System.out.println(eval.toSummaryString());
	}
	
	public static void main(String[] args) throws Exception {
		WekaEvaluator eval = new WekaEvaluator();
		eval.setInput();
		eval.setClassifier(Utils.getOption("M", args));
		eval.execute();
	}
}
