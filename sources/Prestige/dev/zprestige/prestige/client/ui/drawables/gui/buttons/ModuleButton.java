package dev.zprestige.prestige.client.ui.drawables.gui.buttons;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.Setting;
import dev.zprestige.prestige.client.setting.impl.BindSetting;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.ColorSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import dev.zprestige.prestige.client.setting.impl.StringSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.Drawable;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.BindButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.BooleanButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.ColorButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.DragButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.FloatButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.IntButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.ModeButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.MultipleButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.StringButton;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import dev.zprestige.prestige.client.util.impl.animation.Animation;
import dev.zprestige.prestige.client.util.impl.animation.Easing;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;

public class ModuleButton extends Drawable {
    public Module module;
    public ArrayList<SettingsDrawable> settings = new ArrayList<>();
    public float enabled;
    public Animation animation;
    public float animationTime;
    public boolean expanded;

    public ModuleButton(Module module, float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4, module.getDescription());
        this.module = module;
        enabled = module.isEnabled() ? 1 : 0;
        animation = new Animation(500, false, Easing.BACK_OUT);
        animationTime = module.isEnabled() ? 1 : 0;
        for (Setting<?> setting : module.getModuleSettings()) {
            if (setting instanceof BindSetting) {
                settings.add(new BindButton((BindSetting)setting, f, f2, f3, f4));
            }
        }
        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                settings.add(new BooleanButton((BooleanSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof FloatSetting) {
                settings.add(new FloatButton((FloatSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof IntSetting) {
                settings.add(new IntButton((IntSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof ColorSetting) {
                settings.add(new ColorButton((ColorSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof ModeSetting) {
                settings.add(new ModeButton((ModeSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof BindSetting) {
                settings.add(new BindButton((BindSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof MultipleSetting) {
                settings.add(new MultipleButton((MultipleSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof StringSetting) {
                settings.add(new StringButton((StringSetting)setting, f, f2, f3, f4));
            }
            if (setting instanceof DragSetting) {
                settings.add(new DragButton((DragSetting)setting, f, f2, f3, f4));
            }
        }
    }

    @Override
    public void render(int n, int n2, float f, float f2) {
        boolean bl = isInsideCross(n, n2);
        if (bl) {
            Interface.setHover(this);
        }
        enabled = MathUtil.interpolate(enabled, module.isEnabled() ? (bl ? 0.8f : 1) : (bl ? 0.2f : 0.0f), Interface.getDeltaTime() * 0.005f);
        animationTime = MathUtil.interpolate(animationTime, module.isEnabled() ? (bl ? 0.8f : 1) : (bl ? 0.6f : 0.4f), Interface.getDeltaTime() * 0.005f);
        animation.setState(expanded);
        RenderUtil.renderColoredQuad(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(-1, f2));
        RenderUtil.renderColoredQuad(getX(), getY() + 15.0f, getX() + getWidth(), getY() + 15.0f + 5.0f * animation.getAnimationFactor(), true, true, false, false, f2 * animation.getAnimationFactor() * 0.25f);
        RenderUtil.renderColoredQuad(getX(), getY(), getX() + getWidth(), getY() + 15.0f, RenderUtil.getColor(3, f2));
        RenderUtil.renderRoundedRectOutline(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(new Color(0, 0, 0), f2 * 0.4f), 0.0f);
        RenderUtil.renderRoundedRectOutline(getX() - 0.5f, getY() - 0.5f, getX() + getWidth() + 0.5f, getY() + getHeight() + 0.5f, RenderUtil.getColor(new Color(0, 0, 0), f2 * 0.3f), 0.0f);
        RenderUtil.renderColoredQuad(getX(), getY(), getX() + getWidth(), getY() + 15.0f, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), enabled * f2));
        Prestige.Companion.getFontManager().getFontRenderer().drawString(module.getName(), getX() + 2.5f, getY() + 5.0f - Prestige.Companion.getFontManager().getFontRenderer().getStringHeight() / 2.0f, RenderUtil.getColor(animationTime, f2));
        RenderUtil.renderArrows(getX() + getWidth() - 9.5f, getY() + 8, 1 - animation.getAnimationFactor(), RenderUtil.getColor(new Color(0, 0, 0, 150), f2));
        RenderUtil.renderArrows(getX() + getWidth() - 10, getY() + 7.5f, 1 - animation.getAnimationFactor(), RenderUtil.getColor(Color.WHITE, f2));
        float f7 = 5.0f;
        if (animation.getAnimationFactor() > 0.001f) {
            for (SettingsDrawable settingsDrawable : settings) {
                if (settingsDrawable.isVisible()) {
                    RenderUtil.setScissorRegion(getX(), getY(), getX() + getWidth(), getY() + getHeight());
                    settingsDrawable.setX(getX() + 5);
                    settingsDrawable.setY(getY() + 15 + f7);
                    settingsDrawable.setWidth(getWidth() - 10);
                    settingsDrawable.renderButton(n, n2, f, f2, getX(), getY(), getX() + getWidth(), getY() + getHeight());
                    GL11.glDisable(3089);
                    f7 += settingsDrawable.getHeight() + 5;
                }
            }
        }
        setHeight(15 + f7 * animation.getAnimationFactor());
    }

    @Override
    public void mouseReleased(double d, double d2, int n) {
        if (this.expanded) {
            for (SettingsDrawable settingsDrawable : settings) {
                settingsDrawable.mouseReleased(d, d2, n);
            }
        }
    }

    @Override
    public void charTyped(char c, int n) {
        if (this.expanded) {
            for (SettingsDrawable settingsDrawable : settings) {
                settingsDrawable.charTyped(c, n);
            }
        }
    }

    @Override
    public void keyPressed(int n, int n2, int n3) {
        if (this.expanded) {
            for (SettingsDrawable settingsDrawable : settings) {
                settingsDrawable.keyPressed(n, n2, n3);
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        for (SettingsDrawable settingsDrawable : settings) {
            settingsDrawable.clear();
        }
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0 && isInsideCross((int)d, (int)d2)) {
            module.toggle();
        }
        if (n == 1 && isInsideCross((int)d, (int)d2)) {
            expanded = !expanded;
        }
        if (expanded) {
            for (SettingsDrawable settingsDrawable : settings) {
                settingsDrawable.mouseClicked(d, d2, n);
            }
        }
    }

    public boolean hasModule(String string) {
        if (module.getName().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT))) return true;
        if (settings != null && settings.isEmpty()) return false;
        for (SettingsDrawable settingsDrawable : settings) {
            if (settingsDrawable.getName().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + 15.0f;
    }

}
