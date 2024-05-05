package artemis.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import artemis.util.ParticleUtil.Particle;
import net.minecraft.client.gui.Gui;

public class ParticleUtil {
	
	private final List<Particle> particles;
	private int width, height, count;
	
	public ParticleUtil(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.count = 200;
		this.particles = new ArrayList<Particle>();
		for(int count = 0; count <= this.count; ++count); {
			this.addparticle();
		}
	}

	
		public void drawParticle() {
			this.particles.forEach(particle -> particle.drawParticle());
			//this.drawParticle();
	}
	
	
	public class Particle {
		
		private float xPos, yPos;
		
		public Particle(final int xPos, final int yPos)
		{
			this.xPos = xPos;
			this.yPos = yPos;
		}
		
		public void drawParticle() {
			this.xPos += new Random().nextInt(2);
			this.yPos += new Random().nextInt(2);
			final int particleSize = 2;
			
			if(this.xPos > ParticleUtil.this.height) {
				this.xPos = -particleSize;
			}
			
			if(this.yPos > ParticleUtil.this.height) {
				this.yPos = -particleSize;
			}
			
			Gui.drawRect(this.xPos, this.yPos,  this.xPos + particleSize, this.yPos + particleSize, Color.WHITE.getRGB());
			
		}
	}
	public void addparticle() {
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
	}
}


