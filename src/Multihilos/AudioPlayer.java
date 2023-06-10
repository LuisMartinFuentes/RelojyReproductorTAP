
package Multihilos;

/**
 *
 * @author Martin Fuentes
 */
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;

public class AudioPlayer extends JFrame {
    private JButton playButton, pauseButton, restartButton, forwardButton, backwardButton, deleteButton, addButton;
    private JList<String> audioList;
    private DefaultListModel<String> audioListModel;
    private Clip audioClip;

    public AudioPlayer() {
        setTitle("Reproductor de Audio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        audioListModel = new DefaultListModel<>();
        audioList = new JList<>(audioListModel);
        JScrollPane scrollPane = new JScrollPane(audioList);
        scrollPane.setPreferredSize(new Dimension(380, 150));
        add(scrollPane);

        playButton = new JButton("Reproducir");
        add(playButton);

        pauseButton = new JButton("Pausa");
        add(pauseButton);

        restartButton = new JButton("Reiniciar");
        add(restartButton);

        forwardButton = new JButton("Adelantar");
        add(forwardButton);

        backwardButton = new JButton("Atrasar");
        add(backwardButton);

        deleteButton = new JButton("Eliminar");
        add(deleteButton);

        addButton = new JButton("Agregar");
        add(addButton);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread playThread = new Thread(new Runnable() {
                    public void run() {
                        playAudio();
                    }
                });
                playThread.start();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseAudio();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartAudio();
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forwardAudio();
            }
        });

        backwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backwardAudio();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteAudio();
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAudio();
            }
        });
    }

    private void playAudio() {
        if (audioList.getSelectedIndex() != -1) {
            String audioFile = audioListModel.getElementAt(audioList.getSelectedIndex());
            try {
                if (audioClip != null && audioClip.isRunning()) {
                    audioClip.stop();
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(audioFile));
                audioClip = AudioSystem.getClip();
                audioClip.open(audioStream);
                audioClip.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void pauseAudio() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

    private void restartAudio() {
        if (audioClip != null) {
            audioClip.setFramePosition(0);
            audioClip.start();
        }
    }

    private void forwardAudio() {
        if (audioClip != null) {
            int framePosition = audioClip.getFramePosition();
            audioClip.setFramePosition(framePosition + 1000); 
        }
    }

    private void backwardAudio() {
        if (audioClip != null) {
            int framePosition = audioClip.getFramePosition();
            audioClip.setFramePosition(framePosition - 1000); 
        }
    }

    private void deleteAudio() {
        int selectedIndex = audioList.getSelectedIndex();
        if (selectedIndex != -1) {
            audioListModel.remove(selectedIndex);
        }
    }

    private void addAudio() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de audio", "wav", "mp3", "ogg");
        fileChooser.setFileFilter(filter);
        fileChooser.setMultiSelectionEnabled(true);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            for (File file : selectedFiles) {
                audioListModel.addElement(file.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AudioPlayer player = new AudioPlayer();
                player.setVisible(true);
            }
        });
    }
}
