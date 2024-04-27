/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package dev.sakura_starring.ui;

import dev.sakura_starring.ui.GuiMainMenu;
import dev.sakura_starring.util.render.Screen;
import dev.sakura_starring.util.safe.HWIDUtil;
import dev.sakura_starring.util.safe.QQUtils;
import dev.sakura_starring.util.safe.Safe;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

public class GuiLogin
extends WrappedGuiScreen {
    public static boolean login = false;
    public static boolean welcome = true;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.representedScreen.drawBackground(0);
        int n3 = 400;
        int n4 = 250;
        int n5 = Screen.getWidth() / 2 - n3 / 2;
        int n6 = Screen.getHeight() / 2 - n4 / 2;
        RenderUtils.drawImage(LiquidBounce.wrapper.getClassProvider().createResourceLocation("atfield" + "/login.png".toLowerCase()), Screen.getWidth() / 2 - n3 / 2, Screen.getHeight() / 2 - n4 / 2, n3, n4);
        Fonts.bold36.drawString("Login to AtField", n5 + 5, n6 + 5, Color.black.getRGB());
        Fonts.bold12.drawString("By Sakura\u00b7StarRing", n5 + 5, n6 + 8 + Fonts.bold36.getFontHeight() * 2, Color.black.getRGB());
        super.drawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        int n = 400;
        int n2 = 250;
        int n3 = Screen.getWidth() / 2 - n / 2;
        int n4 = Screen.getHeight() / 2 - n2 / 2;
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(1, n3 + 10, n4 + 220, 100, 20, "HWID Login"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(0, n3 + 290, n4 + 220, 100, 20, "QQ Login"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(2, n3 + 145, n4 + 220, 100, 20, "Exit"));
        if (welcome) {
            welcome = false;
            LiquidBounce.INSTANCE.displayTray("AtField v1.3", "\u4f60\u597d,\u6b22\u8fce\u4f7f\u7528AtField", TrayIcon.MessageType.INFO);
        }
        if (login) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiMainMenu()));
        }
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) {
        if (iGuiButton.getId() == 0) {
            if (!QQUtils.verification()) {
                LiquidBounce.INSTANCE.displayTray("AtField v1.3", "QQ\u9a8c\u8bc1\u5931\u8d25", TrayIcon.MessageType.ERROR);
                StringSelection stringSelection = new StringSelection(QQUtils.qqNumber);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } else {
                LiquidBounce.INSTANCE.displayTray("AtField v1.3", "QQ\u9a8c\u8bc1\u6210\u529f", TrayIcon.MessageType.INFO);
                login = true;
                Safe.verification = true;
                HWIDUtil.username = QQUtils.qqName;
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiMainMenu()));
            }
        } else if (iGuiButton.getId() == 1) {
            if (!HWIDUtil.newVerification()) {
                LiquidBounce.INSTANCE.displayTray("AtField v1.3", "HWID\u9a8c\u8bc1\u5931\u8d25", TrayIcon.MessageType.ERROR);
                StringSelection stringSelection = new StringSelection(HWIDUtil.nativeHwid);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } else {
                LiquidBounce.INSTANCE.displayTray("AtField v1.3", "HWID\u9a8c\u8bc1\u6210\u529f", TrayIcon.MessageType.INFO);
                login = true;
                Safe.verification = true;
                QQUtils.qqName = HWIDUtil.username;
                QQUtils.qqNumber = HWIDUtil.username;
                mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiMainMenu()));
            }
        } else if (iGuiButton.getId() == 2) {
            Minecraft.func_71410_x().func_71400_g();
        }
    }
}

