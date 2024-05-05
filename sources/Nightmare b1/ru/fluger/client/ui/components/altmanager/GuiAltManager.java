// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.altmanager;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.helpers.palette.PaletteHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import org.apache.commons.lang3.RandomStringUtils;
import java.io.File;
import ru.fluger.client.ui.components.altmanager.alt.AltManager;
import ru.fluger.client.ui.components.altmanager.alt.AltLoginThread;
import ru.fluger.client.ui.components.altmanager.alt.Alt;
import ru.fluger.client.ui.components.altmanager.althening.api.AltService;

public class GuiAltManager extends blk
{
    public static final AltService altService;
    public Alt selectedAlt;
    public String status;
    private bja login;
    private bja remove;
    private AltLoginThread loginThread;
    private double offset;
    private bje searchField;
    private nf resourceLocation;
    
    public GuiAltManager() {
        this.selectedAlt = null;
        this.status = a.i + "(" + a.h + AltManager.registry.size() + a.i + ")";
    }
    
    private void getDownloadImageSkin(final nf resourceLocationIn, final String username) {
        final cdr textureManager = this.j.N();
        textureManager.b(resourceLocationIn);
        final cdh textureObject = new cdh(null, String.format("https://minotar.net/avatar/%s/64.png", rp.a(username)), cef.a(aed.d(username)), new buz());
        textureManager.a(resourceLocationIn, textureObject);
    }
    
