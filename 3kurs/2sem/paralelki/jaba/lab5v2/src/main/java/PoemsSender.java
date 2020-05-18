import java.io.*;
import java.net.Socket;
import java.util.*;

public class PoemsSender extends Thread {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static final int NUM_SONNETS = 154;
    private final String SONNETS_FILE_PATH;
    private static final Random r = new Random();
    
    public PoemsSender(Socket socket, String sonnetsFilePath) throws IOException {
        this.SONNETS_FILE_PATH = sonnetsFilePath;

        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        //в конструкторе получаем потоки ввода и вывода для общения с клиентом и вызвываем старт
        start();
    }
    @Override
    public void run() {
        String word;
        try {
            while (true) {
                //в отдельном потоке в бесконечном цикле отправляем клиенту доступные команды
                this.send("available commands: sonnet, stop\n");

                word = ((String) in.readObject()).trim();
                //получаем команду от сокета

                if (word.equals("stop")) {
                    PoemsServer.sendersList.remove(this);
                    in.close();
                    out.close();
                    break;
                } else if (word.equals("sonnet")) {
                    //если команда sonnet - отправляем соннет
                    this.send(getSonnetFromFile(SONNETS_FILE_PATH, r.nextInt(NUM_SONNETS)));
                } else {
                    this.send("unknown input\n");
                }
            }
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Client disconnected");
        }
    }
    
    private void send(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ignored) {}
    }
    
    private static String getSonnetFromFile(String sonnetsFilePath, int sonnetIndex) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(sonnetsFilePath)));
        for (int i = 0; i < sonnetIndex; i++) {
            while (!reader.readLine().isEmpty()){}
        }
        String sonnet = "#" + (sonnetIndex + 1) + "\n";
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            sonnet += line + "\n";
        }
        return sonnet;
    }
}
