/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.flux;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.file.configs.ProfilesConfig;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class ProfilesScreen
extends GuiScreen {
    private final GuiScreen prevScreen;
    private GuiTextField textField;

    public ProfilesScreen(GuiScreen prevScreen) {
        this.prevScreen = prevScreen;
    }

    public void func_73866_w_() {
        super.func_73866_w_();
        this.textField = new GuiTextField(0, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 100, this.field_146295_m / 2 - 50, 200, 20);
        this.textField.func_146195_b(true);
        GuiButton addButton = new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 2, "Add");
        GuiButton cancelButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + 25, "Cancel");
        this.field_146292_n.add(addButton);
        this.field_146292_n.add(cancelButton);
        Keyboard.enableRepeatEvents((boolean)true);
    }

    public void func_73876_c() {
        this.textField.func_146178_a();
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        this.textField.func_146201_a(typedChar, keyCode);
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_146284_a(GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                if (this.textField.func_146179_b().isEmpty()) break;
                ProfilesConfig profilesConfig = new ProfilesConfig(new File(LiquidBounce.fileManager.settingsDir, this.textField.func_146179_b() + ".profile"));
                if (profilesConfig.getFile().exists()) {
                    profilesConfig.deleteConfig();
                    LiquidBounce.fileManager.profilesConfigs.remove(profilesConfig);
                }
                try {
                    if (!profilesConfig.hasConfig()) {
                        profilesConfig.createConfig();
                    }
                    profilesConfig.saveConfig();
                    ClientUtils.getLogger().info("[FileManager] Saved config: " + profilesConfig.getFile().getName() + ".");
                }
                catch (Throwable throwable) {
                    ClientUtils.getLogger().error("[FileManager] Failed to save config file: " + profilesConfig.getFile().getName() + ".", throwable);
                }
                LiquidBounce.fileManager.profilesConfigs.add(profilesConfig);
                this.field_146297_k.func_147108_a(this.prevScreen);
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a(this.prevScreen);
            }
        }
        super.func_146284_a(button);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        this.textField.func_146192_a(mouseX, mouseY, mouseButton);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawRect((float)(-this.field_146294_l), (float)(-this.field_146295_m), (float)this.field_146294_l, (float)this.field_146295_m, new Color(0, 0, 0, 200).getRGB());
        this.textField.func_146194_f();
        this.field_146297_k.field_71466_p.func_175063_a("Name", (float)this.field_146294_l * 0.5f - 100.0f, (float)this.field_146295_m * 0.5f - 60.0f, -1);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}

