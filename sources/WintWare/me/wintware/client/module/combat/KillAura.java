/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.wintware.client.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import me.wintware.client.Main;
import me.wintware.client.clickgui.ClickGuiScreen;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.event.impl.EventRender3D;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.event.impl.MoveEvent;
import me.wintware.client.friendsystem.Friend;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.AntiBot;
import me.wintware.client.module.combat.TargetStrafe;
import me.wintware.client.ui.notification.NotificationPublisher;
import me.wintware.client.ui.notification.NotificationType;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.combat.RotationSpoofer;
import me.wintware.client.utils.combat.RotationUtil;
import me.wintware.client.utils.entity.EntityUtil;
import me.wintware.client.utils.inventory.InventoryUtil;
import me.wintware.client.utils.movement.MovementUtil;
import me.wintware.client.utils.other.MathUtils;
import me.wintware.client.utils.other.RandomUtils;
import me.wintware.client.utils.other.TimerHelper;
import me.wintware.client.utils.other.TimerUtils;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class KillAura
extends Module {
    Random random;
    public static int deltaTime;
    TimerHelper oldTimer = new TimerHelper();
    public static EntityLivingBase target;
    public static TimerUtils timer;
    public static Setting rotationPitch;
    public static Setting range;
    public static Setting fov;
    public static Setting onlyCrit;
    public static Setting rotationStrafe;
    public static Setting oldPvpSystem;
    public static Setting oldPvpSystemDelay;
    private static int test;
    private static float animtest;
    private static boolean anim;
    private double time;
    int easingHealth = 0;
    private int sword;
    List<EntityLivingBase> targets;

    public KillAura() {
        super("KillAura", Category.Combat);
        this.random = new Random();
        this.targets = new ArrayList<EntityLivingBase>();
        ArrayList<String> rotation = new ArrayList<String>();
        rotation.add("Matrix");
        rotation.add("None");
        rotation.add("Legit");
        Main.instance.setmgr.rSetting(new Setting("Rotation Mode", this, "Matrix", rotation));
        fov = new Setting("FOV", this, 360.0, 0.0, 360.0, true);
        Main.instance.setmgr.rSetting(fov);
        Main.instance.setmgr.rSetting(new Setting("HitChance", this, 100.0, 1.0, 100.0, false));
        range = new Setting("Range", this, 4.0, 3.0, 7.0, false);
        Main.instance.setmgr.rSetting(range);
        rotationPitch = new Setting("RotationPitch", this, true);
        Main.instance.setmgr.rSetting(rotationPitch);
        Main.instance.setmgr.rSetting(new Setting("Players", this, true));
        Main.instance.setmgr.rSetting(new Setting("Mobs", this, false));
        Main.instance.setmgr.rSetting(new Setting("Invisible", this, false));
        Main.instance.setmgr.rSetting(new Setting("SnapHead", this, true));
        Main.instance.setmgr.rSetting(new Setting("ESPHead", this, false));
        Main.instance.setmgr.rSetting(new Setting("Walls", this, false));
        Main.instance.setmgr.rSetting(new Setting("TargetHud", this, false));
        Main.instance.setmgr.rSetting(new Setting("ShieldBreaker", this, false));
        Main.instance.setmgr.rSetting(new Setting("ShieldPresser", this, false));
        Main.instance.setmgr.rSetting(new Setting("Stop Sprinting", this, false));
        Main.instance.setmgr.rSetting(new Setting("Weapon Only", this, false));
        Main.instance.setmgr.rSetting(new Setting("AutoResetCoolDown", this, false));
        onlyCrit = new Setting("OnlyCrits", this, false);
        Main.instance.setmgr.rSetting(onlyCrit);
        Main.instance.setmgr.rSetting(new Setting("Crits Fall Distance", this, 0.1, 0.08, 0.5, false));
        Main.instance.setmgr.rSetting(new Setting("RayCast", this, false));
        Main.instance.setmgr.rSetting(new Setting("RayCast Box", this, 0.6, 0.4, 1.5, false));
        rotationStrafe = new Setting("RotationStrafe", this, false);
        Main.instance.setmgr.rSetting(rotationStrafe);
        oldPvpSystem = new Setting("OldPvpSystem", this, false);
        Main.instance.setmgr.rSetting(oldPvpSystem);
        oldPvpSystemDelay = new Setting("OldPvpSystemDelay", this, 100.0, 1.0, 150.0, false);
        Main.instance.setmgr.rSetting(oldPvpSystemDelay);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet1 = (SPacketPlayerPosLook)event.getPacket();
            Minecraft.player.rotationYaw = packet1.getYaw();
            Minecraft.player.rotationPitch = packet1.getPitch();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Objects.requireNonNull(KillAura.target.rayTrace(Main.instance.setmgr.getSettingByName("Range").getValDouble(), 1.0f)).typeOfHit == RayTraceResult.Type.MISS) {
            Vec3d eyesVec = target.getPositionEyes(1.0f);
            Vec3d lookVec = target.getLook(1.0f);
            Vec3d pointingVec = eyesVec.addVector(lookVec.xCoord * Main.instance.setmgr.getSettingByName("Range").getValDouble(), lookVec.yCoord * Main.instance.setmgr.getSettingByName("Range").getValDouble(), lookVec.zCoord * Main.instance.setmgr.getSettingByName("Range").getValDouble());
            int border = (int)Minecraft.player.getCollisionBorderSize();
            AxisAlignedBB bb = Minecraft.player.getEntityBoundingBox().expand(border, border, border);
            bb.calculateIntercept(eyesVec, pointingVec);
        }
    }

    @EventTarget
    public void onEventPreMotion(EventPreMotionUpdate e) {
        if (Minecraft.player.isEntityAlive()) {
            if (KillAura.mc.currentScreen instanceof GuiGameOver && KillAura.target.isDead) {
                this.toggle();
                NotificationPublisher.queue(this.getName(), "toggled off", NotificationType.INFO);
                return;
            }
            if (Minecraft.player.ticksExisted <= 1) {
                this.toggle();
                NotificationPublisher.queue(this.getName(), "toggled off", NotificationType.INFO);
                return;
            }
            String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
            target = this.getClosest(Main.instance.setmgr.getSettingByName("Range").getValDouble());
            this.setSuffix(mode + ", " + MathUtils.round(range.getValFloat(), 1));
            if (target == null) {
                return;
            }
            if (target.getHealth() > 0.0f) {
                float cdValue = Main.instance.setmgr.getSettingByName("OnlyCrits").getValue() ? 0.95f : 1.0f;
                float chance = Main.instance.setmgr.getSettingByName("HitChance").getValFloat();
                if (!(Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                    if (!(Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Main.instance.setmgr.getSettingByName("Weapon Only").getValue()) {
                        return;
                    }
                }
                if (Main.instance.setmgr.getSettingByName("SnapHead").getValue()) {
                    if (Minecraft.player.getCooledAttackStrength(0.0f) > cdValue && RandomUtils.getRandomDouble(0.0, 100.0) <= (double)chance) {
                        float[] rots = RotationUtil.getMatrixRotations(target, false);
                        e.setYaw(rots[0]);
                        e.setPitch(rots[1]);
                        Minecraft.player.rotationYaw = rots[0];
                        Minecraft.player.rotationPitch = rots[1];
                    }
                }
                if (Minecraft.player.getCooledAttackStrength(0.0f) >= cdValue && RandomUtils.getRandomDouble(0.0, 100.0) <= (double)chance) {
                    double gt1;
                    double gt = Main.instance.setmgr.getSettingByName("OldPvpSystemDelay").getValDouble();
                    if (!this.oldTimer.check((float)(gt + RandomUtils.getRandomDouble(0.0, gt1 = 100.0))) && Main.instance.setmgr.getSettingByName("OldPvpSystem").getValue()) {
                        return;
                    }
                    if (!RotationSpoofer.isLookingAtEntity(target) && Main.instance.setmgr.getSettingByName("RayCast").getValue()) {
                        return;
                    }
                    if (!MovementUtil.isBlockAboveHead()) {
                        if (!(Minecraft.player.fallDistance > Main.instance.setmgr.getSettingByName("Crits Fall Distance").getValFloat())) {
                            if (!Minecraft.player.isInLiquid2() && Main.instance.setmgr.getSettingByName("OnlyCrits").getValue()) {
                                return;
                            }
                        }
                    } else if (Minecraft.player.fallDistance != 0.0f) {
                        if (!Minecraft.player.isInLiquid2() && Main.instance.setmgr.getSettingByName("OnlyCrits").getValue()) {
                            return;
                        }
                    }
                    KillAura.mc.playerController.attackEntity(Minecraft.player, target);
                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                    this.oldTimer.reset();
                }
            }
        }
    }

    @EventTarget
    public void onRotations(EventPreMotionUpdate event) {
        block21: {
            block22: {
                block24: {
                    block23: {
                        if (target == null) {
                            return;
                        }
                        if (!(target.getHealth() > 0.0f)) break block21;
                        String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
                        float[] rots = RotationUtil.getMatrixRotations(target, false);
                        if (!(Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                            if (!(Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Main.instance.setmgr.getSettingByName("Weapon Only").getValue()) {
                                return;
                            }
                        }
                        if (mode.equalsIgnoreCase("Matrix")) {
                            event.setYaw(rots[0]);
                            Minecraft.player.renderYawOffset = rots[0];
                            Minecraft.player.rotationYawHead = rots[0];
                            if (Main.instance.setmgr.getSettingByName("RotationPitch").getValue()) {
                                Minecraft.player.rotationPitchHead = rots[1];
                                event.setPitch(rots[1]);
                            }
                        }
                        if (mode.equalsIgnoreCase("Legit")) {
                            Minecraft.player.rotationYaw = rots[0];
                            if (Main.instance.setmgr.getSettingByName("RotationPitch").getValue()) {
                                Minecraft.player.rotationPitch = rots[1];
                            }
                        }
                        if (!Main.instance.setmgr.getSettingByName("ShieldPresser").getValue()) break block22;
                        if (target == null) break block23;
                        if ((double)Minecraft.player.getDistanceToEntity(target) <= Main.instance.setmgr.getSettingByName("Range").getValDouble()) break block24;
                    }
                    if (target.getHealth() < 0.0f) {
                        EntityUtil.blockByShield(false);
                        return;
                    }
                }
                if (target.getHealth() < 0.0f) {
                    EntityUtil.blockByShield(false);
                    return;
                }
                if ((double)Minecraft.player.getCooledAttackStrength(0.0f) <= 0.1) {
                    EntityUtil.blockByShield(true);
                }
                if ((double)Minecraft.player.getCooledAttackStrength(0.0f) >= 0.75) {
                    EntityUtil.blockByShield(false);
                }
            }
            if (Main.instance.setmgr.getSettingByName("ShieldBreaker").getValue()) {
                if (target == null) {
                    return;
                }
                if (target.isBlocking()) {
                    Minecraft.player.inventory.currentItem = EntityUtil.getAxeAtHotbar();
                    boolean look = RotationUtil.isLookingAtEntity(target);
                    if (look) {
                        EntityUtil.attackEntity(target, true, true);
                    }
                }
            } else if (target.getHeldItemOffhand().getItem() instanceof ItemShield) {
                Minecraft.player.inventory.currentItem = InventoryUtil.getSwordAtHotbar();
            }
        }
    }

    private EntityLivingBase getClosest(double range) {
        this.targets.clear();
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : KillAura.mc.world.loadedEntityList) {
            EntityLivingBase player;
            Entity entity = (Entity)object;
            if (!(entity instanceof EntityLivingBase) || !this.canAttack(player = (EntityLivingBase)entity)) continue;
            double currentDist = Minecraft.player.getDistanceToEntity(player);
            if (!(currentDist <= dist)) continue;
            dist = currentDist;
            target = player;
            this.targets.add(player);
        }
        return target;
    }

    public static final void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static Color setAlpha(Color color, int alpha) {
        alpha = (int)MathUtils.clamp(alpha, 0.0f, 255.0f);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static final void color(double red, double green, double blue) {
        KillAura.color(red, green, blue, 1.0);
    }

    @EventTarget
    public void onRend(EventRender3D event) {
        if (target == null) {
            return;
        }
        if (target.getHealth() > 0.0f && this.getState() && Main.instance.setmgr.getSettingByName("ESPHead").getValue()) {
            GL11.glPushMatrix();
            int color = KillAura.target.hurtResistantTime > 15 ? ColorUtils.getColor(new Color(255, 70, 70, 80)) : ColorUtils.getColor(new Color(255, 255, 255, 80));
            double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * (double)KillAura.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double y = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * (double)KillAura.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * (double)KillAura.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            double d = (double)target.getEyeHeight() + 0.35;
            double d2 = target.isSneaking() ? 0.25 : 0.0;
            double mid = 0.5;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glTranslated((x -= 0.5) + mid, (y += d - d2) + mid, (z -= 0.5) + mid);
            GL11.glRotated(-KillAura.target.rotationYaw % 360.0f, 0.0, 1.0, 0.0);
            GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            RenderUtil.glColor(color);
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    public void e(Event2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (target instanceof EntityPlayer && Main.instance.setmgr.getSettingByName("TargetHud").getValue()) {
            if (ClickGuiScreen.scaling <= 1.0) {
                // empty if block
            }
            int width = 20 + Minecraft.getMinecraft().clickguismall.getStringWidth(target.getName());
            GL11.glPushMatrix();
            GL11.glTranslated(width / 100, sr.getScaledHeight() / 100, 0.0);
            GL11.glScalef((float)(ClickGuiScreen.scaling += 0.003f), (float)ClickGuiScreen.scaling, 1.0f);
            GL11.glTranslated(sr.getScaledWidth() / 2 + 10, sr.getScaledHeight() / 2, sr.getScaledWidth() / 2 + 10);
            RenderUtil.drawSmoothRect(-10.0f, 20.0f, 2 + width, target.getTotalArmorValue() != 0 ? 56.0f : 50.0f, new Color(35, 35, 40, 230).getRGB());
            Minecraft.getMinecraft().clickguismall.drawString(target.getName(), 10.0f, 26.0f, 0xFFFFFF);
            Minecraft.getMinecraft().clickguismall.drawStringWithShadow(MathUtils.round(target.getHealth() / 2.0f, 1) + " HP", 10.0f, 35.0f, 0xFFFFFF);
            this.drawHead(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), -8, 22);
            RenderUtil.drawSmoothRect(-8.0f, 44.0f, width, 46.0f, new Color(25, 25, 35, 255).getRGB());
            this.easingHealth = (int)AnimationUtil.animation(this.easingHealth, target.getHealth() - (float)this.easingHealth, 1.0E-4f);
            this.easingHealth += (int)((double)(target.getHealth() - (float)this.easingHealth) / Math.pow(2.0, 7.0));
            if (this.easingHealth < 0 || (float)this.easingHealth > target.getMaxHealth()) {
                this.easingHealth = (int)target.getHealth();
            }
            if ((float)this.easingHealth > target.getHealth()) {
                RenderUtil.drawSmoothRect(-8.0f, 66.0f, (float)this.easingHealth / target.getMaxHealth() * (float)width, 58.0f, new Color(231, 182, 0, 255).getRGB());
            }
            if ((float)this.easingHealth < target.getHealth()) {
                RenderUtil.drawRect((float)this.easingHealth / target.getMaxHealth() * (float)width, 56.0, (float)this.easingHealth / target.getMaxHealth() * (float)width, 58.0, new Color(231, 182, 0, 255).getRGB());
            }
            RenderUtil.drawSmoothRect(-8.0f, 44.0f, target.getHealth() / target.getMaxHealth() * (float)width, 46.0f, ColorUtils.getHealthColor(target).getRGB());
            if (target.getTotalArmorValue() != 0) {
                RenderUtil.drawSmoothRect(-8.0f, 50.0f, width, 52.0f, new Color(25, 25, 35, 255).getRGB());
                RenderUtil.drawSmoothRect(-8.0f, 50.0f, target.getTotalArmorValue() / 20 * width, 52.0f, new Color(77, 128, 255, 255).getRGB());
            }
            GL11.glPopMatrix();
        }
    }

    public void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8.0f, 8.0f, 8, 8, 16, 16, 64.0f, 64.0f);
    }

    private boolean canAttack(EntityLivingBase player) {
        for (Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) continue;
            return false;
        }
        if (Main.instance.moduleManager.getModuleByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player)) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !Main.instance.setmgr.getSettingByName("Players").getValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
                return false;
            }
            if (player instanceof EntityMob && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
                return false;
            }
        }
        if (player.isInvisible() && !Main.instance.setmgr.getSettingByName("Invisible").getValue()) {
            return false;
        }
        if (!RotationUtil.canSeeEntityAtFov(player, (float)Main.instance.setmgr.getSettingByName("FOV").getValDouble()) && !KillAura.canSeeEntityAtFov(player, (float)Main.instance.setmgr.getSettingByName("FOV").getValDouble())) {
            return false;
        }
        if (!this.range(player, Main.instance.setmgr.getSettingByName("Range").getValDouble())) {
            return false;
        }
        if (!player.canEntityBeSeen(Minecraft.player)) {
            return Main.instance.setmgr.getSettingByName("Walls").getValue();
        }
        return player != Minecraft.player;
    }

    private boolean range(EntityLivingBase entity, double range) {
        Minecraft.player.getDistanceToEntity(entity);
        return (double)Minecraft.player.getDistanceToEntity(entity) <= range;
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        Minecraft.getMinecraft();
        double diffX = entityLiving.posX - Minecraft.player.posX;
        Minecraft.getMinecraft();
        double diffZ = entityLiving.posZ - Minecraft.player.posZ;
        float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double d = newYaw;
        Minecraft.getMinecraft();
        double difference = KillAura.angleDifference(d, Minecraft.player.rotationYaw);
        return difference <= (double)scope;
    }

    public boolean isPlayerValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            Collection<NetworkPlayerInfo> playerInfo = Minecraft.player.connection.getPlayerInfoMap();
            for (NetworkPlayerInfo info : playerInfo) {
                if (!info.getGameProfile().getName().matches(entity.getName())) continue;
                return true;
            }
        }
        return false;
    }

    @EventTarget
    public void onMoveFlyingEvent(MoveEvent e) {
        if (target != null) {
            if (!this.getState()) {
                return;
            }
            if (KillAura.mc.gameSettings.keyBindBack.isKeyDown()) {
                return;
            }
            if (!KillAura.mc.gameSettings.keyBindJump.isKeyDown()) {
                if (Minecraft.player.onGround && Main.instance.setmgr.getSettingByName("RotationStrafe").getValue()) {
                    if (Main.instance.moduleManager.getModuleByClass(TargetStrafe.class).getState() && target != null) {
                        if (Minecraft.player.getDistanceToEntity(target) <= TargetStrafe.range.getValFloat() && Main.instance.setmgr.getSettingByName("AutoJump").getValue()) {
                            return;
                        }
                    }
                    MovementUtil.setSpeedAt(e, (float)((double)Main.fakeYaw + MovementUtil.getPressedMoveDir() - (double)Minecraft.player.rotationYaw), MovementUtil.getMoveSpeed(e));
                }
            }
        }
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }
}

