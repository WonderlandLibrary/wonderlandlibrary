/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.combat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vector3d;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;
import tk.rektsky.Client;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.utils.combat.RotationUtil;

public class TrackingTrainer
extends Module {
    private File dataFile;
    private JsonArray yawData = new JsonArray();
    private JsonArray pitchData = new JsonArray();
    Gson gson = new Gson();
    private long countDown = 0L;
    private int leftTime = 0;
    private boolean started = false;
    private long lastUpdateTime = 0L;
    private double diff = 3.0;
    private float targetYaw = 90.0f;
    private float targetPitch = 0.0f;
    private List<Float> yawBuffer = new ArrayList<Float>();
    private List<Float> pitchBuffer = new ArrayList<Float>();

    public TrackingTrainer(boolean disabled) {
        super("Tracking", "The most undetectable aim bot on the market", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.start();
    }

    @Override
    public void onDisable() {
        this.stop();
    }

    public void start() {
        File file = new File("TrackingData");
        this.dataFile = new File(file, System.currentTimeMillis() + ".json");
        Client.addClientChat("Please try and track the red box");
        this.targetYaw = this.mc.thePlayer.rotationYaw;
        this.yawData = new JsonArray();
        this.pitchData = new JsonArray();
    }

    public void stop() {
        JsonObject object = new JsonObject();
        object.add("xDiff", this.yawData);
        object.add("yDiff", this.pitchData);
        String s2 = this.gson.toJson(object);
        FileOutputStream outputStream = new FileOutputStream(this.dataFile);
        outputStream.write(s2.getBytes());
        outputStream.close();
    }

    @Subscribe
    public void onRender(RenderEvent event) {
        GlStateManager.pushMatrix();
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GlStateManager.color(20.0f, 20.0f, 255.0f, 255.0f);
        GL11.glLineWidth(4.0f);
        GlStateManager.translate(0.0f, this.mc.thePlayer.getEyeHeight(), 0.0f);
        GL11.glBegin(2);
        for (int i2 = 0; i2 < 4; ++i2) {
            double yawOffset = 0.0;
            double pitchOffset = 0.0;
            if (i2 == 0) {
                yawOffset = 0.0;
                pitchOffset = -this.diff;
            } else if (i2 == 1) {
                yawOffset = -this.diff;
                pitchOffset = 0.0;
            } else if (i2 == 2) {
                yawOffset = 0.0;
                pitchOffset = this.diff;
            } else {
                yawOffset = this.diff;
                pitchOffset = 0.0;
            }
            Vector3d e2 = RotationUtil.get360Position((double)this.targetYaw + yawOffset, (double)this.targetPitch + pitchOffset, 4.0, 0.0f);
            GL11.glVertex3d(e2.x, e2.y, e2.z);
        }
        GL11.glEnd();
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GlStateManager.popMatrix();
        if (System.currentTimeMillis() - this.lastUpdateTime <= 10L) {
            return;
        }
        this.targetYaw = (float)((double)this.targetYaw + 0.07);
        if (this.targetYaw > 180.0f) {
            this.targetYaw = -180.0f;
        }
        float yawDiff = ((this.mc.thePlayer.rotationYaw - this.targetYaw) % 360.0f + 540.0f) % 360.0f - 180.0f;
        this.yawData.add(Float.valueOf(yawDiff));
        this.pitchData.add(Float.valueOf(this.mc.thePlayer.rotationPitch - this.targetPitch));
        Client.addClientChat(this.yawData.size());
    }
}

