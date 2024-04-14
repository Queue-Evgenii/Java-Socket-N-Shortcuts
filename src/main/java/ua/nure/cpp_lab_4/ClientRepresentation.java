package ua.nure.cpp_lab_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientRepresentation {
    private final PrintWriter output;
    private final BufferedReader input;
    private final int clientId;

    public ClientRepresentation(int clientId, PrintWriter output, BufferedReader input) {
        this.clientId = clientId;
        this.output = output;
        this.input = input;
    }
    public String readLine() throws IOException {
        return input.readLine();
    }
    public void sendMessage(String message) {
        output.println(message);
    }
    public int getClientId() {
        return clientId;
    }
}
