package artemis.mods.Combat;

import java.util.ArrayList;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.settings.Setting;
import artemis.util.PlayerUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends module {
	EntityPlayer entityplayer;
	String mode = Artemis.instance.settingsManager.getSettingByName("Criticals Mode").getValString();
	private Packet packet;
    public Criticals() {
        super("Criticals", "Deals double damage", 0, 909090, Category.COMBAT);
    }
    public Packet getPacket() {
        return packet;
    }
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<String>();
        options.add("Packet");
        options.add("MiniJump");
        options.add("Legit");
        Artemis.instance.settingsManager.rSetting(new Setting("Criticals Mode", this, "Packet", options));
    }
    
    
    public void legit() {
    	if(this.isToggled()) {
    		 if(mode.equalsIgnoreCase("Legit")) {
             	if(entityplayer instanceof EntityPlayerSP && mc.thePlayer.onGround) {
             		mc.thePlayer.jump();
             	}
             }
    		
    	}
    }

    //@EventTarget
   /* public void onUpdate(EventUpdate event) {
        String mode = artemis_client.instance.settingsManager.getSettingByName("Criticals Mode").getValString();
        this.setName("Criticals \u00A77" + mode);
    }

    //@EventTarget
    */
    public void onUpdate() {
        if (isToggled()) {
        
        this.setName("Criticals \u00A77" + mode);
        
        
        if(canCrit()) {
            if (this.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity)this.getPacket();
                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if(mode.equalsIgnoreCase("Packet")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + .1625, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.0E-6, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-6, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                    }
                }
            }
            
            if(mode.equalsIgnoreCase("MiniJump")) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY -= .30000001192092879;
            }
           
        }
        }
    }

    private boolean canCrit() {
        return !PlayerUtils.isInLiquid() && mc.thePlayer.onGround;
    }
}
