/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.AveReborn.Value;
import me.AveReborn.events.EventPostMotion;
import me.AveReborn.events.EventPreMotion;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.mods.COMBAT.Antibot;
import me.AveReborn.mod.mods.MISC.Teams;
import me.AveReborn.util.PlayerUtil;
import me.AveReborn.util.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

public class Killaura
extends Mod {
    public Value<String> priority = new Value("Killaura", "Priority", 0);
    public Value<Boolean> players = new Value<Boolean>("Killaura_Players", true);
    public Value<Boolean> others = new Value<Boolean>("Killaura_Others", false);
    public Value<Boolean> invis = new Value<Boolean>("Killaura_Invisible", false);
    public Value<Boolean> raycast = new Value<Boolean>("Killaura_Raycast", false);
    public static Value<Boolean> autoblock = new Value<Boolean>("Killaura_AutoBlock", false);
    public Value<Double> fov = new Value<Double>("Killaura_Fov", 180.0, 1.0, 180.0, 1.0);
    public static Value<Double> range = new Value<Double>("Killaura_Range", 4.2, 3.5, 7.0, 0.1);
    public Value<Double> randCPS = new Value<Double>("Killaura_RandCPS", 2.0, 0.0, 6.0, 1.0);
    public Value<Double> cps = new Value<Double>("Killaura_CPS", 11.0, 1.0, 20.0, 1.0);
    public Value<Double> crack = new Value<Double>("Killaura_CrackSize", 2.0, 0.0, 10.0, 1.0);
    private List<EntityLivingBase> loaded = new CopyOnWriteArrayList<EntityLivingBase>();
    private int index;
    public EntityLivingBase target;
    public EntityLivingBase curTarget;
    public timeHelper delay = new timeHelper();
    public timeHelper switchTimer = new timeHelper();
    private float currentYaw;
    private float currentPitch;
    public static boolean shouldAction = false;

    public Killaura() {
        super("Killaura", Category.COMBAT);
        this.priority.mode.add("Angle");
        this.priority.mode.add("Range");
        this.priority.mode.add("Fov");
        this.priority.mode.add("Health");
    }

    private static int randomNumber(int max, int min) {
        return - min + (int)(Math.random() * (double)(max - (- min) + 1));
    }

    @Override
    public void onEnable() {
        this.target = null;
        this.loaded.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        shouldAction = false;
        this.loaded.clear();
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        DecimalFormat df2 = new DecimalFormat("0.00");
        this.setDisplayName(String.valueOf(df2.format(range.getValueState())));
        if (this.switchTimer.delay(200.0f)) {
            this.loaded = this.getTargets();
        }
        if (this.index >= this.loaded.size()) {
            this.index = 0;
        }
        if (this.loaded.size() > 0) {
            EntityLivingBase target;
            if (this.switchTimer.delay(200.0f)) {
                this.incrementIndex();
                this.switchTimer.reset();
            }
            if ((target = this.loaded.get(this.index)) != null) {
                if (autoblock.getValueState().booleanValue() && Minecraft.thePlayer.inventory.getCurrentItem() != null && Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.getCurrentEquippedItem(), 71999);
                }
                float[] rotations = this.getEntityRotations(target);
                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
            } else {
                shouldAction = false;
            }
        }
    }

    public float[] getEntityRotations(Entity target) {
        double xDiff = target.posX - Minecraft.thePlayer.posX;
        double yDiff = target.posY - Minecraft.thePlayer.posY;
        double zDiff = target.posZ - Minecraft.thePlayer.posZ;
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / 0.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        if (yDiff > -0.2 && yDiff < 0.2) {
            pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.CHEST.getOffset() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        } else if (yDiff > -0.2) {
            pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.FEET.getOffset() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        } else if (yDiff < 0.3) {
            pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.HEAD.getOffset() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        }
        return new float[]{yaw, pitch};
    }

    @EventTarget
    public void onPostMotion(EventPostMotion event) {
        int aps2 = this.cps.getValueState().intValue();
        int randAps = this.randCPS.getValueState().intValue();
        int crackSize = this.crack.getValueState().intValue();
        int delayValue = (20 / aps2 + Killaura.randomNumber(randAps, randAps)) * 50;
        if (this.delay.delay(delayValue) && this.loaded.size() > 0 && this.loaded.get(this.index) != null) {
            EntityLivingBase target = this.loaded.get(this.index);
            if (target != null && this.raycast.getValueState().booleanValue()) {
                shouldAction = true;
                Entity rayCast = PlayerUtil.raycast(target);
                if (rayCast != null) {
                    if (Minecraft.thePlayer.isBlocking() && autoblock.getValueState().booleanValue()) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    Minecraft.thePlayer.swingItem();
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                    this.delay.reset();
                }
            } else if (target != null && !this.raycast.getValueState().booleanValue()) {
                Minecraft.thePlayer.swingItem();
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                this.delay.reset();
            }
            int i2 = 0;
            while ((double)i2 < (double)crackSize) {
                this.mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
                this.mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT_MAGIC);
                ++i2;
            }
        }
    }

    private void incrementIndex() {
        ++this.index;
        if (this.index >= this.loaded.size()) {
            this.index = 0;
        }
    }

    private boolean validEntity(Entity entity) {
        if ((double)Minecraft.thePlayer.getDistanceToEntity(entity) <= range.getValueState() && entity.isEntityAlive()) {
            EntityPlayer lol;
            if (!this.isInFOV(entity)) {
                return false;
            }
            if (Antibot.invalid.contains(entity)) {
                return false;
            }
            if (entity instanceof EntityPlayer && this.players.getValueState().booleanValue() && (lol = (EntityPlayer)entity) != Minecraft.thePlayer) {
                if (!(Teams.isOnSameTeam(lol) || lol.isInvisible() && !this.invis.getValueState().booleanValue())) {
                    return true;
                }
                return false;
            }
            if ((entity instanceof EntityMob || entity instanceof EntityVillager) && this.others.getValueState().booleanValue()) {
                return true;
            }
            if (entity instanceof EntityAnimal && this.others.getValueState().booleanValue()) {
                return true;
            }
        }
        return false;
    }

    private void sortList(List<EntityLivingBase> weed) {
        if (this.priority.isCurrentMode("Range")) {
            weed.sort((o1, o2) -> (int)(o1.getDistanceToEntity(Minecraft.thePlayer) - o2.getDistanceToEntity(Minecraft.thePlayer)));
        }
        if (this.priority.isCurrentMode("Fov")) {
            weed.sort(Comparator.comparingDouble(o2 -> RotationUtils.getDistanceBetweenAngles(Minecraft.thePlayer.rotationPitch, RotationUtils.getRotations(o2)[0])));
        }
        if (this.priority.isCurrentMode("Angle")) {
            weed.sort((o1, o2) -> {
                float[] rot1 = RotationUtils.getRotations(o1);
                float[] rot2 = RotationUtils.getRotations(o2);
                return (int)(Minecraft.thePlayer.rotationYaw - rot1[0] - (Minecraft.thePlayer.rotationYaw - rot2[0]));
            });
        }
        if (this.priority.isCurrentMode("Health")) {
            weed.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        }
    }

    private EntityLivingBase getTarget(List<EntityLivingBase> list) {
        this.sortList(list);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private List<EntityLivingBase> getTargets() {
        ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (Entity o2 : this.mc.theWorld.getLoadedEntityList()) {
            EntityLivingBase entity;
            if (!(o2 instanceof EntityLivingBase) || !this.validEntity(entity = (EntityLivingBase)o2)) continue;
            targets.add(entity);
        }
        this.sortList(targets);
        return targets;
    }

    private boolean isInFOV(Entity entity) {
        int fov = this.fov.getValueState().intValue();
        if (Math.abs(RotationUtils.getYawChange(entity.posX, entity.posZ)) <= (float)fov && Math.abs(RotationUtils.getPitchChange(entity, entity.posY)) <= (float)fov) {
            return true;
        }
        return false;
    }

    private static enum HitLocation {
        AUTO(0.0),
        HEAD(1.0),
        CHEST(1.5),
        FEET(3.5);
        
        private double offset;

        private HitLocation(double offset, int n3, double d2) {
            this.offset = offset;
        }

        public double getOffset() {
            return this.offset;
        }
    }

    public class timeHelper {
        private long prevMS = 0L;

        public boolean delay(float milliSec) {
            if ((float)(this.getTime() - this.prevMS) >= milliSec) {
                return true;
            }
            return false;
        }

        public void reset() {
            this.prevMS = this.getTime();
        }

        public long getTime() {
            return System.nanoTime() / 1000000L;
        }

        public long getDifference() {
            return this.getTime() - this.prevMS;
        }

        public void setDifference(long difference) {
            this.prevMS = this.getTime() - difference;
        }
    }

}

