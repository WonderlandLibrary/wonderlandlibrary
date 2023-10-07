package best.azura.client.impl.module.impl.player;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.*;

import java.util.ArrayList;

@ModuleInfo(name = "Chest Aura", description = "Automatically open chests near you", category = Category.PLAYER)
public class ChestAura extends Module {

    private final BooleanValue legit = new BooleanValue("Legit", "Use legit clicking", false);
    private final NumberValue<Double> range = new NumberValue<>("Range", "Range for checking for chests", 4.0, 0.5, 2.0, 6.0);
    private final NumberValue<Long> delay = new NumberValue<>("Delay", "Delay for clicking chests", 50L, 50L, 0L, 500L);
    private final ArrayList<TileEntityChest> clickedChests = new ArrayList<>();
    private final DelayUtil delayUtil = new DelayUtil();
    private TileEntityChest current;

    public ChestAura() {
        super();
        Client.INSTANCE.getEventBus().register(new WorldChange());
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                if (current == null) return;
                e.apply(RotationUtil.getNeededRotations(RotationUtil.getBlockVecCenter(current.getPos())));
            }
            if (e.isUpdate()) {
                current = null;
                for (double xx = -range.getObject(); xx < range.getObject(); xx++) {
                    for (double yy = -range.getObject(); yy < range.getObject(); yy++) {
                        for (double zz = -range.getObject(); zz < range.getObject(); zz++) {
                            final BlockPos pos = new BlockPos(mc.thePlayer.posX + xx, mc.thePlayer.posY + yy, mc.thePlayer.posZ + zz);
                            final TileEntity entity = mc.theWorld.getTileEntity(pos);
                            if (!(entity instanceof TileEntityChest)) continue;
                            final TileEntityChest chest = (TileEntityChest) entity;
                            if (clickedChests.contains(chest)) continue;
                            current = chest;
                        }
                    }
                }
                if (current != null) {
                    boolean chest = mc.thePlayer.openContainer instanceof ContainerChest;
                    if (delayUtil.hasReached(delay.getObject()) && !chest) {
                        if (legit.getObject()) mc.rightClickMouse();
                        else {
                            float[] rots = RotationUtil.faceVector(RotationUtil.getBlockVecCenter(current.getPos()));
                            float yaw = rots[0];
                            float pitch = rots[1];
                            float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
                            float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
                            float f2 = -MathHelper.cos(-pitch * 0.017453292F);
                            float f3 = MathHelper.sin(-pitch * 0.017453292F);
                            Vec3 vec = new Vec3(f1 * f2, f3, f * f2);
                            EnumFacing facing = EnumFacing.getFacingFromVector((float) vec.xCoord, (float) vec.yCoord, (float) vec.zCoord);
                            facing = facing == EnumFacing.UP ? facing : facing.getOpposite();
                            Vec3i direction = facing.getDirectionVec();
                            vec = new Vec3(current.getPos()).add(new Vec3(Math.max(direction.getX(), -direction.getX()), Math.max(direction.getY(), -direction.getY()), Math.max(direction.getZ(), -direction.getZ())));
                            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), current.getPos(), facing, vec))
                                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        }
                        delayUtil.reset();
                    }
                    if (chest) {
                        clickedChests.add(current);
                        current = null;
                    }
                }
            }
        }
    }

    private class WorldChange {
        @EventHandler
        public final Listener<EventWorldChange> eventWorldChangeListener = e -> clickedChests.clear();
    }

}