package objectrecognition.rross.imageprocessor;

import org.json.JSONException;
import org.springframework.web.util.UriComponents;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public interface IObjectRecognition {
    Map<String,Double> identifyObject(String imageURL, String contentType) throws IOException, JSONException;
    Map<String,Double> identifyObject(File image, String contentType) throws IOException, URISyntaxException, JSONException;
    UriComponents getPredictionURI();
    Map<String, Double> parsePredictionsFromJsonString(String jsonResponse) throws JSONException;
}
