/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Fucker
extends Feature {
    public static NumberSetting rad;
    private final NumberSetting fuckerDelay;
    private final ListSetting mode;
    private final TimerHelper timerUtils = new TimerHelper();
    private int xPos;
    private int yPos;
    private int zPos;
    private int blockid;

    public Fucker() {
        super("Fucker", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0440\u0443\u0448\u0438\u0442 \u043a\u0440\u043e\u0432\u0430\u0442\u0438 \u0438 \u0442\u043e\u0440\u0442\u044b \u0441\u043a\u0432\u043e\u0437\u044c \u0431\u043b\u043e\u043a\u0438", Type.Misc);
        this.mode = new ListSetting("Block", "Bed", () -> true, "Bed", "Cake");
        rad = new NumberSetting("Fucker Radius", 4.0f, 1.0f, 6.0f, 0.5f, () -> true);
        this.fuckerDelay = new NumberSetting("Fucker Delay", 100.0f, 0.0f, 1000.0f, 50.0f, () -> true);
        this.addSettings(this.mode, rad, this.fuckerDelay);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        float radius = rad.getCurrentValue();
        int x = (int)(-radius);
        while ((float)x < radius) {
            int y = (int)radius;
            while ((float)y > -radius) {
                int z = (int)(-radius);
                while ((float)z < radius) {
                    this.xPos = (int)Fucker.mc.player.posX + x;
                    this.yPos = (int)Fucker.mc.player.posY + y;
                    this.zPos = (int)Fucker.mc.player.posZ + z;
                    BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
                    Block block = Fucker.mc.world.getBlockState(blockPos).getBlock();
                    switch (this.mode.getOptions()) {
                        case "Bed": {
                            this.blockid = 26;
                            break;
                        }
                        case "Cake": {
                            this.blockid = 354;
                        }
                    }
                    if (Block.getIdFromBlock(block) == this.blockid && (block != null || blockPos != null)) {
                        float[] rotations = RotationHelper.getRotationVector(new Vec3d((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f));
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        Fucker.mc.player.renderYawOffset = rotations[0];
                        Fucker.mc.player.rotationYawHead = rotations[0];
                        Fucker.mc.player.rotationPitchHead = rotations[1];
                        Fucker.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, Fucker.mc.player.getHorizontalFacing()));
                        if (this.timerUtils.hasReached(this.fuckerDelay.getCurrentValue())) {
                            Fucker.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, Fucker.mc.player.getHorizontalFacing()));
                            Fucker.mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.timerUtils.reset();
                        }
                    }
                    ++z;
                }
                --y;
            }
            ++x;
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        int playerX = (int)Fucker.mc.player.posX;
        int playerZ = (int)Fucker.mc.player.posZ;
        int playerY = (int)Fucker.mc.player.posY;
        int range = (int)rad.getCurrentValue();
        for (int y = playerY - range; y <= playerY + range; ++y) {
            for (int x = playerX - range; x <= playerX + range; ++x) {
                for (int z = playerZ - range; z <= playerZ + range; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (Fucker.mc.world.getBlockState(pos).getBlock() != Blocks.BED || pos == null || Fucker.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) continue;
                    RenderHelper.blockEsp(pos, Color.RED, true, 1.0, 1.0);
                }
            }
        }
    }
}

