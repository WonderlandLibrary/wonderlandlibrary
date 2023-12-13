// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import java.util.Map;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.HashMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.StringUtils;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.optifine.Config;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractClientPlayer extends EntityPlayer
{
    private NetworkPlayerInfo field_175157_a;
    private ResourceLocation ofLocationCape;
    private static final String __OBFID = "CL_00000935";
    
    public AbstractClientPlayer(final World worldIn, final GameProfile p_i45074_2_) {
        super(worldIn, p_i45074_2_);
        this.ofLocationCape = null;
        final String username = p_i45074_2_.getName();
        this.downloadCape(username);
    }
    
    @Override
    public boolean func_175149_v() {
        final NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getGameProfile().getId());
        return var1 != null && var1.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
    
    public boolean hasCape() {
        return this.func_175155_b() != null;
    }
    
    protected NetworkPlayerInfo func_175155_b() {
        if (this.field_175157_a == null) {
            this.field_175157_a = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getUniqueID());
        }
        return this.field_175157_a;
    }
    
    public boolean hasSkin() {
        final NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 != null && var1.hasLocationSkin();
    }
    
    public ResourceLocation getLocationSkin() {
        final NetworkPlayerInfo var1 = this.func_175155_b();
        return (var1 == null) ? DefaultPlayerSkin.func_177334_a(this.getUniqueID()) : var1.getLocationSkin();
    }
    
    public ResourceLocation getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        }
        if (this.ofLocationCape != null) {
            return this.ofLocationCape;
        }
        final NetworkPlayerInfo var1 = this.func_175155_b();
        return (var1 == null) ? null : var1.getLocationCape();
    }
    
    public static ThreadDownloadImageData getDownloadImageSkin(final ResourceLocation resourceLocationIn, final String username) {
        final TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
        Object var3 = var2.getTexture(resourceLocationIn);
        if (var3 == null) {
            var3 = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.func_177334_a(EntityPlayer.func_175147_b(username)), new ImageBufferDownload());
            var2.loadTexture(resourceLocationIn, (ITextureObject)var3);
        }
        return (ThreadDownloadImageData)var3;
    }
    
    public static ResourceLocation getLocationSkin(final String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }
    
    public String func_175154_l() {
        final NetworkPlayerInfo var1 = this.func_175155_b();
        return (var1 == null) ? DefaultPlayerSkin.func_177332_b(this.getUniqueID()) : var1.getSkinType();
    }
    
    public float func_175156_o() {
        float var1 = 1.0f;
        if (this.capabilities.isFlying) {
            var1 *= 1.1f;
        }
        final IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        var1 *= (float)((var2.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0) / 2.0);
        if (this.capabilities.getWalkSpeed() == 0.0f || Float.isNaN(var1) || Float.isInfinite(var1)) {
            var1 = 1.0f;
        }
        if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
            final int var3 = this.getItemInUseDuration();
            float var4 = var3 / 20.0f;
            if (var4 > 1.0f) {
                var4 = 1.0f;
            }
            else {
                var4 *= var4;
            }
            var1 *= 1.0f - var4 * 0.15f;
        }
        return this.isSprinting() ? 1.15f : 1.0f;
    }
    
    private void downloadCape(String username) {
        if (username != null && !username.isEmpty()) {
            username = StringUtils.stripControlCodes(username);
            final String ofCapeUrl = "http://s.optifine.net/capes/" + username + ".png";
            final MinecraftProfileTexture mpt = new MinecraftProfileTexture(ofCapeUrl, (Map)new HashMap());
            final ResourceLocation rl = new ResourceLocation("skins/" + mpt.getHash());
            final IImageBuffer iib = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();
                
                @Override
                public BufferedImage parseUserSkin(final BufferedImage var1) {
                    return AbstractClientPlayer.this.parseCape(var1);
                }
                
                @Override
                public void func_152634_a() {
                    AbstractClientPlayer.this.ofLocationCape = rl;
                }
            };
            final ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, mpt.getUrl(), null, iib);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureCape);
        }
    }
    
    private BufferedImage parseCape(final BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
}
