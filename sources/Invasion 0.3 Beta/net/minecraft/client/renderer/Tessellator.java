package net.minecraft.client.renderer;

public class Tessellator
{
    private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();

    /** The static instance of the Tessellator. */
    private static final Tessellator instance = new Tessellator(2097152);
public static boolean shouldRenderLSD = true;
    public static Tessellator getInstance()
    {
    	  shouldRenderLSD = true;
        return instance;
    }

    public Tessellator(int bufferSize)
    {
        this.worldRenderer = new WorldRenderer(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        this.worldRenderer.finishDrawing();
       shouldRenderLSD = true;
        this.vboUploader.func_181679_a(this.worldRenderer);
    }

    public WorldRenderer getWorldRenderer()
    {
    	  shouldRenderLSD = true;
        return this.worldRenderer;
    }
}
