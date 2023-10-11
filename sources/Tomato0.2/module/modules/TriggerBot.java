/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.events.EventClick;
import cow.milkgod.cheese.events.EventPostMotionUpdates;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.module.modules.AutoPot;
import cow.milkgod.cheese.module.modules.CivBreak;
import cow.milkgod.cheese.module.modules.Killaura;
import cow.milkgod.cheese.module.modules.Nametags;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.CombatUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class TriggerBot
extends Module {
    public static ArrayList<Entity> attackList = new ArrayList();
    public static ArrayList<Entity> targets = new ArrayList();
    public static int currentTarget;
    public static boolean attacking;
    private int attackDelay = 0;

    public TriggerBot() {
        super("TriggerBot", 37, Category.COMBAT, 13369344, true, "Automaticly attack entities that are within legit FOV", new String[]{"tbot"});
    }

    private boolean isValidTarget(Entity target) {
        Random randomReach = new Random();
        int randomreachincrease = randomReach.nextInt(4);
        double RRincrease = (double)randomreachincrease * 0.1;
        if (!target.isDead && target.isEntityAlive() && (double)Wrapper.mc.thePlayer.getDistanceToEntity(target) <= 3.0 + RRincrease && targets.contains(target) && target != Wrapper.mc.thePlayer && target.ticksExisted >= 15 && (FriendManager.isFriend(target.getName()) && Killaura.friends.getValue().booleanValue() || !FriendManager.isFriend(target.getName()))) {
            return true;
        }
        return false;
    }

    private boolean canAttack() {
        if (!attackList.isEmpty()) {
            Cheese.getInstance();
            if (!(Cheese.moduleManager.getModState("Freecam") || Wrapper.mc.thePlayer.isUsingItem() && !Wrapper.mc.thePlayer.isBlocking() || CivBreak.isMining)) {
                return true;
            }
        }
        return false;
    }

    private void doAttack(Entity target) {
        Wrapper.mc.thePlayer.swingItem();
        Wrapper.mc.playerController.attackEntity(Wrapper.mc.thePlayer, target);
    }

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        Entity e2;
        for (Object o22 : Wrapper.mc.theWorld.loadedEntityList) {
            Entity e22 = (Entity)o22;
            if (!(e22 instanceof EntityPlayer) || targets.contains(e22)) continue;
            targets.add(e22);
        }
        for (Object o : Wrapper.mc.theWorld.loadedEntityList) {
            e2 = (Entity)o;
            if (!this.isValidTarget(e2) || attackList.contains(e2)) continue;
            attackList.add(e2);
        }
        for (Object o2 : attackList) {
            e2 = (Entity)o2;
            if (this.isValidTarget(e2)) continue;
            attackList.remove(e2);
        }
        if (currentTarget >= 1) {
            currentTarget = 0;
        }
        if (Wrapper.mc.thePlayer != null) {
            ++this.attackDelay;
        }
        if (!this.canAttack()) {
            attacking = false;
            return;
        }
        attacking = true;
        if (!AutoPot.CurrentlyPotting() && !Wrapper.mc.thePlayer.isDead) {
            Random random = new Random();
            float randomRotationSpeed = random.nextInt(4);
            if ((Nametags.isFacingAtEntity(attackList.get(currentTarget), 180.0) || Wrapper.mc.objectMouseOver.entityHit != null) && Wrapper.mc.thePlayer.canEntityBeSeen(attackList.get(currentTarget))) {
                CombatUtils.faceTarget(attackList.get(currentTarget), randomRotationSpeed, randomRotationSpeed);
                Random randomYawBodyRotationChange = new Random();
                Random randomYawHeadRotationChange = new Random();
                Random randomPitchRotationChange = new Random();
                EntityPlayerSP thePlayer = Wrapper.mc.thePlayer;
                thePlayer.rotationYaw += (float)randomYawBodyRotationChange.nextInt(3);
                EntityPlayerSP thePlayer2 = Wrapper.mc.thePlayer;
                thePlayer2.rotationYawHead += (float)randomYawBodyRotationChange.nextInt(8);
                EntityPlayerSP thePlayer3 = Wrapper.mc.thePlayer;
                thePlayer3.rotationYaw -= (float)randomYawBodyRotationChange.nextInt(3);
                EntityPlayerSP thePlayer4 = Wrapper.mc.thePlayer;
                thePlayer4.rotationYawHead -= (float)randomYawBodyRotationChange.nextInt(8);
                EntityPlayerSP thePlayer5 = Wrapper.mc.thePlayer;
                thePlayer5.rotationPitch += (float)randomPitchRotationChange.nextInt(3);
                EntityPlayerSP thePlayer6 = Wrapper.mc.thePlayer;
                thePlayer6.rotationPitch -= (float)randomPitchRotationChange.nextInt(3);
            }
        }
    }

    @EventTarget
    public void onPost(EventPostMotionUpdates post) {
        if (!attacking) {
            return;
        }
        if (!AutoPot.CurrentlyPotting()) {
            Random random = new Random();
            int randomD = random.nextInt(5);
            if (Wrapper.mc.thePlayer.canEntityBeSeen(attackList.get(currentTarget)) && (Nametags.isFacingAtEntity(attackList.get(currentTarget), 100.0) || Wrapper.mc.objectMouseOver.entityHit != null) && this.attackDelay >= 20 / (15 - randomD)) {
                this.doAttack(attackList.get(currentTarget));
                this.attackDelay = 0;
                if (TriggerBot.attackList.get((int)TriggerBot.currentTarget).isDead || !this.isValidTarget(attackList.get(currentTarget))) {
                    ++currentTarget;
                }
            } else {
                ++currentTarget;
            }
        }
    }

    @Override
    public void toggleModule() {
        super.toggleModule();
        if (!this.getState()) {
            attackList.clear();
            currentTarget = 0;
        }
    }

    @EventTarget
    public void onClick(EventClick click) {
        if (!attacking) {
            return;
        }
        click.setCancelled(true);
    }
}

