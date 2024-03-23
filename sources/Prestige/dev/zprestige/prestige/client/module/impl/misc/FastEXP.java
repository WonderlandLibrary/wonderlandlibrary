package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class FastEXP extends Module {

    public DragSetting delay;
    public TimerUtil timer;

    public FastEXP() {
        super("Fast EXP", Category.Misc, "Spams EXP bottles");
        delay = setting("Delay", 30, 50, 20, 500).description("Delay between throwing each bottle");
        timer = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused()) {
            return;
        }
        if (timer.delay(delay) && !Prestige.Companion.getClickManager().click() && hasItem() && GLFW.glfwGetMouseButton(getMc().getWindow().getHandle(), 1) == 1) {
            Prestige.Companion.getClickManager().setClick(1, 0.0f);
            delay.setValue();
            timer.reset();
        }
    }

    private boolean hasItem() {
        return getMc().player != null && getMc().player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE;
    }
}