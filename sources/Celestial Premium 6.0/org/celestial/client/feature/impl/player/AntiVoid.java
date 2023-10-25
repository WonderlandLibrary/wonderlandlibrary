/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.movement.Flight;
import org.celestial.client.feature.impl.movement.Scaffold;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AntiVoid
extends Feature {
    private final NumberSetting fallDist;
    private final ListSetting antiVoidMode = new ListSetting("AntiVoid Mode", "Packet", () -> true, "Packet", "High-Motion");
    float fall = 0.0f;

    public AntiVoid() {
        super("AntiVoid", "\u041d\u0435 \u0434\u0430\u0435\u0442 \u0432\u0430\u043c \u0443\u043f\u0430\u0441\u0442\u044c \u0432 \u0431\u0435\u0437\u0434\u043d\u0443", Type.Player);
        this.fallDist = new NumberSetting("Fall Distance", 5.0f, 1.0f, 10.0f, 1.0f, () -> true);
        this.addSettings(this.antiVoidMode, this.fallDist);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        BlockPos pos;
        String mode = this.antiVoidMode.getOptions();
        this.setSuffix(mode);
        if (Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState() || Celestial.instance.featureManager.getFeatureByClass(Scaffold.class).getState()) {
            return;
        }
        boolean blockUnderneath = false;
        int checkedY = 0;
        int i = 0;
        while ((double)i < AntiVoid.mc.player.posY + 2.0) {
            pos = new BlockPos(AntiVoid.mc.player.posX, (double)i, AntiVoid.mc.player.posZ);
            if (!(AntiVoid.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) {
                blockUnderneath = true;
                checkedY = i;
            }
            ++i;
        }
        if (blockUnderneath) {
            pos = new BlockPos(AntiVoid.mc.player.posX, (double)checkedY, AntiVoid.mc.player.posZ);
            if (AntiVoid.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid || AntiVoid.mc.world.getBlockState(pos).getBlock() instanceof BlockDynamicLiquid) {
                int antiLiquid = 0;
                while ((double)antiLiquid < AntiVoid.mc.player.posY) {
                    BlockPos posTest = new BlockPos(AntiVoid.mc.player.posX, AntiVoid.mc.player.posY - (double)antiLiquid, AntiVoid.mc.player.posZ);
                    Block block = AntiVoid.mc.world.getBlockState(posTest).getBlock();
                    if (!(block instanceof BlockLiquid) && !(block instanceof BlockAir)) {
                        return;
                    }
                    ++antiLiquid;
                }
            } else {
                return;
            }
        }
        if (!AntiVoid.mc.player.onGround && !AntiVoid.mc.player.isCollidedVertically) {
            if (AntiVoid.mc.player.motionY < -0.08) {
                this.fall = (float)((double)this.fall - AntiVoid.mc.player.motionY);
            }
            if (this.fall > this.fallDist.getCurrentValue()) {
                this.fall = 0.0f;
                if (mode.equalsIgnoreCase("High-Motion")) {
                    AntiVoid.mc.player.motionY += 3.0;
                } else {
                    AntiVoid.mc.player.connection.sendPacket(new CPacketPlayer.Position(AntiVoid.mc.player.posX, AntiVoid.mc.player.posY + 12.0, AntiVoid.mc.player.posZ, true));
                }
                AntiVoid.mc.player.fallDistance = 0.0f;
            }
        } else {
            this.fall = 0.0f;
        }
    }
}

