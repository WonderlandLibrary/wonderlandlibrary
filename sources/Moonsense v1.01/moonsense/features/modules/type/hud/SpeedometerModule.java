// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.util.MathHelper;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class SpeedometerModule extends SCDefaultRenderModule
{
    private final Setting decimalPoints;
    private final Setting unit;
    
    public SpeedometerModule() {
        super("Speedometer", "Display your speed in blocks per second on the HUD.", 18);
        new Setting(this, "Number Options");
        this.decimalPoints = new Setting(this, "Decimal Points").setDefault(2).setRange(0, 8, 1);
        this.unit = new Setting(this, "Unit").setDefault(0).setRange("blocks/sec", "meters/sec", "b/s", "m/s", "BpS", "MpS", "NONE");
    }
    
    @Override
    public Object getValue() {
        final double distX = this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX;
        final double distZ = this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ;
        return String.format("%." + this.decimalPoints.getInt() + "f", MathHelper.sqrt_double(distX * distX + distZ * distZ) / 0.05f);
    }
    
    @Override
    public String getFormat() {
        String bracketType = this.brackets.getValue().get(this.brackets.getInt() + 1);
        if (bracketType.equalsIgnoreCase("NONE")) {
            bracketType = "  ";
        }
        if (this.unit.getValue().get(this.unit.getInt() + 1).equalsIgnoreCase("NONE")) {
            return String.valueOf(bracketType.charAt(0)) + "%value%" + bracketType.charAt(1);
        }
        return String.valueOf(bracketType.charAt(0)) + "%value% " + this.unit.getValue().get(this.unit.getInt() + 1) + bracketType.charAt(1);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_LEFT;
    }
}
