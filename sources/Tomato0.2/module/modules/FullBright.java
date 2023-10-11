/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class FullBright
extends Module {
    public FullBright() {
        super("FullBright", 0, Category.RENDER, 39168, true, "Cheese lords shines down unto you", new String[]{"bright", "fb", "light"});
    }

    @Override
    public void onUpdate() {
        if (this.getState()) {
            if (Wrapper.mc.gameSettings.gammaSetting < 16.0f) {
                Wrapper.mc.gameSettings.gammaSetting += 0.5f;
            }
        } else if (Wrapper.mc.gameSettings.gammaSetting > 0.5f) {
            Wrapper.mc.gameSettings.gammaSetting = Wrapper.mc.gameSettings.gammaSetting < 1.0f ? 0.5f : (Wrapper.mc.gameSettings.gammaSetting -= 0.5f);
        }
    }
}

