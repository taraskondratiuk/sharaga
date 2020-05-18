import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static ServerSocket server;
    
    public static void main(String[] args) {
        try {
            server = new ServerSocket(1234);
            System.out.println("Server up");
            
            Socket socket1 = server.accept();
            Socket socket2 = server.accept();
            
            ClientsConnector sender1to2 = new ClientsConnector(socket1, socket2);
            ClientsConnector sender2to1 = new ClientsConnector(socket2, socket1);
            sender1to2.start();
            sender2to1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
