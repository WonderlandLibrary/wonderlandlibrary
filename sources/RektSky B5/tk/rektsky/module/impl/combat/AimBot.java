/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.combat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vector3d;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import tk.rektsky.Client;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.utils.combat.RotationUtil;
import tk.rektsky.utils.entity.EntityUtil;

public class AimBot
extends Module {
    Gson gson = new Gson();
    private float targetYaw = 90.0f;
    private float targetPitch = 0.0f;
    private long lastUpdateTime = 0L;
    private final List<Float> yaw = new ArrayList<Float>();
    private final List<Float> pitch = new ArrayList<Float>();
    private int currentYaw = 0;
    private int currentPitch = 0;
    private FlickingAlgorithm currentFlickingAlgorithm = null;
    private float flickTargetYaw = 0.0f;
    private float flickStartYaw = 0.0f;
    private float flickTargetPitch = 0.0f;
    private float flickStartPitch = 0.0f;
    private long startFlickingTime = 0L;
    private boolean isFlicking = false;
    private float lastTargetYaw = 0.0f;
    private float lastTargetPitch = 0.0f;
    float lastDiff;

    public AimBot(boolean disabled) {
        super("AimBot", "Basically 90% undetectable", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        Random random = new Random();
        this.currentPitch = this.currentYaw = random.nextInt(this.yaw.size() - 1);
        this.isFlicking = false;
        this.lastDiff = 0.0f;
    }

    @Override
    public void onDisable() {
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
                pitchOffset = -5.0;
            } else if (i2 == 1) {
                yawOffset = -5.0;
                pitchOffset = 0.0;
            } else if (i2 == 2) {
                yawOffset = 0.0;
                pitchOffset = 5.0;
            } else {
                yawOffset = 5.0;
                pitchOffset = 0.0;
            }
            Vector3d vector3d = RotationUtil.get360Position((double)this.targetYaw + yawOffset, (double)this.targetPitch + pitchOffset, 4.0, 0.0f);
        }
        GL11.glEnd();
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GlStateManager.popMatrix();
        List<EntityLivingBase> targets = EntityUtil.getEntities_old(false);
        targets = targets.stream().filter(new EntityUtil.FriendFilter()).collect(Collectors.toList());
        EntityLivingBase target = EntityUtil.getClosest_old(targets, 5.0);
        if (target == null) {
            this.lastDiff = 0.0f;
            return;
        }
        Vector2f face = RotationUtil.tryFace((target.posX - target.lastTickPosX) * (double)event.getPartialTick() + target.lastTickPosX, (target.posY - target.lastTickPosY) * (double)event.getPartialTick() + target.lastTickPosY + (double)(target.height / 2.0f), (target.posZ - target.lastTickPosZ) * (double)event.getPartialTick() + target.lastTickPosZ);
        this.lastTargetPitch = this.targetPitch;
        this.lastTargetYaw = this.targetYaw;
        this.targetYaw = face.x;
        this.targetPitch = face.y;
        float yawDiff = Math.abs(((this.mc.thePlayer.rotationYaw - this.targetYaw) % 360.0f + 540.0f) % 360.0f - 180.0f);
        float pitchDiff = Math.abs(this.mc.thePlayer.rotationPitch - this.targetPitch);
        float lastYawDiff = ((this.targetYaw - this.lastTargetYaw) % 360.0f + 540.0f) % 360.0f - 180.0f;
        float lastPitchDiff = this.targetPitch - this.lastTargetPitch;
        if (Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff) < 15.0) {
            this.lastDiff = Math.min(30.0f, Math.max((float)Math.sqrt(lastYawDiff * lastYawDiff + lastPitchDiff * lastPitchDiff), this.lastDiff - 0.1f));
            this.lastUpdateTime = System.currentTimeMillis();
            this.isFlicking = false;
            this.mc.thePlayer.rotationYaw += ((this.targetYaw - this.mc.thePlayer.rotationYaw) % 360.0f + 540.0f) % 360.0f - 180.0f + 1.0f * Math.min(Math.max(this.yaw.get(this.currentYaw++).floatValue() * Math.min(1.0f, this.lastDiff / 3.0f) * 2.0f, -10.0f), 10.0f);
            this.mc.renderManager.playerViewX = this.mc.thePlayer.rotationPitch = this.targetPitch + this.pitch.get(this.currentPitch++).floatValue() * 4.0f;
            this.mc.renderManager.playerViewY = this.mc.thePlayer.rotationYaw;
        } else {
            this.lastUpdateTime = System.currentTimeMillis();
            if (!this.isFlicking || (float)(System.currentTimeMillis() - this.startFlickingTime) / 400.0f > 1.0f) {
                this.isFlicking = true;
                File file = new File("AimData/");
                Random random = new Random();
                int i3 = random.nextInt(file.listFiles().length);
                File algorithmFile = file.listFiles()[i3];
                Client.addClientChat("Loading data: " + algorithmFile.getName());
                this.currentFlickingAlgorithm = new FlickingAlgorithm(algorithmFile);
                this.flickStartYaw = this.mc.thePlayer.rotationYaw;
                this.flickStartPitch = this.mc.thePlayer.rotationPitch;
                this.startFlickingTime = System.currentTimeMillis();
            }
            this.flickTargetYaw = this.targetYaw;
            this.flickTargetPitch = this.targetPitch;
            Vector2f data = this.currentFlickingAlgorithm.getData((float)(System.currentTimeMillis() - this.startFlickingTime) / 400.0f);
            this.mc.thePlayer.rotationYaw = (((this.flickTargetYaw - this.flickStartYaw) % 360.0f + 540.0f) % 360.0f - 180.0f) * data.x + this.flickStartYaw;
            this.mc.thePlayer.rotationPitch = (this.flickTargetPitch - this.flickStartPitch) * data.x + this.flickStartPitch;
            this.mc.renderManager.playerViewX = this.mc.thePlayer.rotationPitch = Math.min(90.0f, Math.max(-90.0f, this.mc.thePlayer.rotationPitch));
            this.mc.renderManager.playerViewY = this.mc.thePlayer.rotationYaw;
        }
    }

    public class FlickingAlgorithm {
        private File file;
        public final int length;
        private List<Float> xData = new ArrayList<Float>();
        private List<Float> yDiffData = new ArrayList<Float>();

        public FlickingAlgorithm(File file) {
            this.file = file;
            JsonObject object = AimBot.this.gson.fromJson((Reader)new FileReader(file), JsonObject.class);
            for (JsonElement x2 : object.get("x").getAsJsonArray()) {
                this.xData.add(Float.valueOf(x2.getAsFloat()));
            }
            for (JsonElement yDiff : object.get("yDiff").getAsJsonArray()) {
                this.yDiffData.add(Float.valueOf(yDiff.getAsFloat()));
            }
            this.length = this.yDiffData.size();
        }

        public Vector2f getData(float normalizedData) {
            float xIn;
            if (normalizedData > 1.0f) {
                normalizedData = 1.0f;
            }
            if ((xIn = normalizedData * (float)this.length) > (float)(this.length - 1)) {
                xIn = this.length - 1;
            }
            Float axFloat = this.xData.get((int)xIn);
            Float bxFloat = this.xData.get((int)xIn + 1);
            float extraX = xIn % 1.0f;
            float yIn = normalizedData * (float)this.length;
            Float ayFloat = this.yDiffData.get((int)yIn);
            Float byFloat = this.yDiffData.get((int)yIn + 1);
            float extraY = yIn % 1.0f;
            return new Vector2f(axFloat.floatValue() + (bxFloat.floatValue() - axFloat.floatValue()) * extraX, ayFloat.floatValue() + (byFloat.floatValue() - ayFloat.floatValue()) * extraY);
        }
    }
}

