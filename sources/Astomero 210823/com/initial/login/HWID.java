package com.initial.login;

import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import com.initial.ui.*;
import java.security.*;
import java.net.*;
import java.io.*;

public class HWID extends GuiScreen
{
    private static GuiTextField hwid;
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        (HWID.hwid = new GuiTextField(123123, this.mc.fontRendererObj, HWID.width / 2 - 100, HWID.width / 2 - 20 - 100, 200, 20)).setMaxStringLength(16);
        this.buttonList.add(new Button(1001, HWID.width / 2 - 100, HWID.width / 2 + 20 - 100, "Login"));
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1001: {
                try {
                    System.out.print("Sex");
                    this.mc.displayGuiScreen(new GuiCustomMainMenu());
                }
                catch (Exception e) {
                    this.mc.shutdownMinecraftApplet();
                }
                break;
            }
        }
    }
    
    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "-";
            }
            ++i;
        }
        return s;
    }
    
    public static String requestURLSRC(final String BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27) throws IOException {
        final URL urlObject = new URL(BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27);
        final URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        return AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(urlConnection.getInputStream());
    }
    
    private static String AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(final InputStream L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG) throws IOException {
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG, "UTF-8"))) {
            final StringBuilder stringBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            return stringBuilder.toString();
        }
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void drawScreen(final int x2, final int y2, final float z2) {
        this.drawDefaultBackground();
        HWID.hwid.drawTextBox();
        super.drawScreen(x2, y2, z2);
    }
    
    public void mouseClicked(final int x2, final int y2, final int z2) {
        try {
            super.mouseClicked(x2, y2, z2);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        HWID.hwid.mouseClicked(x2, y2, z2);
    }
    
    @Override
    protected void keyTyped(final char character, final int key) {
        HWID.hwid.textboxKeyTyped(character, key);
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
}
