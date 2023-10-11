package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.TimeUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPosition;
import org.lwjgl.input.Keyboard;

public class Eagle extends AbstractModule {

    private final NumberSetting delay;

    private final TimeUtil timer;

    public Eagle() {
        super("Eagle", "Sneaks on the edge of a block.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                delay = new NumberSetting(this, "Delay", 1, 300, 30)

        );

        timer = new TimeUtil();
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            if (mc.theWorld.getBlockState(new BlockPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
                if(timer.elapsed(delay.get().intValue(), true)) mc.gameSettings.keyBindSneak.pressed = true;
            } else {
                mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
            }
        }
    }

    @Override
    public void onDisable() { mc.gameSettings.keyBindSneak.pressed = false; }
}