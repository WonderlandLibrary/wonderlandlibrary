package koks.manager.module.impl.debug;

import god.buddy.aot.BCompiler;
import koks.manager.event.Event;
import koks.manager.event.impl.EventPacket;
import koks.manager.event.impl.EventTick;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:46
 */

@ModuleInfo(name = "Debug", description = "Test Module", category = Module.Category.DEBUG)
public class Debug extends Module {

    public Setting testCheck = new Setting("Check", true, this);
    public Setting testCombo = new Setting("Combo", new String[]{"M1", "M2"}, "M1", this);
    public Setting testSlider = new Setting("Slider", 10, 5, 20, false, this);

    // Hunger Balken sind flaggs
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        //C0FPacketConfirmTransaction

        if (event instanceof EventUpdate) {

            /*PlayerCapabilities capabilities = new PlayerCapabilities();
            capabilities.isCreativeMode = false;
            capabilities.isFlying = true;
            capabilities.disableDamage = false;
            capabilities.allowFlying = true;
            capabilities.allowEdit = true;

            sendPacket(new C13PacketPlayerAbilities(capabilities));*/


           /* if(timeHelper.hasReached(3000)) {
                getPlayer().sendChatMessage(randomUtil.getRandomInt(0, 1000) + " - Join Now - MasterOf13FPS - https://discord.gg/ZH5QdUqVwN");
                timeHelper.reset();
            }*/

            /*getPlayer().isInWeb = false;*/

            /* if (getPlayer().isOnLadder() && getPlayer().isCollidedHorizontally && !getPlayer().onGround) {
                if (timeHelper.hasReached(550)) {
                    getPlayer().motionY = 0.55F;
                    timeHelper.reset();
                }
            }*/
        }

        if (event instanceof EventTick) {
            /* if(timeHelper.hasReached(25)) {
                getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(true));
                timeHelper.reset();
            }

            getPlayer().capabilities.isCreativeMode = true;

            getPlayer().motionY = -0.0001;*/
            /*getPlayer().isInWeb = false;*/

            /* for(Entity entity : getWorld().loadedEntityList) {
                if(entity.getDistanceSqToEntity(getPlayer()) < 8 && entity instanceof EntityPlayer) {
                    if(((EntityPlayer) entity).hurtTime != 0 && entity != getPlayer()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C);
                    }
                }
           }*/
            /*MovementUtil movementUtil = new MovementUtil();
            if (getPlayer().onGround) {

                mc.thePlayer.motionY = 0.2;
            } else {
                getPlayer().setSprinting(false);
            }
            if (timeHelper.hasReached(300)) {

                if (getPlayer().onGround) {


                    movementUtil.setSpeed(0.35, false);
                    getPlayer().setSprinting(true);
                    timeHelper.reset();

                }


            }*/
        }

        /* if (event instanceof EventPacket) {
            if (((EventPacket) event).getType() == EventPacket.Type.SEND) {
                Packet packet = ((EventPacket) event).getPacket();
                if (!(packet instanceof C03PacketPlayer || packet instanceof C0APacketAnimation || packet instanceof C09PacketHeldItemChange || packet instanceof C0EPacketClickWindow))
                    System.out.println(packet);
            }
        }*/

        /*                        mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));

            if (event instanceof EventPacket && ((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
             if(packet instanceof S06PacketUpdateHealth) {
                S06PacketUpdateHealth s06PacketUpdateHealth = (S06PacketUpdateHealth) packet;

                sendmsg("damage " + s06PacketUpdateHealth.getHealth(), true);
            }*/
    }


    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

}
