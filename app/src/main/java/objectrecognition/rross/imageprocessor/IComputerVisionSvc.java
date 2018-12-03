package objectrecognition.rross.imageprocessor;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javazoom.jl.decoder.JavaLayerException;

public interface IComputerVisionSvc {
    void identifyObject(String imageURL) throws IOException, JavaLayerException, JSONException, ExecutionException, InterruptedException;
    void identifyObject(File image) throws IOException, JavaLayerException, URISyntaxException, JSONException, Exception;
}
