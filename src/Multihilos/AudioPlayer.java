
package Multihilos;

/**
 *
 * @author Martin Fuentes
 */
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer implements Runnable {
    private String filePath;

    public AudioPlayer(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
            audioLine.open(format);
            audioLine.start();

            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesRead = 0;

            while ((bytesRead = audioStream.read(buffer, 0, bufferSize)) != -1) {
                audioLine.write(buffer, 0, bytesRead);
            }

            audioLine.drain();
            audioLine.close();
            audioStream.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "path/to/audio/file.wav";
        Thread audioThread = new Thread(new AudioPlayer(filePath));
        audioThread.start();
    }
}

