package today.flux.addon.api.entities;

import com.soterdev.SoterObfuscator;
import net.minecraft.entity.EntityLivingBase;
import today.flux.utility.MoveUtils;

public class AddonEntityLivingBase extends AddonEntity {
    public AddonEntityLivingBase(EntityLivingBase entity) {
        super(entity);
    }

    
    public EntityLivingBase getEntity() {
        return ((EntityLivingBase) entity);
    }

    /**
     * 获取实体MoveForward
     * @return MoveForward值
     */
    
    public float getMoveForward() {
        return getEntity().moveForward;
    }

    /**
     * 获取实体MoveStrafing值
     * @return MoveStrafing值
     */
    
    public float getMoveStrafing() {
        return getEntity().moveStrafing;
    }

    /**
     * 获取实体MoveForward值
     * @param forward MoveForward值
     */
    
    public void setMoveForward(float forward) {
        getEntity().moveForward = forward;
    }

    /**
     * 获取实体MoveStrafing值
     * @param strafing MoveStrafing值
     */
    
    public void setMoveStrafing(float strafing) {
        getEntity().moveStrafing = strafing;
    }

    /**
     * 获取实体血量
     * @return 实体血量
     */
    
    public float getHealth() {
        return getEntity().getHealth();
    }

    /**
     * 获取实体是否在梯子上
     * @return 是否在梯子上
     */
    
    public boolean isOnLadder() {
        return getEntity().isOnLadder();
    }

    /**
     * 获取实体受伤时间
     * @return 受伤时间
     */
    
    public int getHurtTime() {
        return getEntity().hurtTime;
    }

    /**
     * 设置实体JumpMovementFactor
     * @param factor 实体JumpMovementFactor
     */
    
    public void setJumpMovementFactor(float factor) {
        getEntity().jumpMovementFactor = factor;
    }

    /**
     * 获取实体JumpMovementFactor
     * @return 实体JumpMovementFactor
     */
    
    public float getJumpMovementFactor() {
        return getEntity().jumpMovementFactor;
    }

    /**
     * 摇摆手上物品
     */
    
    public void swingItem() {
        getEntity().swingItem();
    }


    /**
     * 获取基础移动速度
     * @return 基础移动速度
     */
    
    public double getBaseMoveSpeed() {
        return MoveUtils.defaultSpeed((EntityLivingBase) entity);
    }
}
