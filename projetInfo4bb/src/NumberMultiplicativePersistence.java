package src;

public class NumberMultiplicativePersistence {
    public static int calculate(int n){
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




    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String host, int port) {
        try {
            // Connexion au serveur
            socket = new Socket(host, port);

            // Création des flux d'entrée et de sortie
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Envoi de la commande "CONNECT"
            out.println("CONNECT");
            out.flush();

            // Lecture de la réponse du serveur
            String response = in.readLine();

            // Vérification de la réponse du serveur
            if (!response.equals("OK")) {
                throw new RuntimeException("Erreur de connexion au serveur : " + response);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur de connexion au serveur : " + e.getMessage());
        }
    }

    public void close() {
        try {
            // Envoi de la commande "DISCONNECT"
            out.println("DISCONNECT");
            out.flush();

            // Fermeture de la connexion
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
    public String sendCommand(String command) {
        try {
            // Envoi de la commande au serveur
            out.println(command);
            out.flush();

            // Lecture de la réponse du serveur
            String response = in.readLine();

            // Vérification de la réponse du serveur
            if (response == null) {
                throw new RuntimeException("Erreur de communication avec le serveur : réponse nulle");
            }

            // Traitement de la réponse du serveur
            if (response.startsWith("ERROR")) {
                throw new RuntimeException(response.substring(6));
            } else {
                return response;
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur de communication avec le serveur : " + e.getMessage());
        }
    }
}