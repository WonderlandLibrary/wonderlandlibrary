package vestige.anticheat.impl;

import net.minecraft.entity.player.EntityPlayer;
import vestige.Vestige;
import vestige.anticheat.ACPlayer;
import vestige.anticheat.AnticheatCheck;
import vestige.anticheat.PlayerData;
import vestige.module.impl.combat.Antibot;
import vestige.util.misc.LogUtil;

public class BotCheck extends AnticheatCheck {

    private double buffer;

    public BotCheck(ACPlayer player) {
        super("BotCheck", player);
    }

    @Override
    public void check() {
        PlayerData data = player.getData();

        int ticksExisted = player.getEntity().ticksExisted;

        boolean lowDistance = mc.thePlayer.getDistanceToEntity(player.getEntity()) < 20;

        boolean failedSpeed = ticksExisted >= 15 && data.getDist() > (ticksExisted < 100 && mc.thePlayer.ticksExisted > 500 ? 0.7 : 2) && lowDistance;
        boolean failedFly = ticksExisted >= 15 && !data.isCloseToGround() && data.getCloseToGroundTicks() >= 6 && data.getMotionY() == 0 || data.getLastMotionY() == 0 && lowDistance;

        if(failedSpeed) {
            EntityPlayer entity = player.getEntity();

            if(++buffer >= (entity.isInvisible() || entity.isInvisibleToPlayer(mc.thePlayer) ? 2 : 4) && !player.isBot()) {
                player.setBot(true);

                Antibot antibotModule = Vestige.instance.getModuleManager().getModule(Antibot.class);

                if(antibotModule.isEnabled() && antibotModule.advancedDetection.isEnabled() && antibotModule.debug.isEnabled()) {
                    LogUtil.addChatMessage("Detected bot : " + entity.getGameProfile().getName());
                    LogUtil.addChatMessage("Pos Y : " + mc.thePlayer.posY + " | Bot pos Y : " + data.getY());
                }
            }
        } else {
            buffer -= 0.015;

            if(buffer < 0) {
                buffer = 0;
            }
        }

        if(failedFly) {

        }
    }
}
