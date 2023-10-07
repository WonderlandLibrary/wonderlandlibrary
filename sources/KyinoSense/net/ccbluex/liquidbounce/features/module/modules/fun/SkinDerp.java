/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="SkinDerp", description="Makes your skin blink (Requires multi-layer skin).", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0012H\u0016J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/SkinDerp;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hatValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "jacketValue", "leftPantsValue", "leftSleeveValue", "prevModelParts", "", "Lnet/minecraft/entity/player/EnumPlayerModelParts;", "rightPantsValue", "rightSleeveValue", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "onDisable", "", "onEnable", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class SkinDerp
extends Module {
    private final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 1000);
    private final BoolValue hatValue = new BoolValue("Hat", true);
    private final BoolValue jacketValue = new BoolValue("Jacket", true);
    private final BoolValue leftPantsValue = new BoolValue("LeftPants", true);
    private final BoolValue rightPantsValue = new BoolValue("RightPants", true);
    private final BoolValue leftSleeveValue = new BoolValue("LeftSleeve", true);
    private final BoolValue rightSleeveValue = new BoolValue("RightSleeve", true);
    private Set<? extends EnumPlayerModelParts> prevModelParts = SetsKt.emptySet();
    private final MSTimer timer = new MSTimer();

    @Override
    public void onEnable() {
        GameSettings gameSettings = SkinDerp.access$getMc$p$s1046033730().field_71474_y;
        Intrinsics.checkExpressionValueIsNotNull(gameSettings, "mc.gameSettings");
        Set set = gameSettings.func_178876_d();
        Intrinsics.checkExpressionValueIsNotNull(set, "mc.gameSettings.modelParts");
        this.prevModelParts = set;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        GameSettings gameSettings = SkinDerp.access$getMc$p$s1046033730().field_71474_y;
        Intrinsics.checkExpressionValueIsNotNull(gameSettings, "mc.gameSettings");
        for (EnumPlayerModelParts enumPlayerModelParts : gameSettings.func_178876_d()) {
            SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(enumPlayerModelParts, false);
        }
        for (EnumPlayerModelParts enumPlayerModelParts : this.prevModelParts) {
            SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(enumPlayerModelParts, true);
        }
        super.onDisable();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            if (((Boolean)this.hatValue.get()).booleanValue()) {
                SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.HAT, Random.Default.nextBoolean());
            }
            if (((Boolean)this.jacketValue.get()).booleanValue()) {
                SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.JACKET, Random.Default.nextBoolean());
            }
            if (((Boolean)this.leftPantsValue.get()).booleanValue()) {
                SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.LEFT_PANTS_LEG, Random.Default.nextBoolean());
            }
            if (((Boolean)this.rightPantsValue.get()).booleanValue()) {
                SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.RIGHT_PANTS_LEG, Random.Default.nextBoolean());
            }
            if (((Boolean)this.leftSleeveValue.get()).booleanValue()) {
                SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.LEFT_SLEEVE, Random.Default.nextBoolean());
            }
            if (((Boolean)this.rightSleeveValue.get()).booleanValue()) {
                SkinDerp.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.RIGHT_SLEEVE, Random.Default.nextBoolean());
            }
            this.timer.reset();
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

