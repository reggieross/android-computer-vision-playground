package objectrecognition.rross.texttospeach.impl;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyPresigningClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechPresignRequest;
import com.amazonaws.services.polly.model.Voice;

import java.io.IOException;
import java.net.URL;

import objectrecognition.rross.awslambdafunctions.AwsCredintials;
import objectrecognition.rross.texttospeach.ITextToSpeech;

import static android.content.ContentValues.TAG;

public class TextToSpeechImpl implements ITextToSpeech {
    private final AmazonPollyPresigningClient polly;
    private final Voice voice;
    private AwsCredintials awsCredintials;

    public TextToSpeechImpl(Context context, Regions region, String languageCode) {
        awsCredintials = new AwsCredintials(context, region);
        polly = new AmazonPollyPresigningClient(awsCredintials.getCredentialsProvider());

        DescribeVoicesRequest describeVoicesRequest =
            new DescribeVoicesRequest()
                .withLanguageCode(languageCode);
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        voice = describeVoicesResult.getVoices().get(0);
    }

    public void transformTextToSpeech(String phrase) throws IOException {
        URL speech = this.synthesize(phrase, OutputFormat.Mp3);
        this.speakUtterance(speech);
    }

    private URL synthesize(String text, OutputFormat format) throws IOException {
        SynthesizeSpeechPresignRequest synthesizeSpeechPresignRequest =
            new SynthesizeSpeechPresignRequest()
                .withText(text)
                .withVoiceId(voice.getId())
                .withOutputFormat(format);

        return polly.getPresignedSynthesizeSpeechUrl(synthesizeSpeechPresignRequest);
    }

    private void speakUtterance(URL presignedSynthesizeSpeechUrl) {

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(presignedSynthesizeSpeechUrl.toString());
        } catch (IOException e) {
            Log.e(TAG, "Unable to set data source for the media player! " + e.getMessage());
        }

        mediaPlayer.prepareAsync();

        mediaPlayer.setOnPreparedListener(mp -> mp.start());

        mediaPlayer.setOnCompletionListener(mp -> mp.release());
    }
}
