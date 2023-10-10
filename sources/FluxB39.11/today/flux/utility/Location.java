package today.flux.utility;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Location {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public Location(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location(EntityLivingBase entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public Location add(final int x, final int y, final int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Location add(final double x, final double y, final double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Location subtract(final int x, final int y, final int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Location subtract(final double x, final double y, final double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Block getBlock() {
        return Minecraft.getMinecraft().theWorld.getBlockState(this.toBlockPos()).getBlock();
    }

    public double getX() {
        return this.x;
    }

    public Location setX(final double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return this.y;
    }

    public Location setY(final double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return this.z;
    }

    public Location setZ(final double z) {
        this.z = z;
        return this;
    }

    public float getYaw() {
        return this.yaw;
    }

    public Location setYaw(final float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Location setPitch(final float pitch) {
        this.pitch = pitch;
        return this;
    }

    public static Location fromBlockPos(final BlockPos blockPos) {
        return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this.getX(), this.getY(), this.getZ());
    }

    public double distanceTo(Location loc) {
        double dx = loc.x - this.x;
        double dz = loc.z - this.z;
        double dy = loc.y - this.y;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceToXZ(Location loc) {
        double dx = loc.x - this.x;
        double dz = loc.z - this.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public double distanceToY(Location loc) {
        double dy = loc.y - this.y;
        return Math.sqrt(dy * dy);
    }

    public Vec3 toVector(){
        return new Vec3(this.x, this.y, this.z);
    }
}
