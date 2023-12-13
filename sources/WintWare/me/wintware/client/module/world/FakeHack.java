/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.world;

import java.util.ArrayList;
import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.combat.RotationUtil;
import me.wintware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;

public class FakeHack
extends Module {
    public static ArrayList<String> fakeHackers = new ArrayList();
    private final float yaw = 75.0f;
    private final float pitch = 0.0f;

    public FakeHack() {
        super("FakeHack", Category.World);
        Main.instance.setmgr.rSetting(new Setting("FakeAttackDistance", this, 3.0, 1.0, 7.0, false));
        Main.instance.setmgr.rSetting(new Setting("FakeSneak", this, false));
    }

    @Override
    public void onDisable() {
        for (String name : fakeHackers) {
            if (!Main.instance.setmgr.getSettingByName("FakeSneak").getValue()) continue;
            EntityPlayer player = FakeHack.mc.world.getPlayerEntityByName(name);
            player.setSneaking(false);
            player.setSprinting(false);
        }
        super.onDisable();
    }

    @Override
    public void onEnable() {
        for (int i = 0; i < 3; ++i) {
            ChatUtils.addChatMessage("To use this function write - .fakehack (nick)");
        }
        fakeHackers.clear();
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        for (String name : fakeHackers) {
            EntityPlayer player = FakeHack.mc.world.getPlayerEntityByName(name);
            if (player == null) continue;
            if (Main.instance.setmgr.getSettingByName("FakeSneak").getValue()) {
                player.setSneaking(true);
                player.setSprinting(true);
            }
            float[] rots = RotationUtil.getFacePosEntityRemote(player, Minecraft.player);
            float rc = (float)Main.instance.setmgr.getSettingByName("FakeAttackDistance").getValDouble();
            if (player.getDistanceToEntity(Minecraft.player) <= rc) {
                player.rotationYaw = rots[0];
                player.rotationYawHead = rots[0];
                player.rotationPitch = rots[1];
                if (Minecraft.player.ticksExisted % 4 == 0) {
                    player.swingArm(EnumHand.MAIN_HAND);
                    if (Minecraft.player.getDistanceToEntity(player) <= rc) {
                        Minecraft.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0f, 1.0f);
                    }
                }
            }
            if (!(Minecraft.player.getDistanceToEntity(player) > rc) || Main.instance.setmgr.getSettingByName("FakeSneak").getValue()) continue;
            player.rotationYaw = this.yaw;
            player.rotationPitch = this.pitch;
            player.rotationYawHead = this.yaw;
        }
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
        Iterator<String> iter = fakeHackers.iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            if (FakeHack.mc.world.getPlayerEntityByName(name) == null || !en.isEntityEqual(FakeHack.mc.world.getPlayerEntityByName(name))) continue;
            FakeHack.mc.world.getPlayerEntityByName(name).setSneaking(false);
            iter.remove();
        }
    }
}

