/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import net.minecraft.network.play.client.C01PacketChatMessage;

@ModuleInfo(name = "ChatBypass", description = "Bypasses chat filters", category = Category.OTHER)
public final class ChatBypass extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Hypixel");

    private final char[] chars = new char[]{
            '͸', '͹', 'Ϳ', '΀', '΁', '΂', '΃', '΋', '΍', '΢', 'Ԥ', 'ԥ', 'Ԧ', 'ԧ', 'Ԩ', 'ԩ', 'Ԫ', 'ԫ', 'Ԭ', 'ԭ', 'Ԯ', 'ԯ', '԰', '՗', '՘', 'ՠ', 'ֈ', '֋', '֌', '֍', '֎', '֏', '֐', '׈', '׉', '׊', '׋', '׌', '׍', '׎', '׏', '׫', '׬', '׭', '׮', 'ׯ', '׵', '׶', '׷', '׸', '׹', '׺', '׻', '׼', '׽', '׾', '׿', '؄', '؅', '؜', '؝', 'ؠ', 'ٟ', '܎', '݋', '݌', '޲', '޳', '޴', '޵', '޶', '޷', '޸', '޹', '޺', '޻', '޼', '޽', '޾', '޿', '߻', '߼', '߽', '߾', '߿', 'ࠀ', 'ࠁ', 'ࠂ', 'ࠃ', 'ࠄ', 'ࠅ', 'ࠆ', 'ࠇ', 'ࠈ', 'ࠉ', 'ࠊ', 'ࠋ', 'ࠌ', 'ࠍ', 'ࠎ', 'ࠏ', 'ࠐ', 'ࠑ', 'ࠒ', 'ࠓ', 'ࠔ', 'ࠕ', 'ࠖ', 'ࠗ', '࠘', '࠙', 'ࠚ', 'ࠛ', 'ࠜ', 'ࠝ', 'ࠞ', 'ࠟ', 'ࠠ', 'ࠡ', 'ࠢ', 'ࠣ', 'ࠤ', 'ࠥ', 'ࠦ', 'ࠧ', 'ࠨ', 'ࠩ', 'ࠪ', 'ࠫ', 'ࠬ', '࠭', '࠮', '࠯', '࠰', '࠱', '࠲', '࠳', '࠴', '࠵', '࠶', '࠷', '࠸', '࠹', '࠺', '࠻', '࠼', '࠽', '࠾', '࠿', 'ࡀ', 'ࡁ', 'ࡂ', 'ࡃ', 'ࡄ', 'ࡅ', 'ࡆ', 'ࡇ', 'ࡈ', 'ࡉ', 'ࡊ', 'ࡋ', 'ࡌ', 'ࡍ', 'ࡎ', 'ࡏ', 'ࡐ', 'ࡑ', 'ࡒ', 'ࡓ', 'ࡔ', 'ࡕ', 'ࡖ', 'ࡗ', 'ࡘ', '࡙', '࡚', '࡛', '࡜', '࡝', '࡞', '࡟', 'ࡠ', 'ࡡ', 'ࡢ', 'ࡣ', 'ࡤ', 'ࡥ', 'ࡦ', 'ࡧ', 'ࡨ', 'ࡩ', 'ࡪ', '࡫', '࡬', '࡭', '࡮', '࡯', 'ࡰ', 'ࡱ', 'ࡲ', 'ࡳ', 'ࡴ', 'ࡵ', 'ࡶ', 'ࡷ', 'ࡸ', 'ࡹ', 'ࡺ', 'ࡻ', 'ࡼ', 'ࡽ', 'ࡾ', 'ࡿ', 'ࢀ', 'ࢁ', 'ࢂ', 'ࢃ', 'ࢄ', 'ࢅ', 'ࢆ', 'ࢇ', '࢈', 'ࢉ', 'ࢊ', 'ࢋ', 'ࢌ', 'ࢍ', 'ࢎ', '࢏', '࢐', '࢑', '࢒', '࢓', '࢔', '࢕', '࢖', 'ࢗ', '࢘', '࢙', '࢚', '࢛', '࢜', '࢝', '࢞', '࢟', 'ࢠ', 'ࢡ', 'ࢢ', 'ࢣ', 'ࢤ', 'ࢥ', 'ࢦ', 'ࢧ', 'ࢨ', 'ࢩ', 'ࢪ', 'ࢫ', 'ࢬ', 'ࢭ', 'ࢮ', 'ࢯ', 'ࢰ', 'ࢱ', 'ࢲ', 'ࢳ', 'ࢴ', 'ࢵ', 'ࢶ', 'ࢷ', 'ࢸ', 'ࢹ', 'ࢺ', 'ࢻ', 'ࢼ', 'ࢽ', 'ࢾ', 'ࢿ', 'ࣀ', 'ࣁ', 'ࣂ', 'ࣃ', 'ࣄ', 'ࣅ', 'ࣆ', 'ࣇ', 'ࣈ', 'ࣉ', '࣊', '࣋', '࣌', '࣍', '࣎', '࣏', '࣐', '࣑', '࣒', '࣓', 'ࣔ', 'ࣕ', 'ࣖ', 'ࣗ', 'ࣘ', 'ࣙ', 'ࣚ', 'ࣛ', 'ࣜ', 'ࣝ', 'ࣞ', 'ࣟ', '࣠', '࣡', '࣢', 'ࣣ', 'ࣤ', 'ࣥ', 'ࣦ', 'ࣧ', 'ࣨ', 'ࣩ', '࣪', '࣫', '࣬', '࣭', '࣮', '࣯', 'ࣰ', 'ࣱ', 'ࣲ', 'ࣳ', 'ࣴ', 'ࣵ', 'ࣶ', 'ࣷ', 'ࣸ', 'ࣹ', 'ࣺ', 'ࣻ', 'ࣼ', 'ࣽ', 'ࣾ', 'ࣿ', 'ऀ', 'ऺ', 'ऻ', 'ॎ', 'ॏ', 'ॕ', 'ॖ', 'ॗ', 'ॳ', 'ॴ', 'ॵ', 'ॶ', 'ॷ', 'ॸ', 'ॹ', 'ॺ', 'ঀ', '঄', '঍', '঎', '঑', '঒', '঩', '঱', '঳', '঴', '঵', '঺', '঻', '৅', '৆', '৉', '৊', '৏', '৐', '৑', '৒', '৓', '৔', '৕', '৖', '৘', '৙', '৚', '৛', '৞', '৤', '৥', '৻', 'ৼ', '৽', '৾', '৿', '਀', '਄', '਋', '਌', '਍', '਎', '਑', '਒', '਩', '਱', '਴', '਷', '਺', '਻', '਽', '੃', '੄', '੅', '੆', '੉', '੊', '੎', '੏'
    };

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            final C01PacketChatMessage packet = ((C01PacketChatMessage) event.getPacket());
            final String message = packet.getMessage();

            if (!message.startsWith("/")) {
                final StringBuilder bypass = new StringBuilder(message.length() * 2);

                for (int i = 0; i < message.length(); i++) {
                    final char character = message.charAt(i);
                    char randomChar = chars[randomInt(0, chars.length)];

                    if (mode.is("Hypixel"))
                        randomChar = 'ˌ';

                    bypass.append(character).append(randomChar);
                }

                packet.setMessage(bypass.toString());
                event.setPacket(packet);
            }
        }
    }
}
