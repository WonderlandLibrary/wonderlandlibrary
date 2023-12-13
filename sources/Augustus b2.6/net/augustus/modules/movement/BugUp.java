// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.augustus.utils.PlayerUtil;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class BugUp extends Module
{
    public final DoubleValue maxDistance;
    private final TimeHelper timeHelper;
    public StringValue mode;
    private double[] xyz;
    private final ArrayList<Packet> packets;
    
    public BugUp() {
        super("BugUp", Color.DARK_GRAY, Categorys.MOVEMENT);
        this.maxDistance = new DoubleValue(2, "MaxDistance", this, 15.0, 3.0, 30.0, 0);
        this.timeHelper = new TimeHelper();
        this.mode = new StringValue(1, "Mode", this, "OnGround", new String[] { "Teleport", "OnGround" });
        this.xyz = new double[3];
        this.packets = new ArrayList<Packet>();
    }
    
    @Override
    public void onEnable() {
        this.packets.clear();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.packets.clear();
        super.onDisable();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (BugUp.mc.thePlayer.onGround) {
            this.xyz = new double[] { BugUp.mc.thePlayer.posX, BugUp.mc.thePlayer.posY, BugUp.mc.thePlayer.posZ };
        }
        if (this.shouldBugUp()) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Teleport": {
                    if (this.timeHelper.reached(200L)) {
                        BugUp.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.xyz[0], this.xyz[1], this.xyz[2], true));
                        this.timeHelper.reset();
                        break;
                    }
                    break;
                }
                case "OnGround": {
                    BugUp.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    break;
                }
            }
        }
    }
    
    private boolean shouldBugUp() {
        if (BugUp.mm.longJump.isToggled() || BugUp.mm.fly.isToggled() || BugUp.mc.thePlayer.fallDistance < 2.0f) {
            return false;
        }
        double posX = BugUp.mc.thePlayer.posX;
        double posY = BugUp.mc.thePlayer.posY;
        double posZ = BugUp.mc.thePlayer.posZ;
        double motionX = BugUp.mc.thePlayer.motionX;
        double motionY = BugUp.mc.thePlayer.motionY;
        double motionZ = BugUp.mc.thePlayer.motionZ;
        boolean isJumping = BugUp.mc.thePlayer.isJumping;
        for (int i = 0; i < 200; ++i) {
            final double[] doubles = PlayerUtil.getPredictedPos(BugUp.mc.thePlayer.movementInput.moveForward, BugUp.mc.thePlayer.movementInput.moveStrafe, motionX, motionY, motionZ, posX, posY, posZ, isJumping);
            isJumping = false;
            posX = doubles[0];
            posY = doubles[1];
            posZ = doubles[2];
            motionX = doubles[3];
            motionY = doubles[4];
            motionZ = doubles[5];
            final BlockPos b = new BlockPos(posX, posY, posZ);
            final Block block = BugUp.mc.theWorld.getBlockState(b).getBlock();
            if (!(block instanceof BlockAir)) {
                return false;
            }
            if (Math.abs(BugUp.mc.thePlayer.posY - posY) > this.maxDistance.getValue()) {
                break;
            }
        }
        return true;
    }
}
