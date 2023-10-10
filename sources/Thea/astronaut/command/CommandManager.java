package astronaut.command;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private static String prefix = ".";

    public static List<Command> commands = new ArrayList<>();
    public static void checkMessage(String message) {

        String[] command = message.split(" ");

        addCommand(new Bind());
        addCommand(new Toggle());

        if (message.startsWith(prefix))
        {
            for (Command c : commands)
            {
                if (c.getName().equalsIgnoreCase(command[0]))
                {
                    c.performAction(message);
                    break;
                }
            }
        }
        else
        {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
        }

    }

    public static void addCommand(Command command) {commands.add(command);}
}