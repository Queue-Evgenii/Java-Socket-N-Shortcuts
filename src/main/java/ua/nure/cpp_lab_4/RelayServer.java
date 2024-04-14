package ua.nure.cpp_lab_4;
import java.io.*;
import java.net.*;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class RelayServer {
    public int PORT = 8800;
    public int MAX_CONNECTION_SIZE = 100;
    private ServerSocket serverSocket;
    private Map<Integer, PrintWriter> clients = new HashMap<>();

    public RelayServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT);
    }

    private ClientRepresentation[] clientsR = new ClientRepresentation[MAX_CONNECTION_SIZE];
    private int length = 0;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private int clientId;
    public void startServer() throws IOException {
        while (true) {
            socket = serverSocket.accept();
            if (createIOStreams()) return;
            connectClient();

            new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = clientsR[clientId].readLine()) != null) {
                        clientId = retrieveId(inputLine);
                        inputLine = retrieveMessage(inputLine);

                        respondToClients(inputLine);
                    }
                } catch (IOException e) {
                    System.out.println("Exception in ClientHandler: " + e.getMessage());
                } finally {
                    closeSocket();
                }
            }).start();
        }
    }

    private boolean createIOStreams() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Exception in ClientHandler: " + e.getMessage());
            return true;
        }
        return false;
    }
    private void connectClient() {
        clientId = length;
        clientsR[length] = new ClientRepresentation(clientId, output, input);
        clientsR[length].sendMessage("id=" + clientId);
        ++length;

        System.out.println("Client with id " + clientId + " successfully connected!");
    }
    private int retrieveId(String inputLine) throws InvalidParameterException {
        try {
            return Integer.parseInt(inputLine.split("\\?")[0].split("=")[1]);
        } catch (Exception e) {
            throw new InvalidParameterException();
        }
    }
    private String retrieveMessage(String inputLine) throws InvalidParameterException {
        try {
            return inputLine.split("\\?")[1];
        } catch (Exception e) {
            throw new InvalidParameterException();
        }
    }
    private void respondToClients(String inputLine) {
        for (ClientRepresentation el : clientsR) {
            if (el != null && el.getClientId() != clientId) {
                el.sendMessage(inputLine);
            }
        }
    }
    private void closeSocket() {
        try {
            if (output != null) {
                clients.remove(clientId);
                output.close();
            }
            if (input != null) {
                input.close();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
