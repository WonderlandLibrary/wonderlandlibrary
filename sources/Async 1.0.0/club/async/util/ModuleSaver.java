package club.async.util;

import club.async.Async;
import club.async.interfaces.MinecraftInterface;
import club.async.module.Module;
import club.async.module.setting.Setting;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;

import java.io.*;

public final class ModuleSaver implements MinecraftInterface {

    public static void save() {
        File file = new File(mc.mcDataDir, "Async/Modules.async");
        try {
            if (file.exists())
                file.delete();
            if (new File(mc.mcDataDir, "Async/").mkdir() || !file.exists())
                file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (Module module : Async.INSTANCE.getModuleManager().getModules()) {
                bufferedWriter.write(module.getName() + ":" + module.isEnabled() + ":" + module.getKey() + "\n");
                bufferedWriter.flush();
            }
            bufferedWriter.close();

            file = new File(mc.mcDataDir, "Async/Settings.async");
            if (file.exists())
                file.delete();
            if (new File(mc.mcDataDir, "Async/").mkdir() || !file.exists())
                file.createNewFile();

            bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (Module module : Async.INSTANCE.getModuleManager().getModules()) {
                for (Setting setting : module.getSettings()) {
                    if(setting instanceof ModeSetting) {
                        bufferedWriter.write("MODE:" + module.getName() + ":" + setting.getName() + ":" + ((ModeSetting) setting).getCurrMode() + "\n");
                        bufferedWriter.flush();
                    } else if(setting instanceof BooleanSetting) {
                        bufferedWriter.write("BOOLEAN:" + module.getName() + ":" + setting.getName() + ":" + ((BooleanSetting) setting).get() + "\n");
                        bufferedWriter.flush();
                    } else if(setting instanceof NumberSetting) {
                        bufferedWriter.write("NUMBER:" + module.getName() + ":" + setting.getName() + ":" + ((NumberSetting) setting).getDouble() + "\n");
                        bufferedWriter.flush();
                    }
                }
            }
            bufferedWriter.close();
        } catch (IOException ignored) {}
    }

    public static void load() {
        File file = new File(mc.mcDataDir, "Async/Modules.async");
        try {
            if (new File(mc.mcDataDir, "Async/").mkdir() || !file.exists()) {
                file.createNewFile();
            }
            System.out.println("PENIS: " + file.getAbsolutePath());
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                bufferedReader.lines().forEach(s -> {
                    try {
                        Async.INSTANCE.getModuleManager().getModule(s.split(":")[0]).setToggled(Boolean.parseBoolean(s.split(":")[1]));
                        Async.INSTANCE.getModuleManager().getModule(s.split(":")[0]).setKey(Integer.parseInt(s.split(":")[2]));
                    } catch (Exception ignored) {}
                });
            }

            file = new File(mc.mcDataDir, "Async/Settings.async");
            if (new File(mc.mcDataDir, "Async/").mkdir() || !file.exists()) {
                file.createNewFile();
                return;
            }
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                bufferedReader.lines().forEach(s -> {
                    try {
                        switch (s.split(":")[0]) {
                            case "MODE":
                                ((ModeSetting) (Async.INSTANCE.getModuleManager().getModule(s.split(":")[1]).getSetting(s.split(":")[2]))).setCurrMode(s.split(":")[3]);
                                break;
                            case "BOOLEAN":
                                ((BooleanSetting) (Async.INSTANCE.getModuleManager().getModule(s.split(":")[1]).getSetting(s.split(":")[2]))).setToggled(Boolean.parseBoolean(s.split(":")[3]));
                                break;
                            case "NUMBER":
                                ((NumberSetting) (Async.INSTANCE.getModuleManager().getModule(s.split(":")[1]).getSetting(s.split(":")[2]))).setCurrent(Double.parseDouble(s.split(":")[3]));
                                break;
                        }
                    } catch (Exception ignored) {}
                });
            }
        } catch (IOException ignored) {}
    }

}
