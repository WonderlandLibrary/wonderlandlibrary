package me.rhys.base.util.entity;

import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class Location {
  public float yaw;
  
  public float pitch;
  
  public double x;
  
  public double y;
  
  public double z;
  
  public boolean ignoreF3Rotations;
  
  public boolean can3DRotate;
  
  public Location(Entity entity) {
    this.yaw = entity.rotationYaw;
    this.pitch = entity.rotationPitch;
    this.x = entity.posX;
    this.y = entity.posY;
    this.z = entity.posZ;
  }
  
  public Location(float yaw, float pitch, double x, double y, double z) {
    this.yaw = yaw;
    this.pitch = pitch;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Location(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Location(float yaw, float pitch) {
    this.yaw = yaw;
    this.pitch = pitch;
  }
  
  public Location clone() {
    return new Location(this.yaw, this.pitch, this.x, this.y, this.z);
  }
  
  public Location normalize() {
    normalizeX();
    normalizeY();
    normalizeZ();
    return this;
  }
  
  public boolean isCan3DRotate() {
    return this.can3DRotate;
  }
  
  public void setCan3DRotate(boolean can3DRotate) {
    this.can3DRotate = can3DRotate;
  }
  
  public boolean isIgnoreF3Rotations() {
    return this.ignoreF3Rotations;
  }
  
  public void setIgnoreF3Rotations(boolean ignoreF3Rotations) {
    this.ignoreF3Rotations = ignoreF3Rotations;
  }
  
  public Location normalizeX() {
    (Minecraft.getMinecraft()).thePlayer.motionX = this.x;
    return this;
  }
  
  public Location normalizeY() {
    (Minecraft.getMinecraft()).thePlayer.motionY = this.y;
    return this;
  }
  
  public Location normalizeZ() {
    (Minecraft.getMinecraft()).thePlayer.motionZ = this.z;
    return this;
  }
  
  public Location add(Location location) {
    this.x += location.x;
    this.y += location.y;
    this.z += location.z;
    return this;
  }
  
  public Location subtract(Location location) {
    this.x -= location.x;
    this.y -= location.y;
    this.z -= location.z;
    return this;
  }
  
  public Location multiply(Location location) {
    this.x *= location.x;
    this.y *= location.y;
    this.z *= location.z;
    return this;
  }
  
  public Location divide(Location location) {
    this.x /= location.x;
    this.y /= location.y;
    this.z /= location.z;
    return this;
  }
  
  public Location add(double x, double y, double z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }
  
  public Location subtract(double x, double y, double z) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    return this;
  }
  
  public Location multiply(double x, double y, double z) {
    this.x *= x;
    this.y *= y;
    this.z *= z;
    return this;
  }
  
  public Location divide(double x, double y, double z) {
    this.x /= x;
    this.y /= y;
    this.z /= z;
    return this;
  }
  
  public Location setYaw(float yaw) {
    this.yaw = yaw;
    return this;
  }
  
  public Location setPitch(float pitch) {
    this.pitch = pitch;
    return this;
  }
  
  public Location setX(double x) {
    this.x = x;
    return this;
  }
  
  public Location setY(double y) {
    this.y = y;
    return this;
  }
  
  public Location setZ(double z) {
    this.z = z;
    return this;
  }
  
  public Vec2f getRotations() {
    return new Vec2f(this.yaw, this.pitch);
  }
  
  public Location setRotation(Vec2f rotation) {
    if (rotation == null)
      return this; 
    this.yaw = rotation.x;
    this.pitch = Math.min(Math.max(rotation.y, -90.0F), 90.0F);
    return this;
  }
  
  public float getYaw() {
    return this.yaw;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public double getX() {
    return this.x;
  }
  
  public double getY() {
    return this.y;
  }
  
  public double getZ() {
    return this.z;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\entity\Location.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */