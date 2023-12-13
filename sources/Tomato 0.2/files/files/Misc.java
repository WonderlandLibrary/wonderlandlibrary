/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.files.files;

import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.files.FileManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class Misc
extends FileManager.CustomFile {
    public Misc(String name, boolean Module2, boolean loadOnStart) {
        super(name, Module2, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        String line;
        BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        while ((line = variable9.readLine()) != null) {
            int i2 = line.indexOf(":");
            if (i2 < 0) continue;
            String string = Cheese.prefix = line.substring(i2 + 1).trim();
        }
        System.out.println("Loaded " + this.getName() + " File!");
        variable9.close();
    }

    @Override
    public void saveFile() throws IOException {
        PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        variable9.println("Prefix:" + Cheese.prefix);
        variable9.close();
    }
}

