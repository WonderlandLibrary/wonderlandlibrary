package net.minecraft.client.renderer;

import net.minecraft.client.Minecraft;

import java.util.concurrent.Callable;

class EntityRenderer2 implements Callable {
    final EntityRenderer field_90025_c;
    private static final String __OBFID = "CL_00000948";

    EntityRenderer2(EntityRenderer p_i46419_1_) {
        this.field_90025_c = p_i46419_1_;
    }

    public String call() throws Exception {
        return Minecraft.getInstance().currentScreen.getClass().getCanonicalName();
    }
}
