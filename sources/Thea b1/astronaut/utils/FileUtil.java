package astronaut.utils;

import astronaut.Duckware;
import astronaut.modules.Module;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static final File DIRECTORY = new File(Minecraft.getMinecraft().mcDataDir, Duckware.name);


    public void loadKeys() {
        try {
            final Path path1 = Paths.get(Minecraft.getMinecraft().mcDataDir + "/" + Duckware.name);
            if (!Files.exists(path1)) try {
                Files.createDirectory(path1);
            } catch (final IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
            final Path path = Paths.get(Minecraft.getMinecraft().mcDataDir + "/" + Duckware.name + "/keys.txt");
            //if directory exists?
            if (!Files.exists(path)) try {
                Files.createFile(path);
            } catch (final IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
            final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Duckware.name + "/keys.txt");
            if (!file.exists()) file.createNewFile();
            else if (file.exists()) {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readString;
                while ((readString = bufferedReader.readLine()) != null) {
                    final String[] split = readString.split(":");
                    final Module mod = Duckware.moduleManager.getModuleByName(split[0]);
                    if (mod != null) {
                        final int key = Integer.parseInt(split[1]);
                        mod.setKeyBind(key);
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void saveKeys() {
        final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Duckware.name + "/keys.txt");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (final Module module : Duckware.moduleManager.modules) {
                final String moduleName = module.getName();
                final int moduleKey = module.getKeyBind(); // TODO: Keybinding system
                final String endstring = moduleName + ":" + moduleKey + "\n";
                writer.write(endstring);
            }
            writer.close();
        } catch (final Exception ignored) {
        }
    }


    public void saveModules() {
        final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Duckware.name + "/modules.txt");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (final Module module : Duckware.moduleManager.modules) {
                final String modName = module.getName();
                final String string = modName + ":" + module.isToggled() + "\n";
                writer.write(string);
            }
            writer.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModules() {
        try {
            final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Duckware.name + "/modules.txt");
            if (!file.exists()) file.createNewFile();
            else {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readString;
                while ((readString = bufferedReader.readLine()) != null) {
                    final String[] split = readString.split(":");
                    final Module mod = Duckware.moduleManager.getModuleByName(split[0]);
                    final boolean enabled = Boolean.parseBoolean(split[1]);
                    if (mod != null) mod.setToggled(enabled);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    public void createFolder() {

        if (!DIRECTORY.exists() || !DIRECTORY.isDirectory()) {
            DIRECTORY.mkdir();
            DIRECTORY.mkdirs();
        }
    }
}
