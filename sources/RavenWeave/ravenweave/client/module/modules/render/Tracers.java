package ravenweave.client.module.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.client.Targets;
import ravenweave.client.module.setting.impl.RGBSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

import java.awt.*;
import java.util.Iterator;

public class Tracers extends Module {
    public static RGBSetting color;
    public static TickSetting rainbow, redshift, showInvalidTargets, showInvis;
    public static SliderSetting lineWidth, distance;
    private boolean g;

    public Tracers() {
        super("Tracers", ModuleCategory.render);
        this.registerSetting(showInvis = new TickSetting("Show invis", true));
        this.registerSetting(lineWidth = new SliderSetting("Line Width", 1.0D, 1.0D, 5.0D, 1.0D));
        this.registerSetting(distance = new SliderSetting("Distance", 1.0D, 1.0D, 512.0D, 1.0D));
        this.registerSetting(color = new RGBSetting("Color", 0, 255, 0));
        this.registerSetting(rainbow = new TickSetting("Rainbow", false));
        this.registerSetting(redshift = new TickSetting("Redshift w distance", false));
        this.registerSetting(showInvalidTargets = new TickSetting("Show invalid targets", false));
    }

    @Override
    public void onEnable() {
        this.g = mc.gameSettings.viewBobbing;
        if (this.g)
            mc.gameSettings.viewBobbing = false;

    }

    @Override
    public void onDisable() {
        mc.gameSettings.viewBobbing = this.g;
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (mc.gameSettings.viewBobbing)
            mc.gameSettings.viewBobbing = false;
    }

    @SubscribeEvent
    public void onRender(RenderWorldEvent event) {
        if (!this.enabled) return;
        if (Utils.Player.isPlayerInGame()) {
            int rgb = rainbow.isToggled() ? Utils.Client.rainbowDraw(2L, 0L) : this.color.getRGB();
            Iterator<EntityPlayer> players = mc.theWorld.playerEntities.iterator();

            for (EntityPlayer player: mc.theWorld.playerEntities) {
                // Hide invisibles
                if (player.isInvisible() && !showInvis.isToggled()) continue;

                // Hide dead players
                if (player.deathTime != 0) continue;

                // Don't draw ourselves
                if (player == mc.thePlayer) continue;

                // Hide invalid targets
                if (!Targets.isValidTarget(player) && !showInvalidTargets.isToggled()) continue;
                if (redshift.isToggled() && (mc.thePlayer.getDistanceToEntity(player) < 25)) {
                    int red = (int) (Math.abs(mc.thePlayer.getDistanceToEntity(player) - 25) * 10);
                    int green = Math.abs(red - 255);
                    rgb = new Color(red, green, this.color.getBlue()).getRGB();
                }
                Utils.HUD.dtl(player, rgb, (float) lineWidth.getInput());
            }
        }
    }
}