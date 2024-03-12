// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render;

import org.lwjgl.opengl.GLContext;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import java.util.function.Function;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.opengl.GL15;
import java.util.HashSet;
import java.util.ArrayList;
import dev.tenacity.event.impl.game.TickEvent;
import net.minecraft.scoreboard.Team;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import dev.tenacity.event.impl.game.RenderTickEvent;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityArmorStand;
import dev.tenacity.event.impl.render.RendererLivingEntityEvent;
import dev.tenacity.Tenacity;
import net.minecraft.entity.Entity;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.entity.RenderManager;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class EntityCulling extends Module
{
    private static final NumberSetting cullingDelay;
    private static final ModeSetting cullingMode;
    private static final NumberSetting entityCullingDis;
    private static final NumberSetting mobCullingDis;
    private static final NumberSetting playerCullingDis;
    private static final NumberSetting passiveCullingDis;
    private static final RenderManager renderManager;
    private static final ConcurrentHashMap<UUID, OcclusionQuery> queries;
    private static final boolean SUPPORT_NEW_GL;
    public static boolean shouldPerformCulling;
    private int destroyTimer;
    
    public EntityCulling() {
        super("EntityCulling", "Entity Culling", Category.RENDER, "Culls entities that are out of range");
        EntityCulling.entityCullingDis.addParent(EntityCulling.cullingMode, modeSetting -> modeSetting.is("Grouped"));
        EntityCulling.playerCullingDis.addParent(EntityCulling.cullingMode, modeSetting -> modeSetting.is("Custom"));
        EntityCulling.mobCullingDis.addParent(EntityCulling.cullingMode, modeSetting -> modeSetting.is("Custom"));
        EntityCulling.passiveCullingDis.addParent(EntityCulling.cullingMode, modeSetting -> modeSetting.is("Custom"));
        this.addSettings(EntityCulling.cullingDelay, EntityCulling.cullingMode, EntityCulling.entityCullingDis, EntityCulling.playerCullingDis, EntityCulling.mobCullingDis, EntityCulling.passiveCullingDis);
    }
    
    public static boolean renderItem(final Entity stack) {
        return Tenacity.INSTANCE.isEnabled(EntityCulling.class) && EntityCulling.shouldPerformCulling && stack.worldObj == EntityCulling.mc.thePlayer.worldObj && checkEntity(stack);
    }
    
    @Override
    public void onRendererLivingEntityEvent(final RendererLivingEntityEvent e) {
        if (e.isPost() || !EntityCulling.shouldPerformCulling) {
            return;
        }
        final EntityLivingBase entity = e.getEntity();
        final boolean armorstand = entity instanceof EntityArmorStand;
        if (entity == EntityCulling.mc.thePlayer || entity.worldObj != EntityCulling.mc.thePlayer.worldObj || entity.isInvisibleToPlayer(EntityCulling.mc.thePlayer)) {
            return;
        }
        if (checkEntity(entity)) {
            e.cancel();
            if (!canRenderName(entity)) {
                return;
            }
            final double x = e.getX();
            final double y = e.getY();
            final double z = e.getZ();
            final RendererLivingEntity<EntityLivingBase> renderer = (RendererLivingEntity<EntityLivingBase>)e.getRenderer();
            renderer.renderName(entity, x, y, z);
        }
        if (entity.isInvisible() && entity instanceof EntityPlayer) {
            e.cancel();
        }
        if (EntityCulling.shouldPerformCulling) {
            final float entityDistance = entity.getDistanceToEntity(EntityCulling.mc.thePlayer);
            final String mode = EntityCulling.cullingMode.getMode();
            switch (mode) {
                case "Grouped": {
                    if (entityDistance > EntityCulling.entityCullingDis.getValue()) {
                        e.cancel();
                        break;
                    }
                    break;
                }
                case "Custom": {
                    if (entity instanceof IMob && entityDistance > EntityCulling.mobCullingDis.getValue()) {
                        e.cancel();
                        break;
                    }
                    if ((entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof EntityWaterMob) && entityDistance > EntityCulling.passiveCullingDis.getValue()) {
                        e.cancel();
                        break;
                    }
                    if (entity instanceof EntityPlayer && entityDistance > EntityCulling.playerCullingDis.getValue()) {
                        e.cancel();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void onRenderTickEvent(final RenderTickEvent e) {
        if (e.isPre()) {
            EntityCulling.mc.addScheduledTask(this::check);
        }
    }
    
    public static boolean canRenderName(final EntityLivingBase entity) {
        final EntityPlayerSP player = EntityCulling.mc.thePlayer;
        if (entity instanceof EntityPlayer && entity != player) {
            final Team otherEntityTeam = entity.getTeam();
            final Team playerTeam = player.getTeam();
            if (otherEntityTeam != null) {
                final Team.EnumVisible teamVisibilityRule = otherEntityTeam.getNameTagVisibility();
                switch (teamVisibilityRule) {
                    case NEVER: {
                        return false;
                    }
                    case HIDE_FOR_OTHER_TEAMS: {
                        return playerTeam == null || otherEntityTeam.isSameTeam(playerTeam);
                    }
                    case HIDE_FOR_OWN_TEAM: {
                        return playerTeam == null || !otherEntityTeam.isSameTeam(playerTeam);
                    }
                    default: {
                        return true;
                    }
                }
            }
        }
        return Minecraft.isGuiEnabled() && entity != EntityCulling.mc.getRenderManager().livingPlayer && (entity instanceof EntityArmorStand || !entity.isInvisibleToPlayer(player)) && entity.riddenByEntity == null;
    }
    
    @Override
    public void onTickEvent(final TickEvent e) {
        if (e.isPost() || this.destroyTimer++ < 120) {
            return;
        }
        this.destroyTimer = 0;
        final WorldClient theWorld = EntityCulling.mc.theWorld;
        if (theWorld == null) {
            return;
        }
        final List<UUID> remove = new ArrayList<UUID>();
        final Set<UUID> loaded = new HashSet<UUID>();
        for (final Entity entity : theWorld.loadedEntityList) {
            loaded.add(entity.getUniqueID());
        }
        for (final OcclusionQuery value : EntityCulling.queries.values()) {
            if (loaded.contains(value.uuid)) {
                continue;
            }
            remove.add(value.uuid);
            if (value.nextQuery == 0) {
                continue;
            }
            GL15.glDeleteQueries(value.nextQuery);
        }
        for (final UUID uuid : remove) {
            EntityCulling.queries.remove(uuid);
        }
    }
    
    private void check() {
        long delay = 0L;
        switch (EntityCulling.cullingDelay.getValue().intValue() - 1) {
            case 0: {
                delay = 10L;
                break;
            }
            case 1: {
                delay = 25L;
                break;
            }
            case 2: {
                delay = 50L;
                break;
            }
        }
        final long nanoTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        for (final OcclusionQuery query : EntityCulling.queries.values()) {
            if (query.nextQuery != 0) {
                final long queryObject = GL15.glGetQueryObjecti(query.nextQuery, 34919);
                if (queryObject != 0L) {
                    query.occluded = (GL15.glGetQueryObjecti(query.nextQuery, 34918) == 0);
                    GL15.glDeleteQueries(query.nextQuery);
                    query.nextQuery = 0;
                }
            }
            if (query.nextQuery == 0 && nanoTime - query.executionTime > delay) {
                query.executionTime = nanoTime;
                query.refresh = true;
            }
        }
    }
    
    private static boolean checkEntity(final Entity entity) {
        final OcclusionQuery query = EntityCulling.queries.computeIfAbsent(entity.getUniqueID(), OcclusionQuery::new);
        if (query.refresh) {
            query.nextQuery = getQuery();
            query.refresh = false;
            final int mode = EntityCulling.SUPPORT_NEW_GL ? 35887 : 35092;
            GL15.glBeginQuery(mode, query.nextQuery);
            drawSelectionBoundingBox(entity.getEntityBoundingBox().expand(0.2, 0.2, 0.2).offset(-EntityCulling.renderManager.renderPosX, -EntityCulling.renderManager.renderPosY, -EntityCulling.renderManager.renderPosZ));
            GL15.glEndQuery(mode);
        }
        return query.occluded;
    }
    
    public static void drawSelectionBoundingBox(final AxisAlignedBB b) {
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(false, false, false, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(8, DefaultVertexFormats.POSITION);
        worldrenderer.pos(b.maxX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.maxX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.maxY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.maxY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.maxZ).endVertex();
        worldrenderer.pos(b.minX, b.minY, b.minZ).endVertex();
        worldrenderer.pos(b.maxX, b.minY, b.minZ).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableAlpha();
    }
    
    private static int getQuery() {
        try {
            return GL15.glGenQueries();
        }
        catch (Throwable throwable) {
            return 0;
        }
    }
    
    static {
        cullingDelay = new NumberSetting("Culling Delay", 2.0, 3.0, 1.0, 1.0);
        cullingMode = new ModeSetting("Culling Mode", "Grouped", new String[] { "Grouped", "Custom" });
        entityCullingDis = new NumberSetting("Culling Distance", 45.0, 150.0, 10.0, 1.0);
        mobCullingDis = new NumberSetting("Mob Cull Distance", 40.0, 150.0, 10.0, 1.0);
        playerCullingDis = new NumberSetting("Player Cull Distance", 45.0, 150.0, 10.0, 1.0);
        passiveCullingDis = new NumberSetting("Passive Cull Distance", 30.0, 150.0, 10.0, 1.0);
        renderManager = EntityCulling.mc.getRenderManager();
        queries = new ConcurrentHashMap<UUID, OcclusionQuery>();
        SUPPORT_NEW_GL = GLContext.getCapabilities().OpenGL33;
        EntityCulling.shouldPerformCulling = false;
    }
    
    static class OcclusionQuery
    {
        private final UUID uuid;
        private int nextQuery;
        private boolean refresh;
        private boolean occluded;
        private long executionTime;
        
        public OcclusionQuery(final UUID uuid) {
            this.refresh = true;
            this.executionTime = 0L;
            this.uuid = uuid;
        }
    }
}
