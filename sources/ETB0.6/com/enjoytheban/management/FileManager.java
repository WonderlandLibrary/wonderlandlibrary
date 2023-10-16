package com.enjoytheban.management;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.enjoytheban.Client;
import com.enjoytheban.ui.login.Alt;

import net.minecraft.client.Minecraft;

/**
 * A File Manager that can read and write data onto the operating system
 * @author Purity
 */

public class FileManager {

	//sets the file directory
	private static File dir = new File(Minecraft.getMinecraft().mcDataDir, Client.instance.name);
	private static final File ALT = FileManager.getConfigFile("Alts");
	private static final File LASTALT = FileManager.getConfigFile("LastAlt");
	/**
	 * This shit fucking hurts jesus christ
	 */
	public static void loadLastAlt() {
		try {
			String s;
			if (!LASTALT.exists()) {
				PrintWriter printWriter = new PrintWriter(new FileWriter(LASTALT));
				printWriter.println();
				printWriter.close();
			} else if (LASTALT.exists()) {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(LASTALT));
				while ((s = bufferedReader.readLine()) != null) {
					if (s.contains("\t")) {
						s = s.replace("\t", "    ");
					}
					if (s.contains("    ")) {
						String[] parts = s.split("    ");
						String[] account = parts[1].split(":");
						if (account.length == 2) {
							Client.instance.getAltManager().setLastAlt(new Alt(account[0], account[1], parts[0]));
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Client.instance.getAltManager().setLastAlt(new Alt(account[0], pw, parts[0]));
						}
					} else {
						String[] account = s.split(":");
						if (account.length == 1) {
							Client.instance.getAltManager().setLastAlt(new Alt(account[0], ""));
						} else if (account.length == 2) {
							Client.instance.getAltManager().setLastAlt(new Alt(account[0], account[1]));
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Client.instance.getAltManager().setLastAlt(new Alt(account[0], pw));
						}
					}
				}
				bufferedReader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveLastAlt() {
		try {
			PrintWriter printWriter = new PrintWriter(LASTALT);
			Alt alt = Client.instance.getAltManager().getLastAlt();
			if (alt != null) {
				if (alt.getMask().equals("")) {
					printWriter.println(alt.getUsername() + ":" + alt.getPassword());
				} else {
					printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadAlts() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(ALT));
			String s;
			if (!ALT.exists()) {
				PrintWriter printWriter = new PrintWriter(new FileWriter(ALT));
				printWriter.println();
				printWriter.close();
			} else if (ALT.exists()) {
				while ((s = bufferedReader.readLine()) != null) {
					if (s.contains("\t")) {
						s = s.replace("\t", "    ");
					}
					if (s.contains("    ")) {
						String[] parts = s.split("    ");
						String[] account = parts[1].split(":");
						if (account.length == 2) {
							Client.instance.getAltManager().getAlts().add(new Alt(account[0], account[1], parts[0]));
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Client.instance.getAltManager().getAlts().add(new Alt(account[0], pw, parts[0]));
						}
					} else {
						String[] account = s.split(":");
						if (account.length == 1) {
							Client.instance.getAltManager().getAlts().add(new Alt(account[0], ""));
						} else if (account.length == 2) {
							try {
								Client.instance.getAltManager().getAlts().add(new Alt(account[0], account[1]));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Client.instance.getAltManager().getAlts().add(new Alt(account[0], pw));
						}
					}
				}
			}
			bufferedReader.close();
		} catch (Exception e) {

		}
	}

	public static void saveAlts() {
		try {
			System.out.println("skrt");
			PrintWriter printWriter = new PrintWriter(ALT);
			for (Alt alt : Client.instance.getAltManager().getAlts()) {
				if (alt.getMask().equals("")) {
					printWriter.println(alt.getUsername() + ":" + alt.getPassword());
				} else {
					printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static File getConfigFile(final String name) {
		final File file = new File(dir, String.format("%s.txt", name));
		if (!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (IOException ex) {}
		}
		return file;
	}

	// END OF CANCER
	
	//init method
	public static void init() {
		//if the dir doesnt exist create it
		//loadAlts();
		if(!dir.exists()) {
			dir.mkdir();
		}
		loadLastAlt();
		loadAlts();
	}
	
	//method for reading
	public static List<String> read(String file) {
		//a new arraylist
		List<String> out = new ArrayList();

		try {
			
			if (!dir.exists()) {
				dir.mkdir();
			}
			
			File f = new File(dir, file);
			//if it doesnt exist create it
			if (!f.exists()) {
				f.createNewFile();
			}
		
			//basically just read the file
			try (FileInputStream fis = new FileInputStream(f);
					InputStreamReader isr = new InputStreamReader(fis);
					BufferedReader br = new BufferedReader(isr)) {
				String line = "";
				while ((line = br.readLine()) != null) {
					out.add(line);
				}
			}
			
		//catch
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return the out variable
		return out;
	}
	
	//method for saving
	public static void save(String file, String content, boolean append) {
		try {
			File f = new File(dir, file);
			//if the file doesnt exist create it
			if (!f.exists()) {
				f.createNewFile();
			}
			//write to the file
			try (FileWriter writer = new FileWriter(f, append)) {
				writer.write(content);
			}
		//catch
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}