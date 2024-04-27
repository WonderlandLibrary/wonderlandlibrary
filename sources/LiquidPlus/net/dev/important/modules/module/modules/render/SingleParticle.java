/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2={"Lnet/dev/important/modules/module/modules/render/SingleParticle;", "", "str", "", "posX", "", "posY", "posZ", "(Ljava/lang/String;DDD)V", "getPosX", "()D", "getPosY", "getPosZ", "getStr", "()Ljava/lang/String;", "ticks", "", "getTicks", "()I", "setTicks", "(I)V", "LiquidBounce"})
public final class SingleParticle {
    @NotNull
    private final String str;
    private final double posX;
    private final double posY;
    private final double posZ;
    private int ticks;

    public SingleParticle(@NotNull String str, double posX, double posY, double posZ) {
        Intrinsics.checkNotNullParameter(str, "str");
        this.str = str;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    @NotNull
    public final String getStr() {
        return this.str;
    }

    public final double getPosX() {
        return this.posX;
    }

    public final double getPosY() {
        return this.posY;
    }

    public final double getPosZ() {
        return this.posZ;
    }

    public final int getTicks() {
        return this.ticks;
    }

    public final void setTicks(int n) {
        this.ticks = n;
    }
}

