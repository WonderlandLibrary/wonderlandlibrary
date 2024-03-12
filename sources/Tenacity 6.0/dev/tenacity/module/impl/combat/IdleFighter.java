// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import java.util.Iterator;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import dev.tenacity.commands.impl.FriendCommand;
import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.viamcp.utils.AttackOrder;
import dev.tenacity.event.Event;
import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.player.AttackEvent;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.player.RotationUtils;
import net.minecraft.entity.Entity;
import dev.tenacity.module.api.TargetManager;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import dev.tenacity.module.Module;

public final class IdleFighter extends Module
{
    private final List<EntityLivingBase> targets;
    private final NumberSetting minCPS;
    private final NumberSetting maxCPS;
    private final NumberSetting reach;
    private final TimerUtil attackTimer;
    
    public IdleFighter() {
        super("IdleFighter", "Idle Fighter", Category.COMBAT, "Automatically finds the nearest player and attempts to kill them");
        this.targets = new ArrayList<EntityLivingBase>();
        this.minCPS = new NumberSetting("Min CPS", 10.0, 20.0, 1.0, 1.0);
        this.maxCPS = new NumberSetting("Max CPS", 10.0, 20.0, 1.0, 1.0);
        this.reach = new NumberSetting("Reach", 4.0, 6.0, 3.0, 0.1);
        this.attackTimer = new TimerUtil();
        this.addSettings(this.minCPS, this.maxCPS, this.reach);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (this.minCPS.getValue() > this.maxCPS.getValue()) {
            this.minCPS.setValue(this.minCPS.getValue() - 1.0);
        }
        IdleFighter.mc.gameSettings.keyBindForward.pressed = (TargetManager.target != null && IdleFighter.mc.thePlayer.getDistanceToEntity(TargetManager.target) > this.reach.getValue());
        IdleFighter.mc.gameSettings.keyBindJump.pressed = (IdleFighter.mc.thePlayer.isCollidedHorizontally || IdleFighter.mc.thePlayer.isInWater());
        if (event.isPre()) {
            this.sortTargets();
            if (!this.targets.isEmpty()) {
                TargetManager.target = this.targets.get(0);
                final float[] rotations = RotationUtils.getRotations(TargetManager.target.posX, TargetManager.target.posY, TargetManager.target.posZ);
                IdleFighter.mc.thePlayer.rotationYaw = rotations[0];
                IdleFighter.mc.thePlayer.rotationPitch = rotations[1];
                if (IdleFighter.mc.thePlayer.getDistanceToEntity(TargetManager.target) <= this.reach.getValue() && this.attackTimer.hasTimeElapsed(1000.0 / MathUtils.getRandomInRange(this.minCPS.getValue(), this.maxCPS.getValue()))) {
                    final AttackEvent attackEvent = new AttackEvent(TargetManager.target);
                    Tenacity.INSTANCE.getEventProtocol().handleEvent(attackEvent);
                    if (!attackEvent.isCancelled()) {
                        AttackOrder.sendFixedAttack(IdleFighter.mc.thePlayer, TargetManager.target);
                    }
                    this.attackTimer.reset();
                }
            }
            else {
                TargetManager.target = null;
            }
        }
    }
    
    @Override
    public void onDisable() {
        TargetManager.target = null;
        IdleFighter.mc.gameSettings.keyBindForward.pressed = false;
        this.targets.clear();
        super.onDisable();
    }
    
    private void sortTargets() {
        this.targets.clear();
        for (final Entity entity : IdleFighter.mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                if (!TargetManager.checkEntity(entity) || IdleFighter.mc.thePlayer == entityLivingBase || FriendCommand.isFriend(entityLivingBase.getName())) {
                    continue;
                }
                this.targets.add(entityLivingBase);
            }
        }
        this.targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)IdleFighter.mc.thePlayer::getDistanceToEntity));
    }
}
