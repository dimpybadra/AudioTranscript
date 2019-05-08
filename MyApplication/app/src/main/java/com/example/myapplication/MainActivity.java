package com.example.myapplication;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.O)
    public static void transcribeModelSelection(String fileName) throws Exception {
        Path path = Paths.get(fileName);
        byte[] content = Files.readAllBytes(path);

        try (SpeechClient speech = SpeechClient.create()) {
            // Configure request with video media type
            RecognitionConfig recConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(16000)
                            .setModel("video")
                            .build();
            //will read audio from video

            RecognitionAudio recognitionAudio =
                    RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(content)).build();

            RecognizeResponse recognizeResponse = speech.recognize(recConfig, recognitionAudio);
            SpeechRecognitionResult result = recognizeResponse.getResultsList().get(0);
            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf("Transcript : %s\n", alternative.getTranscript());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            transcribeModelSelection("any file name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}