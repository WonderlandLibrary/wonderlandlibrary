package net.minecraft.client.resources;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractResourcePack implements IResourcePack {
  private static final Logger resourceLog = LogManager.getLogger();
  
  public final File resourcePackFile;
  
  private static final String __OBFID = "CL_00001072";
  
  public static final int SIZE_CAP = 64;
  
  public AbstractResourcePack(File resourcePackFileIn) {
    this.resourcePackFile = resourcePackFileIn;
  }
  
  private static String locationToName(ResourceLocation location) {
    return String.format("%s/%s/%s", new Object[] { "assets", location.getResourceDomain(), location.getResourcePath() });
  }
  
  protected static String getRelativeName(File p_110595_0_, File p_110595_1_) {
    return p_110595_0_.toURI().relativize(p_110595_1_.toURI()).getPath();
  }
  
  public InputStream getInputStream(ResourceLocation location) throws IOException {
    return getInputStreamByName(locationToName(location));
  }
  
  public boolean resourceExists(ResourceLocation location) {
    return hasResourceName(locationToName(location));
  }
  
  protected abstract InputStream getInputStreamByName(String paramString) throws IOException;
  
  protected abstract boolean hasResourceName(String paramString);
  
  protected void logNameNotLowercase(String p_110594_1_) {
    resourceLog.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", new Object[] { p_110594_1_, this.resourcePackFile });
  }
  
  public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException {
    return readMetadata(p_135058_1_, getInputStreamByName("pack.mcmeta"), p_135058_2_);
  }
  
  static IMetadataSection readMetadata(IMetadataSerializer p_110596_0_, InputStream p_110596_1_, String p_110596_2_) {
    JsonObject jsonobject = null;
    BufferedReader bufferedreader = null;
    try {
      bufferedreader = new BufferedReader(new InputStreamReader(p_110596_1_, Charsets.UTF_8));
      jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
    } catch (RuntimeException runtimeexception) {
      throw new JsonParseException(runtimeexception);
    } finally {
      IOUtils.closeQuietly(bufferedreader);
    } 
    return p_110596_0_.parseMetadataSection(p_110596_2_, jsonobject);
  }
  
  public BufferedImage getPackImage() throws IOException {
    return scalePackImage(TextureUtil.readBufferedImage(getInputStreamByName("pack.png")));
  }
  
  public BufferedImage scalePackImage(BufferedImage image) {
    if (image == null)
      return null; 
    System.out.println("Scaling resource pack icon from " + image.getWidth() + " to " + '@');
    BufferedImage smallImage = new BufferedImage(64, 64, 2);
    Graphics graphics = smallImage.getGraphics();
    graphics.drawImage(image, 0, 0, 64, 64, null);
    graphics.dispose();
    return smallImage;
  }
  
  public String getPackName() {
    return this.resourcePackFile.getName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\AbstractResourcePack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */