package slavikcodd3r.rainbow.module.modules.misc;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.RainbowPortalMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "RainbowPortal")
public class RainbowPortal extends Module
{
	private RainbowPortalMode v1;
	private RainbowPortalMode v2;
	private RainbowPortalMode v3;
	private RainbowPortalMode v4;
	private RainbowPortalMode v5;
	private RainbowPortalMode v6;
	private RainbowPortalMode v7;
	private RainbowPortalMode lunar;
	private RainbowPortalMode badlion;
	private RainbowPortalMode vimeworld;
	private RainbowPortalMode cheatbreaker;
	private RainbowPortalMode labymod;
	private RainbowPortalMode gommeprotector;
	private RainbowPortalMode cristalix;
	private RainbowPortalMode streamcraft;
	private RainbowPortalMode minecraftonly;
    public static int var;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public RainbowPortal() {
        this.v1 = new RainbowPortalMode("1.7", true, this);
        this.v2 = new RainbowPortalMode("1.8", false, this);
        this.v3 = new RainbowPortalMode("1.9-1.11", false, this);
        this.v4 = new RainbowPortalMode("1.12", false, this);
        this.v5 = new RainbowPortalMode("1.13", false, this);
        this.v6 = new RainbowPortalMode("1.14", false, this);
        this.v7 = new RainbowPortalMode("1.15", false, this);
        this.lunar = new RainbowPortalMode("Lunar", false, this);
        this.badlion = new RainbowPortalMode("Badlion", false, this);
        this.vimeworld = new RainbowPortalMode("VimeWorld", false, this);
        this.cheatbreaker = new RainbowPortalMode("CheatBreaker", false, this);
        this.labymod = new RainbowPortalMode("LabyMod", false, this);
        this.gommeprotector = new RainbowPortalMode("GommeProtector", false, this);
        this.cristalix = new RainbowPortalMode("Cristalix", false, this);
        this.streamcraft = new RainbowPortalMode("StreamCraft", false, this);
        this.minecraftonly = new RainbowPortalMode("MinecraftOnly", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.v1);
        OptionManager.getOptionList().add(this.v2);
        OptionManager.getOptionList().add(this.v3);
        OptionManager.getOptionList().add(this.v4);
        OptionManager.getOptionList().add(this.v5);
        OptionManager.getOptionList().add(this.v6);
        OptionManager.getOptionList().add(this.v7);
        OptionManager.getOptionList().add(this.lunar);
        OptionManager.getOptionList().add(this.badlion);
        OptionManager.getOptionList().add(this.vimeworld);
        OptionManager.getOptionList().add(this.cheatbreaker);
        OptionManager.getOptionList().add(this.labymod);
        OptionManager.getOptionList().add(this.gommeprotector);
        OptionManager.getOptionList().add(this.cristalix);
        OptionManager.getOptionList().add(this.streamcraft);
        OptionManager.getOptionList().add(this.minecraftonly);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.v1.getValue()) {
            this.setSuffix("1.7");
        }
        else if (this.v2.getValue()) {
        	this.setSuffix("1.8");
        }
        else if (this.v3.getValue()) {
        	this.setSuffix("1.9-1.11");
        }
        else if (this.v4.getValue()) {
        	this.setSuffix("1.12");
        }
        else if (this.v5.getValue()) {
        	this.setSuffix("1.13");
        }
        else if (this.v6.getValue()) {
        	this.setSuffix("1.14");
        }
        else if (this.v7.getValue()) {
        	this.setSuffix("1.15");
        }
        else if (this.lunar.getValue()) {
        	this.setSuffix("Lunar");
        }
        else if (this.badlion.getValue()) {
        	this.setSuffix("Badlion");
        }
        else if (this.vimeworld.getValue()) {
        	this.setSuffix("VimeWorld");
        }
        else if (this.cheatbreaker.getValue()) {
        	this.setSuffix("CheatBreaker");
        }
        else if (this.labymod.getValue()) {
        	this.setSuffix("LabyMod");
        }
        else if (this.gommeprotector.getValue()) {
        	this.setSuffix("GommeProtector");
        }
        else if (this.cristalix.getValue()) {
        	this.setSuffix("Cristalix");
        }
        else if (this.streamcraft.getValue()) {
        	this.setSuffix("StreamCraft");
        }
        else if (this.minecraftonly.getValue()) {
        	this.setSuffix("MinecraftOnly");
        }
    }
    
    public int protocol() {
        if (this.v1.getValue()) {
            return var = 5;
        }
        else if (this.v2.getValue()) {
            return var = 47;
        }
        else if (this.v3.getValue()) {
            return var = 107;
        }
        else if (this.v4.getValue()) {
            return var = 340;
        }
        else if (this.v5.getValue()) {
            return var = 393;
        }
        else if (this.v6.getValue()) {
            return var = 498;
        }
        else if (this.v7.getValue()) {
            return var = 575;
        }
        else if (this.cristalix.getValue()) {
            return var = 340;
        }
        else if (this.streamcraft.getValue()) {
            return var = 107;
        }
        else if (this.minecraftonly.getValue()) {
            return var = 340;
        }
        return var = 0;
    }
    
    public void enable() {
    	var = 47;
    	super.enable();
    }
    
    public void disable() {
    	var = 47;
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onPacket(final PacketSendEvent event) {
    	if (event.getPacket() instanceof C00Handshake) {
            final C00Handshake handshake = (C00Handshake)event.getPacket();
            handshake.protocolVersion = this.protocol();
    	}
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.lunar.getValue()) {
            if (mc.thePlayer.ticksExisted % 20 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString("Lunar Client|lunarclient:dfd76ad")));
            }
    	}
    	else if (this.badlion.getValue()) {
            if (mc.thePlayer.ticksExisted % 20 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString("BLC|KOZ9FP")));
            }
    	}
    	else if (this.vimeworld.getValue()) {
            if (mc.thePlayer.ticksExisted % 20 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString("VimeWorld: SlavikCodd3r, LOBBY_1")));
            }
    	}
    	else if (this.cheatbreaker.getValue()) {
            if (mc.thePlayer.ticksExisted % 20 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString("CheatBreaker|vanilla")));
            }
    	}
    	else if (this.labymod.getValue()) {
    		System.out.println("LabyMod 1.8.9 " + mc.thePlayer.ticksExisted);
    	}
    	else if (this.gommeprotector.getValue()) {
    		System.out.println("Gomme Protector 1.8.9 " + mc.thePlayer.ticksExisted);
    	}
    	else if (this.cristalix.getValue()) {
    		if (mc.thePlayer.ticksExisted % 20 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString("Cristalix")));
            }
    	}
    	else if (this.streamcraft.getValue()) {
    		if (mc.thePlayer.ticksExisted % 20 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString("StreamCraft|MiniGames")));
            }
    	}
    	else if (this.minecraftonly.getValue()) {
    		if (mc.thePlayer.ticksExisted % 20 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString("MinecraftOnly.Ru")));
            }
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
