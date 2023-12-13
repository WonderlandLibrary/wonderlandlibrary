package de.tired.base.guis.newclickgui.renderables.setting;

import de.tired.base.guis.newclickgui.abstracts.ClickGUIHandler;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.util.render.RenderUtil;
import de.tired.util.render.Translate;
import de.tired.base.font.FontManager;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;

import java.awt.*;

public class RenderableSelector extends ClickGUIHandler {

    public String[] settings;
    private int modeIndex;

    private boolean extended;

    private Animation fadeInAlpha = new Animation();

    private int x, y;

    private Animation textFadeInAnimation = new Animation();

    public float float3;

    private Animation movingAnimation2 = new Animation();

    public Translate translateText;

    public ModeSetting modeSetting;

    public RenderableSelector(ModeSetting setting) {
        this.modeSetting = setting;
        this.modeIndex = 0;
        translateText = new Translate(0, 0);
    }

    public void onReset() {

        translateText.interpolate(0, 0, 5);
        textFadeInAnimation.setValue(.5f);
        fadeInAlpha.setValue(0);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {

    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseKey) {
        if (isHovered(mouseX, mouseY, x, y, width, height)) {
            int maxIndex = modeSetting.getOptions().length;

            if (mouseKey == 0) {

                if (modeIndex + 1 >= maxIndex && modeIndex - 1 < 0) {
                    modeIndex = 0;
                } else {
                    modeIndex++;
                }

            }
            if (mouseKey == 1) {
                if (modeIndex + 1 >= maxIndex && modeIndex - 1 < 0)
                    modeIndex = 0;
                else
                    modeIndex--;

            }
            if (maxIndex <= 1) return;
            if (modeIndex >= maxIndex) modeIndex = 0;
            if (modeIndex < 0) modeIndex = maxIndex - 1;

            modeSetting.setValue(modeSetting.getOptions()[modeIndex]);
            modeSetting.setModeIndex(modeIndex);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int x, int y) {
        this.x = x;
        this.y = y;

        this.height = 16;

        fadeInAlpha.update();
        fadeInAlpha.animate(255, .1, Easings.BOUNCE_IN);
        String name = modeSetting.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        movingAnimation2.update();

        movingAnimation2.animate(FontManager.interMedium14.getStringWidth(name) + 20, 5, true);

        if (Math.round(movingAnimation2.getValue()) >= FontManager.interMedium14.getStringWidth(name) + 19)
            movingAnimation2.setValue(-FontManager.interMedium14.getStringWidth(name));


        textFadeInAnimation.update();
        textFadeInAnimation.animate(0, .1, Easings.NONE);

        RenderUtil.instance.doRenderShadow(Color.BLACK, x + 11, y + 6, FontManager.interMedium10.getStringWidth(name + " : " + modeSetting.getValue()), 5, 35);
        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + 8, y + 3, FontManager.interMedium10.getStringWidth(name + " :" + modeSetting.getValue()) + 7, 10, 2, modeSetting.getModule().isState() ? new Color(194, 194, 194, 142) : new Color(26, 26, 26));

        FontManager.interMedium10.drawString(name + " : " + modeSetting.getValue(), x + 11, y + 9, -1);


    }
}
