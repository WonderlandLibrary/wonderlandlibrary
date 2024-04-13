package net.smoothboot.client.module.combat;

import net.smoothboot.client.module.Mod;

public class WTap extends Mod {

    public WTap() {
        super("W Tap", "", Category.Combat);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (!mc.options.forwardKey.isPressed()) return;

        super.onTick();
    }
}
