package best.azura.client.impl.module.impl.movement;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.module.impl.movement.clicktp.RedeDarkClickTPImpl;
import best.azura.client.impl.module.impl.movement.clicktp.SpartanB453ClickTPImpl;
import best.azura.client.impl.module.impl.movement.clicktp.VerusClickTPImpl;
import best.azura.client.util.modes.ModeUtil;
import best.azura.client.impl.value.ModeValue;

import java.util.Arrays;

@ModuleInfo(name = "Teleport", category = Category.MOVEMENT, description = "teleport haha brr")
public class Teleport extends Module {

    public static final ModeValue mode = new ModeValue("Mode", "Lets you fly.", "");

    public Teleport() {
        super();
        ModeUtil.registerModuleModes(this, Arrays.asList(new VerusClickTPImpl(), new RedeDarkClickTPImpl(), new SpartanB453ClickTPImpl()), mode, true);
    }


    @Override
    public void onEnable() {
        super.onEnable();
        ModeUtil.onEnable(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ModeUtil.onDisable(this);
    }
}
