/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.Module;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.ScaleUtils;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.Setting;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.Tab;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class DropdownGUI
extends GuiScreen {
    private final List<Tab> tabs = new CopyOnWriteArrayList<Tab>();
    private boolean dragging;
    private int dragX;
    private int dragY;
    private int alpha;
    private final ResourceLocation hudIcon = new ResourceLocation("liquidbounce/custom_hud_icon.png");
    int alphaBG = 0;

    public void func_73866_w_() {
        float x = 75.0f;
        this.alphaBG = 0;
        if (this.tabs.isEmpty()) {
            for (ModuleCategory value : ModuleCategory.values()) {
                this.tabs.add(new Tab(value, x, 10.0f));
                x += 110.0f;
            }
        }
        if (!(this.field_146297_k.field_71462_r instanceof GuiChest) && this.field_146297_k.field_71462_r != this) {
            for (Tab tab : this.tabs) {
                for (Module module : tab.modules) {
                    module.fraction = 0.0f;
                    for (Setting setting : module.settings) {
                        setting.setPercent(0.0f);
                    }
                }
            }
        }
        super.func_73866_w_();
    }

    public void func_146281_b() {
        super.func_146281_b();
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        if (Mouse.isButtonDown((int)0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.field_146295_m - 5 && mouseY >= this.field_146295_m - 50) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiHudDesigner());
        }
        RenderUtils.drawImage(this.hudIcon, 9, this.field_146295_m - 41, 32, 32);
        GL11.glPushMatrix();
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
        int x = ScaleUtils.getScaledMouseCoordinates(this.field_146297_k, mouseX, mouseY)[0];
        int y = ScaleUtils.getScaledMouseCoordinates(this.field_146297_k, mouseX, mouseY)[1];
        ScaleUtils.scale(this.field_146297_k);
        for (Tab tab : this.tabs) {
            int panlesize;
            int i;
            tab.drawScreen(x, y);
            if (tab.dragging) {
                tab.setPosX(this.dragX + x);
                tab.setPosY(this.dragY + y);
            }
            if ((i = 0) >= (panlesize = this.tabs.size())) continue;
            this.updatemouse();
            ++i;
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();
    }

    public void updatemouse() {
        int scrollWheel = Mouse.getDWheel();
        int panlesize = this.tabs.size();
        for (int i = 0; i < panlesize; ++i) {
            if (scrollWheel < 0) {
                this.tabs.get(i).setPosY(this.tabs.get(i).getPosY() - 15.0f);
                continue;
            }
            if (scrollWheel <= 0) continue;
            this.tabs.get(i).setPosY(this.tabs.get(i).getPosY() + 15.0f);
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        int x = ScaleUtils.getScaledMouseCoordinates(this.field_146297_k, mouseX, mouseY)[0];
        int y = ScaleUtils.getScaledMouseCoordinates(this.field_146297_k, mouseX, mouseY)[1];
        for (Tab tab : this.tabs) {
            if (tab.isHovered(x, y) && mouseButton == 0 && !this.anyDragging()) {
                tab.dragging = true;
                this.dragX = (int)(tab.getPosX() - (float)x);
                this.dragY = (int)(tab.getPosY() - (float)y);
            }
            try {
                tab.mouseClicked(x, y, mouseButton);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    private boolean anyDragging() {
        for (Tab tab : this.tabs) {
            if (!tab.dragging) continue;
            return true;
        }
        return false;
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && !this.areAnyHovered()) {
            this.field_146297_k.func_147108_a(null);
            if (this.field_146297_k.field_71462_r == null) {
                this.field_146297_k.func_71381_h();
            }
        } else {
            this.tabs.forEach(tab -> tab.keyTyped(typedChar, keyCode));
        }
    }

    private boolean areAnyHovered() {
        return false;
    }

    protected void func_146286_b(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.tabs.forEach(tab -> {
                tab.dragging = false;
            });
        }
        this.tabs.forEach(tab -> tab.mouseReleased(mouseX, mouseY, state));
        super.func_146286_b(mouseX, mouseY, state);
    }

    public List<Tab> getTabs() {
        return this.tabs;
    }
}

