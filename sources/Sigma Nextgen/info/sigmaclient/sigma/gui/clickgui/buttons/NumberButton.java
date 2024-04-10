package info.sigmaclient.sigma.gui.clickgui.buttons;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;

import java.awt.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NumberButton extends Button {
    public NumberValue value;
    boolean dragging = false;
    PartialTicksAnim animationUtils;
    PartialTicksAnim hoverAnim;
    public NumberButton(NumberValue value){
        hoverAnim = new PartialTicksAnim(0);
        animationUtils = new PartialTicksAnim((int) (54 * (value.getValue().floatValue() / (value.max.floatValue() - value.min.floatValue()))));
        this.value = value;
    }

    @Override
    public void animTick(int x, int y, int mx, int my) {
        float width = 54;
        int endX = x + 220;
        int cx = (int) (width * ((value.getValue().floatValue() - value.min.floatValue()) / (value.max.floatValue() - value.min.floatValue())));
        animationUtils.interpolate(cx, 20f);

        if(ClickUtils.isClickable(endX - 57 - 1, y + 5 - 4, endX - 3 + 1, y + 8 + 4, mx, my) || dragging) {
            hoverAnim.interpolate(10, 7.0);
        }else{
            hoverAnim.interpolate(0, 7.0);
        }
        hoverAnim.interpolate(0, 0);
        super.animTick(x, y, mx, my);
    }

    public boolean isHidden(){
        return value.isHidden();
    }
    @Override
    public void drawButton(int x, int y, int mx, int my, float pticks, float alpha) {
        int intalpha = (int)(alpha * 255);
        JelloFontUtil.jelloFont25.drawString(value.name, x, y, new Color(0, 0, 0, intalpha).getRGB());
        float width = 54;
        int endX = x + 220;
        int dx = endX - 57;
        int tx = endX - 3;

        RenderUtils.drawRoundedRect(
                endX - 57, y + 5, endX - 3, y + 8,
                1f, new Color(215,234,254, intalpha).getRGB());

        RenderUtils.drawRoundedRect(
                endX - 57, y + 5, dx + animationUtils.getValue(), y + 8,
                1f, new Color(59,153,253, intalpha).getRGB());


        float aa = hoverAnim.getValue() / 10;
        String ss = value.inc == NumberValue.NUMBER_TYPE.INT ? String.format("%.0f", value.getValue().floatValue()) :
                (value.inc == NumberValue.NUMBER_TYPE.FLOAT ? String.format("%.2f", value.getValue().floatValue()) : String.format("%.1f", value.getValue().floatValue()));
        JelloFontUtil.jelloFont14.drawString(ss, endX - 57 - 1 - 3
                 - JelloFontUtil.jelloFont14.getStringWidth(ss) - 4 /* pad to make it readable (when 0, the circle hides it)*/, y + 5,
                new Color(130,130,130,(int)(aa * 255 * alpha)).getRGB());
        RenderUtils.drawTexture(dx + 5 + animationUtils.getValue() - 16F - 1, y + 5.5f - 12.5F + 1 , 25 , 25,
                "playermodelshadow",(dragging ? 0.5F : 0.2F) * alpha);
        RenderUtils.drawFilledCircleNoGL(dx + animationUtils.getValue(), y + 6, 6, new Color(255, 255, 255, intalpha).getRGB(), 2);

        if(dragging){
            float pp = (mx - dx) / width;
            pp = Math.max(0, pp);
            pp = Math.min(1, pp);
            float mmx = value.min.floatValue() + pp * (value.max.floatValue() - value.min.floatValue());
            if(value.inc == NumberValue.NUMBER_TYPE.INT)
                mmx = (int) mmx;
            else if(value.inc == NumberValue.NUMBER_TYPE.FLOAT)
                mmx = (float)(Math.round(mmx*100))/100;
            else mmx = (float)(Math.round(mmx*10))/10;
            value.setValue(mmx);
        }
        super.drawButton(x, y, mx, my, pticks, alpha);
    }

    @Override
    public void release(int x, int y, int mx, int my) {
        dragging = false;
        super.release(x, y, mx, my);
    }

    @Override
    public boolean clickButton(int x, int y, int mx, int my) {
        float width = 54;
        int endX = x + 220;
        int dx = endX - 57;
        int tx = endX - 3;
        if(ClickUtils.isClickable(endX - 57 - 1, y + 5 - 4, endX - 3 + 1, y + 8 + 4, mx, my)){
            dragging = true;
            return true;
        }
        return super.clickButton(x, y, mx, my);
    }
}
