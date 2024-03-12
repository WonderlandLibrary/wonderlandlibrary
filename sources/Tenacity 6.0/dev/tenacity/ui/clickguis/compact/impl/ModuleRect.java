// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.compact.impl;

import dev.tenacity.module.settings.impl.KeybindSetting;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.font.FontUtil;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import org.lwjgl.input.Keyboard;
import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.render.HUDMod;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.ui.sidegui.utils.TooltipObject;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.Module;
import dev.tenacity.ui.Screen;

public class ModuleRect implements Screen
{
    private int searchScore;
    public final Module module;
    private final Animation enableAnimation;
    private final SettingComponents settingComponents;
    public float x;
    public float y;
    public float width;
    public float height;
    public float rectHeight;
    public float rectWidth;
    public Module binding;
    public boolean typing;
    public final TooltipObject tooltipObject;
    private final TimerUtil timerUtil;
    
    public ModuleRect(final Module module) {
        this.enableAnimation = new DecelerateAnimation(150, 1.0);
        this.tooltipObject = new TooltipObject();
        this.timerUtil = new TimerUtil();
        this.module = module;
        this.settingComponents = new SettingComponents(module);
    }
    
    @Override
    public void initGui() {
        this.settingComponents.initGui();
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.binding != null) {
            if (keyCode == 57 || keyCode == 1 || keyCode == 211) {
                this.binding.getKeybind().setCode(0);
            }
            else {
                this.binding.getKeybind().setCode(keyCode);
            }
            this.binding = null;
        }
        else {
            this.settingComponents.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.typing = false;
        Gui.drawRect2(this.x, this.y, this.rectWidth, 20.0, new Color(39, 39, 39).getRGB());
        final HUDMod hudMod = Tenacity.INSTANCE.getModuleCollection().getModule(HUDMod.class);
        final Pair<Color, Color> colors = HUDMod.getClientColors();
        final Color actualColor = colors.getFirst();
        if (this.binding != null && !this.typing) {
            this.typing = true;
        }
        ModuleRect.tenacityBoldFont20.drawString(this.module.getName(), this.x + 5.0f, this.y + ModuleRect.tenacityBoldFont20.getMiddleOfBox(20.0f), -1);
        final KeybindSetting keybindSetting = this.module.getKeybind();
        final float bindWidth = ModuleRect.tenacityFont14.getStringWidth(Keyboard.getKeyName(keybindSetting.getCode())) + 4.0f;
        final boolean hovered = HoveringUtil.isHovering(this.x + ModuleRect.tenacityFont20.getStringWidth(this.module.getName()) + 13.0f, this.y + 6.0f, bindWidth, 8.0f, mouseX, mouseY);
        final boolean hoveringModule = HoveringUtil.isHovering(this.x, this.y, this.width, 20.0f, mouseX, mouseY);
        if (!hoveringModule) {
            this.timerUtil.reset();
        }
        this.tooltipObject.setTip(this.module.getDescription());
        this.tooltipObject.setRound(false);
        this.tooltipObject.setHovering(this.timerUtil.hasTimeElapsed(900L));
        final Color bindRect = new Color(64, 68, 75);
        Gui.drawRect2(this.x + ModuleRect.tenacityFont20.getStringWidth(this.module.getName()) + 13.0f, this.y + 6.0f, ModuleRect.tenacityFont14.getStringWidth(Keyboard.getKeyName(keybindSetting.getCode())) + 4.0f, 8.0, hovered ? bindRect.brighter().getRGB() : bindRect.getRGB());
        ModuleRect.tenacityFont14.drawCenteredString(Keyboard.getKeyName(keybindSetting.getCode()), this.x + ModuleRect.tenacityFont20.getStringWidth(this.module.getName()) + 13.0f + bindWidth / 2.0f, this.y + 8.0f, -1);
        Gui.drawRect2(this.x, this.y + 20.0f, this.rectWidth, this.rectHeight, new Color(35, 35, 35).getRGB());
        this.enableAnimation.setDirection(this.module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
        final float o = this.enableAnimation.getOutput().floatValue();
        RenderUtil.drawGoodCircle(this.x + this.rectWidth - 10.0f, this.y + 10.0f, 4.0f, ColorUtil.interpolateColor(new Color(64, 68, 75), actualColor, o));
        GlStateManager.pushMatrix();
        GlStateManager.translate(3.5f, 2.0f, 0.0f);
        GlStateManager.scale(o, o, o);
        GlStateManager.translate(-3.5f, -2.0f, 0.0f);
        FontUtil.iconFont16.drawString("o", (this.x + this.rectWidth - 13.5f) / o, (this.y + 8.0f) / o, ColorUtil.interpolateColor(new Color(45, 45, 45), Color.WHITE, o));
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        this.settingComponents.x = this.x;
        this.settingComponents.y = this.y + 20.0f;
        this.settingComponents.actualColor = actualColor;
        this.settingComponents.rectWidth = this.rectWidth;
        this.settingComponents.drawScreen(mouseX, mouseY);
        if (!this.typing) {
            this.typing = this.settingComponents.typing;
        }
        this.rectHeight = ((this.settingComponents.size > 0.0f) ? this.settingComponents.size : 0.0f);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final float bindWidth = ModuleRect.tenacityFont14.getStringWidth(Keyboard.getKeyName(this.module.getKeybind().getCode())) + 4.0f;
        final boolean hovered = HoveringUtil.isHovering(this.x + ModuleRect.tenacityFont20.getStringWidth(this.module.getName()) + 13.0f, this.y + 6.0f, bindWidth, 8.0f, mouseX, mouseY);
        if (!hovered && HoveringUtil.isHovering(this.x, this.y, this.rectWidth, 20.0f, mouseX, mouseY)) {
            if (button == 0) {
                this.module.toggleSilent();
            }
        }
        else if (hovered) {
            this.binding = this.module;
            return;
        }
        this.settingComponents.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.settingComponents.mouseReleased(mouseX, mouseY, state);
    }
    
    public int getSearchScore() {
        return this.searchScore;
    }
    
    public void setSearchScore(final int searchScore) {
        this.searchScore = searchScore;
    }
}
