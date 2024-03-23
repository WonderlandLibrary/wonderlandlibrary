package xyz.northclient.features.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import xyz.northclient.draggable.impl.Watermark;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.MotionEvent;
import xyz.northclient.features.events.SendPacketEvent;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.util.MoveUtil;
import xyz.northclient.util.Stopwatch;

import java.util.Random;

@ModuleInfo(name = "Flight", description = "", category = Category.MOVEMENT, keyCode = Keyboard.KEY_F)
public class Flight extends AbstractModule {
    public ModeValue speedMode = new ModeValue("Mode", this)
            .add(new Watermark.StringMode("Vulcan Glide", this))
            .add(new Watermark.StringMode("Vanilla", this))
            .setDefault("Vulcan Glide");

    public DoubleValue speed = new DoubleValue("Speed", this)
            .setDefault(1)
            .setMin(0.0)
            .setMax(10);

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventLink
    public void onMove(MotionEvent event) {
        setSuffix(speedMode.get().getName());

        switch (speedMode.get().getName()) {
            case "Vulcan Glide":
                if (mc.thePlayer.ticksExisted % 2 == 0) {
                    mc.thePlayer.motionY = -0.155;
                } else {
                    mc.thePlayer.motionY = -0.1;
                }
                break;
            case "Vanilla":
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;

                if (mc.gameSettings.keyBindJump.pressed) {
                    mc.thePlayer.motionY += speed.get().floatValue();
                } else if (mc.gameSettings.keyBindSneak.pressed) {
                    mc.thePlayer.motionY -= speed.get().floatValue();
                }

                if (MoveUtil.isMoving()) {
                    MoveUtil.strafe(speed.get().floatValue());
                }
                break;
        }

    }

    @EventLink
    public void onPacket(SendPacketEvent event) {

    }
}
