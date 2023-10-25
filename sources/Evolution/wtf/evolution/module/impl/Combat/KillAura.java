package wtf.evolution.module.impl.Combat;

import com.github.steveice10.mc.protocol.data.message.ChatFormat;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Vector2f;
import wtf.evolution.Main;
import wtf.evolution.editor.Drag;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.*;
import wtf.evolution.helpers.Castt;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.helpers.animation.impl.EaseBackIn;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.helpers.render.TPS;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.*;

import java.awt.*;
import java.util.ArrayList;

import static net.minecraft.util.math.MathHelper.clamp;
import static net.minecraft.util.math.MathHelper.wrapDegrees;

@ModuleInfo(name = "KillAura", type = Category.Combat)
public class KillAura extends Module {

    public SliderSetting range = new SliderSetting("Range", 3, 3, 6, 0.1f);
    public SliderSetting prerange = new SliderSetting("Pre Range", 1, 0, 6, 0.1f);
    public SliderSetting cooldown = new SliderSetting("Cool Down", 1f, 0.1f, 1, 0.01f);
    public ListSetting shieldFunc = new ListSetting("Shield", "Shield Breaker", "Better Shield", "Shield Desync");
    public BooleanSetting sync = new BooleanSetting("TPS Sync", true);
    public BooleanSetting raycast = new BooleanSetting("Ray Cast", false);
    public BooleanSetting look = new BooleanSetting("Client Look", false);

    public ListSetting test = new ListSetting("Targets", "Player", "Mobs", "Animals");

    public static BooleanSetting sprint = new BooleanSetting("Keep Sprint", false);
    public BooleanSetting crit = new BooleanSetting("Falling Criticals", true);
    public ModeSetting mode = new ModeSetting("Rotation", "Matrix", "Matrix", "Sunrise");
    public static EntityLivingBase target;
    public Vector2f rotation;

    public KillAura() {
        addSettings(range, prerange, shieldFunc, cooldown, mode, sync, crit, look, raycast, sprint, test);
    }

    public float rotYaw = 0;
    public float rotPitch = 0;

    @EventTarget
    public void onUpdate(EventMotion e) {

        setSuffix(mode.get());
        rotYaw = rotation.x;
        rotPitch = rotation.y;
        if (target != null && target.getHealth() <= 0 || target != null && target.isDead) {
            target = null;

        } else {
            if (getDistanceAura(target) >= range.get() + prerange.get())
                target = null;
            if (target != null) {

                e.setPitch(rotPitch);
                e.setYaw(rotYaw);
                if (look.get()) {
                    mc.player.rotationYaw = rotYaw;
                    mc.player.rotationPitch = rotPitch;
                }
                mc.player.rotationYawHead = rotYaw;
                mc.player.rotationPitchHead = rotPitch;
                mc.player.renderYawOffset = rotYaw;
            }
        }
    }

    public boolean mayAttack() {
        if (crit.get())
            return mc.player.fallDistance > 0 && !mc.player.isInWater() && !mc.player.isInLava();
        else
            return true;
    }

    public Counter timer = new Counter();



