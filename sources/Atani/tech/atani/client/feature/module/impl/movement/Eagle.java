package tech.atani.client.feature.module.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "Eagle", description = "Makes you shift on block edges.", category = Category.MOVEMENT)
public class Eagle extends Module {
    private final SliderValue<Integer> delay = new SliderValue<Integer>("Sneak Delay", "How big will the delay be to sneak?", this, 30, 0, 300, 0);

    private final TimeHelper timer = new TimeHelper();

    @Listen
    public final void onMotion(UpdateMotionEvent updateMotionEvent) {
        if (updateMotionEvent.getType() == UpdateMotionEvent.Type.MID) {
            if (Methods.mc.theWorld.getBlockState(new BlockPos(Methods.mc.thePlayer.posX, Methods.mc.thePlayer.posY - 1.0, Methods.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && Methods.mc.thePlayer.onGround) {
                if(timer.hasReached(delay.getValue(), true)) {
                    Methods.mc.gameSettings.keyBindSneak.pressed = true;
                }
            } else {
                Methods.mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(Methods.mc.gameSettings.keyBindSneak.getKeyCode());
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() { Methods.mc.gameSettings.keyBindSneak.pressed = false; }
}
