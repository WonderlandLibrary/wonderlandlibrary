package dev.zprestige.prestige.client.protection.auth;

import dev.zprestige.prestige.client.managers.ProtectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class KeepAlive
{
    public static int run() {
        if (!ProtectionManager.ip.equals("45.153.241.167")) {
            ProtectionManager.exit("N");
        }
        try {
            Socket socket = new Socket("45.153.241.167", 8000);
            new PrintWriter(socket.getOutputStream(), true).println("keepAlive=2");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String string = sb.toString();
            try {
                if (!string.contains("JAR file not found.")) {
                    socket.close();
                    return Integer.parseInt(string);
                }
                ProtectionManager.exit("N");
                socket.close();
                return 0;
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
        catch (IOException ex) {
            return 0;
        }
    }
}
