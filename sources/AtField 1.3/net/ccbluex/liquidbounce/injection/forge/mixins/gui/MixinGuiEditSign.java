/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.gui.inventory.GuiEditSign
 *  net.minecraft.tileentity.TileEntitySign
 *  net.minecraft.util.ChatAllowedCharacters
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.event.ClickEvent
 *  net.minecraft.util.text.event.ClickEvent$Action
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiEditSign.class})
public class MixinGuiEditSign
extends GuiScreen {
    private boolean enabled;
    @Final
    @Shadow
    private TileEntitySign field_146848_f;
    private GuiTextField signCommand4;
    private GuiTextField signCommand3;
    private GuiTextField signCommand2;
    @Shadow
    private int field_146851_h;
    private GuiButton toggleButton;
    @Shadow
    private GuiButton field_146852_i;
    private GuiTextField signCommand1;

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    private void drawFields(CallbackInfo callbackInfo) {
        this.field_146289_q.func_78276_b("\u00a7c\u00a7lCommands \u00a77(\u00a7f\u00a7l1.8\u00a77)", this.field_146294_l / 2 - 100, this.field_146295_m - 75, Color.WHITE.getRGB());
        this.signCommand1.func_146194_f();
        this.signCommand2.func_146194_f();
        this.signCommand3.func_146194_f();
        this.signCommand4.func_146194_f();
    }

    protected void func_73864_a(int n, int n2, int n3) throws IOException {
        this.signCommand1.func_146192_a(n, n2, n3);
        this.signCommand2.func_146192_a(n, n2, n3);
        this.signCommand3.func_146192_a(n, n2, n3);
        this.signCommand4.func_146192_a(n, n2, n3);
        super.func_73864_a(n, n2, n3);
    }

    @Overwrite
    protected void func_73869_a(char c, int n) throws IOException {
        this.signCommand1.func_146201_a(c, n);
        this.signCommand2.func_146201_a(c, n);
        this.signCommand3.func_146201_a(c, n);
        this.signCommand4.func_146201_a(c, n);
        if (this.signCommand1.func_146206_l() || this.signCommand2.func_146206_l() || this.signCommand3.func_146206_l() || this.signCommand4.func_146206_l()) {
            return;
        }
        if (n == 200) {
            this.field_146851_h = this.field_146851_h - 1 & 3;
        }
        if (n == 208 || n == 28 || n == 156) {
            this.field_146851_h = this.field_146851_h + 1 & 3;
        }
        String string = this.field_146848_f.field_145915_a[this.field_146851_h].func_150260_c();
        if (n == 14 && string.length() > 0) {
            string = string.substring(0, string.length() - 1);
        }
        if ((ChatAllowedCharacters.func_71566_a((char)c) || this.enabled && c == '\u00a7') && this.field_146289_q.func_78256_a(string + c) <= 90) {
            string = string + c;
        }
        this.field_146848_f.field_145915_a[this.field_146851_h] = new TextComponentString(string);
        if (n == 1) {
            this.func_146284_a(this.field_146852_i);
        }
    }

    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    private void actionPerformed(GuiButton guiButton, CallbackInfo callbackInfo) {
        switch (guiButton.field_146127_k) {
            case 0: {
                if (!this.signCommand1.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[0].func_150255_a(new Style().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand1.func_146179_b())));
                }
                if (!this.signCommand2.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[1].func_150255_a(new Style().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand2.func_146179_b())));
                }
                if (!this.signCommand3.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[2].func_150255_a(new Style().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand3.func_146179_b())));
                }
                if (this.signCommand4.func_146179_b().isEmpty()) break;
                this.field_146848_f.field_145915_a[3].func_150255_a(new Style().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand4.func_146179_b())));
                break;
            }
            case 1: {
                this.enabled = !this.enabled;
                this.toggleButton.field_146126_j = this.enabled ? "Disable Formatting codes" : "Enable Formatting codes";
            }
        }
    }

    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void initGui(CallbackInfo callbackInfo) {
        this.toggleButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 145, this.enabled ? "Disable Formatting codes" : "Enable Formatting codes");
        this.field_146292_n.add(this.toggleButton);
        this.signCommand1 = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 15, 200, 10);
        this.signCommand2 = new GuiTextField(1, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 30, 200, 10);
        this.signCommand3 = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 45, 200, 10);
        this.signCommand4 = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m - 60, 200, 10);
        this.signCommand1.func_146180_a("");
        this.signCommand2.func_146180_a("");
        this.signCommand3.func_146180_a("");
        this.signCommand4.func_146180_a("");
    }
}

