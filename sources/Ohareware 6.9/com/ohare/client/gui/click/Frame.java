package com.ohare.client.gui.click;

import com.ohare.client.gui.click.tab.Tab;
import com.ohare.client.module.Module;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Frame {
    private final String label;
    private int posX, posY, lastPosX, lastPosY, width, height;
    private boolean dragging, extended;
    private Module extendedmod;
    private Module.Category selectedcategory = Module.Category.COMBAT;
    private Tab tab;
    private int valuescrollY;
    private int modulescrollY;
    private int enumscrollY;
    private TimerUtil timer = new TimerUtil();
    private EnumValue extendedenum;
    private boolean enumextended;

    public Frame(String label, int posX, int posY, int width, int height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtil.prepareScissorBox(sr, posX, posY, width , height);

        GL11.glColor4f(1, 1, 1, 1);
        if (dragging) {
            this.posX = mouseX + this.lastPosX;
            this.posY = mouseY + this.lastPosY;
            // boundingbox
            if (posX < 0) posX = 0;
            if (posX + width > sr.getScaledWidth()) posX = sr.getScaledWidth() - width;
            if (posY < 0) posY = 0;
            if (posY + height > sr.getScaledHeight()) posY = sr.getScaledHeight() - height;
            //
        }
        RenderUtil.drawRoundedRect(posX, posY, width, height, 4, new Color(38, 46, 52, 255).getRGB());
        RenderUtil.drawBorderedRoundedRect(posX, posY, width, 20, 3, 0.5f, new Color(26, 31, 41, 255).getRGB(), new Color(31, 38, 48, 255).getRGB());
        Fonts.clickfont.drawStringWithShadow(StringUtils.stripControlCodes("oHareWare"), posX + width - 2 - Fonts.clickfont.getStringWidth("oHareWare"), posY + height - 7, -1);
        int catx = posX + 4;
        for (Module.Category category : Module.Category.values()) {
            RenderUtil.drawRoundedRect(catx, posY + 4, 20, 12, 3, selectedcategory == category ? (mouseWithinBounds(mouseX,mouseY,catx, posY + 4, 20, 12)? new Color(0, 130, 240, 255).getRGB() : new Color(0, 107, 214, 255).getRGB()) : (mouseWithinBounds(mouseX,mouseY,catx, posY + 4, 20, 12)? new Color(0x505760).getRGB() : new Color(48, 56, 66, 255).getRGB()));
            Fonts.iconfont.drawString(getFontChar(category), catx + 4.5f, posY + 7, -1);
            if (selectedcategory == category) {
                tab = new Tab(this, category, posX + 4, posY + 30, 320, 100);
                tab.drawScreen(mouseX, mouseY, partialTicks);
            }
            catx += 24;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (mouseWithinBounds(mouseX, mouseY, posX, posY, width, 20)) {
                dragging = true;
                this.lastPosX = (posX - mouseX);
                this.lastPosY = (posY - mouseY);
            }
            int catx = posX + 4;
            for (Module.Category category : Module.Category.values()) {
                if (mouseWithinBounds(mouseX, mouseY, catx, posY + 4, 20, 12)) {
                    selectedcategory = category;
                    setExtended(false);
                    setExtendedmod(null);
                    dragging = false;
                    setValuescrollY(0);
                    setModulescrollY(0);
                    setEnumscrollY(0);
                }
                if (selectedcategory == category) {
                    tab.mouseClicked(mouseX, mouseY, mouseButton);
                }
                catx += 24;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && dragging) {
            dragging = false;
        }
        for (Module.Category category : Module.Category.values()) {
            if (selectedcategory == category) {
                tab.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void keyTyped(char typedChar, int key) {
    }

    public String getLabel() {
        return label;
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


    public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

    public String getFontChar(Module.Category cat) {
        switch (cat) {
            case COMBAT: {
                return "a";
            }
            case VISUALS: {
                return "f";
            }
            case MOVEMENT: {
                return "c";
            }
            case OTHER: {
                return "d";
            }
            case PLAYER: {
                return "e";
            }
            case EXPLOITS: {
                return "b";
            }
        }
        return "NONE";
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public Module getExtendedmod() {
        return extendedmod;
    }

    public void setExtendedmod(Module extendedmod) {
        this.extendedmod = extendedmod;
    }

    public int getValuescrollY() {
        return valuescrollY;
    }

    public void setValuescrollY(int valuescrollY) {
        this.valuescrollY = valuescrollY;
    }

    public TimerUtil getTimer() {
        return timer;
    }

    public boolean isEnumextended() {
        return enumextended;
    }

    public void setEnumextended(boolean enumextended) {
        this.enumextended = enumextended;
    }

    public EnumValue getExtendedenum() {
        return extendedenum;
    }

    public void setExtendedenum(EnumValue extendedenum) {
        this.extendedenum = extendedenum;
    }

    public int getModulescrollY() {
        return modulescrollY;
    }

    public void setModulescrollY(int modulescrollY) {
        this.modulescrollY = modulescrollY;
    }

    public int getEnumscrollY() {
        return enumscrollY;
    }

    public void setEnumscrollY(int enumscrollY) {
        this.enumscrollY = enumscrollY;
    }
}