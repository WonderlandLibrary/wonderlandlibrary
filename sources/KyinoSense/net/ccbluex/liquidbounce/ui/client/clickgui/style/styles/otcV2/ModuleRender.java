/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.report.liquidware.utils.verify.WbxMain;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings.BoolSetting;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings.ColorSetting;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings.FloatSetting;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings.ListSetting;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings.NumberSetting;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings.TextSetting;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.Position;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ColorValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;
import org.lwjgl.input.Keyboard;

public class ModuleRender {
    public Position pos;
    public float y;
    public float x;
    private float offset = 0.0f;
    private final Module parentModule;
    public boolean selected;
    public boolean binds;
    public int height = 0;
    public int scrollY = 0;
    public List<Downward> downwards = new ArrayList<Downward>();
    private float modulex;
    private float moduley;

    public ModuleRender(Module module, float modX, float modY, float w, float h) {
        this.parentModule = module;
        int cHeight = 20;
        for (Value<?> setting : module.getValues()) {
            if (setting instanceof IntegerValue) {
                this.downwards.add(new NumberSetting((IntegerValue)setting, modX, modY + (float)cHeight, 0, 0, this));
                cHeight += 16;
            }
            if (setting instanceof FloatValue) {
                this.downwards.add(new FloatSetting((FloatValue)setting, modX, modY + (float)cHeight, 0, 0, this));
                cHeight += 16;
            }
            if (setting instanceof BoolValue) {
                this.downwards.add(new BoolSetting((BoolValue)setting, modX, modY + (float)cHeight - 6.0f, 0, 0, this));
                cHeight += 16;
            }
            if (setting instanceof ListValue) {
                this.downwards.add(new ListSetting((ListValue)setting, modX, modY + (float)cHeight, -6, 0, this));
                cHeight += 22;
            }
            if (setting instanceof TextValue) {
                this.downwards.add(new TextSetting((TextValue)setting, modX, modY + (float)cHeight, -6, 0, this));
                cHeight += 22;
            }
            if (!(setting instanceof ColorValue)) continue;
            this.downwards.add(new ColorSetting((ColorValue)setting, modX, modY + (float)cHeight, -6, 0, this));
            cHeight += 22;
        }
        this.height = cHeight;
        this.pos = new Position(modX, modY, w, cHeight);
    }

