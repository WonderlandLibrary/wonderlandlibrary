/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.TypeCastException;
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

@ModuleInfo(name="Regen", description="Regenerates your health much faster.", category=ModuleCategory.PLAYER)
public final class Regen
extends Module {
    private final IntegerValue speedValue;
    private final BoolValue noAirValue;
    private final BoolValue potionEffectValue;
    private final IntegerValue foodValue;
    private boolean resetTimer;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Spartan"}, "Vanilla");
    private final IntegerValue healthValue = new IntegerValue("Health", 18, 0, 20);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        if (this.resetTimer) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        }
        this.resetTimer = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.noAirValue.get()).booleanValue() && !iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.getCapabilities().isCreativeMode() || iEntityPlayerSP2.getFoodStats().getFoodLevel() <= ((Number)this.foodValue.get()).intValue() || !iEntityPlayerSP2.isEntityAlive() || !(iEntityPlayerSP2.getHealth() < ((Number)this.healthValue.get()).floatValue())) return;
        if (((Boolean)this.potionEffectValue.get()).booleanValue() && !iEntityPlayerSP2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION))) {
            return;
        }
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case -2011701869: {
                if (!string.equals("spartan")) return;
                break;
            }
            case 233102203: {
                if (!string.equals("vanilla")) return;
                n = ((Number)this.speedValue.get()).intValue();
                boolean bl = false;
                int n2 = 0;
                n2 = 0;
                int n3 = n;
                while (n2 < n3) {
                    int n4 = n2++;
                    boolean bl2 = false;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP2.getOnGround()));
                }
                return;
            }
        }
        if (MovementUtils.isMoving() || !iEntityPlayerSP2.getOnGround()) {
            return;
        }
        n = 9;
        boolean bl = false;
        int n5 = 0;
        n5 = 0;
        int n6 = n;
        while (n5 < n6) {
            int n7 = n5++;
            boolean bl3 = false;
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP2.getOnGround()));
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(0.45f);
        this.resetTimer = true;
        return;
    }

    public Regen() {
        this.foodValue = new IntegerValue("Food", 18, 0, 20);
        this.speedValue = new IntegerValue("Speed", 100, 1, 100);
        this.noAirValue = new BoolValue("NoAir", false);
        this.potionEffectValue = new BoolValue("PotionEffect", false);
    }
}

