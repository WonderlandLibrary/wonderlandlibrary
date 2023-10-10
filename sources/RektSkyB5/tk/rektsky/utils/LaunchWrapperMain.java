/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import javax.swing.JOptionPane;
import net.minecraft.client.main.Other;
import tk.rektsky.utils.LaunchWrapper;

public class LaunchWrapperMain {
    public static void st(String[] args, int l2) {
        LaunchWrapper.logger.info("I2");
        if (l2 < 283576 || l2 % 4572 != 5) {
            try {
                throw new BootstrapMethodError();
            }
            catch (BootstrapMethodError ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Initialization error", 0);
                System.exit(-42);
            }
        }
        LaunchWrapper.logger.info("Successfully Started!");
        Other.main(LaunchWrapper.concat(new String[]{"--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}", "--username", "$PLACEHOLDER NAME$"}, args));
    }

    public static void main(String[] args) {
        Other.main(LaunchWrapper.concat(new String[]{"--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}", "--username", "$PLACEHOLDER NAME$"}, args));
    }
}

