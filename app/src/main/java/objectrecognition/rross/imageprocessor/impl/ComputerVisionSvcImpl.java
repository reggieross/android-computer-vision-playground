package objectrecognition.rross.imageprocessor.impl;

import android.content.Context;
import com.amazonaws.regions.Regions;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import objectrecognition.rross.awslambdafunctions.invokers.FragmentToSentenceLambdaInvoker;
import objectrecognition.rross.imageprocessor.IComputerVisionSvc;
import objectrecognition.rross.imageprocessor.IObjectRecognition;

public class ComputerVisionSvcImpl implements IComputerVisionSvc {

    private IObjectRecognition objectRecognitionSVC;
    private FragmentToSentenceLambdaInvoker lamdaInvoker;


    public ComputerVisionSvcImpl(
        String svcHost,
        String svcEndPoint,
        String iterationId,
        Context context
    ) {
        lamdaInvoker = new FragmentToSentenceLambdaInvoker(context, Regions.US_EAST_1);
        objectRecognitionSVC = new ObjectRecognitionImpl(svcHost, svcEndPoint, iterationId);
    }

    public void identifyObject(
        String imageURL
    ) throws IOException, JSONException, ExecutionException, InterruptedException {
        Map<String, Double> sentenceFragments =
            objectRecognitionSVC.identifyObject(imageURL, "application/json");
        lamdaInvoker.invokeFunction(sentenceFragments);
    }

    public void identifyObject(
        File image
    ) throws Exception {
        Map<String, Double> sentenceFragments =
            objectRecognitionSVC.identifyObject(image, "application/octet-stream");
        lamdaInvoker.invokeFunction(sentenceFragments);
    }
}
