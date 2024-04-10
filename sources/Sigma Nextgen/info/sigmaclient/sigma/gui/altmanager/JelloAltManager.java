package info.sigmaclient.sigma.gui.altmanager;

import com.mojang.blaze3d.matrix.MatrixStack;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.gui.JelloTextField;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.modules.gui.Shader;
import info.sigmaclient.sigma.sigma5.utils.*;
import info.sigmaclient.sigma.utils.alt.HCMLJsonParser;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.config.alts.Alt;
import info.sigmaclient.sigma.config.alts.AltConfig;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.UUID;

import static info.sigmaclient.sigma.gui.Sigma5LoadProgressGui.霥瀳놣㠠釒;
import static info.sigmaclient.sigma.modules.Module.mc;
import static info.sigmaclient.sigma.modules.render.NameTags.贞䩉㥇딨햖;
import static info.sigmaclient.sigma.sigma5.utils.SomeAnim.欫좯콵甐鶲㥇;
import static info.sigmaclient.sigma.gui.mainmenu.SigmaGuiMainMenu.animatedMouseX;
import static info.sigmaclient.sigma.gui.mainmenu.SigmaGuiMainMenu.animatedMouseY;
import static info.sigmaclient.sigma.utils.render.RenderUtils.*;

public class JelloAltManager extends Screen {
    int scroll;
    Screen parent;
    public static Alt select = null;
    boolean adding = false;
    boolean offlineInput = false;
    MicrosoftLoginHelper microsoftLoginHelper = null;
    ConfigButton 쇼ኞ錌卒酋3;
    Sigma5AnimationUtil addAnimation = new Sigma5AnimationUtil(400, 400);
    Sigma5AnimationUtil deleteAnimation = new Sigma5AnimationUtil(400, 400);
    JelloTextField jelloTextField;
    public JelloAltManager(Screen parent){

        super(new StringTextComponent("AltManager"));
        parent = parent;
    }

    @Override
    public void onGuiClosed() {
        if(microsoftLoginHelper != null){
            microsoftLoginHelper.close();
        }
        super.onGuiClosed();
    }

