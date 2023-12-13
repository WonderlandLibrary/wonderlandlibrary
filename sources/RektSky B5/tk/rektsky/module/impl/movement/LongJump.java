/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import tk.rektsky.event.EventManager;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.specials.flys.VerusFlat;

public class LongJump
extends Module {
    private VerusFlat flat = new VerusFlat();

    public LongJump() {
        super("LongJump", "Shortcut to Flatten Fly bcs they are the same... isn't it? (Flags slowly)", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        this.flat.onEnable();
        EventManager.EVENT_BUS.register(this.flat);
    }

    @Override
    public void onDisable() {
        this.flat.onDisable();
        EventManager.EVENT_BUS.unregister(this.flat);
    }
}

