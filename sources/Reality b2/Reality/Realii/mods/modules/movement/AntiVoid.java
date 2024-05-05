package Reality.Realii.mods.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.mods.modules.world.Scaffold;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;
import de.gerrygames.viarewind.utils.PacketUtil;

import java.util.ArrayList;
public class AntiVoid
        extends Module {
	private Mode mode = new Mode("Mode","Mode",new String[] {"BlocksMc","MushMc","Vulcan","Basic","Vclip","Watchdog"},"BlocksMc");
	private Option<Boolean> sufix = new Option<Boolean>("ShowSufix", "ShowSufix", false);
	 public static Numbers<Number>  distance = new Numbers<>("MotionFlySpeed", 5, 0, 10, 1);
    public AntiVoid() {
        super("AntiVoid", ModuleType.Movement);
        this.addValues(this.mode, this.sufix,distance);
    }
    private final ConcurrentLinkedQueue<C03PacketPlayer> packets1 = new ConcurrentLinkedQueue<>();
    private Vec3 position;


    public ArrayList<Packet> packets = new ArrayList();
    public TimerUtil timerUtil = new TimerUtil();
    double lastPosXG, lastPosYG, lastPosZG;
    
    public void sendNoEvent(final Packet<?> packet) {
       
        mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(packet);
    }
    
    @EventHandler
    public void onWorldChange(EventPacketRecieve event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
        	   if (packets.size() > 1) {
                   packets.clear();
               }
        }
    }
    
    @EventHandler
    public void onPacketrecive(EventPacketRecieve event) {
    	   if(mode.getValue().equals("Watchdog")) {
    		   if(event.getPacket() instanceof S08PacketPlayerPosLook) {
    		   if (packets.size() > 1) {
   	            packets.clear();
   	        }
    		   }
    	   }
    }
    
    @EventHandler
    public void onPacket3(EventPacketSend event) {
    	   if(mode.getValue().equals("Watchdog")) {
    		   
    		
    		   if (Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()) {
    	        //   Client.instance.getModuleManager().getModuleByClass(Scaffold.class).setEnabled(false);
    	        }

    	        final Packet<?> p = event.getPacket();
    	      
    	        if (p instanceof C03PacketPlayer) {
    	            final C03PacketPlayer wrapper = (C03PacketPlayer) p;
    	            
    	            if (!PlayerUtils.isBlockUnder()) {
    	                packets1.add(wrapper);
    	                event.setCancelled(true);

    	                if (position != null && mc.thePlayer.fallDistance > distance.getValue().floatValue()) {
    	                	mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(position.xCoord, position.yCoord + 0.1, position.zCoord, false));
    	                }
    	            } else {
    	                if (mc.thePlayer.onGround) {
    	                    position = new Vec3(wrapper.x, wrapper.y, wrapper.z);
    	                }
    	                
    	                
    	                if (!packets1.isEmpty()) {
    	                  
    	                    packets1.clear();
    	                }
    	            }
    	        }
    		   
    	   }
    		   
    	
    }
    

    @EventHandler
    public void onPacket(EventPacketSend e) {
    	
        if (this.mc.thePlayer.fallDistance < 3.0f) {
            return;
        }
        boolean blockUnderneath = false;
        int i = 0;
        while ((double) i < this.mc.thePlayer.posY + 2.0) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double) i, this.mc.thePlayer.posZ);
            if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                blockUnderneath = true;
            }
            ++i;
        }
        if (blockUnderneath) {
            return;
        }
        if(mode.getValue().equals("Hypixel2"))
        	mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer) e.getPacket()).getPositionX(), ((C03PacketPlayer) e.getPacket()).getPositionY(), ((C03PacketPlayer) e.getPacket()).getPositionZ(), ((C03PacketPlayer) e.getPacket()).isOnGround()));
//        if (!this.mc.thePlayer.onGround && !this.mc.thePlayer.isCollidedVertically) {
//            if (packets.size() < 10 && (e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook || e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition)) {
//                packets.add(e.getPacket());
//                e.setCancelled(true);
//            } else {
//                for (Packet p : packets) {
//                    mc.getNetHandler().addToSendQueueWithoutEvent(p);
//                }
//                packets.clear();
//            }
//        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    	 if (ArrayList2.Sufix.getValue().equals("On")) {
        	
        
    		this.setSuffix(mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	
    	if(mode.getValue().equals("MushMc")) {
    		 boolean blockUnderneath = false;
    	        int i = 0;
    	        while ((double) i < this.mc.thePlayer.posY + 2.0) {
    	            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double) i, this.mc.thePlayer.posZ);
    	            if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
    	                blockUnderneath = true;
    	            }
    	            ++i;
    	        }
    	        if (blockUnderneath) {
    	            return;
    	        }
    	        if (this.mc.thePlayer.fallDistance < 4.0f) {
    	            return;
    	        }
    	        
    	        mc.thePlayer.motionY = 0.2;

    	        
             // this.mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
    	           
    	}
    		
    	 
        if(mode.getValue().equals("BlocksMc")) {
        boolean blockUnderneath = false;
        int i = 0;
        while ((double) i < this.mc.thePlayer.posY + 2.0) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double) i, this.mc.thePlayer.posZ);
            if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                blockUnderneath = true;
            }
            ++i;
        }
        if (blockUnderneath) {
            return;
        }
        if (this.mc.thePlayer.fallDistance < 4.0f) {
            return;
        }

           mc.thePlayer.motionY = 0.0;
           
           
        



//        }
    }
        if(mode.getValue().equals("Vulcan")) {
            boolean blockUnderneath = false;
            int i = 0;
            while ((double) i < this.mc.thePlayer.posY + 2.0) {
                BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double) i, this.mc.thePlayer.posZ);
                if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                    blockUnderneath = true;
                }
                ++i;
            }
            if (blockUnderneath) {
                return;
            }
            if (this.mc.thePlayer.fallDistance < 4.0f) {
                return;
               
            }

       
            this.mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7, mc.thePlayer.posZ);
         
            e.setY(e.getY() - e.getY() % 0.015625);
            mc.thePlayer.onGround = true;

            mc.thePlayer.motionY = -0.08D;
            
        
            
            



//            }
        }
        
        if(mode.getValue().equals("Basic")) {
            boolean blockUnderneath = false;
            int i = 0;
            while ((double) i < this.mc.thePlayer.posY + 2.0) {
                BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double) i, this.mc.thePlayer.posZ);
                if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                    blockUnderneath = true;
                }
                ++i;
            }
            if (blockUnderneath) {
                return;
            }
            if (this.mc.thePlayer.fallDistance < 4.0f) {
                return;
                
               
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.fallDistance + 2, mc.thePlayer.posZ, true));
            // mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.fallDistance + 2, mc.thePlayer.posZ, false));

       //mc.thePlayer.onGround = true;
          
         
          
            
        
            
            



//            }
        }
        
        
        if(mode.getValue().equals("Vclip")) {
            boolean blockUnderneath = false;
            int i = 0;
            while ((double) i < this.mc.thePlayer.posY + 2.0) {
                BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double) i, this.mc.thePlayer.posZ);
                if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                    blockUnderneath = true;
                }
                ++i;
            }
            if (blockUnderneath) {
                return;
            }
            if (this.mc.thePlayer.fallDistance < 4.0f) {
                return;
                
               
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + -1, mc.thePlayer.posZ, true));
            // mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.fallDistance + 2, mc.thePlayer.posZ, false));

       //mc.thePlayer.onGround = true;
          
         
          
            
        
            
            



//            }
        }
        
        
        
        
    }
    
    
    
        
}

