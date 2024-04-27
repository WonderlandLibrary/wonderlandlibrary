/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.World
 */
package net.dev.important.modules.module.modules.player;

import java.util.Objects;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.render.FreeCam;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.timer.TickTimer;
import net.dev.important.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import oh.yalan.NativeClass;

@NativeClass
@Info(name="NoFall", description="Prevents you from taking fall damage.", category=Category.PLAYER, cnName="\u65e0\u6389\u843d\u4f24\u5bb3")
public class NoFall
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Spartan", "Packet", "Hypixel", "SpoofGround", "NoGround", "AAC", "AAC5"}, "SpoofGround");
    private int state;
    private boolean aac5doFlag = false;
    private boolean aac5Check = false;
    private int aac5Timer = 0;
    private final TickTimer spartanTimer = new TickTimer();
    private int tick = 0;

    @Override
    public void onEnable() {
        this.aac5Check = false;
        this.aac5doFlag = false;
        this.aac5Timer = 0;
        this.tick = 0;
    }

    @EventTarget(ignoreCondition=true)
    public void onUpdate() {
        if (!this.getState() || Objects.requireNonNull(Client.moduleManager.getModule(FreeCam.class)).getState()) {
            return;
        }
        if (BlockUtils.collideBlock(NoFall.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(NoFall.mc.field_71439_g.func_174813_aQ().field_72336_d, NoFall.mc.field_71439_g.func_174813_aQ().field_72337_e, NoFall.mc.field_71439_g.func_174813_aQ().field_72334_f, NoFall.mc.field_71439_g.func_174813_aQ().field_72340_a, NoFall.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01, NoFall.mc.field_71439_g.func_174813_aQ().field_72339_c), block -> block instanceof BlockLiquid)) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "packet": {
                if (!(NoFall.mc.field_71439_g.field_70143_R > 2.0f)) break;
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer(true));
                break;
            }
            case "hypixel": {
                double offset = 2.5;
                if (!NoFall.mc.field_71439_g.field_70122_E && (double)NoFall.mc.field_71439_g.field_70143_R - (double)this.tick * offset >= 0.0) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    ++this.tick;
                    break;
                }
                if (!NoFall.mc.field_71439_g.field_70122_E) break;
                this.tick = 1;
                break;
            }
            case "aac5": {
                this.aac5Check = false;
                for (double offsetYs = 0.0; NoFall.mc.field_71439_g.field_70181_x - 1.5 < offsetYs; offsetYs -= 0.5) {
                    BlockPos blockPos = new BlockPos(NoFall.mc.field_71439_g.field_70165_t, NoFall.mc.field_71439_g.field_70163_u + offsetYs, NoFall.mc.field_71439_g.field_70161_v);
                    Block block2 = BlockUtils.getBlock(blockPos);
                    assert (block2 != null);
                    AxisAlignedBB axisAlignedBB = block2.func_180640_a((World)NoFall.mc.field_71441_e, blockPos, BlockUtils.getState(blockPos));
                    if (axisAlignedBB == null) continue;
                    offsetYs = -999.9;
                    this.aac5Check = true;
                }
                if (NoFall.mc.field_71439_g.field_70122_E) {
                    NoFall.mc.field_71439_g.field_70143_R = -2.0f;
                    this.aac5Check = false;
                }
                if (this.aac5Timer > 0) {
                    --this.aac5Timer;
                }
                if (this.aac5Check && (double)NoFall.mc.field_71439_g.field_70143_R > 3.125) {
                    this.aac5doFlag = true;
                    this.aac5Timer = 16;
                } else if (this.aac5Timer < 2) {
                    this.aac5doFlag = false;
                }
                if (!this.aac5doFlag) break;
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.field_71439_g.field_70165_t, NoFall.mc.field_71439_g.field_70163_u + 0.5, NoFall.mc.field_71439_g.field_70161_v, true));
                break;
            }
            case "aac": {
                if (NoFall.mc.field_71439_g.field_70143_R > 2.0f) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    this.state = 2;
                } else if (this.state == 2 && NoFall.mc.field_71439_g.field_70143_R < 2.0f) {
                    NoFall.mc.field_71439_g.field_70181_x = 0.1;
                    this.state = 3;
                    return;
                }
                switch (this.state) {
                    case 3: {
                        NoFall.mc.field_71439_g.field_70181_x = 0.1;
                        this.state = 4;
                        break;
                    }
                    case 4: {
                        NoFall.mc.field_71439_g.field_70181_x = 0.1;
                        this.state = 5;
                        break;
                    }
                    case 5: {
                        NoFall.mc.field_71439_g.field_70181_x = 0.1;
                        this.state = 1;
                    }
                }
                break;
            }
            case "spartan": {
                this.spartanTimer.update();
                if (!((double)NoFall.mc.field_71439_g.field_70143_R > 1.5) || !this.spartanTimer.hasTimePassed(10)) break;
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.field_71439_g.field_70165_t, NoFall.mc.field_71439_g.field_70163_u + 10.0, NoFall.mc.field_71439_g.field_70161_v, true));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.field_71439_g.field_70165_t, NoFall.mc.field_71439_g.field_70163_u - 10.0, NoFall.mc.field_71439_g.field_70161_v, true));
                this.spartanTimer.reset();
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        String mode = (String)this.modeValue.get();
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer playerPacket = (C03PacketPlayer)packet;
            if (mode.equalsIgnoreCase("SpoofGround")) {
                playerPacket.field_149474_g = true;
            }
            if (mode.equalsIgnoreCase("NoGround")) {
                playerPacket.field_149474_g = false;
            }
            if (mode.equalsIgnoreCase("Hypixel") && NoFall.mc.field_71439_g != null && (double)NoFall.mc.field_71439_g.field_70143_R > 1.5) {
                playerPacket.field_149474_g = NoFall.mc.field_71439_g.field_70173_aa % 2 == 0;
            }
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

