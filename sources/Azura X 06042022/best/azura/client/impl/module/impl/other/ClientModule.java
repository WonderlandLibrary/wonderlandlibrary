package best.azura.client.impl.module.impl.other;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventRenderPlayer;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.color.HSBColor;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ColorValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

import java.awt.*;

@ModuleInfo(name = "Client", category = Category.OTHER, description = "Configure client stuff")
public class ClientModule extends Module {
    public static final BooleanValue leftHanded = new BooleanValue("Left handed", "Use your left hand.", false);
    public static final BooleanValue otherRotations = new BooleanValue("Minecraft Rotations", "Use minecraft rotations or not", false);
    public static final BooleanValue hideRender = new BooleanValue("Hide render on the ArrayList", "Hide ALL render modules", false);
    public static final BooleanValue borderlessFullscreen = new BooleanValue("Borderless Fullscreen", "Enable borderless fullscreen", true);
    public static final BooleanValue backgroundShaders = new BooleanValue("Background Shaders", "Enable background shaders", OpenGlHelper.shadersSupported);
    public static final ColorValue shaderColor1 = new ColorValue("Shader Color 1", "Change the shader's colors", backgroundShaders::getObject,
            HSBColor.fromColor(new Color(0x2B90FF)));
    public static final ColorValue shaderColor2 = new ColorValue("Shader Color 2", "Change the shader's colors", backgroundShaders::getObject,
            HSBColor.fromColor(new Color(0xAD048B)));
    private float[] lastValues = null;


    @Override
    public void onEnable() {
        this.setEnabled(false);
        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Info", "You can not enable this module", 1500, Type.INFO));
    }


    @EventHandler
    public final Listener<EventRenderPlayer> eventRenderPlayerListener = e -> {
        if ((e.getMode().equals("Pre Model") || e.getMode().equals("Post Model")) && leftHanded.getObject()) {
            e.bodyRotationYaw = -e.bodyRotationYaw;
            GlStateManager.scale(-1, 1, 1);
        }
        if (e.getMode().equals("Set Rotation Angles") && leftHanded.getObject()) {
            if (lastValues == null) lastValues = new float[12];
            final float lastLeftWearX = e.boxList.get(9).rotateAngleX, lastLeftWearY = e.boxList.get(9).rotateAngleY,
                    lastLeftWearZ = e.boxList.get(9).rotateAngleZ;
            final float lastRightWearX = e.boxList.get(10).rotateAngleX, lastRightWearY = e.boxList.get(10).rotateAngleY,
                    lastRightWearZ = e.boxList.get(10).rotateAngleZ;
            final float lastLeftX = e.boxList.get(3).rotateAngleX, lastLeftY = e.boxList.get(3).rotateAngleY,
                    lastLeftZ = e.boxList.get(3).rotateAngleZ;
            final float lastRightX = e.boxList.get(4).rotateAngleX, lastRightY = e.boxList.get(4).rotateAngleY,
                    lastRightZ = e.boxList.get(4).rotateAngleZ;
            lastValues[0] = lastLeftWearX;
            lastValues[1] = lastLeftWearY;
            lastValues[2] = lastLeftWearZ;
            lastValues[3] = lastRightWearX;
            lastValues[4] = lastRightWearY;
            lastValues[5] = lastRightWearZ;
            lastValues[6] = lastLeftX;
            lastValues[7] = lastLeftY;
            lastValues[8] = lastLeftZ;
            lastValues[9] = lastRightX;
            lastValues[10] = lastRightY;
            lastValues[11] = lastRightZ;
        }
    };

}