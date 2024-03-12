// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.ui.clickguis.dropdown.DropdownClickGUI;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.utils.misc.HoveringUtil;
import java.util.Iterator;
import dev.tenacity.ui.clickguis.dropdown.components.settings.ColorComponent;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.ui.clickguis.dropdown.components.settings.StringComponent;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.ui.clickguis.dropdown.components.settings.MultipleBoolComponent;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.ui.clickguis.dropdown.components.settings.NumberComponent;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.ui.clickguis.dropdown.components.settings.ModeComponent;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.ui.clickguis.dropdown.components.settings.BooleanComponent;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.ui.clickguis.dropdown.components.settings.KeybindComponent;
import dev.tenacity.module.settings.impl.KeybindSetting;
import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.impl.EaseOutSine;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.EaseInOutQuad;
import java.util.List;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.ui.sidegui.utils.TooltipObject;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.Module;
import dev.tenacity.ui.Screen;

public class ModuleRect implements Screen
{
    public final Module module;
    private int searchScore;
    private final Animation toggleAnimation;
    private final Animation hoverAnimation;
    private final Animation hoverKeybindAnimation;
    private final Animation settingAnimation;
    public final TooltipObject tooltipObject;
    private final TimerUtil timerUtil;
    private boolean typing;
    public float x;
    public float y;
    public float width;
    public float height;
    public float panelLimitY;
    public float alpha;
    private double settingSize;
    private final List<SettingComponent> settingComponents;
    private double actualSettingCount;
    
    public ModuleRect(final Module module) {
        this.toggleAnimation = new EaseInOutQuad(300, 1.0);
        this.hoverAnimation = new EaseOutSine(400, 1.0, Direction.BACKWARDS);
        this.hoverKeybindAnimation = new DecelerateAnimation(200, 1.0, Direction.BACKWARDS);
        this.settingAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.tooltipObject = new TooltipObject();
        this.timerUtil = new TimerUtil();
        this.settingSize = 1.0;
        this.module = module;
        this.settingComponents = new ArrayList<SettingComponent>();
        for (final Setting setting : module.getSettingsList()) {
            if (setting instanceof KeybindSetting) {
                this.settingComponents.add(new KeybindComponent((KeybindSetting)setting));
            }
            if (setting instanceof BooleanSetting) {
                this.settingComponents.add(new BooleanComponent((BooleanSetting)setting));
            }
            if (setting instanceof ModeSetting) {
                this.settingComponents.add(new ModeComponent((ModeSetting)setting));
            }
            if (setting instanceof NumberSetting) {
                this.settingComponents.add(new NumberComponent((NumberSetting)setting));
            }
            if (setting instanceof MultipleBoolSetting) {
                this.settingComponents.add(new MultipleBoolComponent((MultipleBoolSetting)setting));
            }
            if (setting instanceof StringSetting) {
                this.settingComponents.add(new StringComponent((StringSetting)setting));
            }
            if (setting instanceof ColorSetting) {
                this.settingComponents.add(new ColorComponent((ColorSetting)setting));
            }
        }
    }
    
