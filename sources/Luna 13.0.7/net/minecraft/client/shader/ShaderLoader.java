package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader
{
  private final ShaderType shaderType;
  private final String shaderFilename;
  private int shader;
  private int shaderAttachCount = 0;
  private static final String __OBFID = "CL_00001043";
  
  private ShaderLoader(ShaderType type, int shaderId, String filename)
  {
    this.shaderType = type;
    this.shader = shaderId;
    this.shaderFilename = filename;
  }
  
  public void attachShader(ShaderManager manager)
  {
    this.shaderAttachCount += 1;
    OpenGlHelper.glAttachShader(manager.getProgram(), this.shader);
  }
  
  public void deleteShader(ShaderManager manager)
  {
    this.shaderAttachCount -= 1;
    if (this.shaderAttachCount <= 0)
    {
      OpenGlHelper.glDeleteShader(this.shader);
      this.shaderType.getLoadedShaders().remove(this.shaderFilename);
    }
  }
  
  public String getShaderFilename()
  {
    return this.shaderFilename;
  }
  
  public static ShaderLoader loadShader(IResourceManager resourceManager, ShaderType type, String filename)
    throws IOException
  {
    ShaderLoader var3 = (ShaderLoader)type.getLoadedShaders().get(filename);
    if (var3 == null)
    {
      ResourceLocation var4 = new ResourceLocation("shaders/program/" + filename + type.getShaderExtension());
      BufferedInputStream var5 = new BufferedInputStream(resourceManager.getResource(var4).getInputStream());
      byte[] var6 = func_177064_a(var5);
      ByteBuffer var7 = BufferUtils.createByteBuffer(var6.length);
      var7.put(var6);
      var7.position(0);
      int var8 = OpenGlHelper.glCreateShader(type.getShaderMode());
      OpenGlHelper.glShaderSource(var8, var7);
      OpenGlHelper.glCompileShader(var8);
      if (OpenGlHelper.glGetShaderi(var8, OpenGlHelper.GL_COMPILE_STATUS) == 0)
      {
        String var9 = StringUtils.trim(OpenGlHelper.glGetShaderInfoLog(var8, 32768));
        JsonException var10 = new JsonException("Couldn't compile " + type.getShaderName() + " program: " + var9);
        var10.func_151381_b(var4.getResourcePath());
        throw var10;
      }
      var3 = new ShaderLoader(type, var8, filename);
      type.getLoadedShaders().put(filename, var3);
    }
    return var3;
  }
  
  /* Error */
  protected static byte[] func_177064_a(BufferedInputStream p_177064_0_)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 47	org/apache/commons/io/IOUtils:toByteArray	(Ljava/io/InputStream;)[B
    //   4: astore_1
    //   5: aload_0
    //   6: invokevirtual 48	java/io/BufferedInputStream:close	()V
    //   9: goto +10 -> 19
    //   12: astore_2
    //   13: aload_0
    //   14: invokevirtual 48	java/io/BufferedInputStream:close	()V
    //   17: aload_2
    //   18: athrow
    //   19: aload_1
    //   20: areturn
    // Line number table:
    //   Java source line #90	-> byte code offset #0
    //   Java source line #94	-> byte code offset #5
    //   Java source line #95	-> byte code offset #9
    //   Java source line #94	-> byte code offset #12
    //   Java source line #95	-> byte code offset #17
    //   Java source line #97	-> byte code offset #19
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	21	0	p_177064_0_	BufferedInputStream
    //   4	2	1	var1	byte[]
    //   19	1	1	var1	byte[]
    //   12	6	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	5	12	finally
  }
  
  public static enum ShaderType
  {
    private final String shaderName;
    private final String shaderExtension;
    private final int shaderMode;
    private final Map loadedShaders = Maps.newHashMap();
    private static final ShaderType[] $VALUES = { VERTEX, FRAGMENT };
    private static final String __OBFID = "CL_00001044";
    
    private ShaderType(String p_i45090_1_, int p_i45090_2_, String p_i45090_3_, String p_i45090_4_, int p_i45090_5_)
    {
      this.shaderName = p_i45090_3_;
      this.shaderExtension = p_i45090_4_;
      this.shaderMode = p_i45090_5_;
    }
    
    public String getShaderName()
    {
      return this.shaderName;
    }
    
    protected String getShaderExtension()
    {
      return this.shaderExtension;
    }
    
    protected int getShaderMode()
    {
      return this.shaderMode;
    }
    
    protected Map getLoadedShaders()
    {
      return this.loadedShaders;
    }
  }
}
