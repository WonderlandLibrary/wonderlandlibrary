package today.flux.gui.hud.window.impl;

import net.minecraft.entity.player.EntityPlayer;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.hud.window.HudWindow;
import today.flux.module.implement.Render.HudWindowMod;
import today.flux.module.value.FloatValue;
import today.flux.utility.FriendManager;

import java.awt.*;

public class WindowRadar extends HudWindow {

    public FloatValue scale = new FloatValue("HudWindow", "Radar Scale", 2f, 0f, 20f, 0.1f);

    public WindowRadar() {
        super("Radar", 5, 25, 100, 100, "Radar", "", 12, 1, .5f, true, 100, 100);
    }

    @Override
    public void draw() {
        super.draw();

        float xOffset = x;
        float yOffset = y + draggableHeight;
        float playerOffsetX = (float) mc.thePlayer.posX;
        float playerOffSetZ = (float) mc.thePlayer.posZ;

        RenderUtil.drawRect(xOffset + ((width / 2f) - 0.5f), yOffset + 3.5f, xOffset + (width / 2f) + 0.5f, (yOffset + height) - 3.5f, new Color(255,255,255,80).getRGB());
        RenderUtil.drawRect(xOffset + 3.5f, yOffset + ((height / 2f) - 0.5f), (xOffset + width) - 3.5f, yOffset + (height / 2) + 0.5f, new Color(255,255,255,80).getRGB());

        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer) {
                EntityPlayer ent = (EntityPlayer) o;
                if (ent.isEntityAlive() && ent != mc.thePlayer && !ent.isInvisible() && !ent.isInvisibleToPlayer(mc.thePlayer)) {
                    float pTicks = mc.timer.renderPartialTicks;
                    float posX = (float) ((ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double) pTicks - (double) playerOffsetX) * (scale.getValueState()));
                    float posZ = (float) ((ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double) pTicks - (double) playerOffSetZ) * (scale.getValueState()));
                    int color = (FriendManager.isTeam(ent) && Flux.teams.getValueState()) ? HudWindowMod.radarTeamColours.getColorInt() : HudWindowMod.radarEnemyColours.getColorInt();

                    float cos = (float) Math.cos((double) mc.thePlayer.rotationYaw * 0.017453292519943295D);
                    float sin = (float) Math.sin((double) mc.thePlayer.rotationYaw * 0.017453292519943295D);
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);

                    if (rotY > (height / 2 - 5f)) {
                        rotY = (height / 2) - 5f;
                    } else if (rotY < -(height / 2) + 5f) {
                        rotY = -(height / 2) + 5f;
                    }

                    if (rotX > (width / 2) - 5.0F) {
                        rotX = (width / 2 - 5);
                    } else if (rotX < (-(width / 2 - 5))) {
                        rotX = -((width / 2) - 5.0F);
                    }

                    RenderUtil.circle((xOffset + (width / 2) + rotX), (yOffset + (height / 2) + rotY), 1f, color);
                }
            }
        }
    }
}
