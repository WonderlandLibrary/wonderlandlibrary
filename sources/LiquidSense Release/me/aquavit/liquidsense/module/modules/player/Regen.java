package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Regen", description = "Regenerates your health much faster.", category = ModuleCategory.PLAYER)
public class Regen extends Module {

    private ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Spartan"}, "Vanilla");
    private IntegerValue healthValue = new IntegerValue("Health", 18, 0, 20);
    private IntegerValue foodValue = new IntegerValue("Food", 18, 0, 20);
    private IntegerValue speedValue = new IntegerValue("Speed", 100, 1, 100);
    private BoolValue noAirValue = new BoolValue("NoAir", false);

    private boolean resetTimer;


    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (resetTimer) mc.timer.timerSpeed = 1F;

        if ((!noAirValue.get() || mc.thePlayer.onGround) && !mc.thePlayer.capabilities.isCreativeMode &&
                mc.thePlayer.getFoodStats().getFoodLevel() > foodValue.get() && mc.thePlayer.isEntityAlive() && mc.thePlayer.getHealth() < healthValue.get()) {
            switch (modeValue.get()){
                case "Vanilla": {
                    for (int i=0; i<=speedValue.get();i++){
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
                    }
                    break;
                }
                case "Spartan": {
                    if (MovementUtils.isMoving() || !mc.thePlayer.onGround) return;

                    for (int i=0; i<=9;i++){
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
                    }

                    mc.timer.timerSpeed = 0.45F;
                    resetTimer = true;
                    break;
                }
            }
        }
    }
}
