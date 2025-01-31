import java.io.*;
import java.net.*;
import java.util.*;

public class AuctionServer {
    private static int highestBid = 0;
    private static String highestBidder = "";
    private static List<PrintWriter> clientOutputs = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Le serveur d'enchères est en cours d'exécution...");
        ServerSocket serverSocket = new ServerSocket(12345);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientOutputs.add(out);
        }

        public void run() {
            try {
                out.println("Bienvenue à l'enchère! Le prix de départ est de " + highestBid);
                String name = in.readLine();
                out.println("Bienvenue " + name + "!");

                String bid;
                while ((bid = in.readLine()) != null) {
                    int newBid = Integer.parseInt(bid);
                    if (newBid > highestBid) {
                        highestBid = newBid;
                        highestBidder = name;
                        broadcast("Nouvelle enchère de " + highestBid + " par " + highestBidder);
                    } else {
                        out.println("Votre enchère doit être supérieure à " + highestBid);
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clientOutputs) {
                writer.println(message);
            }
        }
    }
}
