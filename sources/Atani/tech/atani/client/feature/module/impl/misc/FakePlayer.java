package tech.atani.client.feature.module.impl.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.WorldSettings;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;

import java.util.UUID;

@ModuleData(name = "FakePlayer", description = "Spawns in a fake player", category = Category.MISCELLANEOUS)
public class FakePlayer extends Module {

    @Override
    public void onEnable() {
        if(Methods.mc.thePlayer == null || Methods.mc.thePlayer.isDead){
            setEnabled(false);
        }

        EntityOtherPlayerMP clonedPlayer = new EntityOtherPlayerMP(Methods.mc.theWorld, new GameProfile(UUID.fromString("9b7f28c2-98ea-4d70-b2db-48e6c78a4a9d"), Methods.mc.session.getUsername()));
        clonedPlayer.copyLocationAndAnglesFrom(Methods.mc.thePlayer);
        clonedPlayer.rotationYawHead = Methods.mc.thePlayer.rotationYawHead;
        clonedPlayer.rotationYaw = Methods.mc.thePlayer.rotationYaw;
        clonedPlayer.rotationPitch = Methods.mc.thePlayer.rotationPitch;
        clonedPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
        clonedPlayer.setHealth(20);
        Methods.mc.theWorld.addEntityToWorld(-4200, clonedPlayer);
        clonedPlayer.onLivingUpdate();
    }

    @Override
    public void onDisable() {
        if (Methods.mc.theWorld != null) {
            Methods.mc.theWorld.removeEntityFromWorld(-4200);
        }
    }

}