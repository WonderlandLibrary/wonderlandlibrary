/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.files.files.modulefiles;

import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

public class Binds
extends FileManager.CustomFile {
    public Binds(String name, boolean Module2, boolean loadOnStart) {
        super(name, Module2, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        String line;
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        while ((line = variable9.readLine()) != null) {
            int i2 = line.indexOf(":");
            if (i2 < 0) continue;
            String module = line.substring(0, i2).trim();
            String key = line.substring(i2 + 1).trim();
            Cheese.getInstance();
            Module m2 = Cheese.moduleManager.getModbyName(module);
            m2.setKeybind(Keyboard.getKeyIndex(key.toUpperCase()));
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File!");
    }

    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        for (Module m2 : ModuleManager.activeModules) {
            variable9.println(String.valueOf(String.valueOf(m2.getName())) + ":" + Keyboard.getKeyName(m2.getKeybind()));
        }
        variable9.close();
    }
}

