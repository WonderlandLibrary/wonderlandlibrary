/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u000e\n\u0002\u0010\t\n\u0002\b\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010M\u001a\u00020NH\u0002J\u0006\u0010O\u001a\u00020PR\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\b\"\u0004\b\r\u0010\nR\u001a\u0010\u000e\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\b\"\u0004\b\u0010\u0010\nR\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\b\"\u0004\b\u0015\u0010\nR\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0019\"\u0004\b\u001e\u0010\u001bR\u001a\u0010\u001f\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0019\"\u0004\b!\u0010\u001bR\u001a\u0010\"\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0019\"\u0004\b$\u0010\u001bR\u001a\u0010%\u001a\u00020&X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u001a\u0010+\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0019\"\u0004\b-\u0010\u001bR\u001a\u0010.\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0019\"\u0004\b0\u0010\u001bR\u001a\u00101\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b2\u0010\u0019\"\u0004\b3\u0010\u001bR\u001a\u00104\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u0019\"\u0004\b6\u0010\u001bR\u001a\u00107\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0019\"\u0004\b9\u0010\u001bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010;R\u001a\u0010<\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\b\"\u0004\b>\u0010\nR\u001a\u0010?\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b@\u0010\b\"\u0004\bA\u0010\nR*\u0010B\u001a\u0012\u0012\u0004\u0012\u00020D0Cj\b\u0012\u0004\u0012\u00020D`EX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bF\u0010G\"\u0004\bH\u0010IR\u001a\u0010J\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bK\u0010\b\"\u0004\bL\u0010\n\u00a8\u0006Q"}, d2={"Lme/report/liquidware/modules/fun/HackerData;", "", "player", "Lnet/minecraft/entity/player/EntityPlayer;", "(Lnet/minecraft/entity/player/EntityPlayer;)V", "airTicks", "", "getAirTicks", "()I", "setAirTicks", "(I)V", "aliveTicks", "getAliveTicks", "setAliveTicks", "aps", "getAps", "setAps", "apsTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "groundTicks", "getGroundTicks", "setGroundTicks", "lastMotionX", "", "getLastMotionX", "()D", "setLastMotionX", "(D)V", "lastMotionXZ", "getLastMotionXZ", "setLastMotionXZ", "lastMotionY", "getLastMotionY", "setLastMotionY", "lastMotionZ", "getLastMotionZ", "setLastMotionZ", "lastMovePacket", "", "getLastMovePacket", "()J", "setLastMovePacket", "(J)V", "motionX", "getMotionX", "setMotionX", "motionXZ", "getMotionXZ", "setMotionXZ", "motionY", "getMotionY", "setMotionY", "motionZ", "getMotionZ", "setMotionZ", "packetBalance", "getPacketBalance", "setPacketBalance", "getPlayer", "()Lnet/minecraft/entity/player/EntityPlayer;", "preAps", "getPreAps", "setPreAps", "tempAps", "getTempAps", "setTempAps", "useHacks", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "getUseHacks", "()Ljava/util/ArrayList;", "setUseHacks", "(Ljava/util/ArrayList;)V", "vl", "getVl", "setVl", "calculateGround", "", "update", "", "KyinoClient"})
public final class HackerData {
    private double packetBalance;
    private long lastMovePacket;
    private int aliveTicks;
    private int airTicks;
    private int groundTicks;
    private double motionX;
    private double motionY;
    private double motionZ;
    private double motionXZ;
    private double lastMotionX;
    private double lastMotionY;
    private double lastMotionZ;
    private double lastMotionXZ;
    private int aps;
    private int preAps;
    private int tempAps;
    private final MSTimer apsTimer;
    private int vl;
    @NotNull
    private ArrayList<String> useHacks;
    @NotNull
    private final EntityPlayer player;

    public final double getPacketBalance() {
        return this.packetBalance;
    }

    public final void setPacketBalance(double d) {
        this.packetBalance = d;
    }

    public final long getLastMovePacket() {
        return this.lastMovePacket;
    }

    public final void setLastMovePacket(long l) {
        this.lastMovePacket = l;
    }

    public final int getAliveTicks() {
        return this.aliveTicks;
    }

    public final void setAliveTicks(int n) {
        this.aliveTicks = n;
    }

    public final int getAirTicks() {
        return this.airTicks;
    }

    public final void setAirTicks(int n) {
        this.airTicks = n;
    }

    public final int getGroundTicks() {
        return this.groundTicks;
    }

    public final void setGroundTicks(int n) {
        this.groundTicks = n;
    }

    public final double getMotionX() {
        return this.motionX;
    }

    public final void setMotionX(double d) {
        this.motionX = d;
    }

    public final double getMotionY() {
        return this.motionY;
    }

    public final void setMotionY(double d) {
        this.motionY = d;
    }

    public final double getMotionZ() {
        return this.motionZ;
    }

    public final void setMotionZ(double d) {
        this.motionZ = d;
    }

    public final double getMotionXZ() {
        return this.motionXZ;
    }

    public final void setMotionXZ(double d) {
        this.motionXZ = d;
    }

    public final double getLastMotionX() {
        return this.lastMotionX;
    }

    public final void setLastMotionX(double d) {
        this.lastMotionX = d;
    }

    public final double getLastMotionY() {
        return this.lastMotionY;
    }

    public final void setLastMotionY(double d) {
        this.lastMotionY = d;
    }

    public final double getLastMotionZ() {
        return this.lastMotionZ;
    }

    public final void setLastMotionZ(double d) {
        this.lastMotionZ = d;
    }

    public final double getLastMotionXZ() {
        return this.lastMotionXZ;
    }

    public final void setLastMotionXZ(double d) {
        this.lastMotionXZ = d;
    }

    public final int getAps() {
        return this.aps;
    }

    public final void setAps(int n) {
        this.aps = n;
    }

    public final int getPreAps() {
        return this.preAps;
    }

    public final void setPreAps(int n) {
        this.preAps = n;
    }

    public final int getTempAps() {
        return this.tempAps;
    }

    public final void setTempAps(int n) {
        this.tempAps = n;
    }

    public final int getVl() {
        return this.vl;
    }

    public final void setVl(int n) {
        this.vl = n;
    }

    @NotNull
    public final ArrayList<String> getUseHacks() {
        return this.useHacks;
    }

    public final void setUseHacks(@NotNull ArrayList<String> arrayList) {
        Intrinsics.checkParameterIsNotNull(arrayList, "<set-?>");
        this.useHacks = arrayList;
    }

    public final void update() {
        double d;
        int n = this.aliveTicks;
        this.aliveTicks = n + 1;
        if (this.apsTimer.hasTimePassed(1000L)) {
            this.preAps = this.aps;
            this.aps = this.tempAps;
            this.tempAps = 0;
        }
        if (this.calculateGround()) {
            n = this.groundTicks;
            this.groundTicks = n + 1;
            this.airTicks = 0;
        } else {
            n = this.airTicks;
            this.airTicks = n + 1;
            this.groundTicks = 0;
        }
        this.lastMotionX = this.motionX;
        this.lastMotionY = this.motionY;
        this.lastMotionZ = this.motionZ;
        this.lastMotionXZ = this.motionXZ;
        this.motionX = this.player.field_70165_t - this.player.field_70169_q;
        this.motionY = this.player.field_70163_u - this.player.field_70167_r;
        this.motionZ = this.player.field_70161_v - this.player.field_70166_s;
        double d2 = this.motionX * this.motionX + this.motionZ * this.motionZ;
        HackerData hackerData = this;
        boolean bl = false;
        hackerData.motionXZ = d = Math.sqrt(d2);
    }

    private final boolean calculateGround() {
        AxisAlignedBB playerBoundingBox = this.player.func_174813_aQ();
        boolean blockHeight = true;
        AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.field_72336_d, this.player.field_70163_u - (double)blockHeight, playerBoundingBox.field_72334_f, playerBoundingBox.field_72340_a, this.player.field_70163_u, playerBoundingBox.field_72339_c);
        return Minecraft.func_71410_x().field_71441_e.func_72829_c(customBox);
    }

    @NotNull
    public final EntityPlayer getPlayer() {
        return this.player;
    }

    public HackerData(@NotNull EntityPlayer player) {
        Intrinsics.checkParameterIsNotNull(player, "player");
        this.player = player;
        this.lastMovePacket = System.currentTimeMillis();
        this.apsTimer = new MSTimer();
        this.useHacks = new ArrayList();
    }
}

