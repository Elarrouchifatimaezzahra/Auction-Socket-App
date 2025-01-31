import java.io.*;
import java.net.*;

public class AuctionClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(in.readLine());  // Message de bienvenue
        System.out.print("Entrez votre nom : ");
        String name = userInput.readLine();
        out.println(name);

        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        String userBid;
        while ((userBid = userInput.readLine()) != null) {
            out.println(userBid);
        }
    }
}
