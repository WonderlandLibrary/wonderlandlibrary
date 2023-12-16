package wtf.expensive.modules.impl.util;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CAnimateHandPacket;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.*;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.math.RayTraceUtil;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.movement.MoveUtil;

import java.util.concurrent.ConcurrentLinkedQueue;

@FunctionAnnotation(name = "BlockFly", type = Type.Movement)
public class Scaffold extends Function {

    private BlockCache blockCache, lastBlockCache;
    public Vector2f rotation;
    private float savedY;

    public static final ConcurrentLinkedQueue<TimedPacket> packets = new ConcurrentLinkedQueue<>();

    @Override
    protected void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1;

        for (TimedPacket p : packets) {
            mc.player.connection.getNetworkManager().sendPacketWithoutEvent(p.getPacket());
        }
        packets.clear();

    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (mc.player != null)
            savedY = (float) mc.player.getPosY();
    }

    public boolean sneak;
    public TimerUtil timerUtil = new TimerUtil();

    @Override
    public void onEvent(Event event) {

        if (event instanceof EventPacket e) {
            if (mc.player != null && mc.world != null && !mc.isSingleplayer() && !mc.player.getShouldBeDead()) {

                if (e.isSendPacket()) {
                    IPacket<?> packet = e.getPacket();
                    packets.add(new TimedPacket(packet, System.currentTimeMillis()));
                    e.setCancel(true);
                }
            } else setState(false);
        }
        if (event instanceof EventMotion e) {
            for (TimedPacket timedPacket : packets) {
                if (System.currentTimeMillis() - timedPacket.getTime() >= 1000) {
                    mc.player.connection.getNetworkManager().sendPacketWithoutEvent(timedPacket.getPacket());
                    packets.remove(timedPacket);
                }
            }
        }

        if (event instanceof EventPacket e) {
            if (e.isSendPacket()) {
                if (e.getPacket() instanceof CEntityActionPacket p) {
                    if (p.getAction() == CEntityActionPacket.Action.START_SPRINTING || p.getAction() == CEntityActionPacket.Action.STOP_SPRINTING) {
                        e.setCancel(true);
                    }
                }
            }
        }

        if (event instanceof EventAction e) {
            e.setSprintState(false);
        }

        if (event instanceof EventInput e) {
            if (MoveUtil.isMoving())
                e.setJump(false);
            e.setSneak(false);
            if (!mc.player.isOnGround()) {
                e.setForward(0);
                e.setStrafe(0);
            }

            if (rotation != null) {
                RayTraceResult result = RayTraceUtil.rayTrace(3, rotation.x, rotation.y, mc.player);
                if (result.getType() != RayTraceResult.Type.BLOCK && mc.world.getBlockState(mc.player.getPosition().add(0, -0.5f, 0)).getBlock() == Blocks.AIR) {
                    e.setSneak(true);
                }
            }

        }

        if (event instanceof EventMove e) {
            if (mc.player.isOnGround())
                MoveUtil.MoveEvent.setMoveMotion(e, Math.min(0.16779580771923064, MoveUtil.getMotion()));
        }
        if (event instanceof EventMotion e) {
            if (mc.player.isOnGround()) {
                savedY = (float) Math.floor(mc.player.getPosY() - 1.0);
            }

            blockCache = getBlockInfo();
            if (blockCache != null) {
                lastBlockCache = getBlockInfo();
            } else {
                return;
            }
            if (mc.world.getBlockState(mc.player.getPosition().add(0, -0.5f, 0)).getBlock() == Blocks.AIR) {
                float[] rot = getRotations(blockCache.position, blockCache.facing);
                rotation = new Vector2f(rot[0], rot[1]);

                e.setYaw(rotation.x);
                e.setPitch(rotation.y);


                mc.player.rotationPitchHead = rotation.y;
                mc.player.rotationYawHead = rotation.x;
            }


        }

        if (event instanceof EventUpdate e) {

            if (blockCache == null || lastBlockCache == null) return;

            int block = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack s = mc.player.inventory.getStackInSlot(i);
                if (s.getItem() instanceof BlockItem) {
                    block = i;
                    break;
                }
            }

            if (block == -1) {
                ClientUtil.sendMesage("Для использования этой функции у вас должны блоки в хотбаре!");
                toggle();
                return;
            }

            if (rotation == null)
                return;

            RayTraceResult result = RayTraceUtil
                    .rayTrace(3,
                            rotation.x,
                            rotation.y,
                            mc.player);

            if (mc.world.getBlockState(mc.player.getPosition().add(0, -0.5f, 0)).getBlock() == Blocks.AIR && result.getType() == RayTraceResult.Type.BLOCK) {
                int last = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = block;
                mc.playerController.processRightClickBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockRayTraceResult(getVector(lastBlockCache), lastBlockCache.getFacing(), lastBlockCache.getPosition(), false));
                mc.player.connection.sendPacket(new CAnimateHandPacket(Hand.MAIN_HAND));
                blockCache = null;
                mc.player.inventory.currentItem = last;
            }
        }

    }

    public float[] getRotations(BlockPos blockPos, Direction enumFacing) {
        double d = (double) blockPos.getX() + 0.5 - mc.player.getPosX() + (double) enumFacing.getXOffset() * 0.25;
        double d2 = (double) blockPos.getZ() + 0.5 - mc.player.getPosZ() + (double) enumFacing.getZOffset() * 0.25;
        double d3 = mc.player.getPosY() + (double) mc.player.getEyeHeight() - blockPos.getY() - (double) enumFacing.getYOffset() * 0.25;
        double d4 = MathHelper.sqrt(d * d + d2 * d2);
        float f = (float) (Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float) (Math.atan2(d3, d4) * 180.0 / Math.PI);
        return new float[]{MathHelper.wrapDegrees(f), f2};
    }

    public class BlockCache {

        private final BlockPos position;
        private final Direction facing;

        public BlockCache(final BlockPos position, final Direction facing) {
            this.position = position;
            this.facing = facing;
        }

        public BlockPos getPosition() {
            return this.position;
        }

        public Direction getFacing() {
            return this.facing;
        }
    }


    public BlockCache getBlockInfo() {

        int y = (int) (mc.player.getPosY() - 1.0 >= savedY && Math.max(mc.player.getPosY(), savedY)
                - Math.min(mc.player.getPosY(), savedY) <= 3.0 && !mc.gameSettings.keyBindJump.isKeyDown()
                ? savedY
                : mc.player.getPosY() - 1.0);

        final BlockPos belowBlockPos = new BlockPos(mc.player.getPosX(), y - (mc.player.isSneaking() ? -1 : 0), mc.player.getPosZ());
        if (mc.world.getBlockState(belowBlockPos).getBlock() instanceof AirBlock) {
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    for (int i = -1; i < 1; i += 1) {
                        final BlockPos blockPos = belowBlockPos.add(x * i, 0, z * i);
                        if (mc.world.getBlockState(blockPos).getBlock() instanceof AirBlock) {
                            for (Direction direction : Direction.values()) {
                                final BlockPos block = blockPos.offset(direction);
                                final Material material = mc.world.getBlockState(block).getBlock().getDefaultState().getMaterial();
                                if (material.isSolid() && !material.isLiquid()) {
                                    return new BlockCache(block, direction.getOpposite());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public Vector3d getVector(BlockCache data) {
        BlockPos pos = data.position;
        Direction face = data.facing;
        double x = (double) pos.getX() + 0.5, y = (double) pos.getY() + 0.5, z = (double) pos.getZ() + 0.5;
        if (face != Direction.UP && face != Direction.DOWN) {
            y += 0.5;
        } else {
            x += 0.3;
            z += 0.3;
        }
        if (face == Direction.WEST || face == Direction.EAST) {
            z += 0.15;
        }
        if (face == Direction.SOUTH || face == Direction.NORTH) {
            x += 0.15;
        }
        return new Vector3d(x, y, z);
    }

    public static class TimedPacket {
        private final IPacket<?> packet;
        private final long time;

        public TimedPacket(final IPacket<?> packet, final long time) {
            this.packet = packet;
            this.time = time;
        }

        public IPacket<?> getPacket() {
            return packet;
        }

        public long getTime() {
            return time;
        }
    }

}
