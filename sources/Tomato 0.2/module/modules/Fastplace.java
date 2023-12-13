/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import net.minecraft.client.Minecraft;

public class Fastplace
extends Module {
    public Fastplace() {
        super("FastPlace", 0, Category.PLAYER, 15120384, true, "Lowers the delay between right clicks", new String[]{"fp", "fastright", "fplace"});
    }

    @EventTarget
    public void onTick(EventTick event) {
        Minecraft.getMinecraft();
        Minecraft.rightClickDelayTimer = 1;
    }
}

