// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils.screenshot;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import java.awt.image.BufferedImage;

public class ScreenshotRunnable implements Runnable
{
    private String host;
    private BufferedImage screenshot;
    private boolean processing;
    
    public ScreenshotRunnable(final String host, final BufferedImage screenshot) {
        this.screenshot = screenshot;
        this.host = host;
    }
    
    public boolean isProcessing() {
        return this.processing;
    }
    
    public BufferedImage get() {
        return this.processing ? null : this.screenshot;
    }
    
    @Override
    public void run() {
        this.processing = true;
        if (this.host.equals("imgur")) {
            try {
                final ImageHost imageHost = ImageHost.imageHosts.get("imgur");
                if (imageHost == null) {
                    new ImgurHandler();
                }
                System.out.println(imageHost != null);
                imageHost.upload(this.screenshot, ImageHost.UPLOAD_METHOD.ANON, new String[0]);
                final String link = imageHost.getLink();
                final ChatComponentText linkChat = new ChatComponentText(EnumChatFormatting.GREEN + "Uploaded screenshot at " + EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + link + EnumChatFormatting.RESET + EnumChatFormatting.GREEN + "!");
                linkChat.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
                Minecraft.getMinecraft().thePlayer.addChatMessage(linkChat);
                addToClipboard(link);
                imageHost.upload(this.screenshot, ImageHost.UPLOAD_METHOD.ANON, new String[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.processing = false;
    }
    
    public static void addToClipboard(final String string) {
        final StringSelection selection = new StringSelection(string);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
