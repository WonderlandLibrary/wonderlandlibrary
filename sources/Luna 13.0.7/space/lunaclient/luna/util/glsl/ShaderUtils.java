package space.lunaclient.luna.util.glsl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderUtils
{
  private boolean useShader;
  private int program;
  
  /* Error */
  public ShaderUtils(String fragment, String vertex)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 2	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: iconst_0
    //   6: putfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   9: iconst_0
    //   10: istore_3
    //   11: iconst_0
    //   12: istore 4
    //   14: aload_0
    //   15: aload_1
    //   16: ldc 5
    //   18: invokespecial 6	space/lunaclient/luna/util/glsl/ShaderUtils:createShader	(Ljava/lang/String;I)I
    //   21: istore 4
    //   23: aload_0
    //   24: aload_2
    //   25: ldc 8
    //   27: invokespecial 6	space/lunaclient/luna/util/glsl/ShaderUtils:createShader	(Ljava/lang/String;I)I
    //   30: istore 4
    //   32: iload_3
    //   33: ifne +7 -> 40
    //   36: iconst_1
    //   37: goto +4 -> 41
    //   40: iconst_0
    //   41: iload 4
    //   43: ifne +7 -> 50
    //   46: iconst_1
    //   47: goto +4 -> 51
    //   50: iconst_0
    //   51: ior
    //   52: ifeq +65 -> 117
    //   55: return
    //   56: astore 5
    //   58: aload 5
    //   60: invokevirtual 10	java/lang/Exception:printStackTrace	()V
    //   63: iload_3
    //   64: ifne +7 -> 71
    //   67: iconst_1
    //   68: goto +4 -> 72
    //   71: iconst_0
    //   72: iload 4
    //   74: ifne +7 -> 81
    //   77: iconst_1
    //   78: goto +4 -> 82
    //   81: iconst_0
    //   82: ior
    //   83: ifeq +4 -> 87
    //   86: return
    //   87: return
    //   88: astore 6
    //   90: iload_3
    //   91: ifne +7 -> 98
    //   94: iconst_1
    //   95: goto +4 -> 99
    //   98: iconst_0
    //   99: iload 4
    //   101: ifne +7 -> 108
    //   104: iconst_1
    //   105: goto +4 -> 109
    //   108: iconst_0
    //   109: ior
    //   110: ifeq +4 -> 114
    //   113: return
    //   114: aload 6
    //   116: athrow
    //   117: aload_0
    //   118: invokestatic 11	org/lwjgl/opengl/ARBShaderObjects:glCreateProgramObjectARB	()I
    //   121: putfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   124: aload_0
    //   125: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   128: ifne +4 -> 132
    //   131: return
    //   132: aload_0
    //   133: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   136: iload_3
    //   137: invokestatic 12	org/lwjgl/opengl/ARBShaderObjects:glAttachObjectARB	(II)V
    //   140: aload_0
    //   141: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   144: iload 4
    //   146: invokestatic 12	org/lwjgl/opengl/ARBShaderObjects:glAttachObjectARB	(II)V
    //   149: aload_0
    //   150: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   153: invokestatic 13	org/lwjgl/opengl/ARBShaderObjects:glLinkProgramARB	(I)V
    //   156: aload_0
    //   157: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   160: ldc 15
    //   162: invokestatic 16	org/lwjgl/opengl/ARBShaderObjects:glGetObjectParameteriARB	(II)I
    //   165: ifne +17 -> 182
    //   168: getstatic 17	java/lang/System:err	Ljava/io/PrintStream;
    //   171: aload_0
    //   172: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   175: invokestatic 18	space/lunaclient/luna/util/glsl/ShaderUtils:getLogInfo	(I)Ljava/lang/String;
    //   178: invokevirtual 19	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   181: return
    //   182: aload_0
    //   183: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   186: invokestatic 20	org/lwjgl/opengl/ARBShaderObjects:glValidateProgramARB	(I)V
    //   189: aload_0
    //   190: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   193: ldc 21
    //   195: invokestatic 16	org/lwjgl/opengl/ARBShaderObjects:glGetObjectParameteriARB	(II)I
    //   198: ifne +17 -> 215
    //   201: getstatic 17	java/lang/System:err	Ljava/io/PrintStream;
    //   204: aload_0
    //   205: getfield 3	space/lunaclient/luna/util/glsl/ShaderUtils:program	I
    //   208: invokestatic 18	space/lunaclient/luna/util/glsl/ShaderUtils:getLogInfo	(I)Ljava/lang/String;
    //   211: invokevirtual 19	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   214: return
    //   215: aload_0
    //   216: iconst_1
    //   217: putfield 22	space/lunaclient/luna/util/glsl/ShaderUtils:useShader	Z
    //   220: return
    // Line number table:
    //   Java source line #26	-> byte code offset #0
    //   Java source line #24	-> byte code offset #4
    //   Java source line #27	-> byte code offset #9
    //   Java source line #30	-> byte code offset #14
    //   Java source line #31	-> byte code offset #23
    //   Java source line #36	-> byte code offset #32
    //   Java source line #32	-> byte code offset #56
    //   Java source line #33	-> byte code offset #58
    //   Java source line #36	-> byte code offset #63
    //   Java source line #34	-> byte code offset #87
    //   Java source line #36	-> byte code offset #88
    //   Java source line #37	-> byte code offset #114
    //   Java source line #38	-> byte code offset #117
    //   Java source line #40	-> byte code offset #124
    //   Java source line #41	-> byte code offset #131
    //   Java source line #43	-> byte code offset #132
    //   Java source line #44	-> byte code offset #140
    //   Java source line #46	-> byte code offset #149
    //   Java source line #47	-> byte code offset #156
    //   Java source line #48	-> byte code offset #168
    //   Java source line #49	-> byte code offset #181
    //   Java source line #52	-> byte code offset #182
    //   Java source line #53	-> byte code offset #189
    //   Java source line #54	-> byte code offset #201
    //   Java source line #55	-> byte code offset #214
    //   Java source line #58	-> byte code offset #215
    //   Java source line #59	-> byte code offset #220
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	221	0	this	ShaderUtils
    //   0	221	1	fragment	String
    //   0	221	2	vertex	String
    //   10	127	3	vertexShader	int
    //   12	133	4	fragmentShader	int
    //   56	3	5	e	Exception
    //   88	27	6	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   14	32	56	java/lang/Exception
    //   14	32	88	finally
    //   56	63	88	finally
    //   88	90	88	finally
  }
  
  public void draw()
  {
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    
    GL11.glPushMatrix();
    GL20.glUseProgram(this.program);
    GL20.glUniform2f(GL20.glGetUniformLocation(this.program, "resolution"), sr.getScaledWidth(), sr.getScaledHeight());
    
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glColor3d(1.0D, 1.0D, 1.0D);
    GL11.glBegin(7);
    GL11.glVertex2f(-1.0F, -1.0F);
    GL11.glVertex2f(1.0F, -1.0F);
    GL11.glVertex2f(1.0F, 1.0F);
    GL11.glVertex2f(-1.0F, 1.0F);
    GL11.glEnd();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL20.glUseProgram(0);
    GL11.glPopMatrix();
  }
  
  private int createShader(String filename, int shaderType)
    throws Exception
  {
    int shader = 0;
    try
    {
      shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
      if (shader == 0) {
        return 0;
      }
      ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
      ARBShaderObjects.glCompileShaderARB(shader);
      if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
        throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
      }
      return shader;
    }
    catch (Exception exc)
    {
      ARBShaderObjects.glDeleteObjectARB(shader);
      throw exc;
    }
  }
  
  private static String getLogInfo(int obj)
  {
    return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
  }
  
  public static String readFileAsString(String filename)
    throws Exception
  {
    StringBuilder source = new StringBuilder();
    
    FileInputStream in = new FileInputStream(filename);
    
    Exception exception = null;
    for (;;)
    {
      try
      {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        
        Exception innerExc = null;
        try
        {
          String line;
          if ((line = reader.readLine()) != null)
          {
            source.append(line).append('\n'); continue;
          }
          try
          {
            reader.close();
          }
          catch (Exception exc)
          {
            innerExc = exc;
          }
          if (innerExc == null) {
            continue;
          }
        }
        catch (Exception exc)
        {
          exception = exc;
        }
        finally
        {
          try
          {
            reader.close();
          }
          catch (Exception exc)
          {
            innerExc = exc;
          }
        }
        throw innerExc;
      }
      catch (Exception exc)
      {
        exception = exc;
      }
      finally
      {
        try
        {
          in.close();
        }
        catch (Exception exc)
        {
          if (exception == null) {
            exception = exc;
          } else {
            exc.printStackTrace();
          }
        }
        if (exception == null) {
          continue;
        }
        throw exception;
      }
      try
      {
        in.close();
      }
      catch (Exception exc)
      {
        if (exception == null) {
          exception = exc;
        } else {
          exc.printStackTrace();
        }
      }
    }
    if (exception != null) {
      throw exception;
    }
    return source.toString();
  }
}
