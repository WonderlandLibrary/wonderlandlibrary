// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.macro;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.io.File;
import java.util.List;

public class MacroManager
{
    public List<Macro> macros;
    private static final File macroFile;
    
    public MacroManager() {
        this.macros = new ArrayList<Macro>();
    }
    
    public List<Macro> getMacros() {
        return this.macros;
    }
    
    public void init() throws Exception {
        if (!MacroManager.macroFile.exists()) {
            MacroManager.macroFile.createNewFile();
        }
        else {
            this.readMacro();
        }
    }
    
    public void addMacros(final Macro macro) {
        this.macros.add(macro);
        this.updateFile();
    }
    
    public void deleteMacro(final int key) {
        this.macros.removeIf(macro -> macro.getKey() == key);
        this.updateFile();
    }
    
    public void onKeyPressed(final int key) {
        this.macros.stream().filter(macro -> macro.getKey() == key).forEach(macro -> {
            Minecraft.getMinecraft();
            Minecraft.player.sendChatMessage(macro.getMessage());
        });
    }
    
    public void updateFile() {
        try {
            final StringBuilder builder = new StringBuilder();
            this.macros.forEach(macro -> builder.append(macro.getMessage()).append(":").append(String.valueOf(macro.getKey()).toUpperCase()).append("\n"));
            Files.write(MacroManager.macroFile.toPath(), builder.toString().getBytes(), new OpenOption[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void readMacro() {
        try {
            final FileInputStream fileInputStream = new FileInputStream(MacroManager.macroFile.getAbsolutePath());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fileInputStream)));
            String line;
            while ((line = reader.readLine()) != null) {
                final String curLine = line.trim();
                final String command = curLine.split(":")[0];
                final String key = curLine.split(":")[1];
                this.macros.add(new Macro(command, Integer.parseInt(key)));
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        macroFile = new File("C:\\Minced\\game\\minced\\", "macro.pon");
    }
}
