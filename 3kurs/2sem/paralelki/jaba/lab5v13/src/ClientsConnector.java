import java.io.*;
import java.net.Socket;

class ClientsConnector extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    
    public ClientsConnector(Socket client1, Socket client2) throws IOException {
        this.in = new ObjectInputStream(client1.getInputStream());
        this.out = new ObjectOutputStream(client2.getOutputStream());
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                out.writeObject(in.readObject());
                out.flush();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
