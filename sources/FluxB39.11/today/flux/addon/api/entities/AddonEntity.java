package today.flux.addon.api.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import today.flux.addon.api.utils.EntityPosition;
import today.flux.addon.api.utils.Motion;
import today.flux.addon.api.utils.Rotation;
import today.flux.utility.MoveUtils;
import today.flux.utility.PlayerUtils;

public class AddonEntity {
    protected final Entity entity;

    public AddonEntity(Entity entity) {
        this.entity = entity;
    }


    public Entity getEntity() {
        return entity;
    }

    public static Minecraft mc = Minecraft.getMinecraft();

    /**
     * 获取玩家的运动
     *
     * @return Motion对象
     */

    public Motion getMotion() {
        return new Motion(getEntity().motionX, getEntity().motionY, getEntity().motionZ);
    }

    /**
     * 设置玩家的运动
     *
     * @param motion Motion对象
     */

    public void setMotion(Motion motion) {
        getEntity().motionX = motion.x;
        getEntity().motionY = motion.y;
        getEntity().motionZ = motion.z;
    }

    /**
     * 获取实体坐标
     *
     * @return EntityPosition对象 EntityPosition(posX, posY, posZ)
     */

    public EntityPosition getPosition() {
        return new EntityPosition(getEntity().posX, getEntity().posY, getEntity().posZ);
    }

    /**
     * 获取实体上一个Tick的坐标
     *
     * @return EntityPosition对象 EntityPosition(posX, posY, posZ)
     */

    public EntityPosition getLastTickPosition() {
        return new EntityPosition(getEntity().lastTickPosX, getEntity().lastTickPosY, getEntity().lastTickPosZ);
    }

    /**
     * 设置实体坐标
     *
     * @param position EntityPosition对象实例
     */

    public void setPosition(EntityPosition position) {
        getEntity().setPosition(position.x, position.y, position.z);
    }

    /**
     * 获取头部方向数据
     *
     * @return Rotation对象 Rotation(Yaw, Pitch)
     */

    public Rotation getRotation() {
        return new Rotation(getEntity().rotationYaw, getEntity().rotationPitch);
    }

    /**
     * 设置头部方向
     *
     * @param rotation Rotation对象 Rotation(Yaw, Pitch)
     */

    public void setRotation(Rotation rotation) {
        getEntity().rotationYaw = rotation.yaw;
        getEntity().rotationPitch = rotation.pitch;
    }

    /**
     * 获取下落距离
     *
     * @return 下落距离
     */

    public float getFallDistance() {
        return getEntity().fallDistance;
    }

    /**
     * 设置下落距离
     *
     * @param distance 距离
     */

    public void setFallDistance(float distance) {
        getEntity().fallDistance = distance;
    }

    /**
     * 判断当前是否在地面
     *
     * @return 判断值
     */

    public boolean isOnGround() {
        return getEntity().onGround;
    }

    /**
     * 设置是否在地面
     *
     * @param onGround boolean值
     */

    public void setOnGround(boolean onGround) {
        getEntity().onGround = onGround;
    }

    /**
     * 判断是否在移动
     *
     * @return 判断值
     */

    public boolean isMoving() {
        return PlayerUtils.isMoving();
    }

    /**
     * 判断横向碰撞
     *
     * @return 判断值
     */

    public boolean isCollidedHorizontally() {
        return getEntity().isCollidedHorizontally;
    }

    /**
     * 判断纵向碰撞
     *
     * @return 判断值
     */

    public boolean isCollidedVertically() {
        return getEntity().isCollidedVertically;
    }

    /**
     * 获取实体存在存活了多少Ticks
     *
     * @return
     */

    public int getTicksExisted() {
        return getEntity().ticksExisted;
    }

    /**
     * 在聊天栏显示信息
     *
     * @param message 字符串信息
     */

    public void addChatMessage(String message) {
        PlayerUtils.tellPlayer(message);
    }

    /**
     * 获取实体名称
     *
     * @return 实体名称
     */

    public String getName() {
        return getEntity().getName();
    }

    /**
     * 获取当前玩家跳跃效果等级
     *
     * @return 跳跃效果等级
     */

    public int getJumpEffect() {
        return MoveUtils.getJumpEffect();
    }

    /**
     * 获取当前玩家速度效果等级
     *
     * @return 速度效果等级
     */

    public int getSpeedEffect() {
        return MoveUtils.getSpeedEffect();
    }

    /**
     * 判断当前玩家是否隐身
     *
     * @return 判断值
     */

    public boolean isInvisible() {
        return getEntity().isInvisible();
    }

    /**
     * 获取到某个坐标到到距离
     *
     * @param position EntityPosition对象 EntityPosition(posX, posY, posZ)
     * @return double距离值
     */

    public double getDistanceToPosition(EntityPosition position) {
        double xDiff = getPosition().x - position.x;
        double yDiff = getPosition().y - position.y;
        double zDiff = getPosition().z - position.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

    /**
     * 获取当前移动速度
     *
     * @return 当前移动速度
     */

    public double getMoveSpeed() {
        return PlayerUtils.getSpeed(entity);
    }
}

