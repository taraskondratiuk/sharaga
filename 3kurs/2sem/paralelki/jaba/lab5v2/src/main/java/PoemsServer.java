import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class PoemsServer {
    protected static List<PoemsSender> sendersList = new ArrayList<>();
    private static ServerSocket server;
    private static String SONNETS_FILE_NAME;
    
    public static void main(String[] args) {
        SONNETS_FILE_NAME = args[0];
        
        try {
            try {
                // с помощью класса ServerSocket создаем обьект сервеа на порте 4004
                server = new ServerSocket(4004);
                System.out.println("Server turned on");
                
                while (true) {
                    //в бесконечном цикле принимаем новые соединения
                    Socket socket = server.accept();
                    try {
                        //добавляем в список получателей новые обьекты класса poemsSender для каждого соединения
                        sendersList.add(new PoemsSender(socket, SONNETS_FILE_NAME));
                        System.out.println("Client connected");
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                        socket.close();
                    }
                }
                
            } finally {
                System.out.println("Server turned off");
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
