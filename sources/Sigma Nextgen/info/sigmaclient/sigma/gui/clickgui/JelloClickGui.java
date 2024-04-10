package info.sigmaclient.sigma.gui.clickgui;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.clickgui.config.ConfigManagerGUI;
import info.sigmaclient.sigma.sigma5.utils.BrainFreezeParticles;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.BrainFreeze;
import info.sigmaclient.sigma.modules.gui.Shader;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.clickgui.musicplayer.JelloMusicPlayer;
import info.sigmaclient.sigma.gui.clickgui.simple.JelloModuleShower;
import info.sigmaclient.sigma.gui.clickgui.simple.JelloSubUI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static info.sigmaclient.sigma.modules.Module.mc;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class JelloClickGui extends Screen {
    public BrainFreezeParticles particleManager = new BrainFreezeParticles("brainFreeze");
    public ConfigManagerGUI configManagerGUI = new ConfigManagerGUI();
    public List<JelloSubUI> dropGUIS = new ArrayList<>();
    public JelloMusicPlayer musicPlayer = new JelloMusicPlayer();
    public Sigma5AnimationUtil joinAnim = new Sigma5AnimationUtil(450, 125);
    public Sigma5AnimationUtil moduleSelectAnim;
    public Sigma5AnimationUtil moduleSelectAnim2;
    public static JelloModuleShower currentModule = null;
    public static PartialTicksAnim leave = new PartialTicksAnim(1.8f);
    int cacheMouseX, cacheMouseY;
    public boolean startLeaving; // always 0 - 1
    public JelloClickGui(){
        super(new StringTextComponent("JelloClickGUI"));
        // 初始化
        for(Category category : Category.values()){
            List<Module> a = SigmaNG.getSigmaNG().moduleManager.getModulesInType(category);
            JelloSubUI dropGUI = new JelloSubUI(a, category);
            dropGUIS.add(dropGUI);
        }
        float dx = 10, dy = 10;
        int stage = 0;
        for(JelloSubUI subUI : dropGUIS){
            subUI.x = dx;
            subUI.y = dy;
            dx += 105;
            stage ++;
            if(stage == 4){
                dx = 10;
                dy += 165;
            }
        }
        moduleSelectAnim2 = new Sigma5AnimationUtil(200, 120);
        moduleSelectAnim = new Sigma5AnimationUtil(240, 200);
//        moduleSelectAnim.setValue(0);
        joinAnim.reset();
        startLeaving = false;
    }
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        configManagerGUI.setScroll(mouseX, mouseY, delta);
        // 滚动
        for(JelloSubUI subUI : dropGUIS){
            subUI.scrollMouse(cacheMouseX, cacheMouseY, delta);
        }
        if(currentModule != null){
            currentModule.scrollMouse(cacheMouseX, cacheMouseY, delta);
        }
        if(delta != 0){
            musicPlayer.handleMouse(delta > 0 ? 10 : -10, cacheMouseX, cacheMouseY);
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    boolean init = false;
    @Override
    public void initGui() {
        for (Module module : SigmaNG.getSigmaNG().moduleManager.modules) {
            module.hove = false;
        }
        musicPlayer.init();
        musicPlayer.searchBar.setFocused2(false);
        joinAnim.reset();
        currentModule = null;
        startLeaving = false;
        joinAnim = new Sigma5AnimationUtil(450, 125);
        leave.setValue(1.0f);
        configManagerGUI.show = false;
        configManagerGUI.showAdd = false;
        configManagerGUI.reset();
        init = true;
        super.initGui();
    }

    @Override
    public void tick() {
        // 进场
//        if(joinAnim.getScale() != 1){
//            joinAnim.anim(11, false);
//        }

        if(currentModule != null) {
            currentModule.renderAnim();
        }
        if(currentModule != null) {
            currentModule.animTick(cacheMouseX, cacheMouseY);
        }
        // 先动画
//        if(currentModule != null && !currentModule.close) {
//            // 向后
//            if (moduleSelectAnim.getValue() != 10) {
//                moduleSelectAnim.interpolate(10, 4); //0 - 100
//            }
//        }else{
//            // 向前
//            if(moduleSelectAnim.getValue() != 0) {
//                moduleSelectAnim.interpolate(0, 4);
//            }
//        }
        for(JelloSubUI subUI : dropGUIS){
            subUI.ticks();
        }
        if(currentModule != null) {
            // 完成
//            if (moduleSelectAnim.getAnim() == 0) {
//                currentModule = null;
//            }
        }
        musicPlayer.ticks(cacheMouseX, cacheMouseY);
        super.tick();
    }

    public float getAnimation(float f, float f2) {
        if (joinAnim.isAnim != Sigma5AnimationUtil.AnimState.SLEEPING) {
            return (float)(Math.pow(2.0, -10.0f * f) * Math.sin((double)(f - f2 / 4.0f) * (Math.PI * 2) / (double)f2) + 1.0);
        }
        return 牰䩜躚㢸錌ꈍ(f, 0.0f, 1.0f, 1.0f);
    }
    public static float 牰䩜躚㢸錌ꈍ(float n, final float n2, final float n3, final float n4) {
        return -n3 * (n /= n4) * (n - 2.0f) + n2;
    }
    public static float 䩉罡쟗붃괠㨳(float n, final float n2, final float n3, final float n4) {
        final float n5 = 1.70158f;
        return n3 * ((n = n / n4 - 1.0f) * n * ((n5 + 1.0f) * n + n5) + 1.0f) + n2;
    }
    public static float 陂瀳瀳疂쿨뵯(float n, final float n2, final float n3, final float n4) {
        n /= n4;
        return n3 * n * n * n + n2;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        startLeaving = mc.currentScreen == null;
        joinAnim.animTo(!startLeaving ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        float f2 = getAnimation(joinAnim.getAnim(), 1f);
        float f8 = joinAnim.getAnim();
        f2 = 1.5f - f2 * 0.5f;
        float f3 = f2;
//        KawaseBlur.renderBlurFull(Math.min(1.0f, joinAnim.getAnim() * 4.0f));
        if(joinAnim.getAnim() != 1)
            Shader.clickGUIIter = (int) (Math.min(1.0f, joinAnim.getAnim() * 4.0f) * 20);
        else
            Shader.clickGUIIter = 20;
//        stopShader();
//        float f6 = 䩉罡쟗붃괠㨳(joinAnim.getAnim(), 0.0f, 1.0f, 1.0f);
//        if (this.ኞ훔竁㐖杭.핇樽랾쇼뗴.홵玑ࡅ뎫ಽ嶗() == 蛊퉧쬷湗좯.眓頉뼢牰玑) {
//            f6 = 훔웎䢿刃쇼.陂瀳瀳疂쿨뵯(this.ኞ훔竁㐖杭.핇樽랾쇼뗴.杭眓鱀陂ၝ㠠(), 0.0f, 1.0f, 1.0f);
//        }
        if(startLeaving) {
//            float f5 = 1.0f;
//            f5 -= joinAnim.getAnim() * 0.1f;
//            f3 *= 1.0f + f6 * 0.2f;
        }
        
        float outro_ = f3;
//                leave.getValue();
        float moduleScale, alpha = 1;
//        if(joinAnim.getScale() != 1) {

        if(f2 != 1) {
////            joinAnim.anim(10, false);
//            double scale = 1 + (1 - f2) * 0.3 - 0.06; //1.3 - 1.0
//            /* 回溯*/
//            if (scale <= 0.97) scale = 0.97 + (0.97 - scale);
            alpha = (float) (1 - Math.min(Math.max(f2 - 1, 0), 0.2) * 1.2 /* 0.0 - 1 */);
        }
        if(mc.currentScreen == null) {
            alpha = (float) Math.min(1, Math.max((1.5 - outro_) * 2.1f, 0));
        }
        float f4 = 0.2f * alpha;
        if(startLeaving){
            Shader.clickGUIIter = (int) (alpha * 20);
        }
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();

        RenderUtils.drawRect(0, 0, width, height, new Color(0, 0, 0, f4).getRGB());
        // ??
        JelloFontUtil.jelloFont20.drawString("", 0, 0, 0);
        if(SigmaNG.getSigmaNG().moduleManager.getModule(BrainFreeze.class).enabled){
            particleManager.render(alpha);
        }
        cacheMouseX = mouseX;
        cacheMouseY = mouseY;

        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        // ??

        if(f3 != 1){
//            joinAnim.anim(10, false);
//            double scale = 1 + (1 - f2) * 0.3 - 0.06 /* 1.14 - 0.94 */; // 1.3 - 0.9 [+-0.03]
//            /* 回溯*/ if(scale <= 0.97) scale = 0.97 + (0.97 - scale); // 0.0 - 0.03 [+]
            GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
            GlStateManager.scale(f3, f3, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
        }
        moduleSelectAnim.animTo((currentModule != null && !currentModule.close) ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        moduleSelectAnim2.animTo((currentModule != null && !currentModule.close) ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);


        if(moduleSelectAnim2.getAnim() == 0 && currentModule != null && currentModule.close)
            currentModule = null;
        float f5 = 1.0f;
        float f7 = 䩉罡쟗붃괠㨳(moduleSelectAnim.getAnim(), 0.0f, 1.0f, 1.0f);
        if (this.moduleSelectAnim.isAnim == Sigma5AnimationUtil.AnimState.SLEEPING) {
            f7 = 陂瀳瀳疂쿨뵯(moduleSelectAnim.getAnim(), 0.0f, 1.0f, 1.0f);
        }
        f5 -= f7 * 0.1f;
        f3 *= 1.0f + f7 * 0.2f;
        // 先动画
        if(currentModule != null && !currentModule.close) {
            // 向后
//            double scale = 0.93 + 0.05 * 0.25;
//            if(moduleSelectAnim.getValue() != 10) {
//                moduleScale = moduleSelectAnim.getValue() / 10;
//
//                scale = (1.0 - moduleScale * 0.1); // 1 - 0.9
//                if(scale <= 0.95){
//                    double a = 0.95 - scale; // 0 - 0.05
//                    scale = 0.93 + a * 0.25; // 0.93 - 0.95
//                } // jello bounce
//
//            }
//            if (this.ኞ훔竁㐖杭 != null) {
                // dd
//            }
            GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
            GlStateManager.scale(f5, f5, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
            if(mc.currentScreen != null)
                alpha = f5;
        }else{
            if(f5 != 1) {
                // 向前
//            if(moduleSelectAnim.getValue() != 0) {
//                moduleScale = (moduleSelectAnim.getValue() / 10);
//                // scaled
//                double scale = 0.93 + 0.05 * 0.25 + (1 - moduleScale) * (1 - 0.93 - 0.05 * 0.25); // 1 - 0.9
                GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
                GlStateManager.scale(f5, f5, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
                if(mc.currentScreen != null)
                    alpha = f5;
            }
//            }
        }

        // 每个
        for(JelloSubUI subUI : dropGUIS){
            subUI.render(mouseX, mouseY, partialTicks, alpha, f5);
        }
        // todo musicplayer
        musicPlayer.drawPlayer(mouseX, mouseY, alpha);
        GL11.glPopMatrix();
        configManagerGUI.render(alpha, mouseX, mouseY);
        GL11.glPushMatrix();
        // 选择
        if(currentModule != null) {
            float 杭眓鱀陂ၝ㠠 = moduleSelectAnim2.getAnim();
            float n = 䩉罡쟗붃괠㨳(杭眓鱀陂ၝ㠠, 0.0f, 1.0f, 1.0f);
            if (currentModule.close) {
                n = 牰䩜躚㢸錌ꈍ(杭眓鱀陂ၝ㠠, 0.0f, 1.0f, 1.0f);
            }
            // 完成
//            if (!currentModule.anim.isEnd()) {
            float scales = 0.8f + n * 0.2f;
                currentModule.drawPanel(mouseX, mouseY, partialTicks, 杭眓鱀陂ၝ㠠, scales);
//            */
        }
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 选择
        if(button == 0)
            musicPlayer.isMouseDown = true;
        if(currentModule != null){
            if(currentModule.close) return super.mouseClicked(mouseX, mouseY, button);
            if(currentModule.clickPanel((int) mouseX, (int) mouseY)) {
                return super.mouseClicked(mouseX, mouseY, button);
            }else{
                currentModule.close();
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
        if(configManagerGUI.click(mouseX, mouseY, button)){
            return false;
        }
        // todo musicplayer
        if(musicPlayer.clickPanel((int) mouseX, (int) mouseY, button)){
            return super.mouseClicked(mouseX, mouseY, button);
        }
        for(JelloSubUI subUI : dropGUIS){
            if(subUI.clicked((int) mouseX, (int) mouseY, button)){
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if(musicPlayer.searchBar != null)
            musicPlayer.searchBar.keyReleased(keyCode, scanCode, modifiers);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        configManagerGUI.keyPress(keyCode, scanCode, modifiers);
        if(musicPlayer.searchBar != null)
            musicPlayer.searchBar.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        configManagerGUI.typeKet(codePoint, modifiers);
        if(musicPlayer.searchBar != null)
            musicPlayer.searchBar.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        musicPlayer.isMouseDown = false;
        // todo musicplayer
        musicPlayer.releasePanel((int) mouseX, (int) mouseY);
        for(JelloSubUI subUI : dropGUIS){
            subUI.release(mouseX, mouseY, button); // stop drag
        }
        if(currentModule != null){
            currentModule.release(0, 0, (int) mouseX, (int) mouseY);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onGuiClosed() {
        for (Module module : SigmaNG.getSigmaNG().moduleManager.modules) {
            module.hove = false;
        }
        new Thread(()-> SigmaNG.SigmaNG.configManager.saveDefaultConfigWithoutAlt()).start();
        currentModule = null;
//        moduleSelectAnim.setValue(0);
//        Shader.clickGUIIter = 0;
        startLeaving = true;
//        startLeaving = leaveAnimAlpha.getValue() != 0;
//        leaveAnim.setValue(10);
//        leaveAnimAlpha.setValue(10);
    }
}
