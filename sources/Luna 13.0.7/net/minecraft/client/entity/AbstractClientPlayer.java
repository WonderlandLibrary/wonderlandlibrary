package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
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
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import optifine.CapeUtils;
import optifine.Config;
import optifine.PlayerConfigurations;
import optifine.Reflector;
import optifine.ReflectorMethod;
import space.lunaclient.luna.api.cape.Capes;

public abstract class AbstractClientPlayer
  extends EntityPlayer
{
  private NetworkPlayerInfo field_175157_a;
  private ResourceLocation locationOfCape = null;
  private String nameClear = null;
  private static final String __OBFID = "CL_00000935";
  
  public AbstractClientPlayer(World worldIn, GameProfile p_i45074_2_)
  {
    super(worldIn, p_i45074_2_);
    this.nameClear = p_i45074_2_.getName();
    if ((this.nameClear != null) && (!this.nameClear.isEmpty())) {
      this.nameClear = StringUtils.stripControlCodes(this.nameClear);
    }
    CapeUtils.downloadCape(this);
    PlayerConfigurations.getPlayerConfiguration(this);
  }
  
  public boolean func_175149_v()
  {
    NetworkPlayerInfo var1 = Minecraft.getNetHandler().func_175102_a(getGameProfile().getId());
    return (var1 != null) && (var1.getGameType() == WorldSettings.GameType.SPECTATOR);
  }
  
  public boolean hasCape()
  {
    return func_175155_b() != null;
  }
  
  protected NetworkPlayerInfo func_175155_b()
  {
    if (this.field_175157_a == null) {
      this.field_175157_a = Minecraft.getNetHandler().func_175102_a(getUniqueID());
    }
    return this.field_175157_a;
  }
  
  public boolean hasSkin()
  {
    NetworkPlayerInfo var1 = func_175155_b();
    return (var1 != null) && (var1.func_178856_e());
  }
  
  public ResourceLocation getLocationSkin()
  {
    NetworkPlayerInfo var1 = func_175155_b();
    return var1 == null ? DefaultPlayerSkin.func_177334_a(getUniqueID()) : var1.func_178837_g();
  }
  
  public ResourceLocation getLocationCape()
  {
    if (!Config.isShowCapes()) {
      return null;
    }
    try
    {
      if (Capes.hasCape(Main.getLicense())) {
        return Capes.getCape(Main.getLicense());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (this.locationOfCape != null) {
      return this.locationOfCape;
    }
    NetworkPlayerInfo var1 = func_175155_b();
    return var1 == null ? null : var1.func_178861_h();
  }
  
  public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username)
  {
    TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
    Object var3 = var2.getTexture(resourceLocationIn);
    if (var3 == null)
    {
      var3 = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] { StringUtils.stripControlCodes(username) }), DefaultPlayerSkin.func_177334_a(func_175147_b(username)), new ImageBufferDownload());
      var2.loadTexture(resourceLocationIn, (ITextureObject)var3);
    }
    return (ThreadDownloadImageData)var3;
  }
  
  public static ResourceLocation getLocationSkin(String username)
  {
    return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
  }
  
  public String func_175154_l()
  {
    NetworkPlayerInfo var1 = func_175155_b();
    return var1 == null ? DefaultPlayerSkin.func_177332_b(getUniqueID()) : var1.func_178851_f();
  }
  
  public float func_175156_o()
  {
    float var1 = 1.0F;
    if (this.capabilities.isFlying) {
      var1 *= 1.1F;
    }
    IAttributeInstance var2 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    var1 = (float)(var1 * ((var2.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
    if ((this.capabilities.getWalkSpeed() == 0.0F) || (Float.isNaN(var1)) || (Float.isInfinite(var1))) {
      var1 = 1.0F;
    }
    if ((isUsingItem()) && (getItemInUse().getItem() == Items.bow))
    {
      int var3 = getItemInUseDuration();
      float var4 = var3 / 20.0F;
      if (var4 > 1.0F) {
        var4 = 1.0F;
      } else {
        var4 *= var4;
      }
      var1 *= (1.0F - var4 * 0.15F);
    }
    return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[] { this, Float.valueOf(var1) }) : var1;
  }
  
  public String getNameClear()
  {
    return this.nameClear;
  }
  
  public ResourceLocation getLocationOfCape()
  {
    return this.locationOfCape;
  }
  
  public void setLocationOfCape(ResourceLocation locationOfCape)
  {
    this.locationOfCape = locationOfCape;
  }
}
