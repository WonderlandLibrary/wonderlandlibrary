package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.betterfps.LibGDXMath;
import me.betterfps.NewMCMath;
import me.betterfps.RivensFullMath;
import me.betterfps.RivensHalfMath;
import me.betterfps.RivensMath;
import me.betterfps.TaylorMath;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="BetterFPS", category=ModuleCategory.WORLD, description=":)")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000L\n\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\b\u000020B¢J!0\"2#0\"¢$J%0\"2#0\"¢$R0¢\b\n\u0000\bR0\b¢\b\n\u0000\b\t\nR0\f¢\b\n\u0000\b\rR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\b ¨&"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/BetterFPS;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "cosMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getCosMode", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "libGDX", "Lme/betterfps/LibGDXMath;", "getLibGDX", "()Lme/betterfps/LibGDXMath;", "newMC", "Lme/betterfps/NewMCMath;", "getNewMC", "()Lme/betterfps/NewMCMath;", "rivens", "Lme/betterfps/RivensMath;", "getRivens", "()Lme/betterfps/RivensMath;", "rivensFull", "Lme/betterfps/RivensFullMath;", "getRivensFull", "()Lme/betterfps/RivensFullMath;", "rivensHalf", "Lme/betterfps/RivensHalfMath;", "getRivensHalf", "()Lme/betterfps/RivensHalfMath;", "sinMode", "getSinMode", "taylor", "Lme/betterfps/TaylorMath;", "getTaylor", "()Lme/betterfps/TaylorMath;", "cos", "", "value", "(F)Ljava/lang/Float;", "sin", "Pride"})
public final class BetterFPS
extends Module {
    @NotNull
    private final LibGDXMath libGDX = new LibGDXMath();
    @NotNull
    private final RivensFullMath rivensFull = new RivensFullMath();
    @NotNull
    private final RivensHalfMath rivensHalf = new RivensHalfMath();
    @NotNull
    private final RivensMath rivens = new RivensMath();
    @NotNull
    private final TaylorMath taylor = new TaylorMath();
    @NotNull
    private final NewMCMath newMC = new NewMCMath();
    @NotNull
    private final ListValue sinMode = new ListValue("SinMode", new String[]{"Vanilla", "Taylor", "LibGDX", "RivensFull", "RivensHalf", "Rivens", "Java", "1.16"}, "Vanilla");
    @NotNull
    private final ListValue cosMode = new ListValue("CosMode", new String[]{"Vanilla", "Taylor", "LibGDX", "RivensFull", "RivensHalf", "Rivens", "Java", "1.16"}, "Vanilla");

    @NotNull
    public final LibGDXMath getLibGDX() {
        return this.libGDX;
    }

    @NotNull
    public final RivensFullMath getRivensFull() {
        return this.rivensFull;
    }

    @NotNull
    public final RivensHalfMath getRivensHalf() {
        return this.rivensHalf;
    }

    @NotNull
    public final RivensMath getRivens() {
        return this.rivens;
    }

    @NotNull
    public final TaylorMath getTaylor() {
        return this.taylor;
    }

    @NotNull
    public final NewMCMath getNewMC() {
        return this.newMC;
    }

    @NotNull
    public final ListValue getSinMode() {
        return this.sinMode;
    }

    @NotNull
    public final ListValue getCosMode() {
        return this.cosMode;
    }

    @Nullable
    public final Float sin(float value) {
        Float f;
        String string = (String)this.sinMode.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "taylor": {
                f = Float.valueOf(this.taylor.sin(value));
                break;
            }
            case "libgdx": {
                f = Float.valueOf(this.libGDX.sin(value));
                break;
            }
            case "rivensfull": {
                f = Float.valueOf(this.rivensFull.sin(value));
                break;
            }
            case "rivenshalf": {
                f = Float.valueOf(this.rivensHalf.sin(value));
                break;
            }
            case "rivens": {
                f = Float.valueOf(this.rivens.sin(value));
                break;
            }
            case "java": {
                double d = value;
                boolean bl2 = false;
                f = Float.valueOf((float)Math.sin(d));
                break;
            }
            case "1.16": {
                f = Float.valueOf(this.newMC.sin(value));
                break;
            }
            default: {
                f = null;
            }
        }
        return f;
    }

    @Nullable
    public final Float cos(float value) {
        Float f;
        String string = (String)this.cosMode.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "taylor": {
                f = Float.valueOf(this.taylor.cos(value));
                break;
            }
            case "libgdx": {
                f = Float.valueOf(this.libGDX.cos(value));
                break;
            }
            case "rivensfull": {
                f = Float.valueOf(this.rivensFull.cos(value));
                break;
            }
            case "rivenshalf": {
                f = Float.valueOf(this.rivensHalf.cos(value));
                break;
            }
            case "rivens": {
                f = Float.valueOf(this.rivens.cos(value));
                break;
            }
            case "java": {
                double d = value;
                boolean bl2 = false;
                f = Float.valueOf((float)Math.cos(d));
                break;
            }
            case "1.16": {
                f = Float.valueOf(this.newMC.cos(value));
                break;
            }
            default: {
                f = null;
            }
        }
        return f;
    }
}
