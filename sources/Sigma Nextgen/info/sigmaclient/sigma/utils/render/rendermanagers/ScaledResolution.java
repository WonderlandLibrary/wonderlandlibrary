//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\1\Desktop\Minecraft-Deobfuscator3000-1.2.3\config"!

package info.sigmaclient.sigma.utils.render.rendermanagers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class
ScaledResolution {
        public double scaledWidthD;
        public double scaledHeightD;
        public int scaledWidth;
        public int scaledHeight;
        public int scaleFactor;

        public ScaledResolution(Minecraft p_i46445_1_)
        {
            this.scaledWidth = p_i46445_1_.getMainWindow().getScaledWidth();
            this.scaledHeight = p_i46445_1_.getMainWindow().getScaledHeight();
            this.scaleFactor = 1;
            boolean flag = p_i46445_1_.getForceUnicodeFont();
            this.scaledWidthD = (double)this.scaledWidth;
            this.scaledHeightD = (double)this.scaledHeight;
            this.scaledWidth = MathHelper.ceil(this.scaledWidthD);
            this.scaledHeight = MathHelper.ceil(this.scaledHeightD);
        }
        public int getScaledWidth()
        {
            return this.scaledWidth;
        }

        public int getScaledHeight()
        {
            return this.scaledHeight;
        }

        public int getScaleFactor()
        {
            return this.scaleFactor;
        }
    }


