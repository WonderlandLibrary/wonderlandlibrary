package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase {

    private static final Logger LOGGER = LogManager.getLogger();

    private final EntityLiving field_179434_b;
    private final Predicate<Entity> field_179435_c;
    private final EntityAINearestAttackableTarget.Sorter field_179432_d;
    private EntityLivingBase field_179433_e;

    public EntityAIFindEntityNearestPlayer(EntityLiving entityLiving) {
        this.field_179434_b = entityLiving;

        if (entityLiving instanceof EntityCreature) {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.field_179435_c = entity -> {
            if (!(entity instanceof EntityPlayer)) {
                return false;
            } else if (((EntityPlayer) entity).abilities.isDisabledDamage()) {
                return false;
            } else {
                double d0 = EntityAIFindEntityNearestPlayer.this.func_179431_f();

                if (entity.isSneaking()) {
                    d0 *= 0.800000011920929D;
                }

                if (entity.isInvisible()) {
                    float f = ((EntityPlayer) entity).getArmorVisibility();

                    if (f < 0.1F) {
                        f = 0.1F;
                    }

                    d0 *= 0.7F * f;
                }

                return !((double) entity.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.field_179434_b) > d0) && EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.field_179434_b, (EntityLivingBase) entity, false, true);
            }
        };
        this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(entityLiving);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        final double d0 = this.func_179431_f();
        final List<EntityPlayer> list = this.field_179434_b.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.field_179434_b.getEntityBoundingBox().expand(d0, 4.0D, d0), this.field_179435_c);

        list.sort(this.field_179432_d);

        if (list.isEmpty()) {
            return false;
        } else {
            this.field_179433_e = list.get(0);
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        final EntityLivingBase entitylivingbase = this.field_179434_b.getAttackTarget();

        if (entitylivingbase == null) {
            return false;
        } else if (!entitylivingbase.isEntityAlive()) {
            return false;
        } else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer) entitylivingbase).abilities.isDisabledDamage()) {
            return false;
        } else {
            final Team team = this.field_179434_b.getTeam();
            final Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team) {
                return false;
            } else {
                final double d0 = this.func_179431_f();
                return !(this.field_179434_b.getDistanceSqToEntity(entitylivingbase) > d0 * d0) && (!(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP) entitylivingbase).theItemInWorldManager.isCreative());
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.field_179434_b.setAttackTarget(this.field_179433_e);
        super.startExecuting();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.field_179434_b.setAttackTarget(null);
        super.startExecuting();
    }

    protected double func_179431_f() {
        final IAttributeInstance iattributeinstance = this.field_179434_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

}
