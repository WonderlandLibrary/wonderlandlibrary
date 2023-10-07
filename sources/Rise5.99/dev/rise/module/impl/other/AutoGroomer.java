package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.combat.AntiBot;
import dev.rise.setting.impl.BooleanSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "AutoGroomer", description = "Grooms people for you", category = Category.OTHER)
public final class AutoGroomer extends Module {

    private final char[] chars = new char[]{
            '⛍', '⛌', '⛗', '⛗', '⛟'
    };
    private final List<Integer> ids = new ArrayList<>();
    private final List<String> messages = Arrays.asList("can I have some tittie pics?", "do you wanna be above or below?",
            "I am gonna be pounding you 24/7", "I am gonna send you something okay? no sharing :wink:", "I am fine below or above",
            "you are gonna be riding this dick all night", "oh I am creaming just looking at you", "I want to make you cum.",
            "my balls are gonna be dry tonight thanks to you", "I am gonna relieve you all night", "daddy is ready.",
            "you will be screaming my name tonight", "fly up here and you can have as much as you want",
            "lick it off like that, until I ram your mouth.", "daddy wants your mouth on all of this tonight",
            "I bet you like daddy pounding you so hard that your knees give out and drag your face forward as I literally pound you flat into the bed.");

    private final BooleanSetting chatBypass = new BooleanSetting("Chat Bypass", this, false);
    private final BooleanSetting dm = new BooleanSetting("DM Messages", this, false);
    private final BooleanSetting spam = new BooleanSetting("Spam", this, false);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (!dm.isEnabled() && mc.thePlayer.ticksExisted % 60 == 0) {
            String message = messages.get(randomInt(0, messages.size()));

            if (chatBypass.isEnabled()) {
                final StringBuilder bypass = new StringBuilder(message.length() * 2);

                for (int i = 0; i < message.length(); i++) {
                    final char character = message.charAt(i);
                    final char randomChar = chars[randomInt(0, chars.length)];

                    bypass.append(character).append(randomChar);
                }

                message = bypass.toString();
            }

            mc.thePlayer.sendChatMessage(message);

            return;
        }

        if (spam.isEnabled() && mc.thePlayer.ticksExisted % 20 != 0) return;

        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            if (player != mc.thePlayer && !player.isInvisible() && !AntiBot.bots.contains(player)
                    && player.getDistanceSqToEntity(mc.thePlayer) < 64) {

                if (!ids.contains(player.getEntityId()) || spam.isEnabled()) {
                    String message = messages.get(randomInt(0, messages.size()));

                    if (chatBypass.isEnabled()) {
                        final StringBuilder bypass = new StringBuilder(message.length() * 2);

                        for (int i = 0; i < message.length(); i++) {
                            final char character = message.charAt(i);
                            final char randomChar = chars[randomInt(0, chars.length)];

                            bypass.append(character).append(randomChar);
                        }

                        message = bypass.toString();
                    }

                    mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(
                            "/msg " + player.getName() + " " + message
                    ));

                    ids.add(player.getEntityId());
                }
            }
        }
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        ids.clear();
    }

    @Override
    protected void onDisable() {
        ids.clear();
    }

    @Override
    protected void onEnable() {
        ids.clear();
    }
}