    @Override
    public void initGui() {
        jelloTextField = new JelloTextField(
                0, mc.fontRenderer, width / 2f - 75 + 17, height / 2f - 60, 100, 30, "Username..."
        );
        select = null;
        addAnimation = new Sigma5AnimationUtil(400, 400);
        addAnimation.animTo(Sigma5AnimationUtil.AnimState.SLEEPING);
        deleteAnimation = new Sigma5AnimationUtil(400, 400);
        deleteAnimation.animTo(Sigma5AnimationUtil.AnimState.SLEEPING);
        for (Alt alt : AltConfig.Instance.alts) {
            alt.animationUtil = new Sigma5AnimationUtil(400, 400);
            alt.animationUtil.animTo(Sigma5AnimationUtil.AnimState.SLEEPING);
            alt.select = new Sigma5AnimationUtil(114, 114);
            if(alt.uuid.equals(mc.session.getPlayerID()) && select == null){
                select = alt;
            }
        }
        쇼ኞ錌卒酋3 = new ConfigButton(
                width - 90 / 2, 11, 70 / 2, 30 / 2, "Add +",(n)->{
            add();
        }, JelloFontUtil.jelloFont25);
        adding = false;
        offlineInput = false;
//        this.addButton(쇼ኞ錌卒酋3);
        super.initGui();
    }
    public void add(){
        adding = !adding;
    }
    public void login(Alt alt){
        mc.session = new Session(
                alt.name,
                alt.uuid,
                alt.offline ? "0" : alt.token,
                "Legacy"
        );
    }
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(delta != 0){
            scroll += delta > 0 ? 20 : -20;
            if(scroll > 0) scroll = 0;
            if(scroll < -AltConfig.Instance.alts.size() * 58) scroll = -AltConfig.Instance.alts.size() * 58;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ScaledResolution sr = new ScaledResolution(mc);
        if(!adding) {
            float height = 50;
            float startY = 57;
            Alt prev = null;
            for (Alt alt : AltConfig.Instance.alts) {
                if (prev == null || prev.animationUtil.getAnim() > 0.2f) {
                    alt.animationUtil.animTo(Sigma5AnimationUtil.AnimState.ANIMING);
                } else {
                    alt.animationUtil.animTo(Sigma5AnimationUtil.AnimState.SLEEPING);
                }
                prev = alt;
                float width2 = (float) Math.floor(sr.getScaledWidth() * 0.34f) + 2;
                float anim = sr.getScaledWidth() - width2 - 8 - 15 + 20;
                float scale = alt.animationUtil.getAnim();
                scale = 欫좯콵甐鶲㥇((float) scale, 0.17, 1.0, 0.51, 1.0);
                scale *= 1.18f;
                if (scale > 1.09f) {
                    scale = 1.18f - scale + 1;
                }
                float 竁藸뎫捉睬 = 15 - anim + anim * scale, 䩉湗鶲娍圭 = startY;
                final int 鶲䩉缰啖錌杭 = scroll;
                final float n5 = 䩉湗鶲娍圭 + 鶲䩉缰啖錌杭;
                if(ClickUtils.isClickableWithRect(竁藸뎫捉睬, n5, sr.getScaledWidth() - width2 - 8 - 15 - 15, height, mouseX, mouseY)){
                    if(button == 0) {
                        if (select == alt) {
                            login(alt);
                        } else {
                            select = alt;
                        }
                    }else{
                        AltConfig.Instance.alts.remove(alt);
                    }
                    return true;
                }
                startY += height + 8;
            }
        }else{
            float wid = 150, hei = 190;
            if(!ClickUtils.isClickableWithRect(sr.getScaledWidth() / 2f - wid / 2f, sr.getScaledHeight() / 2f - hei / 2f,wid,hei, mouseX, mouseY)){
                adding = false;
                offlineInput = false;
                return true;
            }
            if(offlineInput){
                jelloTextField.mouseClicked(mouseX, mouseY, button);
                if(ClickUtils.isClickableWithRect(sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 70,
                        60, 20,mouseX,mouseY)){
                    Alt alt = new Alt(
                            jelloTextField.getText(),
                            UUID.randomUUID().toString().replace("-", "")
                    );
                    AltConfig.Instance.alts.add(alt);
                    login(alt);
                    SigmaNG.getSigmaNG().configManager.saveDefaultConfig();
                    adding = false;
                    offlineInput = false;
                }
                return true;
            }
            if(ClickUtils.isClickableWithRect(sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 50,50,15, mouseX, mouseY)){
                new HCMLJsonParser().parserCheck();
                adding = false;
                offlineInput = false;
                return true;
            }
            if(ClickUtils.isClickableWithRect(sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 70,50,15, mouseX, mouseY)){
                offlineInput = true;
                jelloTextField.setText("");
                return true;
            }
            if(ClickUtils.isClickableWithRect(sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 90,50,15, mouseX, mouseY)){
                adding = false;
                microsoftLoginHelper = new MicrosoftLoginHelper();
                return true;
            }
        }
        if(쇼ኞ錌卒酋3.mouseClicked(mouseX, mouseY, button)){
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    FakeNetHandlerPlayClient fakeNetHandlerPlayClient = new FakeNetHandlerPlayClient(mc);
    ClientWorld fakeworld = new FakeWorld(fakeNetHandlerPlayClient);
    ClientPlayerEntity player;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/background.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float scaleW = sr.getScaledWidth() / 960f;
        float scaleH = sr.getScaledHeight() / 501f;
        float m1 = sr.getScaledWidth() / 960f * 3000;
        float h1 = sr.getScaledHeight() / 501f * 844;
        float m = 1.0f;
        float addM = (m1 * m - sr.getScaledWidth()) / 2f;
        float addH = (h1 * m - sr.getScaledHeight()) / 2f;

        drawTexture(
                -(animatedMouseX / sr.getScaledWidth() - 0.5f) * addM - addM,
                -(animatedMouseY / sr.getScaledHeight() - 0.5f) * addH - addH,
                0, 0,
                m1 * m,
                h1 * m,
                m1 * m,
                h1 * m
        );
        RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0xFAF2F1F2, true).getRGB());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        drawTexture(30 * 0.5, 41 * 0.5, 218 * 0.5, 35 * 0.5, "jelloaltmanager", 1);
//        drawTexture(sr.getScaledWidth() - 67/2f - 22/2f,  50/2f, 67/2f, 19/2f, "add", 1);
        쇼ኞ錌卒酋3.myRender(mouseX, mouseY, new Color(41, 166, 255), 1);


