// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import moonsense.config.utils.AnchorPoint;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.Block;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.Minecraft;
import moonsense.features.SCDefaultRenderModule;

public class MLGHelperModule extends SCDefaultRenderModule
{
    public MLGHelperModule() {
        super("MLG Helper", "No description set!");
    }
    
    @Override
    public Object getValue() {
        final EntityPlayerSP p = this.mc.thePlayer;
        final MovingObjectPosition mp = p.func_174822_a(200.0, 1.0f);
        final int height = mp.func_178782_a().getY() + 1;
        final int down = mp.func_178782_a().getY() + 1;
        final int up = Minecraft.getMinecraft().thePlayer.getPosition().getY();
        final int df = up - down;
        String mlgstatus;
        if (df <= 5) {
            mlgstatus = "Save";
        }
        else if ((df >= 6 && df <= 16) || df == 18 || df == 19 || df == 21 || df == 22 || df == 24 || df == 26 || df == 27 || df == 29 || df == 33 || df == 34 || df == 36 || df == 38 || df == 40 || df == 42 || df == 44 || df == 47 || df == 49 || df == 51 || df == 53 || df == 55 || df == 58 || df == 60 || df == 62 || df == 65 || df == 67 || df == 69 || df == 72 || df == 74 || df == 77 || df == 79 || df == 82 || df == 85 || df == 87 || df == 90 || df == 93 || df == 95 || df == 98 || df == 101 || df == 104 || df == 106 || df == 109 || df == 112 || df == 115 || df == 118 || df == 121 || df == 124 || df == 127 || df == 130 || df == 135 || df == 138 || df == 142 || df == 145 || df == 148 || df == 151 || df == 153 || df == 154) {
            mlgstatus = "Run";
        }
        else if (df == 18 || df == 17 || df == 20 || df == 23 || df == 28 || df == 30 || df == 31 || df == 35 || df == 37 || df == 39 || df == 41 || df == 43 || df == 45 || df == 50 || df == 52 || df == 54 || df == 56 || df == 59 || df == 61 || df == 63 || df == 66 || df == 68 || df == 71 || df == 73 || df == 76 || df == 78 || df == 81 || df == 83 || df == 86 || df == 91 || df == 94 || df == 97 || df == 100 || df == 102 || df == 105 || df == 108 || df == 111 || df == 114 || df == 119 || df == 122 || df == 125 || df == 128 || df == 131 || df == 134 || df == 137 || df == 140 || df == 143 || df == 146 || df == 150 || df == 156) {
            mlgstatus = "Jump";
        }
        else {
            mlgstatus = "Impossible";
        }
        String prefix;
        String suffix;
        if (mp != null && mp.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && df >= 0) {
            prefix = "\u00c2§6" + down + "\u00c2§8: \u00c2§f";
            if (Minecraft.getMinecraft().theWorld.getBlockState(mp.func_178782_a()) != Block.getStateById(30)) {
                if (up < down) {
                    prefix = "\u00c2§6N/A\u00c2§8: \u00c2§f";
                    suffix = "Not specified";
                }
                else {
                    prefix = "\u00c2§6" + down + "\u00c2§8: \u00c2§f";
                    suffix = mlgstatus;
                }
            }
            else {
                suffix = "Cobweb! Choose another block!";
            }
        }
        else {
            prefix = "\u00c2§6N/A\u00c2§8: \u00c2§f";
            suffix = "Not specified";
        }
        return String.valueOf(prefix) + suffix;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_CENTER;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.NEW;
    }
}
