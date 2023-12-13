package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;

public class AntiBlind extends Module {

    public AntiBlind() {
        super("AntiBlind", "Allows u to see, you absolute chinese ninja", Category.RENDER, ServerType.All);
    }

    @Subscribe
    public void onNinjaEvent(PacketEvent e){
        for(PotionEffect potion : mc.thePlayer.getActivePotionEffects()){
            if(isPotionBlackListed(potion)){
                mc.thePlayer.removePotionEffect(potion.getPotionID());
            }
        }
    }

    private boolean isPotionBlackListed(PotionEffect potion) {
        Integer[] blackListedIds = new Integer[] { Potion.blindness.getId(), Potion.confusion.getId() };

        for(int id : blackListedIds){
            if(id == potion.getPotionID()) return true;
        }
        return false;
    }
}
