/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.util.UUIDTypeAdapter
 */
package net.minecraft.realms;

import com.mojang.util.UUIDTypeAdapter;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.util.ResourceLocation;

public class RealmsScreen {
    public static final int SKIN_HAT_WIDTH = 8;
    public static final int SKIN_HAT_HEIGHT = 8;
    public static final int SKIN_HEAD_U = 8;
    public static final int SKIN_TEX_HEIGHT = 64;
    public static final int SKIN_HAT_V = 8;
    public static final int SKIN_TEX_WIDTH = 64;
    protected Minecraft minecraft;
    public int height;
    public static final int SKIN_HAT_U = 40;
    public static final int SKIN_HEAD_HEIGHT = 8;
    public int width;
    private GuiScreenRealmsProxy proxy = new GuiScreenRealmsProxy(this);
    public static final int SKIN_HEAD_V = 8;
    public static final int SKIN_HEAD_WIDTH = 8;

    public void renderTooltip(List<String> list, int n, int n2) {
        this.proxy.drawHoveringText(list, n, n2);
    }

    public void renderTooltip(String string, int n, int n2) {
        this.proxy.drawCreativeTabHoveringText(string, n, n2);
    }

    public int height() {
        return GuiScreenRealmsProxy.height;
    }

    public static void bind(String string) {
        ResourceLocation resourceLocation = new ResourceLocation(string);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }

    public void keyPressed(char c, int n) {
    }

    public void drawString(String string, int n, int n2, int n3) {
        this.proxy.func_154322_b(string, n, n2, n3);
    }

    public void init() {
    }

    public void removed() {
    }

    public void buttonClicked(RealmsButton realmsButton) {
    }

    public RealmsAnvilLevelStorageSource getLevelStorageSource() {
        return new RealmsAnvilLevelStorageSource(Minecraft.getMinecraft().getSaveLoader());
    }

    public List<String> fontSplit(String string, int n) {
        return this.proxy.func_154323_a(string, n);
    }

    public List<RealmsButton> buttons() {
        return this.proxy.func_154320_j();
    }

    public static RealmsButton newButton(int n, int n2, int n3, String string) {
        return new RealmsButton(n, n2, n3, string);
    }

    public void mouseClicked(int n, int n2, int n3) {
    }

    public void tick() {
    }

    public void mouseEvent() {
    }

    public void keyboardEvent() {
    }

    public int fontLineHeight() {
        return this.proxy.func_154329_h();
    }

    public int fontWidth(String string) {
        return this.proxy.func_154326_c(string);
    }

    public void init(Minecraft minecraft, int n, int n2) {
    }

    public static String getLocalizedString(String string, Object ... objectArray) {
        return I18n.format(string, objectArray);
    }

    public void mouseReleased(int n, int n2, int n3) {
    }

    public int width() {
        return GuiScreenRealmsProxy.width;
    }

    public void renderTooltip(ItemStack itemStack, int n, int n2) {
        this.proxy.renderToolTip(itemStack, n, n2);
    }

    public boolean isPauseScreen() {
        return this.proxy.doesGuiPauseGame();
    }

    public void buttonsClear() {
        this.proxy.func_154324_i();
    }

    public void mouseDragged(int n, int n2, int n3, long l) {
    }

    public void fontDrawShadow(String string, int n, int n2, int n3) {
        this.proxy.func_154319_c(string, n, n2, n3);
    }

    public static void bindFace(String string, String string2) {
        ResourceLocation resourceLocation = AbstractClientPlayer.getLocationSkin(string2);
        if (resourceLocation == null) {
            resourceLocation = DefaultPlayerSkin.getDefaultSkin(UUIDTypeAdapter.fromString((String)string));
        }
        AbstractClientPlayer.getDownloadImageSkin(resourceLocation, string2);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }

    public void renderBackground(int n) {
        this.proxy.drawWorldBackground(n);
    }

    public static void blit(int n, int n2, float f, float f2, int n3, int n4, int n5, int n6, float f3, float f4) {
        Gui.drawScaledCustomSizeModalRect(n, n2, f, f2, n3, n4, n5, n6, f3, f4);
    }

    public static void blit(int n, int n2, float f, float f2, int n3, int n4, float f3, float f4) {
        Gui.drawModalRectWithCustomSizedTexture(n, n2, f, f2, n3, n4, f3, f4);
    }

    public void fillGradient(int n, int n2, int n3, int n4, int n5, int n6) {
        this.proxy.drawGradientRect(n, n2, n3, n4, n5, n6);
    }

    public void buttonsAdd(RealmsButton realmsButton) {
        this.proxy.func_154327_a(realmsButton);
    }

    public void confirmResult(boolean bl, int n) {
    }

    public RealmsEditBox newEditBox(int n, int n2, int n3, int n4, int n5) {
        return new RealmsEditBox(n, n2, n3, n4, n5);
    }

    public static String getLocalizedString(String string) {
        return I18n.format(string, new Object[0]);
    }

    public void blit(int n, int n2, int n3, int n4, int n5, int n6) {
        this.proxy.drawTexturedModalRect(n, n2, n3, n4, n5, n6);
    }

    public GuiScreenRealmsProxy getProxy() {
        return this.proxy;
    }

    public void renderBackground() {
        this.proxy.drawDefaultBackground();
    }

    public void buttonsRemove(RealmsButton realmsButton) {
        this.proxy.func_154328_b(realmsButton);
    }

    public void render(int n, int n2, float f) {
        int n3 = 0;
        while (n3 < this.proxy.func_154320_j().size()) {
            this.proxy.func_154320_j().get(n3).render(n, n2);
            ++n3;
        }
    }

    public void drawCenteredString(String string, int n, int n2, int n3) {
        this.proxy.func_154325_a(string, n, n2, n3);
    }

    public static RealmsButton newButton(int n, int n2, int n3, int n4, int n5, String string) {
        return new RealmsButton(n, n2, n3, n4, n5, string);
    }
}

