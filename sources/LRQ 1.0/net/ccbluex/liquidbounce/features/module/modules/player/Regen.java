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
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Spartan"}, "Vanilla");
    private final IntegerValue healthValue = new IntegerValue("Health", 18, 0, 20);
    private final IntegerValue foodValue = new IntegerValue("Food", 18, 0, 20);
    private final IntegerValue speedValue = new IntegerValue("Speed", 100, 1, 100);
    private final BoolValue noAirValue = new BoolValue("NoAir", false);
    private final BoolValue potionEffectValue = new BoolValue("PotionEffect", false);
    private boolean resetTimer;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        if (this.resetTimer) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        }
        this.resetTimer = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (((Boolean)this.noAirValue.get()).booleanValue() && !thePlayer.getOnGround() || thePlayer.getCapabilities().isCreativeMode() || thePlayer.getFoodStats().getFoodLevel() <= ((Number)this.foodValue.get()).intValue() || !thePlayer.isEntityAlive() || !(thePlayer.getHealth() < ((Number)this.healthValue.get()).floatValue())) return;
        if (((Boolean)this.potionEffectValue.get()).booleanValue() && !thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION))) {
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
                    int it = n2++;
                    boolean bl2 = false;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
                }
                return;
            }
        }
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
    }
}

