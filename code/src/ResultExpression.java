import java.util.Collections;
import java.util.Vector;

public class ResultExpression {

    Vector<Double> expressionsError = new Vector<>();
    Vector<String> expressionsresult = new Vector<>();

    void addData(String expression, Double value) {
        expressionsError.add(value);
        expressionsresult.add(expression);
    }

    void resultExpression() {

        Object obj = Collections.min(expressionsError);
        int index = expressionsError.indexOf(obj);
        String result = expressionsresult.get(index);

        System.out.println("Expression result is: " + result);
    }


}