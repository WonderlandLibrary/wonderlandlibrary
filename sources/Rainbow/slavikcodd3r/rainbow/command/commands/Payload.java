package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "payload" })
public class Payload extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload(args[1], new PacketBuffer(Unpooled.buffer()).writeString(args[2])));
    }
    
    @Override
    public String getHelp() {
        return "payload";
    }
}
