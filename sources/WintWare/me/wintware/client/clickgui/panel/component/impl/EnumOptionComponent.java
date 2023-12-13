/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.wintware.client.clickgui.panel.component.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import me.wintware.client.clickgui.panel.Panel;
import me.wintware.client.clickgui.panel.component.Component;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;

public final class EnumOptionComponent
extends Component {
    private int opacity = 120;

    public EnumOptionComponent(Setting option, Panel panel, int x, int y, int width, int height) {
        super(panel, x, y, width, height);
        this.option = option;
    }

    @Override
    public void onDraw(int mouseX, int mouseY) {
        Panel parent = this.getPanel();
        int x = parent.getX() + this.getX();
        int y = parent.getY() + this.getY();
        boolean hovered = this.isMouseOver(mouseX, mouseY);
        if (hovered) {
            if (this.opacity < 200) {
                this.opacity += 5;
            }
        } else if (this.opacity > 120) {
            this.opacity -= 5;
        }
        RenderUtil.drawRect(x, y, x + this.getWidth(), y + this.getHeight(), parent.dragging ? 0x9000000 : ColorUtils.getColorWithOpacity(BACKGROUND, 255 - this.opacity).getRGB());
        int color = new Color(this.opacity, this.opacity, this.opacity).getRGB();
        Minecraft.getMinecraft().smallfontRenderer.drawStringWithShadow(String.format("%s: %s", this.option.getName(), ChatFormatting.GRAY + this.option.getValString()), x + 2, (float)y + (float)this.getHeight() / 2.0f - 2.0f, -1);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseOver(mouseX, mouseY)) {
            ArrayList<String> options = this.option.getOptions();
            int index = options.indexOf(this.option.getValString());
            if (mouseButton == 0) {
                ++index;
            } else if (mouseButton == 1) {
                --index;
            }
            if (index >= options.size()) {
                index = 0;
            } else if (index < 0) {
                index = options.size() - 1;
            }
            this.option.setValString(this.option.getOptions().get(index));
        }
    }
}

