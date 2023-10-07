package com.ohare.client.module.modules.combat;

import com.ohare.client.Client;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.MathUtils;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.block.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * made by oHare for oHareWare
 *
 * @since 5/30/2019
 **/
public class KillAura extends Module {
    public EntityLivingBase target;
    private long time;
    private List<EntityLivingBase> targets = new ArrayList();
    private TimerUtil timer = new TimerUtil();
    private TimerUtil switchtimer = new TimerUtil();
    private int switchIndex;
    private final Random random = new Random();
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.SWITCH);
    private EnumValue<AutoblockMode> autoblockmode = new EnumValue("Autoblock Mode", AutoblockMode.Hypixel);
    private EnumValue<SortMode> sortingmode = new EnumValue("SortMode", SortMode.FOV);
    private NumberValue<Integer> maxtargets = new NumberValue("MaxTargets", 3, 1, 5, 1);
    private NumberValue<Integer> speed = new NumberValue("Speed", 9, 1, 20, 1);
    private NumberValue<Float> range = new NumberValue("Range", 4.2f, 1.0f, 7.0f, 0.1f);
    private NumberValue<Float> blockrange = new NumberValue("BlockRange", 6.0f, 1.0f, 20.0f, 0.1f);
    private NumberValue<Integer> switchspeed = new NumberValue("SwitchSpeed", 300, 100, 2000, 100);
    private NumberValue<Integer> tickdelay = new NumberValue("TickDelay", 500, 100, 1000, 1);
    private NumberValue<Integer> maxangle = new NumberValue("MaxAngle", 12, 1, 50, 1);
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", false);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    public static BooleanValue autoblock = new BooleanValue("AutoBlock", true);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue godmode = new BooleanValue("GodMode", false);
    private BooleanValue teams = new BooleanValue("Teams", false);
    private BooleanValue esp = new BooleanValue("ESP", false);
    private boolean canAttack, hit, groundTicks, ablock = false;
    private float[] prevRotations = new float[2];
    private float[] serverAngles = new float[2];
    private final static double[] CRIT_OFFSETS = new double[]{0.0625, 0.0, 0.0001, 0.0};

    public KillAura() {
        super("KillAura", Category.COMBAT, new Color(243, 78, 70, 255).getRGB());
        addValues(mode, autoblockmode, sortingmode, maxtargets, speed, range, blockrange, switchspeed, tickdelay, maxangle, esp, godmode, players, animals, mobs, autoblock, teams, invisibles);
        setDescription("Auto Attacks a entity");
        setRenderlabel("Kill Aura");
    }

    private enum SortMode {
        FOV, DISTANCE, HEALTH, CYCLE, ARMOR
    }
    private enum Mode {
        SINGLE, SWITCH, SMOOTH, SAFETY
    }
    private enum AutoblockMode {
        Hypixel, Fake
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (!esp.isEnabled() || mc.theWorld == null || target == null) return;
        mc.theWorld.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (entityLivingBase == target && isValid(entityLivingBase, true) && entityLivingBase != mc.thePlayer) {
                    double x = (entityLivingBase.lastTickPosX + (entityLivingBase.posX - entityLivingBase.lastTickPosX) * event.getPartialTicks());
                    double y = entityLivingBase.lastTickPosY + (entityLivingBase.posY - entityLivingBase.lastTickPosY) * event.getPartialTicks();
                    double z = (entityLivingBase.lastTickPosZ + (entityLivingBase.posZ - entityLivingBase.lastTickPosZ) * event.getPartialTicks());
                    Color color = new Color(0x05FF00);
                    if (entityLivingBase.hurtTime > 0) {
                        color = new Color(0xFF001C);
                    }
                    RenderUtil.drawEntityESP(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ, entityLivingBase.height + 0.1, entityLivingBase.width + 0.2, color, false);
                }
            }
        });
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        Criticals criticals = (Criticals)Client.INSTANCE.getModuleManager().getModule("criticals");
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        if (AutoApple.doingStuff || AutoHeal.healing || AutoHeal.doSoup || AutoBard.stage != 0) return;
        if (target != null) {
            if (mc.thePlayer.hurtTime > 0) hit = true;
        } else hit = false;
        switch (mode.getValue()) {
            case SAFETY:
                if (event.isPre()) {
                    if (autoblock.isEnabled()) {
                        if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                            unBlock();
                        }
                    }
                    target = getBestTarget(event.getYaw());
                    if (target != null && mc.thePlayer.canEntityBeSeen(target)) {
                        if (criticals.mode.getValue().equals(Criticals.Mode.DEV) && mc.thePlayer.onGround && !isInsideBlock()) {
                            event.setY(event.getY() + 0.01);
                            event.setOnGround(false);
                        }
                        final float[] dstAngle = getRotations(target);
                        final float[] srcAngle = new float[]{serverAngles[0], serverAngles[1]};
                        serverAngles = smoothAngle(dstAngle, srcAngle);
                        event.setYaw(serverAngles[0]);
                        event.setPitch(serverAngles[1]);
                        if (timer.sleep(1000 / randomNumber(9, 13)) && getDistance(prevRotations) < 7 && mc.currentScreen == null && canAttack) {
                            final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), mc.thePlayer.getCreatureAttribute());
                            if (sharpLevel > 0) mc.thePlayer.onEnchantmentCritical(target);
                            MovingObjectPosition ray;
                            ray = rayCast(mc.thePlayer, target.posX, target.posY + target.getEyeHeight() * 0.7, target.posZ);
                            if (ray != null) {
                                Entity entityHit = ray.entityHit;
                                if (entityHit instanceof EntityLivingBase) {
                                    target = (EntityLivingBase) entityHit;
                                }
                            }
                            attack(target);
                        }
                    } else {
                        serverAngles[0] = (mc.thePlayer.rotationYaw);
                        serverAngles[1] = (mc.thePlayer.rotationPitch);
                    }
                } else if (autoblock.isEnabled() && nearbyTargets()) {
                    if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                        block();
                    }
                }
                break;
            case SMOOTH:
                if (event.isPre()) {

                    if (autoblock.isEnabled()) {
                        if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                            unBlock();
                        }
                    }
                    target = getBestTarget(event.getYaw());
                    if (target != null) {
                        if (criticals.mode.getValue().equals(Criticals.Mode.DEV) && mc.thePlayer.onGround && !isInsideBlock()) {
                            event.setY(event.getY() + 0.01);
                            event.setOnGround(false);
                        }
                        final float[] dstAngle = getRotations(target);
                        final float[] srcAngle = new float[]{serverAngles[0], serverAngles[1]};
                        serverAngles = smoothAngle(dstAngle, srcAngle);
                        event.setYaw(serverAngles[0]);
                        event.setPitch(serverAngles[1]);
                        if (godmode.isEnabled()) {
                            event.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 == 0 ? 4 : 7));
                            event.setOnGround(true);
                        }
                        if (timer.sleep(1000 / randomNumber(9, 13)) && mc.currentScreen == null && canAttack) {
                            final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), mc.thePlayer.getCreatureAttribute());
                            if (sharpLevel > 0) mc.thePlayer.onEnchantmentCritical(target);
                            if (criticals.mode.getValue().equals(Criticals.Mode.DEV2) && mc.thePlayer.onGround && !isInsideBlock()) {
                                event.setY(event.getY());
                                event.setOnGround(false);
                            }
                            mc.thePlayer.swingItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                        }
                        else {
                            if (criticals.mode.getValue().equals(Criticals.Mode.DEV2) && mc.thePlayer.onGround && !isInsideBlock() && mc.thePlayer.ticksExisted %  5 != 0) {
                                event.setY(event.getY() + 0.0153);
                                event.setOnGround(false);
                            }
                        }
                    } else {
                        serverAngles[0] = (mc.thePlayer.rotationYaw);
                        serverAngles[1] = (mc.thePlayer.rotationPitch);
                    }
                } else if (autoblock.isEnabled() && nearbyTargets()) {
                    if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                        block();
                    }
                }
                break;
            case SWITCH:
                if (event.isPre()) {
                    if (KillAura.autoblock.isEnabled()) {
                        if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                            unBlock();
                        }
                    }
                    if (targets.size() > maxtargets.getValue()) targets.clear();
                    mc.theWorld.loadedEntityList.forEach(target1 -> {
                        if (target1 instanceof EntityLivingBase) {
                            EntityLivingBase ent = (EntityLivingBase) target1;
                            if (!targets.contains(target1) && isValid(ent, false) && targets.size() < maxtargets.getValue()) {
                                targets.add(ent);
                            }
                            if (targets.contains(target1) && (!isValid(ent, false))) {
                                targets.remove(ent);
                            }
                        }
                    });
                    if (switchIndex >= targets.size()) {
                        switchIndex = 0;
                    }
                    if (!targets.isEmpty()) {
                        sortTargets(event.getYaw());
                        target = targets.get(switchIndex);
                    }
                    if (target != null) {
                        if (!isValid(target, false)) {
                            target = null;
                            return;
                        }
                        final float rotations[] = getRotations(target);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);

                    }
                } else {
                    if (target != null) {
                        if (timer.sleep(1000 / speed.getValue())) {
                            attack(target);
                        }
                    }
                    if (KillAura.autoblock.isEnabled() && nearbyTargets()) {
                        if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                            block();
                        }
                    }
                    }
                if (target != null && targets.size() > 0 && switchtimer.sleep(switchspeed.getValue())) {
                    ++switchIndex;
                }
                break;
            case SINGLE:
                if (event.isPre()) {
                    if (autoblock.isEnabled()) {
                        if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                            unBlock();
                        }
                    }
                    target = getBestTarget(event.getYaw());
                    if (target != null) {
                        if (!isValid(target, false)) target = null;
                        final float[] rots = getRotations(target);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                    }
                } else {
                    if (target != null) {
                        if (timer.sleep(1000 / speed.getValue())) {
                            attack(target);
                        }
                    }
                    if (autoblock.isEnabled() && nearbyTargets()) {
                        if (autoblockmode.getValue().equals(AutoblockMode.Hypixel)) {
                            block();
                        }
                    }
                    //if (autoblock.isEnabled() && nearbyTargets() && !fakeab.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.currentScreen == null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                    // mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                }
                break;
        }

    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        canAttack = true;
        if (event.isSending() && (event.getPacket() instanceof C03PacketPlayer)) {
            if (groundTicks) {
                event.setCanceled(true);
                groundTicks = false;
            }
        }
        if (event.isSending() && (event.getPacket() instanceof C02PacketUseEntity)) {
            if (Client.INSTANCE.getModuleManager().getModuleClass(Criticals.class).isEnabled()) {
                crit();
            }
        }
    }

    private double getArmorVal(EntityLivingBase ent) {
        if (ent instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) ent;
            double armorstrength = 0;
            for (int index = 3; index >= 0; index--) {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    armorstrength += getArmorStrength(stack);
                }
            }
            return armorstrength;
        }
        return 0;
    }

    private boolean nearbyTargets() {
        for (Object e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase && isValid((EntityLivingBase) e, true)) {
                return true;
            }
        }
        return false;
    }

    private EntityLivingBase getBestTarget(float yaw) {
        targets.clear();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) e;
                if (isValid(ent, false)) {
                    targets.add(ent);
                }
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        sortTargets(yaw);
        return targets.get(0);
    }


    public boolean isAutoBlocking() {
        return autoblock.isEnabled() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && nearbyTargets();
    }

    @Override
    public void onEnable() {
        switchIndex = 0;
        if (mc.thePlayer == null) return;
        serverAngles[0] = mc.thePlayer.rotationYaw;
        serverAngles[1] = mc.thePlayer.rotationPitch;
        hit = false;
    }

    @Override
    public void onDisable() {
        targets.clear();
        target = null;
        unBlock();
    }

    private void block() {
        if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && !ablock) {
            ablock = true;
            mc.playerController.syncCurrentPlayItem();
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
        }
    }

    private void unBlock() {
        if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && ablock) {
            ablock = false;
            mc.playerController.syncCurrentPlayItem();
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    private double yawDist(EntityLivingBase e) {
        final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.thePlayer.getPositionVector().addVector(0.0, mc.thePlayer.getEyeHeight(), 0.0));
        final double d = Math.abs(mc.thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f)) % 360.0f;
        return (d > 180.0f) ? (360.0f - d) : d;
    }

    private double yawDistCycle(EntityLivingBase e, float yaw) {
        final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.thePlayer.getPositionVector().addVector(0.0, mc.thePlayer.getEyeHeight(), 0.0));
        final double d = Math.abs(yaw - Math.atan2(difference.zCoord, difference.xCoord)) % 90.0f;
        return d;
    }

    private void sortTargets(float yaw) {
        switch (sortingmode.getValue()) {
            case DISTANCE:
                targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceSqToEntity));
                break;
            case HEALTH:
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            case FOV:
                targets.sort(Comparator.comparingDouble(this::yawDist));
                break;
            case CYCLE:
                targets.sort(Comparator.comparingDouble(player -> yawDistCycle(player, yaw)));
                break;
            case ARMOR:
                targets.sort(Comparator.comparingDouble(this::getArmorVal));
                break;
        }
    }


    private boolean isValid(EntityLivingBase entity, boolean block) {
        final double d = (block && blockrange.getValue() > range.getValue()) ? blockrange.getValue() : range.getValue();
        return !AntiBot.getBots().contains(entity) && entity.getEntityId() != -1488 && !isTeammate(entity) && entity != null && mc.thePlayer != entity && ((entity instanceof EntityPlayer && players.getValue()) || (entity instanceof EntityVillager && animals.isEnabled())  || (entity instanceof EntityAnimal && animals.isEnabled()) || ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue())) && entity.getDistanceSqToEntity(mc.thePlayer) <= d * d && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue()) && !Client.INSTANCE.getFriendManager().isFriend(entity.getName());
    }

    private void attack(final EntityLivingBase target) {
        final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
        final boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround) && (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater()) && (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
        if (sharpLevel > 0.0F) {
            mc.thePlayer.onEnchantmentCritical(target);
        }
        mc.thePlayer.swingItem();
        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }


    private void crit() {
        Criticals criticals = (Criticals)Client.INSTANCE.getModuleManager().getModule("criticals");
        if (!(MathUtils.getBlockUnderPlayer(mc.thePlayer, 0.06) instanceof BlockStairs) && !(MathUtils.getBlockUnderPlayer(mc.thePlayer, 0.06) instanceof BlockSlab) && !Client.INSTANCE.getModuleManager().getModule("speed").isEnabled()) {
            if (criticals.mode.getValue().equals(Criticals.Mode.PACKETS)) {
                double delay = 100;
                if (target.hurtResistantTime == 0) {
                    delay = 450;
                }
                if (System.currentTimeMillis() - time >= delay && mc.thePlayer.onGround && target.hurtResistantTime <= 13) {
                    double[] Value = {0.07, 0.07, 0.001, 0.001};
                    for (double offset : Value) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                    }
                    groundTicks = true;
                    time = System.currentTimeMillis();
                }
            }
        }
    }

    private boolean canCrit(boolean hypixel) {
        return !mc.gameSettings.keyBindJump.isKeyDown() && !(!hit && hypixel) && mc.thePlayer.onGround && !Client.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !Client.INSTANCE.getModuleManager().getModule("flight").isEnabled();
    }

    private float[] getRotations(EntityLivingBase ent) {
        final double x = ent.posX - mc.thePlayer.posX;
        double y = ent.posY - mc.thePlayer.posY;
        final double z = ent.posZ - mc.thePlayer.posZ;
        y /= mc.thePlayer.getDistanceToEntity(ent);
        final float yaw = (float) (-(Math.atan2(x, z) * 57.29577951308232));
        final float pitch = (float) (-(Math.asin(y) * 57.29577951308232));
        return new float[]{yaw, pitch};
    }

    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private MovingObjectPosition tracePath(final World world, final float x, final float y, final float z, final float tx, final float ty, final float tz, final float borderSize, final HashSet<Entity> excluded) {
        Vec3 startVec = new Vec3(x, y, z);
        Vec3 endVec = new Vec3(tx, ty, tz);
        final float minX = (x < tx) ? x : tx;
        final float minY = (y < ty) ? y : ty;
        final float minZ = (z < tz) ? z : tz;
        final float maxX = (x > tx) ? x : tx;
        final float maxY = (y > ty) ? y : ty;
        final float maxZ = (z > tz) ? z : tz;
        final AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
        final List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);
        MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
        startVec = new Vec3(x, y, z);
        endVec = new Vec3(tx, ty, tz);
        Entity closestHitEntity = null;
        float closestHit = Float.POSITIVE_INFINITY;
        float currentHit;
        for (final Entity ent : allEntities) {
            if (ent.canBeCollidedWith() && !excluded.contains(ent)) {
                final float entBorder = ent.getCollisionBorderSize();
                AxisAlignedBB entityBb = ent.getEntityBoundingBox();
                if (entityBb == null) {
                    continue;
                }
                entityBb = entityBb.expand(entBorder, entBorder, entBorder);
                final MovingObjectPosition intercept = entityBb.calculateIntercept(startVec, endVec);
                if (intercept == null) {
                    continue;
                }
                currentHit = (float) intercept.hitVec.distanceTo(startVec);
                if (currentHit >= closestHit && currentHit != 0.0f) {
                    continue;
                }
                closestHit = currentHit;
                closestHitEntity = ent;
            }
        }
        if (closestHitEntity != null) {
            blockHit = new MovingObjectPosition(closestHitEntity);
        }
        return blockHit;
    }

    private boolean isTeammate(EntityLivingBase entityLivingBase) {
        if (teams.isEnabled() && entityLivingBase != null) {
            String name = entityLivingBase.getDisplayName().getFormattedText();
            StringBuilder append = new StringBuilder().append("§");
            if (name.startsWith(append.append(mc.thePlayer.getDisplayName().getFormattedText().charAt(1)).toString())) {
                return true;
            }
        }
        return false;
    }

    private MovingObjectPosition tracePathD(final World w, final double posX, final double posY, final double posZ, final double v, final double v1, final double v2, final float borderSize, final HashSet<Entity> exclude) {
        return tracePath(w, (float) posX, (float) posY, (float) posZ, (float) v, (float) v1, (float) v2, borderSize, exclude);
    }

    private MovingObjectPosition rayCast(final EntityPlayerSP player, final double x, final double y, final double z) {
        final HashSet<Entity> excluded = new HashSet<>();
        excluded.add(player);
        return tracePathD(player.worldObj, player.posX, player.posY + player.getEyeHeight(), player.posZ, x, y, z, 1.0f, excluded);
    }

    private float getDistance(float[] original) {
        final float yaw = MathHelper.wrapAngleTo180_float(serverAngles[0]) - MathHelper.wrapAngleTo180_float(original[0]);
        final float pitch = MathHelper.wrapAngleTo180_float(serverAngles[1]) - MathHelper.wrapAngleTo180_float(original[1]);
        return (float) Math.sqrt(yaw * yaw + pitch * pitch);
    }

    private long randomNumber(long min, long max) {
        return (long) ((Math.random() * (max - min)) + min);
    }

    private float randomFloat(float min, float max) {
        return min + (random.nextFloat() * (max - min));
    }

    private float[] smoothAngle(float[] dst, float[] src) {
        float[] smoothedAngle = new float[2];
        smoothedAngle[0] = (src[0] - dst[0]);
        smoothedAngle[1] = (src[1] - dst[1]);
        smoothedAngle = MathUtils.constrainAngle(smoothedAngle);
        smoothedAngle[0] = (src[0] - smoothedAngle[0] / 100 * randomFloat(8, 20));
        smoothedAngle[1] = (src[1] - smoothedAngle[1] / 100 * randomFloat(0, 8));
        return smoothedAngle;
    }

    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if ((block != null) && (!(block instanceof BlockAir))) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if ((block instanceof BlockHopper)) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if ((boundingBox != null) && (mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}