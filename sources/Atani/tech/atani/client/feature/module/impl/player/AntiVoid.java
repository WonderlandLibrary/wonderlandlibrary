package tech.atani.client.feature.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import tech.atani.client.listener.event.minecraft.player.movement.MovePlayerEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.utility.world.block.BlockUtil;

@ModuleData(name = "AntiVoid", description = "Tries to make you not fall in the void.", category = Category.PLAYER)
public class AntiVoid extends Module {
    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, (new String[] {"Flag", "Position", "Ground"}));
    private final CheckBoxValue voidCheck = new CheckBoxValue("Void Check", "Should the module check for void?", this, true);

    @Listen
    public void onMove(MovePlayerEvent event) {
        if((!BlockUtil.isBlockUnderPlayer() || voidCheck.getValue() && Methods.mc.thePlayer.fallDistance > 5 && !Methods.mc.gameSettings.keyBindSneak.isKeyDown() && !Methods.mc.thePlayer.capabilities.isFlying)) {
            switch(mode.getValue()) {
                case "Flag":
                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition());
                    break;
                case "Position":
                    event.setY(event.getY() + Methods.mc.thePlayer.fallDistance);
                    break;
                case "Ground":
                    Methods.mc.thePlayer.onGround = true;
                    break;

            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}