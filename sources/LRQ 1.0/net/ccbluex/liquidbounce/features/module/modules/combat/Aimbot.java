/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.random.Random
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="Aimbot", description="Automatically faces selected entities around you.", category=ModuleCategory.COMBAT)
public final class Aimbot
extends Module {
    private final FloatValue rangeValue = new FloatValue("Range", 4.4f, 1.0f, 8.0f);
    private final FloatValue turnSpeedValue = new FloatValue("TurnSpeed", 2.0f, 1.0f, 180.0f);
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 1.0f, 180.0f);
    private final BoolValue centerValue = new BoolValue("Center", false);
    private final BoolValue lockValue = new BoolValue("Lock", true);
    private final BoolValue onClickValue = new BoolValue("OnClick", false);
    private final BoolValue jitterValue = new BoolValue("Jitter", false);
    private final MSTimer clickTimer = new MSTimer();

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onStrafe(StrafeEvent event) {
        Object v2;
        void $this$filterTo$iv$iv;
        if (MinecraftInstance.mc.getGameSettings().getKeyBindAttack().isKeyDown()) {
            this.clickTimer.reset();
        }
        if (((Boolean)this.onClickValue.get()).booleanValue() && this.clickTimer.hasTimePassed(500L)) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        float range = ((Number)this.rangeValue.get()).floatValue();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        Iterable $this$filter$iv = iWorldClient.getLoadedEntityList();
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            IEntity it = (IEntity)element$iv$iv;
            boolean bl = false;
            if (!(EntityUtils.isSelected(it, true) && thePlayer.canEntityBeSeen(it) && PlayerExtensionKt.getDistanceToEntityBox(thePlayer, it) <= (double)range && RotationUtils.getRotationDifference(it) <= ((Number)this.fovValue.get()).doubleValue())) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$minBy$iv = (List)destination$iv$iv;
        boolean $i$f$minBy = false;
        Iterator iterator$iv = $this$minBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v2 = null;
        } else {
            Object minElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v2 = minElem$iv;
            } else {
                IEntity it = (IEntity)minElem$iv;
                boolean bl = false;
                double minValue$iv = RotationUtils.getRotationDifference(it);
                do {
                    Object e$iv = iterator$iv.next();
                    IEntity it2 = (IEntity)e$iv;
                    $i$a$-minBy-Aimbot$onStrafe$entity$2 = false;
                    double v$iv = RotationUtils.getRotationDifference(it2);
                    if (Double.compare(minValue$iv, v$iv) <= 0) continue;
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v2 = minElem$iv;
            }
        }
        IEntity iEntity = v2;
        if (iEntity == null) {
            return;
        }
        IEntity entity = iEntity;
        if (!((Boolean)this.lockValue.get()).booleanValue() && RotationUtils.isFaced(entity, range)) {
            return;
        }
        Rotation rotation = RotationUtils.limitAngleChange(new Rotation(thePlayer.getRotationYaw(), thePlayer.getRotationPitch()), (Boolean)this.centerValue.get() != false ? RotationUtils.toRotation(RotationUtils.getCenter(entity.getEntityBoundingBox()), true) : RotationUtils.searchCenter(entity.getEntityBoundingBox(), false, false, true, false, range).getRotation(), (float)(((Number)this.turnSpeedValue.get()).doubleValue() + Math.random()));
        rotation.toPlayer(thePlayer);
        if (((Boolean)this.jitterValue.get()).booleanValue()) {
            boolean yaw = Random.Default.nextBoolean();
            boolean pitch = Random.Default.nextBoolean();
            boolean yawNegative = Random.Default.nextBoolean();
            boolean pitchNegative = Random.Default.nextBoolean();
            if (yaw) {
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setRotationYaw(iEntityPlayerSP2.getRotationYaw() + (yawNegative ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
            }
            if (pitch) {
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setRotationPitch(iEntityPlayerSP3.getRotationPitch() + (pitchNegative ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
                if (thePlayer.getRotationPitch() > (float)90) {
                    thePlayer.setRotationPitch(90.0f);
                } else if (thePlayer.getRotationPitch() < (float)-90) {
                    thePlayer.setRotationPitch(-90.0f);
                }
            }
        }
    }
}

