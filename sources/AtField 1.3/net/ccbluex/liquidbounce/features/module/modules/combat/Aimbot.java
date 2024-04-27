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
    private final BoolValue onClickValue;
    private final FloatValue fovValue;
    private final FloatValue turnSpeedValue;
    private final FloatValue rangeValue = new FloatValue("Range", 4.4f, 1.0f, 8.0f);
    private final BoolValue jitterValue;
    private final BoolValue lockValue;
    private final BoolValue centerValue;
    private final MSTimer clickTimer;

    @EventTarget
    public final void onStrafe(StrafeEvent strafeEvent) {
        Collection collection;
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
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        float f = ((Number)this.rangeValue.get()).floatValue();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        Object object = iWorldClient.getLoadedEntityList();
        boolean bl = false;
        Iterator iterator2 = object;
        Collection collection2 = new ArrayList();
        boolean bl2 = false;
        Iterator iterator3 = iterator2.iterator();
        while (iterator3.hasNext()) {
            Object t = iterator3.next();
            IEntity iEntity = (IEntity)t;
            boolean bl3 = false;
            if (!(EntityUtils.isSelected(iEntity, true) && iEntityPlayerSP2.canEntityBeSeen(iEntity) && PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP2, iEntity) <= (double)f && RotationUtils.getRotationDifference(iEntity) <= ((Number)this.fovValue.get()).doubleValue())) continue;
            collection2.add(t);
        }
        object = (List)collection2;
        bl = false;
        iterator2 = object.iterator();
        if (!iterator2.hasNext()) {
            collection = null;
        } else {
            collection2 = iterator2.next();
            if (!iterator2.hasNext()) {
                collection = collection2;
            } else {
                IEntity iEntity = (IEntity)((Object)collection2);
                boolean bl4 = false;
                double d = RotationUtils.getRotationDifference(iEntity);
                do {
                    Object e = iterator2.next();
                    IEntity iEntity2 = (IEntity)e;
                    boolean bl5 = false;
                    double d2 = RotationUtils.getRotationDifference(iEntity2);
                    if (Double.compare(d, d2) <= 0) continue;
                    collection2 = e;
                    d = d2;
                } while (iterator2.hasNext());
                collection = collection2;
            }
        }
        IEntity iEntity = (IEntity)((Object)collection);
        if (iEntity == null) {
            return;
        }
        IEntity iEntity3 = iEntity;
        if (!((Boolean)this.lockValue.get()).booleanValue() && RotationUtils.isFaced(iEntity3, f)) {
            return;
        }
        object = RotationUtils.limitAngleChange(new Rotation(iEntityPlayerSP2.getRotationYaw(), iEntityPlayerSP2.getRotationPitch()), (Boolean)this.centerValue.get() != false ? RotationUtils.toRotation(RotationUtils.getCenter(iEntity3.getEntityBoundingBox()), true) : RotationUtils.searchCenter(iEntity3.getEntityBoundingBox(), false, false, true, false, f).getRotation(), (float)(((Number)this.turnSpeedValue.get()).doubleValue() + Math.random()));
        ((Rotation)object).toPlayer(iEntityPlayerSP2);
        if (((Boolean)this.jitterValue.get()).booleanValue()) {
            bl = Random.Default.nextBoolean();
            boolean bl6 = Random.Default.nextBoolean();
            boolean bl7 = Random.Default.nextBoolean();
            bl2 = Random.Default.nextBoolean();
            if (bl) {
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setRotationYaw(iEntityPlayerSP3.getRotationYaw() + (bl7 ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
            }
            if (bl6) {
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                iEntityPlayerSP4.setRotationPitch(iEntityPlayerSP4.getRotationPitch() + (bl2 ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
                if (iEntityPlayerSP2.getRotationPitch() > (float)90) {
                    iEntityPlayerSP2.setRotationPitch(90.0f);
                } else if (iEntityPlayerSP2.getRotationPitch() < (float)-90) {
                    iEntityPlayerSP2.setRotationPitch(-90.0f);
                }
            }
        }
    }

    public Aimbot() {
        this.turnSpeedValue = new FloatValue("TurnSpeed", 2.0f, 1.0f, 180.0f);
        this.fovValue = new FloatValue("FOV", 180.0f, 1.0f, 180.0f);
        this.centerValue = new BoolValue("Center", false);
        this.lockValue = new BoolValue("Lock", true);
        this.onClickValue = new BoolValue("OnClick", false);
        this.jitterValue = new BoolValue("Jitter", false);
        this.clickTimer = new MSTimer();
    }
}

