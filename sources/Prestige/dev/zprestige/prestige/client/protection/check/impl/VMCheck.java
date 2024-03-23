package dev.zprestige.prestige.client.protection.check.impl;

import com.google.common.collect.Lists;
import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VMCheck extends Check {
    public static List<String> files = new ArrayList<>();
    public static List<String> vms = new ArrayList<>();

    public VMCheck() {
        super(Category.Normal);
        files.add("C:\\WINDOWS\\system32\\drivers\\vmmouse.sys");
        files.add("/usr/share/virtualbox");
        vms.add("VMware");
        vms.add("VirtualBox");
        vms.add("virtual");
        vms.add("android");
        vms.add("VBox");
        vms.add("QEMU");
        vms.add("Parallels");
    }

    @Override
    public void run() {
        if (isVM()) {
            ProtectionManager.exit("J");
        }
    }

    public boolean isVM() {
        String string = System.getProperty("java.vendor");
        String string2 = System.getProperty("java.vm.name");
        String string3 = System.getProperty("java.vm.version");
        String string4 = System.getProperty("java.class.path");
        if (string != null && has(string)) {
            return true;
        }
        if (string2 != null && has(string2)) {
            return true;
        }
        if (string3 != null && has(string3)) {
            return true;
        }
        if (string4 != null && has(string4)) {
            return true;
        }
        for (String string5 : files) {
            if (new File(string5).exists()) {
                return true;
            }
        }
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                Process process = Runtime.getRuntime().exec("reg query HKLM\\HARDWARE\\ACPI\\DSDT\\VBOX__");
                process.waitFor();
                if (process.exitValue() == 0) {
                    return true;
                }
            }
            catch (IOException | InterruptedException exception) {
                // empty catch block
            }
        }
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory() <= 0x20000000L;
    }

    boolean has(String string) {
        for (String string2 : vms) {
            if (string.toLowerCase().contains(string2.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}