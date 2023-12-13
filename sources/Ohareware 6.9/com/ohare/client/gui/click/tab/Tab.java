package com.ohare.client.gui.click.tab;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ohare.client.Client;
import com.ohare.client.gui.click.Frame;
import com.ohare.client.gui.click.component.Component;
import com.ohare.client.gui.click.component.comps.ModButton;
import com.ohare.client.module.Module;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;


/**
 * made by oHare for oHareWare
 *
 * @since 6/4/2019
 **/

public class Tab {
    private Module.Category category;
    private int posX, posY, width, height, largestString;
    private ArrayList<Component> components = new ArrayList<>();
    private Frame parent;

    public Tab(Frame parent, Module.Category category, int posX, int posY, int width, int height) {
        this.parent = parent;
        this.category = category;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        largestString = (Fonts.clickfont.getStringWidth(Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(0).getLabel()));
        for (int i = 0; i < Client.INSTANCE.getModuleManager().getCategoryCheats(category).size(); i++) {
            if (Fonts.clickfont.getStringWidth(Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getLabel()) > largestString) {
                largestString = Fonts.clickfont.getStringWidth(Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getLabel());
            }
        }
        int mody = posY + 8;
        for (Module mod : Client.INSTANCE.getModuleManager().getCategoryCheats(category)) {
            components.add(new ModButton(this, mod, posX + 2 + largestString - Fonts.clickfont.getStringWidth(mod.getLabel()) / 2, mody - parent.getModulescrollY(), largestString + 2, height));
            mody += 16;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (components.isEmpty()) return;
        int valwidth = 4;
        for (Module mod : Client.INSTANCE.getModuleManager().getCategoryCheats(category)) {
            if (!mod.getValues().isEmpty()) valwidth = 12;
        }
        RenderUtil.drawBorderedRoundedRect(posX, posY, largestString * 2 + valwidth, 4 + components.size() * 16 > 180 ? 180 : 4 + components.size() * 16, 4, 0.5f, new Color(32, 40, 49, 255).getRGB(), new Color(34, 42, 51, 255).getRGB());
        if (18 + (Client.INSTANCE.getModuleManager().getCategoryCheats(category).size() * 14) > 178) {
            if (mouseWithinBounds(mouseX, mouseY, posX, posY, largestString * 2 + valwidth, components.size() * 14)) {
                int wheel = Mouse.getDWheel();
                if (Mouse.hasWheel()) {
                    if (wheel > 0) {
                        parent.setModulescrollY(parent.getModulescrollY() - 4);
                    } else if (wheel < 0) {
                        if (parent.getModulescrollY() >= (Client.INSTANCE.getModuleManager().getCategoryCheats(category).size() * 15) - 162) {
                            parent.setModulescrollY((Client.INSTANCE.getModuleManager().getCategoryCheats(category).size() * 15) - 162);
                        } else {
                            parent.setModulescrollY(parent.getModulescrollY() + 4);
                        }
                    }
                    if (parent.getModulescrollY() < 0) {
                        parent.setModulescrollY(0);
                    }
                }
            }
        } else {
            if (parent.getModulescrollY() != 0) parent.setModulescrollY(0);
        }
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtil.prepareScissorBox(sr, posX, posY, width, 180);
        components.forEach(comp -> comp.drawScreen(mouseX, mouseY, partialTicks));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        components.forEach(comp -> comp.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        components.forEach(comp -> comp.mouseReleased(mouseX, mouseY, mouseButton));
    }

    public Module.Category getCategory() {
        return category;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void keyTyped(char typedChar, int key) {
    }

    public int getLargestString() {
        return this.largestString;
    }

    public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

    public Frame getParent() {
        return parent;
    }
}
