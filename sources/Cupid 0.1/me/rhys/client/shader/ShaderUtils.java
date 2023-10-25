package me.rhys.client.shader;

import org.lwjgl.opengl.GL20;

public final class ShaderUtils {
  private ShaderUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  public static ShaderProgram loadShader(String vertexPath, String fragmentPath) {
    return createShaderProgram(BufferUtils.loadString(vertexPath), BufferUtils.loadString(fragmentPath));
  }
  
  public static ShaderProgram createShaderProgram(String vertexShader, String fragmentShader) {
    int program = GL20.glCreateProgram();
    int vertexShaderID = GL20.glCreateShader(35633);
    int fragmentShaderID = GL20.glCreateShader(35632);
    GL20.glShaderSource(vertexShaderID, vertexShader);
    GL20.glShaderSource(fragmentShaderID, fragmentShader);
    GL20.glCompileShader(vertexShaderID);
    if (GL20.glGetShaderi(vertexShaderID, 35713) == 0) {
      System.out.println("Vertex shader failed to compile");
      System.out.println(GL20.glGetShaderInfoLog(vertexShaderID, 500));
    } 
    GL20.glCompileShader(fragmentShaderID);
    if (GL20.glGetShaderi(fragmentShaderID, 35713) == 0) {
      System.out.println("Fragment shader failed to compile");
      System.out.println(GL20.glGetShaderInfoLog(fragmentShaderID, 500));
    } 
    GL20.glAttachShader(program, vertexShaderID);
    GL20.glAttachShader(program, fragmentShaderID);
    GL20.glLinkProgram(program);
    GL20.glValidateProgram(program);
    return new ShaderProgram(program, vertexShaderID, fragmentShaderID);
  }
  
  public static class ShaderProgram {
    private final int programID;
    
    private final int vertexShaderID;
    
    private final int fragmentShaderID;
    
    public ShaderProgram(int programID, int vertexShaderID, int fragmentShaderID) {
      this.programID = programID;
      this.vertexShaderID = vertexShaderID;
      this.fragmentShaderID = fragmentShaderID;
    }
    
    public int getProgramID() {
      return this.programID;
    }
    
    public int getVertexShaderID() {
      return this.vertexShaderID;
    }
    
    public int getFragmentShaderID() {
      return this.fragmentShaderID;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\shader\ShaderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */