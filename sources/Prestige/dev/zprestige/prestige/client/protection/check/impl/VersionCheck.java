package dev.zprestige.prestige.client.protection.check.impl;

import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.auth.KeepAlive;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;

import javax.swing.*;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class VersionCheck extends Check {

    public VersionCheck() {
        super(Category.Normal);
    }

    @Override
    public void run() {
        /*int n = KeepAlive.run();
        String string = URLDecoder.decode(VersionCheck.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8);
        File file = new File(string.replace("%20", " "));
        if (file.exists()) {
            long l = file.length();
            if (l != n) {
                String string2 = "You are on an old version of Prestige, please update to the latest.";
                JOptionPane.showMessageDialog(null, string2, "Error", 0);
                ProtectionManager.exit("You are on an old version of Prestige, please update to the latest. " + new String(Base64.getEncoder().encode((l + " : " + n).getBytes())));
            }
        } else {
            String string3 = "You are on an old version of Prestige, please update to the latest.";
            JOptionPane.showMessageDialog(null, string3, "Error", 0);
            ProtectionManager.exit("You are on an old version of Prestige, please update to the latest. -1");
        }*/
    }
}
