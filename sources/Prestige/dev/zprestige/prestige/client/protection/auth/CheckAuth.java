package dev.zprestige.prestige.client.protection.auth;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.ProtectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CheckAuth {

    public static void run(String string, String string2) {
        if (!ProtectionManager.ip.equals("45.153.241.167")) {
            ProtectionManager.exit("N");
        }
        if (Prestige.Companion.getSession() == null) {
            ProtectionManager.exit("S");
        }
        try {
            Socket socket = new Socket("45.153.241.167", 8000);
            new PrintWriter(socket.getOutputStream(), true).println("check=" + string + ":" + string2 + ":" + Prestige.Companion.getSession().getHWID());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            if (!stringBuilder.toString().contains("Successfully processed login!")) {
                try {
                    ProtectionManager.exit("V " + stringBuilder);
                } catch (Throwable object2222) {
                    socket.close();
                    throw object2222;
                }
            }
            socket.close();
        } catch (IOException ignored) {

        }
    }
}
