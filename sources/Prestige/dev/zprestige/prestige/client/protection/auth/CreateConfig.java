package dev.zprestige.prestige.client.protection.auth;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.ProtectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CreateConfig {

    public static String run(String string, String string2, String string3, int n) {
        if (!ProtectionManager.ip.equals("45.153.241.167")) {
            ProtectionManager.exit("N");
        }
        if (Prestige.Companion.getSession() == null) {
            ProtectionManager.exit("S");
        }
        try {
            Socket socket = new Socket("45.153.241.167", 8000);
            String string6 = "createconfig=" + string + ":" + Prestige.Companion.getSession().getID() + ":" + string2 + ":" + Prestige.Companion.getSession().getUsername() + ":" + string3 + ":" + n + ":" + Prestige.Companion.getSession().getHWID();
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(string6);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String result;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            try {
                result = stringBuilder.toString();
            }
            catch (Throwable throwable) {
                try {
                    socket.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            socket.close();
            return result;
        }
        catch (IOException iOException) {
            return "Internal error whilst creating config, please contact the developer.";
        }
    }
}
