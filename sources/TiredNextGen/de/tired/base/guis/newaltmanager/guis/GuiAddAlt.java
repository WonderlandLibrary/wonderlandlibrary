package de.tired.base.guis.newaltmanager.guis;

import de.tired.base.guis.newaltmanager.save.AltFile;
import de.tired.base.guis.newaltmanager.storage.AltData;
import de.tired.base.guis.newaltmanager.storage.AltStorage;
import de.tired.base.font.FontManager;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

import java.awt.*;
import java.io.IOException;

public class GuiAddAlt extends GuiScreen {

    private GuiTextField email, password;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, new Color(30, 30, 30).getRGB());
        Gui.drawRect(0, 0, width, 30, new Color(23, 23, 23, 255).getRGB());
        FontManager.raleWay40.drawCenteredString("ADD ACCOUNT", width / 2, 7, -1);


        ShaderManager.shaderBy(RoundedRectGradient.class).drawRound((int) (width / 2f) - 150, (height / 2) - 100, 300, 200, 1, new Color(30, 30, 30, 255),new Color(40, 40, 40, 255));

        email.drawTextBox(true);
        email.setMaxStringLength(100);
        password.drawTextBox(true);
        password.setCensored(true);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(1337, (int) (width / 2f) - 50, (height / 2) + 50, 100, 15, "Add"));
        this.email = new GuiTextField(12, mc.fontRendererObj, (int) (width / 2f) - 100, (height / 2) - 50, 200, 20);
        this.password = new GuiTextField(1, mc.fontRendererObj, (int) (width / 2f) - 100, (height / 2), 200, 20);
        super.initGui();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        email.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        email.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1337) {
            if (!password.getText().isEmpty()) {
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                try {
                    MicrosoftAuthResult result = authenticator.loginWithCredentials(email.getText(), password.getText());
                    MinecraftProfile profile = result.getProfile();
                    mc.session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");
                    if (result.getProfile() != null) {
                        AltStorage.alts.add(new AltData(profile.getName(), email.getText(), password.getText(), profile.getId()));
                        AltFile.save();
                        //AltManagerRegistry.getRegistry().add(new AccountData(keyInputsEmailBox.getInput(), passwordBox.getInput(), profile.getName(), mc.getSession().getPlayerID(), AccountData.ACCOUNT_TYPE.MICROSOFT));
                        // Atom.ATOM.getFileManagment().writeFile(Atom.ATOM.getFileManagment().fileBy(AltFile.class));

                    }
                } catch (MicrosoftAuthenticationException e) {
                    System.out.println(email.getText() + " pass: " + password.getText());
                    System.out.println("Alt Not working");
                }
            } else {
                mc.session = new Session(email.getText(), "0", "0", "MOJANG");
            }
        }
        super.actionPerformed(button);
    }
}
