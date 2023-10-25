package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.optifine.gui.GuiButtonOF;
import net.optifine.gui.GuiScreenCapeOF;

/*
 * Exception performing whole class analysis ignored.
 */
public class GuiCustomizeSkin
extends GuiScreen {
    private final GuiScreen parentScreen;
    private String title;

    public GuiCustomizeSkin(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    public void initGui() {
        int i = 0;
        this.title = I18n.format((String)"options.skinCustomisation.title", (Object[])new Object[0]);
        for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
            this.buttonList.add((Object)new ButtonPart(this, enumplayermodelparts.getPartId(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts, null));
            ++i;
        }
        if (i % 2 == 1) {
            ++i;
        }
        this.buttonList.add((Object)new GuiButtonOF(210, width / 2 - 100, height / 6 + 24 * (i >> 1), I18n.format((String)"of.options.skinCustomisation.ofCape", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(200, width / 2 - 100, height / 6 + 24 * ((i += 2) >> 1), I18n.format((String)"gui.done", (Object[])new Object[0])));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 210) {
                this.mc.displayGuiScreen((GuiScreen)new GuiScreenCapeOF((GuiScreen)this));
            }
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button instanceof ButtonPart) {
                EnumPlayerModelParts enumplayermodelparts = ButtonPart.access$100((ButtonPart)((ButtonPart)button));
                this.mc.gameSettings.switchModelPartEnabled(enumplayermodelparts);
                button.displayString = this.func_175358_a(enumplayermodelparts);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String func_175358_a(EnumPlayerModelParts playerModelParts) {
        String s = this.mc.gameSettings.getModelParts().contains((Object)playerModelParts) ? I18n.format((String)"options.on", (Object[])new Object[0]) : I18n.format((String)"options.off", (Object[])new Object[0]);
        return playerModelParts.func_179326_d().getFormattedText() + ": " + s;
    }

    static /* synthetic */ String access$200(GuiCustomizeSkin x0, EnumPlayerModelParts x1) {
        return x0.func_175358_a(x1);
    }
}
