/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking
extends GuiScreen
implements IProgressUpdate {
    private boolean doneWorking;
    private int progress;
    private String field_146589_f = "";
    private String field_146591_a = "";

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (this.doneWorking) {
            if (!this.mc.func_181540_al()) {
                this.mc.displayGuiScreen(null);
            }
        } else {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, this.field_146591_a, width / 2, 70, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, String.valueOf(this.field_146589_f) + " " + this.progress + "%", width / 2, 90, 0xFFFFFF);
            super.drawScreen(n, n2, f);
        }
    }

    @Override
    public void displayLoadingString(String string) {
        this.field_146589_f = string;
        this.setLoadingProgress(0);
    }

    @Override
    public void resetProgressAndMessage(String string) {
        this.field_146591_a = string;
        this.displayLoadingString("Working...");
    }

    @Override
    public void setDoneWorking() {
        this.doneWorking = true;
    }

    @Override
    public void setLoadingProgress(int n) {
        this.progress = n;
    }

    @Override
    public void displaySavingString(String string) {
        this.resetProgressAndMessage(string);
    }
}

