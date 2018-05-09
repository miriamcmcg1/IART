import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.sample.SubSampling;

import java.io.*;
import java.util.List;

public class FacialExpressionNetwork {
    private static final int ENTRYNODES = 301;
    private static final int OUTPUTNODES = 1;
    private static final int INTERMEDIATENODES = 20;
    private static final int TRAINPERCENTAGE = 60;


    private MultiLayerPerceptron neuroNetwork;
    private DataSet dataset;
    private DataSet dataTraining;
    private DataSet dataTest;
    private BackPropagation learningRule;
    private File Finalfile;

    public FacialExpressionNetwork() {
    }

    public void generatingNetwork(String path, int maxIterations, double learningRate, double maxError) {

        // create multi layer perceptron
        neuroNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, ENTRYNODES, INTERMEDIATENODES, OUTPUTNODES);
        // create DataSet
        dataset = DataSet.createFromFile(path, ENTRYNODES, OUTPUTNODES, " ", true);

        //Normalizing data
        MaxMinNormalizer normalizing = new MaxMinNormalizer();
        normalizing.normalize(dataset);

        //Implementing Learning Rule
        learningRule = new BackPropagation();
        learningRule.setNeuralNetwork(neuroNetwork);
        learningRule.setLearningRate(learningRate);
        learningRule.setMaxError(maxError);
        learningRule.setMaxIterations(maxIterations);

        SubSampling samples = new SubSampling(TRAINPERCENTAGE, 100 - TRAINPERCENTAGE);
        List<DataSet> dataSets = samples.sample(dataset);
        dataTraining = dataSets.get(0);
        dataTest = dataSets.get(1);

