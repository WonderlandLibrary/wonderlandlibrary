package info.sigmaclient.sigma.gui.clickgui.simple;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.clickgui.buttons.Button;
import info.sigmaclient.sigma.config.values.*;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.JelloEaseAnim;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.clickgui.buttons.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static info.sigmaclient.sigma.modules.Module.mc;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class JelloModuleShower {
    Module module;
    public ArrayList<Button> buttons;
    public double scroll, maxScroll, scrollAnim, targetScrollAnim;
//    PartialTicksAnim scrollAnimUtil = new PartialTicksAnim(0);
    public boolean close = false;
//    public JelloEaseAnim anim = new JelloEaseAnim();
    public JelloModuleShower(Module module){
        this.module = module;
        this.buttons = new ArrayList<>();
        for(Value<?> value : module.values){
            if(value instanceof ModeValue){
                buttons.add(new ModeButton((ModeValue) value));
                this.maxScroll += 18;
            }
            if(value instanceof NumberValue){
                NumberButton bv = new NumberButton((NumberValue) value);
                bv.addY = -1;
                buttons.add(bv);
                this.maxScroll += 17;
            }
            if(value instanceof BooleanValue){
                BooleanButton bv = new BooleanButton((BooleanValue) value);
                bv.addY = -2;
                buttons.add(bv);
                this.maxScroll += 16;
            }
            if(value instanceof ColorValue){
                ColorButton c = new ColorButton((ColorValue) value);
                buttons.add(c);
                this.maxScroll += 18;
                this.maxScroll += (int) (183 / 2f) + 1 - 30;
            }
        }
        this.maxScroll = Math.max(maxScroll - 260, 0);
    }
    public JelloEaseAnim renderAnim(){
        // 动画
//        anim.anim(10, close);
        return null;
    }
    public void release(int x, int y, int mx, int my){
        ScaledResolution sr = new ScaledResolution(mc);
        int sx = sr.getScaledWidth() / 2 - 95;
        int sy = sr.getScaledHeight() / 2 - 140;
        int by = (int) (sy + 42 + scroll);
            for (Button button : buttons) {
                if (button.isHidden()) continue;
                button.release(sx - 15, by, mx, my);
                if (button.isClicked) {
                    button.isClicked = false;
                    return;
                }
                by += 18 + button.addY;
            }
    }
    public void animTick(int mx, int my) {
        ScaledResolution sr = new ScaledResolution(mc);
        int sy = sr.getScaledHeight() / 2 - 140;

        int sx = sr.getScaledWidth() / 2 - 100;
        // 组件
        int by = (int) (sy + 42 + scroll);
        for (Button button : buttons) {
            if (button.isHidden()) continue;
            button.animTick(sx - 15, by, mx, my);
            by += 18 + button.addY;
        }
    }
    // 返回动画
    public JelloEaseAnim drawPanel(int mx, int my, float pticks, float talpha, float scaled) {
        // 检查滚动
        scroll = Math.min(scroll, 0);
        scroll = Math.max(-maxScroll, scroll);

        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        // back
        RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, Math.min(talpha, 1) * 0.4f).getRGB());
//        if (!anim.isDone()) {
//            double scale = anim.getAlpha(); // 0 - 1
//            if(close) {
//                scale = 1 - (1 - scale * 0.2) + 0.8; // 1 - 0.8
//                // 1 - 0.8
//            }else{
//                scale = scale + 0.06; // 0.86 - 1.06
//                if(scale > 1.03){
//                    scale = 1.03 - (scale - 1.03);
//                }
//            }
        if (scaled != 1) {
        GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
        GlStateManager.scale(scaled, scaled, 0);
        GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
    }
