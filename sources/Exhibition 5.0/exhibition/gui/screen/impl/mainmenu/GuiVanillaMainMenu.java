// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.screen.impl.mainmenu;

import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import java.net.URI;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiButtonLanguage;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GLContext;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiVanillaMainMenu extends ClientMainMenu
{
    private static final AtomicInteger field_175373_f;
    private static final Logger logger;
    private static final Random field_175374_h;
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private boolean field_175375_v;
    private final Object someObject;
    private String strUpdateYourFuckingOpenGL;
    private String strUpdateYourFuckingOpenGL2;
    private String strMojangHelpDesk;
    private static final ResourceLocation splashTexts;
    public static final String field_96138_a;
    private int glOldWidth2;
    private int glOldWidth;
    private int glOldPlacementX;
    private int glOldPlacementY;
    private int glOldPlacementXOffset;
    private int glOldPlacementYOffset;
    private GuiButton btnOnline;
    private static final String __OBFID = "CL_00001154";
    
    public GuiVanillaMainMenu() {
        this.field_175375_v = true;
        this.someObject = new Object();
        this.strUpdateYourFuckingOpenGL2 = GuiVanillaMainMenu.field_96138_a;
        this.splashText = "missingno";
        try {
            this.loadSplashes();
        }
        catch (IOException ex) {}
        this.updateCounter = GuiVanillaMainMenu.field_175374_h.nextFloat();
        this.strUpdateYourFuckingOpenGL = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.strUpdateYourFuckingOpenGL = I18n.format("title.oldgl1", new Object[0]);
            this.strUpdateYourFuckingOpenGL2 = I18n.format("title.oldgl2", new Object[0]);
            this.strMojangHelpDesk = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
    
    private void loadSplashes() throws IOException {
        BufferedReader reader = null;
        final ArrayList list = Lists.newArrayList();
        reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(GuiVanillaMainMenu.splashTexts).getInputStream(), Charsets.UTF_8));
        String splash;
        while ((splash = reader.readLine()) != null) {
            splash = splash.trim();
            if (!splash.isEmpty()) {
                list.add(splash);
            }
        }
        if (!list.isEmpty()) {
            do {
                this.splashText = list.get(GuiVanillaMainMenu.field_175374_h.nextInt(list.size()));
            } while (this.splashText.hashCode() == 125780783);
        }
        reader.close();
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final Calendar var1 = Calendar.getInstance();
        var1.setTime(new Date());
        if (var1.get(2) + 1 == 11 && var1.get(5) == 9) {
            this.splashText = "Happy birthday, ez!";
        }
        else if (var1.get(2) + 1 == 6 && var1.get(5) == 1) {
            this.splashText = "Happy birthday, Notch!";
        }
        else if (var1.get(2) + 1 == 12 && var1.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        }
        else if (var1.get(2) + 1 == 1 && var1.get(5) == 1) {
            this.splashText = "Happy new year!";
        }
        else if (var1.get(2) + 1 == 10 && var1.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        final boolean var2 = true;
        final int btnInitHeight = this.height / 4 + 48;
        this.addSingleplayerMultiplayerButtons(btnInitHeight, 24);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, btnInitHeight + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, btnInitHeight + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, btnInitHeight + 72 + 12));
        final Object var3 = this.someObject;
        synchronized (this.someObject) {
            this.glOldWidth = this.fontRendererObj.getStringWidth(this.strUpdateYourFuckingOpenGL);
            this.glOldWidth2 = this.fontRendererObj.getStringWidth(this.strUpdateYourFuckingOpenGL2);
            final int offset = Math.max(this.glOldWidth, this.glOldWidth2);
            this.glOldPlacementX = (this.width - offset) / 2;
            this.glOldPlacementY = this.buttonList.get(0).yPosition - 24;
            this.glOldPlacementXOffset = this.glOldPlacementX + offset;
            this.glOldPlacementYOffset = this.glOldPlacementY + 24;
        }
    }
    
    private void addSingleplayerMultiplayerButtons(final int initHeight, final int objHeight) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, initHeight, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, initHeight + objHeight * 1, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(this.btnOnline = new GuiButton(14, this.width / 2 - 100, initHeight + objHeight * 2, I18n.format("menu.online", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        else if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        else if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        else if (button.id == 14 && this.btnOnline.visible) {
            this.switchToRealms();
        }
        else if (button.id == 4) {
            this.mc.shutdown();
        }
    }
    
    private void switchToRealms() {
        final RealmsBridge realms = new RealmsBridge();
        realms.switchToRealms(this);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (id == 13) {
            if (result) {
                try {
                    final Class var3 = Class.forName("java.awt.Desktop");
                    final Object var4 = var3.getMethod("getDesktop", (Class[])new Class[0]).invoke(null, new Object[0]);
                    var3.getMethod("browse", URI.class).invoke(var4, new URI(this.strMojangHelpDesk));
                }
                catch (Throwable var5) {
                    GuiVanillaMainMenu.logger.error("Couldn't open link", var5);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        final Tessellator tess = Tessellator.getInstance();
        final WorldRenderer renderer = tess.getWorldRenderer();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.width / 2 + 90, 55.0f, 0.0f);
        GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f);
        final float splashBaseSize = 1.8f;
        float splashSize = splashBaseSize - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0f * 3.1415927f * 2.0f) * 0.1f);
        splashSize = splashSize * 100.0f / (this.fontRendererObj.getStringWidth(this.splashText) + 32);
        GlStateManager.scale(splashSize, splashSize, splashSize);
        this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        final String strMinecraft = "Minecraft 1.8";
        this.drawString(this.fontRendererObj, strMinecraft, 2, this.height - 10, -1);
        final String strDistro = "Copyright Mojang AB. Do not distribute!";
        this.drawString(this.fontRendererObj, strDistro, this.width - this.fontRendererObj.getStringWidth(strDistro) - 2, this.height - 10, -1);
        if (this.strUpdateYourFuckingOpenGL != null && this.strUpdateYourFuckingOpenGL.length() > 0) {
            Gui.drawRect(this.glOldPlacementX - 2, this.glOldPlacementY - 2, this.glOldPlacementXOffset + 2, this.glOldPlacementYOffset - 1, 1428160512);
            this.drawString(this.fontRendererObj, this.strUpdateYourFuckingOpenGL, this.glOldPlacementX, this.glOldPlacementY, -1);
            this.drawString(this.fontRendererObj, this.strUpdateYourFuckingOpenGL2, (this.width - this.glOldWidth2) / 2, this.buttonList.get(0).yPosition - 12, -1);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        synchronized (this.someObject) {
            if (this.strUpdateYourFuckingOpenGL.length() > 0 && mouseX >= this.glOldPlacementX && mouseX <= this.glOldPlacementXOffset && mouseY >= this.glOldPlacementY && mouseY <= this.glOldPlacementYOffset) {
                final GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.strMojangHelpDesk, 13, true);
                var5.disableSecurityWarning();
                this.mc.displayGuiScreen(var5);
            }
        }
    }
    
    static {
        field_175373_f = new AtomicInteger(0);
        logger = LogManager.getLogger();
        field_175374_h = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
}
