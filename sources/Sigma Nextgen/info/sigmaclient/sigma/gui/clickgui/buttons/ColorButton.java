package info.sigmaclient.sigma.gui.clickgui.buttons;

import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;

import java.awt.*;

import static info.sigmaclient.sigma.sigma5.utils.SigmaRenderUtils.牰蓳躚唟捉璧;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ColorButton extends Button {
    public ColorValue value;
    boolean dragging = false;
    boolean dragging1 = false;
    public ColorButton(ColorValue value){
        this.value = value;
        addY = (int) (183 / 2f) + 1 - 30;
    }
    public static float[] rgbToHsv(int[] rgb) {
        //切割rgb数组
        int R = rgb[0];
        int G = rgb[1];
        int B = rgb[2];
        //公式运算 /255
        float R_1 = R / 255f;
        float G_1 = G / 255f;
        float B_1 = B / 255f;
        //重新拼接运算用数组
        float[] all = {R_1, G_1, B_1};
        float max = all[0];
        float min = all[0];
        //循环查找最大值和最小值
        for (float v : all) {
            if (max <= v) {
                max = v;
            }
            if (min >= v) {
                min = v;
            }
        }
        float C_max = max;
        float C_min = min;
        //计算差值
        float diff = C_max - C_min;
        float hue = 0f;
        //判断情况计算色调H
        if (diff == 0f) {
            hue = 0f;
        } else {
            if (C_max == R_1) {
                hue = (((G_1 - B_1) / diff) % 6) * 60f;
            }
            if (C_max == G_1) {
                hue = (((B_1 - R_1) / diff) + 2f) * 60f;
            }
            if (C_max == B_1) {
                hue = (((R_1 - G_1) / diff) + 4f) * 60f;
            }
        }
        //计算饱和度S
        float saturation;
        if (C_max == 0f) {
            saturation = 0f;
        } else {
            saturation = diff / C_max;
        }
        //计算明度V
        return new float[]{hue, saturation, C_max};
    }

    @Override
    public void drawButton(int x, int y, int mx, int my, float pticks, float alpha) {
        int intalpha = (int)(alpha * 255);
        int offX = 100;
        JelloFontUtil.jelloFont25.drawString(value.name, x, y + 1, new Color(0, 0, 0, intalpha).getRGB());

        RenderUtils.drawRect(x + 10 + offX + 16 - 0.5f, y + 1 - 0.5f, x + 10 + offX + 16 + 183 / 2f + 0.5f, y + 1 + 77 / 2f + 0.5f, new Color(0.3f, 0.3f, 0.3f, alpha * 0.7f).getRGB());
        RenderUtils.drawTextureLocation(x + 10 + offX + 16, y + 1, 183 / 2f, 77 / 2f,
                "clickgui/color",
                new Color(1,1,1,alpha));

        RenderUtils.drawRect(x + 10 + offX + 16 - 0.5f, y + 1 + 77 / 2f + 4 - 0.5f, x + 10 + offX + 16 + 100 / 2f + 0.5f, y + 1 + 77 / 2f + 4 + 23 / 2f + 0.5f, new Color(0.3f, 0.3f, 0.3f, alpha * 0.7f).getRGB());
        RenderUtils.drawTextureLocation(x + 10 + offX + 16, y + 1 + 77 / 2f + 4, 100 / 2f, 23 / 2f,
                "clickgui/color2",
                new Color(1,1,1,alpha));

        牰蓳躚唟捉璧(x + 10 + 183 / 2f + offX + 5, y + 1 + 48, 20,ColorUtils.reAlpha(new Color(0.3f, 0.3f, 0.3f), alpha * 0.7f).getRGB());
        牰蓳躚唟捉璧(x + 10 + 183 / 2f + offX + 5, y + 1 + 48, 19,ColorUtils.reAlpha(value.getValue(), alpha).getRGB());
        float[] rgbToHsv = rgbToHsv(new int[]{value.getColor().getRed(), value.getColor().getGreen(), value.getColor().getBlue()});
        if(x != 0 && y != 0) {
            if (dragging) {
                float cx = (mx - (x + 10 + offX + 16)) / (183 / 2f);
                cx = Math.min(1, cx);
                cx = Math.max(0, cx);
                float cy = (my - (y + 1)) / (77 / 2f);
                cy = Math.min(1, cy);
                cy = Math.max(0, cy);
                rgbToHsv[0] = cx;
                rgbToHsv[1] = 1 - cy;
                value.setValue(Color.HSBtoRGB(rgbToHsv[0], rgbToHsv[1], rgbToHsv[2]));
            } else {
                if (dragging1) {
                    float cx = (mx - (x + 10 + offX + 16)) / (100 / 2f);
                    cx = Math.min(1, cx);
                    cx = Math.max(0, cx);
                    rgbToHsv[2] = 1 - cx;
                    value.setValue(Color.HSBtoRGB(rgbToHsv[0], rgbToHsv[1], rgbToHsv[2]));
                }
            }
        }
        float cx = rgbToHsv[0] / 360f * (183 / 2f);
        float cy = (1 - rgbToHsv[1]) * (77 / 2f);
        牰蓳躚唟捉璧(x + 10 + offX + 16 + cx,- y + 1 + cy, 20,ColorUtils.reAlpha(new Color(0.1f, 0.1f, 0.1f), alpha * 0.7f).getRGB());
        牰蓳躚唟捉璧(x + 10 + offX + 16 + cx, y + 1 + cy, 19,ColorUtils.reAlpha(new Color(0.7f, 0.7f, 0.7f), alpha).getRGB());

        addY = (int) (183 / 2f) + 1 - 30;
        super.drawButton(x, y, mx, my, pticks, alpha);
    }
    public boolean isHidden(){
        return value.isHidden();
    }

    @Override
    public void release(int x, int y, int mx, int my) {
        dragging = false;
        dragging1 = false;
    }

    @Override
    public boolean clickButton(int x, int y, int mx, int my) {
        if(ClickUtils.isClickableWithRect(x + 10 + 100 + 16, y + 1, 183 / 2f, 77 / 2f, mx, my)){
            dragging = !dragging;
            return true;
        }
        if(ClickUtils.isClickableWithRect(x + 10 + 100 + 16, y + 1 + 77 / 2f + 4, 100 / 2f, 23 / 2f, mx, my)){
            dragging1 = !dragging1;
            return true;
        }
        return super.clickButton(x, y, mx, my);
    }
}
