package today.flux.module.implement.Combat.killaura;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.Vec3;
import net.minecraft.util.*;
import today.flux.Flux;
import today.flux.event.*;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.module.implement.Combat.Criticals;
import today.flux.module.implement.Combat.KillAura;
import today.flux.utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class AAC extends SubModule {
    private static EntityLivingBase currentTarget;

    public List<EntityLivingBase> targets = new ArrayList<>();
    private DelayTimer switchTimer = new DelayTimer();
    private DelayTimer attackTimer = new DelayTimer();
    private int Index;

    private float delay;

    private float virtualYaw;
    private float virtualPitch;

    private float pitchExpand;
    private boolean expandDirectionPitch;

    private float yawExpand;
    private boolean expandDirectionYaw;

    private int aps;
    private DelayTimer apsTimer = new DelayTimer();
    private int apsDelay;

    private PreUpdateEvent beforeLooked;
    private boolean hitting;

    public AAC() {
        super("AAC", "KillAura");
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.virtualPitch = this.mc.thePlayer.rotationPitch;
        this.virtualYaw = this.mc.thePlayer.rotationYaw;
        this.pitchExpand = 0;
        this.yawExpand = 0;
        this.expandDirectionYaw = false;
        this.expandDirectionPitch = false;
        this.setDelay();
        this.setApsDelay();

        ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Caution!" + EnumChatFormatting.WHITE + " AAC mode is still in beta.");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        currentTarget = null;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (apsTimer.hasPassed(this.apsDelay)) {
            this.aps = 10 + new Random().nextInt(3);
            this.setApsDelay();
            apsTimer.reset();
        }
    }
    @EventTarget
    public void onRespawn(RespawnEvent respawnEvent) {
        if (KillAura.autodisable.getValue()) {
            this.setEnabled(false);
            NotificationManager.show("Module", this.getName() + " Disabled (Auto)", Notification.Type.INFO);
        }
    }


    @EventTarget
    public void onUpdatePre(PreUpdateEvent event) {
        this.beforeLooked = null;
        this.targets = this.getTargets();

        if (event.isModified())
            return;

        if (!isValid(currentTarget, false)) {
            this.setTarget(false);
        }

        if (!isValid(currentTarget, false)) {
            this.virtualYaw = this.mc.thePlayer.rotationYaw;
            this.virtualPitch = this.mc.thePlayer.rotationPitch;
            // TODO: Add SmoothReturning?
            return;
        }

        if (this.rayTrace(false) != currentTarget) {
            // TODO: Make as SmartRandomize?
            ChatUtils.debug(this.getFoVDistance(virtualYaw, currentTarget));
            float[] rotations = this.faceTarget(currentTarget, Math.max(10, this.getFoVDistance(virtualYaw, currentTarget) * 0.8f), 10 + new Random().nextInt(30), false);

            virtualYaw = rotations[0];
            virtualPitch = rotations[1];
        }

        event.setYaw(virtualYaw);
        event.setPitch(virtualPitch);

        //smart randomizer (more optimizable)
        if (pitchExpand > 6 && expandDirectionPitch)
            expandDirectionPitch = false;

        if (pitchExpand < -6 && !expandDirectionPitch)
            expandDirectionPitch = true;

        if (yawExpand > 3 && expandDirectionYaw)
            expandDirectionYaw = false;

        if (yawExpand < -3 && !expandDirectionYaw)
            expandDirectionYaw = true;

        // TODO: Randomize those?
        pitchExpand += expandDirectionPitch ? 1 : -1;
        yawExpand += expandDirectionYaw ? 1 : -1;

        if (this.rayTrace(false) == currentTarget) {
            event.setPitch(virtualPitch + pitchExpand);
            event.setYaw(virtualYaw + yawExpand);
        }

        this.beforeLooked = event;
    }

    private float getFoVDistance(final float yaw, final Entity e) {
        return ((Math.abs(RotationUtils.getRotations(e)[0] - yaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(RotationUtils.getRotations(e)[0] - yaw) % 360.0f) : (Math.abs(RotationUtils.getRotations(e)[0] - yaw) % 360.0f));
    }

    private EntityLivingBase rayTrace(boolean attack) {
        Entity entity = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());

        float wasYaw = this.mc.getRenderViewEntity().rotationYaw;
        float wasPitch = this.mc.getRenderViewEntity().rotationPitch;

        this.mc.getRenderViewEntity().rotationYaw = virtualYaw;
        this.mc.getRenderViewEntity().rotationPitch = virtualPitch;

        entity.copyDataFromOld(this.mc.getRenderViewEntity());
        entity.copyLocationAndAnglesFrom(this.mc.getRenderViewEntity());

        this.mc.getRenderViewEntity().rotationPitch = wasPitch;
        this.mc.getRenderViewEntity().rotationYaw = wasYaw;

        return this.entityRaytrace(entity, 1.0f, 4.0f, entity.rayTrace(4.0, 1.0f), !attack);
    }

    private EntityLivingBase entityRaytrace(Entity entity, float partialTicks, double blockReachDistance, MovingObjectPosition input, boolean minimize) {
        Entity pointedEntity = null;
        Vec3 vec3 = entity.getPositionEyes(partialTicks);
        Vec3 vec31 = entity.getLook(partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        float f = 1.0f;
        List list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntityRenderer1(this.mc.entityRenderer)));
        double d2 = input != null ? input.hitVec.distanceTo(vec3) : blockReachDistance;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1 == this.mc.thePlayer)
                continue;

            float f1 = minimize ? -0.1f : entity1.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

            if (axisalignedbb.isVecInside(vec3)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    d2 = 0.0D;
                }
            } else if (movingobjectposition != null) {
                double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1 == entity.ridingEntity) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                        }
                    } else {
                        pointedEntity = entity1;
                        d2 = d3;
                    }
                }
            }
        }

        return (EntityLivingBase) pointedEntity;
    }

    private float[] faceTarget(final Entity target, final float yawSpeed, final float pitchSpeed, final boolean miss) {
        final double var4 = target.posX - this.mc.thePlayer.posX;
        final double var5 = target.posZ - this.mc.thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase) target;
            var7 = var6.posY + var6.getEyeHeight() - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());
        } else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());
        }

        Random rnd = new Random();
        final float offset = miss ? (rnd.nextInt(15) * 0.25f + 5.0f) : 0.0f;
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float) (Math.atan2(var5 + offset, var4) * 180.0 / Math.PI) - 90.0f;
        final float var10 = (float) (-(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.5f : 0.0f) + offset, var8) * 180.0 / Math.PI));
        final float pitch = changeRotation(virtualPitch, var10, pitchSpeed);
        final float yaw = changeRotation(virtualYaw, var9, yawSpeed);
        return new float[]{yaw, pitch};
    }

    private float changeRotation(final float var1, final float var2, final float var3) {
        float var4 = MathHelper.wrapAngleTo180_float(var2 - var1);
        if (var4 > var3) {
            var4 = var3;
        }
        if (var4 < -var3) {
            var4 = -var3;
        }
        return var1 + var4;
    }

    private void setTarget(boolean doSwitch) {
        if (targets == null || targets.size() == 0) {
            currentTarget = null;
            return;
        }

        if (doSwitch && targets.size() > 1) {
            if (Index >= targets.size() - 1) {
                Index = 0;
            } else {
                Index++;
            }

            if (targets.get(Index) != currentTarget) {
                this.switchTimer.reset();
            }

            currentTarget = targets.get(Index);
        } else {
            currentTarget = targets.get(0);
        }
    }

    private void setDelay() {
        this.delay = (int) (1000.00 / this.aps) - new Random().nextInt(20);
    }

    private void setApsDelay() {
        this.apsDelay = 300 + new Random().nextInt(100);
    }

    @EventTarget
    public void onEventUpdatePost(LoopEvent post) {
        if (Criticals.isEating()) {
            return;
        }

        if (!isValid(currentTarget, false) || !this.attackTimer.hasPassed(this.delay)) {
            return;
        }

        if (InvUtils.getCurrentItem().getItem() == Items.fishing_rod) {
            return;
        }

        this.attack(this.rayTrace(true));
        this.attackTimer.reset();

        this.setDelay();

        if (switchTimer.hasPassed(5000)) {
            this.setTarget(true);
        }
    }

    private void attack(final EntityLivingBase entity) {
        if (this.mc.thePlayer.isBlocking()) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }

        if (entity != null && entity.getDistanceToEntity(this.mc.thePlayer) <= 4.0f) {
            this.mc.thePlayer.swingItem();
            this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
            this.mc.thePlayer.onEnchantmentCritical(entity);
            this.mc.thePlayer.onEnchantmentCritical(entity);
            this.hitting = true;
        } else {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            this.hitting = false;
        }
    }

   /* private float getRotationToEntityYaw(final Entity entity) {
        return RotationUtils.getRotationToEntity(entity)[0];
    }*/

    private List<EntityLivingBase> getTargets() {
        return WorldUtil.getLivingEntities().stream()
                .filter(entityLivingBase -> this.isValid(entityLivingBase, false))
                //.sorted(Comparator.comparing(this::getRotationToEntityYaw))
                .collect(Collectors.toList());
    }

    private int rangeExtend;
    public boolean isValid(final EntityLivingBase entity, final boolean attack) {
        if (entity == null || entity.isDead || entity.getHealth() <= 0)
            return false;

        float range = this.mc.thePlayer.canEntityBeSeenFixed(entity) ? 4.0f : 0f;

        if (!attack) {
            rangeExtend++;
            if (rangeExtend > 1)
                rangeExtend = 0;

            range = 4 + rangeExtend;
        }

        //range
        if (!RotationUtils.isValidRangeForNCP(entity, range))
            return false;

       // if (entity instanceof EntityMob || entity instanceof EntityCreature || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid)
     //       return false;

        if (entity instanceof EntityArmorStand) //NO ARMOR STAND
            return false;

        if(entity.isInvisible()){
            return false;
        }

        //friend
        if (entity instanceof EntityPlayer && Flux.INSTANCE.getFriendManager().isFriend(entity.getName()))
            return false;

        //team
        if (Flux.teams.getValue() && entity instanceof EntityPlayer && FriendManager.isTeam((EntityPlayer) entity))
            return false;


        //bots (last)
        if (ModuleManager.antiBotsMod.isEnabled() && ModuleManager.antiBotsMod.isNPC(entity))
            return false;

        return true;
    }

    @EventTarget
    private void onRender3D(final WorldRenderEvent event) {
        if (this.beforeLooked == null)
            return;

        Vec3 look = this.getVectorForRotation(this.beforeLooked.getPitch(), this.beforeLooked.getYaw());

        double posX = (this.mc.thePlayer.lastTickPosX + (look.xCoord * 4)) + ((this.mc.thePlayer.posX + (look.xCoord * 4)) - (this.mc.thePlayer.lastTickPosX + (look.xCoord * 4))) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosX();
        double posY = (this.mc.thePlayer.lastTickPosY + this.mc.thePlayer.getEyeHeight() + (look.yCoord * 4)) + ((this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight() + (look.yCoord * 4)) - (this.mc.thePlayer.lastTickPosY + this.mc.thePlayer.getEyeHeight() + (look.yCoord * 4))) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosY();
        double posZ = (this.mc.thePlayer.lastTickPosZ + (look.zCoord * 4)) + ((this.mc.thePlayer.posZ + (look.zCoord * 4)) - (this.mc.thePlayer.lastTickPosZ + (look.zCoord * 4))) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosZ();

      //  WorldRenderUtils.drawPointESP(posX, posY, posZ, hitting ? new Color(0xFFE500).getRGB() : new Color(0xFF0009).getRGB());
    }

    private Vec3 getVectorForRotation(float pitch, float yaw) {
        float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
        float pitchSin = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double) (yawSin * pitchCos), (double) pitchSin, (double) (yawCos * pitchCos));
    }
}

class EntityRenderer1 implements Predicate
{
    final EntityRenderer field_90032_a;

    public EntityRenderer1(EntityRenderer p_i1243_1_)
    {
        this.field_90032_a = p_i1243_1_;
    }

    public boolean apply(Entity p_apply_1_)
    {
        return p_apply_1_.canBeCollidedWith();
    }

    public boolean apply(Object p_apply_1_)
    {
        return this.apply((Entity)p_apply_1_);
    }
}