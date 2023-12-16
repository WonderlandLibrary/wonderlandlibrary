// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.account;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import moonsense.MoonsenseClient;
import org.lwjgl.opengl.GL11;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.account.AccountLoginThread;
import java.util.Optional;

public class AccountListEntry
{
    private final String email;
    private final String password;
    private Optional<String> username;
    private boolean loggedIn;
    public boolean shouldQueueForRemove;
    private AccountLoginThread thr;
    private String status;
    private boolean isLoggingIn;
    public static int WIDTH;
    public static int HEIGHT;
    
    static {
        AccountListEntry.WIDTH = 120;
        AccountListEntry.HEIGHT = 28;
    }
    
    public AccountListEntry(final String email, final String password) {
        this(email, password, null);
    }
    
    public AccountListEntry(final String email, final String password, final String username) {
        this.loggedIn = false;
        this.shouldQueueForRemove = false;
        this.isLoggingIn = false;
        this.email = email;
        this.password = password;
        this.username = Optional.of(username);
    }
    
    public void drawEntry(final int x, final int y, final int mouseX, final int mouseY) {
        final boolean hovered = this.isMouseInBounds(x, y, mouseX, mouseY);
        Color color = this.loggedIn ? new Color(33, 86, 29, 200) : new Color(86, 33, 29, 200);
        final String status = (this.thr == null) ? "Waiting for login..." : this.thr.getStatus().substring(2);
        if (this.thr != null && status.equalsIgnoreCase("Waiting for login...")) {
            color = new Color(86, 86, 29, 200);
        }
        if (this.thr != null && status.equalsIgnoreCase("Login failed.")) {
            color = new Color(86, 33, 29, 200);
        }
        Color outline = this.loggedIn ? new Color(43, 115, 38, 200) : new Color(115, 43, 38, 200);
        if (this.thr != null && status.equalsIgnoreCase("Waiting for login...")) {
            outline = new Color(115, 115, 38, 200);
        }
        if (this.thr != null && status.equalsIgnoreCase("Login failed.")) {
            outline = new Color(115, 43, 38, 200);
        }
        final boolean hoveredX = this.clickedRemove(x, y, mouseX, mouseY);
        GuiUtils.drawRoundedRect((float)x, (float)y, (float)(x + AccountListEntry.WIDTH), (float)(y + AccountListEntry.HEIGHT), 5.0f, hovered ? color.brighter().getRGB() : color.getRGB());
        GuiUtils.drawRoundedOutline(x, y, x + AccountListEntry.WIDTH, y + AccountListEntry.HEIGHT, 5.0f, 2.0f, outline.getRGB());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        String acc = "Microsoft Account";
        if (this.email == null && this.password == null) {
            acc = "Offline";
        }
        if (this.username.isPresent()) {
            GuiUtils.drawHead(this.username.get(), x + 2, y + 2, 24);
            MoonsenseClient.textRenderer.drawString(this.username.get(), x + 30, y + 1, -1);
            MoonsenseClient.textRenderer.drawString(acc, x + 30, y + 10, new Color(255, 255, 255, 180).getRGB());
        }
        else {
            GuiUtils.drawHead("Steve", x + 2, y + 2, 16);
            MoonsenseClient.textRenderer.drawString(this.username.get(), x + 30, y + 4, -1);
        }
        if (hoveredX) {
            GuiUtils.drawRoundedRect((float)(x + AccountListEntry.WIDTH - 12), (float)(y + 2), (float)(x + AccountListEntry.WIDTH - 2), (float)(y + 12), 4.0f, new Color(120, 120, 120, 160).getRGB());
        }
        else {
            GuiUtils.drawRoundedRect((float)(x + AccountListEntry.WIDTH - 12), (float)(y + 2), (float)(x + AccountListEntry.WIDTH - 2), (float)(y + 12), 4.0f, new Color(95, 95, 95, 160).getRGB());
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/close.png"));
        GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture(x + AccountListEntry.WIDTH - 12, y + 2, 0.0f, 0.0f, 10, 10, 10.0f, 10.0f);
    }
    
    public void mouseClicked(final int x, final int y, final int mouseX, final int mouseY) {
        if (this.isMouseInBounds(x, y, mouseX, mouseY) && this.clickedRemove(x, y, mouseX, mouseY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.shouldQueueForRemove = true;
            return;
        }
        if (this.isMouseInBounds(x, y, mouseX, mouseY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.loggingInOtherButton();
            this.login();
            this.isLoggingIn = true;
        }
    }
    
    public boolean isMouseInBounds(final int x, final int y, final int mouseX, final int mouseY) {
        return mouseX > x && mouseX < x + AccountListEntry.WIDTH && mouseY > y && mouseY < y + AccountListEntry.HEIGHT;
    }
    
    public boolean clickedRemove(final int x, final int y, final int mouseX, final int mouseY) {
        return mouseX > x + AccountListEntry.WIDTH - 12 && mouseX < x + AccountListEntry.WIDTH - 2 && mouseY > y + 2 && mouseY < y + 12;
    }
    
    public void updateEntry() {
        if (!this.isLoggingIn) {
            this.thr = null;
        }
        if (this.thr != null) {
            this.status = this.thr.getStatus();
        }
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public boolean hasStatus() {
        return this.thr != null && this.status != null && !this.status.isEmpty();
    }
    
    public void loggingInOtherButton() {
        this.thr = null;
    }
    
    public void login() {
        (this.thr = new AccountLoginThread(this.getEmail(), this.getPassword())).start();
        this.isLoggingIn = false;
        this.loggedIn = true;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getUsername() {
        return this.username.isPresent() ? this.username.get() : null;
    }
    
    public boolean isLoggedIn() {
        return this.loggedIn;
    }
    
    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
