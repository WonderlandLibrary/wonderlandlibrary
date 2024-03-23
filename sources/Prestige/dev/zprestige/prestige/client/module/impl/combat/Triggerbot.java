package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.Phase;
import dev.zprestige.prestige.client.event.impl.EntityEvent;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.managers.SocialsManager;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import dev.zprestige.prestige.client.util.impl.*;

import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class Triggerbot extends Module {

    public MultipleSetting targetMode;
    public IntSetting cooldown;
    public FloatSetting chance;
    public DragSetting reaction;
    public BooleanSetting weapon;
    public BooleanSetting holdCrystal;
    public BooleanSetting ownCrystals;
    public BooleanSetting inAir;
    public BooleanSetting critTiming;
    public BooleanSetting pauseOnKill;
    public TimerUtil timer;
    public ArrayList<Entity> targets;
    public Vec3d pos;

    public Triggerbot() {
        super("Triggerbot", Category.Combat, "Attacks entities when looking at them");
        targetMode = setting("Targets", new String[]{"Players", "Crystals"}, new Boolean[]{true, true}).description("What entities to attack");
        cooldown = setting("Cooldown Progress", 90, 0, 100).description("Minimum attack cooldown progress to attack");
        chance = setting("Chance", 100.0f, 0.0f, 100.0f).description("Chance to attack an entity successfully");
        reaction = setting("Reaction", 10.0f, 50.0f, 0.0f, 200.0f).description("Delay between looking at the entity and attacking");
        weapon = setting("Require Weapon", true).description("Requires you to hold an axe or sword in order to attack");
        holdCrystal = setting("Hold Crystal", true).description("Waits for you to hold a crystal in your hand before attacking");
        ownCrystals = setting("Only Own Crystals", true).description("Will only break your own crystals (Helps bypass MCPvP's anti-cheat");
        inAir = setting("In Air", false).description("Will only attack when on ground (Helps bypass MCPvP's anti-cheat");
        critTiming = setting("Crit Timing", false).description("Crit timing when jumping, requires In Air to be disabled");
        pauseOnKill = setting("Pause On Kill", false).description("Pauses when there is a dead body");
        timer = new TimerUtil();
        targets = new ArrayList();
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused()) {
            return;
        }
        if (getMc().player != null && (getMc().player.isOnGround() || inAir.getObject())) {
            if (!pauseOnKill.getObject().booleanValue() || !OneLineUtil.isInvalidPlayer()) {
                if (critTiming.getObject()) {
                    if (getMc().player != null && !getMc().player.isOnGround() && getMc().player != null && getMc().player.fallDistance > 0) {
                        return;
                    }
                }
                if (getMc().player != null) {
                    if (getMc().player.getMainHandStack().getItem() instanceof SwordItem) {
                        if (getMc().player != null && getMc().player.getAttackCooldownProgress(0.5f) < cooldown.getObject().floatValue() / 100) {
                            return;
                        }
                    }
                    if (getMc().player.getMainHandStack().getItem() instanceof AxeItem) {
                        if (getMc().player != null && getMc().player.getAttackCooldownProgress(0.5f) < cooldown.getObject().floatValue() / 100) {
                            return;
                        }
                    }
                }
                if (getMc().crosshairTarget instanceof EntityHitResult entityHitResult) {
                    Entity entity = entityHitResult.getEntity();
                    if (method602(entity) && RandomUtil.INSTANCE.randomInRange(0, 100) <= chance.getObject() && timer.delay(reaction)) {
                        entity = entityHitResult.getEntity();
                        if (entity instanceof PlayerEntity player && EntityUtil.INSTANCE.isUsingShield(player)) {
                            return;
                        }
                        getMc().interactionManager.attackEntity(getMc().player, entity);
                        getMc().player.swingHand(Hand.MAIN_HAND);
                        timer.reset();
                        reaction.setValue();
                        return;
                    }
                }
                timer.reset();
                reaction.setValue();
            }
        }
    }

    @EventListener
    public void event(PacketSendEvent event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket) {
            Item item = Items.END_CRYSTAL;
            if (InventoryUtil.INSTANCE.isHoldingItem(item)) {
                pos = ((PlayerInteractBlockC2SPacket)event.getPacket()).getBlockHitResult().getPos();
            }
        }
    }

    @EventListener
    public void event(EntityEvent event) {
        if (pos == null && !(event.getEntity() instanceof EndCrystalEntity)) return;
        if (event.getPhase() == Phase.PRE) {
            Entity entity = event.getEntity();
            if (entity.squaredDistanceTo(pos) <= 2) {
                targets.add(event.getEntity());
            }
        }
        if (event.getPhase() == Phase.POST && targets.contains(event.getEntity())) {
            targets.remove(event.getEntity());
        }
    }

    boolean method599(Entity entity) {
        if (!ownCrystals.getObject()) {
            return true;
        }
        if (!(entity instanceof EndCrystalEntity)) {
            for (Entity entity2 : getMc().world.getEntities()) {
                if (entity2 instanceof EndCrystalEntity && entity2.distanceTo(entity) < 1) {
                    return true;
                }
            }
        }
        return targets.contains(entity);
    }

    boolean method602(Entity entity) {
        if (entity instanceof PlayerEntity) {
            SocialsManager socialsManager = Prestige.Companion.getSocialsManager();
            String string = entity.getEntityName();
            if (socialsManager.isFriend(string)) return false;
        }
        if (!(entity instanceof EndCrystalEntity || entity instanceof MagmaCubeEntity || entity instanceof SlimeEntity || entity instanceof PlayerEntity)) {
            return false;
        }

        if (entity == getMc().player) return false;
        if (entity instanceof PlayerEntity) {
            if (!this.targetMode.getValue("Players") || !Prestige.Companion.getAntiBotManager().isNotBot(entity)) {
                return false;
            }
        }

        if (!(entity instanceof EndCrystalEntity) && !(entity instanceof MagmaCubeEntity) && !(entity instanceof SlimeEntity)) return true;
        if (!targetMode.getValue("Crystals") || !this.method599(entity) && ownCrystals.getObject()) return false;
        if (InventoryUtil.INSTANCE.isHoldingItem(Items.END_CRYSTAL) || !holdCrystal.getObject()) return true;
        return false;
    }
}