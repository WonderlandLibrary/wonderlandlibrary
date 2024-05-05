/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.gui.components;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.clientsetting.clientsettings.ClickGuiConfig;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.ColorAnimation;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.StringUtil;
import com.wallhacks.losebypass.utils.Timer;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModuleComponent {
    static final ResourceLocation muted = new ResourceLocation("textures/icons/mutedicon.png");
    static final ResourceLocation unMuted = new ResourceLocation("textures/icons/unmutedicon.png");
    static final ResourceLocation drawn = new ResourceLocation("textures/icons/visibleicon.png");
    static final ResourceLocation hidden = new ResourceLocation("textures/icons/notvisibleicon.png");
    private final ColorAnimation enabledCircleHover = new ColorAnimation(new Color(0, 0, 0, 0), 0.01f);
    private final ColorAnimation enabledColorAnimation = new ColorAnimation(new Color(0x232323), 0.0075f);
    private final Animation openAnimation = new Animation(0.0f, 0.005f);
    public Module module;
    Timer descriptionTime = new Timer().reset();
    double renderX;
    double renderY;
    boolean open = false;
    boolean listening = false;
    ArrayList<SettingComponent> components = new ArrayList();

    public ModuleComponent(Module module) {
        this.module = module;
        this.components.addAll(GuiUtil.getComponents(module));
    }

    public void setPosition(double posX, double posY) {
        this.renderY = posY;
        this.renderX = posX;
    }

    /*
     * Unable to fully structure code
     */
    public int drawComponent(double posX, double posY, int smoothScroll, double deltaTime, int click, int mouseX, int mouseY) {
        if (this.renderX > posX) {
            this.renderX = Math.max(posX, this.renderX - deltaTime);
        } else if (this.renderX < posX) {
            this.renderX = Math.min(posX, this.renderX + deltaTime);
        }
        if (this.renderY > posY) {
            this.renderY = Math.max(posY, this.renderY - deltaTime);
        } else if (this.renderY < posY) {
            this.renderY = Math.min(posY, this.renderY + deltaTime);
        }
        if (!((double)mouseX > this.renderX) || !((double)mouseX < this.renderX + 200.0) || !((double)mouseY > this.renderY - (double)smoothScroll)) ** GOTO lbl-1000
        v0 = mouseY;
        v1 = this.renderY - (double)smoothScroll + 30.0;
        v2 = this.open != false ? 3 : 0;
        if (v0 < v1 - (double)v2) {
            v3 = true;
        } else lbl-1000:
        // 2 sources

        {
            v3 = insideCircle = false;
        }
        if (insideCircle) {
            if (this.descriptionTime.passedMs(700L)) {
                ClickGui.info.setInfo(this.module.getDescription(), this.module.getName());
            }
            if (click == 0 && !this.module.isHold()) {
                this.module.toggle();
            } else if (click == 1) {
                this.open = this.open == false;
            }
        } else {
            this.descriptionTime.reset();
        }
        offset = 15;
        for (SettingComponent setting : this.components) {
            if (!setting.visible()) continue;
            offset += setting.getHeight();
        }
        this.openAnimation.update(this.open != false ? 1.0f : 0.0f, deltaTime);
        renderOffset = (int)(this.openAnimation.value() * (float)offset);
        GuiUtil.rounded((int)this.renderX, (int)this.renderY - smoothScroll, (int)this.renderX + 201, (int)this.renderY + 31 - smoothScroll + renderOffset, new Color(0, 0, 0, 30).getRGB(), 5);
        GuiUtil.rounded((int)this.renderX, (int)this.renderY - smoothScroll, (int)this.renderX + 200, (int)this.renderY + 30 - smoothScroll + renderOffset, ClickGui.background3(), 5);
        LoseBypass.fontManager.getThickFont().drawString(this.module.getName(), (int)this.renderX + 5, (int)this.renderY + 10 - smoothScroll, -1);
        GuiUtil.setup(new Color(0, 0, 0, 50).getRGB());
        GuiUtil.corner((float)(this.renderX + 186.5), (float)this.renderY - (float)smoothScroll + 15.5f, 4.0, 0, 360);
        GuiUtil.finish();
        this.enabledColorAnimation.update(this.module.isEnabled() != false ? ClickGuiConfig.getInstance().getMainColor() : new Color(0x232323), deltaTime);
        GuiUtil.setup(this.enabledColorAnimation.value().getRGB());
        GuiUtil.corner((int)this.renderX + 186, (int)this.renderY - smoothScroll + 15, 4.0, 0, 360);
        GuiUtil.finish();
        target = insideCircle != false ? new Color(100, 100, 100, 50) : new Color(0, 0, 0, 0);
        this.enabledCircleHover.update(target, deltaTime);
        GuiUtil.setup(this.enabledCircleHover.value().getRGB());
        GuiUtil.corner((int)this.renderX + 186, (int)this.renderY - smoothScroll + 15, 4.0, 0, 360);
        GuiUtil.finish();
        max = (double)ClickGui.minOffset > this.renderY - (double)smoothScroll + 25.0 ? ClickGui.maxOffset : (int)Math.max((double)(ClickGui.minOffset + ClickGui.maxOffset) - (this.renderY - (double)smoothScroll + 25.0), 0.0);
        min = Math.max((int)this.renderY - smoothScroll + 25, ClickGui.minOffset);
        if (renderOffset == 0) return 35 + renderOffset;
        currentY = (int)((double)(-offset + renderOffset) + this.renderY + 25.0);
        ClickGui.maxOffset = max;
        ClickGui.minOffset = min;
        GuiUtil.glScissor((int)posX, ClickGui.minOffset, 200, ClickGui.maxOffset);
        GuiUtil.drawRect((float)this.renderX, currentY - smoothScroll, (float)this.renderX + 200.0f, (float)this.renderY + (float)renderOffset - (float)smoothScroll + 10.0f, ClickGui.background2());
        hoverBind = false;
        hoverHoldToggle = false;
        hoverMute = false;
        hoverDrawn = false;
        if ((double)mouseX > this.renderX && (double)mouseX < this.renderX + 200.0 && mouseY > currentY - smoothScroll + renderOffset - 15 && mouseY < currentY - smoothScroll + renderOffset + 5) {
            if ((double)mouseX < this.renderX + 34.0) {
                hoverHoldToggle = this.module.allowHold();
                if (click == 0 && hoverHoldToggle) {
                    this.module.setHold(this.module.isHold() == false);
                }
            } else if ((double)mouseX < this.renderX + 80.0) {
                hoverBind = true;
                if (click >= 0) {
                    if (this.listening) {
                        this.key(-2 - click);
                    } else {
                        this.listening = true;
                    }
                }
            } else if ((double)mouseX > this.renderX + 160.0) {
                if ((double)mouseX < this.renderX + 180.0) {
                    if (click == 0) {
                        this.module.setMuted(this.module.isMuted() == false);
                    }
                    hoverMute = true;
                } else {
                    if (click == 0) {
                        this.module.setVisible(this.module.isVisible() == false);
                    }
                    hoverDrawn = true;
                }
            }
        }
        if (!hoverBind && click >= 0) {
            this.listening = false;
        }
        GuiUtil.setup(hoverHoldToggle != false ? ClickGui.background6() : ClickGui.background4());
        GL11.glVertex2d((double)(this.renderX + 34.0), (double)(currentY - smoothScroll + renderOffset + 5));
        GL11.glVertex2d((double)(this.renderX + 34.0), (double)(currentY - smoothScroll + renderOffset - 15));
        GL11.glVertex2d((double)this.renderX, (double)(currentY - smoothScroll + renderOffset - 15));
        GuiUtil.corner((int)(this.renderX + 5.0), currentY - smoothScroll + renderOffset, 5.0, 270, 360);
        GuiUtil.finish();
        holdToggle = this.module.isHold() != false ? "Hold:" : "Toggle:";
        mid = 17 - LoseBypass.fontManager.getTextWidth(holdToggle) / 2;
        LoseBypass.fontManager.drawString(holdToggle, (int)this.renderX + mid, currentY - smoothScroll + renderOffset - 8, -1);
        GuiUtil.drawRect((float)this.renderX + 34.0f, currentY - smoothScroll + renderOffset + 5, (float)this.renderX + 80.0f, currentY - smoothScroll + renderOffset - 15, hoverBind != false ? ClickGui.background6() : ClickGui.background4());
        bind = this.listening != false ? "Listening" + GuiUtil.getLoadingText(false) : StringUtil.getNameForKey(this.module.getBind());
        mid = this.listening == false ? 57 - LoseBypass.fontManager.getTextWidth(bind) / 2 : 37;
        LoseBypass.fontManager.drawString(bind, (int)this.renderX + mid, currentY - smoothScroll + renderOffset - 8, -1);
        GuiUtil.drawRect((float)(this.renderX + 80.0), currentY - smoothScroll + renderOffset + 5, (float)this.renderX + 160.0f, currentY - smoothScroll + renderOffset - 15, ClickGui.background4());
        GuiUtil.drawRect((float)this.renderX + 160.0f, currentY - smoothScroll + renderOffset + 5, (float)this.renderX + 180.0f, currentY - smoothScroll + renderOffset - 15, hoverMute != false ? ClickGui.background6() : ClickGui.background4());
        GuiUtil.drawCompleteImage(this.renderX + 163.0, currentY - smoothScroll + renderOffset - 12, 14.0, 14.0, this.module.isMuted() != false ? ModuleComponent.muted : ModuleComponent.unMuted, Color.WHITE);
        GuiUtil.setup(hoverDrawn != false ? ClickGui.background6() : ClickGui.background4());
        GuiUtil.corner((int)(this.renderX + 195.0), currentY - smoothScroll + renderOffset, 5.0, 0, 90);
        GL11.glVertex2d((double)(this.renderX + 200.0), (double)(currentY - smoothScroll + renderOffset - 15));
        GL11.glVertex2d((double)(this.renderX + 180.0), (double)(currentY - smoothScroll + renderOffset - 15));
        GL11.glVertex2d((double)(this.renderX + 180.0), (double)(currentY - smoothScroll + renderOffset + 5));
        GuiUtil.finish();
        GuiUtil.drawCompleteImage(this.renderX + 183.0, currentY - smoothScroll + renderOffset - 12, 14.0, 14.0, this.module.isVisible() != false ? ModuleComponent.drawn : ModuleComponent.hidden, Color.WHITE);
        var25_23 = this.components.iterator();
        while (var25_23.hasNext() != false) {
            setting = var25_23.next();
            if (!setting.visible()) continue;
            if (currentY - smoothScroll > ClickGui.minOffset + ClickGui.maxOffset) {
                return 35 + renderOffset;
            }
            if (setting.getHeight() + currentY - smoothScroll < ClickGui.minOffset) {
                currentY += setting.getHeight();
                continue;
            }
            GuiUtil.glScissor((int)this.renderX, ClickGui.minOffset, 200, ClickGui.maxOffset);
            currentY += setting.drawComponent((int)this.renderX, currentY - smoothScroll, deltaTime, this.openAnimation.done() != false ? click : -1, mouseX, mouseY, this.openAnimation.done() == false);
        }
        return 35 + renderOffset;
    }

    void key(int k) {
        this.listening = false;
        if (k == 211) {
            this.module.setBind(0);
            return;
        }
        if (k == 1) return;
        this.module.setBind(k);
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.components.forEach(settingComponent -> settingComponent.keyTyped(typedChar, keyCode));
        if (!this.listening) return;
        this.key(keyCode);
    }
}

