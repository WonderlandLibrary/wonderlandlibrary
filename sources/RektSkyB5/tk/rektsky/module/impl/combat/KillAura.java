/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.UpdateSprintingEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.movement.Fly;
import tk.rektsky.module.impl.world.BedRekter;
import tk.rektsky.module.impl.world.Scaffold;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.module.settings.IntSetting;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.utils.Timer;
import tk.rektsky.utils.combat.RotationUtil;
import tk.rektsky.utils.entity.EntityUtil;

public class KillAura
extends Module {
    int ticks = 0;
    public ArrayList<Entity> attackedEntity = new ArrayList();
    public BooleanSetting antibot = new BooleanSetting("Antibot", true);
    public DoubleSetting rangeSetting = new DoubleSetting("Range", 2.0, 10.0, 3.2);
    public IntSetting delaySetting = new IntSetting("Delay", 1, 10, 1);
    public ListSetting modeSetting = new ListSetting("Mode", new String[]{"Closest", "AGC"}, "Closest");
    public BooleanSetting blockAnimationSetting = new BooleanSetting("BlockAnimation", true);
    public BooleanSetting blockSetting = new BooleanSetting("DoBlock", true);
    public BooleanSetting targetHudSetting = new BooleanSetting("TargetHUD", true);
    public BooleanSetting autoDisable = new BooleanSetting("AutoDisable", true);
    public BooleanSetting noFriend = new BooleanSetting("NoFriend", false);
    public boolean attackPost;
    public EntityLivingBase t = null;
    public boolean isAttacking = false;
    private boolean blocking = false;
    private Timer timer = new Timer();

    public KillAura() {
        super("KillAura", "Auto Attack Entities (Set range to 3.1 or below if it's later verus)", 0, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
        this.isAttacking = false;
        RotationUtil.setRotating("killaura", true);
        this.blocking = false;
        this.t = null;
        this.attackPost = false;
    }

    @Override
    public void onDisable() {
        if (this.blocking && this.blockSetting.getValue().booleanValue() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        this.isAttacking = false;
        RotationUtil.setRotating("killaura", false);
        this.blocking = false;
        this.t = null;
        this.attackPost = false;
    }

    @Override
    public String getSuffix() {
        return this.modeSetting.getValue();
    }

    @Subscribe
    public void onPacket(PacketReceiveEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S01PacketJoinGame && this.autoDisable.getValue().booleanValue()) {
            this.setToggled(false);
        }
    }

    @Subscribe
    public void onPacketSent(PacketSentEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C07PacketPlayerDigging && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && ((C07PacketPlayerDigging)packet).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
            event.setCanceled(true);
        }
    }

    @Subscribe
    public void onSprintingUpdate(UpdateSprintingEvent event) {
        if (this.isAttacking) {
            event.setSprinting(false);
        }
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        if (ModulesManager.getModuleByClass(Scaffold.class).isToggled()) {
            RotationUtil.setRotating("killaura", false);
            return;
        }
        if (ModulesManager.getModuleByClass(BedRekter.class).target != null) {
            RotationUtil.setRotating("killaura", false);
            return;
        }
        long l2 = System.currentTimeMillis();
        ModulesManager.getModuleByClass(Fly.class);
        if (l2 - Fly.verusFly.startTime < 500L) {
            RotationUtil.setRotating("killaura", false);
            return;
        }
        ++this.ticks;
        if (this.mc.thePlayer.getCurrentEquippedItem() == null || !(this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)) {
            this.blocking = false;
        }
        if (this.modeSetting.getValue().equals("Closest")) {
            EntityLivingBase target;
            List<EntityLivingBase> targets = EntityUtil.getEntities_old(this.antibot.getValue());
            if (!this.noFriend.getValue().booleanValue()) {
                targets = targets.stream().filter(new EntityUtil.FriendFilter()).collect(Collectors.toList());
            }
            if ((target = EntityUtil.getClosest_old(targets, this.rangeSetting.getValue())) != null) {
                RotationUtil.oldFaceBlock(target.posX, target.posY + 1.3, target.posZ);
                if (this.timer.hasTimeElapsed(70 + new Random().nextInt(50), true) && target.ticksExisted > 2) {
                    this.mc.thePlayer.swingItem();
                    this.mc.playerController.attackEntity(this.mc.thePlayer, target);
                    this.t = target;
                    this.isAttacking = true;
                    if (this.blockSetting.getValue().booleanValue() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                        this.blocking = true;
                        this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getCurrentEquippedItem()));
                    }
                    RotationUtil.setRotating("killaura", true);
                }
            } else {
                RotationUtil.setRotating("killaura", false);
                this.isAttacking = false;
                if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && this.blocking && this.mc.thePlayer.getCurrentEquippedItem() != null) {
                    this.blocking = false;
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                this.t = null;
                this.attackPost = false;
            }
        } else if (this.modeSetting.getValue().equals("AGC")) {
            List<EntityLivingBase> targets = EntityUtil.getEntities_old(this.antibot.getValue());
            if (!this.noFriend.getValue().booleanValue()) {
                targets = targets.stream().filter(new EntityUtil.FriendFilter()).collect(Collectors.toList());
            }
            EntityLivingBase target = EntityUtil.getClosest_old(targets, this.rangeSetting.getValue());
            double random = 1.0 + Math.random() * (double)-1.2f;
            if (target != null) {
                RotationUtil.oldFaceBlock(target.posX + random, target.posY + 1.3 + random, target.posZ + random);
                if (this.ticks % (new Random().nextInt(2) + 1) == 0 && target.ticksExisted > 2) {
                    this.mc.thePlayer.swingItem();
                    this.mc.playerController.attackEntity(this.mc.thePlayer, target);
                    this.t = target;
                    this.isAttacking = true;
                    if (this.blockSetting.getValue().booleanValue() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                        this.blocking = true;
                        this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getCurrentEquippedItem()));
                    }
                    RotationUtil.setRotating("killaura", true);
                }
            } else {
                RotationUtil.setRotating("killaura", false);
                this.isAttacking = false;
                if (this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && this.blocking && this.mc.thePlayer.getCurrentEquippedItem() != null) {
                    this.blocking = false;
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                this.t = null;
                this.attackPost = false;
            }
        }
    }
}

