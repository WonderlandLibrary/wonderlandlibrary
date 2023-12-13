package dev.rise.util.misc.socialcreditscore;

import dev.rise.event.impl.packet.PacketReceiveEvent;
import lombok.Getter;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.S0BPacketAnimation;

import java.util.HashMap;

@Getter
public class SocialCreditScore {

    private int score;
    private boolean ownsWeapon;

    private EntityOtherPlayerMP entity;

    private final HashMap<String, EnumCrimes> crimes = new HashMap<>();

    public void handlePacketEvent(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S0BPacketAnimation) {
            final S0BPacketAnimation packetAnimation = (S0BPacketAnimation) event.getPacket();

            if (packetAnimation.getAnimationType() == 0 && packetAnimation.getEntityID() == entity.getEntityId()) {
                score -= entity.getHeldItem().getItem() instanceof ItemSword ? 50 : 10;

//                if(!crimes.containsValue(EnumCrimes.ATTACK))
            }
        }
    }

    public void handleUpdate() {
        if (!ownsWeapon) {
            final Item item = entity.getHeldItem().getItem();

            if (item instanceof ItemTool || item instanceof ItemSword) {
                ownsWeapon = true;
                score -= 100;
            }
        }
    }
}
