package cow.milkgod.cheese.module.modules;

import cow.milkgod.cheese.properties.*;
import cow.milkgod.cheese.module.*;
import cow.milkgod.cheese.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;
import cow.milkgod.cheese.events.*;
import cow.milkgod.cheese.utils.*;
import cow.milkgod.cheese.commands.*;

public class Jesus extends Module
{
    public static boolean Jesus;
    public static int Delay;
    public static Property<Boolean> oldMode;
    public static Property<Boolean> newMode;
    
    public Jesus() {
        super("Jesus", 36, Category.MOVEMENT, 3376639, true, "Walk on liquids.", new String[] { "js", "jebus", "jsus", "waterwalk" });
        cow.milkgod.cheese.module.modules.Jesus.Delay = 0;
        cow.milkgod.cheese.module.modules.Jesus.oldMode = new Property<Boolean>(this, "oldMode", true);
        cow.milkgod.cheese.module.modules.Jesus.newMode = new Property<Boolean>(this, "newMode", false);
    }
    
    @EventTarget
    public void onPreMotionUpdate(final EventPacketSent event) {
        Cheese.getInstance();
        if (Cheese.moduleManager.getModState("Freecam")) {
            return;
        }
        final double posX = Wrapper.mc.thePlayer.posX;
        final double posY = Wrapper.mc.thePlayer.posY;
        final double posZ = Wrapper.mc.thePlayer.posZ;
        if (Wrapper.liquidCollision().equalsIgnoreCase("positive") && !Wrapper.mc.thePlayer.isInWater() && Wrapper.mc.thePlayer.onGround) {
            Wrapper.mc.thePlayer.setPosition(posX - 1.0E-10, posY, posZ - 1.0E-10);
        }
        else if (Wrapper.liquidCollision().equalsIgnoreCase("negative") && !Wrapper.mc.thePlayer.isInWater() && Wrapper.mc.thePlayer.onGround) {
            Wrapper.mc.thePlayer.setPosition(posX + 1.0E-10, posY, posZ + 1.0E-10);
        }
        cow.milkgod.cheese.module.modules.Jesus.Jesus = true;
        if (Wrapper.isOnLiquid() && !Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, -1.0E-5).getMaterial().isLiquid()) {
            if (Wrapper.mc.gameSettings.keyBindSneak.isPressed()) {
                Wrapper.mc.thePlayer.setPosition(posX, posY - 0.1, posZ);
            }
            cow.milkgod.cheese.module.modules.Jesus.Jesus = true;
            float yaw = Wrapper.mc.thePlayer.rotationYaw;
            float pitch = Wrapper.mc.thePlayer.rotationPitch;
            if (Killaura.attacking) {
                pitch = CombatUtils.pitch;
                yaw = CombatUtils.yaw;
            }
            if (event.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer p = (C03PacketPlayer)event.getPacket();
                if (Wrapper.mc.thePlayer.ticksExisted % 2 == 0) {
                    if (!Wrapper.mc.thePlayer.isCollidedHorizontally && Wrapper.mc.thePlayer.posY % 1.0 == 0.0) {
                        event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(posX, Wrapper.mc.thePlayer.posY - (cow.milkgod.cheese.module.modules.Jesus.newMode.value ? 0.215 : 0.01), posZ, yaw, pitch, true));
                    }
                }
                else if (!Wrapper.mc.thePlayer.isCollidedHorizontally) {
                    event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(posX, Wrapper.mc.thePlayer.posY - (cow.milkgod.cheese.module.modules.Jesus.newMode.value ? 0.195 : -0.01), posZ, yaw, pitch, true));
                }
            }
        }
        else {
            cow.milkgod.cheese.module.modules.Jesus.Delay = 0;
            if (Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, -0.1).getMaterial().isLiquid() || Wrapper.mc.thePlayer.fallDistance >= 3.0f) {
                cow.milkgod.cheese.module.modules.Jesus.Jesus = false;
            }
            if (Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, -0.01).getMaterial().isLiquid()) {
                if (Wrapper.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                    return;
                }
                Wrapper.mc.thePlayer.motionY = 0.075;
            }
        }
    }
    
    @Override
    public void toggleModule() {
        super.toggleModule();
        if (!this.getState()) {
            cow.milkgod.cheese.module.modules.Jesus.Jesus = false;
        }
    }
    
    @Override
    protected void addCommand() {
        Cheese.getInstance();
        final CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("Jesus", "Unknown Option! ", new String[] { "jisus, jes" }, "<old | new>", "Manages Jesus") {
            @EventTarget
            public void onTick(final EventTick e) {
                try {
                    final String mode = EventChatSend.getMessage().split(" ")[1];
                    if (mode.equalsIgnoreCase("new")) {
                        cow.milkgod.cheese.module.modules.Jesus.newMode.value = true;
                        cow.milkgod.cheese.module.modules.Jesus.oldMode.value = false;
                        Logger.logChat("Set Jesus mode to §eNew");
                    }
                    else if (mode.equalsIgnoreCase("old")) {
                        cow.milkgod.cheese.module.modules.Jesus.newMode.value = false;
                        cow.milkgod.cheese.module.modules.Jesus.oldMode.value = true;
                        Logger.logChat("Set Jesus mode to §eOld");
                    }
                }
                catch (Exception ex) {
                    Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                }
                Cheese.getInstance();
                Cheese.fileManager.saveFiles();
                this.Toggle();
            }
        });
    }
}
