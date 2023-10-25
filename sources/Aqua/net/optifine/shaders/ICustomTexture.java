package net.optifine.shaders;

public interface ICustomTexture {
    public int getTextureId();

    public int getTextureUnit();

    public void deleteTexture();

    public int getTarget();
}
