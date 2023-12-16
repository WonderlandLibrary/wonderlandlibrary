package wtf.expensive.modules.impl.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CChatMessagePacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.ClientUtil;

import java.awt.*;

@FunctionAnnotation(name = "AutoLeave", type = Type.Util)
public class AutoLeave extends Function {

    public SliderSetting range = new SliderSetting("Дистанция", 15, 5, 40, 1);
    public ModeSetting mode = new ModeSetting("Что делать?", "/spawn", "/spawn", "/hub", "kick");
    public BooleanOption health = new BooleanOption("По здоровью", false);
    public SliderSetting healthSlider = new SliderSetting("Здоровье", 10, 5, 20, 1).setVisible(() -> health.get());

    public AutoLeave() {
        addSettings(range, mode, health, healthSlider);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion e) {
            if (health.get()) {
                if (mc.player.getHealth() <= healthSlider.getValue().floatValue()) {
                    if (mode.is("kick")) {
                        mc.player.connection.getNetworkManager().closeChannel(ClientUtil.gradient("Вы вышли с сервера! \n" +" Мало хп!", new Color(121, 208, 255).getRGB(), new Color(96, 133, 255).getRGB()));
                    } else {
                        mc.player.connection.sendPacket(new CChatMessagePacket(mode.get()));
                    }
                }
                setState(false);
                return;
            }

            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player == mc.player) continue;
                if (player.isBot) continue;
                if (Managment.FRIEND_MANAGER.isFriend(player.getGameProfile().getName())) {
                    continue;
                }

                if (mc.player.getDistance(player) <= range.getValue().floatValue()) {
                    if (mode.is("kick")) {
                        mc.player.connection.getNetworkManager().closeChannel(ClientUtil.gradient("Вы вышли с сервера! \n" + player.getGameProfile().getName(), new Color(121, 208, 255).getRGB(), new Color(96, 133, 255).getRGB()));
                    } else {
                        mc.player.connection.sendPacket(new CChatMessagePacket(mode.get()));
                    }
                    setState(false);
                    break;
                }
            }
        }
    }
}
