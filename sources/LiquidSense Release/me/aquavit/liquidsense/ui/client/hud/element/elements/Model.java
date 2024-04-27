package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;

import static me.aquavit.liquidsense.utils.render.RenderUtils.drawEntityOnScreen;

@ElementInfo(name = "Model")
public class Model extends Element {

    private ListValue yawMode = new ListValue("Yaw", new String[] {"Player", "Animation", "Custom"}, "Animation");
    private FloatValue customYaw = new FloatValue("CustomYaw", 0F, -180F, 180F);

    private ListValue pitchMode = new ListValue("Pitch", new String[] {"Player", "Custom"}, "Player");
    private FloatValue customPitch = new FloatValue("CustomPitch", 0F, -90F, 90F);

    private float rotate = 0F;
    private boolean rotateDirection = false;
    private float yaw = 0F;
    private float pitch = 0F;

    public Model(){
        super(40,100,1f,new Side(Side.Horizontal.LEFT, Side.Vertical.UP));
    }

    @Override
    public Border drawElement() {
        switch (yawMode.get()){
            case "Player":
                yaw = mc.thePlayer.rotationYaw;
                break;
            case "Custom":
                yaw = customYaw.get();
                break;
            case "Animation": {
                int delta = RenderUtils.deltaTime;

                if (rotateDirection) {
                    if (rotate <= 70F) {
                        rotate += 0.12F * delta;
                    } else {
                        rotateDirection = false;
                        rotate = 70F;
                    }
                } else {
                    if (rotate >= -70F) {
                        rotate -= 0.12F * delta;
                    } else {
                        rotateDirection = true;
                        rotate = -70F;
                    }
                }

                yaw = rotate;
                break;
            }
        }
        switch (pitchMode.get()){
            case "Player":
                pitch = mc.thePlayer.rotationPitch;
                break;
            case "Custom":
                pitch = customPitch.get();
                break;
        }

        pitch = pitch > 0 ? -pitch : Math.abs(pitch);

        drawEntityOnScreen(yaw, pitch, mc.thePlayer);

        return new Border(30F, 10F, -30F, -100F);
    }
}
