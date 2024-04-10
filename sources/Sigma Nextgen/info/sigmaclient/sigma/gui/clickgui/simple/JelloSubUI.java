package info.sigmaclient.sigma.gui.clickgui.simple;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.clickgui.JelloClickGui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.naturalOrder;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class JelloSubUI {
    public
    double x = 100;
    public
    double y = 20;
    double lastClickX, lastClickY, startX, startY;
    public double scroll, maxScroll, scrollAnim, targetScrollAnim;
    boolean dragging = false;
    PartialTicksAnim scrollAnimUtil = new PartialTicksAnim(0);
    List<Module> modules;
    public String title;
    public JelloSubUI(List<Module> modules, Category category){
        scroll = 0;
        this.modules = new ArrayList<>();
        List<String> autoSort = new ArrayList<>();

        // 最简...
        for(Module module : modules)
            autoSort.add(module.remapName);
        autoSort.sort(naturalOrder());
        for(String name : autoSort)
            for(Module module : modules)
                if(module.remapName.equals(name))
                    this.modules.add(module);

        this.title = category.name();
        this.maxScroll = Math.max(modules.size() * 15 - 160f + 31.5f, 0);
    }
    public void scrollMouse(int mx, int my, double delta){
        if (!ClickUtils.isClickable(x,y + 31.5F,x + 100,y + 160f, mx, my)) return;
        if(delta != 0){
            scroll += delta > 0 ? 20 : -20;
            targetScrollAnim = 10;
            // 滚动动画继续
            if(scrollAnimUtil.done){
                scrollAnimUtil.done = false;
            }
        }
    }
    public void ticks(){
        if (targetScrollAnim > 0) {
            // 进入 / 离开 动画
            if (!scrollAnimUtil.done) {
                scrollAnimUtil.interpolate((float) targetScrollAnim, 1);
                if (scrollAnimUtil.getValue() == (float) targetScrollAnim) {
                    scrollAnimUtil.done = true;
                }
            } else {
                scrollAnimUtil.interpolate(0f, 1);
                if (scrollAnimUtil.getValue() == 0) {
                    targetScrollAnim = 0;
                    scrollAnimUtil.done = false;
                }
            }
        }
    }
    public void render(int mouseX, int mouseY, float pTicks, float alpha, float scale) {
        // 拖动
        if (dragging) {
            x = lastClickX + mouseX - startX;
            y = lastClickY + mouseY - startY;
        }
        // 阴影
        RenderUtils.drawShadow(x, y, x + 100, y + 160, alpha * 0.6f);
        // 检查滚动
        scroll = Math.min(scroll, 0);
        scroll = Math.max(-maxScroll, scroll);
        // 绘制模块类型名
        RenderUtils.drawRect(x, y, x + 100, y + 31.5F, new Color(255, 255, 255, (int) (230 * alpha)).getRGB());
        RenderUtils.drawRect(x, y + 31.5F, x + 100, y + 160, new Color(250, 250, 250, (int) (255 * alpha)).getRGB());

        if(scale != 1) {
            JelloFontUtil.jelloFont25.drawSmoothString(title, (float) (x + 10), (float) (y + 13), new Color(100, 100, 100, (int) (235 * alpha)).getRGB());
        }else{
            JelloFontUtil.jelloFont25.drawNoBSString(title, (float) (x + 10), (float) (y + 13), new Color(100, 100, 100, (int) (235 * alpha)).getRGB());
        }
        // 所有模块
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y + 31.5F, x + 100, y + 160, -1);
        StencilUtil.readStencilBuffer(1);
        // 偏移Y
        float yd = 31.5F;
        double offsetY = scroll;
        for (Module module : modules) {
            // Hover 动画
            if (module.enabled) {
                RenderUtils.drawRect(x, y + yd + offsetY, x + 100, y + yd + 15 + offsetY, new Color(41, 166, 255, (int) (255 * alpha)).getRGB());
                RenderUtils.drawRect(x, y + yd + offsetY, x + 100, y + yd + 15 + offsetY, new Color(41, 184, 255, (int) (module.hoverAnimation * alpha)).getRGB());
            } else {
                RenderUtils.drawRect(x, y + yd + offsetY, x + 100, y + yd + 15 + offsetY, new Color(250, 250, 250, (int) (255 * alpha)).getRGB());
                RenderUtils.drawRect(x, y + yd + offsetY, x + 100, y + yd + 15 + offsetY, new Color(231, 231, 231, (int) (module.hoverAnimation * alpha)).getRGB());
            }
            if (ClickUtils.isClickable(x, y + yd + offsetY, x + 100, y + yd + 15 + offsetY, mouseX, mouseY) && JelloClickGui.currentModule == null) {
                module.hoverAnimationUtil.interpolate(255, 2);
                module.hoverAnimation = (int) module.hoverAnimationUtil.getValue();
            } else {
                module.hoverAnimationUtil.interpolate(0, 2);
                module.hoverAnimation = (int) module.hoverAnimationUtil.getValue();
            }
            if (module.enabled) {
                if(scale != 1){
                    JelloFontUtil.jelloFont20.drawNoBSString(module.remapName, (float) (x + 10 + 5 + 2), (float) (y + yd + 4 + offsetY), new Color(255, 255, 255, (int) (255 * alpha)).getRGB());
                }else {
                    JelloFontUtil.jelloFont20.drawNoBSString(module.remapName, (float) (x + 10 + 5 + 2), (float) (y + yd + 4 + offsetY), new Color(255, 255, 255, (int) (255 * alpha)).getRGB());
                }
            } else {
                if(scale != 1) {
                    JelloFontUtil.jelloFont20.drawSmoothString(module.remapName, (float) (x + 10 + 2), (float) (y + yd + 4 + offsetY), new Color(0, 0, 0, (int) (255 * alpha)).getRGB());
                }else{
                    JelloFontUtil.jelloFont20.drawNoBSString(module.remapName, (float) (x + 10 + 2), (float) (y + yd + 4 + offsetY), new Color(0, 0, 0, (int) (255 * alpha)).getRGB());
                }
            }
            yd += 15f;
        }
        StencilUtil.uninitStencilBuffer();
        // 下滑的阴影？
        if (offsetY != 0) {
            RenderUtils.drawCustomShader(x, y + 31.5F, x + 100, 0, alpha * 0.7f);
        }
//        RenderUtils.drawCustomShader(x, y + 160, x + 100, 2, alpha * 0.4f);
//        RenderUtils.drawCustomShader(x, y, x + 100, 0, alpha * 0.4f);
//        RenderUtils.drawOutInShader(x, y, x + 100, y + 160, true, alpha * 0.4f);
//        RenderUtils.drawOutInShader(x, y, x + 100, y + 160, false, alpha * 0.4f);
        // 右边显示条
        if (targetScrollAnim > 0) {
            double cc = 10;
            // 进入 / 离开 动画
            if (!scrollAnimUtil.done) {
                scrollAnim = Math.min(scrollAnimUtil.getValue() / cc, 1);
            } else {
                scrollAnim = Math.min(scrollAnimUtil.getValue() / cc, 1);
            }

            float fullSize = modules.size() * 15F;
            float moduleRectSize = 160 - 31.5F;
            float percentSize = moduleRectSize / fullSize;
            // 滚动条子
            RenderUtils.drawRoundedRect(
                    x + 93,
                    y + 31.5F,
                    x + 98,
                    y + 160,
                    2.5F,
                    new Color(0, 0, 0, (int) (50 * scrollAnim)).getRGB()
            );
            if (fullSize > moduleRectSize)
                RenderUtils.drawRoundedRect(
                        x + 93,
                        y + 30 - offsetY * percentSize,
                        x + 98,
                        y + 31.5F + moduleRectSize * percentSize - percentSize * offsetY
                        , 2.5F
                        , new Color(0, 0, 0, (int) (50 * scrollAnim)).getRGB());
        }
        GlStateManager.resetColor();
    }
    public void release(double mouseX, double mouseY, int mouseButton){
        float yd = 31.5F;
        double offsetY = scroll;
        dragging = false;
        if(ClickUtils.isClickable(x, y + 31.5F, x + 100, y + 160, mouseX, mouseY)) {
            for (Module module : modules) {
                // Hover 动画
                if (ClickUtils.isClickable(x, y + yd + offsetY, x + 100, y + yd + 15 + offsetY, mouseX, mouseY)) {
                    if (module.hove) {
                        // 切换
                        if (mouseButton == 0) {
                            module.toggle();
                            break;
                        } else if (mouseButton == 1) {
                            JelloClickGui.currentModule = new JelloModuleShower(module);
                            break;
                        }
                    }
                }
                yd += 15f;
            }
        }
        for (Module module : modules) {
            module.hove = false;
        }
    }
    public boolean clicked(int mouseX, int mouseY, int mouseButton){
        if(!ClickUtils.isClickable(x, y, x + 100, y + 160, mouseX, mouseY)) return false;
        // 拖我
        if(mouseButton == 0 && ClickUtils.isClickable(x,y,x + 100,y + 31.5F, mouseX, mouseY)){
            startX = mouseX;
            startY = mouseY;
            lastClickX = x;
            lastClickY = y;
            dragging = true;
            // 放到最高位
        }
        float yd = 31.5F;
        double offsetY = scroll;

        if(!ClickUtils.isClickable(x, y + 31.5F, x + 100, y + 160, mouseX, mouseY)) return false;
        for (Module module : modules) {
            // Hover 动画
            if(ClickUtils.isClickable(x, y + yd + offsetY, x + 100, y + yd + 15 + offsetY, mouseX, mouseY)){
                // 切换
                    module.hove = true;
                    return true;
            }
            yd += 15f;
        }
        //
        return false;
    }
}
