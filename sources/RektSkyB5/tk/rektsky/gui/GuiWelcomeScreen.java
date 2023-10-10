/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.Client;
import tk.rektsky.main.HwidUtil;
import tk.rektsky.utils.DiscordManager;

public class GuiWelcomeScreen
extends GuiScreen {
    private long firstRenderTime = Minecraft.getSystemTime();
    public static boolean setupDiscord = false;
    public static ResourceLocation avatar;
    private static HyperiumFontRenderer frTitle;
    private static HyperiumFontRenderer frDescription;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Color color;
        long renderedTime = Minecraft.getSystemTime() - this.firstRenderTime;
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("rektsky/images/background.png"));
        GuiWelcomeScreen.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        if (avatar == null && setupDiscord) {
            avatar = this.mc.getTextureManager().getDynamicTextureLocation(Client.user.discordId + "", DiscordManager.getAvatar());
        }
        frTitle.drawString("Welcome back!", (float)this.width / 2.0f - frTitle.getWidth("Welcome back!") / 2.0f, (float)this.height / 2.0f - (float)(GuiWelcomeScreen.frTitle.FONT_HEIGHT * 3), 0xFFFFFF);
        frDescription.drawString("Logged in as ", (float)this.width / 2.0f - (frDescription.getWidth("Logged in as  ") + 32.0f + frDescription.getWidth("[" + Client.user.role.name + "] " + Client.user.username)) / 2.0f, (float)this.height / 2.0f + (float)(GuiWelcomeScreen.frTitle.FONT_HEIGHT * 5), 0xFFFFFF);
        frDescription.drawString("[" + Client.user.role.name + "] " + Client.user.username, (float)this.width / 2.0f - (frDescription.getWidth("Logged in as  ") + 32.0f + frDescription.getWidth("[" + Client.user.role.name + "] " + Client.user.username)) / 2.0f + (frDescription.getWidth("Logged in as  ") + 32.0f), (float)this.height / 2.0f + (float)(GuiWelcomeScreen.frTitle.FONT_HEIGHT * 5), 0xFFFFFF);
        if (avatar != null) {
            this.mc.getTextureManager().bindTexture(avatar);
            GuiWelcomeScreen.drawModalRectWithCustomSizedTexture(Math.round((float)this.width / 2.0f - (frDescription.getWidth("Logged in as  ") + 32.0f + frDescription.getWidth("[" + Client.user.role.name + "] " + Client.user.username)) / 2.0f + frDescription.getWidth("Logged in as  ")), Math.round((float)this.height / 2.0f + (float)(GuiWelcomeScreen.frTitle.FONT_HEIGHT * 5) - 8.0f), 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        }
        if (renderedTime <= 3000L) {
            color = new Color(0.0f, 0.0f, 0.0f, 1.0f - (float)renderedTime / 3000.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, (float)renderedTime / 3000.0f);
            this.drawGradientRect(0, 0, this.width, this.height, color.getRGB(), color.getRGB());
        }
        if (renderedTime >= 7999L) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        if (renderedTime >= 5000L) {
            color = new Color(0.0f, 0.0f, 0.0f, Math.min(((float)renderedTime - 5000.0f) / 3000.0f, 1.0f));
            GlStateManager.color(1.0f, 1.0f, 1.0f, (float)(renderedTime - 8000L) / 3000.0f);
            this.drawGradientRect(0, 0, this.width, this.height, color.getRGB(), color.getRGB());
        }
    }

    private boolean checkAuth(Minecraft mc) throws Exception {
        return true;
    }

    @Override
    public void initGui() {
        Runnable r2 = () -> {
            Runnable r2 = () -> {
                try {
                    try {
                        if (!this.checkAuth(this.mc)) {
                            throw new BootstrapMethodError();
                        }
                    }
                    catch (Exception e2) {
                        throw new BootstrapMethodError();
                    }
                }
                catch (BootstrapMethodError ex) {
                    try {
                        StringSelection stringSelection = new StringSelection(HwidUtil.getHwid());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                        JOptionPane.showMessageDialog(null, "HWID mismatch (0x01) your HWID is: " + HwidUtil.getHwid() + " (copied to clipboard)", "Error", 0);
                        System.exit(-42);
                    }
                    catch (Exception e3) {
                        JOptionPane.showMessageDialog(null, "Failed to get your HWID (0x02)", "Error", 0);
                        System.exit(-42);
                    }
                }
            };
            r2.run();
        };
        r2.run();
        if (DiscordManager.discordUser != null) {
            setupDiscord = true;
        }
    }

    static {
        frTitle = Client.getFontWithSize(112);
        frDescription = Client.getFontWithSize(36);
    }
}

