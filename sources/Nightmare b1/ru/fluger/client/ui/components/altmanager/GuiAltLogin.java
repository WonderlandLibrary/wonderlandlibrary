// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.altmanager;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import ru.fluger.client.ui.components.altmanager.alt.Alt;
import ru.fluger.client.ui.components.altmanager.alt.AltLoginThread;

public final class GuiAltLogin extends blk
{
    private final blk previousScreen;
    private PasswordField password;
    private AltLoginThread thread;
    private bje username;
    
    public GuiAltLogin(final blk previousScreen) {
        this.previousScreen = previousScreen;
    }
    
    @Override
    protected void a(final bja button) {
        try {
            switch (button.k) {
                case 0: {
                    (this.thread = new AltLoginThread(new Alt(this.username.b(), this.password.getText()))).start();
                    break;
                }
                case 1: {
                    this.j.a(this.previousScreen);
                    break;
                }
                case 2: {
                    final String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!data.contains(":")) {
                        break;
                    }
                    final String[] credentials = data.split(":");
                    this.username.a(credentials[0]);
                    this.password.setText(credentials[1]);
                    break;
                }
            }
        }
        catch (Throwable e) {
            throw new RuntimeException();
        }
    }
    
    @Override
    public void a(final int x, final int y, final float z) {
        final bit sr = new bit(this.j);
        RectHelper.drawRect(0.0, 0.0, sr.a(), sr.b(), new Color(17, 17, 17, 255).getRGB());
        RenderHelper.drawImage(new nf("celestial/skeet.png"), 0.0f, 0.0f, (float)sr.a(), 2.0f, ClientHelper.getClientColor());
        this.username.g();
        this.password.drawTextBox();
        this.j.k.drawCenteredString("Alt Login", this.l / 2.0f, 20.0f, -1);
        this.j.k.drawCenteredString((this.thread == null) ? (a.h + "Alts...") : this.thread.getStatus(), this.l / 2.0f, 29.0f, -1);
        if (this.username.b().isEmpty() && !this.username.m()) {
            this.c(this.j.k, "Username / E-Mail", this.l / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            this.c(this.j.k, "Password", this.l / 2 - 96, 106, -7829368);
        }
        super.a(x, y, z);
    }
    
    @Override
    public void b() {
        final int height1 = this.m / 4 + 24;
        this.n.add(new bja(0, this.l / 2 - 100, height1 + 72 + 12, "Login"));
        this.n.add(new bja(1, this.l / 2 - 100, height1 + 72 + 12 + 24, "Back"));
        this.n.add(new bja(2, this.l / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
        this.username = new bje(height1, this.j.k, this.l / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.j.k, this.l / 2 - 100, 100, 200, 20);
        this.username.b(true);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void a(final char character, final int key) {
        try {
            super.a(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.m() && !this.password.isFocused()) {
                this.username.b(true);
            }
            else {
                this.username.b(this.password.isFocused());
                this.password.setFocused(!this.username.m());
            }
        }
        if (character == '\r') {
            this.a(this.n.get(0));
        }
        this.username.a(character, key);
        this.password.textboxKeyTyped(character, key);
    }
    
    @Override
    protected void a(final int x, final int y, final int button) {
        try {
            super.a(x, y, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.a(x, y, button);
        this.password.mouseClicked(x, y, button);
    }
    
    @Override
    public void m() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void e() {
        this.username.a();
        this.password.updateCursorCounter();
    }
}
