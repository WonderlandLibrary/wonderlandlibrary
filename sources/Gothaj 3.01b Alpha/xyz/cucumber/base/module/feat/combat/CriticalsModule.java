package xyz.cucumber.base.module.feat.combat;


import org.lwjgl.input.Keyboard;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventAttack;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ModeSettings;

@ModuleInfo(category = Category.COMBAT, description = "Allows you to deal criticals every hit", name = "Criticals", key = Keyboard.KEY_NONE)

public class CriticalsModule extends Mod {
	
	public ModeSettings mode = new ModeSettings("Mode", new String[] {"Edit", "Ground", "Packet", "Verus", "Vulcan", "NCP"});
    
	public CriticalsModule() {
		this.addSettings(mode);
	}
	
    public int ticks, attacks;
    public boolean attacked;
    
    public void onEnable() {
    
    setInfo(mode.getMode());
    
    }
    
    @EventListener
    public void onAttack(EventAttack event) {
    	if(event.getEntity() != null) {
    		if(event.getEntity() instanceof EntityLivingBase) {
    			EntityLivingBase ent = (EntityLivingBase) event.getEntity();
    			
    			attacked = true;
    			
    			if(ent.hurtTime <= 2) {
    				if(mode.getMode().equalsIgnoreCase("Packet")) {
    					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, false));
    					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, false));
    				}
    			}
    			
    			attacks++;
    		}
    	}
    }
    
    @EventListener
    public void onMotion(EventMotion event) {
    	KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
    	if(!ka.isEnabled() || ka.target == null)return;
    	
    	switch(mode.getMode().toLowerCase()) {
        	case "packet":
        		event.setOnGround(false);
                if(mc.thePlayer.ticksExisted % 20 == 0) {
                	event.setOnGround(true);
                }
        		break;
        	case "edit":
        		if (mc.thePlayer.onGround && attacked) {
                    ticks++;
                    switch (ticks) {
                        case 1: {
                        	event.setY(event.getY() + 0.0005D);
                            break;
                        }

                        case 2: {
                        	event.setY(event.getY() + 0.0001D);
                            attacked = false;
                            break;
                        }
                    }
                    event.setOnGround(false);
                    if(mc.thePlayer.ticksExisted % 20 == 0) {
                    	event.setOnGround(true);
                    }
        		} else {
                    attacked = false;
                    ticks = 0;
                }
        		break;
        	case "ground":
        		if (mc.thePlayer.onGround && attacked) {
                    ticks++;
                    switch (ticks) {
                        case 1: {
                            event.setY(event.getY() + 0.0005D);
                            break;
                        }

                        case 2: {
                            event.setY(event.getY() + 0.0001D);
                            attacked = false;
                            break;
                        }
                    }

                    event.setOnGround(false);
                } else {
                    attacked = false;
                    ticks = 0;
                }
        		break;
        	case "verus":
        		if (attacked) {
                    ticks++;
                    switch (ticks) {
                        case 1: {
                            event.setY(event.getY() + 0.001);
                            event.setOnGround(true);
                            break;
                        }

                        case 2: {
                        	event.setOnGround(false);
                            attacked = false;
                            break;
                        }
                    }
                } else {
                    attacked = false;
                    ticks = 0;
                }
        		break;
        	case "vulcan":
        		if (attacked) {
                    ticks++;
                    switch (ticks) {
                        case 1: {
                            event.setY(event.getY() + 0.16477328182606651);
                            event.setOnGround(false);
                            break;
                        }
                        
                        case 2: {
                            event.setY(event.getY() + 0.08307781780646721);
                            event.setOnGround(false);
                            break;
                        }

                        case 3: {
                            event.setY(event.getY() + 0.0030162615090425808);
                            event.setOnGround(false);
                            attacked = false;
                            break;
                        }
                    }
                } else {
                    attacked = false;
                    ticks = 0;
                }
        		break;
        	case "ncp":
        		if (attacked) {
                    ticks++;
                    switch (ticks) {
                        case 1: {
                            event.setY(event.getY() + 0.001);
                            event.setOnGround(true);
                            break;
                        }

                        case 2: {
                        	event.setOnGround(false);
                            attacked = false;
                            break;
                        }
                    }
                } else {
                    attacked = false;
                    ticks = 0;
                }
        		break;
    	}
    }
}
