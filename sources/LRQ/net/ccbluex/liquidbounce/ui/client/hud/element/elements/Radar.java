/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.io.CloseableKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Vector2f
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.io.Closeable;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.vertex.IVertexBuffer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.MiniMapRegister;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

@ElementInfo(name="Radar", disableScale=true, priority=1)
public final class Radar
extends Element {
    private final FloatValue sizeValue;
    private final FloatValue viewDistanceValue;
    private final ListValue playerShapeValue;
    private final FloatValue playerSizeValue;
    private final BoolValue useESPColorsValue;
    private final FloatValue fovSizeValue;
    private final FloatValue fovAngleValue;
    private final BoolValue minimapValue;
    private final FloatValue rainbowXValue;
    private final FloatValue rainbowYValue;
    private final IntegerValue backgroundRedValue;
    private final IntegerValue backgroundGreenValue;
    private final IntegerValue backgroundBlueValue;
    private final IntegerValue backgroundAlphaValue;
    private final FloatValue borderStrengthValue;
    private final IntegerValue borderRedValue;
    private final IntegerValue borderGreenValue;
    private final IntegerValue borderBlueValue;
    private final IntegerValue borderAlphaValue;
    private final BoolValue borderRainbowValue;
    private IVertexBuffer fovMarkerVertexBuffer;
    private float lastFov;
    private static final float SQRT_OF_TWO;
    public static final Companion Companion;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Override
    public Border drawElement() {
        void enable$iv;
        MiniMapRegister.INSTANCE.updateChunks();
        float fovAngle = ((Number)this.fovAngleValue.get()).floatValue();
        if (this.lastFov != fovAngle || this.fovMarkerVertexBuffer == null) {
            Unit unit;
            IVertexBuffer iVertexBuffer = this.fovMarkerVertexBuffer;
            if (iVertexBuffer != null) {
                iVertexBuffer.deleteGlBuffers();
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            this.fovMarkerVertexBuffer = this.createFovIndicator(fovAngle);
            this.lastFov = fovAngle;
        }
        IEntity iEntity = MinecraftInstance.mc.getRenderViewEntity();
        if (iEntity == null) {
            Intrinsics.throwNpe();
        }
        IEntity renderViewEntity = iEntity;
        float size = ((Number)this.sizeValue.get()).floatValue();
        if (!((Boolean)this.minimapValue.get()).booleanValue()) {
            RenderUtils.drawRect(0.0f, 0.0f, size, size, new Color(((Number)this.backgroundRedValue.get()).intValue(), ((Number)this.backgroundGreenValue.get()).intValue(), ((Number)this.backgroundBlueValue.get()).intValue(), ((Number)this.backgroundAlphaValue.get()).intValue()).getRGB());
        }
        float viewDistance = ((Number)this.viewDistanceValue.get()).floatValue() * 16.0f;
        double maxDisplayableDistanceSquare = ((double)viewDistance + (double)((Number)this.fovSizeValue.get()).floatValue()) * ((double)viewDistance + (double)((Number)this.fovSizeValue.get()).floatValue());
        float halfSize = size / 2.0f;
        float f = (float)this.getX();
        float f2 = (float)this.getY();
        float f3 = (float)this.getX();
        boolean bl = false;
        float f4 = (float)Math.ceil(size);
        float f5 = f + f4;
        f4 = (float)this.getY();
        f = f5;
        bl = false;
        float f6 = (float)Math.ceil(size);
        RenderUtils.makeScissorBox(f3, f2, f, f4 + f6);
        GL11.glEnable((int)3089);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)halfSize, (float)halfSize, (float)0.0f);
        GL11.glRotatef((float)renderViewEntity.getRotationYaw(), (float)0.0f, (float)0.0f, (float)-1.0f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (((Boolean)this.minimapValue.get()).booleanValue()) {
            GL11.glEnable((int)3553);
            float chunkSizeOnScreen = size / ((Number)this.viewDistanceValue.get()).floatValue();
            int n = 1;
            float f7 = SQRT_OF_TWO * (((Number)this.viewDistanceValue.get()).floatValue() * 0.5f);
            boolean bl2 = false;
            int n2 = (int)Math.ceil(f7);
            bl2 = false;
            int chunksToRender = Math.max(n, n2);
            double currX = renderViewEntity.getPosX() / 16.0;
            double currZ = renderViewEntity.getPosZ() / 16.0;
            int n3 = -chunksToRender;
            int n4 = chunksToRender;
            if (n3 <= n4) {
                while (true) {
                    void x;
                    int n5;
                    int n6;
                    if ((n6 = -chunksToRender) <= (n5 = chunksToRender)) {
                        while (true) {
                            void z;
                            MiniMapRegister miniMapRegister = MiniMapRegister.INSTANCE;
                            boolean bl3 = false;
                            double d = Math.floor(currX);
                            int n7 = (int)d + x;
                            bl3 = false;
                            double d2 = Math.floor(currZ);
                            MiniMapRegister.MiniMapTexture currChunk = miniMapRegister.getChunkTextureAt(n7, (int)d2 + z);
                            if (currChunk != null) {
                                double sc = chunkSizeOnScreen;
                                double d3 = currX;
                                boolean bl4 = false;
                                d2 = Math.floor(currX);
                                double onScreenX = (d3 - (double)((long)d2) - 1.0 - (double)x) * sc;
                                d3 = currZ;
                                boolean bl5 = false;
                                d2 = Math.floor(currZ);
                                double onScreenZ = (d3 - (double)((long)d2) - 1.0 - (double)z) * sc;
                                MinecraftInstance.classProvider.getGlStateManager().bindTexture(currChunk.getTexture().func_110552_b());
                                GL11.glBegin((int)7);
                                GL11.glTexCoord2f((float)0.0f, (float)0.0f);
                                GL11.glVertex2d((double)onScreenX, (double)onScreenZ);
                                GL11.glTexCoord2f((float)0.0f, (float)1.0f);
                                GL11.glVertex2d((double)onScreenX, (double)(onScreenZ + (double)chunkSizeOnScreen));
                                GL11.glTexCoord2f((float)1.0f, (float)1.0f);
                                GL11.glVertex2d((double)(onScreenX + (double)chunkSizeOnScreen), (double)(onScreenZ + (double)chunkSizeOnScreen));
                                GL11.glTexCoord2f((float)1.0f, (float)0.0f);
                                GL11.glVertex2d((double)(onScreenX + (double)chunkSizeOnScreen), (double)onScreenZ);
                                GL11.glEnd();
                            }
                            if (z == n5) break;
                            ++z;
                        }
                    }
                    if (x == n4) break;
                    ++x;
                }
            }
            MinecraftInstance.classProvider.getGlStateManager().bindTexture(0);
            GL11.glDisable((int)3553);
        }
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        boolean triangleMode = StringsKt.equals((String)((String)this.playerShapeValue.get()), (String)"triangle", (boolean)true);
        boolean circleMode = StringsKt.equals((String)((String)this.playerShapeValue.get()), (String)"circle", (boolean)true);
        ITessellator tessellator = MinecraftInstance.classProvider.getTessellatorInstance();
        IWorldRenderer worldRenderer = tessellator.getWorldRenderer();
        if (circleMode) {
            GL11.glEnable((int)2832);
        }
        float playerSize = ((Number)this.playerSizeValue.get()).floatValue();
        GL11.glEnable((int)2881);
        if (triangleMode) {
            playerSize *= (float)2;
        } else {
            worldRenderer.begin(0, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
            GL11.glPointSize((float)playerSize);
        }
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity closeable : iWorldClient.getLoadedEntityList()) {
            boolean transform;
            Vector2f positionRelativeToPlayer;
            if (!(closeable.equals(MinecraftInstance.mc.getThePlayer()) ^ true) || !EntityUtils.isSelected(closeable, false) || maxDisplayableDistanceSquare < (double)(positionRelativeToPlayer = new Vector2f((float)(renderViewEntity.getPosX() - closeable.getPosX()), (float)(renderViewEntity.getPosZ() - closeable.getPosZ()))).lengthSquared()) continue;
            boolean bl2 = transform = triangleMode || ((Number)this.fovSizeValue.get()).floatValue() > 0.0f;
            if (transform) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(positionRelativeToPlayer.x / viewDistance * size), (float)(positionRelativeToPlayer.y / viewDistance * size), (float)0.0f);
                GL11.glRotatef((float)closeable.getRotationYaw(), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            if (((Number)this.fovSizeValue.get()).floatValue() > 0.0f) {
                IVertexBuffer vbo;
                GL11.glPushMatrix();
                GL11.glRotatef((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                float sc = ((Number)this.fovSizeValue.get()).floatValue() / viewDistance * size;
                GL11.glScalef((float)sc, (float)sc, (float)sc);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)((Boolean)this.minimapValue.get() != false ? 0.75f : 0.25f));
                if (this.fovMarkerVertexBuffer == null) {
                    Intrinsics.throwNpe();
                }
                vbo.bindBuffer();
                GL11.glEnableClientState((int)32884);
                GL11.glVertexPointer((int)3, (int)5126, (int)12, (long)0L);
                vbo.drawArrays(6);
                vbo.unbindBuffer();
                GL11.glDisableClientState((int)32884);
                GL11.glPopMatrix();
            }
            if (triangleMode) {
                if (((Boolean)this.useESPColorsValue.get()).booleanValue()) {
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(ESP.class);
                    if (module == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.ESP");
                    }
                    Color color = ((ESP)module).getColor(closeable);
                    GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
                } else {
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
                GL11.glBegin((int)4);
                GL11.glVertex2f((float)(-playerSize * 0.25f), (float)(playerSize * 0.5f));
                GL11.glVertex2f((float)(playerSize * 0.25f), (float)(playerSize * 0.5f));
                GL11.glVertex2f((float)0.0f, (float)(-playerSize * 0.5f));
                GL11.glEnd();
            } else {
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(ESP.class);
                if (module == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.ESP");
                }
                Color color = ((ESP)module).getColor(closeable);
                worldRenderer.pos(positionRelativeToPlayer.x / viewDistance * size, positionRelativeToPlayer.y / viewDistance * size, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f).endVertex();
            }
            if (!transform) continue;
            GL11.glPopMatrix();
        }
        if (!triangleMode) {
            tessellator.draw();
        }
        if (circleMode) {
            GL11.glDisable((int)2832);
        }
        GL11.glDisable((int)2881);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        RainbowShader.Companion companion = RainbowShader.Companion;
        boolean x = (Boolean)this.borderRainbowValue.get();
        float positionRelativeToPlayer = ((Number)this.rainbowXValue.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowXValue.get()).floatValue();
        float transform = ((Number)this.rainbowYValue.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowYValue.get()).floatValue();
        float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
        boolean $i$f$begin = false;
        RainbowShader instance$iv = RainbowShader.INSTANCE;
        if (enable$iv != false) {
            void y$iv;
            void x$iv;
            instance$iv.setStrengthX((float)x$iv);
            instance$iv.setStrengthY((float)y$iv);
            instance$iv.setOffset(offset$iv);
            instance$iv.startShader();
        }
        Closeable closeable = instance$iv;
        boolean bl7 = false;
        Throwable throwable = null;
        try {
            RainbowShader it = (RainbowShader)closeable;
            boolean bl8 = false;
            RenderUtils.drawBorder(0.0f, 0.0f, size, size, ((Number)this.borderStrengthValue.get()).floatValue(), new Color(((Number)this.borderRedValue.get()).intValue(), ((Number)this.borderGreenValue.get()).intValue(), ((Number)this.borderBlueValue.get()).intValue(), ((Number)this.borderAlphaValue.get()).intValue()).getRGB());
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            RenderUtils.glColor(((Number)this.borderRedValue.get()).intValue(), ((Number)this.borderGreenValue.get()).intValue(), ((Number)this.borderBlueValue.get()).intValue(), ((Number)this.borderAlphaValue.get()).intValue());
            GL11.glLineWidth((float)((Number)this.borderStrengthValue.get()).floatValue());
            GL11.glBegin((int)1);
            GL11.glVertex2f((float)halfSize, (float)0.0f);
            GL11.glVertex2f((float)halfSize, (float)size);
            GL11.glVertex2f((float)0.0f, (float)halfSize);
            GL11.glVertex2f((float)size, (float)halfSize);
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            Unit unit = Unit.INSTANCE;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return new Border(0.0f, 0.0f, size, size);
    }

    private final IVertexBuffer createFovIndicator(float angle) {
        float end;
        IWorldRenderer worldRenderer = MinecraftInstance.classProvider.getTessellatorInstance().getWorldRenderer();
        worldRenderer.begin(6, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        float start = (90.0f - angle * 0.5f) / 180.0f * (float)Math.PI;
        double radius = 1.0;
        worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
        for (float curr = end = (90.0f + angle * 0.5f) / 180.0f * (float)Math.PI; curr >= start; curr -= 0.15f) {
            IWorldRenderer iWorldRenderer = worldRenderer;
            boolean bl = false;
            float f = (float)Math.cos(curr);
            double d = (double)f * radius;
            bl = false;
            float f2 = (float)Math.sin(curr);
            iWorldRenderer.pos(d, (double)f2 * radius, 0.0).endVertex();
        }
        IVertexBuffer safeVertexBuffer = MinecraftInstance.classProvider.createSafeVertexBuffer(worldRenderer.getVertexFormat());
        worldRenderer.finishDrawing();
        worldRenderer.reset();
        safeVertexBuffer.bufferData(worldRenderer.getByteBuffer());
        return safeVertexBuffer;
    }

    public Radar(double x, double y) {
        super(x, y, 0.0f, null, 12, null);
        this.sizeValue = new FloatValue("Size", 90.0f, 30.0f, 500.0f);
        this.viewDistanceValue = new FloatValue("View Distance", 4.0f, 0.5f, 32.0f);
        this.playerShapeValue = new ListValue("Player Shape", new String[]{"Triangle", "Rectangle", "Circle"}, "Triangle");
        this.playerSizeValue = new FloatValue("Player Size", 2.0f, 0.5f, 20.0f);
        this.useESPColorsValue = new BoolValue("Use ESP Colors", true);
        this.fovSizeValue = new FloatValue("FOV Size", 10.0f, 0.0f, 50.0f);
        this.fovAngleValue = new FloatValue("FOV Angle", 70.0f, 30.0f, 160.0f);
        this.minimapValue = new BoolValue("Minimap", true);
        this.rainbowXValue = new FloatValue("Rainbow-X", -1000.0f, -2000.0f, 2000.0f);
        this.rainbowYValue = new FloatValue("Rainbow-Y", -1000.0f, -2000.0f, 2000.0f);
        this.backgroundRedValue = new IntegerValue("Background Red", 0, 0, 255);
        this.backgroundGreenValue = new IntegerValue("Background Green", 0, 0, 255);
        this.backgroundBlueValue = new IntegerValue("Background Blue", 0, 0, 255);
        this.backgroundAlphaValue = new IntegerValue("Background Alpha", 50, 0, 255);
        this.borderStrengthValue = new FloatValue("Border Strength", 2.0f, 1.0f, 5.0f);
        this.borderRedValue = new IntegerValue("Border Red", 0, 0, 255);
        this.borderGreenValue = new IntegerValue("Border Green", 0, 0, 255);
        this.borderBlueValue = new IntegerValue("Border Blue", 0, 0, 255);
        this.borderAlphaValue = new IntegerValue("Border Alpha", 150, 0, 255);
        this.borderRainbowValue = new BoolValue("Border Rainbow", false);
    }

    public /* synthetic */ Radar(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 5.0;
        }
        if ((n & 2) != 0) {
            d2 = 130.0;
        }
        this(d, d2);
    }

    public Radar() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    static {
        Companion = new Companion(null);
        float f = 2.0f;
        boolean bl = false;
        SQRT_OF_TWO = (float)Math.sqrt(f);
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

