package src;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    
    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server.");
            
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void getResults() {
        try {
            out.writeUTF("get_results");
            out.flush();
            
            int numResults = in.readInt();
            List<Integer> results = new ArrayList<>();
            for (int i = 0; i < numResults; i++) {
                results.add(in.readInt());
            }
            
            System.out.println("Results: " + results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void getStats() {
        try {
            out.writeUTF("get_stats");
            out.flush();
            
            int numValues = in.readInt();
            Map<Integer, Integer> stats = new HashMap<>();
            for (int i = 0; i < numValues; i++) {
                int key = in.readInt();
                int value = in.readInt();
                stats.put(key, value);
            }
            
            System.out.println("Statistics: " + stats);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void getNumsWithMaxPersistence() {
        try {
            out.writeUTF("get_nums_with_max_persistence");
            out.flush();
            
            int numValues = in.readInt();
            List<Integer> nums = new ArrayList<>();
            for (int i = 0; i < numValues; i++) {
                nums.add(in.readInt());
            }
            
            System.out.println("Numbers with maximum persistence: " + nums);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            out.writeUTF("exit");
            out.flush();
            
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void command(String commande){
        switch(commande){
            case "close" : 
                close();
                break;
            case "getResults" : 
                getResults();
                break;
        }
    }
    public static void main(String[] args) {
       try{
            Client client = new Client("localhost", 5034);
            while(true){
                Scanner sc = new Scanner(System.in);
                System.out.print(">>> ");
                String str = sc.nextLine();
                System.out.println("");
                client.command(str);
            }
        }catch(Exception e){
            System.err.println(e);
       }

    }
}
       





   