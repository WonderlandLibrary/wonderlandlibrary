// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.security;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.minecraft.util.Util;

public class AntiVM
{
    public static boolean run() {
        return Util.getOSType() == Util.EnumOS.WINDOWS && (run("wmic computersystem get model", "Model", new String[] { "virtualbox", "vmware", "kvm", "hyper-v" }) && run("WMIC BIOS GET SERIALNUMBER", "SerialNumber", new String[] { "0" }) && run("wmic baseboard get Manufacturer", "Manufacturer", new String[] { "Microsoft Corporation" }));
    }
    
    private static boolean run(final String command, final String startsWith, final String[] closePhrase) {
        try {
            final Process p = Runtime.getRuntime().exec(command);
            final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!line.startsWith(startsWith) && !line.equals("")) {
                    final String model = line.replaceAll(" ", "");
                    if (closePhrase.length > 1) {
                        final String[] stringArray = closePhrase;
                        for (int n = closePhrase.length, n2 = 0; n2 < n; ++n2) {
                            final String str = stringArray[n2];
                            if (model.contains(str)) {
                                Minecraft.getMinecraft().shutdownMinecraftApplet();
                                return false;
                            }
                        }
                    }
                    else if (model.equals(closePhrase[0])) {
                        Minecraft.getMinecraft().shutdownMinecraftApplet();
                        return false;
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
