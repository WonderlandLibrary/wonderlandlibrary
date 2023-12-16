// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import moonsense.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import moonsense.config.ModuleConfig;
import moonsense.utils.KeyBinding;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class SnaplookModule extends SCModule
{
    public static SnaplookModule INSTANCE;
    public final Setting snapLook;
    private final Setting snapLookToggle;
    private final Setting snapLookPerspective;
    public float cameraYaw;
    public float cameraPitch;
    private int behindYouPrevView;
    private boolean behindYouWasDown;
    
    public SnaplookModule() {
        super("Snaplook", "Snap to a different perspective.");
        new Setting(this, "Snaplook Options");
        this.snapLook = new Setting(this, "Keybind", "snaplook.keybind").setDefault(new KeyBinding(47));
        this.snapLookToggle = new Setting(this, "Toggle", "snaplook.toggle").setDefault(false);
        this.snapLookPerspective = new Setting(this, "Perspective", "snaplook.perspective").setDefault(1).setRange("First Person", "Third Person Back", "Third Person Front");
        SnaplookModule.INSTANCE = this;
    }
    
    public void onTick() {
        final boolean active = ((KeyBinding)this.snapLook.getObject()).isKeyDown() && ModuleConfig.INSTANCE.isEnabled(this);
        if (this.behindYouWasDown != active) {
            if (!(this.behindYouWasDown = active)) {
                Minecraft.getMinecraft().gameSettings.thirdPersonView = this.behindYouPrevView;
            }
            this.behindYouPrevView = Minecraft.getMinecraft().gameSettings.thirdPersonView;
            if (this.behindYouWasDown && Minecraft.getMinecraft().gameSettings.thirdPersonView != this.snapLookPerspective.getInt()) {
                Minecraft.getMinecraft().gameSettings.thirdPersonView = this.snapLookPerspective.getInt();
            }
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
}