    @EventTarget
    public void onPacket(InteractEvent e) {
        if (target != null) {
            e.cancel();
        }
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (target != null) {
            if (e.getPacket() instanceof CPacketUseEntity) {
                CPacketUseEntity p = (CPacketUseEntity) e.getPacket();
                if (!(p.getAction() == CPacketUseEntity.Action.ATTACK)) {
                    e.cancel();
                }
            }
            if (e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock) e.getPacket();
//                if (p.getHand() == EnumHand.OFF_HAND)
//                    e.cancel();
            }

        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        findTarget();
        if (Resolver.resolveAura()) {
            resolvePlayers();
        }


        if (target != null) {
            rotate();
            if (shieldFunc.selected.contains("Shield Desync") && target.getActiveItemStack().getItem() instanceof ItemShield && timer.hasReached(wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(100, 400))) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
                timer.reset();
            }
            if (getDistanceAura(target) <= range.get()) {

                float attackDelay = 0.83f * TPS.getTPSServer() / 20.0F;
                if (mc.player.getCooledAttackStrength(sync.get() ? attackDelay : 0) >= 0.93) {
                    if (shieldFunc.selected.contains("Better Shield")) {
                        if (mc.player.isActiveItemStackBlocking()) {
                            mc.playerController.onStoppedUsingItem(mc.player);
                        }
                    }
                    if (mayAttack()) {
                        mc.player.setSprinting(false);
                        rX = mode.is("Matrix") ? (Math.sin(System.nanoTime()) / 1000000000f) / wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(80, 100) : 0;
                        rY =  mode.is("Matrix") ? (Math.cos(System.nanoTime()) / 1000000000f) / wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(80, 100) : 0;
                        rZ = -(mode.is("Matrix") ? Math.cos(System.nanoTime() / 1000000000f) / wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(80, 100) : 0);
                        rotYaw = rotation.x;
                        rotPitch = rotation.y;
                        if (shieldFunc.selected.contains("Shield Breaker")) {
                            if (target.getActiveItemStack().getItem() instanceof ItemShield && target.getItemInUseMaxCount() > 4 && target.isHandActive() && findAxe() != -1) {
                                int lastItem = mc.player.inventory.currentItem;
                                mc.player.connection.sendPacket(new CPacketHeldItemChange(findAxe()));
                                mc.player.connection.sendPacket(new CPacketUseEntity(target));
                                mc.player.connection.sendPacket(new CPacketHeldItemChange(lastItem));
                            }
                        }

                        if (raycast.get()) {
                                if (Castt.getMouseOver(target, rotYaw, rotPitch, range.get(), false) != null) {
                                    mc.playerController.attackEntity(mc.player, target);

                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                    mc.player.resetCooldown();
                                }
                            } else {
                                mc.playerController.attackEntity(mc.player, target);
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                                mc.player.resetCooldown();
                            }



                    }
                }

            }
        }
        if (Resolver.resolveAura()) {
            releaseResolver();
        }
    }

    public void resolvePlayers() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player instanceof EntityOtherPlayerMP) {
                ((EntityOtherPlayerMP) player).resolve();
            }
        }
    }

    public void releaseResolver() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player instanceof EntityOtherPlayerMP) {
                ((EntityOtherPlayerMP) player).releaseResolver();
            }
        }
    }

    public  float getFixedRotation(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }

    public float getGCDValue() {
        return (float) (getGCD() * 0.15);
    }

    public float getGCD() {
        float f1;
        return (f1 = (float) (mc.gameSettings.mouseSensitivity * 0.6 + 0.2)) * f1 * f1 * 8;
    }

    public float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }

    public int findAxe() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return -1;
    }

    public double rX = 0;
    public double rY = 0;
    public double rZ = 0;



    public Vec3d getVecTarget() {
        double[] rax = mc.player.razXZ(target);
        Vec3d vec = target.getPositionVector().add(new Vec3d(rax[0], MathHelper.clamp(target.getEyeHeight() * (mc.player.getDistance(target) / (range.get() + target.width)), 0.2, mc.player.getEyeHeight()), rax[1]));
        if (!isHitBoxVisible(vec)) {
            for (double i = target.width * 0.05; i <= target.width * 0.95; i += target.width * 0.9 / 8f) {
                for (double j = target.width * 0.05; j <= target.width * 0.95; j += target.width * 0.9 / 8f) {
                    for (double k = 0; k <= target.height; k += target.height / 8f) {
                        if (isHitBoxVisible(new Vec3d(i, k, j).add(target.getPositionVector().add(new Vec3d(-target.width / 2, 0, -target.width / 2))))) {
                            vec = new Vec3d(i, k, j).add(target.getPositionVector().add(new Vec3d(-target.width / 2, 0, -target.width / 2)));
                            break;
                        }
                    }
                }
            }
        }
        return vec;
    }



    boolean isHitBoxVisible(Vec3d vec3d) {
        final Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
        return mc.world.rayTraceBlocks(eyesPos, vec3d, false, true, false) == null;
    }

    public void rotate() {

        double xDiff = (mode.is("Sunrise") ? target.posX : getVecTarget().x) - mc.player.posX + rX + (mode.is("Sunrise") ? target.posX - target.prevPosX : 0);
        double yDiff = (mode.is("Sunrise") ? target.posY : getVecTarget().y) - mc.player.posY - mc.player.getEyeHeight() + 0.3f - (getDistanceAura(target) > 1 ? rY : 0) + (mode.is("Sunrise") ? 1 : 0) + (mode.is("Sunrise") ? target.posY - target.prevPosY : 0);
        double zDiff = (mode.is("Sunrise") ? target.posZ : getVecTarget().z) - mc.player.posZ - rZ + (mode.is("Sunrise") ? target.posZ - target.prevPosZ : 0);
        double yaw = getFixedRotation((float) (wrapDegrees(Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90)) + wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(-2, 2));
        double pitch = getFixedRotation((float) wrapDegrees(Math.toDegrees(-Math.atan2(yDiff, Math.hypot(xDiff, zDiff)))) + wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(-2, 2));
        pitch = MathHelper.clamp(pitch, -90, 90);
        rotation = new Vector2f((float) yaw, (float) pitch);
    }

    public float getDistanceAura(Entity entityIn)
    {
        float f = (float)(mc.player.posX - (entityIn.posX + mc.player.razXZ(entityIn)[0]));
        float f1 = (float)(mc.player.posY - entityIn.posY);
        float f2 = (float)(mc.player.posZ - entityIn.posZ + mc.player.razXZ(entityIn)[1]);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }


    public boolean isValidTarget(EntityLivingBase e) {
        if (e == mc.player) return false;
        if (e.isDead) return false;
        if (AntiBot.isBot.contains(e)) return false;
        if (getDistanceAura(e) > range.get() + prerange.get()) return false;
        if (Main.f.isFriend(e.getName())) return false;

        if (e instanceof EntityPlayer && test.selected.contains("Player")) return true;
        if (e instanceof EntityMob && test.selected.contains("Mobs")) return true;
        if (e instanceof IAnimals && test.selected.contains("Animals")) return true;
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            rotYaw = mc.player.rotationYaw;
            rotPitch = mc.player.rotationPitch;
        }
        target = null;
    }

    public void findTarget() {
        ArrayList<EntityLivingBase> parsedEntities = new ArrayList<>(mc.world.getEntities(EntityLivingBase.class, this::isValidTarget));
        parsedEntities.sort((e1, e2) -> e1.getHealth() > e2.getHealth() ? 1 : -1);
        target = parsedEntities.get(0);
    }


}
