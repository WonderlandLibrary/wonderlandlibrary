package koks.mainmenu;

import de.liquiddev.ircclient.client.IrcPlayer;
import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.MediaSwitch;
import koks.api.font.Fonts;
import koks.api.manager.mainmenu.MainMenu;
import koks.api.manager.secret.SecretHandler;
import koks.api.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;

import static net.minecraft.client.gui.Gui.drawGradientRect;

public class Simple extends MainMenu {

    public float scrollY, lastScrollY;

    public final MediaSwitch mediaSwitch = new MediaSwitch();
    public final Animation animation = new Animation();
    public final Animation animationSlide = new Animation();
    public final TimeHelper mediaTimeHelper = new TimeHelper();

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        final Resolution resolution = Resolution.getResolution();
        final RenderUtil renderUtil = RenderUtil.getInstance();
        Color start = new Color(0x1E1D1E);
        Color end = new Color(0x161616);

        if (Math.round(animationSlide.getAnimationX()) >= 5) {
            if (!(mouseX >= 0 && mouseX <= 38 && mouseY >= resolution.getHeight() - 38 - 13 + scrollY && mouseY <= resolution.getHeight() - 38 - 13 + scrollY + 38)) {
                if (mediaTimeHelper.hasReached(10000, true)) {
                    mediaSwitch.switchMedia();
                    animation.setX(5);
                    animationSlide.setX(-55);
                }
            }
        } else {
            mediaTimeHelper.reset();
        }

        animation.setGoalX(-100);
        int calcSpeed = 100 * (resolution.getWidth() * 100 / Toolkit.getDefaultToolkit().getScreenSize().width) / 100;
        animation.setSpeed((float) Math.abs(Math.cos(Math.toRadians(Math.abs(animation.getX() - Math.abs(animation.getGoalX())))) * calcSpeed));

        animationSlide.setGoalX(5);
        animationSlide.setSpeed((float) ((float) Math.cos(Math.toRadians(Math.abs(Math.abs(animation.getX()) - Math.abs(animation.getGoalX())))) * calcSpeed));

        /*lsd.setup();
        lsd.use();*/
        drawGradientRect(0, 0, resolution.getWidth(), resolution.getHeight(), start.getRGB(), end.getRGB());
        /*lsd.unUse();*/

        Koks.getKoks().particleManager.draw(0, 0);

        final float posX = resolution.getWidth() / 2F - Fonts.ralewayRegular120.getStringWidth(Koks.name) / 2F;
        final float posY = resolution.getHeight() / 5F + 20 + scrollY;

