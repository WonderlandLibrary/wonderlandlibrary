package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.module.modules.misc.Teams;
import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.render.MiniMapRegister;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.SafeVertexBuffer;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

@ElementInfo(name = "Radar")
public class Radar extends Element {

    public Radar() {
        super(5.0, 130, 1f, new Side(Side.Horizontal.LEFT, Side.Vertical.UP));
    }
    public static float SQRT_OF_TWO = (float) Math.sqrt(2f);

    private FloatValue sizeValue = new FloatValue("Size", 90f, 30f, 500f);
    private FloatValue viewDistanceValue = new FloatValue("View Distance", 4F, 0.5F, 32F);
    private ListValue modeValue = new ListValue("Mode",new String[]{"Normal", "MiniMap"}, "MiniMap");

    private FloatValue playerSizeValue = new FloatValue("Player Size", 8.0F, 0.5f, 20F);
    private FloatValue fovSizeValue = new FloatValue("FOV Size", 10F, 0F, 50F);
    private FloatValue fovAngleValue = new FloatValue("FOV Angle", 70F, 30F, 160F);

    private VertexBuffer fovMarkerVertexBuffer;
    private float lastFov = 0f;

    @Override
    public Border drawElement() {
        MiniMapRegister.updateChunks();


        float fovAngle = fovAngleValue.get();

        if (lastFov != fovAngle || fovMarkerVertexBuffer == null) {
            // Free Memory
            if (fovMarkerVertexBuffer != null) fovMarkerVertexBuffer.deleteGlBuffers();

            fovMarkerVertexBuffer = createFovIndicator(fovAngle);
            lastFov = fovAngle;
        }

        Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();

        float size = sizeValue.get();

        if (!modeValue.get().equalsIgnoreCase("MiniMap")) {
            BlurBuffer.blurArea((int)(getRenderX() * getScale()),(int)(getRenderY() * getScale()),size*getScale(),size*getScale(),true);
            if (!this.getInfo().disableScale())
                GL11.glScalef(this.getScale(), this.getScale(), this.getScale());

            GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);
            RenderUtils.drawRect(0F, 0F, size, size, new Color(14, 14,
                    14, 200).getRGB());
        }

        RenderUtils.drawRoundedRect(-0.5F, -7F - Fonts.csgo40.FONT_HEIGHT, size + 0.5F, 7F + Fonts.csgo40.FONT_HEIGHT,1.5F,
                new Color(16, 25, 32, 200).getRGB(), 1F,new Color(16, 25, 32, 200).getRGB());
        //RenderUtils.drawBorderedRect(0F, -7.2F - Fonts.csgo40.FONT_HEIGHT, size, 0, 3F, new Color(16, 25, 32, 200).getRGB(), new Color(16, 25, 32, 200).getRGB());
        Fonts.csgo40.drawString("P", 3.8F, -(Fonts.csgo40.FONT_HEIGHT + 1.7F), new Color(0, 131, 193).getRGB(), false);
        Fonts.font20.drawString("Radar", Fonts.csgo40.getStringWidth("P") + 9.5F, -(Fonts.csgo40.FONT_HEIGHT + 2.1F), Color.WHITE.getRGB(), false);
        float viewDistance = viewDistanceValue.get() * 16.0F;

        float maxDisplayableDistanceSquare = ((viewDistance + fovSizeValue.get()) *
                (viewDistance + fovSizeValue.get()));
        float halfSize = size / 2f;

        RenderUtils.makeScissorBox((float)getX() * getScale(), (float)getY() * getScale(), ((float)getX() + (float)Math.ceil(size)) * getScale(), ((float)getY() + (float)Math.ceil(size))* getScale());

        glEnable(GL_SCISSOR_TEST);

        glPushMatrix();

        glTranslatef(halfSize, halfSize, 0f);
        glRotatef(renderViewEntity.rotationYaw, 0f, 0f, -1f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        if (modeValue.get().equalsIgnoreCase("MiniMap")){
            glEnable(GL_TEXTURE_2D);

            float chunkSizeOnScreen = size / viewDistanceValue.get();
            float chunksToRender = (float) max(1, ceil((SQRT_OF_TWO * (viewDistanceValue.get() * 0.5f))));

            double currX = renderViewEntity.posX / 16.0;
            double currZ = renderViewEntity.posZ / 16.0;

            for (float x = -chunksToRender; x <= chunksToRender; ++x) {
                for (float z = -chunksToRender; z <= chunksToRender; ++z) {
                    MiniMapRegister.MiniMapTexture currChunk = MiniMapRegister.getChunkTextureAt((int) (floor(currX) + x), (int) (floor(currZ) + z));

                    if (currChunk != null) {
                        double sc = chunkSizeOnScreen;

                        double onScreenX = (currX - floor(currX) - 1 - x) * sc;
                        double onScreenZ = (currZ - floor(currZ) - 1 - z) * sc;

                        GlStateManager.bindTexture(currChunk.getTexture().getGlTextureId());

                        glBegin(GL_QUADS);


                        glTexCoord2f(0f, 0f);
                        glVertex2d(onScreenX, onScreenZ);
                        glTexCoord2f(0f, 1f);
                        glVertex2d(onScreenX, onScreenZ + chunkSizeOnScreen);
                        glTexCoord2f(1f, 1f);
                        glVertex2d(onScreenX + chunkSizeOnScreen, onScreenZ + chunkSizeOnScreen);
                        glTexCoord2f(1f, 0f);
                        glVertex2d(onScreenX + chunkSizeOnScreen, onScreenZ);

                        glEnd();
                    }

                }
            }

            GlStateManager.bindTexture(0);

            glDisable(GL_TEXTURE_2D);
        }

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);


        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        float playerSize = playerSizeValue.get();