//        }else{
//            if(close) {
//                GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
//                GlStateManager.scale(anim.getScale(), anim.getScale(), 0);
//                GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
//            }
//        }
        float alpha = talpha;
        alpha = Math.min(1, alpha);
        alpha = Math.max(0, alpha);
        int alphaInt = (int) (alpha * 255);

        int sx = sr.getScaledWidth() / 2 - 95;
        int sy = sr.getScaledHeight() / 2 - 140;

        // 基础
        RenderUtils.drawRoundedRect(sx - 30, sy, sx + 200 + 20, sy + 300, 5, new Color(253, 253, 252, alphaInt).getRGB());

        JelloFontUtil.jelloFontBold38.drawNoBSString(
                module.remapName,
                sx - 23 - 3 - 1, sy + 4 - 16 - 5 - 10 + 1,
                new Color(255, 255, 255, alphaInt).getRGB());
        JelloFontUtil.jelloFont20.drawNoBSString(
                module.describe,
                sx - 2 - 5 - 3 - 5, sy + 4 - 16 + 38 - 10 - 2 - 2 + 6,
                new Color(90, 90, 90, alphaInt).getRGB());
        alpha = Math.max(alpha, 0);
        alpha = Math.min(1, alpha);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(sx - 30, sy + 30, sx + 200 + 20, sy + 300, -1);
        StencilUtil.readStencilBuffer(1);
        // 开始画组件
        int by = (int) (sy + 42 + scroll);
        for(Button button : buttons){
            if(button.isHidden()) continue;
            button.drawButton(sx - 15, by, mx, my, pticks, alpha);
            by += 18 + button.addY;
        }
        StencilUtil.uninitStencilBuffer();
        by = (int) (sy + 42 + scroll);
        for(Button button : buttons){
            if(button.isHidden()) continue;
            button.markDraw(sx - 15, by, mx, my, pticks, alpha);
            by += 18 + button.addY;
        }

        // 右边显示条
//        if (targetScrollAnim > 0) {
//            float x = sx + 200 + 15;
//            float dy = sy + 295;
//            float y = sy + 5;
//            double cc = 10;
//            // 进入 / 离开 动画
//            if (!scrollAnimUtil.done) {
//                scrollAnimUtil.interpolate((float) targetScrollAnim, 0.1);
//                scrollAnim = Math.min(scrollAnimUtil.getValue() / cc, 1);
//                if (scrollAnimUtil.getValue() == (float) targetScrollAnim) {
//                    scrollAnimUtil.done = true;
//                }
//            } else {
//                scrollAnimUtil.interpolate(0f, 0.1);
//                scrollAnim = Math.min(scrollAnimUtil.getValue() / cc, 1);
//                if (scrollAnimUtil.getValue() == 0) {
//                    targetScrollAnim = 0;
//                    scrollAnimUtil.done = false;
//                }
//            }
//
//            float fullSize = (float) maxScroll;
//            float moduleRectSize = dy - y;
//            // 滚动条子
//            RenderUtils.drawRoundedRect(
//                    x,
//                    y,
//                    x + 3,
//                    dy,
//                    2.5F,
//                    new Color(0, 0, 0, (int) (50 * scrollAnim * alpha)).getRGB()
//            );
//            if (fullSize > moduleRectSize)
//                RenderUtils.drawRoundedRect(
//                        x,
//                        y - scroll,
//                        x + 3,
//                        dy + moduleRectSize - scroll
//                        , 2.5F
//                        , new Color(0, 0, 0, (int) (50 * scrollAnim * alpha)).getRGB());
//        }
        GL11.glPopMatrix();
        return null;
    }
    public void scrollMouse(int mx, int my, double delta){
        ScaledResolution sr = new ScaledResolution(mc);
        int sx = sr.getScaledWidth() / 2 - 95;
        int sy = sr.getScaledHeight() / 2 - 140;
        if (!ClickUtils.isClickable(sx - 30, sy, sx + 200 + 20, sy + 300, mx, my)) return;
        if(delta != 0){
            scroll += delta > 0 ? 20 : -20;
            targetScrollAnim = 20;
            // 滚动动画继续
        }
    }
    // 在退出module后的动画
    public void close(){
        close = true;
    }
    public boolean clickPanel(int mx, int my){
        ScaledResolution sr = new ScaledResolution(mc);
        int sx = sr.getScaledWidth() / 2 - 100;
        int sy = sr.getScaledHeight() / 2 - 140;

        // 组件
        int by = (int) (sy + 42 + scroll);
        if(ClickUtils.isClickable(sx - 30, sy, sx + 200 + 30, sy + 300, mx, my)) {
//            for (Button button : buttons) {
//                if (button.isHidden() || !(button instanceof ModeButton)) continue;
//                if(button.clickButton(sx - 15, by, mx, my)) break;
//                by += 18 + button.addY;
//            }
//            by = (int) (sy + 42 + scroll);
            for (Button button : buttons) {
                if (button.isHidden()) continue;
                if(button.clickButton(sx - 15, by, mx, my)) break;
                by += 18 + button.addY;
            }
            return true;
        }else{
            // 阻止
            by = (int) (sy + 42 + scroll);
            for (Button button : buttons) {
                if (button.isHidden()) continue;
                if(button.isCancelClick(sx - 15, by, mx, my))
                    return true;
                by += 18 + button.addY;
            }
        }
        return false;
    }
}
