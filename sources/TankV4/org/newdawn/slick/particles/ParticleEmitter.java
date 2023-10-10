package org.newdawn.slick.particles;

import org.newdawn.slick.Image;

public interface ParticleEmitter {
   void update(ParticleSystem var1, int var2);

   boolean completed();

   void wrapUp();

   void updateParticle(Particle var1, int var2);

   boolean isEnabled();

   void setEnabled(boolean var1);

   boolean useAdditive();

   Image getImage();

   boolean isOriented();

   boolean usePoints(ParticleSystem var1);

   void resetState();
}
