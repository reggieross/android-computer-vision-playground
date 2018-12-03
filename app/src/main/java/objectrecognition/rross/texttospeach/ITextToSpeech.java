package objectrecognition.rross.texttospeach;

import java.io.IOException;

public interface ITextToSpeech {
    void transformTextToSpeech(String text) throws IOException;
}