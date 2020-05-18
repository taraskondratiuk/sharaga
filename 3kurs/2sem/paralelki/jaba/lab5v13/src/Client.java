import java.io.*;
import java.net.Socket;

public class Client extends Thread {
    
    private static Socket clientSocket;
    private static BufferedReader systemInputReader;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static String key;
    
    public static void main(String[] args) {
        try {
            clientSocket = new Socket("localhost", 1234);
            systemInputReader = new BufferedReader(new InputStreamReader(System.in));
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            
            System.out.println("Enter your encryption key or pass empty line to generate it");
            key = systemInputReader.readLine();
            
            if (key.isEmpty()) {
                key = generateKey();
            }
            System.out.println("Your key: " + key);
            System.out.println("Type your messages");
            new Client().start();
            while (true) {
                String input = systemInputReader.readLine();
                byte[] encodedInput = xorWithKey(key, input.getBytes());
                out.writeObject(encodedInput);
                out.flush();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                byte[] encodedMsg = (byte[]) in.readObject();
                String msg = new String(xorWithKey(key, encodedMsg));
                System.out.println("=== " + msg);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    
    static byte[] xorWithKey(String key, byte[] message) {
        StringBuilder keySb = new StringBuilder(key);
        
        while (message.length > keySb.length()) {
            keySb.append(key);
        }
        byte[] keyBytes = keySb.toString().getBytes();
        byte[] res = new byte[message.length];
        for (int i = 0; i < message.length; i++) {
            res[i] = (byte) (message[i] ^ keyBytes[i]);
        }
        return res;
    }
    
    static String generateKey() {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 15; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}