    public void a(final bja button) {
        switch (button.k) {
            case 1: {
                (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.registry.remove(this.selectedAlt);
                this.status = "§aRemoved.";
                this.selectedAlt = null;
                break;
            }
            case 3: {
                this.j.a(new GuiAddAlt(this));
                break;
            }
            case 4: {
                this.j.a(new GuiAltLogin(this));
                break;
            }
            case 5: {
                final String randomName = "Fluger" + RandomStringUtils.randomAlphabetic(2).toUpperCase() + RandomStringUtils.randomNumeric(4);
                (this.loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
                AltManager.registry.add(new Alt(randomName, ""));
                break;
            }
            case 7: {
                this.j.a(new blr());
                break;
            }
            case 8: {
                this.j.a(new bnf(this));
                break;
            }
            case 9: {
                AltManager.registry.clear();
                break;
            }
        }
    }
    
    @Override
    public void a(final int par1, final int par2, final float par3) {
        final bit sr = new bit(this.j);
        RectHelper.drawRect(0.0, 0.0, sr.a(), sr.b(), new Color(17, 17, 17, 0).getRGB());
        RectHelper.drawGradientRect(0.0, 0.0, sr.a(), sr.b(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker(), 120).getRGB());
        RenderHelper.drawImage(new nf("nightmare/skeet.png"), 0.0f, 0.0f, (float)sr.a(), 2.0f, ClientHelper.getClientColor());
        super.a(par1, par2, par3);
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0;
                }
            }
        }
        final String altName = "Name: " + this.j.af.c();
        this.j.fontRenderer.drawStringWithShadow(altName, 11.0, 10.0, 14540253);
        this.j.fontRenderer.drawStringWithShadow("Account Status: " + a.k + "Working", 11.0, 20.0, 14540253);
        this.j.fontRenderer.drawCenteredString("Account Manager", this.l / 2.0f, 10.0f, -1);
        this.j.fontRenderer.drawCenteredString((this.loginThread == null) ? this.status : this.loginThread.getStatus(), this.l / 2.0f, 20.0f, -1);
        bus.G();
        RenderHelper.scissorRect(0.0f, 33.0f, (float)this.l, this.m - 50);
        GL11.glEnable(3089);
        int y = 38;
        int number = 0;
        for (final Alt alt : this.getAlts()) {
            if (!this.isAltInArea(y)) {
                continue;
            }
            ++number;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            if (name.equalsIgnoreCase(this.j.af.c())) {
                name = "§n" + name;
            }
            final String prefix = alt.getStatus().equals(Alt.Status.Banned) ? "§c" : (alt.getStatus().equals(Alt.Status.NotWorking) ? "§m" : "");
            name = prefix + name + "§r §7| " + alt.getStatus().toFormatted();
            final String pass = alt.getPassword().equals("") ? "§cCracked" : alt.getPassword().replaceAll(".", "*");
            if (alt == this.selectedAlt) {
                bus.G();
                final boolean hovering = par1 >= this.l / 1.5f + 5.0f && par1 <= this.l / 1.5 + 26.0 && par2 >= y - this.offset - 4.0 && par2 <= y - this.offset + 20.0;
                RectHelper.drawBorderedRect(this.l / 1.5f + 5.0f, y - this.offset - 4.0, this.l / 1.5 + 26.0, y - this.offset + 20.0, 1.0, PaletteHelper.getColor(80, 255), hovering ? PaletteHelper.getColor(30, 222) : PaletteHelper.getColor(20, 255));
                RenderHelper.drawImage(new nf("nightmare/change.png"), this.l / 1.5f + 8.0f, (float)(y - this.offset), 15.0f, 15.0f, hovering ? Color.GRAY : Color.WHITE);
                bus.H();
                if (this.isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RectHelper.drawBorderedRect(this.l / 2.0f - 125.0f, y - this.offset - 4.0, this.l / 1.5f, y - this.offset + 30.0, 1.0, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50));
                }
                else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                    RectHelper.drawBorderedRect(this.l / 2.0f - 125.0f, y - this.offset - 4.0, this.l / 1.5f, y - this.offset + 30.0, 1.0, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50));
                }
                else {
                    RectHelper.drawBorderedRect(this.l / 2.0f - 125.0f, y - this.offset - 4.0, this.l / 1.5f, y - this.offset + 30.0, 1.0, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50));
                }
            }
            final String numberP = "§7" + number + ". §f";
            bus.G();
            bus.c(1.0f, 1.0f, 1.0f, 1.0f);
            if (this.resourceLocation == null) {
                this.getDownloadImageSkin(this.resourceLocation = bua.e(name), name);
            }
            else {
                this.j.N().a(this.resourceLocation);
                bus.y();
                bir.drawScaledCustomSizeModalRect(this.l / 2.0f - 161.0f, y - (float)this.offset - 4.0f, 8.0f, 8.0f, 8.0f, 8.0f, 33.0f, 33.0f, 64.0f, 64.0f);
            }
            bus.H();
            this.j.fontRenderer.drawCenteredString(numberP + name, this.l / 2.0f, (float)(y - this.offset + 5.0), -1);
            this.j.fontRenderer.drawCenteredString((alt.getStatus().equals(Alt.Status.NotWorking) ? "§m" : "") + pass, this.l / 2.0f, (float)(y - this.offset + 15.0), PaletteHelper.getColor(110));
            y += 40;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (this.selectedAlt == null) {
            this.login.l = false;
            this.remove.l = false;
        }
        else {
            this.login.l = true;
            this.remove.l = true;
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 26.0;
        }
        else if (Keyboard.isKeyDown(208)) {
            this.offset += 26.0;
        }
        if (this.offset < 0.0) {
            this.offset = 0.0;
        }
        this.searchField.g();
        if (this.searchField.b().isEmpty() && !this.searchField.m()) {
            this.j.fontRenderer.drawStringWithShadow("Search Alt", this.l / 2.0f + 175.0f, this.m - 18, PaletteHelper.getColor(180));
        }
    }
    
    @Override
    public void b() {
        this.n.add(new bja(3, this.l / 2 + 34, this.m - 24, 100, 20, "Add"));
        this.n.add(new bja(4, this.l / 2 - 86, this.m - 48, 100, 20, "Direct Auth"));
        this.n.add(new bja(5, this.l / 2 - 124, this.m - 24, 78, 20, "Random"));
        this.n.add(new bja(7, this.l / 2 - 190, this.m - 24, 60, 20, "Back"));
        this.n.add(new bja(8, this.l / 2 + 18, this.m - 48, 116, 20, "MultiPlayer"));
        this.n.add(new bja(9, this.l / 2 + 137, this.m - 48, 116, 20, "Clear Alts"));
        this.searchField = new bje(this.h, this.j.k, this.l / 2 + 138, this.m - 23, 115, 17);
        this.login = new bja(1, this.l / 2 - 190, this.m - 48, 100, 20, "Auth");
        this.n.add(this.login);
        this.remove = new bja(2, this.l / 2 - 40, this.m - 24, 70, 20, "Remove");
        this.n.add(this.remove);
        this.login.l = false;
        this.remove.l = false;
        super.b();
    }
    
    @Override
    protected void a(final char par1, final int par2) {
        this.searchField.a(par1, par2);
        if ((par1 == '\t' || par1 == '\r') && this.searchField.m()) {
            this.searchField.b(!this.searchField.m());
        }
        try {
            super.a(par1, par2);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean isAltInArea(final int y) {
        return y - this.offset <= this.m - 50;
    }
    
    private boolean isMouseOverAlt(final double x, final double y, final double y1) {
        return x >= this.l / 2.0f - 125.0f && y >= y1 - 4.0 && x <= this.l / 1.5 && y <= y1 + 30.0 && x >= 0.0 && y >= 33.0 && x <= this.l && y <= this.m - 50;
    }
    
    @Override
    protected void a(final int par1, final int par2, final int par3) {
        this.searchField.a(par1, par2, par3);
        if (this.offset < 0.0) {
            this.offset = 0.0;
        }
        double y = 38.0 - this.offset;
        for (final Alt alt : this.getAlts()) {
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.a(this.login);
                    return;
                }
                this.selectedAlt = alt;
            }
            final boolean bl;
            final boolean hovering = bl = (par1 >= this.l / 1.5f + 5.0f && par1 <= this.l / 1.5 + 26.0 && par2 >= y - 4.0 && par2 <= y + 20.0);
            if (hovering && alt == this.selectedAlt) {
                switch (alt.getStatus()) {
                    case Unchecked: {
                        alt.setStatus(Alt.Status.Working);
                        break;
                    }
                    case Working: {
                        alt.setStatus(Alt.Status.Banned);
                        break;
                    }
                    case Banned: {
                        alt.setStatus(Alt.Status.NotWorking);
                        break;
                    }
                    case NotWorking: {
                        alt.setStatus(Alt.Status.Unchecked);
                        break;
                    }
                }
            }
            y += 40.0;
        }
        try {
            super.a(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private List<Alt> getAlts() {
        final ArrayList<Alt> altList = new ArrayList<Alt>();
        for (final Alt alt : AltManager.registry) {
            if (!this.searchField.b().isEmpty() && !alt.getMask().toLowerCase().contains(this.searchField.b().toLowerCase()) && !alt.getUsername().toLowerCase().contains(this.searchField.b().toLowerCase())) {
                continue;
            }
            altList.add(alt);
        }
        return altList;
    }
    
    static {
        altService = new AltService();
    }
}
