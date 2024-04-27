package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.event.events.AttackEvent;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name = "SuperKnockback", description = "Increases knockback dealt to other entities.", category = ModuleCategory.BLATANT)
public class SuperKnockback extends Module {

    private IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);

    @EventTarget
    public void onAttack(AttackEvent event){
        if (event.getTargetEntity() instanceof EntityLivingBase) {
            if (((EntityLivingBase) event.getTargetEntity()).hurtTime > hurtTimeValue.get())
                return;

            if (mc.thePlayer.isSprinting())
                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));

            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            mc.thePlayer.setSprinting(true);
            mc.thePlayer.serverSprintState = true;
        }
    }
}
