package wtf.monsoon.impl.commands;

import wtf.monsoon.Monsoon;

import wtf.monsoon.api.command.Command;
import wtf.monsoon.api.util.entity.DamageUtil;
import wtf.monsoon.impl.ui.notification.Notification;
import wtf.monsoon.impl.ui.notification.NotificationManager;
import wtf.monsoon.impl.ui.notification.NotificationType;
import net.minecraft.entity.player.EntityPlayer;

public class BedlessTP extends Command {
    public BedlessTP() {
        super("BedlessTP", "Teleport on the server bridger.land", ".bedlesstp <playername> or .bedlesstp <x> <y> <z>", ".btp");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length == 1) {
            Monsoon.sendMessage(args[0]);
            String playername = args[0];
            for(Object toCast : mc.theWorld.playerEntities) {
                if(toCast instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) toCast;

                    if(player.getName().equalsIgnoreCase(playername)) {

                        Thread thread = new Thread(){
                            public void run(){
                               double x = player.posX;
                               double y = player.posY;
                               double z = player.posZ;

                                NotificationManager.show(new Notification(NotificationType.INFO, "BedlessTP", "Teleporting...", 1));

                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                DamageUtil.damageVerus();
                                mc.thePlayer.setPosition(x, y, z);
                                DamageUtil.damageVerus();
                            }
                        };

                        thread.start();

                    }
                }
            }
        }
        if(args.length == 3) {
            Thread thread = new Thread(){
                public void run(){
                    double x = Double.parseDouble(args[0]);
                    double y = Double.parseDouble(args[1]);
                    double z = Double.parseDouble(args[2]);

                    NotificationManager.show(new Notification(NotificationType.INFO, "BedlessTP", "Teleporting...", 1));

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    DamageUtil.damageVerus();
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    DamageUtil.damageVerus();
                    mc.thePlayer.setPosition(x, y, z);
                    DamageUtil.damageVerus();
                }
            };

            thread.start();
        }
    }
}
