// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.settings;

import com.elementars.eclient.util.NumberUtils;
import com.elementars.eclient.macro.Macro;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import com.elementars.eclient.module.render.Waypoints;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.guirewrite.Element;
import com.elementars.eclient.guirewrite.Frame;
import com.elementars.eclient.guirewrite.HUD;
import dev.xulu.newgui.Panel;
import dev.xulu.newgui.NewGUI;
import com.elementars.eclient.enemy.Enemy;
import com.elementars.eclient.enemy.Enemies;
import com.elementars.eclient.friend.Nicknames;
import com.elementars.eclient.friend.Friend;
import com.elementars.eclient.friend.Friends;
import com.elementars.eclient.module.render.Search;
import java.util.Objects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import com.elementars.eclient.module.render.Xray;
import com.elementars.eclient.guirewrite.elements.Welcome;
import com.elementars.eclient.guirewrite.elements.StickyNotes;
import com.elementars.eclient.util.FontHelper;
import com.elementars.eclient.font.CFontManager;
import com.elementars.eclient.dummy.DummyMod;
import com.elementars.eclient.module.Category;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.Xulu;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.nio.file.Paths;
import com.elementars.eclient.util.Wrapper;
import java.io.File;

public class FileManager
{
    public File Eclient;
    public File EclientSettings;
    public File EclientStorageESP;
    public File EclientCache;
    
    public FileManager() {
        this.Eclient = new File(Wrapper.getMinecraft().gameDir + File.separator + "Xulu");
        if (!this.Eclient.exists()) {
            this.Eclient.mkdirs();
        }
        this.EclientSettings = new File(Wrapper.getMinecraft().gameDir + File.separator + "Xulu" + File.separator + "Xulu Settings");
        if (!this.EclientSettings.exists()) {
            this.EclientSettings.mkdirs();
        }
        this.EclientStorageESP = new File(Wrapper.getMinecraft().gameDir + File.separator + "Xulu" + File.separator + "StorageESP Logs");
        if (!this.EclientStorageESP.exists()) {
            this.EclientStorageESP.mkdirs();
        }
    }
    
    public boolean appendTextFile(final String data, final String file) {
        try {
            final Path path = Paths.get(file, new String[0]);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path, new LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to write file: " + file);
            return false;
        }
        return true;
    }
    
