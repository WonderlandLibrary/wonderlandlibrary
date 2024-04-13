package dev.tenacity.module.impl.render;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.TimerUtil;

public final class BlockAnimationModule extends Module {

    private final TimerUtil timerUtil = new TimerUtil();
    private static final ModeSetting animationMode = new ModeSetting("Animation Mode", "1.7", "Butter", "Stella", "Fathum", "Exhi", "Exhi 2", "Shred", "Smooth", "Sigma");

    public BlockAnimationModule() {
        super("BlockAnimation", "Changes your block animations", ModuleCategory.RENDER);
        initializeSettings(animationMode);
        if (animationMode.isMode("Butter")) {
            ChatUtil.credit("Thanks to Slqnt on discord for this animation!");
        }
    }
    private final IEventListener<UpdateEvent> onUpdateEvent = event -> setSuffix(animationMode.getCurrentMode());

    public ModeSetting getAnimationMode() {
        return animationMode;
    }


    @Override
    public void onEnable() {
        if (animationMode.isMode("Butter")) {
            ChatUtil.credit("Thanks to Slqnt on discord for this animation!");
        }
        super.onEnable();
    }

    private final IEventListener<Render2DEvent> onRender2DEvent = event -> {
    };
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
