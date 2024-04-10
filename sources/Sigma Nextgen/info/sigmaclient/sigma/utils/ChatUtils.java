package info.sigmaclient.sigma.utils;

import info.sigmaclient.sigma.SigmaNG;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
public class ChatUtils {
    public static void sendMessage(String message){
        Minecraft.getInstance().player.addChatMessage(
                new StringTextComponent(
                        message
                )
        );
    }
    public static void sendMessageWithPrefix(String message){
        if(SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA){
            Minecraft.getInstance().player.addChatMessage(
                    new StringTextComponent(
                            "\n§f[§6Sigma§f] §7" + message + "\n"
                    )
            );
        }else{
            Minecraft.getInstance().player.addChatMessage(
                    new StringTextComponent(
                            "§7[§cNursultan§7] §f" + message + ""
                    )
            );
        }
    }
}
