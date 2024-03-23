package dev.zprestige.prestige.client.protection.auth;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.protection.HWIDGrabber;
import dev.zprestige.prestige.client.protection.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HWIDLogin {

    public static boolean run(String s) {
        try {
            Socket socket = new Socket("45.153.241.167", 8000);
            new PrintWriter(socket.getOutputStream(), true).println("hwid=" + s);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            if (sb.toString().contains("Successfully processed HWID!")) {
                String[] split = sb.toString().split("\\(")[1].split(":");
                Prestige.Companion.setSession(new Session(Integer.parseInt(split[0]), split[1], split[2], split[3], s));
                try {
                    socket.close();
                    return true;
                } catch (Throwable t) {
                    try {
                        socket.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                    throw t;
                }
            }
        }
        catch (IOException ignored) {}
        return false;
    }

    private static void login() {
        if (!run(HWIDGrabber.getHWID())) {
            String hwid = HWIDGrabber.getHWIDWithWin();
            try {
                Thread.sleep(2500L);
            }
            catch (InterruptedException cause) {
                throw new RuntimeException(cause);
            }
            if (hwid != null) {
                run(hwid);
            }
        }
    }
}