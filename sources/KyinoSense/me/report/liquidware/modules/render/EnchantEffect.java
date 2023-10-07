/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.ColorUti;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="EnchantEffect", category=ModuleCategory.RENDER, description="Enchant Effect")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lme/report/liquidware/modules/render/EnchantEffect;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "blueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "greenValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "redValue", "getColor", "Ljava/awt/Color;", "KyinoClient"})
public final class EnchantEffect
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Rainbow", "AnotherRainbow", "ARGB2", "Custom"}, "ARGB");
    private final IntegerValue redValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue greenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue blueValue = new IntegerValue("Blue", 255, 0, 255);

    @NotNull
    public final Color getColor() {
        Color color;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "rainbow": {
                color = ColorUtils.rainbow();
                break;
            }
            case "anotherrainbow": {
                color = ColorUtils.INSTANCE.skyRainbow(10, 0.9f, 1.0f, 1.0);
                break;
            }
            case "argb": {
                color = ColorUti.rainbow();
                break;
            }
            default: {
                color = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue());
            }
        }
        return color;
    }
}

