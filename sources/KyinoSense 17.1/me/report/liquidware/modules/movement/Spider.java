/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.AxisAlignedBB
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Spider", category=ModuleCategory.MOVEMENT, description=":/")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lme/report/liquidware/modules/movement/Spider;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "groundHeight", "", "heightValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "modifyBB", "", "motionValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "startHeight", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Spider
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Collide", "Motion"}, "Collide");
    private final IntegerValue heightValue = new IntegerValue("Height", 2, 1, 10);
    private final FloatValue motionValue = new FloatValue("Motion", 0.42f, 0.1f, 1.0f);
    private double startHeight;
    private double groundHeight;
    private boolean modifyBB;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!Spider.access$getMc$p$s1046033730().field_71439_g.field_70123_F || !Spider.access$getMc$p$s1046033730().field_71474_y.field_74351_w.field_74513_e || Spider.access$getMc$p$s1046033730().field_71439_g.field_70163_u - ((Number)this.heightValue.get()).doubleValue() > this.startHeight) {
            if (Spider.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                this.startHeight = Spider.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                this.groundHeight = Spider.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
            }
            this.modifyBB = false;
            return;
        }
        this.modifyBB = true;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case -1068318794: {
                if (!string.equals("motion")) return;
                break;
            }
            case 949448766: {
                if (!string.equals("collide") || !Spider.access$getMc$p$s1046033730().field_71439_g.field_70122_E) return;
                Spider.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
                this.groundHeight = Spider.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                return;
            }
        }
        Spider.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.motionValue.get()).floatValue();
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!Spider.access$getMc$p$s1046033730().field_71439_g.field_70123_F || !Spider.access$getMc$p$s1046033730().field_71474_y.field_74351_w.field_74513_e || Spider.access$getMc$p$s1046033730().field_71439_g.field_70163_u - ((Number)this.heightValue.get()).doubleValue() > this.startHeight) {
            return;
        }
        if (!this.modifyBB || Spider.access$getMc$p$s1046033730().field_71439_g.field_70181_x > 0.0) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 949448766: {
                if (!string.equals("collide") || !(event.getBlock() instanceof BlockAir) || !((double)event.getY() <= Spider.access$getMc$p$s1046033730().field_71439_g.field_70163_u) || !((double)event.getY() > this.groundHeight - 0.0156249) || !((double)event.getY() < this.groundHeight + 0.0156249)) break;
                event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)((double)event.getX() + 1.0), (double)((double)event.getY() + 1.0), (double)((double)event.getZ() + 1.0)));
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

