
package Multihilos;

/**
 *
 * @author Martin Fuentes
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class RelojDigitalAnalogico extends JFrame {
    private JLabel labelDigital;
    private JPanel panelAnalogico;
    private SimpleDateFormat formatoHora;
    private boolean relojDetenido;

    public RelojDigitalAnalogico() {
        setTitle("Reloj Digital y Analógico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        JPanel panelRelojes = new JPanel();
        panelRelojes.setLayout(new CardLayout());

        labelDigital = new JLabel();
        panelAnalogico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawClock(g);
            }
        };

        formatoHora = new SimpleDateFormat("hh:mm:ss a");

        panelRelojes.add(panelAnalogico, "ANALOGICO");
        panelRelojes.add(labelDigital, "DIGITAL");

        add(panelRelojes, BorderLayout.CENTER);

        JRadioButton btnDigital = new JRadioButton("Digital");
        btnDigital.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRelojDigital();
            }
        });

        JRadioButton btnAnalogico = new JRadioButton("Analógico");
        btnAnalogico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRelojAnalogico();
            }
        });

        ButtonGroup grupoBotones = new ButtonGroup();
        grupoBotones.add(btnDigital);
        grupoBotones.add(btnAnalogico);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnDigital);
        panelBotones.add(btnAnalogico);

        JButton btnDetener = new JButton("Detener");
        btnDetener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detenerReloj();
            }
        });

        JButton btnIniciar = new JButton("Iniciar");
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarReloj();
            }
        });

        panelBotones.add(btnDetener);
        panelBotones.add(btnIniciar);

        add(panelBotones, BorderLayout.NORTH);

        setVisible(true);

        mostrarRelojDigital(); 

        iniciarReloj();
    }

    private void iniciarReloj() {
        relojDetenido = false;

        Thread threadReloj = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!relojDetenido) {
                    Date fechaActual = new Date();
                    int horas = fechaActual.getHours();
                    int minutos = fechaActual.getMinutes();
                    int segundos = fechaActual.getSeconds();

                    panelAnalogico.repaint(); // Redibujar el reloj analógico

                    String horaActual = formatoHora.format(fechaActual);

                    labelDigital.setText("<html><center><font size='50'>" + horaActual + "</font></center></html>");

                    try {
                        Thread.sleep(1000); // Actualiza cada segundo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        threadReloj.start();
    }

    private void detenerReloj() {
        relojDetenido = true;
    }

    private void mostrarRelojDigital() {
        CardLayout cardLayout = (CardLayout) labelDigital.getParent().getLayout();
        cardLayout.show(labelDigital.getParent(), "DIGITAL");
    }

    private void mostrarRelojAnalogico() {
        CardLayout cardLayout = (CardLayout) panelAnalogico.getParent().getLayout();
        cardLayout.show(panelAnalogico.getParent(), "ANALOGICO");
    }

    private void drawClock(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int panelWidth = panelAnalogico.getWidth();
        int panelHeight = panelAnalogico.getHeight();
        int clockSize = Math.min(panelWidth, panelHeight) - 20;

        int centerX = panelWidth / 2;
        int centerY = panelHeight / 2;

        // Dibujar el fondo del reloj
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - clockSize / 2, centerY - clockSize / 2, clockSize, clockSize);

     // Dibujar los números del reloj
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 14));
    FontMetrics fm = g2d.getFontMetrics();

    for (int i = 1; i <= 12; i++) {
        double angle = Math.toRadians(360 - (i * 30) + 90);
        int numberWidth = fm.stringWidth(String.valueOf(i));
        int numberHeight = fm.getHeight();
        int numberX = (int) (centerX + Math.cos(angle) * clockSize * 0.4 - numberWidth / 2);
        int numberY = (int) (centerY - Math.sin(angle) * clockSize * 0.4 + numberHeight / 4);
        g2d.drawString(String.valueOf(i), numberX, numberY);
    }

        // Dibujar los punteros del reloj
        Date fechaActual = new Date();
        int horas = fechaActual.getHours();
        int minutos = fechaActual.getMinutes();
        int segundos = fechaActual.getSeconds();

        double angleSegundos = Math.toRadians((segundos * 6) - 90);
        double angleMinutos = Math.toRadians((minutos * 6) - 90);
        double angleHoras = Math.toRadians(((horas % 12) * 30) + (minutos * 0.5) - 90);

        int segundosX = (int) (centerX + Math.cos(angleSegundos) * clockSize * 0.45);
        int segundosY = (int) (centerY + Math.sin(angleSegundos) * clockSize * 0.45);
        int minutosX = (int) (centerX + Math.cos(angleMinutos) * clockSize * 0.4);
        int minutosY = (int) (centerY + Math.sin(angleMinutos) * clockSize * 0.4);
        int horasX = (int) (centerX + Math.cos(angleHoras) * clockSize * 0.3);
        int horasY = (int) (centerY + Math.sin(angleHoras) * clockSize * 0.3);

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawLine(centerX, centerY, segundosX, segundosY);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(centerX, centerY, minutosX, minutosY);
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawLine(centerX, centerY, horasX, horasY);
    }

 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RelojDigitalAnalogico::new);
    }
}

