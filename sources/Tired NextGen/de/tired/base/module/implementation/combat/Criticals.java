package de.tired.base.module.implementation.combat;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.util.hook.PlayerHook;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventBobbing;
import de.tired.base.event.events.UpdateEvent;
import de.tired.Tired;
import net.minecraft.block.BlockAir;

@ModuleAnnotation(name = "Criticals", category = ModuleCategory.COMBAT)
public class Criticals extends Module {

    public int startY;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.isState()) return;
        final KillAura killAura = Tired.INSTANCE.moduleManager.moduleBy(KillAura.class);
        final boolean hasTarget = killAura.isState() && killAura.target != null;

        if (hasTarget) {
            if (doesMeetsCriticalRequirements()) {
                startY = (int) MC.thePlayer.posY;
                MC.timer.timerSpeed = 1F;
                if (MC.thePlayer.onGround) {
                    MC.thePlayer.jump();
                    MC.timer.timerSpeed = 1.1F;
                } else if (PlayerHook.getBlockRelativeToPlayer(0, -.8, 0) instanceof BlockAir) {
                    MC.thePlayer.motionY -= .8F;
                    MC.timer.timerSpeed = .9F;
                }
            } else {
                MC.timer.timerSpeed = 1F;
            }
        } else {
            MC.timer.timerSpeed = 1F;
        }
    }

    @EventTarget
    public void onBobbing(EventBobbing eventBobbing) {
        final KillAura killAura = Tired.INSTANCE.moduleManager.moduleBy(KillAura.class);
        final boolean hasTarget = killAura.isState() && killAura.target != null;

        if (hasTarget) {

            }
        }

    private boolean doesMeetsCriticalRequirements() {
        return MC.thePlayer.onGround && !MC.thePlayer.isInWater() && !MC.thePlayer.isOnLadder();
    }

    @Override
    public void onState() {
        startY = (int) MC.thePlayer.posY;
    }

    @Override
    public void onUndo() {
        MC.timer.timerSpeed = 1F;
    }
}
