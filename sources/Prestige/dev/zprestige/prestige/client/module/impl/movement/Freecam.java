package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.api.mixin.IKeyBinding;
import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.CameraOffsetEvent;
import dev.zprestige.prestige.client.event.impl.Render3DEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import java.util.ArrayList;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class Freecam extends Module {

    public FloatSetting speed;
    public BooleanSetting renderLine;
    public Vec3d pos;
    public Vec3d pos2;

    public Freecam() {
        super("Freecam", Category.Movement, "Allows you to move your camera around without actually moving your player");
        speed = setting("Speed", 1f, 0.1f, 10f).description("How fast you move");
        renderLine = setting("Render Line", true);
        pos = Vec3d.ZERO;
        pos2 = Vec3d.ZERO;
    }

    @Override
    public void onEnable() {
        this.pos = this.pos2 = getMc().player.getEyePos();
    }

    @EventListener
    public void event(TickEvent event) {
        getMc().options.forwardKey.setPressed(false);
        getMc().options.backKey.setPressed(false);
        getMc().options.leftKey.setPressed(false);
        getMc().options.rightKey.setPressed(false);
        getMc().options.jumpKey.setPressed(false);
        getMc().options.sneakKey.setPressed(false);
        float f = (float)Math.PI / 180;
        float f2 = (float)Math.PI;
        ClientPlayerEntity clientPlayerEntity = getMc().player;
        Vec3d vec3d = new Vec3d(-MathHelper.sin(-getMc().player.getYaw() * f - f2), 0.0, -MathHelper.cos(-clientPlayerEntity.getYaw() * f - f2));
        Vec3d vec3d2 = new Vec3d(0.0, 1.0, 0.0);
        Vec3d vec3d3 = vec3d2.crossProduct(vec3d);
        Vec3d vec3d4 = vec3d.crossProduct(vec3d2);
        Vec3d vec3d5 = Vec3d.ZERO;
        KeyBinding keyBinding = getMc().options.forwardKey;
        if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), ((IKeyBinding)keyBinding).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(vec3d);
        }
        KeyBinding keyBinding2 = getMc().options.backKey;
        if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), ((IKeyBinding)keyBinding2).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.subtract(vec3d);
        }
        KeyBinding keyBinding3 = getMc().options.leftKey;
        if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), ((IKeyBinding)keyBinding3).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(vec3d3);
        }
        KeyBinding keyBinding4 = getMc().options.rightKey;
        if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), ((IKeyBinding)keyBinding4).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(vec3d4);
        }
        KeyBinding keyBinding5 = getMc().options.jumpKey;
        if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), ((IKeyBinding)keyBinding5).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(0.0, speed.getObject(), 0.0);
        }
        KeyBinding keyBinding6 = getMc().options.sneakKey;
        if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), ((IKeyBinding)keyBinding6).getBoundKey().getCode()) == 1) {
            vec3d5 = vec3d5.add(0.0, -speed.getObject(), 0.0);
        }
        KeyBinding keyBinding7 = getMc().options.sprintKey;
        vec3d5 = vec3d5.normalize().multiply(speed.getObject() * (GLFW.glfwGetKey(getMc().getWindow().getHandle(), ((IKeyBinding)keyBinding7).getBoundKey().getCode()) == 1 ? 2 : 1));
        pos = pos2;
        pos2 = pos2.add(vec3d5);
    }

    @EventListener
    public void event(Render3DEvent event) {
        if (renderLine.getObject()) {
            RenderHelper.setMatrixStack(event.getMatrixStack());
            RenderUtil.setCameraAction();
            double d = MathHelper.lerp(event.getTickDelta(), pos.x, pos2.x);
            double d2 = MathHelper.lerp(event.getTickDelta(), pos.y, pos2.y);
            double d3 = MathHelper.lerp(event.getTickDelta(), pos.z, pos2.z);
            ArrayList<Vec3d> arrayList = new ArrayList();
            arrayList.add(getMc().player.getPos());
            arrayList.add(new Vec3d(d, d2 - getMc().player.getEyeHeight(getMc().player.getPose()), d3));
            RenderUtil.renderLines(arrayList, Prestige.Companion.getModuleManager().getMenu().getColor().getObject());
            event.getMatrixStack().pop();
        }
    }

    @EventListener
    public void event(@NotNull CameraOffsetEvent event) {
        float f = this.getMc().getTickDelta();
        event.setX(MathHelper.lerp(f, pos.x, pos2.x));
        event.setY(MathHelper.lerp(f, pos.y, pos2.y));
        event.setZ(MathHelper.lerp(f, pos.z, pos2.z));
        event.setCancelled();
    }
}
