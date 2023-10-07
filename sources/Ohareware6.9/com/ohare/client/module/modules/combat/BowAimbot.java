package com.ohare.client.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.ohare.client.Client;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.GLUtil;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;

/**
 * made by oHare for oHareWare
 *
 * @since 7/23/2019
 **/
public class BowAimbot extends Module {
    private EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList();
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", false);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue passives = new BooleanValue("Passives", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private EnumValue<SortMode> sortingmode = new EnumValue("SortMode", SortMode.FOV);
    public NumberValue<Float> range = new NumberValue("Range", 70.0F, 1.0f, 150.0f, 0.1f);

    public BowAimbot() {
        super("BowAimbot", Category.COMBAT, new Color(0xF34E46).getRGB());
        setRenderlabel("Bow Aimbot");
        setDescription("Automatically aim at players while shooting a bow.");
        addValues(players, animals, mobs, passives, invisibles, range, sortingmode);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && mc.thePlayer.isUsingItem()) {
            target = getBestTarget(event.getYaw());
            if (target != null && event.isPre()) {
                final double v = 3.0F;
                final double g = 0.05F;
                final float pitch = (float) -Math.toDegrees(getLaunchAngle(target, v, g));
                if (Double.isNaN(pitch)) return;
                Vec3 pos = predictPos(target, 11);
                final double difX = pos.xCoord - mc.thePlayer.posX, difZ = pos.zCoord - mc.thePlayer.posZ;
                final float yaw = (float) (Math.atan2(difZ, difX) * 180 / Math.PI) - 90;
                event.setYaw(yaw);
                event.setPitch(pitch);
            }
        }else {
                target = null;
        }
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.theWorld == null || target == null) return;
        if (target == null) return;
        Vec3 pos = predictPos(target, 11);
        drawEntityESP(pos.xCoord - RenderManager.renderPosX, pos.yCoord + 0.55 - RenderManager.renderPosY, pos.zCoord - RenderManager.renderPosZ, 0.4, 0.5, new Color(0xE33726));
    }

    private void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.8f);
        GL11.glBlendFunc(770, 771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask(true);
        RenderUtil.BB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), 1, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }

    private static Vec3 lerp(Vec3 pos, Vec3 prev, float time) {
        double x = pos.xCoord + ((pos.xCoord - prev.xCoord) * time);
        double y = pos.yCoord + ((pos.yCoord - prev.yCoord) * time);
        double z = pos.zCoord + ((pos.zCoord - prev.zCoord) * time);
        return new Vec3(x, y, z);
    }

    public static Vec3 predictPos(Entity entity, float time) {
        return lerp(new Vec3(entity.posX, entity.posY, entity.posZ), new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ), time);
    }

    private float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
        double yDif = ((targetEntity.posY + (targetEntity.getEyeHeight() / 2)) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()));
        double xDif = (targetEntity.posX - mc.thePlayer.posX);
        double zDif = (targetEntity.posZ - mc.thePlayer.posZ);
        double xCoord = Math.sqrt((xDif * xDif) + (zDif * zDif));
        return theta(v, g, xCoord, yDif);
    }

    private float theta(double v, double g, double x, double y) {
        double yv = 2 * y * (v * v);
        double gx = g * (x * x);
        double g2 = g * (gx + yv);
        double insqrt = (v * v * v * v) - g2;
        double sqrt = Math.sqrt(insqrt);
        double numerator = (v * v) + sqrt;
        double numerator2 = (v * v) - sqrt;
        double atan1 = Math.atan2(numerator, g * x);
        double atan2 = Math.atan2(numerator2, g * x);
        return (float) Math.min(atan1, atan2);
    }

    private EntityLivingBase getBestTarget(float yaw) {
        targets.clear();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) e;
                if (isValid(ent)) {
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

    private boolean isValid(EntityLivingBase entity) {
        final double d = range.getValue();
        return !AntiBot.getBots().contains(entity) && mc.thePlayer.canEntityBeSeen(entity) && entity != null && mc.thePlayer != entity && ((entity instanceof EntityPlayer && players.getValue()) || (entity instanceof EntityAnimal && animals.isEnabled()) || ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue())) && entity.getDistanceSqToEntity(mc.thePlayer) <= d * d && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue()) && !Client.INSTANCE.getFriendManager().isFriend(entity.getName());
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

    private enum SortMode {
        FOV, DISTANCE, HEALTH, CYCLE, ARMOR
    }
}
