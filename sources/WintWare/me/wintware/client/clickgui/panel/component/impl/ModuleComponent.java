/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.wintware.client.clickgui.panel.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.wintware.client.Main;
import me.wintware.client.clickgui.panel.AnimationState;
import me.wintware.client.clickgui.panel.Panel;
import me.wintware.client.clickgui.panel.component.Component;
import me.wintware.client.clickgui.panel.component.impl.BoolOptionComponent;
import me.wintware.client.clickgui.panel.component.impl.EnumOptionComponent;
import me.wintware.client.clickgui.panel.component.impl.NumberOptionComponent;
import me.wintware.client.clickgui.panel.component.impl.VisibleComponent;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.module.Module;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public final class ModuleComponent
extends Component {
    public final List components = new ArrayList();
    private final ArrayList<Component> children = new ArrayList();
    private static final Color BACKGROUND_COLOR = new Color(23, 23, 23);
    private final Module module;
    private int opacity = 120;
    private int childrenHeight;
    private double scissorBoxHeight;
    private AnimationState state = AnimationState.STATIC;
    private boolean binding;
    private float activeRectAnimate = 0.0f;
    public float animation = 0.0f;
    int onlySettingsY = 0;

    public ModuleComponent(Module module, Panel parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
        this.module = module;
        int y2 = height;
        boolean i = false;
        if (Main.instance.setmgr.getSettingsByMod(module) != null) {
            for (Setting s : Main.instance.setmgr.getSettingsByMod(module)) {
                if (s.isCombo()) {
                    this.children.add(new EnumOptionComponent(s, this.getPanel(), x, y + y2, width, height));
                    y2 += height + 20;
                }
                if (s.isSlider()) {
                    this.children.add(new NumberOptionComponent(s, this.getPanel(), x, y, width, 16));
                    y2 += height + 20;
                    y2 += height + 20;
                }
                if (!s.isCheck()) continue;
                this.children.add(new BoolOptionComponent(s, this.getPanel(), x, y + y2, width, height));
                y2 += height + 20;
            }
        }
        this.children.add(new VisibleComponent(module, this.getPanel(), x, y, width, height));
        this.calculateChildrenHeight();
    }

    @Override
    public double getOffset() {
        return this.scissorBoxHeight;
    }

    private void drawChildren(int mouseX, int mouseY) {
        int childY = 15;
        ArrayList<Component> children = this.children;
        int componentListSize = children.size();
        for (int i = 0; i < componentListSize; ++i) {
            Component child = children.get(i);
            if (child.isHidden()) continue;
            child.setY(this.getY() + childY);
            child.onDraw(mouseX, mouseY);
            childY += 15;
        }
    }

    private int calculateChildrenHeight() {
        int height = 0;
        ArrayList<Component> children = this.children;
        int childrenSize = children.size();
        for (int i = 0; i < childrenSize; ++i) {
            Component component = children.get(i);
            if (component.isHidden()) continue;
            height = (int)((double)(height + component.getHeight()) + component.getOffset());
        }
        return height;
    }

    @Override
    public void onDraw(int mouseX, int mouseY) {
        boolean hover;
        Panel parent = this.getPanel();
        int x = parent.getX() + this.getX();
        int y = parent.getY() + this.getY();
        int height = this.getHeight();
        int width = this.getWidth();
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        this.handleScissorBox();
        this.childrenHeight = this.calculateChildrenHeight();
        if (hovered) {
            if (this.opacity < 200) {
                this.opacity += 5;
            }
        } else if (this.opacity > 120) {
            this.opacity -= 5;
        }
        this.activeRectAnimate = AnimationUtil.animation(this.activeRectAnimate, (hover = hovered) ? 4.0f : 2.0f, 0.001f);
        int opacity = this.opacity;
        RenderUtil.drawRect(x, y, x + width, (float)((double)(y + height) + this.getOffset()), ColorUtils.getColorWithOpacity(BACKGROUND_COLOR, 255 - opacity).getRGB());
        int color = this.module.getState() ? parent.category.getColor() : new Color(opacity, opacity, opacity).getRGB();
        Minecraft.getMinecraft().fontRenderer.drawCenteredStringWithShadow(this.binding ? "Binding... Key:" + Keyboard.getKeyName(this.module.getKey()) : this.module.getName(), (float)x + 48.0f + this.activeRectAnimate, (float)y + (float)height / 1.5f - 4.0f, color);
        if (this.scissorBoxHeight > 0.0) {
            if (parent.state != AnimationState.RETRACTING) {
                RenderUtil.prepareScissorBox(x, y, x + width, (float)((double)y + Math.min(this.scissorBoxHeight, parent.scissorBoxHeight) + (double)height));
            }
            this.drawChildren(mouseX, mouseY);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (this.scissorBoxHeight > 0.0) {
            ArrayList<Component> componentList = this.children;
            int componentListSize = componentList.size();
            for (int i = 0; i < componentListSize; ++i) {
                componentList.get(i).onMouseClick(mouseX, mouseY, mouseButton);
            }
        }
        if (this.isMouseOver(mouseX, mouseY) && mouseButton == 2) {
            boolean bl = this.binding = !this.binding;
        }
        if (this.isMouseOver(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.module.toggle();
            } else if (mouseButton == 1 && !this.children.isEmpty()) {
                if (this.scissorBoxHeight > 0.0 && (this.state == AnimationState.EXPANDING || this.state == AnimationState.STATIC)) {
                    this.state = AnimationState.RETRACTING;
                } else if (this.scissorBoxHeight < (double)this.childrenHeight && (this.state == AnimationState.EXPANDING || this.state == AnimationState.STATIC)) {
                    this.state = AnimationState.EXPANDING;
                }
            }
        }
    }

    @Override
    public void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
        if (this.scissorBoxHeight > 0.0) {
            ArrayList<Component> componentList = this.children;
            int componentListSize = componentList.size();
            for (int i = 0; i < componentListSize; ++i) {
                componentList.get(i).onMouseRelease(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void onKeyPress(int typedChar, int keyCode) {
        if (this.binding) {
            this.module.setKey(keyCode);
            this.binding = false;
            if (keyCode == 211) {
                this.module.setKey(0);
            } else if (keyCode == 1) {
                this.setBinding(false);
            }
        }
        if (this.scissorBoxHeight > 0.0) {
            ArrayList<Component> componentList = this.children;
            int componentListSize = componentList.size();
            for (int i = 0; i < componentListSize; ++i) {
                componentList.get(i).onKeyPress(typedChar, keyCode);
            }
        }
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    private void handleScissorBox() {
        int childrenHeight = this.childrenHeight;
        switch (this.state) {
            case EXPANDING: {
                if (this.scissorBoxHeight < (double)childrenHeight) {
                    this.scissorBoxHeight = AnimationUtil.animate(childrenHeight, this.scissorBoxHeight, 0.06);
                } else if (this.scissorBoxHeight >= (double)childrenHeight) {
                    this.state = AnimationState.STATIC;
                }
                this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, childrenHeight);
                break;
            }
            case RETRACTING: {
                if (this.scissorBoxHeight > 0.0) {
                    this.scissorBoxHeight = AnimationUtil.animate(0.0, this.scissorBoxHeight, 0.06);
                } else if (this.scissorBoxHeight <= 0.0) {
                    this.state = AnimationState.STATIC;
                }
                this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, childrenHeight);
                break;
            }
            case STATIC: {
                if (this.scissorBoxHeight > 0.0 && this.scissorBoxHeight != (double)childrenHeight) {
                    this.scissorBoxHeight = AnimationUtil.animate(childrenHeight, this.scissorBoxHeight, 0.06);
                }
                this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, childrenHeight);
            }
        }
    }

    private double clamp(double a, double max) {
        if (a < 0.0) {
            return 0.0;
        }
        return Math.min(a, max);
    }
}

