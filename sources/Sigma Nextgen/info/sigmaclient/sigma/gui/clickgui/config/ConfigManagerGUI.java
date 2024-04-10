package info.sigmaclient.sigma.gui.clickgui.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.JelloTextField;
import info.sigmaclient.sigma.sigma5.utils.ConfigButton;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static info.sigmaclient.sigma.sigma5.utils.SomeAnim.欫좯콵甐鶲㥇;
import static info.sigmaclient.sigma.utils.render.RenderUtils.霥瀳놣㠠釒;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ConfigManagerGUI {
    MatrixStack emptyStack = new MatrixStack();
    public Sigma5AnimationUtil scale = new Sigma5AnimationUtil(300, 100);
    public Sigma5AnimationUtil addProfile = new Sigma5AnimationUtil(300, 100);
    public CopyOnWriteArrayList<Config> configs = new CopyOnWriteArrayList<>();
    float scroll = 0;
    float maxScroll = 0;
    boolean sss = false;
    Config reNameConfig = null;
    ConfigButton add, blank, dupe;
    JelloTextField edit = new JelloTextField(0, mc.fontRenderer, 0, 0, 80, 25);
    static class Config {
        public boolean show = false, renaming = false;
        public Sigma5AnimationUtil editAnimation = new Sigma5AnimationUtil(290, 290);
        public String name;
        public File path;
        public Sigma5AnimationUtil animationUtil = new Sigma5AnimationUtil(290, 290);
        public float alpha = 0;
        public Config(String name, File path) {
            this.name = name;
            this.path = path;
        }
    }
    public boolean show = false;
    public boolean showAdd = false;
    public boolean renaming = false;
    public void render(float alpha, int x, int y){
        if(!show || showAdd){
            reNameConfig = null;
        }
        edit.setVisible(false);
        scale.animTo(show ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        addProfile.animTo(showAdd ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        float myAlpha = scale.getAnim();
        float n = 欫좯콵甐鶲㥇(myAlpha, 0.37, 1.48, 0.17, 1);
        if (scale.isAnim == Sigma5AnimationUtil.AnimState.SLEEPING) {
            n = 欫좯콵甐鶲㥇(myAlpha, 0.38, 0.73, 0.0, 1.0);
        }
        ScaledResolution sr = new ScaledResolution(mc);
        float w = sr.getScaledWidth(), h = sr.getScaledHeight();
        float mw = 110 / 4f, mh = 82 / 4f;
        RenderUtils.drawTextureLocationZoom(w - mw - 7, h - mh - 7.5f, mw, mh, "options", new Color(1, 1, 1, alpha * 0.3f));
        String configName = ConfigManager.currentProfile == null ? "null" : ConfigManager.currentProfile;
        float width = (float) JelloFontUtil.jelloFont20.getStringWidth(configName);
        JelloFontUtil.jelloFont20.drawNoBSString(configName, w - mw - 8.5f - width - 5f + 1, h - mh - 1, new Color(1, 1, 1, alpha * 0.5f).getRGB());
//        JelloFontUtil
        float sx = (float) (w - mw - 7 + mw), sy = (float) (h - mh - 7.5f + mh);

        if(!ClickUtils.isClickable(sx - 125, sy - 250, sx, sy, x, y)){
            show = false;
            showAdd = false;
        }
        for (Config config : configs) {
            config.editAnimation.animTo(config.show ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        }
        myAlpha *= alpha;
        if(myAlpha != 0 && sss) {
            GL11.glPushMatrix();
            if (n != 1) {
                GlStateManager.translate(sr.getScaledWidth(), sr.getScaledHeight(), 0);
                GlStateManager.scale(0.8f + n * 0.2f, 0.8f + n * 0.2f, 0);
                GlStateManager.translate(-sr.getScaledWidth(), -sr.getScaledHeight(), 0);
            }
            RenderUtils.drawRoundShadow(sx - 125, sy - 250, 125, 250, new Color(244, 244, 244, (int) (myAlpha * 255)).getRGB());
            JelloFontUtil.jelloFont25.drawNoBSString("Profiles", sx - 125 + 12.5f, sy - 250 + 12.5f, 霥瀳놣㠠釒(-16711423, 0.8f * myAlpha));
            RenderUtils.drawRect(sx - 125 + 12.5f, sy - 250 + 30 + 5, sx - 12.5f, sy - 250 + 30 + 5.5f, 霥瀳놣㠠釒(-16711423, 0.05f * myAlpha));
            float cy = sy - 250 + 30 + 5.5f + 5 - scroll;

            float n3 = 0.9f + (1.0f - 欫좯콵甐鶲㥇(addProfile.getAnim(), 0.0, 0.96, 0.69, 0.99)) * 0.1f;
            if (!addProfile.isAnim.is()) {
                n3 = 0.9f + (1.0f - 欫좯콵甐鶲㥇(addProfile.getAnim(), 0.61, 0.01, 0.87, 0.16)) * 0.1f;
            }
            if (add != null)
                add.myRender(x, y, new Color(59, 153, 253), myAlpha);
            GL11.glPushMatrix();
            if (n3 != 1) {
                float scaleX = sx - 125 / 2f, scaleY = sy - 125;
                GlStateManager.translate(scaleX, scaleY, 0);
                GlStateManager.scale(n3, n3, 0);
                GlStateManager.translate(-scaleX, -scaleY, 0);
            }
            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect(sx - 125 + 12.5f - 7f, sy - 250 + 30 + 5 + 3f, sx - 12.5f + 7f, sy, -1);
            StencilUtil.readStencilBuffer(1);
            for (Config config : configs) {
                boolean hh = ClickUtils.isClickable(sx - 125 + 12.5f - 7f, cy, sx - 12.5f + 7f, cy + 35, x, y);
                config.animationUtil.animTo(hh ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
                if(!hh || config.renaming){
                    config.show = false;
                }
                if(reNameConfig == config){
                    edit.setVisible(true);
                    edit.x = (int) (sx - 125 + 12.5f - 7f + 10);
                    edit.y = (int) (cy + 5);
                    edit.drawTextBox();
                    cy += 35;
                    continue;
                }
                float n113 = 欫좯콵甐鶲㥇(config.editAnimation.getAnim(), 0.6, 1.1, 0.3, 1);
                float offsetX = n113 * -100;
                config.alpha = config.animationUtil.getAnim();
                float s8 = sx - 12.5f + 7f + offsetX;
                boolean hh2 = ClickUtils.isClickable(s8, cy, s8 + 50, cy + 35, x, y);
                boolean hh3 = ClickUtils.isClickable(s8 + 50, cy, s8 + 100, cy + 35, x, y);
                RenderUtils.drawRect(s8, cy, s8 + 50, cy + 35, 霥瀳놣㠠釒(hh2 ? new Color(71,108,181).getRGB() : new Color(82,125,212).getRGB(), myAlpha));
                RenderUtils.drawRect(s8 + 50, cy, s8 + 100, cy + 35, 霥瀳놣㠠釒(hh3 ? new Color(184,69,69).getRGB() : new Color(206,85,85).getRGB(), myAlpha));
                JelloFontUtil.jelloFont20.drawNoBSString("Rename", s8 + 7, cy + 14, -1);
                JelloFontUtil.jelloFont20.drawNoBSString("Delete", s8 + 10 + 50, cy + 14, -1);
                RenderUtils.drawRect(sx - 125 + 12.5f - 7f + offsetX, cy, sx - 12.5f + 7f + offsetX, cy + 35, 霥瀳놣㠠釒(-16711423, 0.03f * config.alpha * myAlpha));
                if (n3 == 1) {
                    JelloFontUtil.jelloFont24.drawNoBSString(config.name, sx - 125 + 12.5f - 3f + 6 + offsetX, cy + 12, 霥瀳놣㠠釒(-16711423, 0.8f * myAlpha));
                } else {
                    JelloFontUtil.jelloFont24.drawSmoothString(config.name, sx - 125 + 12.5f - 3f + 6 + offsetX, cy + 12, 霥瀳놣㠠釒(-16711423, 0.8f * myAlpha));
                }

                if (ConfigManager.currentProfile.equals(config.name)) {
                    RenderUtils.drawTextureLocationZoom(sx - 20 - 2 + offsetX, cy + 17.5f - 13 / 2f * 0.5f - 1, 17f * 0.5f, 13f * 0.5f, "alt/active", new Color(霥瀳놣㠠釒(-65794, myAlpha * n), true));
                }
//                RenderUtils.drawRect(sx - 125 + 12.5f - 3f, sy - 250 + 30 + 5 + 3f, sx - 12.5f + 3f, sy, -1);
                cy += 35;
            }
            StencilUtil.uninitStencilBuffer();
            GL11.glPopMatrix();

            float addAlpha = 欫좯콵甐鶲㥇(addProfile.getAnim() * alpha * myAlpha, 0.2, 0.4, 0.4, 1.0);
            float addAlpha2 = alpha * myAlpha;
            float xx = sy - 250 + 30 + 4 + 100 * Math.min(addAlpha, 1), xx2 = sx - 125;
            if (!ClickUtils.isClickable(sx - 125, sy - 250, sx, sy - 250 + 30 + 4 + 100, x, y)) {
                showAdd = false;
            }
            RenderUtils.drawRect(xx2, sy - 250 + 30 + 4, sx, xx, 霥瀳놣㠠釒(new Color(244, 244, 244).getRGB(), addAlpha2));

            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect(xx2, sy - 250 + 30 + 4, sx, xx, -1);
            StencilUtil.readStencilBuffer(1);
            if (dupe != null) {
                dupe.myRender(x, y, new Color(59, 153, 253), addAlpha2);
            }
            if (blank != null) {
                blank.myRender(x, y,new Color(59, 153, 253) , addAlpha2);
            }
            JelloFontUtil.jelloFont14.drawNoBSCenteredString("No Default Profiles Availble", xx2 + (sx - xx2) / 2f, sy - 250 + 30 + 4 + 50, 霥瀳놣㠠釒(new Color(50, 50, 50).getRGB(), 0.5f * addAlpha2));
            StencilUtil.uninitStencilBuffer();
//            if (offsetY != 0) {
            RenderUtils.drawCustomShader2(xx2, xx, xx2 + 125, 0, addAlpha * 0.3f, 15);
//            }

            GL11.glPopMatrix();
        }
//        RenderUtils.drawRoundedRect(sx - 125, sy - 250, sx, sy, 6, ColorUtils.reAlpha(new Color(244,244,244), a).getRGB());
    }
    public void typeKet(char a, int b){
        if(reNameConfig != null) {
            if((a >= 'A' && a <= 'z') || a == ' ' || a == '.' || (a >= '0' && a <= '9')) {
                edit.charTyped(a, b);
            }
        }
    }
    public boolean keyPress(int a, int b, int c){
        if(reNameConfig != null)
            edit.keyPressed(a, b, c);
        if(renaming){
            if(a == 257){
                File f = SigmaNG.getSigmaNG().configManager.getConfigFile(reNameConfig.name);
                File f2 = SigmaNG.getSigmaNG().configManager.getConfigFile(edit.getText());
                boolean l = ConfigManager.currentProfile.equals(reNameConfig.name);
                if(!f2.exists()){
                    f.renameTo(f2);
                    read();
                }
                if(l){
                    ConfigManager.currentProfile = edit.getText();
                }

                renaming = false;
                edit.setVisible(false);
                edit.setText("");
                reNameConfig = null;
            }
        }
        return false;
    }
    public boolean click(double x, double y, int b){
        if(renaming) return false;
        ScaledResolution sr = new ScaledResolution(mc);
        float w = sr.getScaledWidth(), h = sr.getScaledHeight();
        float mw = 110 / 4f, mh = 82 / 4f;
        float sx = (float) (w - mw - 7 + mw), sy = (float) (h - mh - 7.5f + mh);
        float cy = sy - 250 + 30 + 5.5f + 5 - scroll;
        if(ClickUtils.isClickableWithRect(w - mw - 7, h - mh - 7.5f, mw, mh, x, y) && !show){
            show = true;
            sss = true;
            return true;
        }
        if(show) {
            add.mouseClicked(x, y, b);
        }
        if(showAdd){
            blank.mouseClicked(x,y,b);
            dupe.mouseClicked(x,y,b);
        }

        float s8 = sx - 12.5f + 7f - 100;
        if(ClickUtils.isClickable(sx - 125 + 12.5f - 7f, sy - 250 + 30 + 5 + 3f, sx - 12.5f + 7f, sy, x, y)) {
            for (Config config : configs) {
                if(config.show) {
                    if (!config.name.equals(ConfigManager.currentProfile)) {
                        if (ClickUtils.isClickable(s8 + 50, cy, s8 + 100, cy + 35, x, y)) {
                            // delete
                            File f = SigmaNG.getSigmaNG().configManager.getConfigFile(config.name);
                            f.delete();
                            read();
                            return true;
                        }
                    }
                    if (ClickUtils.isClickable(s8, cy, s8 + 50, cy + 35, x, y)) {
                        edit.setText("");
                        edit.setVisible(true);
                        edit.setFocused2(true);
                        config.show = false;
                        reNameConfig = config;
                        showAdd = false;
                        renaming = true;
                        // rename
                        return true;
                    }
                }
                cy += 35;
            }
        }
        cy = sy - 250 + 30 + 5.5f + 5 - scroll;
        if(show && !showAdd) {
            if(ClickUtils.isClickable(sx - 125 + 12.5f - 7f, sy - 250 + 30 + 5 + 3f, sx - 12.5f + 7f, sy, x, y)) {
                for (Config config : configs) {
                    if (ClickUtils.isClickable(sx - 125 + 12.5f - 7f, cy, sx - 12.5f + 7f, cy + 35, x, y)) {
                        if(b == 0) {
                            SigmaNG.getSigmaNG().configManager.saveDefaultConfig();
                            ConfigManager.currentProfile = config.name;
                            SigmaNG.getSigmaNG().configManager.saveLastConfigData();
                            SigmaNG.getSigmaNG().configManager.loadDefaultConfig();
                        }else if(b == 1){
                            config.show = !config.show;
                        }
                        return true;
                    }
                    cy += 35;
                }
            }
        }
        return ClickUtils.isClickable(sx - 125 + 12.5f - 7f, sy - 250 + 30 + 5 + 3f, sx - 12.5f + 7f, sy, x, y);
    }

    public void setScroll(double x, double y, double delta) {
        ScaledResolution sr = new ScaledResolution(mc);
        float w = sr.getScaledWidth(), h = sr.getScaledHeight();
        float mw = 110 / 4f, mh = 82 / 4f;
        float sx = (float) (w - mw - 7 + mw), sy = (float) (h - mh - 7.5f + mh);
        float cy = sy - 250 + 30 + 5.5f + 5 - scroll;
        if(ClickUtils.isClickable(sx - 125, sy - 250, sx, sy, x, y)) {
            if (delta != 0) {
                scroll += delta > 0 ? -25 : 25;
                if (scroll < 0) {
                    scroll = 0;
                }
                if (scroll > maxScroll) {
                    scroll = maxScroll;
                }
            }
        }
    }
    public void reset() {
        reNameConfig = null;
        sss = false;
        scroll = 0;
        renaming = false;
        ScaledResolution sr = new ScaledResolution(mc);
        float w = sr.getScaledWidth(), h = sr.getScaledHeight();
        float mw = 110 / 4f, mh = 82 / 4f;
        float sx = (float) (w - mw - 7 + mw), sy = (float) (h - mh - 7.5f + mh);

        add = new ConfigButton((int) (sx - 20), (int) (sy - 250 + 2), 10, 10, "+", (n)->{
            showAdd = true;
        }, JelloFontUtil.jelloFont25);
        dupe = new ConfigButton((int) (sx - 20 - 15), (int) (sy - 250 + 2 + 30), 10, 10, "Duplicate", (n)->{
            int count = 2;
            File dupeFile = new File(ConfigManager.configDir, ConfigManager.currentProfile + " ("+count+").profile");
            while(dupeFile.exists()){
                count ++;
                dupeFile = new File(ConfigManager.configDir, ConfigManager.currentProfile + " ("+count+").profile");
            }
            SigmaNG.getSigmaNG().configManager.saveDupeConfig(ConfigManager.currentProfile + " ("+count+")");
            read();
        }, JelloFontUtil.jelloFont20);
        blank = new ConfigButton((int) (sx - 125 + 20), (int) (sy - 250 + 2 + 30), 10, 10, "Blank", (n)->{
            int count = 2;
            String s = "New Profile";
            File dupeFile = new File(ConfigManager.configDir, s + " ("+count+").profile");
            while(dupeFile.exists()){
                count ++;
                dupeFile = new File(ConfigManager.configDir, s + " ("+count+").profile");
            }
            SigmaNG.getSigmaNG().configManager.saveEmptyConfig(s + " ("+count+")");
            read();
        }, JelloFontUtil.jelloFont20);
        scale = new Sigma5AnimationUtil(300, 100);
        addProfile = new Sigma5AnimationUtil(290, 290);
        read();
    }
    public void read(){
        configs.clear();
        new Thread(()->{
            for(File f : Objects.requireNonNull(ConfigManager.configDir.listFiles())){
                if(f.isFile()){
                    if(f.getName().endsWith(".profile")){
                        String name = f.getName().substring(0, f.getName().length() - ".profile".length());
                        configs.add(new Config(name, f));
                    }
                }
            }
            maxScroll = configs.size() * 35;
            maxScroll = Math.max(0, maxScroll - 250);
        }).start();
    }
}
