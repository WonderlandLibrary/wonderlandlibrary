// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.ui.augustusmanager;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.File;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import org.json.JSONArray;
import java.awt.Color;
import org.json.JSONObject;
import java.io.Reader;
import org.json.JSONTokener;
import java.io.FileReader;
import java.net.URLConnection;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.io.BufferedInputStream;
import java.net.URL;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import java.util.ArrayList;
import java.util.HashMap;
import net.augustus.ui.widgets.ConfigButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class OnlineConfigs extends GuiScreen
{
    private GuiTextField createTextField;
    private GuiScreen parent;
    private ConfigButton selected;
    private HashMap<Integer, String> loadedConfigs;
    private HashMap<Integer, String> urls;
    private ArrayList<ConfigButton> configButtons;
    
    public GuiScreen start(final GuiScreen parent) {
        this.parent = parent;
        return this;
    }
    
    public OnlineConfigs(final GuiScreen parent) {
        this.loadedConfigs = new HashMap<Integer, String>();
        this.urls = new HashMap<Integer, String>();
        this.configButtons = new ArrayList<ConfigButton>();
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        (this.createTextField = new GuiTextField(1, this.fontRendererObj, sr.getScaledWidth() / 2 - 75, sr.getScaledHeight() - 55, 150, 20)).setMaxStringLength(1377);
        this.buttonList.add(new CustomButton(69420, sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() - 30, 100, 20, "Refresh", Augustus.getInstance().getClientColor()));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 69420) {
            this.reDownload();
        }
        super.actionPerformed(button);
    }
    
    public static void downloadJSON() throws IOException {
        final URL jsonUrl = new URL("https://raw.githubusercontent.com/JonnyOnlineYT/xenzaconfigs/main/onlineconfigs.json");
        final URLConnection connection = jsonUrl.openConnection();
        final BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        final FileOutputStream fileOutputStream = new FileOutputStream(Paths.get("xenzarecode/onlineconfigs.json", new String[0]).toFile());
        final byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        fileOutputStream.close();
        in.close();
    }
    
    private void reDownload() throws IOException {
        downloadJSON();
        this.refresh();
    }
    
    private void refresh() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        int yAdd = 0;
        int highest = 0;
        try {
            final FileReader fileReader = new FileReader("xenzarecode/onlineconfigs.json");
            final JSONTokener tokener = new JSONTokener(fileReader);
            final JSONObject jsonConfig = new JSONObject(tokener);
            final JSONArray namesArray = jsonConfig.getJSONArray("configs");
            for (int i = 0; i < namesArray.length(); ++i) {
                final JSONObject nameObj = namesArray.getJSONObject(i);
                final String name = nameObj.getString("name");
                final JSONObject linkObj = nameObj.getJSONObject("link");
                final String link = linkObj.getString("value");
                final JSONObject idObj = nameObj.getJSONObject("id");
                final int id = idObj.getInt("value");
                this.loadedConfigs.put(id, name);
                this.urls.put(id, link);
                highest = id;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        for (int j = 1; j < highest + 1; ++j) {
            this.configButtons.add(new ConfigButton(j, sr.getScaledWidth() / 2 - 140, 55 + yAdd, 280, 35, this.loadedConfigs.get(j), "", "", Color.gray));
            yAdd += 36;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, 0, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight(), new Color(0, 0, 0, 190).getRGB());
        boolean b = true;
        final float mdw = Mouse.getDWheel() / 10.0f;
        float y = 0.0f;
        if (this.configButtons.size() > 0) {
            if (this.configButtons.get(this.configButtons.size() - 1).yPosition + this.configButtons.get(this.configButtons.size() - 1).getHeight() + mdw < sr.getScaledHeight() - 140 && mdw < 0.0f) {
                b = false;
                y = (float)(55 + (sr.getScaledHeight() - 140 - 55 - this.configButtons.size() * 36) + 2);
            }
            else if (this.configButtons.get(0).yPosition + mdw > 55.0f && mdw > 0.0f) {
                b = false;
                y = 55.0f;
            }
            float yAdd = 0.0f;
            for (final ConfigButton configButton : this.configButtons) {
                if (this.configButtons.size() > 1 && this.configButtons.size() * 36 > sr.getScaledHeight() - 140 - 55) {
                    if (b) {
                        configButton.yPosition += (int)mdw;
                    }
                    else {
                        configButton.yPosition = (int)(y + yAdd);
                    }
                }
                configButton.draw(this.mc, mouseX, mouseY);
                yAdd += 36.0f;
            }
        }
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, 0, sr.getScaledWidth() / 2 + 150, 55, new Color(15, 15, 15, 255).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, sr.getScaledHeight() - 65, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight(), new Color(15, 15, 15, 255).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, 55, sr.getScaledWidth() / 2 - 140, sr.getScaledHeight() - 140, new Color(15, 15, 15, 255).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 + 140, 55, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight() - 140, new Color(15, 15, 15, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.createTextField.drawTextBox();
        GlStateManager.pushMatrix();
        GlStateManager.scale(3.0f, 3.0f, 1.0f);
        this.fontRendererObj.drawStringWithShadow("Online Configs", sr.getScaledWidth() / 2.0f / 3.0f - this.fontRendererObj.getStringWidth("Online Configs") / 2.0f, 5.0f, Color.gray.getRGB());
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.createTextField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.createTextField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1 || mouseButton == 0) {
            for (final ConfigButton configButton : this.configButtons) {
                if (this.mouseOver(mouseX, mouseY, configButton.xPosition, configButton.yPosition, configButton.getButtonWidth(), configButton.getHeight())) {
                    this.selected = configButton;
                    this.handleClick(this.selected.id);
                    configButton.setSelected(false);
                }
                else {
                    configButton.setSelected(false);
                }
            }
        }
    }
    
    private void handleClick(final int id) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/augustus/ui/augustusmanager/OnlineConfigs.selected:Lnet/augustus/ui/widgets/ConfigButton;
        //     4: ldc             "Downloading..."
        //     6: putfield        net/augustus/ui/widgets/ConfigButton.displayString:Ljava/lang/String;
        //     9: new             Ljava/lang/Thread;
        //    12: dup            
        //    13: aload_0         /* this */
        //    14: iload_1         /* id */
        //    15: invokedynamic   BootstrapMethod #0, run:(Lnet/augustus/ui/augustusmanager/OnlineConfigs;I)Ljava/lang/Runnable;
        //    20: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //    23: invokevirtual   java/lang/Thread.start:()V
        //    26: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:382)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:95)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void updateScreen() {
        this.createTextField.updateCursorCounter();
        super.updateScreen();
    }
    
    private void initConfigs() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.configButtons = new ArrayList<ConfigButton>();
        if (!Files.exists(Paths.get("xenzarecode/configs", new String[0]), new LinkOption[0])) {
            try {
                Files.createDirectories(Paths.get("xenzarecode/configs", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (final IOException var11) {
                var11.printStackTrace();
            }
        }
        final File f = new File("xenzarecode/configs");
        final File[] fileArray = f.listFiles();
        int yAdd = 0;
        assert fileArray != null;
        for (final File file : fileArray) {
            if (file.getName().contains(".json")) {
                String name = "";
                for (int i = 0; i < file.getName().length() - 5; ++i) {
                    name += file.getName().charAt(i);
                }
                final String[] s = Augustus.getInstance().getConverter().configReader(name);
                this.configButtons.add(new ConfigButton(6, sr.getScaledWidth() / 2 - 140, 55 + yAdd, 280, 35, name, s[1], s[2], Color.gray));
                yAdd += 36;
            }
        }
    }
    
    public boolean mouseOver(final double mouseX, final double mouseY, final double posX, final double posY, final double width, final double height) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }
}
