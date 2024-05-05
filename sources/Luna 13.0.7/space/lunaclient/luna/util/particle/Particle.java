package space.lunaclient.luna.util.particle;

import java.util.Random;
import net.minecraft.util.MathHelper;
import space.lunaclient.luna.util.particle.utils.ParticleRenderUtils;

public class Particle
{
  public int x;
  public int y;
  public int k;
  public ParticleGenerator pg;
  public boolean reset;
  public float size;
  private Random random = new Random();
  
  public Particle(int x, int y)
  {
    this.x = x;
    this.y = y;
    this.size = genRandom(0.7F, 0.8F);
  }
  
  public void draw()
  {
    if (this.x == -1)
    {
      this.x = ParticleGenerator.breite;
      this.reset = true;
    }
    if (this.y == -1)
    {
      this.y = ParticleGenerator.hohe;
      this.reset = true;
    }
    this.x -= this.random.nextInt(2);
    this.y -= this.random.nextInt(2);
    
    int xx = (int)(MathHelper.cos(0.1F * (this.x + this.k)) * 10.0F);
    ParticleRenderUtils.drawBorderedCircle(this.x + xx, this.y, this.size, 0, -1);
  }
  
  public float genRandom(float min, float max)
  {
    return (float)(min + Math.random() * (max - min + 1.0F));
  }
}