        float width = sr.getScaledWidth() / 17f * 10f;
        float awidth = sr.getScaledWidth() - width;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float width2 = (float) Math.floor(sr.getScaledWidth() * 0.34f) + 2;
        float reduce = 57;
//        drawTexture(sr.getScaledWidth() - width2 * 1.15F - 15, 58 - 15, sr.getScaledWidth() - 15 + 30 - (sr.getScaledWidth() - width2 * 1.15F - 15), sr.getScaledHeight() - 17 + 30 - (58 - 15), "addalt2", 1f);
//        RenderUtils.drawRoundedRect(sr.getScaledWidth() - width2 * 1.15F, 58, sr.getScaledWidth() - 15, sr.getScaledHeight() - 17, 3, -1);
//        RenderUtils.汌ꪕ蒕姮Ⱋ樽();
//        RenderUtils.drawRoundShadow(sx - 125, sy - 250, 125, 250, new Color(244, 244, 244, (int) (myAlpha * 255)).getRGB());
        RenderUtils.drawRoundShadow(sr.getScaledWidth() - width2 - 8, reduce, (float) Math.floor(sr.getScaledWidth() * 0.335f),
                sr.getScaledHeight() - reduce - 17,
                Color.WHITE.getRGB());
        float w2 = (float) Math.floor(sr.getScaledWidth() * 0.335f) - 3;
        float multi = w2 / 920f;
        float multi2 = sr.getScaledWidth() / 1940f;
        float h2 = 684 * multi;
        if(select == null) {
            drawTextureLocationZoom(sr.getScaledWidth() - width2 - 8,
                    reduce + (sr.getScaledHeight() - reduce - 17) / 2f - h2 / 2f,
                    w2,
                    h2, "alt/img", -1); // 920 x 684
        }else{

            drawTextureLocationZoom(sr.getScaledWidth() - width2 - 8 + w2 / 2f - 120 * multi2,
                    reduce + (sr.getScaledHeight() - reduce - 17) / 2f - 365 / 2f * multi2 - 80,
                    240 * multi2,
                    365 * multi2, "alt/man", -1); // 920 x 684
            JelloFontUtil.jelloFont36.drawCenteredString(select.name, sr.getScaledWidth() - width2 - 8 + w2 / 2f,
                    reduce + (sr.getScaledHeight() - reduce - 17) / 2f + 10, new Color(50, 50, 50).getRGB());
        }

