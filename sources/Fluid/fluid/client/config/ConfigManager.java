// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.config;

import java.util.List;
import fluid.client.mods.GuiMod;
import java.util.Iterator;
import fluid.client.exceptions.ConfigLoadException;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import fluid.client.Client;
import java.io.File;
import net.minecraft.client.Minecraft;

public class ConfigManager
{
    public Minecraft mc;
    public File configDir;
    public String configName;
    
    public ConfigManager() {
        this.mc = Minecraft.getMinecraft();
        this.configDir = new File(this.mc.mcDataDir + "/Fluid Client");
        this.configName = "config.fluid";
        try {
            this.load();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void save() throws Exception {
        if (!this.configDir.exists()) {
            this.configDir.mkdir();
        }
        System.out.println(Client.INSTANCE.modManager.guiModList.size());
        Client.INSTANCE.modManager.guiModList.forEach(mod -> {
            new File(String.valueOf(this.configDir.getAbsolutePath()) + "/" + mod.name);
            final File file;
            final File moddir = file;
            if (!moddir.exists()) {
                moddir.mkdir();
            }
            moddir.setWritable(true, false);
            new File(String.valueOf(moddir.getAbsolutePath()) + "/" + this.configName);
            final File file2;
            final File baseconfig = file2;
            if (!baseconfig.exists()) {
                try {
                    baseconfig.createNewFile();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            baseconfig.setWritable(true, false);
            try {
                new BufferedWriter(new FileWriter(baseconfig));
                final BufferedWriter bufferedWriter2;
                final BufferedWriter bufferedWriter = bufferedWriter2;
                bufferedWriter.write("x" + mod.getX());
                mod.x = mod.getX();
                bufferedWriter.newLine();
                bufferedWriter.write("y" + mod.getY());
                mod.y = mod.getY();
                bufferedWriter.newLine();
                bufferedWriter.write("enabled" + mod.enabled);
                bufferedWriter.close();
            }
            catch (final Exception e2) {
                e2.printStackTrace();
            }
            return;
        });
        this.load();
    }
    
    public void load() throws Exception {
        if (!this.configDir.exists()) {
            this.save();
            return;
        }
        System.out.println(Client.INSTANCE.modManager.guiModList.size());
        Client.INSTANCE.modManager.guiModList.forEach(mod -> {
            try {
                new File(String.valueOf(this.configDir.getAbsolutePath()) + "/" + mod.name);
                final File file;
                final File moddir = file;
                new File(String.valueOf(moddir.getAbsolutePath()) + "/" + this.configName);
                final File file2;
                final File baseconfig = file2;
                final FileReader fre = new FileReader(baseconfig);
                final BufferedReader br = new BufferedReader(fre);
                final String line = "";
                final ArrayList linelist = new ArrayList<String>();
                Label_0124_1: {
                    break Label_0124_1;
                    String s;
                    do {
                        final String line2;
                        linelist.add(line2);
                        s = (line2 = br.readLine());
                    } while (s != null);
                }
                fre.close();
                br.close();
                if (linelist.size() > 3) {
                    baseconfig.deleteOnExit();
                    moddir.deleteOnExit();
                    this.configDir.deleteOnExit();
                    throw new ConfigLoadException("Config has too many lines. Please restart game.");
                }
                else {
                    linelist.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final String str = iterator.next();
                        if (str.startsWith("x")) {
                            final String st = str.replaceAll("x", "");
                            mod.setY(Integer.parseInt(st));
                        }
                        if (str.startsWith("y")) {
                            final String st2 = str.replaceAll("y", "");
                            mod.setY(Integer.parseInt(st2));
                        }
                        if (str.startsWith("enabled")) {
                            final String st3 = str.replaceAll("enabled", "");
                            mod.enabled = Boolean.parseBoolean(st3);
                            if (mod.enabled) {
                                mod.onEnable();
                            }
                            else {
                                mod.onDisable();
                            }
                        }
                    }
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }
}