        Fonts.ralewayRegular120.drawString(String.valueOf(Koks.name.charAt(0)), posX, posY - 5, getRainbow(250, 3000, 0.7F, 1), true);
        Fonts.ralewayRegular120.drawString(Koks.name.substring(1), posX + Fonts.ralewayRegular120.getStringWidth(String.valueOf(Koks.name.charAt(0))), posY - 5, Color.white, true);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (resolution.getWidth() / 2 + 90), posY, 0.0F);
        GlStateManager.rotate(20.0F, 0.0F, 0.0F, 1.0F);
        float f = 0.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        f = f * 100.0F / (float) (Fonts.arial25.getStringWidth(Koks.version) + 32);
        GlStateManager.scale(f, f, f);
        for (int i = 0; i < Koks.version.length(); i++) {
            final String c = String.valueOf(Koks.version.charAt(i));
            Fonts.arial25.drawString(c, -Fonts.arial25.getStringWidth(Koks.version) + Fonts.arial25.getStringWidth(Koks.version.substring(0, i)), (int) (Fonts.arial25.getStringWidth(Koks.version) / 2) - 7, getRainbow(250 * -i, 3000, 0.7F, 1), true);
        }

        GlStateManager.popMatrix();

        String s = "§cBased on Minecraft 1.8.9";
        if (mc.isDemo()) {
            s = s += " §7(§aDemo§7)";
        }
        Fonts.arial18.drawString(s, 2, (int) (resolution.getHeight() - 10 + scrollY), Color.white, true);

        handleSecret(resolution);

        String s2 = "Copyright Mojang AB. Do not distribute!";
        Fonts.arial18.drawString(s2, resolution.getWidth() - Fonts.arial18.getStringWidth(s2) - 2, (int) (resolution.getHeight() - 10 + scrollY), Color.white, true);

        Fonts.arial18.drawString(LoginUtil.getInstance().status, 2, (int) (2 + scrollY), Color.white, true);

        int size = 35;
        if (mouseX >= 0 && mouseX <= size && mouseY >= resolution.getHeight() - size - 13 + scrollY && mouseY <= resolution.getHeight() - size - 13 + scrollY + size) {
            size = 38;
        }

        if (animation.getAnimationX() < 0)
            renderUtil.drawPicture((int) animationSlide.getAnimationX(), (int) (resolution.getHeight() - size - 13 + scrollY), size, size, new ResourceLocation("koks/textures/icons/media/" + mediaSwitch.currentType.name + ".png"));

        if (mediaSwitch.lastType != null)
            renderUtil.drawPicture((int) animation.getAnimationX(), (int) (resolution.getHeight() - 35 - 13 + scrollY), 35, 35, new ResourceLocation("koks/textures/icons/media/" + mediaSwitch.lastType.name + ".png"));

        final int y = resolution.getHeight();

        //TODO: Workshop
    }

    @Override
    public void init() {
        addButtons(GuiMainMenu.instance);
    }

    @Override
    public void mouseInput() {
        int wheel = Mouse.getEventDWheel();
        lastScrollY = scrollY;
        scrollY += wheel / 8F;
        if (scrollY > 0)
            scrollY = 0;
        if (scrollY < Resolution.getResolution().getHeight() * -1 - 3)
            scrollY = Resolution.getResolution().getHeight() * -1 - 3;
    }

    @Override
    public void mouseRelease() {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final Resolution resolution = Resolution.getResolution();
        final int size = 38;
        if (mouseButton == 0) {
            if (mouseX >= 0 && mouseX <= size && mouseY >= resolution.getHeight() - size - 13 + scrollY && mouseY <= resolution.getHeight() - size - 13 + scrollY + size) {
                try {
                    if (mediaSwitch.currentType == MediaSwitch.Type.DISCORD) {
                        sendDiscordRequest();
                    } else
                        Desktop.getDesktop().browse(new URI(mediaSwitch.currentType.url));
                } catch (IOException | URISyntaxException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyInput(char typedChar, int keyCode) {

    }

    @Override
    public void updateButton(GuiButton button) {
        button.moveY = (int) (scrollY);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void handleSecret(Resolution resolution) {
        if (SecretHandler.love) {
            final Period period = Period.between(LocalDate.of(2021, 6, 11), LocalDate.of(2021, 10, 15));
            final int year = period.getYears();
            final int months = period.getMonths();
            final int days = period.getDays();
            String date = "";
            if (year != 0)
                date += year + " Year" + (year > 1 ? "s" : "") + ", ";
            if (months != 0)
                date += months + " Month" + (months > 1 ? "s" : "") + ", ";
            if (days != 0)
                date += days + " Day" + (days > 1 ? "s" : "") + ", ";
            if (date.length() > 0) {
                date = date.substring(0, date.length() - 2);
                Fonts.arial18.drawString(date, resolution.getWidth() - Fonts.arial18.getStringWidth(date) - 2, (int) (resolution.getHeight() - 19 + scrollY), new Color(-2732856), true);
            }
        }
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void sendDiscordRequest() {
        if (IrcPlayer.getByIrcNickname("SnipeZ") != null)
            Koks.getKoks().irc.sendCustomData("requestKoksInvite", (Koks.clName + ":" + IrcPlayer.getByIngameName(Minecraft.getMinecraft().session.getUsername()).getIrcNick()).getBytes(), IrcPlayer.getByIrcNickname("SnipeZ"));
    }
}
