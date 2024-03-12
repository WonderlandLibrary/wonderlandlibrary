// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import dev.tenacity.event.impl.player.BoundingBoxEvent;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class NoFall extends Module
{
    private final ModeSetting mode;
    private double dist;
    private boolean doNofall;
    private double lastFallDistance;
    private boolean c04;
    
    public NoFall() {
        super("NoFall", "No Fall", Category.PLAYER, "prevents fall damage");
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Packet", "Verus" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (event.isPre()) {
            this.setSuffix(this.mode.getMode());
            if (NoFall.mc.thePlayer.fallDistance > 3.0 && this.isBlockUnder()) {
                final String mode = this.mode.getMode();
                switch (mode) {
                    case "Vanilla": {
                        event.setOnGround(true);
                        break;
                    }
                    case "Packet": {
                        PacketUtils.sendPacket(new C03PacketPlayer(true));
                        break;
                    }
                }
                NoFall.mc.thePlayer.fallDistance = 0.0f;
            }
        }
    }
    
    @Override
    public void onBoundingBoxEvent(final BoundingBoxEvent event) {
        if (this.mode.is("Verus") && NoFall.mc.thePlayer.fallDistance > 2.0f) {
            final AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromBounds(-5.0, -1.0, -5.0, 5.0, 1.0, 5.0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ());
            event.setBoundingBox(axisAlignedBB);
        }
    }
    
    private boolean isBlockUnder() {
        if (NoFall.mc.thePlayer.posY < 0.0) {
            return false;
        }
        for (int offset = 0; offset < (int)NoFall.mc.thePlayer.posY + 2; offset += 2) {
            final AxisAlignedBB bb = NoFall.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (!NoFall.mc.theWorld.getCollidingBoundingBoxes(NoFall.mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
