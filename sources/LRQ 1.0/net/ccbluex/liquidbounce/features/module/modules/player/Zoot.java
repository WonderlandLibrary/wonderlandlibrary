/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Iterator;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="Zoot", description="Removes all bad potion effects/fire.", category=ModuleCategory.PLAYER)
public final class Zoot
extends Module {
    private final BoolValue badEffectsValue = new BoolValue("BadEffects", true);
    private final BoolValue fireValue = new BoolValue("Fire", true);
    private final BoolValue noAirValue = new BoolValue("NoAir", false);

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        int n;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (((Boolean)this.noAirValue.get()).booleanValue() && !thePlayer.getOnGround()) {
            return;
        }
        if (((Boolean)this.badEffectsValue.get()).booleanValue()) {
            Object v1;
            Iterable $this$maxBy$iv = thePlayer.getActivePotionEffects();
            boolean $i$f$maxBy = false;
            Iterator iterator$iv = $this$maxBy$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v1 = null;
            } else {
                Object maxElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v1 = maxElem$iv;
                } else {
                    IPotionEffect it = (IPotionEffect)maxElem$iv;
                    boolean bl = false;
                    int maxValue$iv = it.getDuration();
                    do {
                        Object e$iv = iterator$iv.next();
                        IPotionEffect it2 = (IPotionEffect)e$iv;
                        $i$a$-maxBy-Zoot$onUpdate$effect$1 = false;
                        int v$iv = it2.getDuration();
                        if (maxValue$iv >= v$iv) continue;
                        maxElem$iv = e$iv;
                        maxValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v1 = maxElem$iv;
                }
            }
            IPotionEffect effect = v1;
            if (effect != null) {
                int n2 = effect.getDuration() / 20;
                n = 0;
                int n3 = 0;
                n3 = 0;
                int maxElem$iv = n2;
                while (n3 < maxElem$iv) {
                    int it = n3++;
                    boolean bl = false;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
                }
            }
        }
        if (((Boolean)this.fireValue.get()).booleanValue() && !thePlayer.getCapabilities().isCreativeMode() && thePlayer.isBurning()) {
            int n4 = 9;
            boolean bl = false;
            n = 0;
            n = 0;
            int n5 = n4;
            while (n < n5) {
                int it = n++;
                boolean bl2 = false;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
            }
        }
    }

    private final boolean hasBadEffect() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return false;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        return thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.HUNGER)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SLOWDOWN)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.DIG_SLOWDOWN)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.HARM)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.CONFUSION)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.WEAKNESS)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.WITHER)) || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.POISON));
    }
}

