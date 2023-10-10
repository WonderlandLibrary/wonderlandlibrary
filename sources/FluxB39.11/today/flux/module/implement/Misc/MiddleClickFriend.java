package today.flux.module.implement.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.MiddleClickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.ChatUtils;

/**
 * Created by John on 2016/11/25.
 */
public class MiddleClickFriend extends Module {
    public MiddleClickFriend(){
        super("MClickFriend", Category.Misc, false);
    }

    @EventTarget
    public void onMiddleClick(MiddleClickEvent event){
        if(this.mc.objectMouseOver.entityHit instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) this.mc.objectMouseOver.entityHit;

            if(Flux.INSTANCE.getFriendManager().isFriend(player.getName())){
                Flux.INSTANCE.getFriendManager().delFriend(player.getName());

                ChatUtils.sendMessageToPlayer("Removed " + EnumChatFormatting.GOLD + player.getName() + EnumChatFormatting.RESET +  " from friends");
            }
            else{
                Flux.INSTANCE.getFriendManager().addFriend(player.getName());
                ChatUtils.sendMessageToPlayer("Added " + EnumChatFormatting.GOLD + player.getName() + EnumChatFormatting.RESET + " to friends");
            }

        }
    }
}
