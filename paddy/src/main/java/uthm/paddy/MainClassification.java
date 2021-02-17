package uthm.paddy;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class MainClassification {

	private static int FEATURES_COUNT = 0;
	private static int CLASSES_COUNT = 0;

//	public MainClassification(String args) {
//		BasicConfigurator.configure();
//		classifiyData(args);
//
//	}
	public static String classifiyData(File fileName, int Numfeatures, int Numclasses) {
		FEATURES_COUNT = Numfeatures;
		CLASSES_COUNT = Numclasses;
		BasicConfigurator.configure();
		String output = null;
		try (RecordReader recordReader = new CSVRecordReader(0, ',')) {
			recordReader.initialize(new FileSplit(fileName));

			DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 500, FEATURES_COUNT,
					CLASSES_COUNT);
			DataSet allData = iterator.next();
			allData.shuffle(123);

			DataNormalization normalizer = new NormalizerStandardize();
			normalizer.fit(allData);
			normalizer.transform(allData);

			SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);
			DataSet trainingData = testAndTrain.getTrain();
			DataSet testingData = testAndTrain.getTest();

			output = irisNNetwork(trainingData, testingData);
		} catch (Exception e) {
			Thread.dumpStack();
			new Exception("Stack trace").printStackTrace();
			System.out.println("Error: " + e.getLocalizedMessage());
		}
		return output;
	}

	private static String irisNNetwork(DataSet trainingData, DataSet testData) {

		MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder().activation(Activation.TANH)
				.weightInit(WeightInit.XAVIER).updater(new Nesterovs(0.1, 0.9)).l2(0.0001).list()
				.layer(0, new DenseLayer.Builder().nIn(FEATURES_COUNT).nOut(256).build())
				.layer(1, new DenseLayer.Builder().nIn(256).nOut(128).build())
				.layer(2,
						new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
								.activation(Activation.SOFTMAX).nIn(128).nOut(CLASSES_COUNT).build())
				.backprop(true).pretrain(false).build();

		MultiLayerNetwork model = new MultiLayerNetwork(configuration);
		model.init();
		model.fit(trainingData);

		INDArray output = model.output(testData.getFeatureMatrix());
		Evaluation eval = new Evaluation(10);
		eval.eval(testData.getLabels(), output);
		// System.out.println(eval.stats());

		return eval.stats();
	}
}
