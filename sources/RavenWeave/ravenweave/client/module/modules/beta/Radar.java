package ravenweave.client.module.modules.beta;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.world.AntiBot;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.RGBSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.utils.RenderUtils;
import ravenweave.client.utils.Utils;

public class Radar extends Module {

    private int x = 200, y = 0, width = 50, height = 50;
    private SliderSetting distance;
    private RGBSetting boxColor, boarderColor, playerColor, selfColor;

    public Radar() {
        super("Radar", ModuleCategory.beta); // Category: Render
        this.registerSetting(new DescriptionSetting("Shows near by opponents."));
        this.registerSetting(distance = new SliderSetting("distance", 25, 5, 100, 1));
        this.registerSetting(boxColor = new RGBSetting("box color", 0, 200, 0));
        this.registerSetting(boarderColor = new RGBSetting("boarder color", 255, 200, 255));
        this.registerSetting(playerColor = new RGBSetting("player color", 0, 0, 255));
        this.registerSetting(selfColor = new RGBSetting("self color", 255, 0, 0));

    }

    @SubscribeEvent
    public void render2D(RenderGameOverlayEvent e) {
        if (!this.enabled) return;
        if(!Utils.Player.isPlayerInGame() || (mc.currentScreen != null))
            return;
        int centreX = x + (width/2), centreY = y + (height/2);
        RenderUtils.drawBorderedRoundedRect(x, y, x + width, y + height, 5, 5, boarderColor.getRGB(), boxColor.getRGB());
        for(Entity en : mc.theWorld.playerEntities) {
        	if((en == mc.thePlayer) || AntiBot.bot(en)) continue;
            int radius = (int) mc.thePlayer.getDistanceToEntity(en);
            if(radius > distance.getInput()) continue;
            int theta = (int) Utils.Player.fovFromEntity(en) -180; //why do i need to put the 180 here huh
            int 	enX = (int) ((radius * Math.sin(Math.toRadians(theta)))*((width/2)/distance.getInput())),
            		enY = (int) ((radius * Math.cos(Math.toRadians(theta)))*((height/2)/distance.getInput()));
            Gui.drawRect((centreX + enX) -1 , (centreY + enY) - 1 , centreX + enX + 1, centreY + enY + 1, playerColor.getRGB());
        }
        Gui.drawRect(centreX - 1, centreY - 1, centreX + 1, centreY + 1, selfColor.getRGB());
    }
}
