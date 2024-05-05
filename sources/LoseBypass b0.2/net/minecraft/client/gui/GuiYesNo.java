/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;

public class GuiYesNo
extends GuiScreen {
    protected GuiYesNoCallback parentScreen;
    protected String messageLine1;
    private String messageLine2;
    private final List<String> field_175298_s = Lists.newArrayList();
    protected String confirmButtonText;
    protected String cancelButtonText;
    protected int parentButtonClickedId;
    private int ticksUntilEnable;

    public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_, String p_i1082_3_, int p_i1082_4_) {
        this.parentScreen = p_i1082_1_;
        this.messageLine1 = p_i1082_2_;
        this.messageLine2 = p_i1082_3_;
        this.parentButtonClickedId = p_i1082_4_;
        this.confirmButtonText = I18n.format("gui.yes", new Object[0]);
        this.cancelButtonText = I18n.format("gui.no", new Object[0]);
    }

    public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_, String p_i1083_3_, String p_i1083_4_, String p_i1083_5_, int p_i1083_6_) {
        this.parentScreen = p_i1083_1_;
        this.messageLine1 = p_i1083_2_;
        this.messageLine2 = p_i1083_3_;
        this.confirmButtonText = p_i1083_4_;
        this.cancelButtonText = p_i1083_5_;
        this.parentButtonClickedId = p_i1083_6_;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
        this.field_175298_s.clear();
        this.field_175298_s.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 0xFFFFFF);
        int i = 90;
        Iterator<String> iterator = this.field_175298_s.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }
            String s = iterator.next();
            this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 0xFFFFFF);
            i += this.fontRendererObj.FONT_HEIGHT;
        }
    }

    public void setButtonDelay(int p_146350_1_) {
        this.ticksUntilEnable = p_146350_1_;
        Iterator iterator = this.buttonList.iterator();
        while (iterator.hasNext()) {
            GuiButton guibutton = (GuiButton)iterator.next();
            guibutton.enabled = false;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (--this.ticksUntilEnable != 0) return;
        Iterator iterator = this.buttonList.iterator();
        while (iterator.hasNext()) {
            GuiButton guibutton = (GuiButton)iterator.next();
            guibutton.enabled = true;
        }
    }
}

