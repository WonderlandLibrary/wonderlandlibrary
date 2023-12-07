package net.minecraft.client.renderer.entityrenderers;

import java.util.concurrent.Callable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

public class EntityRenderer2 implements Callable {
	
    public EntityRenderer field_90025_c;
    private static final String __OBFID = "CL_00000948";

    public EntityRenderer2(EntityRenderer p_i46419_1_)
    {
        this.field_90025_c = p_i46419_1_;
    }

    public String call() throws Exception
    {
        return Minecraft.getMinecraft().currentScreen.getClass().getCanonicalName();
    }
    
}
