package me.robbanrobbin.jigsaw.crash;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

public class JigsawCrashReporter {
	
	public static String host = "192.121.166.142";
	
	public static int port = 8585;
	
	public static boolean sendCrashReportToServer(CrashReport crashReport) {
		
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 10000);
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
			
			out.write("-EOF-e086b60171a1da4394073041acf722ac-EOF-" + "\n");
			
			out.write(crashReport.getCompleteReport() + "\n");
			out.write("-EOF-e086b60171a1da4394073041acf722ac-EOF-" + "\n");
			
			out.write(Minecraft.getMinecraft().session.getUsername() + "\n");
			out.write(Minecraft.getMinecraft().session.getSessionType().toString() + "\n");
			out.write(Jigsaw.getClientVersion() + "\n");
			
			out.flush();
			
			out.close();
			socket.close();
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
