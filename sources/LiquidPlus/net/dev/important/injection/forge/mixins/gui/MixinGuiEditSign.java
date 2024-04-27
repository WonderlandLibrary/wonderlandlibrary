/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.gui.inventory.GuiEditSign
 *  net.minecraft.event.ClickEvent
 *  net.minecraft.event.ClickEvent$Action
 *  net.minecraft.tileentity.TileEntitySign
 *  net.minecraft.util.ChatAllowedCharacters
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.ChatStyle
 */
package net.dev.important.injection.forge.mixins.gui;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.event.ClickEvent;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiEditSign.class})
public class MixinGuiEditSign
extends GuiScreen {
    @Shadow
    private int field_146851_h;
    @Shadow
    private TileEntitySign field_146848_f;
    @Shadow
    private GuiButton field_146852_i;
    private boolean enabled;
    private GuiButton toggleButton;
    private GuiTextField signCommand1;
    private GuiTextField signCommand2;
    private GuiTextField signCommand3;
    private GuiTextField signCommand4;

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

    @Inject(method={"actionPerformed"}, at={@At(value="HEAD")})
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch (button.field_146127_k) {
            case 0: {
                if (!this.signCommand1.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[0].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand1.func_146179_b())));
                }
                if (!this.signCommand2.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[1].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand2.func_146179_b())));
                }
                if (!this.signCommand3.func_146179_b().isEmpty()) {
                    this.field_146848_f.field_145915_a[2].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand3.func_146179_b())));
                }
                if (this.signCommand4.func_146179_b().isEmpty()) break;
                this.field_146848_f.field_145915_a[3].func_150255_a(new ChatStyle().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand4.func_146179_b())));
                break;
            }
            case 1: {
                this.enabled = !this.enabled;
                this.toggleButton.field_146126_j = this.enabled ? "Disable Formatting codes" : "Enable Formatting codes";
            }
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    private void drawFields(CallbackInfo callbackInfo) {
        this.field_146289_q.func_78276_b("\u00a7c\u00a7lCommands \u00a77(\u00a7f\u00a7l1.8\u00a77)", this.field_146294_l / 2 - 100, this.field_146295_m - 75, Color.WHITE.getRGB());
        this.signCommand1.func_146194_f();
        this.signCommand2.func_146194_f();
        this.signCommand3.func_146194_f();
        this.signCommand4.func_146194_f();
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.signCommand1.func_146192_a(mouseX, mouseY, mouseButton);
        this.signCommand2.func_146192_a(mouseX, mouseY, mouseButton);
        this.signCommand3.func_146192_a(mouseX, mouseY, mouseButton);
        this.signCommand4.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    @Overwrite
    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        this.signCommand1.func_146201_a(typedChar, keyCode);
        this.signCommand2.func_146201_a(typedChar, keyCode);
        this.signCommand3.func_146201_a(typedChar, keyCode);
        this.signCommand4.func_146201_a(typedChar, keyCode);
        if (this.signCommand1.func_146206_l() || this.signCommand2.func_146206_l() || this.signCommand3.func_146206_l() || this.signCommand4.func_146206_l()) {
            return;
        }
        if (keyCode == 200) {
            this.field_146851_h = this.field_146851_h - 1 & 3;
        }
        if (keyCode == 208 || keyCode == 28 || keyCode == 156) {
            this.field_146851_h = this.field_146851_h + 1 & 3;
        }
        String s = this.field_146848_f.field_145915_a[this.field_146851_h].func_150260_c();
        if (keyCode == 14 && s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
        if ((ChatAllowedCharacters.func_71566_a((char)typedChar) || this.enabled && typedChar == '\u00a7') && this.field_146289_q.func_78256_a(s + typedChar) <= 90) {
            s = s + typedChar;
        }
        this.field_146848_f.field_145915_a[this.field_146851_h] = new ChatComponentText(s);
        if (keyCode == 1) {
            this.func_146284_a(this.field_146852_i);
        }
    }
}

