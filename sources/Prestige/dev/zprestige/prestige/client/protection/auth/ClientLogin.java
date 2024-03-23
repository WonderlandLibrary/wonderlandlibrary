package dev.zprestige.prestige.client.protection.auth;

import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.HWIDGrabber;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientLogin {

    public static String run(String string, String string2) {
        if (!ProtectionManager.ip.equals("45.153.241.167")) {
            ProtectionManager.exit("N");
        }
        try {
            Socket socket = new Socket("45.153.241.167", 8000);
            new PrintWriter(socket.getOutputStream(), true).println("clientlogin=" + string + ":" + string2 + ":" + HWIDGrabber.getHWID());
            try {
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
            return "Internal error whilst logging in, please contact the developer.";
        }
    }
}
