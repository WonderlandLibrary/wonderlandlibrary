// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class FakePlayer extends Module
{
    public FakePlayer() {
        super("FakePlayer", "Spawns a fake player", 0, Category.MISC, true);
    }
    
    @Override
    public void onEnable() {
        if (FakePlayer.mc.world == null) {
            return;
        }
        final EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03"), "jared2013"));
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        FakePlayer.mc.world.addEntityToWorld(-100, (Entity)fakePlayer);
    }
    
    @Override
    public void onDisable() {
        FakePlayer.mc.world.removeEntityFromWorld(-100);
    }
}
