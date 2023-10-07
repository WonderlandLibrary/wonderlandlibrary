package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

public final class AutoTool extends AbstractModule {

    @Property("switch-back")
    private BooleanProperty switch_back = PropertyFactory.booleanTrue();

    private int oldSlot;
    private int tick;

    /* constructors */
    public AutoTool(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "AutoTool", "Auto Tool", Keyboard.KEY_NONE, EnumModuleType.MISC, "Switches to the best tool");
        Manager.put(new Setting("AT_SWITCH_BACK", "Switch Back", SettingType.CHECKBOX, this, switch_back));
    }

    /* methods */
    @EventTarget
    public void onPre(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (mc.playerController.isBreakingBlock()) {
                tick++;

                if (tick == 1) {
                    oldSlot = mc.player.inventory.currentItem;
                }

                mc.player.updateTool(mc.objectMouseOver.getBlockPos());
            } else if (tick > 0) {
                if (switch_back.get()) {
                    mc.player.inventory.currentItem = oldSlot;
                }

                tick = 0;
            }
        }
    }
}