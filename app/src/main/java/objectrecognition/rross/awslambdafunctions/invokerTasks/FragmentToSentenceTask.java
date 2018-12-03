package objectrecognition.rross.awslambdafunctions.invokerTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Map;

import objectrecognition.rross.awslambdafunctions.interfaces.IFragmentToSentenceSvc;
import objectrecognition.rross.texttospeach.ITextToSpeech;

import static android.content.ContentValues.TAG;

public class FragmentToSentenceTask  extends AsyncTask<Map<String, Double>, Void, Void> {
    private IFragmentToSentenceSvc fragmentToSentenceSvc;
    private ITextToSpeech textToSpeechsvc;
    private String result;

    public FragmentToSentenceTask(
        IFragmentToSentenceSvc fragmentToSentenceSvc,
        ITextToSpeech textToSpeechsvc
    ) {
        this.fragmentToSentenceSvc = fragmentToSentenceSvc;
        this.textToSpeechsvc = textToSpeechsvc;
    }

    @Override
    protected Void doInBackground(Map<String, Double>... params) {
        try {
            String sentence = fragmentToSentenceSvc.fragmentToSentence(params[0]);
            textToSpeechsvc.transformTextToSpeech(sentence);
        } catch (Exception lfe) {
            Log.e(TAG, "Failed to invoke echo", lfe);
        }

        return null;
    }
}
