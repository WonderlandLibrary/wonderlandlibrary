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
import net.ccbluex.liquidbounce.event.Render3DEvent;
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
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="LegitAura", description="\u6ca1\u9519\u53c8\u662f\u4ecewawa\u90a3\u6253\u6ed1\u7684", category=ModuleCategory.COMBAT)
public final class LegitAura
extends Module {
    private final IntegerValue minCPSValue;
    private final BoolValue centerValue;
    private long leftDelay;
    private final FloatValue rangeValue;
    private final BoolValue jitterValue;
    private long leftLastSwing;
    private final IntegerValue maxCPSValue = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final LegitAura this$0;
        {
            this.this$0 = legitAura;
            super(string, n, n2, n3);
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)LegitAura.access$getMinCPSValue$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
        }

        static {
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }
    };
    private final MSTimer clickTimer;
    private final BoolValue lockValue;
    private final FloatValue turnSpeedValue;
    private final FloatValue fovValue;

    public LegitAura() {
        this.minCPSValue = new IntegerValue(this, "MinCPS", 5, 1, 20){
            final LegitAura this$0;

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)LegitAura.access$getMaxCPSValue$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
            }
            {
                this.this$0 = legitAura;
                super(string, n, n2, n3);
            }

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }
        };
        this.rangeValue = new FloatValue("Range", 4.4f, 1.0f, 8.0f);
        this.turnSpeedValue = new FloatValue("TurnSpeed", 2.0f, 1.0f, 180.0f);
        this.fovValue = new FloatValue("FOV", 180.0f, 1.0f, 180.0f);
        this.centerValue = new BoolValue("Center", false);
        this.lockValue = new BoolValue("Lock", true);
        this.jitterValue = new BoolValue("Jitter", false);
        this.clickTimer = new MSTimer();
        this.leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
    }

    @EventTarget
    public final void onStrafe(StrafeEvent strafeEvent) {
        Collection collection;
        if (MinecraftInstance.mc.getGameSettings().getKeyBindAttack().isKeyDown()) {
            this.clickTimer.reset();
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

    public static final IntegerValue access$getMaxCPSValue$p(LegitAura legitAura) {
        return legitAura.maxCPSValue;
    }

    @EventTarget
    public final void onRender(Render3DEvent render3DEvent) {
        if (MinecraftInstance.mc.getGameSettings().getKeyBindAttack().isKeyDown() && System.currentTimeMillis() - this.leftLastSwing >= this.leftDelay && MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.0f) {
            MinecraftInstance.mc.getGameSettings().getKeyBindAttack().onTick(MinecraftInstance.mc.getGameSettings().getKeyBindAttack().getKeyCode());
            this.leftLastSwing = System.currentTimeMillis();
            this.leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
        }
    }

    public static final IntegerValue access$getMinCPSValue$p(LegitAura legitAura) {
        return legitAura.minCPSValue;
    }
}

