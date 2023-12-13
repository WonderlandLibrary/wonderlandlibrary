package me.earth.earthhack.impl.modules.client.fakeplayer;

import com.mojang.authlib.GameProfile;
import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.impl.util.helpers.DisablingModule;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends DisablingModule
{
    private static final FakePlayer INSTANCE = new FakePlayer();

    private EntityOtherPlayerMP fakePlayer;

    private FakePlayer()
    {
        super("FakePlayer", Category.Client);
    }

    public static FakePlayer getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void onEnable()
    {
        fakePlayer = PlayerUtil.createFakePlayerAndAddToWorld(new GameProfile(new UUID(1, 1), "FakePlayer"));
    }

    @Override
    protected void onDisable()
    {
        PlayerUtil.removeFakePlayer(fakePlayer);
        fakePlayer = null;
    }

}
