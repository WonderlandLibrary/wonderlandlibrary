package me.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000T\n\n\n\n\b\n\b\n\b\n\n\b\n\n\b\n\n\b\b\n\n\b\t\n\n\b\n\n\u0000\n\n\b\n\n\u0000\u00002020B¢J-0%2.0/J\b00HJ1022304HJ5022304HJ6022307HR0X¢\n\u0000\b\"\b\b\tR\n0X¢\n\u0000\b\f\r\"\bR0X¢\n\u0000\b\"\b\tR0X¢\n\u0000\b\"\b\tR0X¢\n\u0000R0X¢\n\u0000\b\"\b\tR0X¢\n\u0000\b\"\b R!0X¢\n\u0000\b\"\"\b#\tR$0%X¢\n\u0000\b&'\"\b()R*0X¢\n\u0000\b+\"\b,\t¨8"}, d2={"Lme/manager/CombatManager;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "hours", "", "getHours", "()I", "setHours", "(I)V", "inCombat", "", "getInCombat", "()Z", "setInCombat", "(Z)V", "kill", "getKill", "setKill", "killedEntities", "getKilledEntities", "setKilledEntities", "lastAttackTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "minutes", "getMinutes", "setMinutes", "playedTime", "", "getPlayedTime", "()Ljava/lang/String;", "setPlayedTime", "(Ljava/lang/String;)V", "seconds", "getSeconds", "setSeconds", "target", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setTarget", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "ticks", "getTicks", "setTicks", "getNearByEntity", "radius", "", "handleEvents", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onKilled", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class CombatManager
extends MinecraftInstance
implements Listenable {
    private int kill;
    private boolean inCombat;
    private final MSTimer lastAttackTimer = new MSTimer();
    @Nullable
    private IEntityLivingBase target;
    private int killedEntities;
    private int ticks;
    @NotNull
    private String playedTime = "0h 0m 0s";
    private int seconds;
    private int minutes;
    private int hours;

    public final int getKill() {
        return this.kill;
    }

    public final void setKill(int n) {
        this.kill = n;
    }

    public final boolean getInCombat() {
        return this.inCombat;
    }

    public final void setInCombat(boolean bl) {
        this.inCombat = bl;
    }

    @Nullable
    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    public final int getKilledEntities() {
        return this.killedEntities;
    }

    public final void setKilledEntities(int n) {
        this.killedEntities = n;
    }

    public final int getTicks() {
        return this.ticks;
    }

    public final void setTicks(int n) {
        this.ticks = n;
    }

    @NotNull
    public final String getPlayedTime() {
        return this.playedTime;
    }

    public final void setPlayedTime(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.playedTime = string;
    }

    public final int getSeconds() {
        return this.seconds;
    }

    public final void setSeconds(int n) {
        this.seconds = n;
    }

    public final int getMinutes() {
        return this.minutes;
    }

    public final void setMinutes(int n) {
        this.minutes = n;
    }

    public final int getHours() {
        return this.hours;
    }

    public final void setHours(int n) {
        this.hours = n;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block14: {
            block15: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                ++this.ticks;
                if (this.ticks == 20) {
                    ++this.seconds;
                    this.ticks = 0;
                }
                if (this.seconds == 60) {
                    ++this.minutes;
                    this.seconds = 0;
                }
                if (this.minutes == 60) {
                    ++this.hours;
                    this.minutes = 0;
                }
                this.playedTime = String.valueOf(this.hours) + "h " + String.valueOf(this.minutes) + "m " + String.valueOf(this.seconds) + "s";
                if (MinecraftInstance.mc.getThePlayer() == null) {
                    return;
                }
                MovementUtils.INSTANCE.updateBlocksPerSecond();
                this.inCombat = false;
                if (!this.lastAttackTimer.hasTimePassed(1000L)) {
                    this.inCombat = true;
                    return;
                }
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                for (IEntity entity : iWorldClient.getLoadedEntityList()) {
                    if (!(entity instanceof IEntityLivingBase)) continue;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(entity.getDistanceToEntity(iEntityPlayerSP) < (float)7) || !EntityUtils.isSelected(entity, true)) continue;
                    this.inCombat = true;
                    break;
                }
                if (this.target == null) break block14;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                IEntityLivingBase iEntityLivingBase = this.target;
                if (iEntityLivingBase == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getDistanceToEntity(iEntityLivingBase) > (float)7 || !this.inCombat) break block15;
                IEntityLivingBase iEntityLivingBase2 = this.target;
                if (iEntityLivingBase2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityLivingBase2.isDead()) break block14;
            }
            this.target = null;
        }
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getTargetEntity() instanceof IEntityLivingBase && EntityUtils.isSelected(event.getTargetEntity(), true)) {
            this.target = (IEntityLivingBase)event.getTargetEntity();
        }
        this.lastAttackTimer.reset();
    }

    @EventTarget
    public final void onKilled(@NotNull AttackEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntity iEntity = event.getTargetEntity();
        if (iEntity == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase");
        }
        IEntityLivingBase target = (IEntityLivingBase)iEntity;
        if (!(target instanceof EntityPlayer)) {
            return;
        }
        ++this.killedEntities;
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public final IEntityLivingBase getNearByEntity(float radius) {
        IEntityLivingBase iEntityLivingBase;
        try {
            void $this$filterTo$iv$iv;
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
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getDistanceToEntity(it) < radius && EntityUtils.isSelected(it, true))) continue;
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
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    Comparable comparable = Float.valueOf(it.getDistanceToEntity(iEntityPlayerSP));
                    it = (IEntity)b;
                    Comparable comparable2 = comparable;
                    bl2 = false;
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    Float f = Float.valueOf(it.getDistanceToEntity(iEntityPlayerSP2));
                    return ComparisonsKt.compareValues(comparable2, (Comparable)f);
                }
            };
            iEntityLivingBase = (IEntityLivingBase)CollectionsKt.sortedWith(iterable, comparator).get(0);
        }
        catch (Exception e) {
            iEntityLivingBase = null;
        }
        return iEntityLivingBase;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}
