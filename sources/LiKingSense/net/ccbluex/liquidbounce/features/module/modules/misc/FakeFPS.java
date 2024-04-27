/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.misc.FakeFPS;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;

@ModuleInfo(name="FakeFPS", description="", category=ModuleCategory.MISC, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/FakeFPS;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "fps", "", "maxFps", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "minFps", "minecraftFPS", "getFakeFPS", "LiKingSense"})
public final class FakeFPS
extends Module {
    public final IntegerValue maxFps;
    public final IntegerValue minFps;
    public int minecraftFPS;
    public int fps;

    public final int getFakeFPS() {
        if (this.minecraftFPS != Minecraft.field_71470_ab) {
            this.fps = RandomUtils.nextInt(((Number)this.minFps.get()).intValue(), ((Number)this.maxFps.get()).intValue());
            this.minecraftFPS = Minecraft.field_71470_ab;
        }
        return this.fps;
    }

    public FakeFPS() {
        minFps.1 v3;
        maxFps.1 v2;
        FakeFPS fakeFPS = this;
        maxFps.1 v1 = v2;
        ((FakeFPS)((Object)"MaxFPS")).maxFps = (IntegerValue)0;
        super((FakeFPS)((Object)v3), (String)((Object)v3), (int)this, (int)"MinFPS", 0);
        ((FakeFPS)((Object)v2)).minFps = (IntegerValue)((Object)this);
    }

    public static final /* synthetic */ IntegerValue access$getMinFps$p(FakeFPS $this) {
        return $this.minFps;
    }

    public static final /* synthetic */ IntegerValue access$getMaxFps$p(FakeFPS $this) {
        return $this.maxFps;
    }
}

