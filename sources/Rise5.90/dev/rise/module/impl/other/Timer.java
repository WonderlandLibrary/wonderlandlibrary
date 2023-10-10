/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.font.CustomFont;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.login.server.S00PacketDisconnect;

import java.awt.*;

@ModuleInfo(name = "Timer", description = "Changes the game tick speed", category = Category.OTHER)
public final class Timer extends Module {

    private final NumberSetting timerSpeed = new NumberSetting("Timer Speed", this, 1.0, 0.1, 10.0, 0.1);
    private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "MMC");

    private long balance;
    private boolean sentMessage;
    private long lastPreMotion;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mode.is("Vanilla"))
            mc.timer.timerSpeed = (float) timerSpeed.getValue();
        else {

            final long lastPreMotion = this.lastPreMotion;
            this.lastPreMotion = System.currentTimeMillis();

            if (lastPreMotion != 0) {
                final long difference = System.currentTimeMillis() - lastPreMotion;

                balance += 50;
                balance -= difference;
            }

            if (balance < -1000) {
                //Allow timer :)

                mc.timer.timerSpeed = (float) timerSpeed.getValue();


            } else {
                if (timerSpeed.getValue() > 1 && !sentMessage) {
                    Rise.addChatMessage("Please set timer speed value below 1 to decrease balance");
                    sentMessage = true;

                }
                mc.timer.timerSpeed = 1;
            }

            if (timerSpeed.getValue() < 1) {
                mc.timer.timerSpeed = (float) timerSpeed.getValue();
            }
        }
    }

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        balance = 0;
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S00PacketDisconnect) {
            balance = 0;
            sentMessage = false;
            lastPreMotion = 0;
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        final ScaledResolution sr = new ScaledResolution(mc);

        if (mode.is("MMC"))
            CustomFont.drawCenteredString("Balance: " + balance, sr.getScaledWidth() / 2d, sr.getScaledHeight() - 60, Color.WHITE.getRGB());
    }
}
