/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S45PacketTitle
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0003J\u0010\u0010$\u001a\u00020!2\u0006\u0010\"\u001a\u00020%H\u0003J\u0010\u0010&\u001a\u00020!2\u0006\u0010\"\u001a\u00020'H\u0003R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\bR\u001a\u0010\u001b\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0006\"\u0004\b\u001d\u0010\b\u00a8\u0006("}, d2={"Lnet/ccbluex/liquidbounce/utils/Recorder;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "ban", "", "getBan", "()I", "setBan", "(I)V", "killCounts", "getKillCounts", "setKillCounts", "startTime", "", "getStartTime", "()J", "setStartTime", "(J)V", "syncEntity", "Lnet/minecraft/entity/EntityLivingBase;", "getSyncEntity", "()Lnet/minecraft/entity/EntityLivingBase;", "setSyncEntity", "(Lnet/minecraft/entity/EntityLivingBase;)V", "totalPlayed", "getTotalPlayed", "setTotalPlayed", "win", "getWin", "setWin", "handleEvents", "", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Recorder
implements Listenable {
    @Nullable
    private static EntityLivingBase syncEntity;
    private static int killCounts;
    private static int totalPlayed;
    private static int win;
    private static int ban;
    private static long startTime;
    public static final Recorder INSTANCE;

    @Nullable
    public final EntityLivingBase getSyncEntity() {
        return syncEntity;
    }

    public final void setSyncEntity(@Nullable EntityLivingBase entityLivingBase) {
        syncEntity = entityLivingBase;
    }

    public final int getKillCounts() {
        return killCounts;
    }

    public final void setKillCounts(int n) {
        killCounts = n;
    }

    public final int getTotalPlayed() {
        return totalPlayed;
    }

    public final void setTotalPlayed(int n) {
        totalPlayed = n;
    }

    public final int getWin() {
        return win;
    }

    public final void setWin(int n) {
        win = n;
    }

    public final int getBan() {
        return ban;
    }

    public final void setBan(int n) {
        ban = n;
    }

    public final long getStartTime() {
        return startTime;
    }

    public final void setStartTime(long l) {
        startTime = l;
    }

    @EventTarget
    private final void onAttack(AttackEvent event) {
        syncEntity = (EntityLivingBase)event.getTargetEntity();
    }

    @EventTarget
    private final void onUpdate(UpdateEvent event) {
        if (syncEntity != null) {
            EntityLivingBase entityLivingBase = syncEntity;
            if (entityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            if (entityLivingBase.field_70128_L) {
                ++killCounts;
                syncEntity = null;
            }
        }
    }

    @EventTarget
    private final void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C00Handshake) {
            startTime = System.currentTimeMillis();
        }
        Packet<?> packet = event.getPacket();
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.S02PacketChat");
        }
        IChatComponent iChatComponent = ((S02PacketChat)packet).func_148915_c();
        Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "(event.packet as S02PacketChat).chatComponent");
        String message = iChatComponent.func_150260_c();
        Packet<?> packet2 = event.getPacket();
        if (packet2 instanceof S45PacketTitle) {
            int n;
            String title;
            IChatComponent iChatComponent2 = ((S45PacketTitle)packet2).func_179805_b();
            if (iChatComponent2 == null) {
                return;
            }
            String string = title = iChatComponent2.func_150254_d();
            Intrinsics.checkExpressionValueIsNotNull(string, "title");
            if (StringsKt.startsWith$default(string, "\u00a76\u00a7l", false, 2, null) && StringsKt.endsWith$default(title, "\u00a7r", false, 2, null) || StringsKt.startsWith$default(title, "\u00a7c\u00a7lYOU", false, 2, null) && StringsKt.endsWith$default(title, "\u00a7r", false, 2, null) || StringsKt.startsWith$default(title, "\u00a7c\u00a7lGame", false, 2, null) && StringsKt.endsWith$default(title, "\u00a7r", false, 2, null) || StringsKt.startsWith$default(title, "\u00a7c\u00a7lWITH", false, 2, null) && StringsKt.endsWith$default(title, "\u00a7r", false, 2, null) || StringsKt.startsWith$default(title, "\u00a7c\u00a7lYARR", false, 2, null) && StringsKt.endsWith$default(title, "\u00a7r", false, 2, null)) {
                n = totalPlayed;
                totalPlayed = n + 1;
            }
            if (StringsKt.startsWith$default(title, "\u00a76\u00a7l", false, 2, null) && StringsKt.endsWith$default(title, "\u00a7r", false, 2, null)) {
                n = win;
                win = n + 1;
            }
        }
        String string = message;
        Intrinsics.checkExpressionValueIsNotNull(string, "message");
        if (StringsKt.contains$default((CharSequence)string, "Reason", false, 2, null)) {
            int n = ban;
            ban = n + 1;
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    private Recorder() {
    }

    static {
        Recorder recorder;
        INSTANCE = recorder = new Recorder();
        startTime = System.currentTimeMillis();
    }
}

