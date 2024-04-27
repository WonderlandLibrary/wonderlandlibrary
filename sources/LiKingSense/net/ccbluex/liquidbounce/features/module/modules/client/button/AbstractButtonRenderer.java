/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.client.button;

import ad.novoline.font.Fonts;
import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J \u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH&R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/client/button/AbstractButtonRenderer;", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "(Lnet/minecraft/client/gui/GuiButton;)V", "getButton", "()Lnet/minecraft/client/gui/GuiButton;", "drawButtonText", "", "mc", "Lnet/minecraft/client/Minecraft;", "render", "mouseX", "", "mouseY", "LiKingSense"})
public abstract class AbstractButtonRenderer {
    @NotNull
    private final GuiButton button;

    public abstract void render(int var1, int var2, @NotNull Minecraft var3);

    public void drawButtonText(@NotNull Minecraft mc) {
        Intrinsics.checkParameterIsNotNull((Object)mc, (String)"mc");
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        if (((Boolean)hud.getChineseFontButton().get()).booleanValue()) {
            int n;
            String string = this.button.field_146126_j;
            float f = this.button.field_146128_h + this.button.field_146120_f / 2 - FontLoaders.F18.getStringWidth(this.button.field_146126_j) / 2;
            float f2 = (float)this.button.field_146129_i + (float)(this.button.field_146121_g - 5) / 2.0f - 1.0f;
            if (this.button.field_146124_l) {
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                n = color.getRGB();
            } else {
                Color color = Color.GRAY;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.GRAY");
                n = color.getRGB();
            }
            FontLoaders.F18.DisplayFonts(string, f, f2, n, FontLoaders.F18);
        } else {
            int n;
            CharSequence charSequence = this.button.field_146126_j;
            float f = this.button.field_146128_h + this.button.field_146120_f / 2 - FontLoaders.F18.getStringWidth(this.button.field_146126_j) / 2;
            float f3 = (float)this.button.field_146129_i + (float)(this.button.field_146121_g - 5) / 2.0f - 1.0f;
            if (this.button.field_146124_l) {
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                n = color.getRGB();
            } else {
                Color color = Color.GRAY;
                Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.GRAY");
                n = color.getRGB();
            }
            Fonts.posterama.posterama18.posterama18.drawString(charSequence, f, f3, n);
        }
    }

    @NotNull
    protected final GuiButton getButton() {
        return this.button;
    }

    public AbstractButtonRenderer(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull((Object)button, (String)"button");
        this.button = button;
    }
}

