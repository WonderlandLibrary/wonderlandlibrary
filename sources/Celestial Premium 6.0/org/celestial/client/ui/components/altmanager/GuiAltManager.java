/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.altmanager;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.altmanager.GuiAddAlt;
import org.celestial.client.ui.components.altmanager.GuiAltLogin;
import org.celestial.client.ui.components.altmanager.alt.Alt;
import org.celestial.client.ui.components.altmanager.alt.AltLoginThread;
import org.celestial.client.ui.components.altmanager.alt.AltManager;
import org.celestial.client.ui.components.altmanager.althening.api.AltService;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager
extends GuiScreen {
    public static final AltService altService = new AltService();
    public Alt selectedAlt = null;
    public String status = (Object)((Object)TextFormatting.DARK_GRAY) + "(" + (Object)((Object)TextFormatting.GRAY) + AltManager.registry.size() + (Object)((Object)TextFormatting.DARK_GRAY) + ")";
    private GuiButton login;
    private GuiButton remove;
    private AltLoginThread loginThread;
    private double offset;
    private GuiTextField searchField;
    private ResourceLocation resourceLocation;

    private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager textureManager = this.mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                break;
            }
            case 1: {
                this.loginThread = new AltLoginThread(this.selectedAlt);
                this.loginThread.start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.registry.remove(this.selectedAlt);
                this.status = "\u00a7aRemoved.";
                this.selectedAlt = null;
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                String randomName = "Celestial" + RandomStringUtils.randomAlphabetic(2).toUpperCase() + RandomStringUtils.randomNumeric(4);
                this.loginThread = new AltLoginThread(new Alt(randomName, ""));
                this.loginThread.start();
                AltManager.registry.add(new Alt(randomName, ""));
                break;
            }
            case 7: {
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            }
            case 8: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 9: {
                AltManager.registry.clear();
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RectHelper.drawRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(17, 17, 17, 255).getRGB());
        RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), 0.0f, 0.0f, sr.getScaledWidth(), 2.0f, ClientHelper.getClientColor());
        super.drawScreen(par1, par2, par3);
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0;
                }
            } else if (wheel > 0) {
                this.offset -= 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0;
                }
            }
        }
        String altName = "Name: " + (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : this.mc.session.getUsername());
        this.mc.fontRenderer.drawStringWithShadow((Object)((Object)TextFormatting.GRAY) + "~ User Info ~", 16.0, 75.0, -1);
        this.mc.fontRenderer.drawStringWithShadow(altName, 11.0, 88.0, 0xDDDDDD);
        this.mc.fontRenderer.drawStringWithShadow("Account Status: " + (Object)((Object)TextFormatting.GREEN) + "Working", 11.0, 99.0, 0xDDDDDD);
        RectHelper.drawRect(this.mc.fontRenderer.getStringWidth("Account Status: Working") + 14, this.mc.fontRenderer.getStringHeight("Account Status: Working") + 89, 9.0, this.mc.fontRenderer.getStringHeight("Account Status: Working") + 101, PaletteHelper.getColor(255, 30));
        RectHelper.drawRect(this.mc.fontRenderer.getStringWidth(altName) + 14, this.mc.fontRenderer.getStringHeight(altName) + 78, 9.0, this.mc.fontRenderer.getStringHeight(altName) + 89, PaletteHelper.getColor(255, 30));
        RectHelper.drawBorderedRect(92.0, 82.0, 92.0, 12.0, 3.0, -1, -1);
        GlStateManager.pushMatrix();
        RenderHelper.drawImage(new ResourceLocation("celestial/info.png"), 13.0f, 8.0f, 64.0f, 64.0f, Color.white);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        this.mc.fontRenderer.drawCenteredString("Account Manager", (float)this.width / 2.0f, 10.0f, -1);
        this.mc.fontRenderer.drawCenteredString(this.loginThread == null ? this.status : this.loginThread.getStatus(), (float)this.width / 2.0f, 20.0f, -1);
        GlStateManager.pushMatrix();
        RenderHelper.scissorRect(0.0f, 33.0f, this.width, this.height - 50);
        GL11.glEnable(3089);
        int y = 38;
        int number = 0;
        Iterator<Alt> e = this.getAlts().iterator();
        while (true) {
            if (!e.hasNext()) {
                GL11.glDisable(3089);
                GL11.glPopMatrix();
                if (this.selectedAlt == null) {
                    this.login.enabled = false;
                    this.remove.enabled = false;
                } else {
                    this.login.enabled = true;
                    this.remove.enabled = true;
                }
                if (Keyboard.isKeyDown(200)) {
                    this.offset -= 26.0;
                } else if (Keyboard.isKeyDown(208)) {
                    this.offset += 26.0;
                }
                if (this.offset < 0.0) {
                    this.offset = 0.0;
                }
                this.searchField.drawTextBox();
                if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
                    this.mc.fontRenderer.drawStringWithShadow("Search Alt", (float)this.width / 2.0f + 175.0f, this.height - 18, PaletteHelper.getColor(180));
                }
                return;
            }
            Alt alt = e.next();
            if (!this.isAltInArea(y)) continue;
            ++number;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            if (name.equalsIgnoreCase(this.mc.session.getUsername())) {
                name = "\u00a7n" + name;
            }
            String prefix = alt.getStatus().equals((Object)Alt.Status.Banned) ? "\u00a7c" : (alt.getStatus().equals((Object)Alt.Status.NotWorking) ? "\u00a7m" : "");
            name = prefix + name + "\u00a7r \u00a77| " + alt.getStatus().toFormatted();
            String pass = alt.getPassword().equals("") ? "\u00a7cCracked" : alt.getPassword().replaceAll(".", "*");
            if (alt == this.selectedAlt) {
                GlStateManager.pushMatrix();
                boolean hovering = (float)par1 >= (float)this.width / 1.5f + 5.0f && (double)par1 <= (double)this.width / 1.5 + 26.0 && (double)par2 >= (double)y - this.offset - 4.0 && (double)par2 <= (double)y - this.offset + 20.0;
                RectHelper.drawBorderedRect((float)this.width / 1.5f + 5.0f, (double)y - this.offset - 4.0, (double)this.width / 1.5 + 26.0, (double)y - this.offset + 20.0, 1.0, PaletteHelper.getColor(80, 255), hovering ? PaletteHelper.getColor(30, 222) : PaletteHelper.getColor(20, 255));
                RenderHelper.drawImage(new ResourceLocation("celestial/change.png"), (float)this.width / 1.5f + 8.0f, (float)((double)y - this.offset), 15.0f, 15.0f, hovering ? Color.GRAY : Color.WHITE);
                GlStateManager.popMatrix();
                if (this.isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RectHelper.drawBorderedRect((float)this.width / 2.0f - 125.0f, (double)y - this.offset - 4.0, (float)this.width / 1.5f, (double)y - this.offset + 30.0, 1.0, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50));
                } else if (this.isMouseOverAlt(par1, par2, (double)y - this.offset)) {
                    RectHelper.drawBorderedRect((float)this.width / 2.0f - 125.0f, (double)y - this.offset - 4.0, (float)this.width / 1.5f, (double)y - this.offset + 30.0, 1.0, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50));
                } else {
                    RectHelper.drawBorderedRect((float)this.width / 2.0f - 125.0f, (double)y - this.offset - 4.0, (float)this.width / 1.5f, (double)y - this.offset + 30.0, 1.0, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50));
                }
            }
            String numberP = "\u00a77" + number + ". \u00a7f";
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (this.resourceLocation == null) {
                this.resourceLocation = AbstractClientPlayer.getLocationSkin(name);
                this.getDownloadImageSkin(this.resourceLocation, name);
            } else {
                this.mc.getTextureManager().bindTexture(this.resourceLocation);
                GlStateManager.enableTexture2D();
                Gui.drawScaledCustomSizeModalRect((float)this.width / 2.0f - 161.0f, (float)y - (float)this.offset - 4.0f, 8.0f, 8.0f, 8.0f, 8.0f, 33.0f, 33.0f, 64.0f, 64.0f);
            }
            GlStateManager.popMatrix();
            this.mc.fontRenderer.drawCenteredString(numberP + (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : name), (float)this.width / 2.0f, (float)((double)y - this.offset + 5.0), -1);
            this.mc.fontRenderer.drawCenteredString((alt.getStatus().equals((Object)Alt.Status.NotWorking) ? "\u00a7m" : "") + pass, (float)this.width / 2.0f, (float)((double)y - this.offset + 15.0), PaletteHelper.getColor(110));
            y += 40;
        }
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(3, this.width / 2 + 34, this.height - 24, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 86, this.height - 48, 100, 20, "Direct Auth"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 124, this.height - 24, 78, 20, "Random"));
        this.buttonList.add(new GuiButton(7, this.width / 2 - 190, this.height - 24, 60, 20, "Back"));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 18, this.height - 48, 116, 20, "MultiPlayer"));
        this.buttonList.add(new GuiButton(9, this.width / 2 + 137, this.height - 48, 116, 20, "Clear Alts"));
        this.searchField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 + 138, this.height - 23, 115, 17);
        this.login = new GuiButton(1, this.width / 2 - 190, this.height - 48, 100, 20, "Auth");
        this.buttonList.add(this.login);
        this.remove = new GuiButton(2, this.width / 2 - 40, this.height - 24, 70, 20, "Remove");
        this.buttonList.add(this.remove);
        this.login.enabled = false;
        this.remove.enabled = false;
        super.initGui();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.searchField.textboxKeyTyped(par1, par2);
        if ((par1 == '\t' || par1 == '\r') && this.searchField.isFocused()) {
            this.searchField.setFocused(!this.searchField.isFocused());
        }
        try {
            super.keyTyped(par1, par2);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAltInArea(int y) {
        return (double)y - this.offset <= (double)(this.height - 50);
    }

    private boolean isMouseOverAlt(double x, double y, double y1) {
        return x >= (double)((float)this.width / 2.0f - 125.0f) && y >= y1 - 4.0 && x <= (double)this.width / 1.5 && y <= y1 + 30.0 && x >= 0.0 && y >= 33.0 && x <= (double)this.width && y <= (double)(this.height - 50);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        this.searchField.mouseClicked(par1, par2, par3);
        if (this.offset < 0.0) {
            this.offset = 0.0;
        }
        double y = 38.0 - this.offset;
        for (Alt alt : this.getAlts()) {
            boolean hovering;
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed(this.login);
                    return;
                }
                this.selectedAlt = alt;
            }
            boolean bl = hovering = (float)par1 >= (float)this.width / 1.5f + 5.0f && (double)par1 <= (double)this.width / 1.5 + 26.0 && (double)par2 >= y - 4.0 && (double)par2 <= y + 20.0;
            if (hovering && alt == this.selectedAlt) {
                switch (alt.getStatus()) {
                    case Unchecked: {
                        alt.setStatus(Alt.Status.Working);
                        break;
                    }
                    case Working: {
                        alt.setStatus(Alt.Status.Banned);
                        break;
                    }
                    case Banned: {
                        alt.setStatus(Alt.Status.NotWorking);
                        break;
                    }
                    case NotWorking: {
                        alt.setStatus(Alt.Status.Unchecked);
                    }
                }
            }
            y += 40.0;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Alt> getAlts() {
        ArrayList<Alt> altList = new ArrayList<Alt>();
        Iterator iterator = AltManager.registry.iterator();
        while (iterator.hasNext()) {
            Alt alt = (Alt)iterator.next();
            if (!this.searchField.getText().isEmpty() && !alt.getMask().toLowerCase().contains(this.searchField.getText().toLowerCase()) && !alt.getUsername().toLowerCase().contains(this.searchField.getText().toLowerCase())) continue;
            altList.add(alt);
        }
        return altList;
    }
}

