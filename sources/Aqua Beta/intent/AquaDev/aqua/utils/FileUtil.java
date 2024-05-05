package intent.AquaDev.aqua.utils;

import gui.clickgui.ownClickgui.ClickguiScreen;
import gui.clickgui.ownClickgui.components.CategoryPaneOwn;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.gui.novoline.ClickguiScreenNovoline;
import intent.AquaDev.aqua.gui.novoline.components.CategoryPaneNovoline;
import intent.AquaDev.aqua.modules.Module;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public class FileUtil {
    public static final File DIRECTORY = new File(Minecraft.getMinecraft().mcDataDir, Aqua.name);
    public static final File PIC = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/pic");

    public void createFolder() {
        if (!DIRECTORY.exists() || !DIRECTORY.isDirectory()) {
            DIRECTORY.mkdirs();
            DIRECTORY.mkdir();
        }
    }

    public void createPicFolder() {
        if (!PIC.exists() || !PIC.isDirectory()) {
            PIC.mkdir();
            PIC.mkdirs();
        }
    }

    public void loadKeys() {
        try {
            File file;
            Path path;
            Path path1 = Paths.get((String)(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name), (String[])new String[0]);
            if (!Files.exists((Path)path1, (LinkOption[])new LinkOption[0])) {
                try {
                    Files.createDirectory((Path)path1, (FileAttribute[])new FileAttribute[0]);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!Files.exists((Path)(path = Paths.get((String)(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/keys.txt"), (String[])new String[0])), (LinkOption[])new LinkOption[0])) {
                try {
                    Files.createFile((Path)path, (FileAttribute[])new FileAttribute[0]);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!(file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/keys.txt")).exists()) {
                file.createNewFile();
            } else if (file.exists()) {
                String readString;
                BufferedReader bufferedReader = new BufferedReader((Reader)new FileReader(file));
                while ((readString = bufferedReader.readLine()) != null) {
                    String[] split = readString.split(":");
                    Module mod = Aqua.moduleManager.getModuleByName(split[0]);
                    if (mod == null) continue;
                    int key = Integer.parseInt((String)split[1]);
                    mod.setKeyBind(key);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveKeys() {
        File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/keys.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter((Writer)new FileWriter(file));
            for (Module module : Aqua.moduleManager.modules) {
                String moduleName = module.getName();
                int moduleKey = module.getKeyBind();
                String endstring = moduleName + ":" + moduleKey + "\n";
                writer.write(endstring);
            }
            writer.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void saveModules() {
        File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/modules.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter((Writer)new FileWriter(file));
            for (Module module : Aqua.moduleManager.modules) {
                String modName = module.getName();
                String string = modName + ":" + module.toggeld + "\n";
                writer.write(string);
            }
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModules() {
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/modules.txt");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                String readString;
                BufferedReader bufferedReader = new BufferedReader((Reader)new FileReader(file));
                while ((readString = bufferedReader.readLine()) != null) {
                    String[] split = readString.split(":");
                    Module mod = Aqua.moduleManager.getModuleByName(split[0]);
                    boolean enabled = Boolean.parseBoolean((String)split[1]);
                    if (mod == null) continue;
                    mod.setState(enabled);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadClickGuiOwn(CategoryPaneOwn categoryPanel) {
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/gui.txt");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                String readString;
                BufferedReader bufferedReader = new BufferedReader((Reader)new FileReader(file));
                while ((readString = bufferedReader.readLine()) != null) {
                    String[] split = readString.split(":");
                    if (split.length != 3 || !categoryPanel.getCategory().toString().equals((Object)split[0])) continue;
                    categoryPanel.setX(Integer.parseInt((String)split[1]));
                    categoryPanel.setY(Integer.parseInt((String)split[2]));
                    return;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveClickGuiOwn(ClickguiScreen clickGui) {
        File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/gui.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter((Writer)new FileWriter(file));
            for (CategoryPaneOwn categoryPanel : clickGui.getCategoryPanes()) {
                String string = categoryPanel.getCategory() + ":" + categoryPanel.getX() + ":" + categoryPanel.getY() + "\n";
                writer.write(string);
            }
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveClickGui(ClickguiScreenNovoline clickGui) {
        File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/gui.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter((Writer)new FileWriter(file));
            for (CategoryPaneNovoline categoryPanel : clickGui.getCategoryPanes()) {
                String string = categoryPanel.getCategory() + ":" + categoryPanel.getX() + ":" + categoryPanel.getY() + "\n";
                writer.write(string);
            }
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadClickGui(CategoryPaneNovoline categoryPanel) {
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + Aqua.name + "/gui.txt");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                String readString;
                BufferedReader bufferedReader = new BufferedReader((Reader)new FileReader(file));
                while ((readString = bufferedReader.readLine()) != null) {
                    String[] split = readString.split(":");
                    if (split.length != 3 || !categoryPanel.getCategory().toString().equals((Object)split[0])) continue;
                    categoryPanel.setX(Integer.parseInt((String)split[1]));
                    categoryPanel.setY(Integer.parseInt((String)split[2]));
                    return;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFile(File file) {
        try {
            String curr;
            if (!file.exists()) {
                return null;
            }
            BufferedReader reader = new BufferedReader((Reader)new FileReader(file));
            ArrayList strings = new ArrayList();
            while ((curr = reader.readLine()) != null) {
                strings.add((Object)curr);
            }
            reader.close();
            return strings;
        }
        catch (Exception ignored) {
            return null;
        }
    }

    public static boolean writeFile(File file, List<String> lines) {
        try {
            if (!file.exists()) {
                return false;
            }
            BufferedWriter writer = new BufferedWriter((Writer)new FileWriter(file));
            for (String line : lines) {
                writer.write(line.endsWith("\n") ? line : line + "\n");
            }
            writer.close();
            writer.flush();
            return true;
        }
        catch (Exception ignored) {
            return false;
        }
    }
}