        neuroNetwork.learn(dataTraining, learningRule);
        String[] name = path.split("_");
        File finalFile = new File("Expressions\\Final_" + name[1]);
        this.Finalfile = finalFile;
        if (!finalFile.exists()) {
            try {
                finalFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int i = 0;
        double error;
        double diff = 0.0;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(finalFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter line = new BufferedWriter(new OutputStreamWriter(fos));

        for (DataSetRow dataRow : this.dataTraining.getRows()) {
            neuroNetwork.setInput(dataRow.getInput());
            neuroNetwork.calculate();
            double[] networkOutput = neuroNetwork.getOutput();
            double[] networkDesiredOutput = dataRow.getDesiredOutput();
            diff += Math.pow(networkOutput[0] - networkDesiredOutput[0], 2);


            i++;
        }
        error = diff / i;
        try {
            line.write("Train error: " + error);
            line.newLine();
            line.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public double testNeuralNetwork( DataSet set) {

        if (!this.Finalfile.exists()) {
            try {
                this.Finalfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.Finalfile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter line = new BufferedWriter(new OutputStreamWriter(fos));

        int i = 0;
        double error;
        double diff = 0.0;
        double avg = 0.0;
        for (DataSetRow dataRow : set.getRows()) {
            neuroNetwork.setInput(dataRow.getInput());
            neuroNetwork.calculate();
            double[] networkOutput = neuroNetwork.getOutput();
            double[] networkDesiredOutput = dataRow.getDesiredOutput();
            diff += Math.pow(networkOutput[0] - networkDesiredOutput[0], 2);
            avg += Math.abs(networkOutput[0] - networkDesiredOutput[0]);
            System.out.println("Output: " + networkOutput[0]);
            System.out.println("Desired Output: " + networkDesiredOutput[0]);

            i++;
        }

        error = diff / i;

        try {
            line.write("Test error: " + error);
            line.newLine();
            line.write("Average error: " + avg/i);
            line.newLine();
            line.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return error;
    }

    public static void parseData() throws IOException {

        DataParser affirmative_expression = new DataParser("Expressions\\a_affirmative_datapoints.txt", "Expressions\\a_affirmative_targets.txt", "Expressions\\b_affirmative_datapoints.txt", "Expressions\\b_affirmative_targets.txt");
        affirmative_expression.fileConcatenator();

        DataParser conditional_expression = new DataParser("Expressions\\a_conditional_datapoints.txt", "Expressions\\a_conditional_targets.txt", "Expressions\\b_conditional_datapoints.txt", "Expressions\\b_conditional_targets.txt");
        conditional_expression.fileConcatenator();
        DataParser doubts_question_expression = new DataParser("Expressions\\a_doubt_question_datapoints.txt", "Expressions\\a_doubts_question_targets.txt", "Expressions\\b_doubt_question_datapoints.txt", "Expressions\\b_doubt_question_targets.txt");
        doubts_question_expression.fileConcatenator();

        DataParser emphasis_expression = new DataParser("Expressions\\a_emphasis_datapoints.txt", "Expressions\\a_emphasis_targets.txt", "Expressions\\b_emphasis_datapoints.txt", "Expressions\\b_emphasis_targets.txt");
        emphasis_expression.fileConcatenator();

        DataParser negative_expression = new DataParser("Expressions\\a_negative_datapoints.txt", "Expressions\\a_negative_targets.txt", "Expressions\\b_negative_datapoints.txt", "Expressions\\b_negative_targets.txt");
        negative_expression.fileConcatenator();

        DataParser relative_expression = new DataParser("Expressions\\a_relative_datapoints.txt", "Expressions\\a_relative_targets.txt", "Expressions\\b_relative_datapoints.txt", "Expressions\\b_relative_targets.txt");
        relative_expression.fileConcatenator();

        DataParser topics_expression = new DataParser("Expressions\\a_topics_datapoints.txt", "Expressions\\a_topics_targets.txt", "Expressions\\b_topics_datapoints.txt", "Expressions\\b_topics_targets.txt");
        topics_expression.fileConcatenator();

        DataParser wh_question_expression = new DataParser("Expressions\\a_wh_question_datapoints.txt", "Expressions\\a_wh_question_targets.txt", "Expressions\\b_wh_question_datapoints.txt", "Expressions\\b_wh_question_targets.txt");
        wh_question_expression.fileConcatenator();

        DataParser yn_question_expression = new DataParser("Expressions\\a_yn_question_datapoints.txt", "Expressions\\a_yn_question_targets.txt", "Expressions\\b_yn_question_datapoints.txt", "Expressions\\b_yn_question_targets.txt");
        yn_question_expression.fileConcatenator();
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();


        try {
            FacialExpressionNetwork.parseData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResultExpression result = new ResultExpression();

       File testFile = new File(args[0]);

        if(testFile.isFile() && testFile.exists())
       {
            runNeuralNetworks(testFile, result);
        }



        long time = System.currentTimeMillis() - start;
        System.out.println("Time: " + time);
        result.resultExpression();
    }



    public static void runNeuralNetworks(File testFile, ResultExpression result){
        DataSet dataset = DataSet.createFromFile(testFile.getAbsolutePath(), ENTRYNODES, OUTPUTNODES, " ", true);
        MaxMinNormalizer normalizing = new MaxMinNormalizer();
        normalizing.normalize(dataset);

       FacialExpressionNetwork affirmativeNetwork = new FacialExpressionNetwork();
        affirmativeNetwork.generatingNetwork("Expressions\\SET_affirmative.txt", 1000, 0.2, 0.01);
        double aff_error = affirmativeNetwork.testNeuralNetwork(dataset);
        result.addData("affirmative", aff_error);

        FacialExpressionNetwork conditionalNetwork = new FacialExpressionNetwork();
        conditionalNetwork.generatingNetwork("Expressions\\SET_conditional.txt", 1000, 0.2, 0.01);
        double cond_error = conditionalNetwork.testNeuralNetwork(dataset);
        result.addData("conditional", cond_error);


        FacialExpressionNetwork doubtNetwork = new FacialExpressionNetwork();
        doubtNetwork.generatingNetwork("Expressions\\SET_doubt.txt", 1000, 0.2, 0.01);
        double doubt_error = doubtNetwork.testNeuralNetwork( dataset);
         result.addData("doubt", doubt_error);

        FacialExpressionNetwork empashisNetwork = new FacialExpressionNetwork();
         empashisNetwork.generatingNetwork("Expressions\\SET_emphasis.txt", 1000, 0.2, 0.01);
        double emph_error = empashisNetwork.testNeuralNetwork( dataset);
        result.addData("emphasis", emph_error);

        FacialExpressionNetwork negativeNetwork = new FacialExpressionNetwork();
        negativeNetwork.generatingNetwork("Expressions\\SET_negative.txt", 1000, 0.1, 0.01);
        double neg_error = negativeNetwork.testNeuralNetwork( dataset);
        result.addData("negative", neg_error);

        FacialExpressionNetwork relativeNetwork = new FacialExpressionNetwork();
        relativeNetwork.generatingNetwork("Expressions\\SET_relative.txt", 1000, 0.1, 0.01);
        double rel_error = relativeNetwork.testNeuralNetwork( dataset);
        result.addData("relative", rel_error);

        FacialExpressionNetwork topicsNetwork = new FacialExpressionNetwork();
        topicsNetwork.generatingNetwork("Expressions\\SET_topics.txt", 1000, 0.1, 0.01);
        double top_error = topicsNetwork.testNeuralNetwork( dataset);
        result.addData("topics", top_error);

        FacialExpressionNetwork whNetwork = new FacialExpressionNetwork();
        whNetwork.generatingNetwork("Expressions\\SET_wh.txt", 1000, 0.1, 0.01);
        double wh_error = whNetwork.testNeuralNetwork( dataset);
        result.addData("wh", wh_error);

        FacialExpressionNetwork ynNetwork = new FacialExpressionNetwork();
        ynNetwork.generatingNetwork("Expressions\\SET_yn.txt", 1000, 0.1, 0.01);
        double yn_error = ynNetwork.testNeuralNetwork( dataset);
        result.addData("yn", yn_error);


    }

}