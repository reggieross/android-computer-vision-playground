package objectrecognition.rross.awslambdafunctions.invokers;

import android.content.Context;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import java.util.Map;

import objectrecognition.rross.awslambdafunctions.AwsCredintials;
import objectrecognition.rross.awslambdafunctions.invokerTasks.FragmentToSentenceTask;
import objectrecognition.rross.awslambdafunctions.interfaces.IFragmentToSentenceSvc;
import objectrecognition.rross.texttospeach.ITextToSpeech;
import objectrecognition.rross.texttospeach.impl.TextToSpeechImpl;


public class FragmentToSentenceLambdaInvoker {
    private AwsCredintials awsCredintials;
    private ITextToSpeech textToSpeech;


    public FragmentToSentenceLambdaInvoker(Context context, Regions region) {
        awsCredintials = new AwsCredintials(context, region);
        //TODO make language codes an Enum
        textToSpeech = new TextToSpeechImpl(context, Regions.US_EAST_1, "en-US");

    }

    public void invokeFunction(Map<String, Double> fragments) {
        LambdaInvokerFactory factory = new LambdaInvokerFactory(
            awsCredintials.getContext(),
            awsCredintials.getRegion(),
            awsCredintials.getCredentialsProvider()
        );

        IFragmentToSentenceSvc fragmentToSentenceSvc =
            factory.build(IFragmentToSentenceSvc.class);
        FragmentToSentenceTask fragmentToSentenceTask =
            new FragmentToSentenceTask(fragmentToSentenceSvc, textToSpeech);

        fragmentToSentenceTask.execute(fragments);
    }
}
