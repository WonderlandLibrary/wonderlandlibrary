package dev.rise.module.impl.ghost;

import dev.rise.Rise;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

/**
 * @author Hazsi
 */
@ModuleInfo(name = "Freelook", description = "Allows you to look around in third person", category = Category.LEGIT)
public class Freelook extends Module {

    public BooleanSetting invertPitch = new BooleanSetting("Invert Pitch", this, false);

    private int previousPerspective;
    public float originalYaw, originalPitch, lastYaw, lastPitch;

    public void onEnable() {
        previousPerspective = mc.gameSettings.thirdPersonView;
        originalYaw = lastYaw = mc.thePlayer.rotationYaw;
        originalPitch = lastPitch = mc.thePlayer.rotationPitch;

        if (invertPitch.isEnabled()) lastPitch *= -1;
    }

    public void onDisable() {
        mc.thePlayer.rotationYaw = originalYaw;
        mc.thePlayer.rotationPitch = originalPitch;
        mc.gameSettings.thirdPersonView = previousPerspective;
    }

    public void onRender2DEvent(final Render2DEvent event) {
        if (this.getKeyBind() == Keyboard.KEY_NONE || !Keyboard.isKeyDown(this.getKeyBind())) {
            toggleModule();
            return;
        }

        this.mc.mouseHelper.mouseXYChange();
        final float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float f1 = (float) (f * f * f * 1.5);
        lastYaw += (float) (this.mc.mouseHelper.deltaX * f1);
        lastPitch -= (float) (this.mc.mouseHelper.deltaY * f1);

        lastPitch = MathHelper.clamp_float(lastPitch, -90, 90);
        mc.gameSettings.thirdPersonView = 1;
    }

    public boolean handleInput() {
        return Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freelook")).isEnabled();
    }

    // Can you believe Vape doesn't have this??
    // No, I'm serious, you ban if you flag while in perspective/freelook.
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook wrapper = (S08PacketPlayerPosLook) event.getPacket();

            originalYaw = wrapper.getYaw();
            originalPitch = wrapper.getPitch();
        }
    }
}
