/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.FluxParody;

import java.awt.Color;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.FluxParody.ClickGui;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class GuiExit
extends GuiScreen {
    boolean mouseClicked = false;

    public void func_146281_b() {
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        if (mouseX >= this.field_146294_l / 2 - 75 && mouseX <= this.field_146294_l / 2 - 10 && mouseY >= this.field_146295_m / 2 + 20 && mouseY <= this.field_146295_m / 2 + 35) {
            if (Mouse.isButtonDown((int)0) && !this.mouseClicked) {
                this.field_146297_k.func_147108_a((GuiScreen)new ClickGui());
            }
        } else {
            boolean bl = this.mouseClicked = Mouse.isButtonDown((int)0) && (mouseX < this.field_146294_l / 2 || mouseX > this.field_146294_l / 2 + 65 || mouseY < this.field_146295_m / 2 + 20 || mouseY > this.field_146295_m / 2 + 35);
        }
        if (mouseX >= this.field_146294_l / 2 && mouseX <= this.field_146294_l / 2 + 65 && mouseY >= this.field_146295_m / 2 + 20 && mouseY <= this.field_146295_m / 2 + 35) {
            if (Mouse.isButtonDown((int)0) && !this.mouseClicked) {
                this.field_146297_k.func_147108_a(null);
            }
        } else {
            this.mouseClicked = Mouse.isButtonDown((int)0) && (mouseX < this.field_146294_l / 2 - 75 || mouseX > this.field_146294_l / 2 - 10 || mouseY < this.field_146295_m / 2 + 20 || mouseY > this.field_146295_m / 2 + 35);
        }
        RenderUtils.drawRect((float)(this.field_146294_l / 2 - 80), (float)(this.field_146295_m / 2 - 30), (float)(this.field_146294_l / 2 + 70), (float)(this.field_146295_m / 2 + 40), new Color(40, 40, 40, 125).getRGB());
        RenderUtils.drawRect((float)(this.field_146294_l / 2 - 75), (float)(this.field_146295_m / 2 + 20), (float)(this.field_146294_l / 2 - 10), (float)(this.field_146295_m / 2 + 35), new Color(19, 138, 225).getRGB());
        RenderUtils.drawRect((float)(this.field_146294_l / 2), (float)(this.field_146295_m / 2 + 20), (float)(this.field_146294_l / 2 + 65), (float)(this.field_146295_m / 2 + 35), new Color(241, 54, 35).getRGB());
        Fonts.font40.func_78276_b("Exit Gui", this.field_146294_l / 2 - 25, this.field_146295_m / 2 - 20, -1);
        Fonts.font35.func_78276_b("Back", this.field_146294_l / 2 - 50, this.field_146295_m / 2 + 28, -1);
        Fonts.font35.func_78276_b("Exit", this.field_146294_l / 2 + 25, this.field_146295_m / 2 + 28, -1);
    }
}

