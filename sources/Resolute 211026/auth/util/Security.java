// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.auth.util;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Security
{
    public static boolean wiresharkRunning() throws IOException {
        final ProcessBuilder pb = new ProcessBuilder(new String[0]);
        pb.command("tasklist.exe");
        final Process process = pb.start();
        final BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String tasks;
        while ((tasks = br.readLine()) != null) {
            if (tasks.toLowerCase().contains("wireshark")) {
                return true;
            }
        }
        return false;
    }
}
