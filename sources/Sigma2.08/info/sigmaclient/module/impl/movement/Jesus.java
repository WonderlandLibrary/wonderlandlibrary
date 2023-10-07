package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventLiquidCollide;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;


public class Jesus extends Module {


    public Jesus(ModuleData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    int ticks;

    @Override
    public void onEnable() {
        super.onEnable();
        ticks = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ticks = 0;
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class, EventLiquidCollide.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre() && PlayerUtil.isOnLiquid() && !PlayerUtil.isInLiquid() && !mc.thePlayer.isSneaking() && !mc.gameSettings.keyBindJump.isPressed()) {
                if (ticks == 0 && PlayerUtil.isOnLiquid() && PlayerUtil.isMoving()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
                em.setY(em.getY() + (mc.thePlayer.ticksExisted % 2 == 0 ? 0.000000000001D : -0.000000000001D));
            }
            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() == Blocks.lava)
                em.setGround(mc.thePlayer.ticksExisted % 2 != 0);
            if (!shouldSetBoundingBox() && PlayerUtil.isInLiquid()) {
                mc.thePlayer.fallDistance = 0;
                mc.thePlayer.motionY = mc.thePlayer.isSneaking() ? -0.13 : 0.1;
            }
            if (ticks == 1 && !PlayerUtil.isOnLiquid()) {
                ticks = 0;
            }
            if (PlayerUtil.isOnLiquid() && mc.theWorld.getBlockState((new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ))).getBlock() == Blocks.lava && !PlayerUtil.isMoving()) {
                em.setCancelled(true);
            }
        }
        if (event instanceof EventLiquidCollide) {
            EventLiquidCollide ebb = (EventLiquidCollide) event;
            if (ebb.getPos().getY() + 0.9 < mc.thePlayer.boundingBox.minY) {
                ebb.setBounds(new AxisAlignedBB(ebb.getPos().getX(), ebb.getPos().getY(), ebb.getPos().getZ(), ebb.getPos().getX() + 1, ebb.getPos().getY() + 1, ebb.getPos().getZ() + 1));
                ebb.setCancelled(shouldSetBoundingBox());
            }
        }
    }

    private boolean shouldSetBoundingBox() {
        return (!mc.thePlayer.isSneaking()) && (mc.thePlayer.fallDistance < 4.0F) && !PlayerUtil.isInLiquid();
    }

}
