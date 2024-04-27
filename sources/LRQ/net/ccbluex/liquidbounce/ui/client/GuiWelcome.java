/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client;

import java.awt.Color;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.GuiMainMenu;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class GuiWelcome
extends WrappedGuiScreen {
    @Override
    public void initGui() {
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() - 40, "Ok"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        IFontRenderer font = Fonts.font35;
        font.drawCenteredString("Thank you for downloading and installing our client!", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)70, 0xFFFFFF, true);
        font.drawCenteredString("Here is some information you might find useful if you are using LiquidBounce for the first time.", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)70 + (float)font.getFontHeight(), 0xFFFFFF, true);
        font.drawCenteredString("\u00a7lClickGUI:", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 3), 0xFFFFFF, true);
        StringBuilder stringBuilder = new StringBuilder().append("Press ");
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(ClickGUI.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        font.drawCenteredString(stringBuilder.append(Keyboard.getKeyName((int)module.getKeyBind())).append(" to open up the ClickGUI").toString(), (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)(this.getRepresentedScreen().getHeight() / 8) + 80.0f + (float)(font.getFontHeight() * 4), 0xFFFFFF, true);
        font.drawCenteredString("Right-click modules with a '+' next to them to edit their settings.", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 5), 0xFFFFFF, true);
        font.drawCenteredString("Hover a module to see it's description.", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 6), 0xFFFFFF, true);
        font.drawCenteredString("\u00a7lImportant Commands:", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 8), 0xFFFFFF, true);
        font.drawCenteredString(".bind <module> <key> / .bind <module> none", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 9), 0xFFFFFF, true);
        font.drawCenteredString(".autosettings load <name> / .autosettings list", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 10), 0xFFFFFF, true);
        font.drawCenteredString("\u00a7lNeed help? Feel free to contact us!", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 12), 0xFFFFFF, true);
        font.drawCenteredString("YouTube: https://youtube.com/ccbluex", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 13), 0xFFFFFF, true);
        font.drawCenteredString("Twitter: https://twitter.com/ccbluex", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 14), 0xFFFFFF, true);
        font.drawCenteredString("Forum: https://forum.ccbluex.net/", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + (float)80 + (float)(font.getFontHeight() * 15), 0xFFFFFF, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        Fonts.font40.drawCenteredString("Welcome!", (float)(this.getRepresentedScreen().getWidth() / 2) / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f / (float)2 + (float)20, new Color(0, 140, 255).getRGB(), true);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        if (button.getId() == 1) {
            MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiMainMenu()));
        }
    }
}

