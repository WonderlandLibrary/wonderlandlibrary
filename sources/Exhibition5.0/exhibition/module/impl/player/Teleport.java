// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.RegisterEvent;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import exhibition.util.RenderingUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockSign;
import exhibition.event.impl.EventRender3D;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import exhibition.util.PlayerUtil;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Mouse;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import net.minecraft.util.BlockPos;
import exhibition.module.Module;

public class Teleport extends Module
{
    private boolean canTP;
    private int delay;
    private BlockPos endPos;
    
    public Teleport(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMotion.class, EventRender3D.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                if (this.canTP && Mouse.isButtonDown(1) && !Teleport.mc.thePlayer.isSneaking() && this.delay == 0 && Teleport.mc.inGameHasFocus && !(Teleport.mc.objectMouseOver.entityHit instanceof Entity)) {
                    event.setCancelled(true);
                    this.endPos = Teleport.mc.objectMouseOver.getBlockPos();
                    final double[] startPos = { Teleport.mc.thePlayer.posX, Teleport.mc.thePlayer.posY, Teleport.mc.thePlayer.posZ };
                    PlayerUtil.blinkToPos(startPos, this.endPos, 0.0, new double[] { 0.3, 0.2 });
                    Teleport.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.endPos.getX() + 0.5, this.endPos.getY() - 1.0, this.endPos.getZ() + 0.5, false));
                    Teleport.mc.thePlayer.setPosition(this.endPos.getX() + 0.5, this.endPos.getY() + 1, this.endPos.getZ() + 0.5);
                    this.delay = 5;
                    event.setCancelled(false);
                }
                if (this.delay > 0) {
                    --this.delay;
                }
            }
        }
        if (event instanceof EventRender3D) {
            final EventRender3D er = (EventRender3D)event;
            try {
                final int x = Teleport.mc.objectMouseOver.getBlockPos().getX();
                final int y = Teleport.mc.objectMouseOver.getBlockPos().getY();
                final int z = Teleport.mc.objectMouseOver.getBlockPos().getZ();
                final Block block1 = getBlock(x, y, z);
                final Block block2 = getBlock(x, y + 1, z);
                final Block block3 = getBlock(x, y + 2, z);
                final boolean blockBelow = !(block1 instanceof BlockSign) && block1.getMaterial().isSolid();
                final boolean blockLevel = !(block2 instanceof BlockSign) && block1.getMaterial().isSolid();
                final boolean blockAbove = !(block3 instanceof BlockSign) && block1.getMaterial().isSolid();
                if (getBlock(Teleport.mc.objectMouseOver.getBlockPos()).getMaterial() != Material.air && blockBelow && blockLevel && blockAbove) {
                    this.canTP = true;
                    GL11.glPushMatrix();
                    RenderingUtil.pre3D();
                    Teleport.mc.entityRenderer.setupCameraTransform(er.renderPartialTicks, 2);
                    GL11.glColor4d(0.0, 0.0, 0.0, 0.25);
                    RenderingUtil.drawBoundingBox(new AxisAlignedBB(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ, x - RenderManager.renderPosX + 1.0, y + 1.1 - RenderManager.renderPosY, z - RenderManager.renderPosZ + 1.0));
                    GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                    RenderingUtil.post3D();
                    GL11.glPopMatrix();
                }
                else {
                    this.canTP = false;
                }
            }
            catch (Exception ex) {}
        }
    }
    
    public static Block getBlock(final int x, final int y, final int z) {
        return Teleport.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
    
    public static Block getBlock(final BlockPos pos) {
        return Teleport.mc.theWorld.getBlockState(pos).getBlock();
    }
}
