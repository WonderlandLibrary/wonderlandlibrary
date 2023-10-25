/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class FakeHack
extends Feature {
    public static ArrayList<String> fakeHackers = new ArrayList();
    private final BooleanSetting hackerSneak;
    private final BooleanSetting hackerSpin;
    private final NumberSetting hackerAttackDistance = new NumberSetting("Hacker Attack Range", 3.0f, 1.0f, 7.0f, 1.0f, () -> true);
    public float rot = 0.0f;

    public FakeHack() {
        super("FakeHack", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0441\u0434\u0435\u043b\u0430\u0442\u044c \u043b\u0435\u0433\u0438\u0442\u043d\u043e\u0433\u043e \u0438\u0433\u0440\u043e\u043a\u0430 \u0447\u0438\u0442\u0435\u0440\u043e\u043c", Type.Misc);
        this.hackerSneak = new BooleanSetting("Hacker Sneaking", false, () -> true);
        this.hackerSpin = new BooleanSetting("Hacker Spin", false, () -> true);
        this.addSettings(this.hackerAttackDistance, this.hackerSneak, this.hackerSpin);
    }

    public static boolean isFakeHacker(EntityPlayer player) {
        for (String name : fakeHackers) {
            EntityPlayer en = FakeHack.mc.world.getPlayerEntityByName(name);
            if (en == null || !player.isEntityEqual(en)) continue;
            return true;
        }
        return false;
    }

    public static void removeHacker(EntityPlayer en) {
        Iterator<String> hackers = fakeHackers.iterator();
        while (hackers.hasNext()) {
            String name = hackers.next();
            if (FakeHack.mc.world.getPlayerEntityByName(name) == null || !en.isEntityEqual(Objects.requireNonNull(FakeHack.mc.world.getPlayerEntityByName(name)))) continue;
            Objects.requireNonNull(FakeHack.mc.world.getPlayerEntityByName(name)).setSneaking(false);
            hackers.remove();
        }
    }

    @Override
    public void onDisable() {
        for (String name : fakeHackers) {
            if (!this.hackerSneak.getCurrentValue()) continue;
            EntityPlayer player = FakeHack.mc.world.getPlayerEntityByName(name);
            assert (player != null);
            player.setSneaking(false);
            player.setSprinting(false);
        }
        super.onDisable();
    }

    @Override
    public void onEnable() {
        for (int i = 0; i < 3; ++i) {
            ChatHelper.addChatMessage("To use this function write - .fakehack (nick)");
        }
        fakeHackers.clear();
        super.onEnable();
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        for (String name : fakeHackers) {
            float yaw;
            EntityPlayer player = FakeHack.mc.world.getPlayerEntityByName(name);
            if (player == null) continue;
            if (this.hackerSneak.getCurrentValue()) {
                player.setSneaking(true);
                player.setSprinting(true);
            } else {
                player.setSneaking(false);
                player.setSprinting(false);
            }
            float[] rots = RotationHelper.getFacePosEntityRemote(player, FakeHack.mc.player);
            float hackerReach = this.hackerAttackDistance.getCurrentValue();
            if (!this.hackerSpin.getCurrentValue()) {
                if (player.getDistanceToEntity(FakeHack.mc.player) <= hackerReach) {
                    player.rotationYaw = rots[0];
                    player.rotationYawHead = rots[0];
                    player.rotationPitch = rots[1];
                }
            } else {
                float yaw2;
                float speed = 30.0f;
                player.rotationYaw = yaw2 = (float)Math.floor(this.spinAim(speed));
                player.rotationYawHead = yaw2;
            }
            if (FakeHack.mc.player.ticksExisted % 4 == 0 && player.getDistanceToEntity(FakeHack.mc.player) <= hackerReach) {
                player.swingArm(EnumHand.MAIN_HAND);
                if (FakeHack.mc.player.getDistanceToEntity(player) <= hackerReach) {
                    FakeHack.mc.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0f, 1.0f);
                }
            }
            if (!(FakeHack.mc.player.getDistanceToEntity(player) > hackerReach) || this.hackerSneak.getCurrentValue() || this.hackerSpin.getCurrentValue()) continue;
            player.rotationYaw = yaw = 75.0f;
            player.rotationPitch = 0.0f;
            player.rotationYawHead = yaw;
        }
    }

    public float spinAim(float rots) {
        this.rot += rots;
        return this.rot;
    }
}

