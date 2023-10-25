package org.dreamcore.client.feature.impl.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.RotationHelper;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Iterator;

public class FakeHack extends Feature {

    public static ArrayList<String> fakeHackers = new ArrayList<String>();
    private final BooleanSetting hackerSneak;
    private final BooleanSetting hackerSpin;
    private final NumberSetting hackerAttackDistance;
    public float rot = 0;

    public FakeHack() {
        super("FakeHack", "Позволяет сделать легитного игрока читером", Type.Misc);
        hackerAttackDistance = new NumberSetting("Hacker Attack Range", 3, 1, 7, 1, () -> true);
        hackerSneak = new BooleanSetting("Hacker Sneaking", false, () -> true);
        hackerSpin = new BooleanSetting("Hacker Spin", false, () -> true);
        addSettings(hackerAttackDistance, hackerSneak, hackerSpin);
    }

    public static boolean isFakeHacker(EntityPlayer player) {
        for (String name : fakeHackers) {
            EntityPlayer en = mc.world.getPlayerEntityByName(name);
            if (en == null) {
                return false;
            }
            if (player.isEntityEqual(en)) {
                return true;
            }
        }
        return false;
    }

    public static void removeHacker(EntityPlayer entityPlayer) {
        Iterator<String> hackers = fakeHackers.iterator();
        while (hackers.hasNext()) {
            String name = hackers.next();
            if (mc.world.getPlayerEntityByName(name) == null) {
                continue;
            }
            if (entityPlayer.isEntityEqual(mc.world.getPlayerEntityByName(name))) {
                mc.world.getPlayerEntityByName(name).setSneaking(false);
                hackers.remove();
            }
        }
    }

    @Override
    public void onDisable() {
        for (String name : fakeHackers) {
            if (hackerSneak.getBoolValue()) {
                EntityPlayer player = mc.world.getPlayerEntityByName(name);
                assert player != null;
                player.setSneaking(false);
                player.setSprinting(false);
            }
        }
        super.onDisable();
    }

    @Override
    public void onEnable() {
        ChatHelper.addChatMessage("To use this function write - " + ".fakehack add (nick)");
        fakeHackers.clear();
        super.onEnable();
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        for (String name : fakeHackers) {
            EntityPlayer player = mc.world.getPlayerEntityByName(name);
            if (player == null) {
                return;
            }
            if (hackerSneak.getBoolValue()) {
                player.setSneaking(true);
                player.setSprinting(true);
            } else {
                player.setSneaking(false);
                player.setSprinting(false);
            }
            float[] rots = RotationHelper.getFacePosRemote(player, mc.player, true);
            float hackerReach = hackerAttackDistance.getNumberValue();
            if (!hackerSpin.getBoolValue()) {
                if (player.getDistanceToEntity(mc.player) <= hackerReach) {
                    player.rotationYaw = rots[0];
                    player.rotationYawHead = rots[0];
                    player.rotationPitch = rots[1];
                }
            } else {
                float speed = 30;
                float yaw = ((float) (Math.floor(spinAim(speed))));
                player.rotationYaw = yaw;
                player.rotationYawHead = yaw;
            }
            if (mc.player.ticksExisted % 4 == 0 && player.getDistanceToEntity(mc.player) <= hackerReach) {
                player.swingArm(EnumHand.MAIN_HAND);
                if (mc.player.getDistanceToEntity(player) <= hackerReach) {
                    mc.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0F, 1.0F);
                }
            }
            if (mc.player.getDistanceToEntity(player) > hackerReach && !hackerSneak.getBoolValue() && !hackerSpin.getBoolValue()) {
                float yaw = 75;
                player.rotationYaw = yaw;
                player.rotationPitch = 0;
                player.rotationYawHead = yaw;
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
