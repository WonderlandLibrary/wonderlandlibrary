package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="WaterSpeed", description="Allows you to swim faster.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J02\b0\bHR0X¢\n\u0000¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/WaterSpeed;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "speedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class WaterSpeed
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 1.2f, 1.1f, 1.5f);

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.isInWater()) {
            IBlock iBlock;
            void blockPos$iv;
            WBlockPos wBlockPos = thePlayer.getPosition();
            IClassProvider iClassProvider = MinecraftInstance.classProvider;
            boolean $i$f$getBlock = false;
            Object object = MinecraftInstance.mc.getTheWorld();
            IBlock iBlock2 = object != null && (object = object.getBlockState((WBlockPos)blockPos$iv)) != null ? object.getBlock() : (iBlock = null);
            if (iClassProvider.isBlockLiquid(iBlock)) {
                float speed = ((Number)this.speedValue.get()).floatValue();
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * (double)speed);
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * (double)speed);
            }
        }
    }
}
