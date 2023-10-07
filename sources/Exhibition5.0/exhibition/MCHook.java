// 
// Decompiled by Procyon v0.5.30
// 

package exhibition;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.Minecraft;

public class MCHook extends Minecraft
{
    public MCHook(final GameConfiguration gc) {
        super(gc);
    }
    
    @Override
    protected void func_180510_a(final TextureManager texMan) {
        try {
            new Client().setup();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        super.func_180510_a(texMan);
    }
}
