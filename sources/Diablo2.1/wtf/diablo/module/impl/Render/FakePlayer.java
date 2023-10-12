
package wtf.diablo.module.impl.Render;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;

import java.util.UUID;

public class FakePlayer extends Module {
    public FakePlayer() {
        super("Fake Player", "Renders a box around players", Category.RENDER, ServerType.All);
    }

    @Override
    public void onEnable() {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.fromString("4f7700aa-93d0-4c6a-b58a-d99b1c7287fd"), mc.getSession().getUsername()));
        fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
        mc.theWorld.addEntityToWorld(69420, fakePlayer);
    }

    @Override
    public void onDisable() {
        mc.theWorld.removeEntityFromWorld(69420);
    }
}