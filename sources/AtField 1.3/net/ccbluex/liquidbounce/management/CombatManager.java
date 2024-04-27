/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityKilledEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import org.jetbrains.annotations.Nullable;

public final class CombatManager
extends MinecraftInstance
implements Listenable {
    private boolean inCombat;
    private final MSTimer lastAttackTimer = new MSTimer();
    private int kills;
    private final ArrayList attackedList = new ArrayList();
    private IEntityLivingBase target;

    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        if (attackEvent.getTargetEntity() instanceof IEntityLivingBase) {
            this.target = (IEntityLivingBase)attackEvent.getTargetEntity();
            this.attackedList.add(attackEvent.getTargetEntity());
        }
    }

    @EventTarget
    public final void onWorld(WorldEvent worldEvent) {
        this.inCombat = false;
        this.target = null;
    }

    public final void setInCombat(boolean bl) {
        this.inCombat = bl;
    }

    public final void setKills(int n) {
        this.kills = n;
    }

    public final int getKills() {
        return this.kills;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    public final boolean getInCombat() {
        return this.inCombat;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @EventTarget
    public final void onUpdate(UpdateEvent var1_1) {
        block17: {
            block15: {
                block16: {
                    if (this.target == null) break block15;
                    v0 = MinecraftInstance.mc.getThePlayer();
                    if (v0 == null) {
                        Intrinsics.throwNpe();
                    }
                    v1 = v0;
                    v2 = this.target;
                    if (v2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (PlayerExtensionKt.getDistanceToEntityBox(v1, v2) > (double)7) break block16;
                    v3 = this.target;
                    if (v3 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v3.getHealth() <= (float)false) break block16;
                    v4 = this.target;
                    if (v4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!v4.isDead()) break block15;
                }
                this.target = null;
            }
            this.inCombat = false;
            if (!this.lastAttackTimer.hasTimePassed(1000L)) {
                this.inCombat = true;
                return;
            }
            if (this.target == null) break block17;
            v5 = MinecraftInstance.mc.getThePlayer();
            if (v5 == null) {
                Intrinsics.throwNpe();
            }
            v6 = this.target;
            if (v6 == null) {
                Intrinsics.throwNpe();
            }
            if (v5.getDistanceToEntity(v6) > (float)7 || !this.inCombat) ** GOTO lbl-1000
            v7 = this.target;
            if (v7 == null) {
                Intrinsics.throwNpe();
            }
            if (v7.isDead()) lbl-1000:
            // 2 sources

            {
                this.target = null;
            } else {
                this.inCombat = true;
            }
        }
        var2_2 = this.attackedList;
        var3_3 = false;
        var4_4 = var2_2;
        var5_5 /* !! */  = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)var2_2, (int)10));
        var6_6 = false;
        var7_8 = var4_4.iterator();
        while (var7_8.hasNext()) {
            var8_10 = var7_8.next();
            var9_11 = (IEntityLivingBase)var8_10;
            var11_13 = var5_5 /* !! */ ;
            var10_12 = false;
            var12_14 = var9_11;
            var11_13.add(var12_14);
        }
        var2_2 = (List)var5_5 /* !! */ ;
        var3_3 = false;
        for (Collection var5_5 : var2_2) {
            var6_7 = (IEntityLivingBase)var5_5 /* !! */ ;
            var7_9 = false;
            if (!var6_7.isDead() && !(var6_7.getHealth() <= (float)false)) continue;
            this.attackedList.remove(var6_7);
            ++this.kills;
            LiquidBounce.INSTANCE.getEventManager().callEvent(new EntityKilledEvent(var6_7));
        }
    }

    public final IEntityLivingBase getNearByEntity(float f) {
        Object object;
        try {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            object = iWorldClient.getLoadedEntityList();
            boolean bl = false;
            Iterable iterable = object;
            Collection collection = new ArrayList();
            boolean bl2 = false;
            for (Object t : iterable) {
                IEntity iEntity = (IEntity)t;
                boolean bl3 = false;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getDistanceToEntity(iEntity) < f && EntityUtils.isSelected(iEntity, true))) continue;
                collection.add(t);
            }
            object = (List)collection;
            bl = false;
            iterable = object;
            boolean bl4 = false;
            Comparator comparator = new Comparator(){

                static {
                }

                public final int compare(Object object, Object object2) {
                    boolean bl = false;
                    IEntity iEntity = (IEntity)object;
                    boolean bl2 = false;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    Comparable comparable = Float.valueOf(iEntity.getDistanceToEntity(iEntityPlayerSP));
                    iEntity = (IEntity)object2;
                    Comparable comparable2 = comparable;
                    bl2 = false;
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    Float f = Float.valueOf(iEntity.getDistanceToEntity(iEntityPlayerSP2));
                    return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)f);
                }
            };
            object = (IEntityLivingBase)CollectionsKt.sortedWith((Iterable)iterable, (Comparator)comparator).get(0);
        }
        catch (Exception exception) {
            object = null;
        }
        return object;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

