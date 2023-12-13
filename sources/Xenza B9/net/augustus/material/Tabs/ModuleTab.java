// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.Tabs;

import java.awt.Font;
import java.util.Iterator;
import net.augustus.material.button.values.BMode;
import net.augustus.settings.StringValue;
import net.augustus.material.button.values.BNumbers;
import net.augustus.settings.DoubleValue;
import net.augustus.material.button.values.BOption;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.Setting;
import net.augustus.Augustus;
import net.augustus.material.Main;
import net.augustus.material.button.Button;
import java.util.ArrayList;
import net.augustus.modules.Module;
import net.augustus.font.UnicodeFontRenderer;
import net.augustus.material.Tab;

public class ModuleTab extends Tab
{
    private static UnicodeFontRenderer arial18;
    private static UnicodeFontRenderer arial16;
    public Module module;
    private ArrayList<Button> btns;
    float startX;
    float startY;
    
    public ModuleTab(final Module m) {
        this.btns = new ArrayList<Button>();
        this.startX = Main.windowX + 20.0f;
        this.startY = Main.windowY + 70.0f;
        this.module = m;
        this.name = m.getName();
        for (final Setting v : Augustus.getInstance().getSettingsManager().getSettingsByMod(m)) {
            if (v instanceof BooleanValue) {
                final Button value = new BOption(this.startX, this.startY, v, this);
                this.btns.add(value);
            }
            else if (v instanceof DoubleValue) {
                final Button value = new BNumbers(this.startX, this.startY, v, this);
                this.btns.add(value);
            }
            else {
                if (!(v instanceof StringValue)) {
                    continue;
                }
                final Button value = new BMode(this.startX, this.startY, v, this);
                this.btns.add(value);
            }
        }
    }
    
    @Override
    public void render(final float mouseX, final float mouseY) {
        this.startX = Main.windowX + 20.0f + Main.animListX;
        this.startY = Main.windowY + 70.0f;
        for (final Button v : this.btns) {
            if (!v.v.isVisible()) {
                continue;
            }
            v.x = this.startX;
            v.y = this.startY;
            v.draw(mouseX, mouseY);
            if (this.startX + 100.0f + ModuleTab.arial18.getStringWidth(v.v.getName()) < Main.windowX + Main.windowWidth) {
                if (v instanceof BOption) {
                    this.startX += 40 + ModuleTab.arial18.getStringWidth(v.v.getName());
                }
                else {
                    this.startX += 80.0f;
                }
            }
            else {
                this.startX = Main.windowX + 20.0f + Main.animListX;
                this.startY += 30.0f;
            }
        }
    }
    
    @Override
    public void mouseClicked(final float mouseX, final float mouseY) {
        super.mouseClicked(mouseX, mouseY);
        this.startX = Main.windowX + 20.0f + Main.animListX;
        this.startY = Main.windowY + 70.0f;
        for (final Button v : this.btns) {
            if (!v.v.isVisible()) {
                continue;
            }
            v.mouseClick(mouseX, mouseY);
        }
    }
    
    static {
        try {
            ModuleTab.arial18 = new UnicodeFontRenderer(Font.createFont(0, ModuleTab.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            ModuleTab.arial16 = new UnicodeFontRenderer(Font.createFont(0, ModuleTab.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(16.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
