package best.azura.client.impl.module.impl.player;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.modes.ModeUtil;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.module.impl.player.godmode.MorganGodModeImpl;

import java.util.Arrays;

@ModuleInfo(name = "God Mode", category = Category.PLAYER, description = "Prevent taking damage")
public class GodMode extends Module {

    private final ModeValue mode = new ModeValue("Mode", "Mode for God Mode", "");
    public GodMode() {
        super();
        ModeUtil.registerModuleModes(this, Arrays.asList(new MorganGodModeImpl()), mode, true);
    }

    @Override
    public void onEnable() {
        ModeUtil.onEnable(this);
        super.onEnable();
        setSuffix(mode.getObject());
    }

    @Override
    public void onDisable() {
        ModeUtil.onDisable(this);
        mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}