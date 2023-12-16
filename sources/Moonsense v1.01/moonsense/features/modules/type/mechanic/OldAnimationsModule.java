// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class OldAnimationsModule extends SCModule
{
    public static OldAnimationsModule INSTANCE;
    public final Setting blockHit;
    public final Setting eating;
    public final Setting bow;
    public final Setting build;
    public final Setting rod;
    public final Setting swing;
    public final Setting block;
    public final Setting damage;
    public final Setting hitColor;
    
    public OldAnimationsModule() {
        super("Old Animations", "Revert certain visuals and animations to how they behaved on Minecraft 1.7.", 16);
        new Setting(this, "Animations");
        this.blockHit = new Setting(this, "Blockhit").setDefault(false);
        this.eating = new Setting(this, "Eating").setDefault(false);
        this.bow = new Setting(this, "Bow").setDefault(false);
        this.build = new Setting(this, "Build").setDefault(false);
        this.rod = new Setting(this, "Rod").setDefault(false);
        this.swing = new Setting(this, "Swing").setDefault(false);
        this.block = new Setting(this, "Block").setDefault(false);
        this.damage = new Setting(this, "Damage").setDefault(false);
        new Setting(this, "Color Options");
        this.hitColor = new Setting(this, "Hit Color").setDefault(new Color(1.0f, 0.0f, 0.0f, 0.3f).getRGB(), 0);
        OldAnimationsModule.INSTANCE = this;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
}
