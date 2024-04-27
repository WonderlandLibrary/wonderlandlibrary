/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
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
import net.minecraft.client.gui.FontRenderer;
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
    @Shadow
    private int field_146851_h;
    @Final
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
        GuiTextField guiTextField;
        GuiTextField guiTextField2;
        GuiTextField guiTextField3;
        GuiTextField guiTextField4;
        this.toggleButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 145, this.enabled ? "Disable Formatting codes" : "Enable Formatting codes");
        this.field_146292_n.add(this.toggleButton);
        MixinGuiEditSign mixinGuiEditSign = this;
        GuiTextField guiTextField5 = guiTextField4;
        GuiTextField guiTextField6 = guiTextField4;
        boolean bl = false;
        FontRenderer fontRenderer = this.field_146289_q;
        int n = this.field_146294_l / 2 - 100;
        (this.field_146295_m - 15).signCommand1 = (GuiTextField)0;
        MixinGuiEditSign mixinGuiEditSign2 = this;
        GuiTextField guiTextField7 = guiTextField3;
        GuiTextField guiTextField8 = guiTextField3;
        boolean bl2 = true;
        FontRenderer fontRenderer2 = this.field_146289_q;
        int n2 = this.field_146294_l / 2 - 100;
        (this.field_146295_m - 30).signCommand2 = (GuiTextField)0;
        MixinGuiEditSign mixinGuiEditSign3 = this;
        GuiTextField guiTextField9 = guiTextField2;
        GuiTextField guiTextField10 = guiTextField2;
        int n3 = 2;
        FontRenderer fontRenderer3 = this.field_146289_q;
        int n4 = this.field_146294_l / 2 - 100;
        (this.field_146295_m - 45).signCommand3 = (GuiTextField)0;
        MixinGuiEditSign mixinGuiEditSign4 = this;
        GuiTextField guiTextField11 = guiTextField;
        GuiTextField guiTextField12 = guiTextField;
        int n5 = 3;
        FontRenderer fontRenderer4 = this.field_146289_q;
        int n6 = this.field_146294_l / 2 - 100;
        (this.field_146295_m - 60).signCommand4 = (GuiTextField)0;
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
        this.field_146848_f.field_145915_a[this.field_146851_h] = new TextComponentString(s);
        if (keyCode == 1) {
            this.func_146284_a(this.field_146852_i);
        }
    }
}

