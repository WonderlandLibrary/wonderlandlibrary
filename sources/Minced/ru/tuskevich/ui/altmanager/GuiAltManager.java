// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import ru.tuskevich.util.color.ColorUtility;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.lwjgl.opengl.GL11;
import ru.tuskevich.util.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import ru.tuskevich.util.render.RenderUtility;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Session;
import ru.tuskevich.util.math.RandomUtility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.IImageBuffer;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import ru.tuskevich.ui.altmanager.alt.AltManager;
import net.minecraft.util.text.TextFormatting;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiTextField;
import ru.tuskevich.ui.altmanager.alt.AltLoginThread;
import ru.tuskevich.ui.altmanager.alt.Alt;
import ru.tuskevich.ui.altmanager.api.AltService;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    public static final AltService altService;
    public Alt selectedAlt;
    public String status;
    private GuiAltButton login;
    private GuiAltButton remove;
    private AltLoginThread loginThread;
    private float offset;
    private GuiTextField searchField;
    private ResourceLocation resourceLocation;
    private Color gradientColor1;
    private Color gradientColor2;
    private Color gradientColor3;
    private Color gradientColor4;
    
    public GuiAltManager() {
        this.selectedAlt = null;
        this.gradientColor1 = Color.WHITE;
        this.gradientColor2 = Color.WHITE;
        this.gradientColor3 = Color.WHITE;
        this.gradientColor4 = Color.WHITE;
        this.status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
    }
    
    private void getDownloadImageSkin(final ResourceLocation resourceLocationIn, final String username) {
        final TextureManager textureManager = GuiAltManager.mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        final ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.removeAccount(this.selectedAlt);
                this.status = TextFormatting.GREEN + "Removed.";
                this.selectedAlt = null;
                break;
            }
            case 3: {
                GuiAltManager.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                GuiAltManager.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                final String randomName = RandomUtility.randomString(10);
                AltManager.addAccount(new Alt(randomName, ""));
                GuiAltManager.mc.session = new Session(randomName, "", "", "mojang");
                break;
            }
            case 6: {
                GuiAltManager.mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            }
            case 7: {
                GuiAltManager.mc.displayGuiScreen(new GuiMainMenu());
                break;
            }
            case 8: {
                AltManager.clearAccounts();
                break;
            }
            case 8931: {
                GuiAltManager.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 4545: {
                GuiAltManager.mc.displayGuiScreen(new GuiConnecting(this, GuiAltManager.mc, new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "play.hypixel.net", false)));
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        RenderUtility.drawRect(0.0, 0.0, GuiAltManager.mc.displayWidth, GuiAltManager.mc.displayHeight, new Color(17, 17, 17).getRGB());
        super.drawScreen(par1, par2, par3);
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0f;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0f;
                }
            }
        }
        final String altName = "Name: " + GuiAltManager.mc.session.getUsername();
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        Fonts.MONTSERRAT16.drawCenteredString(altName, this.width / 2.0f, 10.0f, -1);
        GlStateManager.pushMatrix();
        RenderUtility.scissorRect(0.0f, 33.0f, (float)this.width, this.height - 50);
        GL11.glEnable(3089);
        int y = 38;
        int number = 0;
        final Iterator<Alt> e = this.getAlts().iterator();
        while (e.hasNext()) {
            final boolean small = GuiAltManager.mc.displayWidth < 900 && GuiAltManager.mc.displayHeight < 900;
            if (small) {
                RenderUtility.drawRectNotWH(this.width / 2.0f - 120.0f, y - 10.0, (float)this.width / 1.3 + 5.0, y + 30.0, new Color(0, 0, 0, 40).getRGB());
            }
            else {
                RenderUtility.drawRectNotWH(this.width / 2.0f - 120.0f, y - 10.0, (float)this.width / 1.6, y + 30.0, new Color(0, 0, 0, 40).getRGB());
            }
            final Alt alt = e.next();
            if (!this.isAltInArea(y)) {
                continue;
            }
            ++number;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            if (name.equalsIgnoreCase(GuiAltManager.mc.session.getUsername())) {
                name = "§a" + name;
            }
            final String prefix = alt.getStatus().equals(Alt.Status.Banned) ? "§c" : (alt.getStatus().equals(Alt.Status.NotWorking) ? "§m" : "");
            name = prefix + name + "§r ";
            final String pass = alt.getPassword().equals("") ? "§cNot License" : "§aLicense";
            if (alt == this.selectedAlt) {
                GlStateManager.pushMatrix();
                final boolean hovering = par1 >= this.width / 1.5f + 5.0f && par1 <= this.width / 1.5 + 26.0 && par2 >= y - (double)this.offset - 4.0 && par2 <= y - (double)this.offset + 20.0;
                GlStateManager.popMatrix();
                if (this.isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RenderUtility.drawRectNotWH(this.width / 2.0f - 120.0f, y - (double)this.offset - 4.0, this.width / 1.6f, y - (double)this.offset + 30.0, new Color(50, 50, 50, 33).getRGB());
                }
                else if (this.isMouseOverAlt(par1, par2, y - (double)this.offset)) {
                    RenderUtility.drawRectNotWH(this.width / 2.0f - 120.0f, y - (double)this.offset - 4.0, this.width / 1.6f, y - (double)this.offset + 30.0, new Color(50, 50, 50, 33).getRGB());
                }
                else {
                    RenderUtility.drawRectNotWH(this.width / 2.0f - 120.0f, y - (double)this.offset - 4.0, this.width / 1.6f, y - (double)this.offset + 30.0, new Color(50, 50, 50, 33).getRGB());
                }
            }
            final String numberP = "§7" + number + ". §f";
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (this.resourceLocation == null) {
                this.getDownloadImageSkin(this.resourceLocation = AbstractClientPlayer.getLocationSkin(name), name);
            }
            else {
                GuiAltManager.mc.getTextureManager().bindTexture(this.resourceLocation);
                GlStateManager.enableTexture2D();
                Gui.drawScaledCustomSizeModalRect((float)(int)(this.width / 2.0f - 110.0f), (float)(int)(y - this.offset), 8.0f, 8.0f, 8, 8, 25, 25, 64.0f, 64.0f);
            }
            GlStateManager.popMatrix();
            Fonts.MONTSERRAT16.drawString(name, this.width / 2.0f - 80.0f, (float)(y - (double)this.offset + 5.0), -1);
            Fonts.MONTSERRAT16.drawString((alt.getStatus().equals(Alt.Status.NotWorking) ? "§m" : "") + pass, this.width / 2.0f - 80.0f, (float)(y - (double)this.offset + 15.0), ColorUtility.getColor(110));
            y += 40;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
        }
        else {
            this.login.enabled = true;
            this.remove.enabled = true;
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 26.0;
        }
        else if (Keyboard.isKeyDown(208)) {
            this.offset += 26.0;
        }
        if (this.offset < 0.0) {
            this.offset = 0.0f;
        }
    }
    
    @Override
    public void initGui() {
        this.searchField = new GuiTextField(this.eventButton, GuiAltManager.mc.fontRenderer, this.width / 2 + 116, this.height - 22, 72, 16);
        this.buttonList.add(this.login = new GuiAltButton(1, this.width / 2 - 122, this.height - 48, 60, 20, "Login"));
        this.buttonList.add(this.remove = new GuiAltButton(2, this.width / 2 + 4 + 77, this.height - 48, 40, 20, "Remove"));
        this.buttonList.add(new GuiAltButton(3, this.width / 2 + 4 - 60, this.height - 48, 65, 20, "Add Alt"));
        this.buttonList.add(new GuiAltButton(5, this.width / 2 + 15, this.height - 48, 60, 20, "Random"));
        this.buttonList.add(new GuiAltButton(7, this.width / 2 - 122, this.height - 24, 245, 20, "Back"));
        this.buttonList.add(new GuiAltButton(8, this.width / 2 + 4 + 124, this.height - 25, 60, 20, "Clear All"));
        this.login.enabled = false;
        this.remove.enabled = false;
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
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
    
    private boolean isAltInArea(final int y) {
        return y - this.offset <= this.height - 50;
    }
    
    private boolean isMouseOverAlt(final double x, final double y, final double y1) {
        return x >= this.width / 2.0f - 125.0f && y >= y1 - 4.0 && x <= this.width / 1.5 && y <= y1 + 20.0 && x >= 0.0 && y >= 33.0 && x <= this.width && y <= this.height - 50;
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        this.searchField.mouseClicked(par1, par2, par3);
        if (this.offset < 0.0f) {
            this.offset = 0.0f;
        }
        double y = 38.0f - this.offset;
        for (final Alt alt : this.getAlts()) {
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed(this.login);
                    return;
                }
                this.selectedAlt = alt;
            }
            y += 40.0;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    private List<Alt> getAlts() {
        final List<Alt> altList = new ArrayList<Alt>();
        for (final Alt alt : AltManager.registry) {
            if (this.searchField.getText().isEmpty() || alt.getMask().toLowerCase().contains(this.searchField.getText().toLowerCase()) || alt.getUsername().toLowerCase().contains(this.searchField.getText().toLowerCase())) {
                altList.add(alt);
            }
        }
        return altList;
    }
    
    static {
        altService = new AltService();
    }
}
