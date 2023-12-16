// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import moonsense.enums.ModuleCategory;
import moonsense.event.SubscribeEvent;
import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import moonsense.event.impl.SCClientTickEvent;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class FOVModule extends SCModule
{
    public static FOVModule INSTANCE;
    private static float savedFOV;
    private final Setting sprintFOV;
    private final Setting speedFOV;
    private final Setting flyingFOV;
    
    static {
        FOVModule.savedFOV = 0.0f;
    }
    
    public FOVModule() {
        super("FOV", "Changes the Minecraft FOV behavior");
        FOVModule.INSTANCE = this;
        this.sprintFOV = new Setting(this, "Modify Sprinting FOV").setDefault(false);
        this.speedFOV = new Setting(this, "Modify Speed Effect FOV").setDefault(true);
        this.flyingFOV = new Setting(this, "Modify Flying FOV").setDefault(true);
    }
    
    @SubscribeEvent
    public void onTick(final SCClientTickEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        FOVModule.savedFOV = Minecraft.getMinecraft().gameSettings.fovSetting;
        if (this.mc.thePlayer.isSprinting() && this.sprintFOV.getBoolean()) {
            Minecraft.getMinecraft().gameSettings.fovSetting = FOVModule.savedFOV;
        }
        else if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed.id) && this.speedFOV.getBoolean()) {
            Minecraft.getMinecraft().gameSettings.fovSetting = FOVModule.savedFOV;
        }
        else if (this.mc.thePlayer.capabilities.isFlying && this.flyingFOV.getBoolean()) {
            Minecraft.getMinecraft().gameSettings.fovSetting = FOVModule.savedFOV;
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
}
