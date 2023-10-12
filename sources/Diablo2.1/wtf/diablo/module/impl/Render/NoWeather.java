package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.input.Keyboard;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;

@Getter@Setter
public class NoWeather extends Module {
    public NoWeather(){
        super("NoWeather", "Remove all weather from your game", Category.RENDER, ServerType.All);
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        if(mc.thePlayer == null) return;
        WorldInfo worldinfo = mc.theWorld.getWorldInfo();
        worldinfo.setCleanWeatherTime(0);
        worldinfo.setRainTime(0);
        worldinfo.setThunderTime(0);
        worldinfo.setRaining(false);
        worldinfo.setThundering(false);
        
        if(e.getPacket() instanceof S2BPacketChangeGameState){
            e.setCanceled(true);
        }
    }
}
