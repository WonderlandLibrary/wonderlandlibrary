// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin
{
    private static final ResourceLocation field_177337_a;
    private static final ResourceLocation field_177336_b;
    private static final String __OBFID = "CL_00002396";
    
    public static ResourceLocation func_177335_a() {
        return DefaultPlayerSkin.field_177337_a;
    }
    
    public static ResourceLocation func_177334_a(final UUID p_177334_0_) {
        return func_177333_c(p_177334_0_) ? DefaultPlayerSkin.field_177336_b : DefaultPlayerSkin.field_177337_a;
    }
    
    public static String func_177332_b(final UUID p_177332_0_) {
        return func_177333_c(p_177332_0_) ? "slim" : "default";
    }
    
    private static boolean func_177333_c(final UUID p_177333_0_) {
        return (p_177333_0_.hashCode() & 0x1) == 0x1;
    }
    
    static {
        field_177337_a = new ResourceLocation("textures/entity/steve.png");
        field_177336_b = new ResourceLocation("textures/entity/alex.png");
    }
}
