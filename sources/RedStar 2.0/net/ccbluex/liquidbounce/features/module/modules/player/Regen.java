package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Regen", description="Regenerates your health much faster.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J020HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0\tX¢\n\u0000R0\fX¢\n\u0000R\r0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/Regen;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "foodValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "healthValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "noAirValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "potionEffectValue", "resetTimer", "", "speedValue", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class Regen
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Spartan", "NewSpartan"}, "Vanilla");
    private final IntegerValue healthValue = new IntegerValue("Health", 18, 0, 20);
    private final IntegerValue foodValue = new IntegerValue("Food", 18, 0, 20);
    private final IntegerValue speedValue = new IntegerValue("Speed", 100, 1, 100);
    private final BoolValue noAirValue = new BoolValue("NoAir", false);
    private final BoolValue potionEffectValue = new BoolValue("PotionEffect", false);
    private boolean resetTimer;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.resetTimer) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        }
        this.resetTimer = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if ((!((Boolean)this.noAirValue.get()).booleanValue() || thePlayer.getOnGround()) && !thePlayer.getCapabilities().isCreativeMode() && thePlayer.getFoodStats().getFoodLevel() > ((Number)this.foodValue.get()).intValue() && thePlayer.isEntityAlive() && thePlayer.getHealth() < ((Number)this.healthValue.get()).floatValue()) {
            if (((Boolean)this.potionEffectValue.get()).booleanValue() && !thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION))) {
                return;
            }
            String string = (String)this.modeValue.get();
            int n = 0;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "newspartan": {
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP2.getTicksExisted() % 5 == 0) {
                        this.resetTimer = true;
                    }
                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.98f);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                    break;
                }
                case "vanilla": {
                    n = ((Number)this.speedValue.get()).intValue();
                    boolean bl = false;
                    int n2 = 0;
                    n2 = 0;
                    int n3 = n;
                    while (n2 < n3) {
                        int it = n2++;
                        boolean bl2 = false;
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
                    }
                    break;
                }
                case "spartan": {
                    if (MovementUtils.isMoving() || !thePlayer.getOnGround()) {
                        return;
                    }
                    n = 9;
                    boolean bl = false;
                    int n4 = 0;
                    n4 = 0;
                    int n5 = n;
                    while (n4 < n5) {
                        int it = n4++;
                        boolean bl3 = false;
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
                    }
                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.45f);
                    this.resetTimer = true;
                    break;
                }
            }
        }
    }
}
