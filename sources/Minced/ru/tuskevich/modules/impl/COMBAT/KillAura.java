// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import ru.tuskevich.util.math.MathUtility;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import ru.tuskevich.Minced;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.block.material.Material;
import ru.tuskevich.util.math.RotationUtility;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.client.entity.EntityPlayerSP;
import ru.tuskevich.util.GCDFixUtility;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.multiplayer.WorldClient;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.init.MobEffects;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.item.ItemShield;
import ru.tuskevich.util.world.InventoryUtility;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.Entity;
import ru.tuskevich.event.events.impl.EventMotion;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventInteract;
import ru.tuskevich.ui.dropui.setting.Setting;
import net.minecraft.item.ItemStack;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import javax.vecmath.Vector2f;
import net.minecraft.entity.EntityLivingBase;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "KillAura", type = Type.COMBAT)
public class KillAura extends Module
{
    public static EntityLivingBase target;
    public Vector2f rotation;
    public ModeSetting rotateMode;
    public MultiBoxSetting targetsSelection;
    public SliderSetting range;
    public SliderSetting rotateRange;
    public BooleanSetting randomClicks;
    public BooleanSetting onlyCritical;
    public BooleanSetting onlyWaterCritical;
    public BooleanSetting onlyWeapon;
    private boolean rotatedBefore;
    public TimerUtility timer;
    public static KillAura instance;
    float Yaw;
    float Pitch;
    float prevAdditionYaw;
    public ItemStack lastData;
    public TimerUtility timerUtility;
    
    public KillAura() {
        this.rotation = new Vector2f();
        this.rotateMode = new ModeSetting("Rotation Mode", "Advanced", new String[] { "Advanced", "Vulcan" });
        this.targetsSelection = new MultiBoxSetting("Target Selection", new String[] { "Players", "Naked Players", "NPC / Bots", "Mobs" });
        this.range = new SliderSetting("Range", 3.0f, 2.5f, 5.0f, 0.05f);
        this.rotateRange = new SliderSetting("Rotate Range", 0.5f, 0.0f, 2.0f, 0.05f);
        this.randomClicks = new BooleanSetting("Random Clicks", false, () -> this.rotateMode.is("Vulcan"));
        this.onlyCritical = new BooleanSetting("Only Critical", true);
        this.onlyWaterCritical = new BooleanSetting("Water Critical", true, () -> this.onlyCritical.get());
        this.onlyWeapon = new BooleanSetting("Only Weapon", false);
        this.timer = new TimerUtility();
        this.Yaw = 0.0f;
        this.Pitch = 0.0f;
        this.timerUtility = new TimerUtility();
        this.add(this.rotateMode, this.targetsSelection, this.range, this.rotateRange, this.randomClicks, this.onlyCritical, this.onlyWaterCritical, this.onlyWeapon);
    }
    
