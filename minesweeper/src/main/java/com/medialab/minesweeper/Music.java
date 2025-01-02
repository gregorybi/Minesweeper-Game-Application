package com.medialab.minesweeper;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class Music {

    private Clip playing;

    public Music (String location) {
        try {

            InputStream is= getClass().getResourceAsStream(location);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new BufferedInputStream(is));

            playing = AudioSystem.getClip();
            playing.open(audioInput);
            playing.start();
            playing.loop(Clip.LOOP_CONTINUOUSLY);


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stopmusic (Clip clip_to_stop) {
        clip_to_stop.stop();
    }


}
