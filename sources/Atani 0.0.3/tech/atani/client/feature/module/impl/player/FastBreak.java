package tech.atani.client.feature.module.impl.player;

import net.minecraft.util.MovingObjectPosition;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "FastBreak", description = "Break blocks faster", category = Category.PLAYER)
public class FastBreak extends Module {
    private final SliderValue<Float> speed = new SliderValue<>("Speed", "How fast should the breaking speed be?", this, 4F, 0F, 10F, 1);

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        if(Methods.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && isKeyDown(Methods.mc.gameSettings.keyBindAttack.getKeyCode())) {
            if(Methods.mc.playerController.curBlockDamageMP > 1 - (speed.getValue().floatValue() / 10)) {
                Methods.mc.playerController.curBlockDamageMP = 1;
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
