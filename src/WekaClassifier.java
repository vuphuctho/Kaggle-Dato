import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/*
 * This class is responsible for building and evaluating classification model
 * 
 * It performs a single run of cross-validation (1 seed, 3 folds)
 * 
 * Command-line parameters:
 * <ul>
 * 		<li> -train filename - the training file to use;  
 * 		file must be put in folder Data\</li>
 * 		<li> -test filename - the test file to use;  
 * 		file must be put in folder Data\</li>
 * 		<li> -C int - class index, "first" and "last" are accepted as well;
 * 		"last" is used as default </li>
 * 		<li> -R int[] - attributes' indexes to ignore during classification, 
 * 		enclosed by double quotes;
 * 		<li> -M classifier - classname and options, enclosed by double quotes;
 * 		the classifier to cross-validate</li>
 *  </ul>
 *  
 *  Example command-line:
 *  <pre>
 *  	java WekaClassifier -train train.csv -test test.csv -C last -R "1, 3" 
 *  						-M "weka.classifiers.trees.J48 -C 0.25"
 *  </pre>
 */

public class WekaClassifier {
	private final static String INPUT_DIRECTORY = "Data//"; // Directory to input files
	private final static String OUTPUT_DIRECTORY = "Data//";// Directory to output files
	private Instances raw_train;							// raw training data from input 
	private Instances filted_train;						 	// filted data ready to be used
	private Instances raw_test;
	private Instances filted_test;
	private Filter filter;
	private Classifier model;
	
	public WekaClassifier() {		
	}
	
	public void main(String[] args) throws Exception {
		WekaClassifier classifier = new WekaClassifier(); 
		// set data used for classification (training data)
		classifier.setTrain(Utils.getOption("train", args));
		// set data used for classification (testing data):
		classifier.setTest(Utils.getOption("test", args));
		// set which attributes is used as class labeled (target)
		classifier.setClassIndex(Utils.getOption("C", args));
		// set Filter for classifier
		classifier.setFilter(Utils.getOption("R", args));
		// set classifier model
		classifier.setClassificationModel(Utils.getOption("M", args));
		// filter data 
		classifier.filterData();
		// train model and predict using test data
		classifier.predict();
	}
	
	public void setTrain(String _file) throws Exception {
		String input = WekaClassifier.INPUT_DIRECTORY + _file;
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(input));;
		raw_train = loader.getDataSet();
	}
	
	public void setTest(String _file) throws Exception {
		String input = WekaClassifier.INPUT_DIRECTORY + _file;
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(input));;
		raw_test = loader.getDataSet();
	}
	
	public void setClassIndex(String _index) throws Exception {
		if (_index.length()==0) _index = "last";
		if (_index=="last") {
			raw_train.setClassIndex(raw_train.numAttributes()-1);
		} else if (_index=="first") {
			raw_train.setClassIndex(0);
		} else {
			raw_train.setClassIndex(Integer.parseInt(_index));
		}
	} 

	// By default, filter is used to remove attributes
	// Further extensions are being considered
	public void setFilter(String _args) throws Exception {
		String remove_attr = "";
		if (_args.length()>0) {
			remove_attr += ", " + _args;
		} 
		//System.out.println(remove_attr);
		String[] options = {"-R", remove_attr};
		Remove remove = new Remove();
		remove.setOptions(options);
		filter = remove;
	}
	
	public void setClassificationModel(String _args) throws Exception {
		String classifier_name = _args;
		String[] options = Utils.splitOptions(classifier_name);
		String class_name = options[0];
		options[0] = "";
		model = (Classifier) Utils.forName(Classifier.class, class_name, options);
	} 
	
	public void filterData() throws Exception {
		filter.setInputFormat(raw_train);
		filted_train = Filter.useFilter(raw_train, filter);
		filted_train.setClassIndex(filted_train.numAttributes()-1);
		filter.setInputFormat(raw_test);
		filted_test = Filter.useFilter(raw_test, filter);
	}
	
	public void predict() throws Exception {
		model.buildClassifier(filted_train);
		Evaluation eval = new Evaluation(filted_train);
		double[] results = eval.evaluateModel(model, filted_test);
		System.out.println(eval.toSummaryString("\nResults\n====\n", false));
		// prepare the output file
		String output = WekaClassifier.OUTPUT_DIRECTORY + "output.csv";
	}

}
