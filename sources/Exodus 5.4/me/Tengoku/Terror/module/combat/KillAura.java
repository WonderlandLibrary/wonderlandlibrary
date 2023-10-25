/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package me.Tengoku.Terror.module.combat;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import de.Hero.settings.Setting;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.impl.Teams;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.Event3D;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventPostMotionUpdate;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.event.events.EventUseEntity;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.render.HeadRotations;
import me.Tengoku.Terror.module.world.Scaffold;
import me.Tengoku.Terror.util.RotationUtil;
import me.Tengoku.Terror.util.RotationUtils;
import me.Tengoku.Terror.util.Timer;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class KillAura
extends Module {
    private long last;
    private boolean others;
    private float partialTicks;
    public static EntityLivingBase target;
    public float serverYaw;
    public float serverPitch;
    TimerUtils timer;
    FontRenderer fr;
    private float yaw;
    private long current;
    ArrayList<Entity> entities = new ArrayList();
    ItemRenderer renderItem;
    private float pitch;
    Timer timer2;
    private Random randomGenerator;
    Timer blockTimer;
    private int delay = 8;
    Timer timer3;

    private EntityLivingBase getClosest(double d) {
        double d2 = d;
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("KillAura Mode").getValString();
        EntityLivingBase entityLivingBase = null;
        for (Object e : Minecraft.theWorld.loadedEntityList) {
            EntityLivingBase entityLivingBase2;
            Entity entity = (Entity)e;
            if (!(entity instanceof EntityLivingBase) || !this.canAttack(entityLivingBase2 = (EntityLivingBase)entity)) continue;
            double d3 = Minecraft.thePlayer.getDistanceToEntity(entityLivingBase2);
            if (!(d3 <= d2)) continue;
            d2 = d3;
            entityLivingBase = entityLivingBase2;
        }
        return entityLivingBase;
    }

    private float getAngleDifference(float f, float f2) {
        float f3 = Math.abs(f2 - f) % 360.0f;
        float f4 = f3 > 180.0f ? 360.0f - f3 : f3;
        return f4;
    }

    public double getTargetY() {
        return KillAura.target.posY;
    }

    public EntityLivingBase getTarget() {
        return target;
    }

    private void resetTime() {
        this.last = System.nanoTime() / 1000000L;
    }

    public static synchronized void BlocksMCRotations(EntityLivingBase entityLivingBase) {
        float[] fArray = KillAura.faceEntity(entityLivingBase);
        if (fArray != null) {
            Minecraft.getMinecraft();
            Minecraft.thePlayer.rotationYaw = fArray[0];
            Minecraft.getMinecraft();
            Minecraft.thePlayer.rotationPitch = fArray[1] + 1.0f;
        }
    }

    public float[] getCurrentRotation(float f, float f2) {
        return new float[]{f, f2};
    }

    @EventTarget
    public void onHit(EventUseEntity eventUseEntity) {
        Entity entity = eventUseEntity.getEntity();
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("Block Mode", this).getValString();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        C03PacketPlayer c03PacketPlayer;
        if (Minecraft.thePlayer == null) {
            return;
        }
        if (eventPacket.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            c03PacketPlayer = (C03PacketPlayer)eventPacket.getPacket();
            this.serverYaw = c03PacketPlayer.getYaw();
            this.serverPitch = c03PacketPlayer.getPitch();
        }
        if (eventPacket.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            c03PacketPlayer = (C03PacketPlayer)eventPacket.getPacket();
            this.serverYaw = c03PacketPlayer.getYaw();
            this.serverPitch = c03PacketPlayer.getPitch();
        }
    }

    public float[] fixSensitivity(float[] fArray) {
        float f = Minecraft.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float f2 = f * f * f * 1.2f;
        return new float[]{fArray[0] - fArray[0] % f2, fArray[1] - fArray[1] % f2};
    }

    public float[] doRotation(Entity entity) {
        double d = entity.posX - Minecraft.thePlayer.posX;
        double d2 = entity.posY - Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - 1.5;
        double d3 = entity.posZ - Minecraft.thePlayer.posZ;
        double d4 = MathHelper.sqrt_double(d * d + d3 * d3);
        float f = (float)(MathHelper.func_181159_b(d3, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(MathHelper.func_181159_b(d2, d4) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    public static synchronized float[] faceEntity(Entity entity) {
        double d = entity.posX - Minecraft.thePlayer.posX;
        double d2 = entity.posZ - Minecraft.thePlayer.posZ;
        double d3 = entity.posY - Minecraft.thePlayer.posY + (double)entity.height / 1.5 - (double)1.6f;
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d3, d4) * 180.0 / Math.PI));
        Minecraft.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(f2 - Minecraft.thePlayer.rotationPitch);
        Minecraft.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(f - Minecraft.thePlayer.rotationYaw);
        return null;
    }

    public void setTarget(EntityLivingBase entityLivingBase) {
        target = entityLivingBase;
    }

    @EventTarget
    public void onRender(Event3D event3D) {
        this.partialTicks = event3D.getPartialTicks();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (Exodus.INSTANCE.settingsManager.getSettingByName("Perspective").getValBoolean()) {
            Minecraft.gameSettings.thirdPersonView = 0;
        }
    }

    public boolean isFacedAndCanAttack(double d, float f, float f2, Entity entity) {
        if (Minecraft.thePlayer.serverSideYaw == f) {
            if (Minecraft.thePlayer.serverSidePitch == f2 && !(d > 6.0) && entity != null) {
                this.isFaced(entity, d);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (Exodus.INSTANCE.moduleManager.getModuleByClass(Scaffold.class).isToggled()) {
            Exodus.INSTANCE.moduleManager.getModuleByClass(Scaffold.class).toggle();
        }
        if (Exodus.INSTANCE.settingsManager.getSettingByName("Perspective").getValBoolean()) {
            Minecraft.gameSettings.thirdPersonView = 1;
        }
    }

    private boolean canAttack(EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayer || entityLivingBase instanceof EntityAnimal || entityLivingBase instanceof EntityMob || entityLivingBase instanceof EntityVillager) {
            if (entityLivingBase instanceof EntityPlayer && !Exodus.INSTANCE.settingsManager.getSettingByName("Players").getValBoolean()) {
                return false;
            }
            if (entityLivingBase instanceof EntityAnimal && !Exodus.INSTANCE.settingsManager.getSettingByName("Animals").getValBoolean()) {
                return false;
            }
            if (entityLivingBase instanceof EntityMob && !Exodus.INSTANCE.settingsManager.getSettingByName("Monsters").getValBoolean()) {
                return false;
            }
            if (entityLivingBase instanceof EntityVillager && !Exodus.INSTANCE.settingsManager.getSettingByName("Villagers").getValBoolean()) {
                return false;
            }
        }
        if (entityLivingBase.isOnSameTeam(Minecraft.thePlayer) && Exodus.INSTANCE.settingsManager.getSettingByName("Teams").getValBoolean()) {
            return false;
        }
        Teams cfr_ignored_0 = Exodus.commandManager.teams;
        if (!Teams.teamplayers.isEmpty()) {
            Teams cfr_ignored_1 = Exodus.commandManager.teams;
            if (Teams.teamplayers.contains(entityLivingBase.getName().toString()) && Exodus.INSTANCE.settingsManager.getSettingByModule("Advanced Teams", this).getValBoolean()) {
                return false;
            }
        }
        if (entityLivingBase.isInvisible() && !Exodus.INSTANCE.settingsManager.getSettingByName("Invisibles").getValBoolean()) {
            return false;
        }
        if (!this.isInFOV(entityLivingBase, Exodus.INSTANCE.settingsManager.getSettingByName("FOV").getValDouble())) {
            return false;
        }
        return entityLivingBase != Minecraft.thePlayer && entityLivingBase.isEntityAlive() && (double)entityLivingBase.ticksExisted > 30.0;
    }

    public Entity raycastEntity(double d, IEntityFilter iEntityFilter) {
        return this.raycastEntity(d, this.serverYaw, this.serverPitch, iEntityFilter);
    }

    public double getTargetZ() {
        return KillAura.target.posZ;
    }

    private void updateTime() {
        this.current = System.nanoTime() / 1000000L;
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate eventPostMotionUpdate) {
        if (target == null) {
            return;
        }
    }

    public boolean isFaced(Entity entity, double d) {
        return this.raycastEntity(d, entity2 -> entity2 == entity) != null;
    }

    private boolean isInFOV(EntityLivingBase entityLivingBase, double d) {
        double d2 = this.getAngleDifference(Minecraft.thePlayer.rotationYaw, this.getRotations(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ)[0]);
        return d2 > 0.0 && d2 < (d *= 0.5) || -d < d2 && d2 < 0.0;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public void onPre(EventMotion var1_1) {
        var2_2 = Exodus.INSTANCE.settingsManager.getSettingByName("Block Mode").getValString();
        var3_3 = Exodus.INSTANCE.settingsManager.getSettingByName("Reach").getValDouble();
        var5_4 = Exodus.INSTANCE.settingsManager.getSettingByName("Smooth Rotations").getValBoolean();
        var6_5 = Exodus.INSTANCE.settingsManager.getSettingByName("Smooth Rotation Speed").getValDouble();
        var8_6 = Exodus.INSTANCE.settingsManager.getSettingByName("NoSprint").getValBoolean();
        if (KillAura.target == null || !Exodus.INSTANCE.settingsManager.getSettingByName("AutoBlock").getValBoolean()) ** GOTO lbl-1000
        if (Minecraft.thePlayer.getHeldItem() == null) ** GOTO lbl-1000
        if (Minecraft.thePlayer.getHeldItem().getItem() == null) ** GOTO lbl-1000
        if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            v0 = true;
        } else lbl-1000:
        // 4 sources

        {
            v0 = false;
        }
        var9_7 = v0;
        KillAura.target = this.getClosest(var3_3);
        if (KillAura.target == null) {
            return;
        }
        this.updateTime();
        if (var8_6) {
            Minecraft.thePlayer.setSprinting(false);
        }
        if ((var10_8 = Exodus.INSTANCE.settingsManager.getSettingByName("KA Rotation Mode").getValString()).equalsIgnoreCase("Watchdog")) {
            var11_9 = RotationUtil.getRotationsEntity(KillAura.target);
            Minecraft.thePlayer.setServerAngles(var11_9[0], var11_9[1]);
            var12_11 = EnchantmentHelper.func_152377_a(Minecraft.thePlayer.inventory.getCurrentItem(), KillAura.target.getCreatureAttribute());
            if (var12_11 > 0.0f) {
                Minecraft.thePlayer.onEnchantmentCritical(KillAura.target);
            }
            if (!var5_4) {
                var1_1.setYaw(var11_9[0]);
                var1_1.setPitch(var11_9[1]);
            } else {
                var1_1.setYaw(this.smoothRotation(this.serverYaw, var11_9[0], (float)var6_5));
                var1_1.setPitch(this.smoothRotation(this.serverPitch, var11_9[1], (float)var6_5));
            }
            if (Exodus.INSTANCE.moduleManager.getModuleByClass(HeadRotations.class).isToggled()) {
                Minecraft.thePlayer.rotationYawHead = EventMotion.getYaw();
                Minecraft.thePlayer.renderYawOffset = EventMotion.getYaw();
                Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
            }
        }
        if (var10_8.equalsIgnoreCase("AAC")) {
            var11_9 = this.doRotation(KillAura.target);
            this.yaw = this.updateRotation(Minecraft.thePlayer.prevRotationYaw, var11_9[0], 10.0f);
            this.pitch = this.updateRotation(Minecraft.thePlayer.prevRotationPitch, var11_9[1], 10.0f);
            this.fixSensitivity(var11_9);
            if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                Minecraft.thePlayer.rotationYawHead = var11_9[0];
                Minecraft.thePlayer.renderYawOffset = var11_9[0];
                Minecraft.thePlayer.rotationPitchHead = var11_9[1];
            }
        }
        if (var10_8.equalsIgnoreCase("Smooth")) {
            var11_9 = RotationUtils.faceTarget(KillAura.target, 1000.0f, 1000.0f, false);
            var1_1.setYaw(var11_9[0]);
            var1_1.setPitch(var11_9[1]);
            if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                Minecraft.thePlayer.rotationYawHead = var11_9[0];
                Minecraft.thePlayer.renderYawOffset = var11_9[0];
                Minecraft.thePlayer.rotationPitchHead = var11_9[1];
            }
            if (var5_4) {
                var1_1.setYaw(this.smoothRotation(this.serverYaw, var11_9[0], (float)var6_5));
                var1_1.setPitch(this.smoothRotation(this.serverPitch, var11_9[1], (float)var6_5));
            } else {
                var1_1.setYaw(var11_9[0]);
                var1_1.setPitch(var11_9[1]);
            }
        }
        if (var9_7) {
            if (KillAura.target.getDistanceToEntity(Minecraft.thePlayer) < 8.0f && var2_2.equalsIgnoreCase("AAC")) {
                if (var9_7) {
                    var9_7 = false;
                } else {
                    var11_9 = KillAura.faceEntity(KillAura.target);
                    this.fixSensitivity(var11_9);
                }
            }
        }
        if (var2_2.equalsIgnoreCase("NCP") && var9_7) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
            Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getCurrentItem());
        }
        if (var2_2.equalsIgnoreCase("Hypixel") && var9_7 && this.timer.hasReached(10000.0)) {
            if (Minecraft.thePlayer.swingProgressInt == -1) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
            } else if ((double)Minecraft.thePlayer.swingProgressInt < 0.5) {
                if (Minecraft.thePlayer.swingProgressInt != -1) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
                }
            }
            this.timer.reset();
        }
        if (var2_2.equalsIgnoreCase("BlocksMC") && var9_7) {
            Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getCurrentItem());
        }
        if ((double)(this.current - this.last) > 1000.0 / (var11_10 = (double)((int)Exodus.INSTANCE.settingsManager.getSettingByName("APS").getValDouble()))) {
            var13_12 = Exodus.INSTANCE.settingsManager.getSettingByName("Raycast").getValBoolean();
            if (!KillAura.target.isDead && KillAura.target.getHealth() > 0.0f) {
                if (var13_12) {
                    if (this.isFaced(KillAura.target, var3_3)) {
                        this.attack(KillAura.target);
                        this.resetTime();
                    }
                } else {
                    this.attack(KillAura.target);
                    this.resetTime();
                }
            }
        }
    }

    public double getTargetX() {
        return KillAura.target.posX;
    }

    @Override
    public void setup() {
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Raycast", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Crack Size", this, 4.0, 0.0, 15.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Reach", this, 3.25, 0.0, 9.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("APS", this, 15.0, 0.0, 25.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("FOV", this, 360.0, 0.0, 360.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("AutoBlock", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Invisibles", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Players", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Animals", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Monsters", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Villagers", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Teams", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Advanced Teams", this, true));
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Single");
        ArrayList<String> arrayList2 = new ArrayList<String>();
        arrayList2.add("BlocksMC");
        arrayList2.add("NCP");
        arrayList2.add("AAC");
        arrayList2.add("Hypixel");
        arrayList2.add("Fake");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("KillAura Mode", (Module)this, "Single", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Block Mode", (Module)this, "Fake", arrayList2));
        ArrayList<String> arrayList3 = new ArrayList<String>();
        arrayList3.add("Smooth");
        arrayList3.add("Watchdog");
        arrayList3.add("None");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("KA Rotation Mode", (Module)this, "Watchdog", arrayList3));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("NoSprint", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("KeepSprint", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Perspective", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Smooth Rotations", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Smooth Rotation Speed", this, 20.0, 1.0, 20.0, true));
    }

    public KillAura() {
        super("Aura", 19, Category.COMBAT, "Automatically hits entities around you.");
        this.timer = new TimerUtils();
        this.timer2 = new Timer();
        this.timer3 = new Timer();
        this.fr = Minecraft.fontRendererObj;
        this.blockTimer = new Timer();
        this.renderItem = new ItemRenderer(mc);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Block Mode").getValString();
        String string2 = Exodus.INSTANCE.settingsManager.getSettingByName("KA Rotation Mode").getValString();
        String string3 = Exodus.INSTANCE.settingsManager.getSettingByName("KillAura Mode").getValString();
        this.setDisplayName("Aura \ufffdf" + string3);
        if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
            this.toggle();
        }
        if (string.equalsIgnoreCase("BlocksMC")) {
            Minecraft.gameSettings.keyBindJump.pressed = false;
        }
    }

    private float[] getRotations(double d, double d2, double d3) {
        double d4 = d + 0.5 - Minecraft.thePlayer.posX;
        double d5 = (d2 + 0.5) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double d6 = d3 + 0.5 - Minecraft.thePlayer.posZ;
        double d7 = MathHelper.sqrt_double(d4 * d4 + d6 * d6);
        float f = (float)(Math.atan2(d6, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d5, d7) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    private void attack(Entity entity) {
        int n = 0;
        while ((double)n < Exodus.INSTANCE.settingsManager.getSettingByName("Crack Size").getValDouble()) {
            Minecraft.thePlayer.onCriticalHit(entity);
            ++n;
        }
        double d = Exodus.INSTANCE.settingsManager.getSettingByName("Reach").getValDouble();
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByName("AutoBlock").getValBoolean();
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("Block Mode", this).getValString();
        Minecraft.thePlayer.swingItem();
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
        Minecraft.thePlayer.attackTargetEntityWithCurrentItem(target);
    }

    private float smoothRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    private Entity raycastEntity(double d, float f, float f2, IEntityFilter iEntityFilter) {
        Minecraft minecraft = Minecraft.getMinecraft();
        Entity entity = minecraft.getRenderViewEntity();
        if (entity != null && Minecraft.theWorld != null) {
            double d2 = d;
            Vec3 vec3 = entity.getPositionEyes(1.0f);
            float f3 = MathHelper.cos(-f * ((float)Math.PI / 180) - (float)Math.PI);
            float f4 = MathHelper.sin(-f * ((float)Math.PI / 180) - (float)Math.PI);
            float f5 = -MathHelper.cos(-f2 * ((float)Math.PI / 180));
            float f6 = MathHelper.sin(-f2 * ((float)Math.PI / 180));
            Vec3 vec32 = new Vec3(f4 * f5, f6, f3 * f5);
            Vec3 vec33 = vec3.addVector(vec32.xCoord * d2, vec32.yCoord * d2, vec32.zCoord * d2);
            List<Entity> list = Minecraft.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec32.xCoord * d2, vec32.yCoord * d2, vec32.zCoord * d2).expand(1.0, 1.0, 1.0), (Predicate<? super Entity>)Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            Entity entity2 = null;
            for (Entity entity3 : list) {
                double d3;
                if (!iEntityFilter.canRaycast(entity3)) continue;
                float f7 = entity3.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity3.getEntityBoundingBox().expand(f7, f7, f7);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(vec3, vec33);
                if (!axisAlignedBB.isVecInside(vec3)) {
                    if (!(d2 >= 0.0)) continue;
                    entity2 = entity3;
                    d2 = 0.0;
                    continue;
                }
                if (movingObjectPosition == null || !((d3 = vec3.distanceTo(movingObjectPosition.hitVec)) < d2) && d2 != 0.0) continue;
                if (entity3 == entity.ridingEntity) {
                    if (d2 != 0.0) continue;
                    entity2 = entity3;
                    continue;
                }
                entity2 = entity3;
                d2 = d3;
            }
            return entity2;
        }
        return null;
    }

    private float updateRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public static interface IEntityFilter {
        public boolean canRaycast(Entity var1);
    }
}