        float height = 50;
        float startY = 57;
        Alt prev = null;
        for (Alt alt : AltConfig.Instance.alts) {
            if(prev == null || prev.animationUtil.getAnim() > 0.2f) {
                alt.animationUtil.animTo(Sigma5AnimationUtil.AnimState.ANIMING);
            }else{
                alt.animationUtil.animTo(Sigma5AnimationUtil.AnimState.SLEEPING);
            }
            prev = alt;
            float anim = sr.getScaledWidth() - width2 - 8 - 15 + 20;
            float scale = alt.animationUtil.getAnim();
            scale = 欫좯콵甐鶲㥇((float) scale, 0.17, 1.0, 0.51, 1.0);
            scale *= 1.18f;
            if(scale > 1.09f){
                scale = 1.18f - scale + 1;
            }
            float 竁藸뎫捉睬 = 15 - anim + anim * scale, 䩉湗鶲娍圭 = startY;
            int 댠哝娍酋瀳 = this.width, 㢸ꪕ䬾硙葫 = this.height;
            int 쥡娍샱셴ᢻ = 贞䩉㥇딨햖(핇댠䂷呓贞.white.哺卫콗鱀ಽ, 핇댠䂷呓贞.black.哺卫콗鱀ಽ, 2.0f);
            final int 鶲䩉缰啖錌杭 = scroll;
            final float max = Math.max(0, 䩉湗鶲娍圭 - 鶲䩉缰啖錌杭);
            final float max2 = Math.max(0, 㢸ꪕ䬾硙葫 + Math.min(100, 䩉湗鶲娍圭 - 鶲䩉缰啖錌杭 - max));
            final float n2 = Math.min(50, max2) / 50.0f;

            final float n5 = 䩉湗鶲娍圭 + 鶲䩉缰啖錌杭;
            if(n5 < -sr.getScaledHeight() - 10) {startY += height + 8;
                continue;
            }
            if(n5 > sr.getScaledHeight() + 10) {startY += height + 8;
                continue;
            }
            RenderUtils.drawRoundShadow(竁藸뎫捉睬, n5, sr.getScaledWidth() - width2 - 8 - 15 - 15, height,
                    霥瀳놣㠠釒(핇댠䂷呓贞.white.哺卫콗鱀ಽ, n2));
            JelloFontUtil.jelloFont24.drawNoBSString(alt.name, 竁藸뎫捉睬 + 55, n5 + 13, Color.BLACK.getRGB());
                JelloFontUtil.jelloFont15.drawNoBSString("Username: " + alt.name, 竁藸뎫捉睬 + 55, n5 + 27.5f, -6710887);
                JelloFontUtil.jelloFont15.drawNoBSString(alt.offline ? "Offline account" :  "Online account", 竁藸뎫捉睬 + 55, n5 + 35, -6710887);
//            RenderUtils.牰蓳躚唟捉璧(竁藸뎫捉睬 + 6.5f + 37.5f / 2f,
//                    n5 + 6.5f + 37.5f / 2f,
//                    76, new Color(150, 150, 150).getRGB());
//            StencilUtil.initStencilToWrite();
//            RenderUtils.牰蓳躚唟捉璧(竁藸뎫捉睬 + 6.5f + 37.5f / 2f,
//                    n5 + 6.5f + 37.5f / 2f,
//                    74, -1);
//            StencilUtil.readStencilBuffer(1);
            drawTextureLocationZoom(竁藸뎫捉睬 + 6.5f,
                    n5 + 6.5f,
                    37.5f,
                    37.5f, "alt/skin", -1); // 920 x 684
            drawTextureLocationZoom(竁藸뎫捉睬 + 0.5f,
                    n5,
                    50,
                    50, "alt/cercle", -1); // 920 x 684
            alt.select.animTo((alt == select) ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
            float a = alt.select.getAnim();
            if(a != 0)
            drawTextureLocation(sr.getScaledWidth() - width2 - 8 - 15,
                    n5 + 13f,
                    9 * Math.floor(a * 10) / 10,
                    46 / 2f, "alt/select", new Color(-1)); // 920 x 684

//            StencilUtil.uninitStencilBuffer();
//            㕠鄡呓ᢻ낛.퉧핇樽웨䈔属(竁藸뎫捉睬, n5, 竁藸뎫捉睬 + 댠哝娍酋瀳 + 20, n5 + max2, true);
//            if (值埙霥浣浦 != null) {
//                drawHead();
//                drawName();
//                drawStat(n2);
//                if (㝛韤䩜䎰ใ.getAnim() > 0.0f) {
//                    if (max2 > 55) {
//                        㕠鄡呓ᢻ낛.drawTextureSigma((float)(竁藸뎫捉睬 + 㦖缰뫤랾퉧()), n5 + 26 * max2 / 100.0f, 18.0f * 㝛韤䩜䎰ใ.getAnim() * max2 / 100.0f, 47 * max2 / 100.0f, 뚔弻缰硙柿.掬㐖햠쬫竬, 쥦嘖酭綋錌瀳() ? 쥡娍샱셴ᢻ : 핇댠䂷呓贞.white.哺卫콗鱀ಽ);
//                    }
//                }
            startY += height + 8;
        }
        addAnimation.animTo(adding ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        float b = addAnimation.getAnim();
        if(b != 0){
            float scale = b;
            scale = 欫좯콵甐鶲㥇((float) scale, 0.17, 1.0, 0.51, 1.0);
            scale *= 1.18f;
            if(scale > 1.09f){
                scale = 1.18f - scale + 1;
            }

            GL11.glPushMatrix();
            GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
            GlStateManager.scale(scale, scale, 1);
            GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
            float wid = 150, hei = 190;
            Sigma5BlurUtils.vblur(20);
            RenderUtils.drawRoundShadow(sr.getScaledWidth() / 2f - wid / 2f, sr.getScaledHeight() / 2f - hei / 2f,wid,hei,-1);
            JelloFontUtil.jelloFont32.drawSmoothString( offlineInput ? "Log in" : "Login method", sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 20, Color.BLACK.getRGB());
            if(!offlineInput) {
                JelloFontUtil.jelloFont22.drawSmoothString("| Copy from HCML", sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 50, Color.BLACK.getRGB());
                JelloFontUtil.jelloFont22.drawSmoothString("| Offline", sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 70, Color.BLACK.getRGB());
                JelloFontUtil.jelloFont22.drawSmoothString("| Microsoft", sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 90, Color.BLACK.getRGB());
                if (ClickUtils.isClickableWithRect(sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 50, 50, 15, mouseX, mouseY)) {
                    RenderUtils.drawRect(
                            sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 45, sr.getScaledWidth() / 2f - wid / 2f + 17 + 100, sr.getScaledHeight() / 2f - hei / 2f + 50 + 15,
                            new Color(0, 0, 0, 50).getRGB());
                }
                if (ClickUtils.isClickableWithRect(sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 70, 50, 15, mouseX, mouseY)) {
                    RenderUtils.drawRect(
                            sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 65, sr.getScaledWidth() / 2f - wid / 2f + 17 + 100, sr.getScaledHeight() / 2f - hei / 2f + 70 + 15,
                            new Color(0, 0, 0, 50).getRGB());
                }
            }else{
                jelloTextField.drawTextBoxCustom(1, 9, 170, 50);
                RenderUtils.drawRoundShadow(sr.getScaledWidth() / 2f - wid / 2f + 17, sr.getScaledHeight() / 2f - hei / 2f + 70,
                        60, 20, new Color(41, 166, 255).getRGB());
                JelloFontUtil.jelloFont22.drawSmoothString("Login", sr.getScaledWidth() / 2f - wid / 2f + 23, sr.getScaledHeight() / 2f - hei / 2f + 75, Color.WHITE.getRGB());
            }
            GL11.glPopMatrix();
        }
        animatedMouseX += (float) (((mouseX - animatedMouseX) / 3) + 0.2);
        animatedMouseY += (float) (((mouseY - animatedMouseY) / 3) + 0.2);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(adding && offlineInput){
            jelloTextField.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if(adding && offlineInput){
            jelloTextField.keyReleased(keyCode, scanCode, modifiers);
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(adding && offlineInput){
            jelloTextField.charTyped(codePoint, modifiers);
        }
        return super.charTyped(codePoint, modifiers);
    }
}
