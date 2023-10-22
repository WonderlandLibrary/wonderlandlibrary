package epsilon.font.fontrenderer;



import java.nio.ByteBuffer;

public final class TextureData {
    private final int textureId;
    public int getTextureId() {
		return textureId;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public TextureData(int textureId, int width, int height, ByteBuffer buffer) {
		this.textureId = textureId;
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	
	
	private final int width, height;
    private final ByteBuffer buffer;
}