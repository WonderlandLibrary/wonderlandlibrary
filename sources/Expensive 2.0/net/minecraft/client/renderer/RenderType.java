package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.Hash.Strategy;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.tileentity.EndPortalTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.optifine.Config;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.SmartAnimations;
import net.optifine.render.RenderStateManager;
import net.optifine.render.RenderUtils;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.util.CompareUtils;
import net.optifine.util.CompoundKey;

public abstract class RenderType extends RenderState
{
    private static final RenderType SOLID = makeType("solid", DefaultVertexFormats.BLOCK, 7, 2097152, true, false, State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).build(true));
    private static final RenderType CUTOUT_MIPPED = makeType("cutout_mipped", DefaultVertexFormats.BLOCK, 7, 131072, true, false, State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).alpha(CUTOUT_MIPPED_ALPHA).build(true));
    private static final RenderType CUTOUT = makeType("cutout", DefaultVertexFormats.BLOCK, 7, 131072, true, false, State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET).alpha(CUTOUT_MIPPED_ALPHA).build(true));
    private static final RenderType TRANSLUCENT = makeType("translucent", DefaultVertexFormats.BLOCK, 7, 262144, true, true, getTranslucentState());
    private static final RenderType TRANSLUCENT_MOVING__BLOCK = makeType("translucent_moving_block", DefaultVertexFormats.BLOCK, 7, 262144, false, true, getItemEntityState());
    private static final RenderType TRANSLUCENT_NO_CRUMBLING = makeType("translucent_no_crumbling", DefaultVertexFormats.BLOCK, 7, 262144, false, true, getTranslucentState());
    private static final RenderType LEASH = makeType("leash", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, 7, 256, State.getBuilder().texture(NO_TEXTURE).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).build(false));
    private static final RenderType WATER_MASK = makeType("water_mask", DefaultVertexFormats.POSITION, 7, 256, State.getBuilder().texture(NO_TEXTURE).writeMask(DEPTH_WRITE).build(false));
    private static final RenderType ARMOR_GLINT = makeType("armor_glint", DefaultVertexFormats.POSITION_TEX, 7, 256, State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).layer(field_239235_M_).build(false));
    private static final RenderType ARMOR_ENTITY_GLINT = makeType("armor_entity_glint", DefaultVertexFormats.POSITION_TEX, 7, 256, State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(ENTITY_GLINT_TEXTURING).layer(field_239235_M_).build(false));
    private static final RenderType GLINT_TRANSLUCENT = makeType("glint_translucent", DefaultVertexFormats.POSITION_TEX, 7, 256, State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).target(field_241712_U_).build(false));
    private static final RenderType GLINT = makeType("glint", DefaultVertexFormats.POSITION_TEX, 7, 256, State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).build(false));
    private static final RenderType GLINT_DIRECT = makeType("glint_direct", DefaultVertexFormats.POSITION_TEX, 7, 256, State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).build(false));
    private static final RenderType ENTITY_GLINT = makeType("entity_glint", DefaultVertexFormats.POSITION_TEX, 7, 256, State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).target(field_241712_U_).texturing(ENTITY_GLINT_TEXTURING).build(false));
    private static final RenderType ENTITY_GLINT_DIRECT = makeType("entity_glint_direct", DefaultVertexFormats.POSITION_TEX, 7, 256, State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(ENTITY_GLINT_TEXTURING).build(false));
    private static final RenderType LIGHTNING = makeType("lightning", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, State.getBuilder().writeMask(COLOR_DEPTH_WRITE).transparency(LIGHTNING_TRANSPARENCY).target(field_239238_U_).shadeModel(SHADE_ENABLED).build(false));
    private static final RenderType TRIPWIRE = makeType("tripwire", DefaultVertexFormats.BLOCK, 7, 262144, true, true, getWeatherState());
    public static final Type LINES = makeType("lines", DefaultVertexFormats.POSITION_COLOR, 1, 256, State.getBuilder().line(new LineState(OptionalDouble.empty())).layer(field_239235_M_).transparency(TRANSLUCENT_TRANSPARENCY).target(field_241712_U_).writeMask(COLOR_DEPTH_WRITE).build(false));
    private final VertexFormat vertexFormat;
    private final int drawMode;
    private final int bufferSize;
    private final boolean useDelegate;
    private final boolean needsSorting;
    private final Optional<RenderType> renderType;
    private int id = -1;
    public static final RenderType[] CHUNK_RENDER_TYPES = getChunkRenderTypesArray();
    private static Map<CompoundKey, RenderType> RENDER_TYPES;

    public int ordinal()
    {
        return this.id;
    }

    public boolean isNeedsSorting()
    {
        return this.needsSorting;
    }

    private static RenderType[] getChunkRenderTypesArray()
    {
        RenderType[] arendertype = getBlockRenderTypes().toArray(new RenderType[0]);
        RenderType rendertype;

        for (int i = 0; i < arendertype.length; rendertype.id = i++)
        {
            rendertype = arendertype[i];
        }

        return arendertype;
    }

    public static RenderType getSolid()
    {
        return SOLID;
    }

    public static RenderType getCutoutMipped()
    {
        return CUTOUT_MIPPED;
    }

    public static RenderType getCutout()
    {
        return CUTOUT;
    }

    private static State getTranslucentState()
    {
        return State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).transparency(TRANSLUCENT_TRANSPARENCY).target(field_239236_S_).build(true);
    }

    public static RenderType getTranslucent()
    {
        return TRANSLUCENT;
    }

    private static State getItemEntityState()
    {
        return State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).transparency(TRANSLUCENT_TRANSPARENCY).target(field_241712_U_).build(true);
    }

    public static RenderType getTranslucentMovingBlock()
    {
        return TRANSLUCENT_MOVING__BLOCK;
    }

    public static RenderType getTranslucentNoCrumbling()
    {
        return TRANSLUCENT_NO_CRUMBLING;
    }

    public static RenderType getArmorCutoutNoCull(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("armor_cutout_no_cull", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).layer(field_239235_M_).build(true);
            return makeType("armor_cutout_no_cull", DefaultVertexFormats.ENTITY, 7, 256, true, false, rendertype$state);
        });
    }

    public static RenderType getEntitySolid(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_solid", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
            return makeType("entity_solid", DefaultVertexFormats.ENTITY, 7, 256, true, false, rendertype$state);
        });
    }

    public static RenderType getEntityCutout(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_cutout", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
            return makeType("entity_cutout", DefaultVertexFormats.ENTITY, 7, 256, true, false, rendertype$state);
        });
    }

    public static RenderType getEntityCutoutNoCull(ResourceLocation locationIn, boolean outlineIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_cutout_no_cull", locationIn, outlineIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(outlineIn);
            return makeType("entity_cutout_no_cull", DefaultVertexFormats.ENTITY, 7, 256, true, false, rendertype$state);
        });
    }

    public static RenderType getEntityCutoutNoCull(ResourceLocation locationIn)
    {
        return getEntityCutoutNoCull(locationIn, true);
    }

    public static RenderType getEntityCutoutNoCullZOffset(ResourceLocation locationIn, boolean outlineIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_cutout_no_cull_z_offset", locationIn, outlineIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).layer(field_239235_M_).build(outlineIn);
            return makeType("entity_cutout_no_cull_z_offset", DefaultVertexFormats.ENTITY, 7, 256, true, false, rendertype$state);
        });
    }

    public static RenderType getEntityCutoutNoCullZOffset(ResourceLocation locationIn)
    {
        return getEntityCutoutNoCullZOffset(locationIn, true);
    }

    public static RenderType getItemEntityTranslucentCull(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("item_entity_translucent_cull", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).target(field_241712_U_).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).writeMask(RenderState.COLOR_DEPTH_WRITE).build(true);
            return makeType("item_entity_translucent_cull", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
        });
    }

    public static RenderType getEntityTranslucentCull(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_translucent_cull", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
            return makeType("entity_translucent_cull", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
        });
    }

    public static RenderType getEntityTranslucent(ResourceLocation LocationIn, boolean outlineIn)
    {
        LocationIn = getCustomTexture(LocationIn);
        final ResourceLocation temp = LocationIn;
        return getRenderType("entity_translucent", LocationIn, outlineIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(outlineIn);
            return makeType("entity_translucent", DefaultVertexFormats.ENTITY, 7, 256, true, true, rendertype$state);
        });
    }

    public static RenderType getEntityTranslucent(ResourceLocation locationIn)
    {
        return getEntityTranslucent(locationIn, true);
    }

    public static RenderType getEntitySmoothCutout(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_smooth_cutout", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).alpha(HALF_ALPHA).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).shadeModel(SHADE_ENABLED).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).build(true);
            return makeType("entity_smooth_cutout", DefaultVertexFormats.ENTITY, 7, 256, rendertype$state);
        });
    }

    public static RenderType getBeaconBeam(ResourceLocation locationIn, boolean colorFlagIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("beacon_beam", locationIn, colorFlagIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(colorFlagIn ? TRANSLUCENT_TRANSPARENCY : NO_TRANSPARENCY).writeMask(colorFlagIn ? COLOR_WRITE : COLOR_DEPTH_WRITE).fog(NO_FOG).build(false);
            return makeType("beacon_beam", DefaultVertexFormats.BLOCK, 7, 256, false, true, rendertype$state);
        });
    }

    public static RenderType getEntityDecal(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_decal", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).depthTest(DEPTH_EQUAL).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(false);
            return makeType("entity_decal", DefaultVertexFormats.ENTITY, 7, 256, rendertype$state);
        });
    }

    public static RenderType getEntityNoOutline(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_no_outline", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).writeMask(COLOR_WRITE).build(false);
            return makeType("entity_no_outline", DefaultVertexFormats.ENTITY, 7, 256, false, true, rendertype$state);
        });
    }

    public static RenderType getEntityShadow(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_shadow", locationIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_ENABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).writeMask(COLOR_WRITE).depthTest(DEPTH_LEQUAL).layer(field_239235_M_).build(false);
            return makeType("entity_shadow", DefaultVertexFormats.ENTITY, 7, 256, false, false, rendertype$state);
        });
    }

    public static RenderType getEntityAlpha(ResourceLocation locationIn, float refIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("entity_alpha", locationIn, refIn, () ->
        {
            State rendertype$state = State.getBuilder().texture(new TextureState(temp, false, false)).alpha(new AlphaState(refIn)).cull(CULL_DISABLED).build(true);
            return makeType("entity_alpha", DefaultVertexFormats.ENTITY, 7, 256, rendertype$state);
        });
    }

    public static RenderType getEyes(ResourceLocation locationIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("eyes", locationIn, () ->
        {
            TextureState renderstate$texturestate = new TextureState(temp, false, false);
            return makeType("eyes", DefaultVertexFormats.ENTITY, 7, 256, false, true, State.getBuilder().texture(renderstate$texturestate).transparency(ADDITIVE_TRANSPARENCY).writeMask(COLOR_WRITE).fog(BLACK_FOG).build(false));
        });
    }

    public static RenderType getEnergySwirl(ResourceLocation locationIn, float uIn, float vIn)
    {
        locationIn = getCustomTexture(locationIn);
        final ResourceLocation temp = locationIn;
        return getRenderType("energy_swirl", locationIn, uIn, vIn, () ->
        {
            return makeType("energy_swirl", DefaultVertexFormats.ENTITY, 7, 256, false, true, State.getBuilder().texture(new TextureState(temp, false, false)).texturing(new OffsetTexturingState(uIn, vIn)).fog(BLACK_FOG).transparency(ADDITIVE_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(false));
        });
    }

    public static RenderType getLeash()
    {
        return LEASH;
    }

    public static RenderType getWaterMask()
    {
        return WATER_MASK;
    }

    public static RenderType getOutline(ResourceLocation locationIn)
    {
        return getOutline(locationIn, CULL_DISABLED);
    }

    public static RenderType getOutline(ResourceLocation locationIn, CullState cull)
    {
        return getRenderType("outline", locationIn, cull == CULL_ENABLED, () ->
        {
            return makeType("outline", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256, State.getBuilder().texture(new TextureState(locationIn, false, false)).cull(cull).depthTest(DEPTH_ALWAYS).alpha(DEFAULT_ALPHA).texturing(OUTLINE_TEXTURING).fog(NO_FOG).target(OUTLINE_TARGET).func_230173_a_(OutlineState.IS_OUTLINE));
        });
    }

    public static RenderType getArmorGlint()
    {
    return ARMOR_GLINT;
    }

    public static RenderType getArmorEntityGlint()
    {
        return ARMOR_ENTITY_GLINT;
    }

    public static RenderType getGlintTranslucent()
    {
        return GLINT_TRANSLUCENT;
    }

    public static RenderType getGlint()
    {
        return GLINT;
    }

    public static RenderType getGlintDirect()
    {
        return GLINT_DIRECT;
    }

    public static RenderType getEntityGlint()
    {
        return ENTITY_GLINT;
    }

    public static RenderType getEntityGlintDirect()
    {
        return ENTITY_GLINT_DIRECT;
    }

    public static RenderType getCrumbling(ResourceLocation locationIn)
    {
        return getRenderType("crumbling", locationIn, () ->
        {
            TextureState renderstate$texturestate = new TextureState(locationIn, false, false);
            return makeType("crumbling", DefaultVertexFormats.BLOCK, 7, 256, false, true, State.getBuilder().texture(renderstate$texturestate).alpha(DEFAULT_ALPHA).transparency(CRUMBLING_TRANSPARENCY).writeMask(COLOR_WRITE).layer(POLYGON_OFFSET_LAYERING).build(false));
        });
    }

    public static RenderType getText(ResourceLocation locationIn)
    {
        return getRenderType("text", locationIn, () ->
        {
            return makeType("text", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, false, State.getBuilder().texture(new TextureState(locationIn, false, false)).alpha(DEFAULT_ALPHA).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(LIGHTMAP_ENABLED).build(false));
        });
    }

    public static RenderType getTextSeeThrough(ResourceLocation locationIn)
    {
        return getRenderType("text_see_through", locationIn, () ->
        {
            return makeType("text_see_through", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, false, State.getBuilder().texture(new TextureState(locationIn, false, false)).alpha(DEFAULT_ALPHA).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(LIGHTMAP_ENABLED).depthTest(DEPTH_ALWAYS).writeMask(COLOR_WRITE).build(false));
        });
    }

    public static RenderType getLightning()
    {
        return LIGHTNING;
    }

    private static State getWeatherState()
    {
        return State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).transparency(TRANSLUCENT_TRANSPARENCY).target(field_239238_U_).build(true);
    }

    public static RenderType getTripwire()
    {
        return TRIPWIRE;
    }

    public static RenderType getEndPortal(int iterationIn)
    {
        return getRenderType("end_portal", iterationIn, () ->
        {
            TransparencyState renderstate$transparencystate;
            TextureState renderstate$texturestate;

            if (iterationIn <= 1)
            {
                renderstate$transparencystate = TRANSLUCENT_TRANSPARENCY;
                renderstate$texturestate = new TextureState(EndPortalTileEntityRenderer.END_SKY_TEXTURE, false, false);
            }
            else {
                renderstate$transparencystate = ADDITIVE_TRANSPARENCY;
                renderstate$texturestate = new TextureState(EndPortalTileEntityRenderer.END_PORTAL_TEXTURE, false, false);
            }

            return makeType("end_portal", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, State.getBuilder().transparency(renderstate$transparencystate).texture(renderstate$texturestate).texturing(new PortalTexturingState(iterationIn)).fog(BLACK_FOG).build(false));
        });
    }

    public static RenderType getLines()
    {
        return LINES;
    }

    public RenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
    {
        super(nameIn, setupTaskIn, clearTaskIn);
        this.vertexFormat = formatIn;
        this.drawMode = drawModeIn;
        this.bufferSize = bufferSizeIn;
        this.useDelegate = useDelegateIn;
        this.needsSorting = needsSortingIn;
        this.renderType = Optional.of(this);
    }

    public static Type makeType(String nameIn, VertexFormat vertexFormatIn, int drawModeIn, int bufferSizeIn, State renderStateIn)
    {
        return makeType(nameIn, vertexFormatIn, drawModeIn, bufferSizeIn, false, false, renderStateIn);
    }

    public static Type makeType(String name, VertexFormat vertexFormatIn, int glMode, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, State renderStateIn)
    {
        return Type.getOrCreate(name, vertexFormatIn, glMode, bufferSizeIn, useDelegateIn, needsSortingIn, renderStateIn);
    }

    public void finish(BufferBuilder buffer, int cameraX, int cameraY, int cameraZ)
    {
        if (buffer.isDrawing())
        {
            if (this.needsSorting)
            {
                buffer.sortVertexData((float)cameraX, (float)cameraY, (float)cameraZ);
            }

            if (buffer.animatedSprites != null)
            {
                SmartAnimations.spritesRendered(buffer.animatedSprites);
            }

            buffer.finishDrawing();
            this.setupRenderState();

            if (Config.isShaders())
            {
                RenderUtils.setFlushRenderBuffers(false);
                Shaders.pushProgram();
                ShadersRender.preRender(this, buffer);
            }

            WorldVertexBufferUploader.draw(buffer);

            if (Config.isShaders())
            {
                ShadersRender.postRender(this, buffer);
                Shaders.popProgram();
                RenderUtils.setFlushRenderBuffers(true);
            }

            this.clearRenderState();
        }
    }

    public String toString()
    {
        return this.name;
    }

    public static List<RenderType> getBlockRenderTypes()
    {
        return ImmutableList.of(getSolid(), getCutoutMipped(), getCutout(), getTranslucent(), getTripwire());
    }

    public int getBufferSize()
    {
        return this.bufferSize;
    }

    public VertexFormat getVertexFormat()
    {
        return this.vertexFormat;
    }

    public int getDrawMode()
    {
        return this.drawMode;
    }

    public Optional<RenderType> getOutline()
    {
        return Optional.empty();
    }

    public boolean isColoredOutlineBuffer()
    {
        return false;
    }

    public boolean isUseDelegate()
    {
        return this.useDelegate;
    }

    public Optional<RenderType> getRenderType()
    {
        return this.renderType;
    }

    private static RenderType getRenderType(String p_getRenderType_0_, ResourceLocation p_getRenderType_1_, Supplier<RenderType> p_getRenderType_2_)
    {
        CompoundKey compoundkey = new CompoundKey(p_getRenderType_0_, p_getRenderType_1_);
        return getRenderType(compoundkey, p_getRenderType_2_);
    }

    private static RenderType getRenderType(String p_getRenderType_0_, ResourceLocation p_getRenderType_1_, boolean p_getRenderType_2_, Supplier<RenderType> p_getRenderType_3_)
    {
        CompoundKey compoundkey = new CompoundKey(p_getRenderType_0_, p_getRenderType_1_, p_getRenderType_2_);
        return getRenderType(compoundkey, p_getRenderType_3_);
    }

    private static RenderType getRenderType(String p_getRenderType_0_, ResourceLocation p_getRenderType_1_, float p_getRenderType_2_, Supplier<RenderType> p_getRenderType_3_)
    {
        CompoundKey compoundkey = new CompoundKey(p_getRenderType_0_, p_getRenderType_1_, p_getRenderType_2_);
        return getRenderType(compoundkey, p_getRenderType_3_);
    }

    private static RenderType getRenderType(String p_getRenderType_0_, ResourceLocation p_getRenderType_1_, float p_getRenderType_2_, float p_getRenderType_3_, Supplier<RenderType> p_getRenderType_4_)
    {
        CompoundKey compoundkey = new CompoundKey(p_getRenderType_0_, p_getRenderType_1_, p_getRenderType_2_, p_getRenderType_3_);
        return getRenderType(compoundkey, p_getRenderType_4_);
    }

    private static RenderType getRenderType(String p_getRenderType_0_, int p_getRenderType_1_, Supplier<RenderType> p_getRenderType_2_)
    {
        CompoundKey compoundkey = new CompoundKey(p_getRenderType_0_, p_getRenderType_1_);
        return getRenderType(compoundkey, p_getRenderType_2_);
    }

    private static RenderType getRenderType(CompoundKey p_getRenderType_0_, Supplier<RenderType> p_getRenderType_1_)
    {
        if (RENDER_TYPES == null)
        {
            RENDER_TYPES = new HashMap<>();
        }

        RenderType rendertype = RENDER_TYPES.get(p_getRenderType_0_);

        if (rendertype != null)
        {
            return rendertype;
        }
        else
        {
            rendertype = p_getRenderType_1_.get();
            RENDER_TYPES.put(p_getRenderType_0_, rendertype);
            return rendertype;
        }
    }

    public static ResourceLocation getCustomTexture(ResourceLocation p_getCustomTexture_0_)
    {
        if (Config.isRandomEntities())
        {
            p_getCustomTexture_0_ = RandomEntities.getTextureLocation(p_getCustomTexture_0_);
        }

        if (EmissiveTextures.isActive())
        {
            p_getCustomTexture_0_ = EmissiveTextures.getEmissiveTexture(p_getCustomTexture_0_);
        }

        return p_getCustomTexture_0_;
    }

    public boolean isEntitySolid()
    {
        return this.getName().equals("entity_solid");
    }

    public static int getCountRenderStates()
    {
        return LINES.renderState.renderStates.size();
    }

    public ResourceLocation getTextureLocation()
    {
        return null;
    }

    public boolean isGlint()
    {
        return this.getTextureLocation() == ItemRenderer.RES_ITEM_GLINT;
    }

    static enum OutlineState
    {
        NONE("none"),
        IS_OUTLINE("is_outline"),
        AFFECTS_OUTLINE("affects_outline");

        private final String name;

        private OutlineState(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }
    }

    public static final class State
    {
        private final TextureState texture;
        private final TransparencyState transparency;
        private final DiffuseLightingState diffuseLighting;
        private final ShadeModelState shadowModel;
        private final AlphaState alpha;
        private final DepthTestState depthTest;
        private final CullState cull;
        private final LightmapState lightmap;
        private final OverlayState overlay;
        private final FogState fog;
        private final LayerState layer;
        private final TargetState target;
        private final TexturingState texturing;
        private final WriteMaskState writeMask;
        private final LineState line;
        private final OutlineState field_230171_p_;
        private final ImmutableList<RenderState> renderStates;

        private State(TextureState p_i230053_1_, TransparencyState p_i230053_2_, DiffuseLightingState p_i230053_3_, ShadeModelState p_i230053_4_, AlphaState p_i230053_5_, DepthTestState p_i230053_6_, CullState p_i230053_7_, LightmapState p_i230053_8_, OverlayState p_i230053_9_, FogState p_i230053_10_, LayerState p_i230053_11_, TargetState p_i230053_12_, TexturingState p_i230053_13_, WriteMaskState p_i230053_14_, LineState p_i230053_15_, OutlineState p_i230053_16_)
        {
            this.texture = p_i230053_1_;
            this.transparency = p_i230053_2_;
            this.diffuseLighting = p_i230053_3_;
            this.shadowModel = p_i230053_4_;
            this.alpha = p_i230053_5_;
            this.depthTest = p_i230053_6_;
            this.cull = p_i230053_7_;
            this.lightmap = p_i230053_8_;
            this.overlay = p_i230053_9_;
            this.fog = p_i230053_10_;
            this.layer = p_i230053_11_;
            this.target = p_i230053_12_;
            this.texturing = p_i230053_13_;
            this.writeMask = p_i230053_14_;
            this.line = p_i230053_15_;
            this.field_230171_p_ = p_i230053_16_;
            this.renderStates = ImmutableList.of(this.texture, this.transparency, this.diffuseLighting, this.shadowModel, this.alpha, this.depthTest, this.cull, this.lightmap, this.overlay, this.fog, this.layer, this.target, this.texturing, this.writeMask, this.line);
        }

        public boolean equals(Object p_equals_1_)
        {
            if (this == p_equals_1_)
            {
                return true;
            }
            else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
            {
                State rendertype$state = (State)p_equals_1_;
                return this.field_230171_p_ == rendertype$state.field_230171_p_ && this.renderStates.equals(rendertype$state.renderStates);
            }
            else
            {
                return false;
            }
        }

        public int hashCode()
        {
            return CompareUtils.hash(this.renderStates, this.field_230171_p_);
        }

        public String toString()
        {
            return "CompositeState[" + this.renderStates + ", outlineProperty=" + this.field_230171_p_ + ']';
        }

        public static Builder getBuilder()
        {
            return new Builder();
        }

        public Builder getCopyBuilder()
        {
            Builder rendertype$state$builder = new Builder();
            rendertype$state$builder.texture(this.texture);
            rendertype$state$builder.transparency(this.transparency);
            rendertype$state$builder.diffuseLighting(this.diffuseLighting);
            rendertype$state$builder.shadeModel(this.shadowModel);
            rendertype$state$builder.alpha(this.alpha);
            rendertype$state$builder.depthTest(this.depthTest);
            rendertype$state$builder.cull(this.cull);
            rendertype$state$builder.lightmap(this.lightmap);
            rendertype$state$builder.overlay(this.overlay);
            rendertype$state$builder.fog(this.fog);
            rendertype$state$builder.layer(this.layer);
            rendertype$state$builder.target(this.target);
            rendertype$state$builder.texturing(this.texturing);
            rendertype$state$builder.writeMask(this.writeMask);
            rendertype$state$builder.line(this.line);
            return rendertype$state$builder;
        }

        public static class Builder
        {
            private TextureState texture = RenderState.NO_TEXTURE;
            private TransparencyState transparency = RenderState.NO_TRANSPARENCY;
            private DiffuseLightingState diffuseLighting = RenderState.DIFFUSE_LIGHTING_DISABLED;
            private ShadeModelState shadeModel = RenderState.SHADE_DISABLED;
            private AlphaState alpha = RenderState.ZERO_ALPHA;
            private DepthTestState depthTest = RenderState.DEPTH_LEQUAL;
            private CullState cull = RenderState.CULL_ENABLED;
            private LightmapState lightmap = RenderState.LIGHTMAP_DISABLED;
            private OverlayState overlay = RenderState.OVERLAY_DISABLED;
            private FogState fog = RenderState.FOG;
            private LayerState layer = RenderState.NO_LAYERING;
            private TargetState target = RenderState.MAIN_TARGET;
            private TexturingState texturing = RenderState.DEFAULT_TEXTURING;
            private WriteMaskState writeMask = RenderState.COLOR_DEPTH_WRITE;
            private LineState line = RenderState.DEFAULT_LINE;

            private Builder()
            {
            }

            public Builder texture(TextureState p_228724_1_)
            {
                this.texture = p_228724_1_;
                return this;
            }

            public Builder transparency(TransparencyState p_228726_1_)
            {
                this.transparency = p_228726_1_;
                return this;
            }

            public Builder diffuseLighting(DiffuseLightingState p_228716_1_)
            {
                this.diffuseLighting = p_228716_1_;
                return this;
            }

            public Builder shadeModel(ShadeModelState p_228723_1_)
            {
                this.shadeModel = p_228723_1_;
                return this;
            }

            public Builder alpha(AlphaState p_228713_1_)
            {
                this.alpha = p_228713_1_;
                return this;
            }

            public Builder depthTest(DepthTestState p_228715_1_)
            {
                this.depthTest = p_228715_1_;
                return this;
            }

            public Builder cull(CullState p_228714_1_)
            {
                this.cull = p_228714_1_;
                return this;
            }

            public Builder lightmap(LightmapState p_228719_1_)
            {
                this.lightmap = p_228719_1_;
                return this;
            }

            public Builder overlay(OverlayState p_228722_1_)
            {
                this.overlay = p_228722_1_;
                return this;
            }

            public Builder fog(FogState p_228717_1_)
            {
                this.fog = p_228717_1_;
                return this;
            }

            public Builder layer(LayerState p_228718_1_)
            {
                this.layer = p_228718_1_;
                return this;
            }

            public Builder target(TargetState p_228721_1_)
            {
                this.target = p_228721_1_;
                return this;
            }

            public Builder texturing(TexturingState p_228725_1_)
            {
                this.texturing = p_228725_1_;
                return this;
            }

            public Builder writeMask(WriteMaskState p_228727_1_)
            {
                this.writeMask = p_228727_1_;
                return this;
            }

            public Builder line(LineState p_228720_1_)
            {
                this.line = p_228720_1_;
                return this;
            }

            public State build(boolean outlineIn)
            {
                return this.func_230173_a_(outlineIn ? OutlineState.AFFECTS_OUTLINE : OutlineState.NONE);
            }

            public State func_230173_a_(OutlineState p_230173_1_)
            {
                return new State(this.texture, this.transparency, this.diffuseLighting, this.shadeModel, this.alpha, this.depthTest, this.cull, this.lightmap, this.overlay, this.fog, this.layer, this.target, this.texturing, this.writeMask, this.line, p_230173_1_);
            }
        }
    }

    static final class Type extends RenderType
    {
        private static final ObjectOpenCustomHashSet<Type> TYPES = new ObjectOpenCustomHashSet<>(EqualityStrategy.INSTANCE);
        private final State renderState;
        private final int hashCode;
        private final Optional<RenderType> outlineRenderType;
        private final boolean field_230170_V_;
        private Map<ResourceLocation, Type> mapTextured = new HashMap<>();

        private Type(String p_i225993_1_, VertexFormat p_i225993_2_, int p_i225993_3_, int p_i225993_4_, boolean p_i225993_5_, boolean p_i225993_6_, State p_i225993_7_)
        {
            super(p_i225993_1_, p_i225993_2_, p_i225993_3_, p_i225993_4_, p_i225993_5_, p_i225993_6_, () ->
            {
                RenderStateManager.setupRenderStates(p_i225993_7_.renderStates);
            }, () ->
            {
                RenderStateManager.clearRenderStates(p_i225993_7_.renderStates);
            });
            this.renderState = p_i225993_7_;
            this.outlineRenderType = p_i225993_7_.field_230171_p_ == OutlineState.AFFECTS_OUTLINE ? p_i225993_7_.texture.texture().map((p_lambda$new$2_1_) ->
            {
                return getOutline(p_lambda$new$2_1_, p_i225993_7_.cull);
            }) : Optional.empty();
            this.field_230170_V_ = p_i225993_7_.field_230171_p_ == OutlineState.IS_OUTLINE;
            this.hashCode = CompareUtils.hash(super.hashCode(), p_i225993_7_);
        }

        private static Type getOrCreate(String p_228676_0_, VertexFormat p_228676_1_, int p_228676_2_, int p_228676_3_, boolean p_228676_4_, boolean p_228676_5_, State p_228676_6_)
        {
            return TYPES.addOrGet(new Type(p_228676_0_, p_228676_1_, p_228676_2_, p_228676_3_, p_228676_4_, p_228676_5_, p_228676_6_));
        }

        public Optional<RenderType> getOutline()
        {
            return this.outlineRenderType;
        }

        public boolean isColoredOutlineBuffer()
        {
            return this.field_230170_V_;
        }

        public boolean equals(@Nullable Object p_equals_1_)
        {
            return this == p_equals_1_;
        }

        public int hashCode()
        {
            return this.hashCode;
        }

        public String toString()
        {
            return "" + this.name + ":RenderType[" + this.renderState + ", " + ']';
        }

        public Type getTextured(ResourceLocation p_getTextured_1_)
        {
            if (p_getTextured_1_ == null)
            {
                return this;
            }
            else
            {
                Optional<ResourceLocation> optional = this.renderState.texture.texture();

                if (!optional.isPresent())
                {
                    return this;
                }
                else
                {
                    ResourceLocation resourcelocation = optional.get();

                    if (resourcelocation == null)
                    {
                        return this;
                    }
                    else if (p_getTextured_1_.equals(resourcelocation))
                    {
                        return this;
                    }
                    else
                    {
                        Type rendertype$type = this.mapTextured.get(p_getTextured_1_);

                        if (rendertype$type == null)
                        {
                            State.Builder rendertype$state$builder = this.renderState.getCopyBuilder();
                            rendertype$state$builder.texture(new TextureState(p_getTextured_1_, this.renderState.texture.isBlur(), this.renderState.texture.isMipmap()));
                            State rendertype$state = rendertype$state$builder.build(this.field_230170_V_);
                            rendertype$type = makeType(this.name, this.getVertexFormat(), this.getDrawMode(), this.getBufferSize(), this.isUseDelegate(), this.isNeedsSorting(), rendertype$state);
                            this.mapTextured.put(p_getTextured_1_, rendertype$type);
                        }

                        return rendertype$type;
                    }
                }
            }
        }

        public ResourceLocation getTextureLocation()
        {
            Optional<ResourceLocation> optional = this.renderState.texture.texture();
            return !optional.isPresent() ? null : optional.get();
        }

        static enum EqualityStrategy implements Strategy<Type>
        {
            INSTANCE;

            public int hashCode(@Nullable Type p_hashCode_1_)
            {
                return p_hashCode_1_ == null ? 0 : p_hashCode_1_.hashCode;
            }

            public boolean equals(@Nullable Type p_equals_1_, @Nullable Type p_equals_2_)
            {
                if (p_equals_1_ == p_equals_2_)
                {
                    return true;
                }
                else
                {
                    return p_equals_1_ != null && p_equals_2_ != null ? Objects.equals(p_equals_1_.renderState, p_equals_2_.renderState) : false;
                }
            }
        }
    }
}
