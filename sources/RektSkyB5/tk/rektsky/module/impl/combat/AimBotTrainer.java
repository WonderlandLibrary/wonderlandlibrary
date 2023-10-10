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

public class AimBotTrainer
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

    public AimBotTrainer(boolean disabled) {
        super("AimBotTrainer", "The most undetectable aim bot on the market", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.start();
    }

    @Override
    public void onDisable() {
    }

    public void start() {
        this.yawData = new JsonArray();
        this.pitchData = new JsonArray();
        this.targetPitch = 0.0f;
        this.targetYaw = 90.0f;
        this.dataFile = new File("AimData/" + System.currentTimeMillis() + "-" + this.targetPitch + "-" + this.targetYaw + ".json");
        File file = new File("AimData/");
        Client.addClientChat("You have " + file.listFiles().length + " data now! Keep it up!");
        this.leftTime = 1;
        Client.addClientChat(this.leftTime);
        this.started = false;
    }

    public void stop() {
        JsonObject object = new JsonObject();
        object.add("x", this.yawData);
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
        GlStateManager.color(255.0f, 0.0f, 0.0f, 255.0f);
        if ((double)this.mc.thePlayer.rotationYaw > (double)this.targetYaw - this.diff && (double)this.mc.thePlayer.rotationYaw < (double)this.targetYaw + this.diff && (double)this.mc.thePlayer.rotationPitch > (double)this.targetPitch - this.diff && (double)this.mc.thePlayer.rotationPitch < (double)this.targetPitch + this.diff) {
            GlStateManager.color(0.0f, 255.0f, 0.0f, 255.0f);
        }
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
        this.lastUpdateTime = System.currentTimeMillis();
        if (!this.started) {
            this.mc.thePlayer.rotationYaw = 0.0f;
            this.mc.thePlayer.rotationPitch = 0.0f;
            if (System.currentTimeMillis() - this.countDown <= 1000L) {
                // empty if block
            }
            --this.leftTime;
            this.countDown = System.currentTimeMillis();
            if (this.leftTime != 0) {
                // empty if block
            }
            this.started = true;
            Client.addClientChat("Start! Please aim to the button on your right (Yaw 90 \u00b15)");
            this.leftTime = -10;
            return;
        }
        if ((double)this.mc.thePlayer.rotationYaw > (double)this.targetYaw - this.diff && (double)this.mc.thePlayer.rotationYaw < (double)this.targetYaw + this.diff && (double)this.mc.thePlayer.rotationPitch > (double)this.targetPitch - this.diff && (double)this.mc.thePlayer.rotationPitch < (double)this.targetPitch + this.diff) {
            this.yawBuffer.add(Float.valueOf(this.mc.thePlayer.rotationYaw));
            this.pitchBuffer.add(Float.valueOf(this.mc.thePlayer.rotationPitch));
            if (System.currentTimeMillis() - this.countDown > 1000L) {
                if (this.leftTime == -10) {
                    Client.addClientChat("Nice! Please keep your mouse hover on it...");
                    this.leftTime = 2;
                }
                --this.leftTime;
                this.countDown = System.currentTimeMillis();
                if (this.leftTime == 0) {
                    this.started = true;
                    Client.addClientChat("Done! Saving data...");
                    this.stop();
                    this.start();
                } else {
                    Client.addClientChat("Keep it there for extra " + this.leftTime + " second(s)");
                }
            }
            return;
        }
        if (this.leftTime != -10) {
            Client.addClientChat("You left the aim zone! Please keep hovering on it...");
            while (!this.yawBuffer.isEmpty()) {
                this.yawData.add(this.yawBuffer.remove(0));
            }
            while (!this.pitchBuffer.isEmpty()) {
                this.pitchData.add(this.pitchBuffer.remove(0));
            }
        }
        this.yawData.add(Float.valueOf(this.mc.thePlayer.rotationYaw / this.targetYaw * 1.0f));
        this.pitchData.add(Float.valueOf(this.mc.thePlayer.rotationPitch));
        this.leftTime = -10;
    }
}

