/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  org.lwjgl.input.Keyboard
 */
package net.dev.important.gui.client.altmanager.sub;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import net.dev.important.gui.client.altmanager.GuiAltManager;
import net.dev.important.gui.elements.GuiPasswordField;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.TabUtils;
import net.dev.important.utils.login.MinecraftAccount;
import net.dev.important.utils.render.ColorUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class GuiDirectLogin
extends GuiScreen {
    private final GuiScreen prevGui;
    private GuiButton loginButton;
    private GuiButton clipboardLoginButton;
    private GuiTextField username;
    private GuiPasswordField password;
    private String status = "\u00a77Idle...";

    public GuiDirectLogin(GuiAltManager gui) {
        this.prevGui = gui;
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.loginButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 72, "Login");
        this.field_146292_n.add(this.loginButton);
        this.clipboardLoginButton = new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, "Clipboard Login");
        this.field_146292_n.add(this.clipboardLoginButton);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        this.username = new GuiTextField(2, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 60, 200, 20);
        this.username.func_146195_b(true);
        this.username.func_146203_f(Integer.MAX_VALUE);
        this.password = new GuiPasswordField(3, Fonts.font40, this.field_146294_l / 2 - 100, 85, 200, 20);
        this.password.func_146203_f(Integer.MAX_VALUE);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a((int)30, (int)30, (int)(this.field_146294_l - 30), (int)(this.field_146295_m - 30), (int)Integer.MIN_VALUE);
        this.func_73732_a(Fonts.font40, "Direct Login", this.field_146294_l / 2, 34, 0xFFFFFF);
        this.func_73732_a(Fonts.font35, this.status == null ? "" : this.status, this.field_146294_l / 2, this.field_146295_m / 4 + 60, 0xFFFFFF);
        this.username.func_146194_f();
        this.password.func_146194_f();
        if (this.username.func_146179_b().isEmpty() && !this.username.func_146206_l()) {
            this.func_73732_a(Fonts.font40, "\u00a77Username / E-Mail", this.field_146294_l / 2 - 55, 66, 0xFFFFFF);
        }
        if (this.password.func_146179_b().isEmpty() && !this.password.func_146206_l()) {
            this.func_73732_a(Fonts.font40, "\u00a77Password", this.field_146294_l / 2 - 74, 91, 0xFFFFFF);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(GuiButton button) throws IOException {
        if (!button.field_146124_l) {
            return;
        }
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                if (this.username.func_146179_b().isEmpty()) {
                    this.status = "\u00a7cYou have to fill in both fields!";
                    return;
                }
                this.clipboardLoginButton.field_146124_l = false;
                this.loginButton.field_146124_l = false;
                new Thread(() -> {
                    this.status = "\u00a7aLogging in...";
                    this.status = this.password.func_146179_b().isEmpty() ? GuiAltManager.login(new MinecraftAccount(ColorUtils.translateAlternateColorCodes(this.username.func_146179_b()))) : GuiAltManager.login(new MinecraftAccount(this.username.func_146179_b(), this.password.func_146179_b()));
                    this.clipboardLoginButton.field_146124_l = true;
                    this.loginButton.field_146124_l = true;
                }).start();
                break;
            }
            case 2: {
                try {
                    String clipboardData = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    String[] args2 = clipboardData.split(":", 2);
                    if (!clipboardData.contains(":") || args2.length != 2) {
                        this.status = "\u00a7cInvalid clipboard data. (Use: E-Mail:Password)";
                        return;
                    }
                    this.clipboardLoginButton.field_146124_l = false;
                    this.loginButton.field_146124_l = false;
                    new Thread(() -> {
                        this.status = "\u00a7aLogging in...";
                        this.status = GuiAltManager.login(new MinecraftAccount(args2[0], args2[1]));
                        this.clipboardLoginButton.field_146124_l = true;
                        this.loginButton.field_146124_l = true;
                    }).start();
                    break;
                }
                catch (UnsupportedFlavorException e) {
                    this.status = "\u00a7cClipboard flavor unsupported!";
                    ClientUtils.getLogger().error("Failed to read data from clipboard.", (Throwable)e);
                    break;
                }
                catch (IOException e) {
                    this.status = "\u00a7cUnknown error! (See log)";
                    ClientUtils.getLogger().error((Object)e);
                }
            }
        }
        super.func_146284_a(button);
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case 1: {
                this.field_146297_k.func_147108_a(this.prevGui);
                return;
            }
            case 15: {
                TabUtils.tab(this.username, this.password);
                return;
            }
            case 28: {
                this.func_146284_a(this.loginButton);
                return;
            }
        }
        if (this.username.func_146206_l()) {
            this.username.func_146201_a(typedChar, keyCode);
        }
        if (this.password.func_146206_l()) {
            this.password.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.username.func_146192_a(mouseX, mouseY, mouseButton);
        this.password.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        this.username.func_146178_a();
        this.password.func_146178_a();
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        super.func_146281_b();
    }
}