        glEnable(GL_POINT_SMOOTH);

        glEnable(GL_POLYGON_SMOOTH);

        worldRenderer.begin(GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        glPointSize(playerSize);


        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity != null && EntityUtils.isSelected(entity, false, true)) {
                Vector2f positionRelativeToPlayer = new Vector2f((float) (renderViewEntity.posX - entity.posX),
                        (float) (renderViewEntity.posZ - entity.posZ));

                if (maxDisplayableDistanceSquare < positionRelativeToPlayer.lengthSquared())
                    continue;

                float transform = fovSizeValue.get();

                if (transform > 0F) {
                    glPushMatrix();

                    glTranslatef((positionRelativeToPlayer.x / viewDistance) * size,
                            (positionRelativeToPlayer.y / viewDistance) * size, 0f);
                    glRotatef(entity.rotationYaw, 0f, 0f, 1f);
                }

                if (fovSizeValue.get() > 0F) {
                    glPushMatrix();
                    glRotatef(180.0f, 0f, 0f, 1f);
                    float sc = (fovSizeValue.get() / viewDistance) * size;
                    glScalef(sc, sc, sc);


                    glColor4f(1.0f, 1.0f, 1.0f,  0.75f);


                    VertexBuffer vbo = fovMarkerVertexBuffer;

                    vbo.bindBuffer();

                    glEnableClientState(GL_VERTEX_ARRAY);
                    glVertexPointer(3, GL_FLOAT, 12, 0L);

                    vbo.drawArrays(GL_TRIANGLE_FAN);
                    vbo.unbindBuffer();

                    glDisableClientState(GL_VERTEX_ARRAY);

                    glPopMatrix();
                }

                if (Teams.isInYourTeam((EntityLivingBase) entity) && entity != mc.thePlayer) {
                    worldRenderer.pos(((double) (positionRelativeToPlayer.x / viewDistance) * size),
                            ((double) (positionRelativeToPlayer.y / viewDistance) * size), 0.0)
                            .color(0f / 255.0f, 1.0f, 0f / 255.0f, 1.0f).endVertex();
                }else if (entity == mc.thePlayer) {
                    worldRenderer.pos(((double) (positionRelativeToPlayer.x / viewDistance) * size),
                            ((double) (positionRelativeToPlayer.y / viewDistance) * size), 0.0)
                            .color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                }else{
                    worldRenderer.pos(((double) (positionRelativeToPlayer.x / viewDistance) * size),
                            ((double) (positionRelativeToPlayer.y / viewDistance) * size), 0.0)
                            .color(1.0f, 0f / 255.0f, 0f / 255.0f, 1.0f).endVertex();
                }

                if(playerSize > 0) {
                    RenderUtils.drawFullCircle(0, 0, playerSize / 3, 0f, new Color(255, 255, 255,200));
                    GlStateManager.resetColor();
                }




                if (transform  > 0F) {
                    glPopMatrix();
                }

            }
        }

        tessellator.draw();


        glDisable(GL_POINT_SMOOTH);

        glDisable(GL_POLYGON_SMOOTH);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);

        glDisable(GL_SCISSOR_TEST);

        glPopMatrix();

        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        return new Border(0F, 0F, size, size);
    }

    private VertexBuffer createFovIndicator(float angle) {
        // Rendering
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();

        worldRenderer.begin(GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);

        double start = (90.0f - (angle * 0.5f)) / 180.0f * Math.PI;
        double end = (90.0f + (angle * 0.5f)) / 180.0f * Math.PI;

        double curr = end;
        double radius = 1.0;

        worldRenderer.pos(0.0, 0.0, 0.0).endVertex();

        while (curr >= start) {
            worldRenderer.pos(cos(curr) * radius, sin(curr) * radius, 0.0).endVertex();

            curr -= 0.15f;
        }

        // Uploading to VBO

        SafeVertexBuffer safeVertexBuffer = new SafeVertexBuffer(worldRenderer.getVertexFormat());

        worldRenderer.finishDrawing();
        worldRenderer.reset();
        safeVertexBuffer.bufferData(worldRenderer.getByteBuffer());

        return safeVertexBuffer;
    }
}
