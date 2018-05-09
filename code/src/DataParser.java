import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DataParser {

    String a_pathDataPoint;
    String a_pathTarget;
    String b_pathDataPoint;
    String b_pathTarget;
    File DataSetFile;


    public DataParser(String a_pathDataPoint, String a_pathTarget, String b_pathDataPoint, String b_pathTarget) throws IOException {
        this.a_pathDataPoint = a_pathDataPoint;
        this.a_pathTarget = a_pathTarget;
        this.b_pathDataPoint = b_pathDataPoint;
        this.b_pathTarget = b_pathTarget;
        this.DataSetFile = null;

    }
    public void fileConcatenator() throws IOException {
        File a_path = addTarget(this.a_pathDataPoint, this.a_pathTarget);
        File b_path = addTarget(this.b_pathDataPoint, this.b_pathTarget);
        this.concatenateFiles(a_path, b_path);
    }
    public File addTarget(String pathDataPoint, String pathTarget) throws IOException {
        File datapoint = new File(pathDataPoint);
        FileInputStream DataPointstream = new FileInputStream(datapoint);
        File targets = new File(pathTarget);
        FileInputStream Targetstream = new FileInputStream(targets);

        String[] name = datapoint.getName().split("_");
        String folderPath = new String(Arrays.copyOfRange(datapoint.getAbsolutePath().getBytes(), 0,
                datapoint.getAbsolutePath().lastIndexOf("\\")));

        Path pathToFile = Paths.get(folderPath + "\\" + name[0] + "_" + name[1] + ".txt");
        File outPutFile = new File(String.valueOf(pathToFile));
        FileOutputStream fos = new FileOutputStream(outPutFile);

        BufferedReader datapointStream = new BufferedReader(new InputStreamReader(DataPointstream));
        BufferedReader tagetStream = new BufferedReader(new InputStreamReader(Targetstream));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        String labelLine = datapointStream.readLine() + "T;";
        bw.write(labelLine);
        bw.newLine();

        String targetLine, datapointLine;
        while (((datapointLine = datapointStream.readLine()) != null) &&
                ((targetLine = tagetStream.readLine()) != null)) {
            String str = (datapointLine + " " + targetLine + " ");
            bw.write(str);
            bw.newLine();}

        datapointStream.close();
        tagetStream.close();
        bw.close();
        return outPutFile;
    }

    public void concatenateFiles(File file1, File file2) throws IOException {
        FileInputStream Expression1 = new FileInputStream(file1);
        FileInputStream Expression2 = new FileInputStream(file2);

        String[] name = file1.getName().split("_");
        String folderPath = new String(Arrays.copyOfRange(file1.getAbsolutePath().getBytes(), 0,
                file1.getAbsolutePath().lastIndexOf("\\")));

        Path pathToTrainFile = Paths.get(folderPath + "\\" + "SET_" + name[1]);
        File file = new File(pathToTrainFile.toString());
        if(file.exists())
        {
            return;
        }
        this.DataSetFile = new File(String.valueOf(pathToTrainFile));
        FileOutputStream training = new FileOutputStream(this.DataSetFile);

        BufferedReader Stream1 = new BufferedReader(new InputStreamReader(Expression1));
        BufferedReader Stream2 = new BufferedReader(new InputStreamReader(Expression2));
        BufferedWriter DataSetBuffer = new BufferedWriter(new OutputStreamWriter(training));

        String labelLine1 = Stream1.readLine();
        String labelLine2 = Stream2.readLine();

        DataSetBuffer.write(labelLine1);
        DataSetBuffer.newLine();
        String trash = labelLine2;

        String line1, line2;
        while (((line1 = Stream2.readLine()) != null) && ((line2 = Stream2.readLine()) != null)) {
            DataSetBuffer.write(line1);
            DataSetBuffer.newLine();
            DataSetBuffer.write(line2);
            DataSetBuffer.newLine();
        }

        Stream1.close();
        Stream2.close();
        DataSetBuffer.close();
        file1.delete();
        file2.delete();
    }
}

