package io.github.raze.modules.collection.misc;

import com.mojang.authlib.GameProfile;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.WorldSettings;

import java.util.UUID;

public class FakePlayer extends AbstractModule {

    public FakePlayer() {
        super("FakePlayer", "Spawns in a fake player.", ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        if(mc.thePlayer == null || mc.thePlayer.isDead)
            setEnabled(false);

        EntityOtherPlayerMP clonedPlayer = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.fromString("2d8cbeeb-f6c3-422f-aa67-85e4a67e891f"), mc.session.getUsername()));
        clonedPlayer.copyLocationAndAnglesFrom(mc.thePlayer);
        clonedPlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
        clonedPlayer.rotationYaw = mc.thePlayer.rotationYaw;
        clonedPlayer.rotationPitch = mc.thePlayer.rotationPitch;
        clonedPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
        clonedPlayer.setHealth(20);
        mc.theWorld.addEntityToWorld(-6969, clonedPlayer);
        clonedPlayer.onLivingUpdate();
    }

    @Override
    public void onDisable() {
        if (mc.theWorld != null)
            mc.theWorld.removeEntityFromWorld(-6969);
    }

}
