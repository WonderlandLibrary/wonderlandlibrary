// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import moonsense.enums.ModuleCategory;
import org.lwjgl.opengl.Display;
import net.minecraft.client.Minecraft;
import moonsense.config.ModuleConfig;
import moonsense.utils.KeyBinding;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class FreelookModule extends SCModule
{
    public static FreelookModule INSTANCE;
    public final Setting freelook;
    private final Setting freelookToggle;
    private final Setting freelookPerspective;
    public final Setting invertX;
    public final Setting invertY;
    public float cameraYaw;
    public float cameraPitch;
    private int previousView;
    private boolean wasDown;
    private int behindYouPrevView;
    private boolean behindYouWasDown;
    
    public FreelookModule() {
        super("Freelook", "Unlock your camera and look around you.", 22);
        new Setting(this, "Mouse Options");
        this.invertX = new Setting(this, "Invert X").setDefault(false);
        this.invertY = new Setting(this, "Invert Y").setDefault(false);
        new Setting(this, "Freelook Options");
        this.freelook = new Setting(this, "Keybind", "freelook.keybind").setDefault(new KeyBinding(56));
        this.freelookToggle = new Setting(this, "Toggle", "freelook.toggle").setDefault(false);
        this.freelookPerspective = new Setting(this, "Perspective", "freelook.perspective").setDefault(1).setRange("First Person", "Third Person Back", "Third Person Front");
        FreelookModule.INSTANCE = this;
    }
    
    public boolean isHeld() {
        final KeyBinding keyBinding = (KeyBinding)this.freelook.getObject();
        final boolean active = keyBinding.isKeyDown();
        if (ModuleConfig.INSTANCE.isEnabled(FreelookModule.INSTANCE)) {
            if (!active) {
                this.cameraYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                this.cameraPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
            }
            if (this.wasDown != active) {
                if (!(this.wasDown = active)) {
                    Minecraft.getMinecraft().gameSettings.thirdPersonView = this.previousView;
                }
                this.previousView = Minecraft.getMinecraft().gameSettings.thirdPersonView;
                if (this.wasDown && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)) {
                    Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
                }
            }
            return active;
        }
        return false;
    }
    
    public static boolean overrideMouse() {
        if (Minecraft.getMinecraft().inGameHasFocus && Display.isActive()) {
            if (!FreelookModule.INSTANCE.isHeld()) {
                return true;
            }
            Minecraft.getMinecraft().mouseHelper.mouseXYChange();
            final float f1 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
            final float f2 = f1 * f1 * f1 * 8.0f;
            final float f3 = Minecraft.getMinecraft().mouseHelper.deltaX * f2;
            final float f4 = Minecraft.getMinecraft().mouseHelper.deltaY * f2;
            final FreelookModule instance2;
            final FreelookModule instance = instance2 = FreelookModule.INSTANCE;
            instance2.cameraYaw += f3 * 0.15f;
            final FreelookModule freelookModule = instance;
            freelookModule.cameraPitch += f4 * 0.15f;
            if (FreelookModule.INSTANCE.cameraPitch > 90.0f) {
                FreelookModule.INSTANCE.cameraPitch = 90.0f;
            }
            if (FreelookModule.INSTANCE.cameraPitch < -90.0f) {
                FreelookModule.INSTANCE.cameraPitch = -90.0f;
            }
            Minecraft.getMinecraft().renderGlobal.func_174979_m();
        }
        return false;
    }
    
    public static float getCameraYaw() {
        boolean flag = Minecraft.getMinecraft().gameSettings.thirdPersonView == 2;
        flag = false;
        if (ModuleConfig.INSTANCE.isEnabled(FreelookModule.INSTANCE)) {
            return FreelookModule.INSTANCE.isHeld() ? FreelookModule.INSTANCE.cameraYaw : (flag ? (-Minecraft.getMinecraft().thePlayer.rotationYaw) : Minecraft.getMinecraft().thePlayer.rotationYaw);
        }
        if (flag) {
            return -Minecraft.getMinecraft().thePlayer.rotationYaw;
        }
        return Minecraft.getMinecraft().thePlayer.rotationYaw;
    }
    
    public static float getCameraPitch() {
        boolean flag = Minecraft.getMinecraft().gameSettings.thirdPersonView == 2;
        flag = false;
        if (ModuleConfig.INSTANCE.isEnabled(FreelookModule.INSTANCE)) {
            return FreelookModule.INSTANCE.isHeld() ? FreelookModule.INSTANCE.cameraPitch : (flag ? (-Minecraft.getMinecraft().thePlayer.rotationPitch) : Minecraft.getMinecraft().thePlayer.rotationPitch);
        }
        if (flag) {
            return -Minecraft.getMinecraft().thePlayer.rotationPitch;
        }
        return Minecraft.getMinecraft().thePlayer.rotationPitch;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
}
