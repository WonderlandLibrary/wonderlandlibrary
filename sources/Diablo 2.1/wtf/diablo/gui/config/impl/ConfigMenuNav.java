package wtf.diablo.gui.config.impl;

import net.minecraft.client.gui.Gui;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

public class ConfigMenuNav {
    public double width;
    public double height;
    public String displayName = "";
    public int buttonId;

    public ConfigMenuNav(int buttonId, String displayName,double width, double height){
        this.width = width;
        this.height = height;
        this.buttonId = buttonId;
        this.displayName = displayName;
    }

    public ConfigMenuNav(int buttonId,double width, double height){
        this.width = width;
        this.height = height;
        this.buttonId = buttonId;
    }

    double x;
    double y;
    public void drawButton(double x, double y,int mouseX, int mouseY, String displayName, boolean selected){
        this.x = x;
        this.y = y;
        Gui.drawRect(x, y, x + width, y + height, 0xFF212121);

        if(RenderUtil.isHovered(mouseX,mouseY,x,y,x + width, y+ height)){
            Gui.drawRect(x, y, x + width, y + height, 0xFF1E1E1E);
        }
        if(selected){
            Gui.drawRect(x, y, x + width, y + height, 0xFF1B1B1B);
        }
        Fonts.SFReg24.drawStringWithShadow(displayName, x + (width / 2) - Fonts.SFReg24.getStringWidth(displayName)/2f,y + (height / 2) - Fonts.SFReg24.getHeight() / 2f,-1);
    }

    public void drawButton(double x, double y,int mouseX, int mouseY, boolean selected){
        drawButton(x,y,mouseX,mouseY,displayName,selected);
    }

    public boolean buttonClick(int mouseX, int mouseY, int state){
        if(state == 0){
            if(RenderUtil.isHovered(mouseX,mouseY,x,y, x+ width,y+height)){
                return true;
            }
        }
        return false;
    }
}
