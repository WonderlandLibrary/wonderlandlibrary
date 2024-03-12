// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components;

import dev.tenacity.module.Module;
import dev.tenacity.utils.misc.HoveringUtil;
import java.awt.Color;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.ui.Screen;
import dev.tenacity.module.settings.Setting;

public abstract class SettingComponent<T extends Setting> implements Screen
{
    private final T setting;
    public float x;
    public float y;
    public float width;
    public float height;
    public float alpha;
    public boolean typing;
    public float panelLimitY;
    public Pair<Color, Color> clientColors;
    public Color settingRectColor;
    public Color textColor;
    public float countSize;
    
    public SettingComponent(final T setting) {
        this.countSize = 1.0f;
        this.setting = setting;
    }
    
    public boolean isHoveringBox(final int mouseX, final int mouseY) {
        return HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
    }
    
    public boolean isClickable(final float bottomY) {
        return bottomY > this.panelLimitY && bottomY < this.panelLimitY + 17.0f + Module.allowedClickGuiHeight;
    }
    
    public T getSetting() {
        return this.setting;
    }
}