    public void saveStorageESP(final String name, final String coords, final String chests) {
        try {
            final File file = new File(this.EclientStorageESP.getAbsolutePath(), name + ".txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(coords + " with " + chests + " chests");
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveBinds() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Binds.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : Xulu.MODULE_MANAGER.getModules()) {
                try {
                    out.write(module.getName() + ":" + module.getKey());
                    out.write("\r\n");
                }
                catch (Exception ex) {}
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadBinds() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Binds.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String bind = curLine.split(":")[1];
                    final int b = Integer.parseInt(bind);
                    final Module m = Xulu.MODULE_MANAGER.getModuleByName(name);
                    if (m == null) {
                        continue;
                    }
                    m.setKey(b);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveBinds();
        }
    }
    
    public void saveDummy() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Dummy.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : Xulu.MODULE_MANAGER.getModules()) {
                if (module.getCategory() == Category.DUMMY) {
                    try {
                        out.write(module.getName() + ":" + ((module.getHudInfo() == null) ? "null" : module.getHudInfo()));
                        out.write("\r\n");
                    }
                    catch (Exception ex) {}
                }
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadDummy() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Dummy.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String info = curLine.split(":")[1];
                    if (info.equalsIgnoreCase("null")) {
                        Xulu.MODULE_MANAGER.getModules().add(new DummyMod(name));
                    }
                    else {
                        Xulu.MODULE_MANAGER.getModules().add(new DummyMod(name, info));
                    }
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveDummy();
        }
    }
    
    public void saveFont() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Font.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write("Normal:" + (CFontManager.customFont.getFont().getFontName().equalsIgnoreCase("Comfortaa Regular") ? "Comfortaa" : CFontManager.customFont.getFont().getFontName()) + ":" + CFontManager.customFont.getFont().getSize() + ":" + CFontManager.customFont.getFont().getStyle() + ":" + CFontManager.customFont.isAntiAlias() + ":" + CFontManager.customFont.isFractionalMetrics() + "\r\n");
            out.write("Xdolf:" + (CFontManager.xFontRenderer.getFont().getFont().getFontName().equalsIgnoreCase("Comfortaa Regular") ? "Comfortaa" : CFontManager.xFontRenderer.getFont().getFont().getFontName()) + ":" + CFontManager.xFontRenderer.getFont().getFont().getSize() + ":" + CFontManager.xFontRenderer.getFont().getFont().getStyle() + ":" + CFontManager.xFontRenderer.isAntiAliasing() + ":NULL\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadFont() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Font.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String font = curLine.split(":")[0];
                    final String name = curLine.split(":")[1];
                    final String size = curLine.split(":")[2];
                    final String style = curLine.split(":")[3];
                    final String alias = curLine.split(":")[4];
                    final String metrics = curLine.split(":")[5];
                    final int si = Integer.parseInt(size);
                    final int st = Integer.parseInt(style);
                    final boolean al = Boolean.parseBoolean(alias);
                    final boolean me = !metrics.equalsIgnoreCase("null") && Boolean.parseBoolean(metrics);
                    if (font.equalsIgnoreCase("Normal")) {
                        FontHelper.setCFontRenderer(name, st, si, al, me);
                    }
                    else if (font.equalsIgnoreCase("Xdolf")) {
                        FontHelper.setXdolfFontRenderer(name, st, si, al);
                    }
                    else {
                        System.out.println("Invalid Font Type!");
                    }
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveFont();
        }
    }
    
    public void saveStickyNote() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Note.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(StickyNotes.saveText + "\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadStickyNote() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Note.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String note = curLine.split(":")[0];
                    StickyNotes.processText(note);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveStickyNote();
        }
    }
    
    public void saveWelcomeMessage() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Welcome.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Welcome.text + "\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadWelcomeMessage() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Welcome.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    Welcome.handleWelcome(curLine);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveWelcomeMessage();
        }
    }
    
    public void saveXray() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Xray.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Block block : Xray.getBLOCKS()) {
                try {
                    out.write(((ResourceLocation)Block.REGISTRY.getNameForObject((Object)block)).getPath());
                    out.write("\r\n");
                }
                catch (Exception ex) {}
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadXray() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Xray.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Xray.getBLOCKS().clear();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Xray.getBLOCKS().add(Objects.requireNonNull(Block.getBlockFromName(line)));
                }
                catch (NullPointerException ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveSearch() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Search.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Block block : Search.getBLOCKS()) {
                try {
                    out.write(((ResourceLocation)Block.REGISTRY.getNameForObject((Object)block)).getPath());
                    out.write("\r\n");
                }
                catch (Exception ex) {}
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadSearch() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Search.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Search.getBLOCKS().clear();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Search.getBLOCKS().add(Objects.requireNonNull(Block.getBlockFromName(line)));
                }
                catch (NullPointerException ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveHacks() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Modules.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : Xulu.MODULE_MANAGER.getModules()) {
                try {
                    if (!module.isToggled() || module.getName().matches("null") || module.getName().equals("Log Out Spot") || module.getName().equals("Freecam") || module.getName().equals("Blink") || module.getName().equals("Join/Leave msgs") || module.getName().equals("Elytra +") || module.getName().equals("Sound")) {
                        continue;
                    }
                    out.write(module.getName());
                    out.write("\r\n");
                }
                catch (Exception ex) {}
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void saveFriends() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Friends.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Friend f : Friends.getFriends()) {
                try {
                    out.write(f.getUsername());
                    out.write("\r\n");
                }
                catch (Exception ex) {}
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadFriends() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Friends.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Friends.addFriend(line);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveFriends();
        }
    }
    
    public void saveNicks() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Nicknames.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            final Writer writer;
            Nicknames.getAliases().forEach((name, nick) -> {
                try {
                    writer.write(name + ":" + nick);
                    writer.write("\r\n");
                }
                catch (Exception ex) {}
                return;
            });
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadNicks() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Nicknames.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String nick = curLine.split(":")[1];
                    Nicknames.addNickname(name, nick);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveFriends();
        }
    }
    
    public void saveEnemies() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Enemies.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Enemy e : Enemies.getEnemies()) {
                try {
                    out.write(e.getUsername());
                    out.write("\r\n");
                }
                catch (Exception ex) {}
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadEnemies() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Enemies.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Enemies.addEnemy(line);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveEnemies();
        }
    }
    
    public void saveNewGui() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "NewGui.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Panel p : NewGUI.panels) {
                try {
                    out.write(p.title + ":" + p.x + ":" + p.y + ":" + p.extended);
                    out.write("\r\n");
                }
                catch (Exception ex) {}
            }
            out.close();
        }
        catch (Exception ex2) {}
    }
    
    public void loadNewGui() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "NewGui.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String x = curLine.split(":")[1];
                    final String y = curLine.split(":")[2];
                    final String e = curLine.split(":")[3];
                    final double x2 = Double.parseDouble(x);
                    final double y2 = Double.parseDouble(y);
                    final boolean ext = Boolean.parseBoolean(e);
                    final Panel p = NewGUI.getPanelByName(name);
                    if (p == null) {
                        continue;
                    }
                    p.x = x2;
                    p.y = y2;
                    p.extended = ext;
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.saveNewGui();
        }
    }
    
    public void saveHUD() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "HUD.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Frame f : HUD.frames) {
                out.write(f.title + ":" + f.x + ":" + f.y);
                out.write("\r\n");
            }
            out.write(Xulu.hud.hudPanel.title + ":" + Xulu.hud.hudPanel.x + ":" + Xulu.hud.hudPanel.y);
            out.write("\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadHUD() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "HUD.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String x = curLine.split(":")[1];
                    final String y = curLine.split(":")[2];
                    final double x2 = Double.parseDouble(x);
                    final double y2 = Double.parseDouble(y);
                    if (name.equalsIgnoreCase(Xulu.hud.hudPanel.title)) {
                        Xulu.hud.hudPanel.x = x2;
                        Xulu.hud.hudPanel.y = y2;
                    }
                    final Frame p = HUD.getframeByName(name);
                    if (p != null) {
                        p.x = x2;
                        p.y = y2;
                    }
                    final Element e = (Element)Xulu.MODULE_MANAGER.getModuleByName(name);
                    if (e == null) {
                        continue;
                    }
                    e.setX(x2);
                    e.setY(y2);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
            this.saveHUD();
        }
    }
    
    public void savePrefix() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Prefix.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Command.getPrefix());
            out.write("\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadPrefix() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Prefix.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                Command.setPrefix(line);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.savePrefix();
        }
    }
    
    public void saveWaypoints() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Waypoints.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Waypoints.Waypoint waypoint : Waypoints.WAYPOINTS) {
                out.write(waypoint.getName() + ":" + waypoint.getPos().x + ":" + waypoint.getPos().y + ":" + waypoint.getPos().z + ":" + waypoint.getDimension());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadWaypoints() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Waypoints.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String x = curLine.split(":")[1];
                    final String y = curLine.split(":")[2];
                    final String z = curLine.split(":")[3];
                    final String dimension = curLine.split(":")[4];
                    final int posX = Integer.parseInt(x);
                    final int posY = Integer.parseInt(y);
                    final int posZ = Integer.parseInt(z);
                    final int dim = Integer.parseInt(dimension);
                    Waypoints.WAYPOINTS.add(new Waypoints.Waypoint(UUID.randomUUID(), name, new BlockPos(posX, posY, posZ), null, dim));
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveDrawn();
        }
    }
    
    public void saveDrawn() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Drawn.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : Xulu.MODULE_MANAGER.getModules()) {
                out.write(module.getName() + ":" + module.isDrawn());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadDrawn() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Drawn.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String isOn = curLine.split(":")[1];
                    final boolean drawn = Boolean.parseBoolean(isOn);
                    final Module m = Xulu.MODULE_MANAGER.getModuleByName(name);
                    m.setDrawn(drawn);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveDrawn();
        }
    }
    
    public void writeCrash(final String alah) {
        try {
            final SimpleDateFormat format = new SimpleDateFormat("MM_dd_yyyy-HH_mm_ss");
            final Date date = new Date();
            final File file = new File(this.Eclient.getAbsolutePath(), "crashlog-".concat(format.format(date)).concat(".xen"));
            final BufferedWriter outWrite = new BufferedWriter(new FileWriter(file));
            outWrite.write(alah);
            outWrite.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadHacks() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Modules.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                    try {
                        if (m.getName().equals(line)) {
                            m.initToggle(true);
                        }
                        if (!(m instanceof Element)) {
                            continue;
                        }
                        ((Element)m).getFrame().pinned = true;
                    }
                    catch (Exception ex) {}
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveHacks();
        }
    }
    
    public void saveMacros() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Macros.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Macro m : Xulu.MACRO_MANAGER.getMacros()) {
                out.write(m.getMacro() + ":" + m.getKey() + "\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadMacros() {
        try {
            final File file = new File(this.Eclient.getAbsolutePath(), "Macros.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String isOn = curLine.split(":")[1];
                    final int key = Integer.valueOf(isOn);
                    if (Xulu.MACRO_MANAGER.getMacros().contains(new Macro(name, key))) {
                        continue;
                    }
                    Xulu.MACRO_MANAGER.addMacro(name, key);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveMacros();
        }
    }
    
    public String determineNumber(final Object o) {
        if (o instanceof Integer) {
            return "INTEGER";
        }
        if (o instanceof Float) {
            return "FLOAT";
        }
        if (o instanceof Double) {
            return "DOUBLE";
        }
        return "INVALID";
    }
    
    public void saveSettingsList() {
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "Slider.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Value<?> i : Xulu.VALUE_MANAGER.getValues()) {
                if (i.isNumber()) {
                    out.write(i.getName() + ":" + i.getValue().toString() + ":" + i.getParentMod().getName() + "\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex) {}
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "Check.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Value j : Xulu.VALUE_MANAGER.getValues()) {
                if (j.isToggle()) {
                    out.write(j.getName() + ":" + j.getValue().toString() + ":" + j.getParentMod().getName() + "\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex2) {}
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "Combo.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Value j : Xulu.VALUE_MANAGER.getValues()) {
                if (j.isMode()) {
                    if (j.getValue().contains(":")) {
                        out.write(j.getName() + ";" + j.getValue().toString() + ";" + j.getParentMod().getName() + "\r\n");
                    }
                    out.write(j.getName() + ":" + j.getValue().toString() + ":" + j.getParentMod().getName() + "\r\n");
                }
                if (j.isEnum()) {
                    out.write(j.getName() + ":" + j.getValue().toString() + ":" + j.getParentMod().getName() + "\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex3) {}
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "TextBox.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Value j : Xulu.VALUE_MANAGER.getValues()) {
                if (j.isText()) {
                    if (j.getValue().getText().contains(":")) {
                        out.write(j.getName() + ";" + j.getValue().getText() + ";" + j.getParentMod().getName() + "\r\n");
                    }
                    out.write(j.getName() + ":" + j.getValue().getText() + ":" + j.getParentMod().getName() + "\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex4) {}
    }
    
    public void loadSettingsList() {
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "Slider.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String isOn = curLine.split(":")[1];
                    final String m = curLine.split(":")[2];
                    final Value mod = Xulu.VALUE_MANAGER.getValueByMod(Xulu.MODULE_MANAGER.getModuleByName(m), name);
                    Number type = 0;
                    if (mod.getValue() instanceof Double) {
                        type = NumberUtils.createDouble(isOn);
                    }
                    else if (mod.getValue() instanceof Integer) {
                        type = NumberUtils.createInteger(isOn);
                    }
                    else if (mod.getValue() instanceof Float) {
                        type = NumberUtils.createFloat(isOn);
                    }
                    else if (mod.getValue() instanceof Long) {
                        type = NumberUtils.createLong(isOn);
                    }
                    else if (mod.getValue() instanceof Short) {
                        type = NumberUtils.createShort(isOn);
                    }
                    mod.setValue(type);
                }
                catch (Exception ex) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "Check.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    final String name = curLine.split(":")[0];
                    final String isOn = curLine.split(":")[1];
                    final String m = curLine.split(":")[2];
                    final Value mod = Xulu.VALUE_MANAGER.getValueByMod(Xulu.MODULE_MANAGER.getModuleByName(m), name);
                    mod.setValue(Boolean.parseBoolean(isOn));
                }
                catch (Exception ex2) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "Combo.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    String name;
                    String isOn;
                    String m;
                    if (curLine.contains(";")) {
                        name = curLine.split(";")[0];
                        isOn = curLine.split(";")[1];
                        m = curLine.split(";")[2];
                    }
                    else {
                        name = curLine.split(":")[0];
                        isOn = curLine.split(":")[1];
                        m = curLine.split(":")[2];
                    }
                    final Value mod = Xulu.VALUE_MANAGER.getValueByMod(Xulu.MODULE_MANAGER.getModuleByName(m), name);
                    if (mod.isEnum()) {
                        mod.setEnumValue(isOn);
                    }
                    else {
                        mod.setValue(isOn);
                    }
                }
                catch (Exception ex3) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final File file = new File(this.EclientSettings.getAbsolutePath(), "TextBox.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String curLine = line.trim();
                    String name;
                    String isOn;
                    String m;
                    if (curLine.contains(";")) {
                        name = curLine.split(";")[0];
                        isOn = curLine.split(";")[1];
                        m = curLine.split(";")[2];
                    }
                    else {
                        name = curLine.split(":")[0];
                        isOn = curLine.split(":")[1];
                        m = curLine.split(":")[2];
                    }
                    final Value mod = Xulu.VALUE_MANAGER.getValueByMod(Xulu.MODULE_MANAGER.getModuleByName(m), name);
                    mod.setValue(new TextBox(isOn));
                }
                catch (Exception ex4) {}
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
