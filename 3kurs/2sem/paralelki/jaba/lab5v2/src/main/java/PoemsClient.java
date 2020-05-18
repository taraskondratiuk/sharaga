import java.io.*;
import java.net.Socket;

public class PoemsClient {
    
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    
    public static void main(String[] args) {
        try {
            try {

                clientSocket = new Socket("localhost", 4004);
                //подключаемся к локальному сокет серверу на порте 4004
                reader = new BufferedReader(new InputStreamReader(System.in));
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                while (true) {
                    //в бесконечном цикле получаем сообщения от сервера и отправляем ответы
                    String serverMsg = (String)in.readObject();
                    System.out.println(serverMsg);
                    String line = reader.readLine();
                    out.writeObject(line + "\n");
                    out.flush();
                    if (line.equals("stop")) {
                        break;
                    }
                    serverMsg = (String)in.readObject();
                    System.out.println(serverMsg);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Client closed");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}