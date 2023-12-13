package today.flux.module.implement.Command;

import net.minecraft.network.play.client.C01PacketChatMessage;
import today.flux.module.Command;

/**
 * Created by John on 2017/04/25.
 */
@Command.Info(name = "say", syntax = { "<message>" }, help = "Say the specified message.")
public class Say extends Command {
    @Override
    public void execute(String[] args) throws Error {
        if (args.length >= 1) {
            String message = "";
            for (String str: args) {
                message += str + " ";
            }

            this.mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
        } else {
            this.syntaxError();
        }
    }
}
