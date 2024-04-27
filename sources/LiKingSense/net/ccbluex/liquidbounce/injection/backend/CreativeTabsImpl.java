/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.creativetab.CreativeTabs
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.creativetabs.ICreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/CreativeTabsImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/creativetabs/ICreativeTabs;", "wrapped", "Lnet/minecraft/creativetab/CreativeTabs;", "(Lnet/minecraft/creativetab/CreativeTabs;)V", "value", "", "backgroundImageName", "getBackgroundImageName", "()Ljava/lang/String;", "setBackgroundImageName", "(Ljava/lang/String;)V", "getWrapped", "()Lnet/minecraft/creativetab/CreativeTabs;", "equals", "", "other", "", "LiKingSense"})
public final class CreativeTabsImpl
implements ICreativeTabs {
    @NotNull
    private final CreativeTabs wrapped;

    @Override
    @NotNull
    public String getBackgroundImageName() {
        String string = this.wrapped.func_78015_f();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.backgroundImageName");
        return string;
    }

    @Override
    public void setBackgroundImageName(@NotNull String value) {
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        this.wrapped.func_78025_a(value);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof CreativeTabsImpl && Intrinsics.areEqual((Object)((CreativeTabsImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final CreativeTabs getWrapped() {
        return this.wrapped;
    }

    public CreativeTabsImpl(@NotNull CreativeTabs wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