    public void drawScreen(int mouseX, int mouseY) {
        int guiColor = ClickGUI.generateColor().getRGB();
        try {
            this.modulex = OtcClickGUi.getMainx();
            this.moduley = OtcClickGUi.getMainy();
            this.x = this.pos.x;
            this.y = this.pos.y + (float)this.getScrollY();
            RoundedUtil.drawRound(this.modulex + 5.0f + this.x, this.moduley + 17.0f + this.y, 135.0f, this.pos.height, 3.0f, new Color(50, 54, 65));
            RoundedUtil.drawGradientHorizontal(this.modulex + 5.0f + this.x, this.moduley + 18.0f + this.y, 135.0f, 1.5f, 1.0f, new Color(guiColor), new Color(guiColor));
            Fonts.fontTahoma.drawString(this.parentModule.getName(), this.modulex + 5.0f + this.x, this.moduley + 17.0f + this.y - 8.0f, new Color(255, 255, 255).getRGB());
            RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.x + 4.0f, this.moduley + 17.0f + this.y + 8.0f, 7.0f, 7.0f, 1.0f, this.parentModule.getState() ? new Color(86, 94, 115).getRGB() : new Color(50, 54, 65).getRGB(), 1.0f, this.parentModule.getState() ? new Color(86, 94, 115).getRGB() : new Color(85, 90, 96).getRGB());
            if (this.isHovered(mouseX, mouseY)) {
                RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.x + 4.0f, this.moduley + 17.0f + this.y + 8.0f, 7.0f, 7.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, new Color(guiColor).getRGB());
            }
            Fonts.fontTahoma.drawString("Enable", this.modulex + 5.0f + this.x + 4.0f + 10.0f, this.moduley + 17.0f + this.y + 8.0f + 3.0f, new Color(200, 200, 200).getRGB());
            for (Downward downward : this.downwards) {
                downward.draw(mouseX, mouseY);
            }
            RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.x + 115.0f - (float)Fonts.fontTahoma.func_78256_a(this.binds ? "..." : Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 13.0f, this.moduley + 17.0f + this.y + 8.0f + 0.5f, Fonts.fontTahoma.func_78256_a(this.binds ? "..." : Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 4, 7.0f, 1.0f, new Color(28, 32, 40).getRGB(), 1.0f, new Color(86, 94, 115).getRGB());
            Fonts.fontTahoma.drawString(this.binds ? "..." : Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase(), this.modulex + 5.0f + this.x + 117.0f - (float)Fonts.fontTahoma.func_78256_a(this.binds ? "..." : Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 13.0f, this.moduley + 17.0f + this.y + 11.0f, -1);
            if (this.isKeyBindHovered(mouseX, mouseY)) {
                RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.x + 115.0f - (float)Fonts.fontTahoma.func_78256_a(this.binds ? "..." : Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 13.0f, this.moduley + 17.0f + this.y + 8.0f + 0.5f, Fonts.fontTahoma.func_78256_a(this.binds ? "..." : Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 4, 7.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, guiColor);
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isKeyBindHovered(mouseX, mouseY) && mouseButton == 0) {
            this.binds = true;
        }
        if (!this.isKeyBindHovered(mouseX, mouseY) && this.binds && mouseButton == 0) {
            this.binds = false;
        }
        if (this.isHovered(mouseX, mouseY) && mouseButton == 0) {
            this.parentModule.toggle();
        }
        if (this.binds && mouseButton == 1) {
            this.parentModule.setKeyBind(0);
            this.binds = false;
        }
        this.downwards.forEach(e -> e.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.downwards.forEach(e -> e.mouseReleased(mouseX, mouseY, state));
        boolean focused = WbxMain.getInstance().getSideGui().focused;
        WbxMain.getInstance().getSideGui().mouseReleased(mouseX, mouseY, state);
    }

    public void keyTyped(char typedChar, int keyCode) {
        int finalKeyCode = keyCode;
        this.downwards.forEach(e -> e.keyTyped(typedChar, finalKeyCode));
        if (this.binds) {
            if (keyCode == 211) {
                keyCode = 0;
            }
            this.parentModule.setKeyBind(keyCode);
            this.binds = false;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.x + 4.0f && (float)mouseX <= this.modulex + 5.0f + this.x + 4.0f + 15.0f && (float)mouseY >= this.moduley + 17.0f + this.y + 8.0f && (float)mouseY <= this.moduley + 17.0f + this.y + 8.0f + 6.0f;
    }

    public boolean isKeyBindHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.x + 115.0f - (float)Fonts.fontTahoma.func_78256_a(Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 13.0f && (float)mouseX <= this.modulex + 5.0f + this.x + 115.0f - (float)Fonts.fontTahoma.func_78256_a(Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 13.0f + (float)Fonts.fontTahoma.func_78256_a(Keyboard.getKeyName((int)this.parentModule.getKeyBind()).toLowerCase()) + 3.0f && (float)mouseY >= this.moduley + 17.0f + this.y + 8.0f + 0.5f && (float)mouseY <= this.moduley + 17.0f + this.y + 8.0f + 0.5f + 7.0f;
    }

    public Module getparent() {
        return this.parentModule;
    }

    public float getY() {
        return this.pos.y + (float)this.getScrollY();
    }

    public float getMaxValueY() {
        return this.downwards.get(this.downwards.size() - 1).getY();
    }

    public void setY(float y) {
        this.pos.y = y;
    }

    public int getScrollY() {
        return this.scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public float getOffset() {
        return this.offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }
}

