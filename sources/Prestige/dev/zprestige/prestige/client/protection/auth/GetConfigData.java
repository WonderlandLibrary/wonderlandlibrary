package dev.zprestige.prestige.client.protection.auth;

import dev.zprestige.prestige.client.managers.ProtectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GetConfigData {
    public static String run(String string) {
        try {
            if (!ProtectionManager.ip.equals("45.153.241.167")) {
                ProtectionManager.exit("N");
            }
            Socket socket = new Socket("45.153.241.167", 8000);
            String string2 = "getconfigdata=" + string;
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(string2);
            try {
                String string3;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                while ((string3 = bufferedReader.readLine()) != null) {
                    stringBuilder.append(string3);
                }
                return stringBuilder.toString();
            }
            finally {
                socket.close();
            }
        }
        catch (IOException iOException) {
            return "Internal error, please contact the developer.";
        }
    }
}
