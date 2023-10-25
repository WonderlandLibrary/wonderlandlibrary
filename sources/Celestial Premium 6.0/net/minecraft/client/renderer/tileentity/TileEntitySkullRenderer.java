/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragonHead;
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
    private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
    private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");
    private final ModelDragonHead dragonHead = new ModelDragonHead(0.0f);
    public static TileEntitySkullRenderer instance;
    private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
    private final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();

    @Override
    public void func_192841_a(TileEntitySkull p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
        EnumFacing enumfacing = EnumFacing.getFront(p_192841_1_.getBlockMetadata() & 7);
        float f = p_192841_1_.getAnimationProgress(p_192841_8_);
        this.renderSkull((float)p_192841_2_, (float)p_192841_4_, (float)p_192841_6_, enumfacing, (float)(p_192841_1_.getSkullRotation() * 360) / 16.0f, p_192841_1_.getSkullType(), p_192841_1_.getPlayerProfile(), p_192841_9_, f);
    }

    @Override
    public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn) {
        super.setRendererDispatcher(rendererDispatcherIn);
        instance = this;
    }

    public void renderSkull(float x, float y, float z, EnumFacing facing, float p_188190_5_, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks) {
        ModelBase modelbase = this.skeletonHead;
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        } else {
            switch (skullType) {
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
                    modelbase = this.humanoidHead;
                    break;
                }
                case 3: {
                    modelbase = this.humanoidHead;
                    ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
                    if (profile != null) {
                        Minecraft minecraft = Minecraft.getMinecraft();
                        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);
                        if (map.containsKey((Object)MinecraftProfileTexture.Type.SKIN)) {
                            resourcelocation = minecraft.getSkinManager().loadSkin(map.get((Object)MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                        } else {
                            UUID uuid = EntityPlayer.getUUID(profile);
                            resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
                        }
                    }
                    this.bindTexture(resourcelocation);
                    break;
                }
                case 4: {
                    this.bindTexture(CREEPER_TEXTURES);
                    break;
                }
                case 5: {
                    this.bindTexture(DRAGON_TEXTURES);
                    modelbase = this.dragonHead;
                }
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        if (facing == EnumFacing.UP) {
            GlStateManager.translate(x + 0.5f, y, z + 0.5f);
        } else {
            switch (facing) {
                case NORTH: {
                    GlStateManager.translate(x + 0.5f, y + 0.25f, z + 0.74f);
                    break;
                }
                case SOUTH: {
                    GlStateManager.translate(x + 0.5f, y + 0.25f, z + 0.26f);
                    p_188190_5_ = 180.0f;
                    break;
                }
                case WEST: {
                    GlStateManager.translate(x + 0.74f, y + 0.25f, z + 0.5f);
                    p_188190_5_ = 270.0f;
                    break;
                }
                default: {
                    GlStateManager.translate(x + 0.26f, y + 0.25f, z + 0.5f);
                    p_188190_5_ = 90.0f;
                }
            }
        }
        float f = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        if (skullType == 3) {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        }
        modelbase.render(null, animateTicks, 0.0f, 0.0f, p_188190_5_, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

