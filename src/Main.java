import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tablero de Gato");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Player player = new Player();
        frame.add(player);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Crear instancia de ClienteGato después de que se muestre la ventana
        ClienteGato cliente = new ClienteGato(player);
    }
}

class ClienteGato {
    static final String SERVIDOR_IP = "127.0.0.1";
    static final int PUERTO = 12345;

    private Socket socket;
    private PrintWriter salida;

    ClienteGato(Player player) {
        try {
            socket = new Socket(SERVIDOR_IP, PUERTO);
            salida = new PrintWriter(socket.getOutputStream(), true);

            // Manejar clics del jugador y enviar coordenadas al servidor
            player.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = e.getY() / 100;
                    int columna = e.getX() / 100;
                    System.out.println("Fila: " + fila + ", Columna: " + columna);
                    enviarCoordenadas(fila, columna);
                }
            });

            System.out.println("Conectado al servidor.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarCoordenadas(int fila, int columna) {
        if (salida != null) {
            salida.println(fila + " " + columna);
        }
    }
}

class Player extends JPanel {

    Player() {
        setPreferredSize(new Dimension(100 * 3, 100 * 3));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar las líneas verticales y horizontales
        for (int i = 1; i < 3; i++) {
            g.setColor(Color.BLACK);
            g.drawLine(i * 100, 0, i * 100, getHeight());
            g.drawLine(0, i * 100, getWidth(), i * 100);
        }
    }
}
