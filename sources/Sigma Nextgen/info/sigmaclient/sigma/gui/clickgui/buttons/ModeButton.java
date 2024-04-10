package info.sigmaclient.sigma.gui.clickgui.buttons;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ModeButton extends Button {
    public ModeValue value;
    PartialTicksAnim downAnim;
    boolean click, dragg;
    public ModeButton(ModeValue value){
        downAnim = new PartialTicksAnim(0);
        this.value = value;
        for(int i = 0 ; i < this.value.values.length; i++){
            this.value.hoverAnims[i] = new PartialTicksAnim(0);
        }
    }

    @Override
    public void animTick(int x, int y, int mx, int my) {
        int index = 0;
        float yd = y + 14;
        int endX = x + 220;
        float width = 14;
        for (String type : value.values) {
            if (ClickUtils.isClickable(endX - 10 - 2 - 40 - 6 - 5 + 2, yd + 1, endX - 10 - 2 + 7 + 5 - 2, yd + width + 1, mx, my)) {
                value.hoverAnims[index].interpolate(10, 5);
            } else {
                value.hoverAnims[index].interpolate(0, 5);
            }
            yd += width;
            index++;
        }
        downAnim.interpolate(dragg ? 10 : 0, 5);
        super.animTick(x, y, mx, my);
    }

    @Override
    public void drawButton(int x, int y, int mx, int my, float pticks, float alpha) {
        int intalpha = (int)(alpha * 255);
        int endX = x + 220;
        JelloFontUtil.jelloFont25.drawNoBSString(value.name, x, y, new Color(0, 0, 0, intalpha).getRGB());
        boolean hover_ = ClickUtils.isClickable(endX - 10 - 2 - 40 - 6,y+4,endX - 10 - 2 + 7,y + 14, mx, my);
        if(downAnim.getValue() == 0) {
            JelloFontUtil.jelloFont18.drawNoBSString(value.getValue(), endX - 10 - 2 - 40 - 5, y + 5, new Color(98,98,98, intalpha).getRGB());
            JelloFontUtil.jelloFont18.drawNoBSString(">", endX - 10 - 2 - 5 + 6, y + 4,
                (hover_ ? new Color(130,130,130, intalpha) : new Color(174,174,174, intalpha)).getRGB());
        }
        if(!hover_){
            click = false;
        }
        float perc = downAnim.getValue() / 10;
        int size = value.values.length;
        float width = 14;
        if(!ClickUtils.isClickable(endX - 10 - 2 - 40 - 6,
                y+4,endX - 10 - 2 + 7,
                y + 14 + perc * (size) * width, mx ,my)){
            dragg = false;
        }
        super.drawButton(x, y, mx, my, pticks, alpha);
    }

    @Override
    public void markDraw(int x, int y, int mx, int my, float pticks, float alpha) {
        int intalpha = (int)(alpha * 255);
        int endX = x + 220;
        float perc = downAnim.getValue() / 10;
        int size = value.values.length;
        float width = 14;
        if(downAnim.getValue() != 0) {
            RenderUtils.sigma_drawShadow(endX - 10 - 2 - 40 - 6 - 3,
                    y + 2 - 1,
                    endX - 10 - 2 + 7 + 3 - (endX - 10 - 2 - 40 - 6 - 3),
                    y + 14 + perc * (size) * width + 1 - (y + 2 - 1),5
                    , perc * 0.75f);
            RenderUtils.drawRect(endX - 10 - 2 - 40 - 6 - 3,
                    y + 2 - 1, endX - 10 - 2 + 7 + 3,
                    y + 14 + perc * (size) * width + 1, new Color(255, 255, 255, intalpha).getRGB());
            //
            JelloFontUtil.jelloFont18.drawNoBSString(value.getValue(), endX - 10 - 2 - 40 - 5, y + 5, new Color(98, 98, 98, intalpha).getRGB());
            boolean hover_ = true;
            float xOff = 4, yOff = 3;
            // 旋转
            GL11.glPushMatrix();
            GL11.glTranslated(endX - 10 - 2 - 5 + 6 + xOff - 1, y + 4 + yOff + 1,0);
            GL11.glRotated(perc * 90, 0, 0, 1);
            GL11.glTranslated(-(endX - 10 - 2 - 5 + 6 + xOff - 1), -(y + 4 + yOff + 1),0);
            JelloFontUtil.jelloFont18.drawNoBSString(">", endX - 10 - 2 - 5 + 6, y + 4,
                    new Color(130,130,130, intalpha).getRGB());
            GL11.glPopMatrix();

            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect(endX - 10 - 2 - 40 - 6 - 5,
                    y + 2 - 1, endX - 10 - 2 + 7 + 5,
                    y + 14 + perc * (size) * width + 1, -1);
            StencilUtil.readStencilBuffer(1);
            float yd = y + 14;
            int index = 0;
            for (String type : value.values) {
                GlStateManager.resetColor();
                RenderUtils.drawRect(endX - 10 - 2 - 40 - 6 - 5 + 2, yd + 1, endX - 10 - 2 + 7 + 5 - 2, yd + width + 1,
                        new Color(0,0,0,(int)(alpha * 24 * (value.hoverAnims[index].getValue() / 10f))).getRGB());
                GlStateManager.resetColor();
                JelloFontUtil.jelloFont18.drawNoBSString(type, endX - 10 - 2 - 40 - 5, yd + 4.5f, new Color(0, 0, 0, intalpha).getRGB());
                yd += width;
                index++;
            }
            StencilUtil.uninitStencilBuffer();
        }
        super.markDraw(x, y, mx, my, pticks, alpha);
    }
    @Override
    public void release(int x, int y, int mx, int my) {
        int endX = x + 220;
        boolean hover_ = ClickUtils.isClickable(endX - 10 - 2 - 40 - 6,y+4,endX - 10 - 2 + 7,y + 14, mx, my);
        if(click && hover_){
            dragg = !dragg;
            click = false;
        }
        if(dragg){
            int size = value.values.length;
            float width = 14;
            float yd = y + 14;
            for (String type : value.values) {
                if(ClickUtils.isClickable(endX - 10 - 2 - 40 - 6 - 5 + 2, yd + 1, endX - 10 - 2 + 7 + 5 - 2, yd + width + 1, mx, my )){
                    dragg = false;
                    click = false;
                    value.setValue(type);
                    clicked();
                    return;
                }
                yd += width;
            }
        }
        super.release(x, y, mx, my);
    }

    public boolean isHidden(){
        return value.isHidden();
    }
    public boolean isCancelClick(int x, int y, int mx, int my){
        int endX = x + 220;
        if(dragg) {
            float width = 14;
            float yd = y + 14;
            for (String type : value.values) {
                if (ClickUtils.isClickable(endX - 10 - 2 - 40 - 6 - 5 + 2, yd + 1, endX - 10 - 2 + 7 + 5 - 2, yd + width + 1, mx, my)) {
                    return true;
                }
                yd += width;
            }
        }
        return false;
    }
    @Override
    public boolean clickButton(int x, int y, int mx, int my) {
        int endX = x + 220;
        boolean hover_ = ClickUtils.isClickable(endX - 10 - 2 - 40 - 6,y+4,endX - 10 - 2 + 7,y + 14, mx, my);
        if(hover_){
            click = true;
            return true;
        }
        return super.clickButton(x, y, mx, my);
    }
}
