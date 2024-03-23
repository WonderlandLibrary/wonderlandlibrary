/*
qProtect is trash
*/
package dev.zprestige.prestige.client.protection.auth;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.ProtectionManager;

public class GetConfig {

    public static String run() {
        if (!ProtectionManager.ip.equals("45.153.241.167")) {
            ProtectionManager.exit(new StringBuilder().append("N").toString());
        }
        if (Prestige.Companion.getSession() == null) {
            ProtectionManager.exit(new StringBuilder().append("S").toString());
        }
        try {
            Socket socket = new Socket("45.153.241.167", 8000);
            new PrintWriter(socket.getOutputStream(), true).println("getconfigs=" + Prestige.Companion.getSession().getUsername() + ":" + Prestige.Companion.getSession().getID() + ":" + Prestige.Companion.getSession().getHWID());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line;
                if ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                try {
                    socket.close();
                    return sb.toString();
                }
                catch (Throwable t) {
                    try {
                        socket.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                    throw t;
                }
            }
        } catch (IOException ex) {
            return "Internal error whilst creating config, please contact the developer.";
        }
    }
}
