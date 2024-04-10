package info.sigmaclient.sigma.gui.clickgui.buttons;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;

import top.fl0wowp4rty.phantomshield.annotations.Native;
import java.awt.*;

public class BooleanButton extends Button {
    public BooleanValue value;
    public PartialTicksAnim enableAnim;
    boolean click = false;
    public BooleanButton(BooleanValue value){
        this.value = value;
        enableAnim = new PartialTicksAnim(value.isEnable() ? 10 : 0);
    }

    @Override
    public void animTick(int x, int y, int mx, int my)
    {
        enableAnim.interpolate(value.isEnable() ? 10 : 0, 10.0);
        super.animTick(x, y, mx, my);
    }

    @Override
    public void drawButton(int x, int y, int mx, int my, float pticks, float alpha) {
        int intalpha = (int)(alpha * 255);
        int endX = x + 200 + 20;
        JelloFontUtil.jelloFont25.drawString(value.name, x, y + 1, new Color(0, 0, 0, intalpha).getRGB());
        if(!ClickUtils.isClickableWithRect(endX - 10 - 2, y - 1, 31 / 2f, 31 / 2f, mx, my)){
            click = false;
        }
        float drak = click ? 0.9f:1f;
        float perc = enableAnim.getValue() / 10f * alpha;
        drak *= alpha;
        RenderUtils.drawTextureLocation(endX - 10 - 2, y - 1, 31 / 2f, 31 / 2f, "clickgui/disable", new Color(drak,drak,drak,alpha));
        if(enableAnim.getValue() != 0) {
            RenderUtils.drawTextureLocation(endX - 10 - 2, y - 1, 31 / 2f, 31 / 2f, "clickgui/enable", new Color(drak, drak, drak, perc));
        }
        super.drawButton(x, y, mx, my, pticks, alpha);
    }
    public boolean isHidden(){
        return value.isHidden();
    }

    @Override
    public void release(int x, int y, int mx, int my) {
        int endX = x + 200 + 20;
        if(ClickUtils.isClickableWithRect(endX - 10 - 2, y - 1, 31 / 2f, 31 / 2f, mx, my) && click){
            value.setValue(!value.getValue());
            click = false;
        }
    }

    @Override
    public boolean clickButton(int x, int y, int mx, int my) {
        int endX = x + 200 + 20;
        if(ClickUtils.isClickableWithRect(endX - 10 - 2, y - 1, 31 / 2f, 31 / 2f, mx, my)){
            click = true;
            return true;
        }
        return super.clickButton(x, y, mx, my);
    }
}
