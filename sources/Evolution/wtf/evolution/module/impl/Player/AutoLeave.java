package wtf.evolution.module.impl.Player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "AutoLeave", type = Category.Player)
public class AutoLeave extends Module {

    private final ModeSetting mode =
            new ModeSetting("Mode", "/spawn", "/spawn", "/home", "/hub", "/lobby").call(this);
    private final SliderSetting radius = new SliderSetting("Radius", 30, 1, 100, 5).call(this);

    private final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true);

    @EventTarget
    public void onUpdate(EventMotion e) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == mc.player || ignoreFriends.get() && Main.f.friends.contains(player.getName()))
                continue;

            double distance = mc.player.getDistance(player);
            if (distance <= radius.get()) {
                ChatUtil.print(ChatFormatting.YELLOW + "[AutoLeave]: " + ChatFormatting.WHITE + "замечен игрок " +
                        ChatFormatting.GREEN + player.getName() + ChatFormatting.WHITE + " в радиусе " +
                        ChatFormatting.GREEN + MathHelper.round(distance, 1));
                mc.player.sendChatMessage(mode.get());
                toggle();
            }
        }
    }

}