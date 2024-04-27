/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityKilledEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u001d\u001a\u00020\u001eJ\b\u0010\u001f\u001a\u00020\rH\u0016J\u000e\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\nJ\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0007J\u0010\u0010&\u001a\u00020#2\u0006\u0010$\u001a\u00020'H\u0007J\u0010\u0010(\u001a\u00020#2\u0006\u0010$\u001a\u00020)H\u0007R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\bR\u001e\u0010\u000e\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u0019\u001a\u0004\u0018\u00010\u00062\b\u0010\f\u001a\u0004\u0018\u00010\u0006@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006*"}, d2={"Lnet/ccbluex/liquidbounce/management/CombatManager;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "attackedEntityList", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getAttackedEntityList", "()Ljava/util/List;", "focusedPlayerList", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "getFocusedPlayerList", "<set-?>", "", "inCombat", "getInCombat", "()Z", "kills", "", "getKills", "()I", "setKills", "(I)V", "lastAttackTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "target", "getTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getNearByEntity", "radius", "", "handleEvents", "isFocusEntity", "entity", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "LiKingSense"})
public final class CombatManager
extends MinecraftInstance
implements Listenable {
    private final MSTimer lastAttackTimer = new MSTimer();
    private int kills;
    private boolean inCombat;
    @Nullable
    private IEntityLivingBase target;
    @NotNull
    private final List<IEntityLivingBase> attackedEntityList;
    @NotNull
    private final List<IEntityPlayer> focusedPlayerList;

    public final int getKills() {
        return this.kills;
    }

    public final void setKills(int n) {
        this.kills = n;
    }

    public final boolean getInCombat() {
        return this.inCombat;
    }

    @Nullable
    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    @NotNull
    public final List<IEntityLivingBase> getAttackedEntityList() {
        return this.attackedEntityList;
    }

    @NotNull
    public final List<IEntityPlayer> getFocusedPlayerList() {
        return this.focusedPlayerList;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        void $this$mapTo$iv$iv;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        Iterable $this$map$iv = this.attackedEntityList;
        boolean $i$f$map = false;
        Iterable iterable = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void it;
            IEntityLivingBase iEntityLivingBase = (IEntityLivingBase)item$iv$iv;
            Collection collection = destination$iv$iv;
            boolean bl = false;
            void var12_15 = it;
            collection.add(var12_15);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            IEntityLivingBase it = (IEntityLivingBase)element$iv;
            boolean bl = false;
            if (!it.isDead()) continue;
            LiquidBounce.INSTANCE.getEventManager().callEvent(new EntityKilledEvent(it));
            int n = this.kills;
            this.kills = n + 1;
            this.attackedEntityList.remove(it);
        }
        this.inCombat = false;
        if (!this.lastAttackTimer.hasTimePassed(1000L)) {
            this.inCombat = true;
            return;
        }
        if (this.target != null) {
            if (MinecraftInstance.mc.getThePlayer().getDistanceToEntity(this.target) > (float)7 || !this.inCombat || this.target.isDead()) {
                this.target = null;
            } else {
                this.inCombat = true;
            }
        }
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntity target = event.getTargetEntity();
        if (MinecraftInstance.classProvider.isEntityLivingBase(target) && EntityUtils.isSelected(target, true)) {
            this.target = (IEntityLivingBase)target;
            if (!CollectionsKt.contains((Iterable)this.attackedEntityList, (Object)target)) {
                this.attackedEntityList.add((IEntityLivingBase)target);
            }
        }
        this.lastAttackTimer.reset();
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        this.inCombat = false;
        this.target = null;
        this.attackedEntityList.clear();
        this.focusedPlayerList.clear();
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public final IEntityLivingBase getNearByEntity(float radius) {
        IEntityLivingBase iEntityLivingBase;
        try {
            void $this$filterTo$iv$iv;
            Iterable $this$filter$iv = MinecraftInstance.mc.getTheWorld().getLoadedEntityList();
            boolean $i$f$filter = false;
            Iterable iterable = $this$filter$iv;
            Collection destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                IEntity it = (IEntity)element$iv$iv;
                boolean bl = false;
                if (!(MinecraftInstance.mc.getThePlayer().getDistanceToEntity(it) < radius && EntityUtils.isSelected(it, true))) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            Iterable $this$sortedBy$iv = (List)destination$iv$iv;
            boolean $i$f$sortedBy = false;
            iterable = $this$sortedBy$iv;
            boolean bl = false;
            Comparator comparator = new Comparator<T>(){

                public final int compare(T a, T b) {
                    boolean bl = false;
                    IEntity it = (IEntity)a;
                    boolean bl2 = false;
                    Comparable comparable = Float.valueOf(it.getDistanceToEntity(MinecraftInstance.mc.getThePlayer()));
                    it = (IEntity)b;
                    Comparable comparable2 = comparable;
                    bl2 = false;
                    Float f = Float.valueOf(it.getDistanceToEntity(MinecraftInstance.mc.getThePlayer()));
                    return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)f);
                }
            };
            iEntityLivingBase = (IEntityLivingBase)CollectionsKt.sortedWith((Iterable)iterable, (Comparator)comparator).get(0);
        }
        catch (Exception e) {
            iEntityLivingBase = null;
        }
        return iEntityLivingBase;
    }

    public final boolean isFocusEntity(@NotNull IEntityPlayer entity) {
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        if (this.focusedPlayerList.isEmpty()) {
            return true;
        }
        return this.focusedPlayerList.contains(entity);
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public CombatManager() {
        List list;
        CombatManager combatManager = this;
        boolean bl = false;
        combatManager.attackedEntityList = list = (List)new ArrayList();
        combatManager = this;
        bl = false;
        combatManager.focusedPlayerList = list = (List)new ArrayList();
    }
}

