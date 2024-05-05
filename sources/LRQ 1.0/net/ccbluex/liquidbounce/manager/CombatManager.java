/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.manager;

import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityKilledEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import org.jetbrains.annotations.Nullable;

public final class CombatManager
extends MinecraftInstance
implements Listenable {
    private final MSTimer lastAttackTimer = new MSTimer();
    private boolean inCombat;
    private int kills;
    private IEntityLivingBase target;
    private final ArrayList<IEntityLivingBase> attackedList = new ArrayList();

    public final boolean getInCombat() {
        return this.inCombat;
    }

    public final void setInCombat(boolean bl) {
        this.inCombat = bl;
    }

    public final int getKills() {
        return this.kills;
    }

    public final void setKills(int n) {
        this.kills = n;
    }

    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    @EventTarget
    public final void onAttack(AttackEvent event) {
        if (event.getTargetEntity() instanceof IEntityLivingBase) {
            this.target = (IEntityLivingBase)event.getTargetEntity();
            this.attackedList.add((IEntityLivingBase)event.getTargetEntity());
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
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
        $this$map$iv = this.attackedList;
        $i$f$map = false;
        var4_4 = $this$map$iv;
        destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
        $i$f$mapTo = false;
        for (T item$iv$iv : $this$mapTo$iv$iv) {
            var9_11 = (IEntityLivingBase)item$iv$iv;
            var11_13 = destination$iv$iv;
            $i$a$-map-CombatManager$onUpdate$1 = false;
            var12_14 = it;
            var11_13.add(var12_14);
        }
        $this$forEach$iv = (List)destination$iv$iv;
        $i$f$forEach = false;
        for (E element$iv : $this$forEach$iv) {
            it = (IEntityLivingBase)element$iv;
            $i$a$-forEach-CombatManager$onUpdate$2 = false;
            if (!it.isDead() && !(it.getHealth() <= (float)false)) continue;
            this.attackedList.remove(it);
            ++this.kills;
            LiquidBounce.INSTANCE.getEventManager().callEvent(new EntityKilledEvent(it));
        }
    }

    @EventTarget
    public final void onWorld(WorldEvent event) {
        this.inCombat = false;
        this.target = null;
    }
}

