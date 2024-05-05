package me.earth.earthhack.impl.modules.client.pingbypass.submodules.ssurround;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.impl.gui.module.SubModule;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.minecraft.BlockUtil;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

// TODO: Add a setting where you can use Surround only when you are holding Obsidian?

@SuppressWarnings("unused")
public class ServerSurround extends SubModule {

    private static final ServerSurround INSTANCE = new ServerSurround();
    private final StopWatch timer = new StopWatch();

    private final Setting<Boolean> center = register(new BooleanSetting("Center", true));
    private BlockPos surroundPos = new BlockPos(0, -100, 0);

    private ServerSurround() {
        super("S-Surround", Category.Combat, PingBypass.getInstance());
        this.listeners.add(new TickListener(this));
    }

    public static ServerSurround getInstance() {
        return INSTANCE;
    }

    protected void onTick() {

        if (mc.player == null) {
            return;
        }

        if (!mc.player.onGround) return;

        int previousSlot = mc.player.inventory.currentItem;
        int obsidianSlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);

        if (obsidianSlot != -1) {
            EntityPlayer player = mc.player;
            if (new BlockPos(player.getPositionVector()).equals(surroundPos)) {
                BlockPos[] positions = new BlockPos[]{
                        surroundPos.add(0, -1, 1),
                        surroundPos.add(1, -1, 0),
                        surroundPos.add(0, -1, -1),
                        surroundPos.add(-1, -1, 0),
                        surroundPos.add(0, 0, 1),
                        surroundPos.add(1, 0, 0),
                        surroundPos.add(0, 0, -1),
                        surroundPos.add(-1, 0, 0),
                };

                for (BlockPos pos : positions) {
                    if (mc.world.getBlockState(pos).getMaterial().isReplaceable()
                            && mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))
                            .stream().noneMatch(Entity::canBeCollidedWith)) {
                        mc.player.inventory.currentItem = obsidianSlot;
                        BlockUtil.placeBlockMainHand(pos);
                    }
                }

                mc.player.inventory.currentItem = previousSlot;
            } else this.disable();
        }
    }

    @Override
    protected void onEnable() {
        BlockPos pos = surroundPos = new BlockPos(mc.player.getPositionVector());

        if (center.getValue()) {
            double xPosition = mc.player.getPositionVector().x;
            double zPosition = mc.player.getPositionVector().z;

            if (Math.abs((pos.getX() + 0.5) - mc.player.getPositionVector().x) >= 0.2) {
                int xDirection = (pos.getX() + 0.5) - mc.player.getPositionVector().x > 0 ? 1 : -1;
                xPosition += 0.3 * xDirection;
            }

            if (Math.abs((pos.getZ() + 0.5) - mc.player.getPositionVector().z) >= 0.2) {
                int zDirection = (pos.getZ() + 0.5) - mc.player.getPositionVector().z > 0 ? 1 : -1;
                zPosition += 0.3 * zDirection;
            }

            mc.player.motionX = mc.player.motionY = mc.player.motionZ = 0;
            mc.player.setPosition(xPosition, pos.getY(), zPosition);
            mc.player.connection.sendPacket(new CPacketPlayer.Position(xPosition, pos.getY(), zPosition, mc.player.onGround));
        }
    }
}