    @Override
    public void initGui() {
        this.settingAnimation.setDirection(Direction.BACKWARDS);
        this.toggleAnimation.setDirection(Direction.BACKWARDS);
        if (this.settingComponents != null) {
            this.settingComponents.forEach(Screen::initGui);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.module.isExpanded()) {
            for (final SettingComponent settingComponent : this.settingComponents) {
                if (settingComponent.getSetting().cannotBeShown()) {
                    continue;
                }
                settingComponent.keyTyped(typedChar, keyCode);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.toggleAnimation.setDirection(this.module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
        this.settingAnimation.setDirection(this.module.isExpanded() ? Direction.FORWARDS : Direction.BACKWARDS);
        final boolean hoveringModule = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        this.hoverAnimation.setDirection(hoveringModule ? Direction.FORWARDS : Direction.BACKWARDS);
        this.hoverAnimation.setDuration(hoveringModule ? 250 : 400);
        final boolean hoveringText = HoveringUtil.isHovering(this.x + 5.0f, this.y + ModuleRect.tenacityFont18.getMiddleOfBox(this.height), ModuleRect.tenacityFont18.getStringWidth(this.module.getName()), (float)ModuleRect.tenacityFont18.getHeight(), mouseX, mouseY);
        this.tooltipObject.setTip(this.module.getDescription());
        this.tooltipObject.setHovering(hoveringText);
        final Theme theme = Theme.getCurrentTheme();
        final Pair<Color, Color> colors = theme.getColors().apply((color1, color2) -> Pair.of(ColorUtil.applyOpacity(color1, this.alpha), ColorUtil.applyOpacity(color2, this.alpha)));
        Color rectColor = new Color(35, 37, 43, (int)(255.0f * this.alpha));
        final Color textColor = ColorUtil.applyOpacity(Color.WHITE, this.alpha);
        final Color moduleTextColor = ColorUtil.applyOpacity(textColor, 0.9f);
        if (this.module.isEnabled() || !this.toggleAnimation.isDone()) {
            Color toggleColor = colors.getSecond();
            if (DropdownClickGUI.gradient) {
                toggleColor = ColorUtil.interpolateColorC(ColorUtil.applyOpacity(Color.BLACK, 0.15f), ColorUtil.applyOpacity(Color.WHITE, 0.12f), this.hoverAnimation.getOutput().floatValue());
            }
            rectColor = ColorUtil.interpolateColorC(rectColor, toggleColor, this.toggleAnimation.getOutput().floatValue());
        }
        RenderUtil.resetColor();
        Gui.drawRect2(this.x, this.y, this.width, this.height, ColorUtil.interpolateColor(rectColor, ColorUtil.brighter(rectColor, 0.8f), this.hoverAnimation.getOutput().floatValue()));
        RenderUtil.resetColor();
        ModuleRect.tenacityFont20.drawString(this.module.getName(), this.x + this.width / 2.0f - ModuleRect.tenacityFont20.getStringWidth(this.module.getName()) / 2.0f, this.y + ModuleRect.tenacityFont20.getMiddleOfBox(this.height), moduleTextColor);
        final Color settingRectColor = ColorUtil.tripleColor(32, this.alpha);
        final double settingHeight = this.actualSettingCount * this.settingAnimation.getOutput();
        this.actualSettingCount = 0.0;
        if (this.module.isExpanded() || !this.settingAnimation.isDone()) {
            final float settingRectHeight = 16.0f;
            Gui.drawRect2(this.x, this.y + this.height, this.width, (float)(settingHeight * settingRectHeight), settingRectColor.getRGB());
            if (!this.settingAnimation.isDone()) {
                RenderUtil.scissorStart(this.x, this.y + this.height, this.width, settingHeight * settingRectHeight);
            }
            this.typing = false;
            for (final SettingComponent settingComponent : this.settingComponents) {
                if (settingComponent.getSetting().cannotBeShown()) {
                    continue;
                }
                settingComponent.panelLimitY = this.panelLimitY;
                settingComponent.settingRectColor = settingRectColor;
                settingComponent.textColor = textColor;
                settingComponent.clientColors = colors;
                settingComponent.alpha = this.alpha;
                settingComponent.x = this.x;
                settingComponent.y = (float)(this.y + this.height + this.actualSettingCount * settingRectHeight);
                settingComponent.width = this.width;
                settingComponent.typing = this.typing;
                if (settingComponent instanceof ModeComponent) {
                    final ModeComponent modeComponent = (ModeComponent)settingComponent;
                    modeComponent.realHeight = settingRectHeight * modeComponent.normalCount;
                }
                if (settingComponent instanceof MultipleBoolComponent) {
                    final MultipleBoolComponent multipleBoolComponent = (MultipleBoolComponent)settingComponent;
                    multipleBoolComponent.realHeight = settingRectHeight * multipleBoolComponent.normalCount;
                }
                if (settingComponent instanceof ColorComponent) {
                    final ColorComponent colorComponent = (ColorComponent)settingComponent;
                    colorComponent.realHeight = settingRectHeight;
                }
                settingComponent.height = settingRectHeight * settingComponent.countSize;
                settingComponent.drawScreen(mouseX, mouseY);
                if (settingComponent.typing) {
                    this.typing = true;
                }
                this.actualSettingCount += settingComponent.countSize;
            }
            if (!this.settingAnimation.isDone() || GL11.glIsEnabled(3089)) {
                RenderUtil.scissorEnd();
            }
        }
        this.settingSize = settingHeight;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean hoveringModule = this.isClickable(this.y, this.panelLimitY) && HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (this.module.isExpanded() && this.settingAnimation.finished(Direction.FORWARDS)) {
            for (final SettingComponent settingComponent : this.settingComponents) {
                if (settingComponent.getSetting().cannotBeShown()) {
                    continue;
                }
                settingComponent.mouseClicked(mouseX, mouseY, button);
            }
        }
        if (hoveringModule) {
            switch (button) {
                case 0: {
                    this.toggleAnimation.setDirection(this.module.isEnabled() ? Direction.BACKWARDS : Direction.FORWARDS);
                    this.module.toggleSilent();
                    break;
                }
                case 1: {
                    this.module.setExpanded(!this.module.isExpanded());
                    break;
                }
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.module.isExpanded()) {
            for (final SettingComponent settingComponent : this.settingComponents) {
                if (settingComponent.getSetting().cannotBeShown()) {
                    continue;
                }
                settingComponent.mouseReleased(mouseX, mouseY, state);
            }
        }
    }
    
    public boolean isClickable(final float y, final float panelLimitY) {
        return y > panelLimitY && y < panelLimitY + Module.allowedClickGuiHeight;
    }
    
    public int getSearchScore() {
        return this.searchScore;
    }
    
    public void setSearchScore(final int searchScore) {
        this.searchScore = searchScore;
    }
    
    public boolean isTyping() {
        return this.typing;
    }
    
    public double getSettingSize() {
        return this.settingSize;
    }
}
