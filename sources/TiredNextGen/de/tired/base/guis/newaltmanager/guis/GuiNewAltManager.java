package de.tired.base.guis.newaltmanager.guis;


import de.tired.base.guis.newaltmanager.save.AltFile;
import de.tired.base.guis.newaltmanager.storage.AltData;
import de.tired.base.guis.newaltmanager.storage.AltStorage;
import de.tired.util.animation.Easings;

import de.tired.util.render.RenderUtil;
import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.base.interfaces.IHook;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectOutlineShader;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiNewAltManager extends GuiScreen {

    private boolean selectingAltsToDelete;

    private final GuiScreen previousScreen;

    private List<AltData> altsToDelete = new ArrayList<>();

    public GuiNewAltManager(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;

        for (AltData data : AltStorage.alts)
            data.loadHead();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, new Color(30, 30, 30).getRGB());


        Gui.drawRect(0, 0, width, 30, new Color(23, 23, 23, 255).getRGB());

   //     FontManager.raleWay40.drawCenteredString(selectingAltsToDelete ? "ALT MANAGER (Delete Mode) " : "ALT MANAGER", width / 2, 7, -1);

        final AtomicInteger yAxis = new AtomicInteger(40);

        final int altX = (width / 2) - 400 / 2;

        for (AltData altData : AltStorage.alts) {

            final boolean hovering = isHovered(mouseX, mouseY, altX, yAxis.get(), 400, 30);
            {
                altData.hoverAnimationOutlineAlpha.update();
                altData.hoverAnimationOutlineAlpha.animate(hovering ? 199 : 0, .2, Easings.NONE);
                altData.hoverAnimationColor.update();
                altData.hoverAnimationColor.animate(hovering ? new Color(41, 138, 255, 194) : new Color(37, 46, 79), .2);
                RenderUtil.instance.doRenderShadow(Color.BLACK, altX, yAxis.get(), 400, 30, 22);

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(altX, yAxis.get(), 400, 30, 2, new Color(20, 20, 20));
            }

            ShaderManager.shaderBy(RoundedRectOutlineShader.class).drawRound(altX - 1, yAxis.get() - 1, 400 + 2, 32, 3, 1, new Color(244, 244, 244, (int) altData.hoverAnimationOutlineAlpha.getValue()));
            RenderUtil.instance.drawHead(altData, altX + 4, yAxis.get() + 2, 25);
            FontManager.raleWay20.drawString(altData.getName().toUpperCase(), calculateMiddle(altData.getName().toUpperCase(), FontManager.raleWay20, altX, 400), yAxis.get() + 7, -1);
            FontManager.raleWay15.drawString(altData.getEmailAddress().toLowerCase(), calculateMiddle(altData.getEmailAddress().toLowerCase(), FontManager.raleWay15, altX, 400), yAxis.get() + 22, -1);

            yAxis.addAndGet(40);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        final AtomicInteger yAxis = new AtomicInteger(40);
        final int altX = (width / 2) - 400 / 2;
        for (AltData altData : AltStorage.alts) {
            final boolean hovering = isHovered(mouseX, mouseY, altX, yAxis.get(), 400, 30);

            if (hovering && mouseButton == 0 && !selectingAltsToDelete)
                performLogin(altData.getEmailAddress(), altData.getPassword());
            else if (hovering && mouseButton == 0)
                altsToDelete.add(altData);
            yAxis.addAndGet(40);
        }
        AltStorage.alts.removeAll(altsToDelete);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void performLogin(String email, String password) {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        try {
            MicrosoftAuthResult result = authenticator.loginWithCredentials(email, password);
            MinecraftProfile profile = result.getProfile();

            mc.session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");
        } catch (MicrosoftAuthenticationException e) {
            System.out.println("Alt Not working");
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                mc.displayGuiScreen(new GuiAddAlt());
                break;
            }
            case 1: {
                File fDir = new File(IHook.MC.mcDataDir, "Tired-NextGen");
                File f = new File(fDir, fDir + "AltData.json");
                if (!Desktop.isDesktopSupported()) {
                    System.out.println("not supported");
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                if (f.exists())
                    desktop.open(f);
                break;
            }

            case 2: {
                AltStorage.alts.clear();
                AltFile.loadAlts();
                break;
            }
            case 3:
                this.selectingAltsToDelete = !selectingAltsToDelete;
                break;
        }
        super.actionPerformed(button);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, 4, 8, 90, 15, "AddAccount"));
        this.buttonList.add(new GuiButton(1, 112, 8, 110, 15, "Open Alt File"));
        this.buttonList.add(new GuiButton(2, 240, 8, 110, 15, "Reload Alt File"));
        this.buttonList.add(new GuiButton(3, width - 130, 8, 110, 15, "Delete Alt"));
        super.initGui();
    }

    public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }

    public int calculateMousePosition(int mouse, int position) {
        return mouse + position;
    }


}
