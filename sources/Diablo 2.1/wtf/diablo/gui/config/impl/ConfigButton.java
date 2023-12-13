package wtf.diablo.gui.config.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.diablo.Diablo;
import wtf.diablo.config.Config;
import wtf.diablo.gui.config.ConfigMenu;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

public class ConfigButton {
    public double width;
    public double height;
    public Config config;
    public ConfigMenu parent;

    public ConfigButton(ConfigMenu parent, Config config, double width, double height){
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.config = config;
    }
    double x;
    double y;
    public double drawButton(double x, double y,int mouseX, int mouseY){
        this.x = x;
        this.y = y;
        height = 22;
        Gui.drawRect(x, y, x + width, y + height, 0xFF313131);

        if(RenderUtil.isHovered(mouseX,mouseY,x,y,x + width - 15, y+ height)){
            Gui.drawRect(x, y, x + width, y + height, 0xFF2E2E2E);
        }

        //Gui.drawRect(x + width - 15, y + 1, x + width, y + height - 1, 0xFF212121);


        Fonts.SFReg24.drawStringWithShadow(config.getName(), x + 5,y + (height / 2) - Fonts.SFReg24.getHeight() / 2f,-1);


        //RenderUtil.drawRoundedRectangle(x + width - 12, y + 0.5, x + width - 12 + 9, y + 10, 6,0xFF252525);

        if(RenderUtil.isHovered(mouseX,mouseY, x + width - 12, y + 1, x + width - 12 + 9, y + 10))
            GL11.glColor4d(1,1,1,1);
        else
            GL11.glColor4d(0.5,0.5,0.5,1);

        RenderUtil.drawImage((int) (x + width - 12), (int) y + 1, 9, 9,new ResourceLocation("diablo/icons/save.png"));



        //RenderUtil.drawRoundedRectangle(x + width - 12, y + height - 10, x + width - 12 + 9, y + height - 1, 6,0xFF151515);

        if(RenderUtil.isHovered(mouseX,mouseY, x + width - 12, y + height - 10, x + width - 12 + 9, y + height - 1))
            GL11.glColor4d(1,1,1,1);
        else
            GL11.glColor4d(0.5,0.5,0.5,1);

        RenderUtil.drawImage((int) (x + width - 12), (int) ((int) y + height - 10), 9, 9,new ResourceLocation("diablo/icons/trash.png"));


        return height + 2;
    }

    public boolean buttonClick(int mouseX, int mouseY, int state){
        if(state == 0){
            if(RenderUtil.isHovered(mouseX,mouseY,x,y, x+ width - 15,y+height)){

                Diablo.configManager.loadConfig(config.getName(), parent.keepKeybinds);
            }

            if(RenderUtil.isHovered(mouseX,mouseY, x + width - 12, y + height - 10, x + width - 12 + 9, y + height - 1)) {
                Diablo.configManager.removeConfig(config.getName());

                parent.reloadConfigs();
            }else if(RenderUtil.isHovered(mouseX,mouseY,x + width - 12, y + 1, x + width - 12 + 9, y + 10)){
                Diablo.configManager.saveConfig(config.getName());

                parent.reloadConfigs();
            }
        }
        return false;
    }
}
