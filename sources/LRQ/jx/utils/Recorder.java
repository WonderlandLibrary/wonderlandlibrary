/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.network.play.server.SPacketTitle
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.Nullable
 */
package jx.utils;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.Nullable;

public final class Recorder
implements Listenable {
    private static EntityLivingBase syncEntity;
    private static int killCounts;
    private static int totalPlayed;
    private static int totalPlayed2;
    private static int win;
    private static int ban;
    private static long startTime;
    public static final Recorder INSTANCE;

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

    public final int getTotalPlayed2() {
        return totalPlayed2;
    }

    public final void setTotalPlayed2(int n) {
        totalPlayed2 = n;
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
        IPacket iPacket = event.getPacket();
        if (iPacket == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketChat");
        }
        String message = ((SPacketChat)iPacket).func_148915_c().func_150260_c();
        IPacket packet = event.getPacket();
        if (packet instanceof SPacketTitle) {
            int n;
            ITextComponent iTextComponent = ((SPacketTitle)packet).func_179805_b();
            if (iTextComponent == null) {
                return;
            }
            String title = iTextComponent.func_150254_d();
            if (StringsKt.startsWith$default((String)title, (String)"\u00a76\u00a7l", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)title, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)title, (String)"\u00a7c\u00a7lYOU", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)title, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)title, (String)"\u00a7c\u00a7lGame", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)title, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)title, (String)"\u00a7c\u00a7lWITH", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)title, (String)"\u00a7r", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)title, (String)"\u00a7c\u00a7lYARR", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)title, (String)"\u00a7r", (boolean)false, (int)2, null)) {
                n = totalPlayed2;
                totalPlayed2 = n + 1;
            }
            if (StringsKt.startsWith$default((String)title, (String)"\u00a76\u00a7l", (boolean)false, (int)2, null) && StringsKt.endsWith$default((String)title, (String)"\u00a7r", (boolean)false, (int)2, null)) {
                n = win;
                win = n + 1;
            }
        }
        if (message.equals("Reason")) {
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

