package src;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Serveur {
    private static final int PORT = 5034;
    private static final int MAX_WORKERS = 10;

    private static ServerSocket serverSocket;
    private static Hashtable<String, Integer> workerLoads = new Hashtable<>();
    private static List<WorkerThread> workers = new ArrayList<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur en attente de connexions...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connexion entrante : " + socket.getInetAddress());

                if (workers.size() < MAX_WORKERS) {
                    WorkerThread worker = new WorkerThread(socket);
                    workers.add(worker);
                    worker.start();
                } else {
                    socket.close();
                    System.out.println("Refus de connexion : nombre maximum de workers atteint.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addWorkerLoad(String workerIp, int load) {
        workerLoads.put(workerIp, load);
    }

    public static void removeWorker(String workerIp) {
        workerLoads.remove(workerIp);

        for (WorkerThread worker : workers) {
            if (worker.getWorkerIp().equals(workerIp)) {
                workers.remove(worker);
                break;
            }
        }
    }
}