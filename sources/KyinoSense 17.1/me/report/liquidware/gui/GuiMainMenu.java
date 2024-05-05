/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSelectWorld
 *  net.minecraft.client.gui.GuiYesNoCallback
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.resources.I18n
 *  net.minecraftforge.fml.client.GuiModList
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.gui;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.utils.ui.FuckerNMSL;
import me.report.liquidware.utils.ui.utils.EmptyInputBox;
import net.ccbluex.liquidbounce.ui.client.GuiBackground;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiModList;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\f\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0014J \u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u0005H\u0016J\b\u0010\u000f\u001a\u00020\u0007H\u0016J\u0018\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\fH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lme/report/liquidware/gui/GuiMainMenu;", "Lnet/minecraft/client/gui/GuiScreen;", "Lnet/minecraft/client/gui/GuiYesNoCallback;", "()V", "hWidth", "", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "initGui", "keyTyped", "typedChar", "", "keyCode", "KyinoClient"})
public final class GuiMainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private final float hWidth;

    public void func_73866_w_() {
        int defaultHeight = (int)((double)this.field_146295_m / 3.5);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50, defaultHeight, 100, 20, I18n.func_135052_a((String)"menu.singleplayer", (Object[])new Object[0])));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 50, defaultHeight + 24, 100, 20, I18n.func_135052_a((String)"menu.multiplayer", (Object[])new Object[0])));
        this.field_146292_n.add(new GuiButton(100, this.field_146294_l / 2 - 50, defaultHeight + 48, 100, 20, "AltManager"));
        this.field_146292_n.add(new GuiButton(103, this.field_146294_l / 2 - 50, defaultHeight + 72, 100, 20, "Mods"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 50, defaultHeight + 96, 100, 20, I18n.func_135052_a((String)"menu.options", (Object[])new Object[0])));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 50, defaultHeight + 120, 100, 20, I18n.func_135052_a((String)"menu.quit", (Object[])new Object[0])));
        super.func_73866_w_();
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        float ULY = 2.0f;
        int bHeight = (int)((double)this.field_146295_m / 3.5);
        float f = this.field_146294_l / 2;
        float f2 = bHeight - 20;
        Color color = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
        Fonts.font40.drawCenteredString("KyinoClient", f, f2, color.getRGB(), true);
        StringBuilder stringBuilder = new StringBuilder().append("Logged is \u00a7a");
        EmptyInputBox emptyInputBox = FuckerNMSL.username;
        Intrinsics.checkExpressionValueIsNotNull((Object)emptyInputBox, "FuckerNMSL.username");
        String string = stringBuilder.append(emptyInputBox.getText()).toString();
        StringBuilder stringBuilder2 = new StringBuilder().append("Logged is \u00a7a");
        EmptyInputBox emptyInputBox2 = FuckerNMSL.username;
        Intrinsics.checkExpressionValueIsNotNull((Object)emptyInputBox2, "FuckerNMSL.username");
        Fonts.font40.drawString(string, this.hWidth - (float)(Fonts.font40.func_78256_a(stringBuilder2.append(emptyInputBox2.getText()).toString()) / 2), new ScaledResolution(this.field_146297_k).func_78328_b() - Fonts.font40.getHeight() - 4, new Color(150, 150, 150).getRGB());
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.field_71474_y));
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiSelectWorld((GuiScreen)this));
                break;
            }
            case 2: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                break;
            }
            case 4: {
                this.field_146297_k.func_71400_g();
                break;
            }
            case 100: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiAltManager(this));
                break;
            }
            case 102: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiBackground(this));
                break;
            }
            case 103: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiModList((GuiScreen)this));
            }
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) {
    }

    public GuiMainMenu() {
        this.hWidth = 480.0f;
    }
}

