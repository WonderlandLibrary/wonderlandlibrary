/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.util.Session
 *  org.lwjgl.input.Keyboard
 */
package net.dev.important.gui.client.altmanager.sub;

import java.io.IOException;
import net.dev.important.Client;
import net.dev.important.event.SessionEvent;
import net.dev.important.gui.client.altmanager.GuiAltManager;
import net.dev.important.gui.font.Fonts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

public class GuiChangeName
extends GuiScreen {
    private final GuiAltManager prevGui;
    private GuiTextField name;
    private String status;

    public GuiChangeName(GuiAltManager gui) {
        this.prevGui = gui;
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, "Change"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        this.name = new GuiTextField(2, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 60, 200, 20);
        this.name.func_146195_b(true);
        this.name.func_146180_a(this.field_146297_k.func_110432_I().func_111285_a());
        this.name.func_146203_f(16);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a((int)30, (int)30, (int)(this.field_146294_l - 30), (int)(this.field_146295_m - 30), (int)Integer.MIN_VALUE);
        this.func_73732_a(Fonts.font40, "Change Name", this.field_146294_l / 2, 34, 0xFFFFFF);
        this.func_73732_a(Fonts.font40, this.status == null ? "" : this.status, this.field_146294_l / 2, this.field_146295_m / 4 + 84, 0xFFFFFF);
        this.name.func_146194_f();
        if (this.name.func_146179_b().isEmpty() && !this.name.func_146206_l()) {
            this.func_73732_a(Fonts.font40, "\u00a77Username", this.field_146294_l / 2 - 74, 66, 0xFFFFFF);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
                break;
            }
            case 1: {
                if (this.name.func_146179_b().isEmpty()) {
                    this.status = "\u00a7cEnter a name!";
                    return;
                }
                if (!this.name.func_146179_b().equalsIgnoreCase(this.field_146297_k.func_110432_I().func_111285_a())) {
                    this.status = "\u00a7cJust change the upper and lower case!";
                    return;
                }
                this.field_146297_k.field_71449_j = new Session(this.name.func_146179_b(), this.field_146297_k.func_110432_I().func_148255_b(), this.field_146297_k.func_110432_I().func_148254_d(), this.field_146297_k.func_110432_I().func_152428_f().name());
                Client.eventManager.callEvent(new SessionEvent());
                this.prevGui.status = this.status = "\u00a7aChanged name to \u00a77" + this.name.func_146179_b() + "\u00a7c.";
                this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
            }
        }
        super.func_146284_a(button);
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
            return;
        }
        if (this.name.func_146206_l()) {
            this.name.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.name.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        this.name.func_146178_a();
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        super.func_146281_b();
    }
}

