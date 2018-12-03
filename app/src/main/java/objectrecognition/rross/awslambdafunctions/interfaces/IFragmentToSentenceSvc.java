package objectrecognition.rross.awslambdafunctions.interfaces;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import java.util.Map;

public interface IFragmentToSentenceSvc {
    /**
     * Invoke lambda function "fragmentToSentence". The function name is the method name
     */
    @LambdaFunction
    String fragmentToSentence(Map<String, Double> nameInfo);

}
