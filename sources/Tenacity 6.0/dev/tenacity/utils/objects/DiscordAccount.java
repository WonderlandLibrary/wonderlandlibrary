// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import net.minecraft.util.ResourceLocation;

public class DiscordAccount
{
    public String bannerColor;
    public ResourceLocation discordAvatar;
    public ResourceLocation discordBanner;
    
    public void setBannerColor(final String bannerColor) {
        this.bannerColor = bannerColor;
    }
    
    public void setDiscordAvatar(final ResourceLocation discordAvatar) {
        this.discordAvatar = discordAvatar;
    }
    
    public void setDiscordBanner(final ResourceLocation discordBanner) {
        this.discordBanner = discordBanner;
    }
    
    public String getBannerColor() {
        return this.bannerColor;
    }
    
    public ResourceLocation getDiscordAvatar() {
        return this.discordAvatar;
    }
    
    public ResourceLocation getDiscordBanner() {
        return this.discordBanner;
    }
}
