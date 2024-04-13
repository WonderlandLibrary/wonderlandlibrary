package net.smoothboot.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.hud.frame;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.ModeSetting;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PlayerESP extends Mod {

    String glow = "Glow";
    String esp2d = "2D";

    public ModeSetting espMode; {
        espMode = new ModeSetting("Mode", glow, glow, esp2d);
    }
    public static BooleanSetting ESPTeamcheck = new BooleanSetting("Team check", true);

    public PlayerESP() {
        super("Player ESP", "Render players through walls", Category.Render);
        addsettings(espMode, ESPTeamcheck);
    }

    public static boolean outliningEntities = false;

    @Override
    public void onTick() {
        if (espMode.isMode(esp2d)) {
            outliningEntities = false;
        }
        else if (espMode.isMode(glow)){
            outliningEntities = true;
        }
        super.onTick();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (this.isEnabled() && espMode.isMode(esp2d)) {
            for (PlayerEntity entity : mc.world.getPlayers()) {
                if (!(entity instanceof ClientPlayerEntity) && entity instanceof PlayerEntity) {
                    if (ESPTeamcheck.isEnabled() && !entity.isTeammate(mc.player)) {
                        renderOutline(entity, new Color(frame.menured, frame.menugreen, frame.menublue, 255), matrices);
                        super.onWorldRender(matrices);
                        }
                    else if (!ESPTeamcheck.isEnabled()) {
                        renderOutline(entity, new Color(frame.menured, frame.menugreen, frame.menublue, 255), matrices);
                        super.onWorldRender(matrices);
                    }
                    }
                }
            }
        }

    @Override
    public void onDisable() {
        outliningEntities = false;
        super.onDisable();
    }

    void renderOutline(PlayerEntity e, Color color, MatrixStack stack) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = mc.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        Vec3d start = e.getPos().subtract(camPos);
        float x = (float) start.x;
        float y = (float) start.y;
        float z = (float) start.z;

        double r = Math.toRadians(-c.getYaw() + 90);
        float sin = (float) (Math.sin(r) * (e.getWidth() / 1.7));
        float cos = (float) (Math.cos(r) * (e.getWidth() / 1.7));
        stack.push();

        Matrix4f matrix = stack.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableBlend();
        stack.pop();
    }
}
