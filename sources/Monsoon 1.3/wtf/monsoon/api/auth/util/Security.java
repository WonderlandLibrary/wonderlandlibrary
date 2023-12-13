package wtf.monsoon.api.auth.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Security {

    public static boolean wiresharkRunning() throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("tasklist.exe");

        Process process = pb.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String tasks;
        while ((tasks = br.readLine()) != null)
        {
            if (tasks.toLowerCase().contains("wireshark"))
            {
                return true;
            }
        }

        return false;
    }
}