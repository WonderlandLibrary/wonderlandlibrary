package koks.manager.module.impl.render;

import koks.Koks;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.event.impl.EventMotion;
import koks.manager.event.impl.EventRender3D;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 06:07
 */

@ModuleInfo(name = "TrailESP", description = "Its render your positions in to a line", category = Module.Category.RENDER)
public class TrailESP extends Module {

    public Setting length = new Setting("Length", 50, 5, 1000, true, this);
    public Setting inFirstPerson = new Setting("Show In FirstPerson", true, this);

    public ArrayList<double[]> positions = new ArrayList<>();

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventRender3D) {
            if (mc.gameSettings.thirdPersonView != 0 || inFirstPerson.isToggled()) {
                GL11.glPushMatrix();
                GL11.glColor4f(Koks.getKoks().clientColor.getRed() / 255F, Koks.getKoks().clientColor.getGreen() / 255F, Koks.getKoks().clientColor.getBlue() / 255F, Koks.getKoks().clientColor.getAlpha() / 255F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glLineWidth(2F);

                GL11.glBegin(GL11.GL_LINE_STRIP);

                for (double[] pos : positions) {
                    GL11.glVertex3d(pos[0] - mc.getRenderManager().renderPosX, pos[1] - mc.getRenderManager().renderPosY, pos[2] - mc.getRenderManager().renderPosZ);
                }

                GL11.glVertex3d(0, 0.01, 0);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glColor4f(1, 1, 1, 1);

                GL11.glPopMatrix();
            }
        }

        if (event instanceof EventUpdate) {
            setInfo(length.getCurrentValue() + "");
            for (int i = 0; i < positions.size(); i++) {
                if(System.currentTimeMillis() - positions.get(i)[3] > length.getCurrentValue()) {
                    positions.remove(i);
                }
            }
        }

        if (event instanceof EventMotion) {
            if (isMoving()) {
                positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, System.currentTimeMillis()});
            }
        }
    }

    @Override
    public void onEnable() {
        positions.clear();
    }

    @Override
    public void onDisable() {

    }
}
