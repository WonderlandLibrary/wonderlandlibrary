package me.offeex.bloomware.api.gui.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.config.impl.TokenConfig;
import me.offeex.bloomware.api.helper.Stopwatch;
import me.offeex.bloomware.event.dispatcher.Subscribe;
import me.offeex.bloomware.event.events.EventRender;
import me.offeex.bloomware.event.events.EventWorld;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class LoginScreen extends Screen {
    private final List<ClickableWidget> widgets = new ArrayList<>();
    int fieldWidth = 200;
    int buttonWidth = 100;
    private final Stopwatch stopwatch = new Stopwatch();
    String response = "";

    public LoginScreen() {
        super(Text.literal("Login Screen"));
        Bloomware.INSTANCE.getEVENTBUS().register(this);

        int height = 18;
        int x = centerX(fieldWidth);
        int y = centerY(height);

        addDrawableChild(textField(x, y - 25, fieldWidth, height, "Email"));
        addDrawableChild(textField(x, y, fieldWidth, height, "Password")).setMaxLength(32);
        addDrawableChild(new ButtonWidget.Builder(Text.literal("Login"), button -> {
            if (stopwatch.passed(2000)) {
                TextFieldWidget emailField = (TextFieldWidget) widgets.get(0);
                TextFieldWidget passwordField = (TextFieldWidget) widgets.get(1);
                String email = emailField.getText();
                String password = passwordField.getText();

                if (email.isEmpty() || password.isEmpty()) return;
                if (!email.contains("@") || !email.contains(".") || password.length() < 8) return;

                emailField.setEditable(false);
                passwordField.setEditable(false);
                emailField.active = false;
                passwordField.active = false;

                response = TokenConfig.INSTANCE.login(email, password);
                stopwatch.reset();

                emailField.setEditable(true);
                passwordField.setEditable(true);
                emailField.active = true;
                passwordField.active = true;
            }
        }).dimensions(x - width / 2, y + 25, buttonWidth, height).build());
        addDrawableChild(new ButtonWidget.Builder(Text.literal("Quit"), button -> {
            System.exit(0);
        }).dimensions(x + width / 2, y + 25, buttonWidth, height).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        widgets.add((ClickableWidget) drawableElement);
        return super.addDrawableChild(drawableElement);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Subscribe
    private void onJoinWorld(EventWorld.Join event) {
        if (System.currentTimeMillis() >= Bloomware.INSTANCE.getExpiresAt()) {
            System.exit(-2);
        }
    }

    @Subscribe
    private void onRenderBeforeText(EventRender.TitleScreen.AfterPanorama event) {
        if (System.currentTimeMillis() >= Bloomware.INSTANCE.getExpiresAt()) {
            Bloomware.INSTANCE.setCurrentScreen(this);
            event.setCanceled(true);
        }
    }

    @Subscribe
    public void onRenderScreen(EventRender.Screen event) {
        if (!(Bloomware.INSTANCE.getCurrentScreen() instanceof LoginScreen)) return;

        MatrixStack matrices = event.getMatrices();
        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();
        float tickDelta = event.getTickDelta();

        matrices.push();
        matrices.translate(0, 0, 1);

        DrawableHelper.fill(matrices, 0, 0, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth(), Bloomware.INSTANCE.getMc().getWindow().getScaledHeight(), new Color(56, 0, 11, 111).getRGB());

        String label = "Sign in Bloomware";
        Bloomware.INSTANCE.getMc().textRenderer.drawWithShadow(matrices, label, centerX(Bloomware.INSTANCE.getMc().textRenderer.getWidth(label)), centerY(Bloomware.INSTANCE.getMc().textRenderer.fontHeight) - 50, -1);

        widgets.get(0).setX(centerX(widgets.get(0).getWidth()));
        widgets.get(0).setY(centerY(widgets.get(0).getHeight()) - 25);

        widgets.get(1).setX(centerX(widgets.get(1).getWidth()));
        widgets.get(1).setY(centerY(widgets.get(1).getHeight()));

        int x = centerX(buttonWidth * 2);
        int offsetX = 2;

        widgets.get(2).setX(x - offsetX);
        widgets.get(2).setY(centerY(widgets.get(2).getHeight()) + 25);

        widgets.get(3).setX(x + buttonWidth + offsetX);
        widgets.get(3).setY(centerY(widgets.get(3).getHeight()) + 25);

        Bloomware.INSTANCE.getMc().textRenderer.drawWithShadow(matrices, response, centerX(Bloomware.INSTANCE.getMc().textRenderer.getWidth(response)), centerY(Bloomware.INSTANCE.getMc().textRenderer.fontHeight) + 50, -1);

        super.render(matrices, mouseX, mouseY, tickDelta);
        matrices.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        TextFieldWidget emailField = (TextFieldWidget) widgets.get(0);
        TextFieldWidget passwordField = (TextFieldWidget) widgets.get(1);

        emailField.mouseClicked(mouseX, mouseY, button);
        passwordField.mouseClicked(mouseX, mouseY, button);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        TextFieldWidget emailField = (TextFieldWidget) widgets.get(0);
        TextFieldWidget passwordField = (TextFieldWidget) widgets.get(1);

        emailField.mouseReleased(mouseX, mouseY, button);
        passwordField.mouseReleased(mouseX, mouseY, button);

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        TextFieldWidget emailField = (TextFieldWidget) widgets.get(0);
        TextFieldWidget passwordField = (TextFieldWidget) widgets.get(1);

        emailField.charTyped(chr, modifiers);
        passwordField.charTyped(chr, modifiers);

        emailField.setSuggestion(emailField.getText().equals("") ? "Email" : "");
        passwordField.setSuggestion(passwordField.getText().equals("") ? "Password" : "");

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        TextFieldWidget emailField = (TextFieldWidget) widgets.get(0);
        TextFieldWidget passwordField = (TextFieldWidget) widgets.get(1);

        emailField.keyPressed(keyCode, scanCode, modifiers);
        passwordField.keyPressed(keyCode, scanCode, modifiers);

        emailField.setSuggestion(emailField.getText().equals("") ? "Email" : "");
        passwordField.setSuggestion(passwordField.getText().equals("") ? "Password" : "");

        return false;
    }

    public TextFieldWidget textField(int x, int y, int width, int height, String text) {
        TextFieldWidget textField = new TextFieldWidget(Bloomware.INSTANCE.getMc().textRenderer, x, y, width, height, Text.of(""));
        textField.setSuggestion(text);
        return textField;
    }

    public int centerX(int width) {
        return (Bloomware.INSTANCE.getMc().getWindow().getScaledWidth() / 2) - (width / 2);
    }

    public int centerY(int height) {
        return (Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() / 2) - (height / 2);
    }
}
