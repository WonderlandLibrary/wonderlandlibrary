/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture$Type
 */
package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntitySkullRenderer
extends TileEntitySpecialRenderer<TileEntitySkull> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
    private static final ResourceLocation CREEPER_TEXTURES;
    private static final ResourceLocation ZOMBIE_TEXTURES;
    public static TileEntitySkullRenderer instance;
    private final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();
    private static final ResourceLocation WITHER_SKELETON_TEXTURES;

    static {
        WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
        ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
        CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
    }

    @Override
    public void renderTileEntityAt(TileEntitySkull tileEntitySkull, double d, double d2, double d3, float f, int n) {
        EnumFacing enumFacing = EnumFacing.getFront(tileEntitySkull.getBlockMetadata() & 7);
        this.renderSkull((float)d, (float)d2, (float)d3, enumFacing, (float)(tileEntitySkull.getSkullRotation() * 360) / 16.0f, tileEntitySkull.getSkullType(), tileEntitySkull.getPlayerProfile(), n);
    }

    public void renderSkull(float f, float f2, float f3, EnumFacing enumFacing, float f4, int n, GameProfile gameProfile, int n2) {
        ModelSkeletonHead modelSkeletonHead = this.skeletonHead;
        if (n2 >= 0) {
            this.bindTexture(DESTROY_STAGES[n2]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        } else {
            switch (n) {
                default: {
                    this.bindTexture(SKELETON_TEXTURES);
                    break;
                }
                case 1: {
                    this.bindTexture(WITHER_SKELETON_TEXTURES);
                    break;
                }
                case 2: {
                    this.bindTexture(ZOMBIE_TEXTURES);
                    modelSkeletonHead = this.humanoidHead;
                    break;
                }
                case 3: {
                    modelSkeletonHead = this.humanoidHead;
                    ResourceLocation resourceLocation = DefaultPlayerSkin.getDefaultSkinLegacy();
                    if (gameProfile != null) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(gameProfile);
                        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                            resourceLocation = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                        } else {
                            UUID uUID = EntityPlayer.getUUID(gameProfile);
                            resourceLocation = DefaultPlayerSkin.getDefaultSkin(uUID);
                        }
                    }
                    this.bindTexture(resourceLocation);
                    break;
                }
                case 4: {
                    this.bindTexture(CREEPER_TEXTURES);
                }
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        if (enumFacing != EnumFacing.UP) {
            switch (enumFacing) {
                case NORTH: {
                    GlStateManager.translate(f + 0.5f, f2 + 0.25f, f3 + 0.74f);
                    break;
                }
                case SOUTH: {
                    GlStateManager.translate(f + 0.5f, f2 + 0.25f, f3 + 0.26f);
                    f4 = 180.0f;
                    break;
                }
                case WEST: {
                    GlStateManager.translate(f + 0.74f, f2 + 0.25f, f3 + 0.5f);
                    f4 = 270.0f;
                    break;
                }
                default: {
                    GlStateManager.translate(f + 0.26f, f2 + 0.25f, f3 + 0.5f);
                    f4 = 90.0f;
                    break;
                }
            }
        } else {
            GlStateManager.translate(f + 0.5f, f2, f3 + 0.5f);
        }
        float f5 = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        ((ModelBase)modelSkeletonHead).render(null, 0.0f, 0.0f, 0.0f, f4, 0.0f, f5);
        GlStateManager.popMatrix();
        if (n2 >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    @Override
    public void setRendererDispatcher(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super.setRendererDispatcher(tileEntityRendererDispatcher);
        instance = this;
    }
}

