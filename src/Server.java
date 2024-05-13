import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Server {

    private static final int PUERTO = 12345;
    private static final int MAX_JUGADORES = 2;
    private List<GatoServidorThread> jugadores;

    public Server() {
        jugadores = new ArrayList<>();
    }

    public void iniciar() {
        try (ServerSocket servidorSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor de Gato iniciado. Esperando jugadores...");

            while (true) {
                if (jugadores.size() < MAX_JUGADORES) {
                    Socket socket = servidorSocket.accept();
                    GatoServidorThread jugador = new GatoServidorThread(socket, this);
                    jugadores.add(jugador);
                    jugador.start();
                    System.out.println("Nuevo jugador conectado: " + jugador.getId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void manejarJugada(GatoServidorThread jugador, int fila, int columna) {
        // Aquí maneja la lógica del juego y comunica la jugada a los otros jugadores
    }

    public static void main(String[] args) {
        Server servidor = new Server();
        servidor.iniciar();
    }
}

class GatoServidorThread extends Thread {
    private Socket socket;
    private Server servidor;
    private BufferedReader entrada;
    private PrintWriter salida;

    public GatoServidorThread(Socket socket, Server servidor) {
        this.socket = socket;
        this.servidor = servidor;
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String mensaje = entrada.readLine();
                if (mensaje != null) {
                    System.out.println("Mensaje recibido de " + getId() + ": " + mensaje);

                    // Analizar y manejar el mensaje (por ejemplo, jugada del jugador)
                    // Ejemplo: servidor.manejarJugada(this, fila, columna);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para enviar un mensaje al cliente
    public void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }
}
