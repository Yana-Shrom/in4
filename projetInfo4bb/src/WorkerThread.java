package src;

import java.io.*;
import java.net.*;

public class WorkerThread extends Thread {
    private Socket socket;
    private String workerIp;
    private int numCores;

    public int getNumCores() {
        return numCores;
    }

    public void setNumCores(int numCores) {
        this.numCores = numCores;
    }

    public WorkerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            
            // Demande d'informations sur le worker
            out.println("IDENTIFICATION");
            workerIp = in.readLine();
            numCores = Integer.parseInt(in.readLine());
            System.out.println("je suis passé");
            // Ajout du worker à la liste des workers du serveur
            Serveur.addWorkerLoad(workerIp, 0);

            while (true) {
                String message = in.readLine();

                if (message.equals("TASK")) {
                    // Récupération de l'intervalle à tester
                    int start = Integer.parseInt(in.readLine());
                    int end = Integer.parseInt(in.readLine());

                    // Exécution des calculs
                    int result = executeCalculations(start, end);

                    // Envoi du résultat au serveur
                    out.println(result);

                    // Mise à jour de la charge du worker
                    Serveur.addWorkerLoad(workerIp, 1);
                } else if (message.equals("BYE")) {
                    // Suppression du worker de la liste des workers du serveur
                    Serveur.removeWorker(workerIp);

                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWorkerIp() {
        return workerIp;
    }

    private int executeCalculations(int start, int end) {
        int maxPersistence = 0;

        for (int i = start; i <= end; i++) {
            int persistence = calculatePersistence(i);

            if (persistence > maxPersistence) {
                maxPersistence = persistence;
            }
        }

        return maxPersistence;
    }

    private int calculatePersistence(int n) {
        int persistence = 0;

        while (n >= 10) {
            int multiplication = 1;
            while (n != 0) {
                multiplication *= n % 10;
                n /= 10;
            }
            n = multiplication;
            persistence++;
        }
        return persistence;
    }
    public static void main(String[] args) {
        try{
            Socket sk= new Socket("localhost", 5034);
            WorkerThread worker= new WorkerThread(sk);
            worker.start();
        }catch(Exception e){
            System.err.println(e);
        }
    }
}

