package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.render.CaveFinder;
import me.aquavit.liquidsense.utils.render.Colors;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteOrder;
import java.nio.IntBuffer;

@Mixin(WorldRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinWorldRenderer {

    @Shadow
    private boolean noColor;

    @Shadow
    public abstract int getColorIndex(int alpha);

    private IntBuffer rawIntBuffer;

    /**
     * @author CCBlueX
     * @reason CCBlueX
     */
    @Overwrite
    public void putColorMultiplier(float red, float green, float blue, int alpha) {
        int iG;
        int iR;
        int iB;
        int i = this.getColorIndex(alpha);
        int j = -1;
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            iR = (int)((float)(j & 255) * red);
            iG = (int)((float)(j >> 8 & 255) * green);
            iB = (int)((float)(j >> 16 & 255) * blue);
            j &= -16777216;
            j = j | iB << 16 | iG << 8 | iR;
        } else {
            iR = (int)((float)(j >> 24 & 255) * red);
            iG = (int)((float)(j >> 16 & 255) * green);
            iB = (int)((float)(j >> 8 & 255) * blue);
            j &= 255;
            j = j | iR << 24 | iG << 16 | iB << 8;
        }
        if (this.noColor) {
            j = -1;
        }
        if (LiquidSense.moduleManager.getModule(CaveFinder.class).getState()) {
            j = Colors.getColor(iR, iG, iB, CaveFinder.opacity.get());
        }
        this.rawIntBuffer.put(i, j);
    }

}
