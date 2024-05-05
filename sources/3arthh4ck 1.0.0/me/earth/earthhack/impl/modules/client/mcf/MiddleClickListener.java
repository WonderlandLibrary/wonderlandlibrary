package me.earth.earthhack.impl.modules.client.mcf;

import com.mojang.authlib.GameProfile;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.keyboard.ClickMiddleEvent;
import me.earth.earthhack.impl.services.chat.ChatManager;
import me.earth.earthhack.impl.services.client.FriendManager;
import me.earth.earthhack.impl.util.client.ChatIDs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class MiddleClickListener extends ModuleListener<MiddleClickFriends, ClickMiddleEvent>
{
    protected MiddleClickListener(MiddleClickFriends module)
    {
        super(module, ClickMiddleEvent.class);
    }

    @Override
    public void invoke(ClickMiddleEvent event)
    {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY)
        {
            Entity entity = mc.objectMouseOver.entityHit;
            if (entity instanceof EntityPlayer)
            {
                if (FriendManager.getInstance().isFriend((EntityPlayer) entity))
                {
                    FriendManager.getInstance().removeFriend(entity);
                    ChatManager.getInstance().sendDeleteMessage(
                            TextColor.RED + entity.getName() + " unfriended.", entity.getName(), ChatIDs.FRIEND);
                }
                else
                {
                    GameProfile profile = ((EntityPlayer) entity).getGameProfile();
                    FriendManager.getInstance().addFriend(profile.getName(), profile.getId());
                    ChatManager.getInstance().sendDeleteMessage(
                            TextColor.AQUA + entity.getName() + "friended.", entity.getName(), ChatIDs.FRIEND);
                }
            }
        }
    }

}
