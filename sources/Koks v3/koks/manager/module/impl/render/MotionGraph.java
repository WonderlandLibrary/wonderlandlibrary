package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.api.util.RenderUtil;
import koks.manager.event.Event;
import koks.manager.event.impl.EventRender2D;
import koks.manager.event.impl.EventSettingUpdate;
import koks.manager.event.impl.EventTick;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "MotionGraph", description = "", category = Module.Category.RENDER)
public class MotionGraph extends Module {

    private List<Double> motionSpeed = new ArrayList<>();

    public Setting rgb = new Setting("RGB",false,this);
    public Setting width = new Setting("Width",140,1,1000,true,this);

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        motionSpeed.clear();
    }

    @Override
    public void onEvent(Event event) {

        if(event instanceof EventSettingUpdate) {
            EventSettingUpdate valueChangeEvent = (EventSettingUpdate)event;
            if(valueChangeEvent.getSetting().equals(width)) {
                motionSpeed.clear();
            }
        }

        if(event instanceof EventTick) {
            motionSpeed.add(Math.hypot(getPlayer().motionX,getPlayer().motionZ) * 100);

            if(motionSpeed.size() > width.getCurrentValue() / 2) {
//                sendmsg((motionSpeed.size() - ((int)width.getCurrentValue() / 2+1))+"",false);
                motionSpeed.remove(((motionSpeed.size() - ((int)width.getCurrentValue() / 2+1))));
//                motionSpeed.clear();
            }
        }
        if(event instanceof EventRender2D) {
            ScaledResolution sr = new ScaledResolution(mc);

            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glColor3f(0, 0, 0);
            GL11.glLineWidth(4);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            double add2 = 0;
            for (int i = 0; i < motionSpeed.size(); i++) {
                GL11.glVertex2d(sr.getScaledWidth() / 2 - width.getCurrentValue() / 2 + add2, sr.getScaledHeight() - 75 - motionSpeed.get(i));
                add2 += 2;
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            if(!rgb.isToggled()) {
                GL11.glColor3f(1, 1, 1);
            }
            GL11.glLineWidth(2);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            double add = 0;
            int offset = 0;
            for (int i = 0; i < motionSpeed.size(); i++) {
                if(rgb.isToggled()) {
                    Color color = renderUtil.getRainbow(offset, 2000, 1, 1);
                    GL11.glColor3f(((float) color.getRed() / 255), ((float) color.getGreen() / 255), ((float) color.getBlue() / 255));
                    offset += 10;
                }
                GL11.glVertex2d(sr.getScaledWidth() / 2 - width.getCurrentValue() / 2 + add, sr.getScaledHeight() - 75 - motionSpeed.get(i));
                add += 2;
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }
}
