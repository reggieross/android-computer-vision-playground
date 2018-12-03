package objectrecognition.rross.imageprocessor.impl;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import objectrecognition.rross.imageprocessor.IObjectRecognition;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ObjectRecognitionImpl implements IObjectRecognition {

    private String API_KEY = "9eb1cd61cf8f42299c375250504053a3";
    private static String URI_SCHEME = "https";
    private UriComponents predictionURI;

    public ObjectRecognitionImpl(
        String svcHost,
        String svcEndPoint,
        String iterationID
    ) {
        this.predictionURI = buildPredictionURI(
            svcHost,
            svcEndPoint,
            iterationID
        );
    }

    public UriComponents getPredictionURI() {
        return this.predictionURI;
    }

    public Map<String, Double> identifyObject(
        File content,
        String contentType
    ) throws IOException, JSONException {
        return submitRequest(
            RequestBody.create(MediaType.parse("application/octet-stream"), content),
            contentType
        );
    }

    public Map<String, Double> identifyObject(
        String fileURL,
        String contentType
    ) throws IOException, JSONException {
        return submitRequest(
            RequestBody.create(
                MediaType.parse(contentType),
                "{ \"Url\": \"" + fileURL + "\"}"
            ),
            contentType
        );
    }

    private Map<String, Double> submitRequest(
        RequestBody requestBody,
        String contentType
    ) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .build();

        Request request = new Request
            .Builder()
                .addHeader("Prediction-Key", API_KEY)
                .addHeader("Content-Type", contentType)
                .url(getPredictionURI().toString())
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return parsePredictionsFromJsonString(response.body().string());
    }

    @SuppressLint("NewApi")
    public Map<String, Double> parsePredictionsFromJsonString(
        String jsonResponse
    ) throws JSONException {
        Map<String, Double> predictionTags = new HashMap<>();
        JSONObject jsonObj = new JSONObject(jsonResponse);

        if (!jsonObj.has("predictions")) {
            return predictionTags;
        }

        JSONArray predictions = jsonObj.getJSONArray("predictions");

        if (predictions.length() == 0) {
            return predictionTags;
        }

        for (int i = 0; i < predictions.length(); i++) {
            JSONObject prediction = predictions.getJSONObject(i);
            String tagName = prediction.get("tagName").toString();
            Double probability = Double.parseDouble(prediction.get("probability").toString());

            predictionTags.put(tagName, probability);
        }

        return predictionTags
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 0.60)
            .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
    }

    private UriComponents buildPredictionURI(
        String svcHost,
        String svcEndPoint,
        String iterationID
    ) {
        return UriComponentsBuilder.newInstance()
            .scheme(URI_SCHEME)
            .host(svcHost)
            .path(svcEndPoint)
            .queryParam("iterationId", iterationID)
            .build();
    }
}
