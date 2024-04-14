package de.violence.packet.packets;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.packet.Packet;

public class VelocityPacket extends Packet {
   private boolean aacDelay = false;

   public VelocityPacket() {
      super("velocity");
   }

   public boolean dispatchPacket(Object... objects) {
      if(!Module.getByName("Velocity").isToggled()) {
         return false;
      } else {
         double motionX = Double.parseDouble(String.valueOf(objects[0]));
         double motionY = Double.parseDouble(String.valueOf(objects[1]));
         double motionZ = Double.parseDouble(String.valueOf(objects[2]));
         String velocityMode = VSetting.getByName("Mode", Module.getByName("Velocity")).getActiveMode();
         if(velocityMode.equalsIgnoreCase("Custom")) {
            boolean x = VSetting.getByName("Motion X", Module.getByName("Velocity")).isToggled();
            boolean setY = VSetting.getByName("Motion Y", Module.getByName("Velocity")).isToggled();
            boolean y = VSetting.getByName("Motion Z", Module.getByName("Velocity")).isToggled();
            double reduceX = VSetting.getByName("Reduce X", Module.getByName("Velocity")).getCurrent();
            double reduceY = VSetting.getByName("Reduce Y", Module.getByName("Velocity")).getCurrent();
            double moveToLastPos = VSetting.getByName("Reduce Z", Module.getByName("Velocity")).getCurrent();
            motionX /= this.calcMotion(reduceX);
            motionY /= this.calcMotion(reduceY);
            motionZ /= this.calcMotion(moveToLastPos);
            if(!x) {
               motionX = 0.0D;
            }

            if(!setY) {
               motionY = 0.0D;
            }

            if(!y) {
               motionZ = 0.0D;
            }

            this.mc.thePlayer.setVelocity(motionX, motionY, motionZ);
         } else {
            final double z;
            final double x1;
            final double y1;
            if(velocityMode.equalsIgnoreCase("AAC")) {
               x1 = this.mc.thePlayer.posX;
               y1 = this.mc.thePlayer.posZ;
               z = this.mc.thePlayer.posY;
               this.mc.thePlayer.setVelocity(motionX / 9000.0D, motionY / 8000.0D, motionZ / 9000.0D);
               final boolean boostSpeed = this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F;
               boolean moveToLastPos1 = VSetting.getByName("Move to last Pos", Module.getByName("Velocity")).isToggled() && !VSetting.getByName("Boost", Module.getByName("Velocity")).isToggled();
               if(moveToLastPos1) {
                  try {
                     (new Thread(new Runnable() {
                        public void run() {
                           try {
                              Thread.sleep(200L);
                              double distX = VelocityPacket.this.mc.thePlayer.posX - x1;
                              double distZ = VelocityPacket.this.mc.thePlayer.posZ - y1;
                              double dist = Math.sqrt(distX * distX + distZ * distZ);
                              if(dist < 0.0D) {
                                 return;
                              }

                              if(Math.abs(z - VelocityPacket.this.mc.thePlayer.posY) > 0.6D) {
                                 dist /= 20.0D;
                              }

                              if(boostSpeed && !VelocityPacket.this.mc.gameSettings.keyBindBack.pressed) {
                                 Module.getByName("Velocity").move(VelocityPacket.this.mc.thePlayer.rotationYaw, (float)dist / 5.0F);
                              }
                           } catch (Exception var7) {
                              ;
                           }

                        }
                     })).start();
                  } catch (Exception var18) {
                     ;
                  }
               } else {
                  (new Thread(new Runnable() {
                     public void run() {
                        try {
                           Thread.sleep(110L);
                           Module.getByName("Velocity").move(VelocityPacket.this.mc.thePlayer.rotationYaw, 0.08F);
                           if(VSetting.getByName("Boost", Module.getByName("Velocity")).isToggled() && boostSpeed) {
                              float boostSpeedx = (float)VSetting.getByName("Boost Speed", Module.getByName("Velocity")).getCurrent();
                              if(Math.abs(z - VelocityPacket.this.mc.thePlayer.posY) > 0.6D) {
                                 boostSpeedx /= 35.0F;
                              }

                              if(boostSpeed && !VelocityPacket.this.mc.gameSettings.keyBindBack.pressed) {
                                 Module.getByName("Velocity").move(VelocityPacket.this.mc.thePlayer.rotationYaw, (float)((double)boostSpeedx / 10.0D));
                              }
                           }
                        } catch (Exception var2) {
                           ;
                        }

                     }
                  })).start();
               }
            } else if(velocityMode.equalsIgnoreCase("AAC Push Plus")) {
               x1 = motionX / 8000.0D;
               y1 = motionY / 8000.0D;
               z = motionZ / 8000.0D;
               final double boostSpeed1 = VSetting.getByName("Extra Boost", Module.getByName("Velocity")).getCurrent();
               if(!this.aacDelay && (Math.abs(x1) > 0.1D || Math.abs(z) > 0.1D)) {
                  this.aacDelay = true;
                  (new Thread(new Runnable() {
                     public void run() {
                        try {
                           try {
                              Thread.sleep(60L);
                           } catch (InterruptedException var19) {
                              ;
                           }

                           VelocityPacket.this.mc.thePlayer.motionX = x1 / 3.0D;
                           VelocityPacket.this.mc.thePlayer.motionZ = z / 3.0D;

                           try {
                              Thread.sleep(60L);
                           } catch (InterruptedException var18) {
                              ;
                           }

                           if(VelocityPacket.this.mc.gameSettings.keyBindForward.isKeyDown()) {
                              VelocityPacket.this.mc.thePlayer.motionX = 0.0D;
                              VelocityPacket.this.mc.thePlayer.motionZ = 0.0D;
                           } else {
                              VelocityPacket.this.mc.thePlayer.motionX = -VelocityPacket.this.mc.thePlayer.motionX / 3.0D;
                              VelocityPacket.this.mc.thePlayer.motionZ = -VelocityPacket.this.mc.thePlayer.motionX / 3.0D;
                           }

                           try {
                              Thread.sleep(60L);
                           } catch (InterruptedException var17) {
                              ;
                           }

                           float yaw = VelocityPacket.this.mc.thePlayer.rotationYaw;
                           if(VelocityPacket.this.mc.gameSettings.keyBindLeft.isKeyDown()) {
                              yaw -= 50.0F;
                           }

                           if(VelocityPacket.this.mc.gameSettings.keyBindRight.isKeyDown()) {
                              yaw += 50.0F;
                           }

                           if(VelocityPacket.this.mc.gameSettings.keyBindForward.isKeyDown()) {
                              Module.getByName("Velocity").move(yaw, (float)boostSpeed1 / 3.0F);
                           }

                           try {
                              Thread.sleep(60L);
                           } catch (InterruptedException var16) {
                              ;
                           }

                           if(VelocityPacket.this.mc.gameSettings.keyBindForward.isKeyDown()) {
                              Module.getByName("Velocity").move(yaw, (float)(boostSpeed1 / 1.5D));
                           }

                           try {
                              Thread.sleep(60L);
                           } catch (InterruptedException var15) {
                              ;
                           }

                           if(VelocityPacket.this.mc.thePlayer.hurtTime < 5 && VelocityPacket.this.mc.gameSettings.keyBindForward.isKeyDown()) {
                              Module.getByName("Velocity").move(yaw, (float)boostSpeed1);
                              System.out.println("Extra push!");
                           }

                           try {
                              Thread.sleep(60L);
                           } catch (InterruptedException var14) {
                              ;
                           }

                           if(VelocityPacket.this.mc.thePlayer.hurtTime < 4 && VSetting.getByName("Ultra Boost", Module.getByName("Velocity")).isToggled() && VelocityPacket.this.mc.gameSettings.keyBindForward.isKeyDown()) {
                              Module.getByName("Velocity").move(yaw, (float)(boostSpeed1 + boostSpeed1 / 1.2D));
                              System.out.println("EXTREME push!!");
                           }

                           try {
                              Thread.sleep(100L);
                           } catch (InterruptedException var13) {
                              ;
                           }
                        } catch (Exception var20) {
                           ;
                        } finally {
                           VelocityPacket.this.aacDelay = false;
                        }

                     }
                  })).start();
                  this.mc.thePlayer.setVelocity(x1, y1, z);
               }
            }
         }

         return true;
      }
   }

   private double calcMotion(double recude) {
      return recude * 100.0D + 8000.0D;
   }
}
