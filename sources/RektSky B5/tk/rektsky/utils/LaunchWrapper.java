/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.util.Arrays;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.rektsky.utils.LaunchWrapperMain;

public class LaunchWrapper {
    public static boolean paused1 = true;
    public static boolean paused2 = true;
    public static boolean wait = false;
    public static String woops = "";
    public static final Logger logger = LogManager.getLogger();
    public static boolean s = false;

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static void _start(String[] args) {
        logger.info("I1");
        try {
            LaunchWrapperMain.st(args, 288041);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Initialization error", 0);
            System.exit(-42);
        }
    }

    private static int randGen(int max, int min) {
        return (int)Math.floor(Math.random() * (double)max + (double)min);
    }

    public static void _check2() {
    }
}

