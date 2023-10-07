package net.minecraft.client.entity;

import cc.novoline.Novoline;
import cc.novoline.utils.tasks.FutureTask;
import com.mojang.authlib.GameProfile;
import static net.minecraft.client.Minecraft.getInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.optifine.*;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractClientPlayer extends EntityPlayer {

    private NetworkPlayerInfo playerInfo;
    private ResourceLocation locationOfCape = null;
    private String nameClear;

    public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
        this.nameClear = playerProfile.getName();

        if (nameClear != null && !nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes(nameClear);
        }

        CapeUtils.downloadCape(this);
        AbstractClientPlayer niggastrudel = this;

        Novoline.getInstance().getTaskManager().queue(new FutureTask(500) {
            @Override
            public void execute() {

            }

            @Override
            public void run() {
                tryDownloadNovolineCape(niggastrudel);
            }
        });

        PlayerConfigurations.getPlayerConfiguration(this);
    }

    public static void tryDownloadNovolineCape(AbstractClientPlayer player) {
        String nameClear = player.getNameClear();

        if (nameClear != null && !nameClear.isEmpty() && player.getLocationCape() == null && player == getInstance().player) {
//            UserEntity user = Novoline.getInstance().getIRC().getUserManager().findByNickname(nameClear);


            String url = "https://assets.novoline.wtf/capes/" + /*user.getUsername()*/ "gast" + ".png";
            String baseName = FilenameUtils.getBaseName(url);
            ResourceLocation resourceLocation = new ResourceLocation("capeof/" + baseName);
            TextureManager textureManager = getInstance().getTextureManager();
            ITextureObject textureObject = textureManager.getTexture(resourceLocation);

            if (textureObject instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData threadDownloadImageData = (ThreadDownloadImageData) textureObject;

                if (threadDownloadImageData.imageFound != null) {
                    if (threadDownloadImageData.imageFound) {
                        player.setLocationOfCape(resourceLocation);

                        return;
                    }
                }
            }

            CapeImageBuffer capeImageBuffer = new CapeImageBuffer(player, resourceLocation);
            ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, capeImageBuffer);
            threadDownloadImageData.pipeline = true;
            textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        }
    }

    public static    @NotNull
    ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager texturemanager = getInstance().getTextureManager();
        ITextureObject object = texturemanager.getTexture(resourceLocationIn);

        if (object == null) {
            object = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
            texturemanager.loadTexture(resourceLocationIn, object);
        }

        return (ThreadDownloadImageData) object;
    }

    /**
     * Checks if this instance of AbstractClientPlayer has any associated player data.
     */
    public boolean hasPlayerInfo() {
        return getPlayerInfo() != null;
    }

    /**
     * Returns true if the player is in spectator mode.
     */
    @Override
    public boolean isSpectator() {
        NetworkPlayerInfo networkplayerinfo = NetHandlerPlayClient.getPlayerInfo(getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
    }

    /**
     * Returns true if the player has an associated skin.
     */
    public boolean hasSkin() {
        NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
        return networkplayerinfo != null && networkplayerinfo.hasLocationSkin();
    }

    public void setPlayerInfo(NetworkPlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    /**
     * Returns true if the player instance has an associated skin.
     */
    public ResourceLocation getLocationSkin() {
        NetworkPlayerInfo networkPlayerInfo = getPlayerInfo();
        return networkPlayerInfo == null ? DefaultPlayerSkin.getDefaultSkin(getUniqueID()) : networkPlayerInfo.getLocationSkin();
    }

    public ResourceLocation getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        } else if (locationOfCape != null) {
            return locationOfCape;
        } else {
            NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
            return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
        }
    }

    protected NetworkPlayerInfo getPlayerInfo() {
        if (playerInfo == null) {
            this.playerInfo = NetHandlerPlayClient.getPlayerInfo(getUniqueID());
        }

        return playerInfo;
    }

    /**
     * Returns true if the username has an associated skin.
     */
    public static @NotNull ResourceLocation getLocationSkin(String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }

    public String getSkinType() {
        NetworkPlayerInfo networkPlayerInfo = getPlayerInfo();
        return networkPlayerInfo == null ? DefaultPlayerSkin.getSkinType(getUniqueID()) : networkPlayerInfo.getSkinType();
    }

    public float getFovModifier() {
        float f = 1.0F;

        if (abilities.isFlying()) {
            f *= 1.1F;
        }

        IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        f = (float) ((double) f * ((iattributeinstance.getAttributeValue() / (double) abilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (abilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0F;
        }

        if (isUsingItem() && getItemInUse().getItem() == Items.bow) {
            int i = getItemInUseDuration();
            float f1 = (float) i / 20.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

        return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, this, f) : f;
    }

    public String getNameClear() {
        return nameClear;
    }

    public ResourceLocation getLocationOfCape() {
        return locationOfCape;
    }

    public void setLocationOfCape(@Nullable ResourceLocation capeResourceLocation) {
        this.locationOfCape = capeResourceLocation;
    }

}
