package ravenweave.client.module.modules.beta;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.event.ext.EventDirection;
import ravenweave.client.event.impl.PacketEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;

public class LongJump extends Module {
    public boolean hasJumped = false, shouldJump = false;
    public static SliderSetting speed;
    public static TickSetting autoDisable;

    public LongJump() {
        super("LongJump", ModuleCategory.beta); // Category: Movement
        this.registerSetting(new DescriptionSetting("Makes your jump longer."));
        this.registerSetting(speed = new SliderSetting("Speed:", 5, 1, 10, 0.25));
        this.registerSetting(autoDisable = new TickSetting("Auto Disable", true));
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.getDirection() == EventDirection.OUTGOING) return;
        if (!(event.getPacket() instanceof S12PacketEntityVelocity)) return;
        if (((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId())
            this.shouldJump = true;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (this.shouldJump) {
            if (!this.hasJumped) {
                this.hasJumped = true;
                if (Module.mc.thePlayer.onGround) {
                    Module.mc.thePlayer.jump();
                }
            } else {
                if (Module.mc.thePlayer.onGround || Module.mc.thePlayer.isCollidedHorizontally) {
                    this.shouldJump = false;
                    this.hasJumped = false;
                    return;
                }

                double s = 1.94D * (speed.getInput() / 4);
                double r = Math.toRadians(Module.mc.thePlayer.rotationYaw + 90.0F);
                Module.mc.thePlayer.motionX = s * Math.cos(r);
                Module.mc.thePlayer.motionZ = s * Math.sin(r);
                if (autoDisable.isToggled()) {
                    this.disable();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.shouldJump = false;
        this.hasJumped = false;
        super.onDisable();
    }
}
