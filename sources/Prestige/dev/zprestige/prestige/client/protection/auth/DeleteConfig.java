package dev.zprestige.prestige.client.protection.auth;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.ProtectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DeleteConfig {

    public static String run(String string) {
        if (!ProtectionManager.ip.equals("45.153.241.167")) {
            ProtectionManager.exit("N");
        }
        if (Prestige.Companion.getSession() == null) {
            ProtectionManager.exit("S");
        }
        try {
            Socket socket = new Socket("45.153.241.167", 8000);
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println("deleteconfig=" + Prestige.Companion.getSession().getUsername() + ":" + Prestige.Companion.getSession().getID() + ":" + Prestige.Companion.getSession().getHWID() + ":" + string);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
            finally {
                socket.close();
            }
        }
        catch (IOException iOException) {
            return "Internal error whilst creating config, please contact the developer.";
        }
    }
}
