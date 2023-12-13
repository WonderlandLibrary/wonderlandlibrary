package me.nyan.flush.module.impl.render;

import me.nyan.flush.Flush;
import me.nyan.flush.module.Module;
import org.lwjgl.opengl.Display;

public class UnfocusedCpu extends Module {
    public UnfocusedCpu() {
        super("UnfocusedCPU", Category.RENDER);
    }

    public static boolean isUnfocused() {
        UnfocusedCpu module = Flush.getInstance().getModuleManager().getModule(UnfocusedCpu.class);
        return module.isEnabled() && !Display.isActive();
    }
}
