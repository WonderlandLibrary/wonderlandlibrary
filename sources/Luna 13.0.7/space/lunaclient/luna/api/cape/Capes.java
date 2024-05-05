package space.lunaclient.luna.api.cape;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class Capes
{
  private static Map<String, ResourceLocation> capes = new HashMap();
  
  public Capes() {}
  
  public static void loadCape(String hwid)
  {
    String url = null;
    try
    {
      url = "http://lunaclient.pw/api/cape/" + hwid + ".png";
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    final ResourceLocation resourceLocation = new ResourceLocation("capes/" + hwid + ".png");
    TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    
    IImageBuffer iImageBuffer = new IImageBuffer()
    {
      public BufferedImage parseUserSkin(BufferedImage var1)
      {
        return var1;
      }
      
      public void func_152634_a()
      {
        Capes.capes.put(this.val$hwid, resourceLocation);
      }
    };
    ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, iImageBuffer);
    textureManager.loadTexture(resourceLocation, threadDownloadImageData);
  }
  
  public static void deleteCape(String hwid)
  {
    capes.remove(hwid);
  }
  
  public static ResourceLocation getCape(String hwid)
  {
    return capes.containsKey(hwid) ? (ResourceLocation)capes.get(hwid) : null;
  }
  
  public static boolean hasCape(String hwid)
  {
    return capes.containsKey(hwid);
  }
}
