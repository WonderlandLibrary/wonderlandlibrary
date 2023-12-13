// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import dev.xulu.settings.OnChangedValue;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.DynamicTexture;
import javax.imageio.ImageIO;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.util.VectorUtils;
import net.minecraft.util.math.Vec3d;
import java.util.Comparator;
import java.util.Collection;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import java.io.IOException;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.culling.Frustum;
import com.elementars.eclient.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.ResourceLocation;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ImageESP extends Module
{
    private final Value<Boolean> noRenderPlayers;
    private final Value<CachedImage> imageUrl;
    private ResourceLocation waifu;
    private ICamera camera;
    
    public ImageESP() {
        super("ImageESP", "overlay cute images over players", 0, Category.RENDER, true);
        this.noRenderPlayers = this.register(new Value<Boolean>("No Render Players", this, false));
        this.imageUrl = this.register(new Value<CachedImage>("Image", this, CachedImage.LAUREN, CachedImage.values())).onChanged(imagesOnChangedValue -> {
            this.waifu = null;
            this.onLoad();
            return;
        });
        this.camera = (ICamera)new Frustum();
        this.onLoad();
    }
    
    @Override
    public void onEnable() {
        ImageESP.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        ImageESP.EVENT_BUS.unregister((Object)this);
    }
    
    private <T> BufferedImage getImage(final T source, final ThrowingFunction<T, BufferedImage> readFunction) {
        try {
            return readFunction.apply(source);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private boolean shouldDraw(final EntityLivingBase entity) {
        return !entity.equals((Object)ImageESP.mc.player) && entity.getHealth() > 0.0f && EntityUtil.isPlayer((Entity)entity);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (this.waifu == null) {
            return;
        }
        final double d3 = ImageESP.mc.player.lastTickPosX + (ImageESP.mc.player.posX - ImageESP.mc.player.lastTickPosX) * event.getPartialTicks();
        final double d4 = ImageESP.mc.player.lastTickPosY + (ImageESP.mc.player.posY - ImageESP.mc.player.lastTickPosY) * event.getPartialTicks();
        final double d5 = ImageESP.mc.player.lastTickPosZ + (ImageESP.mc.player.posZ - ImageESP.mc.player.lastTickPosZ) * event.getPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        final List<EntityPlayer> players = new ArrayList<EntityPlayer>(ImageESP.mc.world.playerEntities);
        players.sort(Comparator.comparing(entityPlayer -> ImageESP.mc.player.getDistance((Entity)entityPlayer)).reversed());
        for (final EntityPlayer player : players) {
            if (player != ImageESP.mc.getRenderViewEntity() && player.isEntityAlive() && this.camera.isBoundingBoxInFrustum(player.getEntityBoundingBox())) {
                final EntityLivingBase living = (EntityLivingBase)player;
                final Vec3d bottomVec = EntityUtil.getInterpolatedPos((Entity)living, event.getPartialTicks());
                final Vec3d topVec = bottomVec.add(new Vec3d(0.0, player.getRenderBoundingBox().maxY - player.posY, 0.0));
                final VectorUtils.ScreenPos top = VectorUtils._toScreen(topVec.x, topVec.y, topVec.z);
                final VectorUtils.ScreenPos bot = VectorUtils._toScreen(bottomVec.x, bottomVec.y, bottomVec.z);
                if (!top.isVisible && !bot.isVisible) {
                    continue;
                }
                final int height;
                final int width = height = bot.y - top.y;
                final int x = (int)(top.x - width / 1.8);
                final int y = top.y;
                ImageESP.mc.renderEngine.bindTexture(this.waifu);
                GlStateManager.color(255.0f, 255.0f, 255.0f);
                Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, (float)width, (float)height);
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Pre event) {
        if (this.noRenderPlayers.getValue() && !event.getEntity().equals((Object)ImageESP.mc.player)) {
            event.setCanceled(true);
        }
    }
    
    public void onLoad() {
        BufferedImage image = null;
        try {
            if (this.getFile(this.imageUrl.getValue().getName()) != null) {
                image = this.getImage(this.getFile(this.imageUrl.getValue().getName()), ImageIO::read);
            }
            if (image == null) {
                this.LOGGER.warn("Failed to load image");
            }
            else {
                final DynamicTexture dynamicTexture = new DynamicTexture(image);
                dynamicTexture.loadTexture(ImageESP.mc.getResourceManager());
                this.waifu = ImageESP.mc.getTextureManager().getDynamicTextureLocation("XULU_" + this.imageUrl.getValue().name(), dynamicTexture);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private InputStream getFile(final String string) {
        return ImageESP.class.getResourceAsStream(string);
    }
    
    private enum CachedImage
    {
        LAUREN("/images/lauren.png"), 
        DELTA("/images/delta.png"), 
        OMEGA("/images/omega.png"), 
        TRIANGLE("/images/triangle.png"), 
        WAIFU("/images/waifu.png"), 
        WAIFU2("/images/waifu2.png"), 
        XULU("/images/xulutransparent.png"), 
        PETER("/images/peter.png"), 
        LOTUS("/images/lotus.png"), 
        LOGAN("/images/logan.png"), 
        ZHN("/images/zhn.png"), 
        BIGOUNCE("/images/bigounce.png"), 
        JOINT("/images/joint.png");
        
        String name;
        
        private CachedImage(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
    
    @FunctionalInterface
    private interface ThrowingFunction<T, R>
    {
        R apply(final T p0) throws IOException;
    }
}
