/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import me.AveReborn.Client;
import me.AveReborn.mod.Category;
import me.AveReborn.ui.click.ClickMenuCategory;
import me.AveReborn.ui.click.ClickMenuMods;
import me.AveReborn.util.FileUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ClickMenu {
    private ArrayList<ClickMenuCategory> categories;
    public static int WIDTH = 100;
    public static int TAB_HEIGHT = 20;
    private MouseInputHandler handler = new MouseInputHandler(0);
    private Minecraft mc = Minecraft.getMinecraft();
    private String fileDir;

    public ClickMenu() {
        this.fileDir = String.valueOf(String.valueOf(this.mc.mcDataDir.getAbsolutePath())) + "/" + Client.CLIENT_NAME;
        this.categories = new ArrayList();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        UnicodeFontRenderer font = Client.instance.fontMgr.sansation18;
        this.addCategorys();
        try {
            this.loadClickGui();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void draw(int mouseX, int mouseY) {
        for (ClickMenuCategory c2 : this.categories) {
            c2.draw(mouseX, mouseY);
        }
    }

    private void addCategorys() {
        int xAxis = 10;
        Category[] arrcategory = Category.values();
        int n2 = arrcategory.length;
        int n22 = 0;
        while (n22 < n2) {
            Category c2 = arrcategory[n22];
            this.categories.add(new ClickMenuCategory(c2, xAxis, 90, WIDTH, TAB_HEIGHT, this.handler));
            xAxis += 115;
            ++n22;
        }
    }

    public void mouseClick(int mouseX, int mouseY) {
        for (ClickMenuCategory cat : this.categories) {
            cat.mouseClick(mouseX, mouseY);
        }
    }

    public void mouseRelease(int mouseX, int mouseY) {
        for (ClickMenuCategory cat : this.categories) {
            cat.mouseRelease(mouseX, mouseY);
        }
        Client.instance.fileMgr.saveValues();
        this.saveClickGui();
    }

    public ArrayList<ClickMenuCategory> getCategories() {
        return this.categories;
    }

    public void saveClickGui() {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/gui.txt");
        try {
            if (!f2.exists()) {
                f2.createNewFile();
            }
            PrintWriter pw2 = new PrintWriter(f2);
            for (ClickMenuCategory cat : this.getCategories()) {
                String name = cat.c.name();
                String x2 = String.valueOf(cat.x);
                String y2 = String.valueOf(cat.y);
                String open = String.valueOf(cat.uiMenuMods.open);
                pw2.print(String.valueOf(String.valueOf(name)) + ":" + x2 + ":" + y2 + ":" + open + "\n");
            }
            pw2.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void loadClickGui() throws IOException {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/gui.txt");
        if (!f2.exists()) {
            f2.createNewFile();
        } else {
            String line;
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            while ((line = br2.readLine()) != null) {
                try {
                    String[] s2 = line.split(":");
                    if (s2.length < 4) continue;
                    String name = s2[0];
                    int x2 = Integer.valueOf(s2[1]);
                    int y2 = Integer.valueOf(s2[2]);
                    boolean open = Boolean.valueOf(s2[3]);
                    for (ClickMenuCategory cat : this.getCategories()) {
                        String name2 = cat.c.name();
                        if (!name2.equals(name)) continue;
                        cat.x = x2;
                        cat.y = y2;
                        cat.uiMenuMods.open = open;
                    }
                }
                catch (Exception s2) {
                    // empty catch block
                }
            }
            br2.close();
        }
    }
}

