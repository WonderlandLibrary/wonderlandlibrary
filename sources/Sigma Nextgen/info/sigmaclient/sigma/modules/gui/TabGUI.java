package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.event.player.KeyEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.hud.JelloTabGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static info.sigmaclient.sigma.gui.font.RenderSystem.drawGradientRect;
import static info.sigmaclient.sigma.utils.render.RenderUtils.drawTexture;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class TabGUI extends Module {
    public static JelloTabGUI tabgui = new JelloTabGUI();
    public TabGUI() {
        super("TabGUI", Category.Gui, "Jello tab gui");
    }
    public static int smoothAnimation(double current, double last){
        return (int) (current * mc.timer.renderPartialTicks + (last * (1.0f - mc.timer.renderPartialTicks)));
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            tabgui.tick();
            int x = 0;
            for(Category c : tabgui.cats) {
                if (c.location == null) continue;
                boolean selected = c.equals(tabgui.cats.get(tabgui.currentCategory));
                c.anim.interpolate(selected ? 7f : 0, -1);
            }
            tabgui.anim.interpolate(tabgui.currentCategory * 15f, 3);
//            category.lastSelectedTrans = category.selectedTrans;
            int y = 0;
            int categoryIndex = tabgui.currentCategory;
            Category category = tabgui.cats.get(categoryIndex);
            category.anim2.interpolate(category.selectedIndex * 15f, 3);
            List<Module> modules = SigmaNG.SigmaNG.moduleManager.getModulesInType(category);
            for(Module m : modules){
                if(y == category.selectedIndex){
                    m.sAnim.interpolate(5, 3);
                }else{
                    m.sAnim.interpolate(0, 3);
                }
                y++;
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onKeyEvent(KeyEvent event) {
        if(event.key == InputMappings.getInputByName("key.keyboard.up").getKeyCode()) {tabgui.keyUp();}
        if(event.key == InputMappings.getInputByName("key.keyboard.right").getKeyCode()) {tabgui.keyRight();}
        if(event.key == InputMappings.getInputByName("key.keyboard.down").getKeyCode()) {tabgui.keyDown();}
        if(event.key == InputMappings.getInputByName("key.keyboard.left").getKeyCode()) {tabgui.keyLeft();}
        super.onKeyEvent(event);
    }

    public void onRenderEvent(RenderEvent e){
        if (Minecraft.getInstance().isF3Enabled()) {
            return;
        }
        boolean enableBlur = Shader.isEnable();
        int tR = smoothAnimation(JelloTabGUI.tRed, JelloTabGUI.lasttRed);
        int tG = smoothAnimation(JelloTabGUI.tGreen, JelloTabGUI.lasttGreen);
        int tB = smoothAnimation(JelloTabGUI.tBlue, JelloTabGUI.lasttBlue);

        int bR = smoothAnimation(JelloTabGUI.bRed, JelloTabGUI.lastbRed);
        int bG = smoothAnimation(JelloTabGUI.bGreen, JelloTabGUI.lastbGreen);
        int bB = smoothAnimation(JelloTabGUI.bBlue, JelloTabGUI.lastbBlue);

        GlStateManager.resetColor();
//        RenderUtils.drawTexture(0.5f, 45.5f, 84, 86f, "tabguishadow", 1.0f);
        if(enableBlur){
            Shader.addBlur(5F, 50 - 0.5f, 5 + 75f, 50 + 77);
            RenderUtils.drawRect(5F, 45F + 5 - 0.5f, 5 + 75f, 45 + 77 + 5, ColorUtils.reAlpha(-6710887, 0.05f).getRGB());
        }else{
            drawGradientRect(5.0, 45 + 5 - 0.5f, 5 + 75, 45 + 77 + 5,(new Color(tR, tG, tB, 255)).getRGB(), (new Color(bR, bG, bB, 255)).getRGB());
        }
        RenderUtils.sigma_drawShadow(5F, 45F + 5 - 0.5f, 75f, 77, 4f, 1f);
        onRender(enableBlur);
    }
    public static float smoothTrans(double current, double last){
        return (float) (current * mc.timer.renderPartialTicks + (last * (1.0f - mc.timer.renderPartialTicks)));
    }
    public static void onRender(boolean enableBlur){
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        RenderUtils.startBlend();
//        GL11.glEnable(3042);
        GL11.glColor4f(1, 1, 1, 1);
        if(tabgui.showModules){
            int categoryIndex = tabgui.currentCategory;
            Category category = tabgui.cats.get(categoryIndex);
            List<Module> modules = SigmaNG.SigmaNG.moduleManager.getModulesInType(category);
            int size = modules.size();

            int tR = smoothAnimation(JelloTabGUI.tRed, JelloTabGUI.lasttRed);
            int tG = smoothAnimation(JelloTabGUI.tGreen, JelloTabGUI.lasttGreen);
            int tB = smoothAnimation(JelloTabGUI.tBlue, JelloTabGUI.lasttBlue);

            int bR = smoothAnimation(JelloTabGUI.bRed, JelloTabGUI.lastbRed);
            int bG = smoothAnimation(JelloTabGUI.bGreen, JelloTabGUI.lastbGreen);
            int bB = smoothAnimation(JelloTabGUI.bBlue, JelloTabGUI.lastbBlue);
            if(enableBlur){
                Shader.addBlur(0.5f +  75 + 10 - 0.5F, 40.5f + 5f - 0.5F + 5, 0.5f +  75+84 + 10 + 0.5F, 40.5f +
                            15*modules.size() + 5F + 2.5F + 5);
                RenderUtils.drawRect(0.5f +  75 + 10 - 0.5F, 40.5f + 5f - 0.5F + 5, 0.5f +  75+84 + 10 + 0.5F, 40.5f +
                        15*modules.size() + 5F + 2.5F + 5,new Color(255,255,255,30).getRGB());
            }else{
                drawGradientRect(0.5f +  75 + 10 - 0.5F, 40.5f + 5f - 0.5F + 5, 0.5f +  75+84 + 10 + 0.5F, 40.5f +
                        15*modules.size() + 5F + 2.5F + 5,/*415277420*/(new Color(tR, tG, tB, 255)).getRGB(), (new Color(bR, bG, bB, 255)).getRGB());
            }
//            float trans = category.selectedTrans;
//            float lastTrans = category.lastSelectedTrans;

            float smoothTrans = category.anim2.getValue();

            int y = 0;

            if(size != 0){
                GL11.glColor4f(1, 1, 1, 1);
                mc.getTextureManager().bindTexture(new ResourceLocation("sigma/slector.png"));
                drawTexture(85f, 50 + smoothTrans, 0f, 0f, 75 + 10f, 17.5f, 75, 17.5f);
            }
            for(Module m : modules){
                if(y == 0){
                    mc.getTextureManager().bindTexture(new ResourceLocation("sigma/shadowgui.png"));
                    drawTexture(0.5f +  75 + 5, 40.5f + 5f, 0, 0, 84 + 10, 20, 84 + 10, 86);
                }else
                if(y == size-1){
                    mc.getTextureManager().bindTexture(new ResourceLocation("sigma/shadowgui.png"));
                    drawTexture(0.5f +  75 + 5, 40.5f + 15*y + 5f + 5f, 0, 64.5f, 84 + 10, 20, 84 + 10, 86);
                }else{
                    mc.getTextureManager().bindTexture(new ResourceLocation("sigma/shadowgui.png"));
                    drawTexture(0.5f +  75 + 5, 40.5f + 15*y + 5f + 5f, 0, 30, 84 + 10, 15, 84 + 10, 86);
                }
                if(m.enabled){
                    JelloFontUtil.jelloFontBoldSmall.drawNoBSString(m.remapName, 85 + 11/2f + m.sAnim.getValue(), 45+ 15*y + 5 + 5, -1);
                }else{
                    JelloFontUtil.jelloFontMarker.drawNoBSString(m.remapName, 85 + 11/2f + m.sAnim.getValue(), 45+ 15*y + 5 + 5, -1);
                }
                y++;
            }
        }

        tabgui.seenTrans = tabgui.anim.getValue();
        RenderUtils.drawTexture(5, 45 + tabgui.seenTrans + 4.5f,75,17f,"slector",1f);

        int x = 0;
        for(Category c : tabgui.cats){
            if(c.location == null) continue;
            c.seenTrans = c.anim.getValue();
            GL11.glColor4f(1,1,1,1);
            JelloFontUtil.jelloFont20.drawNoBSString(c.n ,5 + c.seenTrans + 5.5f,45 + x*15 + 5 + 5,Color.WHITE.getRGB());
            x++;
        }
        GlStateManager.resetColor();
    }
}
