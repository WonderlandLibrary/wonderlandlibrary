package rip.helium.cheat.impl.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.MathUtils;
import rip.helium.utils.Stopwatch;

import java.util.ArrayList;

public class ChestAura extends Cheat {
    private boolean openInventory;
    private final ArrayList<BlockPos> blackListedLocation;
    private final Stopwatch resetPositions;
    private int position;

    public ChestAura() {
        super("Chest Aura", "Opens chests around you", CheatCategory.MISC);
        this.resetPositions = new Stopwatch();
        this.blackListedLocation = new ArrayList<BlockPos>();
    }

    public static boolean isValidPosition(final boolean placeable, final BlockPos pos) {
        final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        return block == Blocks.chest;
    }

    public void onEnable() {
        this.resetPositions.reset();
    }

    public void onDisable() {
        this.resetPositions.reset();
    }

    @Collect
    public void onPlayerUpdate(final PlayerUpdateEvent event) {
        if (this.resetPositions.hasPassed(30000.0)) {
            this.blackListedLocation.clear();
            this.resetPositions.reset();
        }
        final BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + this.position, mc.thePlayer.posZ);
        final BlockData blockData = this.retriveBlockData(1, true, true, playerPos);
        if (this.position >= 2) {
            this.position = 0;
        } else {
            ++this.position;
        }
        final boolean steal = true;
        if (blockData == null || this.openInventory || !steal) {
            return;
        }
        final BlockPos sideBlock = blockData.position;
        final Entity temp = new EntitySnowball(mc.theWorld);
        temp.posX = sideBlock.getX() + 0.5;
        temp.posY = sideBlock.getY() + 0.5;
        temp.posZ = sideBlock.getZ() + 0.5;
        if (mc.theWorld.getBlockState(blockData.position).getBlock() == Blocks.chest && !this.blackListedLocation.contains(blockData.position)) {
            if (event.isPre()) {
                this.setYaw(event, this.getBlockRotations(sideBlock.getX(), sideBlock.getY(), sideBlock.getZ(), blockData.face)[0]);
                event.setPitch(this.getBlockRotations(sideBlock.getX(), sideBlock.getY(), sideBlock.getZ(), blockData.face)[1] + 12.0f);
            } else {
                final double hitvecx = blockData.position.getX() + 0.5 + MathUtils.getRandomInRange(-0.03, 0.263) + blockData.face.getFrontOffsetX() / 2;
                final double hitvecy = blockData.position.getY() + 0.5 + MathUtils.getRandomInRange(-0.03, 0.263) + blockData.face.getFrontOffsetY() / 2;
                final double hitvecz = blockData.position.getZ() + 0.5 + MathUtils.getRandomInRange(-0.03, 0.263) + blockData.face.getFrontOffsetZ() / 2;
                final Vec3 vec = new Vec3(hitvecx, hitvecy, hitvecz);
                this.blackListedLocation.add(blockData.position);
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), blockData.position, blockData.face, vec);
                mc.thePlayer.swingItem();
                this.openInventory = true;
            }
        }
    }

    public void setYaw(final PlayerUpdateEvent e, final float yaw) {
        final boolean notarget = !Helium.instance.cheatManager.isCheatEnabled("KillAura");
        if (notarget) {
            e.setYaw(yaw);
        }
    }

    private float[] getBlockRotations(final int x, final int y, final int z, final EnumFacing facing) {
        final Entity temp = new EntitySnowball(mc.theWorld);
        temp.posX = x + 0.5;
        temp.posY = y + 0.5;
        temp.posZ = z + 0.5;
        return mc.thePlayer.canEntityBeSeen(temp) ? this.getAngles(temp) : this.getRotationToBlock(new BlockPos(x, y, z), facing);
    }

    private float[] getAngles(final Entity e) {
        return new float[]{this.getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, this.getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch};
    }

    private float getYawChangeToEntity(final Entity entity) {
        final double deltaX = entity.posX - mc.thePlayer.posX;
        final double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
    }

    public float[] getRotationToBlock(final BlockPos pos, final EnumFacing face) {
        final double xDiff = pos.getX() + 0.5 - mc.thePlayer.posX + face.getDirectionVec().getX() / 2;
        final double zDiff = pos.getZ() + 0.5 - mc.thePlayer.posZ + face.getDirectionVec().getZ() / 2;
        final double yDiff = pos.getY() - mc.thePlayer.posY - 1.0;
        final double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (-Math.toDegrees(Math.atan2(xDiff, zDiff)));
        final float pitch = (float) (-Math.toDegrees(Math.atan(yDiff / distance)));
        return new float[]{(Math.abs(yaw - mc.thePlayer.rotationYaw) < 0.1) ? mc.thePlayer.rotationYaw : yaw, (Math.abs(pitch - mc.thePlayer.rotationPitch) < 0.1) ? mc.thePlayer.rotationPitch : pitch};
    }

    private float getPitchChangeToEntity(final Entity entity) {
        final double deltaX = entity.posX - mc.thePlayer.posX;
        final double deltaZ = entity.posZ - mc.thePlayer.posZ;
        final double deltaY = entity.posY - 1.6 + entity.getEyeHeight() - 0.4 - mc.thePlayer.posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
    }

    @Collect
    public void onSendPacket(final SendPacketEvent event) {
        if (event.getPacket() instanceof C0DPacketCloseWindow) {
            this.openInventory = false;
        }
    }

    private BlockData retriveBlockData(final int i, final boolean placeable, final boolean godown, final BlockPos pos) {
        final EnumFacing up = EnumFacing.UP;
        final EnumFacing east = EnumFacing.EAST;
        final EnumFacing west = EnumFacing.WEST;
        final EnumFacing north = EnumFacing.NORTH;
        final EnumFacing south = EnumFacing.SOUTH;
        if (isValidPosition(true, pos.add(0, -i, 0))) {
            return new BlockData(pos.add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(0, 0, i))) {
            return new BlockData(pos.add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(0, 0, -i))) {
            return new BlockData(pos.add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0))) {
            return new BlockData(pos.add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(i, 0, 0))) {
            return new BlockData(pos.add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(0, -i, 0))) {
            return new BlockData(pos.add(-i, 0, 0).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(0, 0, i))) {
            return new BlockData(pos.add(-i, 0, 0).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(0, 0, -i))) {
            return new BlockData(pos.add(-i, 0, 0).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(-i, 0, 0))) {
            return new BlockData(pos.add(-i, 0, 0).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(i, 0, 0))) {
            return new BlockData(pos.add(-i, 0, 0).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(0, -i, 0))) {
            return new BlockData(pos.add(i, 0, 0).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(0, 0, i))) {
            return new BlockData(pos.add(i, 0, 0).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(0, 0, -i))) {
            return new BlockData(pos.add(i, 0, 0).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(-i, 0, 0))) {
            return new BlockData(pos.add(i, 0, 0).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(i, 0, 0))) {
            return new BlockData(pos.add(i, 0, 0).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(0, -i, 0))) {
            return new BlockData(pos.add(0, 0, i).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(0, 0, i))) {
            return new BlockData(pos.add(0, 0, i).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(0, 0, -i))) {
            return new BlockData(pos.add(0, 0, i).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(-i, 0, 0))) {
            return new BlockData(pos.add(0, 0, i).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(i, 0, 0))) {
            return new BlockData(pos.add(0, 0, i).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(0, -i, 0))) {
            return new BlockData(pos.add(0, 0, -i).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(0, 0, i))) {
            return new BlockData(pos.add(0, 0, -i).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(0, 0, -i))) {
            return new BlockData(pos.add(0, 0, -i).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(-i, 0, 0))) {
            return new BlockData(pos.add(0, 0, -i).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(i, 0, 0))) {
            return new BlockData(pos.add(0, 0, -i).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(0, -i, 0))) {
            return new BlockData(pos.add(-i, 0, 0).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(0, 0, i))) {
            return new BlockData(pos.add(-i, 0, 0).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(0, 0, -i))) {
            return new BlockData(pos.add(-i, 0, 0).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(-i, 0, 0))) {
            return new BlockData(pos.add(-i, 0, 0).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(-i, 0, 0).add(i, 0, 0))) {
            return new BlockData(pos.add(-i, 0, 0).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(0, -i, 0))) {
            return new BlockData(pos.add(i, 0, 0).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(0, 0, i))) {
            return new BlockData(pos.add(i, 0, 0).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(0, 0, -i))) {
            return new BlockData(pos.add(i, 0, 0).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(-i, 0, 0))) {
            return new BlockData(pos.add(i, 0, 0).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(i, 0, 0).add(i, 0, 0))) {
            return new BlockData(pos.add(i, 0, 0).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(0, -i, 0))) {
            return new BlockData(pos.add(0, 0, i).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(0, 0, i))) {
            return new BlockData(pos.add(0, 0, i).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(0, 0, -i))) {
            return new BlockData(pos.add(0, 0, i).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(-i, 0, 0))) {
            return new BlockData(pos.add(0, 0, i).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(0, 0, i).add(i, 0, 0))) {
            return new BlockData(pos.add(0, 0, i).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(0, -i, 0))) {
            return new BlockData(pos.add(0, 0, -i).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(0, 0, i))) {
            return new BlockData(pos.add(0, 0, -i).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(0, 0, -i))) {
            return new BlockData(pos.add(0, 0, -i).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(-i, 0, 0))) {
            return new BlockData(pos.add(0, 0, -i).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(0, 0, -i).add(i, 0, 0))) {
            return new BlockData(pos.add(0, 0, -i).add(i, 0, 0), west);
        }
        if (isValidPosition(true, pos.add(0, -i, 0).add(0, -i, 0))) {
            return new BlockData(pos.add(0, -i, 0).add(0, -i, 0), up);
        }
        if (isValidPosition(true, pos.add(0, -i, 0).add(0, 0, i))) {
            return new BlockData(pos.add(0, -i, 0).add(0, 0, i), north);
        }
        if (isValidPosition(true, pos.add(0, -i, 0).add(0, 0, -i))) {
            return new BlockData(pos.add(0, -i, 0).add(0, 0, -i), south);
        }
        if (isValidPosition(true, pos.add(0, -i, 0).add(-i, 0, 0))) {
            return new BlockData(pos.add(0, -i, 0).add(-i, 0, 0), east);
        }
        if (isValidPosition(true, pos.add(0, -i, 0).add(i, 0, 0))) {
            return new BlockData(pos.add(0, -i, 0).add(i, 0, 0), west);
        }
        return null;
    }

    private class BlockData {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
