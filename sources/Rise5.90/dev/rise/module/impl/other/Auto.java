/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;

@ModuleInfo(name = "Auto", description = "Automatically does things for you", category = Category.OTHER)
public final class Auto extends Module {

    private final BooleanSetting redeskyAutoCaptcha = new BooleanSetting("Redesky Auto Captcha", this, false);
    private final BooleanSetting redeskyAutoRegister = new BooleanSetting("Redesky Auto Register", this, false);

    boolean sentReg, sentLogin;
    int ticks;
    int amountFlagged;

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {

        final Packet packet = event.getPacket();

        if (redeskyAutoCaptcha.isEnabled()) {

            if (packet instanceof S2FPacketSetSlot) {

                final S2FPacketSetSlot S2F = (S2FPacketSetSlot) packet;

                if (S2F.func_149175_c() == 0) {
                    return;
                }

                final ItemStack item = S2F.func_149174_e();

                if (item != null && item.getDisplayName().toLowerCase().contains("aqui")) {

                    mc.thePlayer.sendQueue.addToSendQueue(new C0EPacketClickWindow(S2F.func_149175_c(), S2F.func_149173_d(), 0, 0, item, (short) 1919));

                }

            }

        }

    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {

        if (mc.thePlayer.ticksExisted % 20 == 0)
            amountFlagged--;

        if (amountFlagged < 0)
            amountFlagged = 0;

        if (redeskyAutoRegister.isEnabled()) {

            if (mc.thePlayer.ticksExisted == 1) {
                sentLogin = sentReg = false;
            }

            if (mc.currentScreen == null) {
                ticks++;
                if (ticks > 30) {

                    if (!sentReg) {
                        mc.thePlayer.sendChatMessage("/register Lolxd00 Lolxd00");
                        sentReg = true;
                    } else if (!sentLogin) {
                        mc.thePlayer.sendChatMessage("/login Lolxd00");
                        sentLogin = true;
                    }

                }


            } else {
                ticks = 0;
            }

        }

    }


}