    @EventTarget
    public void onInteractEntity(final EventInteract e) {
        if (KillAura.target != null) {
            e.cancel();
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        final boolean isTargetValid = KillAura.target != null && this.isValid(KillAura.target);
        if (!isTargetValid) {
            KillAura.target = this.findTarget();
        }
        if (KillAura.target == null) {
            final Vector2f rotation = this.rotation;
            final Minecraft mc = KillAura.mc;
            rotation.x = Minecraft.player.rotationYaw;
            final Vector2f rotation2 = this.rotation;
            final Minecraft mc2 = KillAura.mc;
            rotation2.y = Minecraft.player.rotationPitch;
            return;
        }
        this.rotatedBefore = false;
        this.attack(KillAura.target);
        if (!this.rotatedBefore) {
            this.rotation(KillAura.target, false);
        }
    }
    
    @EventTarget
    public void onMotion(final EventMotion e) {
        final ItemStack lastData = this.lastData;
        final Minecraft mc = KillAura.mc;
        if (lastData != Minecraft.player.getActiveItemStack()) {
            this.timerUtility.reset();
            final Minecraft mc2 = KillAura.mc;
            this.lastData = Minecraft.player.getActiveItemStack();
        }
        if (KillAura.target != null) {
            e.setYaw(this.rotation.x);
            e.setPitch(this.rotation.y);
        }
    }
    
    private void attack(final EntityLivingBase base) {
        if (this.whenFalling()) {
            this.rotation(base, true);
            if (calculateDistance(base, this.rotation.x, this.rotation.y, this.range.getFloatValue(), true) == base) {
                final Minecraft mc = KillAura.mc;
                if (Minecraft.player.isHandActive()) {
                    final Minecraft mc2 = KillAura.mc;
                    final Item item = Minecraft.player.getActiveItemStack().getItem();
                    final Minecraft mc3 = KillAura.mc;
                    if (item.getItemUseAction(Minecraft.player.getActiveItemStack()) == EnumAction.BLOCK) {
                        final PlayerControllerMP playerController = KillAura.mc.playerController;
                        final Minecraft mc4 = KillAura.mc;
                        playerController.onStoppedUsingItem(Minecraft.player);
                    }
                }
                this.timerUtility.reset();
                final Minecraft mc5 = KillAura.mc;
                final NetHandlerPlayClient connection = Minecraft.player.connection;
                final Minecraft mc6 = KillAura.mc;
                connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SPRINTING));
                final PlayerControllerMP playerController2 = KillAura.mc.playerController;
                final Minecraft mc7 = KillAura.mc;
                playerController2.attackEntity(Minecraft.player, base);
                final Minecraft mc8 = KillAura.mc;
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                final Minecraft mc9 = KillAura.mc;
                if (Minecraft.player.isHandActive()) {
                    final Minecraft mc10 = KillAura.mc;
                    final Item item2 = Minecraft.player.getActiveItemStack().getItem();
                    final Minecraft mc11 = KillAura.mc;
                    if (item2.getItemUseAction(Minecraft.player.getActiveItemStack()) == EnumAction.BLOCK) {
                        final Minecraft mc12 = KillAura.mc;
                        final NetHandlerPlayClient connection2 = Minecraft.player.connection;
                        final Minecraft mc13 = KillAura.mc;
                        connection2.sendPacket(new CPacketPlayerTryUseItem(Minecraft.player.getActiveHand()));
                    }
                }
                breakShieldMethod(base, true);
            }
        }
    }
    
    public static Entity calculateDistance(final Entity target, final float yaw, final float pitch, final double distance, final boolean ignoreWalls) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Entity entity = mc.getRenderViewEntity();
        if (entity == null || mc.world == null) {
            return null;
        }
        RayTraceResult objectMouseOver = ignoreWalls ? null : rayTrace(distance, yaw, pitch);
        final Vec3d vec3d = entity.getPositionEyes(1.0f);
        boolean flag = false;
        double d1 = distance;
        if (distance > 3.0) {
            flag = true;
        }
        if (objectMouseOver != null) {
            d1 = objectMouseOver.hitVec.distanceTo(vec3d);
        }
        final Vec3d vec3d2 = getVectorForRotation(pitch, yaw);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);
        Entity pointedEntity = null;
        Vec3d vec3d4 = null;
        double d2 = d1;
        final AxisAlignedBB axisalignedbb = target.getEntityBoundingBox().expandXyz(target.getCollisionBorderSize());
        final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d3);
        if (axisalignedbb.isVecInside(vec3d)) {
            if (d2 >= 0.0) {
                pointedEntity = target;
                vec3d4 = ((raytraceresult == null) ? vec3d : raytraceresult.hitVec);
                d2 = 0.0;
            }
        }
        else if (raytraceresult != null) {
            final double d3 = vec3d.distanceTo(raytraceresult.hitVec);
            if (d3 < d2 || d2 == 0.0) {
                final boolean flag2 = false;
                if (!flag2 && target.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                    if (d2 == 0.0) {
                        pointedEntity = target;
                        vec3d4 = raytraceresult.hitVec;
                    }
                }
                else {
                    pointedEntity = target;
                    vec3d4 = raytraceresult.hitVec;
                    d2 = d3;
                }
            }
        }
        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d4) > distance) {
            pointedEntity = null;
            objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
        }
        if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
            objectMouseOver = new RayTraceResult(pointedEntity, vec3d4);
        }
        if (objectMouseOver == null) {
            return null;
        }
        return objectMouseOver.entityHit;
    }
    
    public static RayTraceResult rayTrace(final double blockReachDistance, final float yaw, final float pitch) {
        Minecraft.getMinecraft();
        final Vec3d vec3d = Minecraft.player.getPositionEyes(1.0f);
        final Vec3d vec3d2 = getVectorForRotation(pitch, yaw);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * blockReachDistance, vec3d2.y * blockReachDistance, vec3d2.z * blockReachDistance);
        return Minecraft.getMinecraft().world.rayTraceBlocks(vec3d, vec3d3, true, true, true);
    }
    
    public static void breakShieldMethod(final EntityLivingBase base, final boolean setting) {
        if (InventoryUtility.doesHotbarHaveAxe() && setting) {
            final int item = InventoryUtility.getAxe();
            if (base instanceof EntityPlayer && base.getActiveItemStack().getItem() instanceof ItemShield) {
                final Minecraft mc = KillAura.mc;
                Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(item));
                final PlayerControllerMP playerController = KillAura.mc.playerController;
                final Minecraft mc2 = KillAura.mc;
                playerController.attackEntity(Minecraft.player, base);
                final Minecraft mc3 = KillAura.mc;
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                final Minecraft mc4 = KillAura.mc;
                Minecraft.player.resetCooldown();
                final Minecraft mc5 = KillAura.mc;
                final NetHandlerPlayClient connection = Minecraft.player.connection;
                final Minecraft mc6 = KillAura.mc;
                connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
            }
        }
    }
    
    public boolean whenFalling() {
        boolean b = false;
        Label_0131: {
            if (this.onlyWaterCritical.get()) {
                final WorldClient world = KillAura.mc.world;
                final Minecraft mc = KillAura.mc;
                final double posX = Minecraft.player.posX;
                final Minecraft mc2 = KillAura.mc;
                final double posY = Minecraft.player.posY;
                final Minecraft mc3 = KillAura.mc;
                if (world.getBlockState(new BlockPos(posX, posY, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid) {
                    final WorldClient world2 = KillAura.mc.world;
                    final Minecraft mc4 = KillAura.mc;
                    final double posX2 = Minecraft.player.posX;
                    final Minecraft mc5 = KillAura.mc;
                    final double y = Minecraft.player.posY + 1.0;
                    final Minecraft mc6 = KillAura.mc;
                    if (world2.getBlockState(new BlockPos(posX2, y, Minecraft.player.posZ)).getBlock() instanceof BlockAir) {
                        b = true;
                        break Label_0131;
                    }
                }
            }
            b = false;
        }
        final boolean critWater = b;
        final Minecraft mc7 = KillAura.mc;
        boolean b2 = false;
        Label_0212: {
            if (!Minecraft.player.isPotionActive(MobEffects.BLINDNESS)) {
                final Minecraft mc8 = KillAura.mc;
                if (!Minecraft.player.isOnLadder()) {
                    final Minecraft mc9 = KillAura.mc;
                    if (!Minecraft.player.isInWater() || critWater) {
                        final Minecraft mc10 = KillAura.mc;
                        if (!Minecraft.player.isInWeb) {
                            final Minecraft mc11 = KillAura.mc;
                            if (!Minecraft.player.capabilities.isFlying) {
                                b2 = false;
                                break Label_0212;
                            }
                        }
                    }
                }
            }
            b2 = true;
        }
        final boolean reason = b2;
        final Minecraft mc12 = KillAura.mc;
        if (Minecraft.player.getCooledAttackStrength(1.5f) < 0.92f) {
            return false;
        }
        if (this.onlyCritical.get() && !reason) {
            if (MoveUtility.isBlockAboveHead()) {
                final Minecraft mc13 = KillAura.mc;
                if (Minecraft.player.onGround) {
                    final Minecraft mc14 = KillAura.mc;
                    if (Minecraft.player.fallDistance > 0.0f) {
                        return true;
                    }
                }
            }
            final Minecraft mc15 = KillAura.mc;
            if (!Minecraft.player.onGround) {
                final Minecraft mc16 = KillAura.mc;
                if (Minecraft.player.fallDistance > ((this.randomClicks.get() && this.rotateMode.is("Vulcan")) ? randomizeFloat(0.0f, 0.3f) : 0.0f)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public static boolean reason(final boolean water) {
        boolean b = false;
        Label_0125: {
            if (water) {
                final WorldClient world = KillAura.mc.world;
                final Minecraft mc = KillAura.mc;
                final double posX = Minecraft.player.posX;
                final Minecraft mc2 = KillAura.mc;
                final double posY = Minecraft.player.posY;
                final Minecraft mc3 = KillAura.mc;
                if (world.getBlockState(new BlockPos(posX, posY, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid) {
                    final WorldClient world2 = KillAura.mc.world;
                    final Minecraft mc4 = KillAura.mc;
                    final double posX2 = Minecraft.player.posX;
                    final Minecraft mc5 = KillAura.mc;
                    final double y = Minecraft.player.posY + 1.0;
                    final Minecraft mc6 = KillAura.mc;
                    if (world2.getBlockState(new BlockPos(posX2, y, Minecraft.player.posZ)).getBlock() instanceof BlockAir) {
                        b = true;
                        break Label_0125;
                    }
                }
            }
            b = false;
        }
        final boolean critWater = b;
        final Minecraft mc7 = KillAura.mc;
        if (!Minecraft.player.isPotionActive(MobEffects.BLINDNESS)) {
            final Minecraft mc8 = KillAura.mc;
            if (!Minecraft.player.isOnLadder()) {
                final Minecraft mc9 = KillAura.mc;
                if (!Minecraft.player.isInWater() || critWater) {
                    final Minecraft mc10 = KillAura.mc;
                    if (!Minecraft.player.isInWeb) {
                        final Minecraft mc11 = KillAura.mc;
                        if (!Minecraft.player.capabilities.isFlying) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public static float randomizeFloat(final float min, final float max) {
        return (float)(Math.random() * (max - min)) + min;
    }
    
    public final void rotation(final EntityLivingBase base, final boolean attack) {
        this.rotatedBefore = true;
        Vec3d bestHitbox = this.getVector(base, this.rotateRange.getFloatValue() + this.range.getFloatValue());
        if (bestHitbox == null) {
            bestHitbox = base.getPositionEyes(1.0f);
        }
        Minecraft.getMinecraft();
        final EntityPlayerSP client = Minecraft.player;
        final float sensitivity = 1.0001f;
        final double x = bestHitbox.x - client.posX;
        final double y = bestHitbox.y - client.getPositionEyes(1.0f).y;
        final double z = bestHitbox.z - client.posZ;
        final double dst = Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));
        final float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0);
        final float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(y, dst)));
        float yawDelta = MathHelper.wrapDegrees(yawToTarget - this.rotation.x) / sensitivity;
        final float pitchDelta = (pitchToTarget - this.rotation.y) / sensitivity;
        if (yawDelta > 180.0f) {
            yawDelta -= 180.0f;
        }
        int yawDeltaAbs = (int)Math.abs(yawDelta);
        if (yawDeltaAbs < 180.0) {
            if (this.rotateMode.is("Advanced")) {
                final float pitchDeltaAbs = Math.abs(pitchDelta);
                float additionYaw = Math.min(Math.max((float)yawDeltaAbs, 1.0f), 80.0f);
                final float additionPitch = Math.max(attack ? pitchDeltaAbs : 1.0f, 2.0f);
                if (Math.abs(additionYaw - this.prevAdditionYaw) <= 3.0f) {
                    additionYaw = getSensitivity(this.prevAdditionYaw + 3.1f);
                }
                final float newYaw = this.rotation.x + ((yawDelta > 0.0f) ? additionYaw : (-additionYaw)) * sensitivity;
                final float newPitch = MathHelper.clamp(this.rotation.y + ((pitchDelta > 0.0f) ? additionPitch : (-additionPitch)) * sensitivity, -90.0f, 90.0f);
                this.rotation.x = newYaw;
                this.rotation.y = newPitch;
                this.prevAdditionYaw = additionYaw;
            }
            if (this.rotateMode.is("Vulcan") && attack) {
                final int pitchDeltaAbs2 = (int)Math.abs(pitchDelta);
                if (Math.abs(yawDeltaAbs - this.prevAdditionYaw) <= 3.0f) {
                    yawDeltaAbs = (int)GCDFixUtility.getFixedRotation(this.prevAdditionYaw + 3.1f);
                }
                final float newYaw2 = this.rotation.x + ((yawDelta > 0.0f) ? yawDeltaAbs : (-yawDeltaAbs)) * sensitivity;
                final float newPitch2 = MathHelper.clamp(this.rotation.y + ((pitchDelta > 0.0f) ? pitchDeltaAbs2 : (-pitchDeltaAbs2)) * sensitivity, -90.0f, 90.0f);
                this.rotation.x = newYaw2;
                this.rotation.y = newPitch2;
                this.prevAdditionYaw = (float)yawDeltaAbs;
            }
        }
        else {
            final Vector2f rotation = this.rotation;
            final Minecraft mc = KillAura.mc;
            rotation.x = Minecraft.player.rotationYaw;
            final Vector2f rotation2 = this.rotation;
            final Minecraft mc2 = KillAura.mc;
            rotation2.y = Minecraft.player.rotationPitch;
        }
    }
    
    public static float getSensitivity(final float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }
    
    public static float getGCDValue() {
        return (float)(getGCD() * 0.15);
    }
    
    public static float getGCD() {
        final float f1;
        return (f1 = (float)(KillAura.mc.gameSettings.mouseSensitivity * 0.6 + 0.2)) * f1 * f1 * 8.0f;
    }
    
    public static float getDeltaMouse(final float delta) {
        return (float)Math.round(delta / getGCDValue());
    }
    
    public EntityLivingBase findTarget() {
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity : KillAura.mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase && this.isValid((EntityLivingBase)entity)) {
                targets.add((EntityLivingBase)entity);
            }
        }
        final Minecraft mc;
        final int dst1;
        final Minecraft mc2;
        final int dst2;
        targets.sort((e1, e2) -> {
            mc = KillAura.mc;
            dst1 = (int)(Minecraft.player.getDistance(e1) * 1000.0f);
            mc2 = KillAura.mc;
            dst2 = (int)(Minecraft.player.getDistance(e2) * 1000.0f);
            return dst1 - dst2;
        });
        return targets.isEmpty() ? null : targets.get(0);
    }
    
    public Vec3d getVector(final Entity target, final double rotateDistance) {
        if (target.getDistanceSqToEntity(target) >= 36.0) {
            return null;
        }
        final Vec3d positionVector = target.getPositionVector();
        final double xIn = 0.0;
        final float eyeHeight = target.getEyeHeight();
        final Minecraft mc = KillAura.mc;
        final double num = eyeHeight * (Minecraft.player.getDistance(target) / (this.range.getFloatValue() + this.rotateRange.getFloatValue()) + target.width);
        final double min = 0.2;
        final Minecraft mc2 = KillAura.mc;
        final Vec3d vec = positionVector.add(new Vec3d(xIn, MathHelper.clamp(num, min, Minecraft.player.getEyeHeight()), 0.0));
        final ArrayList<Vec3d> points = new ArrayList<Vec3d>();
        points.add(vec);
        points.removeIf(point -> !isHitBoxVisible(target, point, rotateDistance));
        if (points.isEmpty()) {
            return null;
        }
        final Vector2f r1;
        final Vector2f r2;
        final float y1;
        final float y2;
        points.sort((d1, d2) -> {
            r1 = RotationUtility.getDeltaForCoord(this.rotation, d1);
            r2 = RotationUtility.getDeltaForCoord(this.rotation, d2);
            y1 = Math.abs(r1.y);
            y2 = Math.abs(r2.y);
            return (int)((y1 - y2) * 1000.0f);
        });
        return points.get(0);
    }
    
    public static float getAngle(final EntityLivingBase entity) {
        final double posX = entity.posX;
        final Minecraft mc = KillAura.mc;
        final double diffX = posX - Minecraft.player.posX;
        final double posZ = entity.posZ;
        final Minecraft mc2 = KillAura.mc;
        final double diffZ = posZ - Minecraft.player.posZ;
        final double n = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0;
        final Minecraft mc3 = KillAura.mc;
        return (float)Math.abs(MathHelper.wrapDegrees(n - Minecraft.player.rotationYaw));
    }
    
    public static boolean checkIfVisible(final float angleX, final float angleY, final float hitboxSize, final Entity target, final double distance) {
        final Minecraft mc = KillAura.mc;
        final Vec3d playerPos = Minecraft.player.getPositionEyes(1.0f);
        final Minecraft mc2 = KillAura.mc;
        final EntityPlayerSP player = Minecraft.player;
        final Vec3d lookDirection = Entity.getVectorForRotation(angleY, angleX);
        final Vec3d reach = playerPos.add(lookDirection.scale(distance));
        final RayTraceResult result = KillAura.mc.world.rayTraceBlocks(playerPos, reach, false, false, true);
        return result != null && target.getEntityBoundingBox().expandXyz(hitboxSize).calculateIntercept(playerPos, reach) != null;
    }
    
    public static Vector2f getVectorForCoord(final Vector2f rot, final Vec3d point) {
        Minecraft.getMinecraft();
        final EntityPlayerSP client = Minecraft.player;
        final double x = point.x - client.posX;
        final double y = point.y - client.getPositionEyes(1.0f).y;
        final double z = point.z - client.posZ;
        final double dst = Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));
        final float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0);
        final float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(y, dst)));
        final float yawDelta = MathHelper.wrapDegrees(yawToTarget - rot.x);
        final float pitchDelta = pitchToTarget - rot.y;
        return new Vector2f(yawDelta, pitchDelta);
    }
    
    public static Vector2f getRotationForCoord(final Vec3d point) {
        Minecraft.getMinecraft();
        final EntityPlayerSP client = Minecraft.player;
        final double x = point.x - client.posX;
        final double y = point.y - client.getPositionEyes(1.0f).y;
        final double z = point.z - client.posZ;
        final double dst = Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));
        final float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0);
        final float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(y, dst)));
        return new Vector2f(yawToTarget, pitchToTarget);
    }
    
    public static Entity getPointedEntity(final Vector2f rot, final double dst, final float scale, final boolean walls, final Entity target) {
        final Minecraft mc = KillAura.mc;
        final Entity entity = Minecraft.player;
        final double d0 = dst;
        RayTraceResult objectMouseOver = rayTrace(d0, rot.x, rot.y, walls);
        final Vec3d vec3d = entity.getPositionEyes(1.0f);
        final boolean flag = false;
        double d2 = d0;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3d);
        }
        final Vec3d vec3d2 = getLook(rot.x, rot.y);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0);
        Entity pointedEntity = null;
        Vec3d vec3d4 = null;
        double d3 = d2;
        final Entity entity2 = target;
        final float widthPrev = entity2.width;
        final float heightPrev = entity2.height;
        final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expandXyz(entity2.getCollisionBorderSize());
        final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d3);
        if (axisalignedbb.isVecInside(vec3d)) {
            if (d3 >= 0.0) {
                pointedEntity = entity2;
                vec3d4 = ((raytraceresult == null) ? vec3d : raytraceresult.hitVec);
                d3 = 0.0;
            }
        }
        else if (raytraceresult != null) {
            final double d4 = vec3d.distanceTo(raytraceresult.hitVec);
            if (d4 < d3 || d3 == 0.0) {
                final boolean flag2 = false;
                if (!flag2 && entity2.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                    if (d3 == 0.0) {
                        pointedEntity = entity2;
                        vec3d4 = raytraceresult.hitVec;
                    }
                }
                else {
                    pointedEntity = entity2;
                    vec3d4 = raytraceresult.hitVec;
                    d3 = d4;
                }
            }
        }
        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d4) > dst) {
            pointedEntity = null;
            objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new RayTraceResult(pointedEntity, vec3d4);
        }
        return (objectMouseOver != null) ? ((objectMouseOver.entityHit instanceof Entity) ? objectMouseOver.entityHit : null) : null;
    }
    
    public static RayTraceResult rayTrace(final double blockReachDistance, final float yaw, final float pitch, final boolean walls) {
        if (!walls) {
            return null;
        }
        final Minecraft mc = KillAura.mc;
        final Vec3d vec3d = Minecraft.player.getPositionEyes(1.0f);
        final Vec3d vec3d2 = getLook(yaw, pitch);
        final Vec3d vec3d3 = vec3d.add(vec3d2.x * blockReachDistance, vec3d2.y * blockReachDistance, vec3d2.z * blockReachDistance);
        return KillAura.mc.world.rayTraceBlocks(vec3d, vec3d3, true, true, true);
    }
    
    static Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3d(f2 * f3, f4, f * f3);
    }
    
    static Vec3d getLook(final float yaw, final float pitch) {
        return getVectorForRotation(pitch, yaw);
    }
    
    public static boolean isHitBoxVisible(final Entity target, final Vec3d vector, final double dst) {
        return getPointedEntity(getRotationForCoord(vector), dst, 1.0f, !entityBehindWall(), target) == target;
    }
    
    public static boolean isHitBoxVisible(final Vec3d vec3d) {
        final Minecraft mc = KillAura.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc2 = KillAura.mc;
        final double minY = Minecraft.player.getEntityBoundingBox().minY;
        final Minecraft mc3 = KillAura.mc;
        final double yIn = minY + Minecraft.player.getEyeHeight();
        final Minecraft mc4 = KillAura.mc;
        final Vec3d eyesPos = new Vec3d(posX, yIn, Minecraft.player.posZ);
        return KillAura.mc.world.rayTraceBlocks(eyesPos, vec3d, false, true, false) == null;
    }
    
    public static boolean entityBehindWall() {
        final Minecraft mc = KillAura.mc;
        final double lastReportedPosX = Minecraft.player.lastReportedPosX;
        final Minecraft mc2 = KillAura.mc;
        final double lastReportedPosY = Minecraft.player.lastReportedPosY;
        final Minecraft mc3 = KillAura.mc;
        final BlockPos pos = new BlockPos(lastReportedPosX, lastReportedPosY, Minecraft.player.lastReportedPosZ);
        return KillAura.mc.world.getBlockState(pos).getMaterial() == Material.AIR;
    }
    
    public boolean isValid(final EntityLivingBase base) {
        final Minecraft mc = KillAura.mc;
        if (base == Minecraft.player || base.isDead || base.getHealth() <= 0.0f) {
            return false;
        }
        final Vec3d positionVector = base.getPositionVector();
        final Minecraft mc2 = KillAura.mc;
        if (positionVector.distanceTo(Minecraft.player.getPositionEyes(1.0f)) > 6.0f + this.rotateRange.getFloatValue()) {
            return false;
        }
        if (base instanceof EntityPlayer && base.getTotalArmorValue() == 0 && !this.targetsSelection.get(1)) {
            return false;
        }
        if ((base instanceof EntityPlayer || base.isInvisible()) && !this.targetsSelection.get(0)) {
            return false;
        }
        if (this.onlyWeapon.get()) {
            final Minecraft mc3 = KillAura.mc;
            if (!(Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                final Minecraft mc4 = KillAura.mc;
                if (!(Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemAxe)) {
                    return false;
                }
            }
        }
        final String name = base.getName();
        final Minecraft mc5 = KillAura.mc;
        return !name.equals(Minecraft.player.getName()) && !Minced.getInstance().friendManager.isFriend(base.getName()) && ((!(base instanceof EntityMob) && !(base instanceof EntityAnimal) && !(base instanceof EntityGolem) && !(base instanceof EntitySlime) && !(base instanceof EntitySquid) && !(base instanceof EntityVillager)) || this.targetsSelection.get(3)) && !(base instanceof EntityArmorStand);
    }
    
    @Override
    public void onDisable() {
        final Minecraft mc = KillAura.mc;
        if (Minecraft.player == null) {
            return;
        }
        final Minecraft mc2 = KillAura.mc;
        this.Yaw = Minecraft.player.rotationYaw;
        final Minecraft mc3 = KillAura.mc;
        this.Pitch = Minecraft.player.rotationPitch;
        final Vector2f rotation = this.rotation;
        final Minecraft mc4 = KillAura.mc;
        rotation.x = Minecraft.player.rotationYaw;
        final Vector2f rotation2 = this.rotation;
        final Minecraft mc5 = KillAura.mc;
        rotation2.y = Minecraft.player.rotationPitch;
        KillAura.target = null;
        super.onDisable();
    }
    
    public float[] Rotation(final Entity e) {
        final AxisAlignedBB aabb = e.getRenderBoundingBox();
        final double eX = aabb.minX + (aabb.maxX - aabb.minX) / 2.0;
        final double eY = aabb.minY;
        final double eZ = aabb.minZ + (aabb.maxZ - aabb.minZ) / 2.0;
        final double n = eX;
        final Minecraft mc = KillAura.mc;
        final double x = n - Minecraft.player.posX;
        final double n2 = eZ;
        final Minecraft mc2 = KillAura.mc;
        final double z = n2 - Minecraft.player.posZ;
        final EntityLivingBase ent = (EntityLivingBase)e;
        final double n3;
        final double y = n3 = eY + ent.getEyeHeight() * 0.6666667f;
        final Minecraft mc3 = KillAura.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc4 = KillAura.mc;
        final double dy = n3 - (posY + Minecraft.player.getEyeHeight());
        final double dstxz = MathHelper.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793 - 92.0);
        float pitch = (float)Math.toDegrees(-Math.atan2(dy, dstxz));
        yaw = this.Yaw + getSensitivity(MathHelper.wrapDegrees(yaw - this.Yaw));
        pitch = this.Pitch + getSensitivity(MathHelper.wrapDegrees(pitch - this.Pitch));
        pitch = MathHelper.clamp(pitch, -88.5f, 89.9f);
        return new float[] { yaw, pitch };
    }
    
    public float[] getNeededFacing2(final Entity target, final boolean randomizer) {
        final float yaw = this.Rotation(target)[0];
        final float pitch = this.Rotation(target)[1];
        final Minecraft mc = KillAura.mc;
        final int speed = (Minecraft.player.ticksExisted % 2 == 0) ? 20 : 1;
        if (MoveUtility.isMoving() || getDifferenceOf(this.Yaw, yaw) > 10.0) {
            this.Yaw = MathUtility.lerp(this.Yaw, yaw, 0.05f * speed);
            this.Pitch = MathUtility.lerp(this.Pitch, pitch, 0.05f * speed);
            this.Pitch = MathUtility.clamp(this.Pitch, -90.0f, 90.0f);
            this.Yaw += getSensitivity(MathHelper.wrapDegrees(this.Rotation(target)[0] - this.Yaw));
            this.Pitch += getSensitivity(MathHelper.wrapDegrees(this.Rotation(target)[1] - this.Pitch));
        }
        return new float[] { this.Yaw, this.Pitch };
    }
    
    public static double getDifferenceOf(final float num1, final float num2) {
        return (Math.abs(num2 - num1) > Math.abs(num1 - num2)) ? Math.abs(num1 - num2) : ((double)Math.abs(num2 - num1));
    }
    
    public enum Hitbox
    {
        HEAD, 
        CHEST, 
        LEGS;
    }
}
