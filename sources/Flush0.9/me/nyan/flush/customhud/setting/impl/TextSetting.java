package me.nyan.flush.customhud.setting.impl;

import com.mojang.realmsclient.dto.PlayerInfo;
import me.nyan.flush.Flush;
import me.nyan.flush.customhud.setting.Setting;
import me.nyan.flush.ui.elements.TextBox;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class TextSetting extends Setting {
    private final TextBox textField;

    public TextSetting(String name, String value) {
        this(name, value, null);
    }

    public TextSetting(String name, String value, BooleanSupplier supplier) {
        super(name, supplier);
        textField = new TextBox(0, 0, getWidth(), getHeight() / 2F);
        textField.setFontSize(18);
        textField.setText(value);
        textField.setBackground(false);
        textField.setStencil(false);
    }

    public String getValue() {
        return textField.getText();
    }

    public String getFormattedValue() {
        return buildFormattedString(getValue());
    }

    public void setValue(String value) {
        textField.setText(value);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        Gui.drawRect(0, 0, getWidth(), getHeight() / 2F, 0xFF1E1E1E);
        Gui.drawRect(0, getHeight() / 2F, getWidth(), getHeight(), 0xFF000000);
        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 16);
        font.drawXYCenteredString(getName(), getWidth() / 2F, getHeight() / 4F, -1);
        GlStateManager.translate(0, getHeight() / 2F, 0);
        textField.draw();
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        textField.mouseClicked(mouseX - (int) x, (int) (mouseY - (y + getHeight() / 2F)), button);
    }

    @Override
    public float getWidth() {
        return 120;
    }

    @Override
    public float getHeight() {
        return 24;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        textField.keyTyped(typedChar, keyCode);
        return textField.isFocused();
    }

    private String buildFormattedString(String value) {
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '&' && StringUtils.containsIgnoreCase("0123456789AaBbCcDdEeFfKkLlMmNnOoRr", String.valueOf(chars[i + 1]))) {
                chars[i] = '§';
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }

        String string = new String(chars);
        for (Map.Entry<String, String> entry : getPlaceholders().entrySet()) {
            string = string.replace("$" + entry.getKey(), entry.getValue());
        }

        return string;
    }

    public static Map<String, String> getPlaceholders() {
        Minecraft mc = Minecraft.getMinecraft();
        HashMap<String, String> map = new HashMap<>();
        map.put("fps", String.valueOf(Minecraft.getDebugFPS()));
        map.put("bps", String.valueOf(Math.round(MovementUtils.getBPS() * 100f) / 100f));
        Date currentDate = new Date();
        String time = new SimpleDateFormat("HH:mm:ss").format(currentDate);
        String date = new SimpleDateFormat("dd/MM/yy").format(currentDate);
        map.put("time", time);
        map.put("date", date);
        map.put("serverIP", mc.isIntegratedServerRunning() ? "Singleplayer" : mc.getCurrentServerData().serverIP);
        map.put("serverName", mc.isIntegratedServerRunning() ? "Singleplayer" : mc.getCurrentServerData().serverName);

        if (mc.thePlayer != null) {
            map.put("posX", String.valueOf(Math.round(mc.thePlayer.posX)));
            map.put("posY", String.valueOf(Math.round(mc.thePlayer.posY)));
            map.put("posZ", String.valueOf(Math.round(mc.thePlayer.posZ)));
            NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
            if (info != null) {
                map.put("ping", mc.isIntegratedServerRunning() ? "-1" : String.valueOf(info.getResponseTime()));
                map.put("gameMode", info.getGameType().getName());
            }
            map.put("username", mc.thePlayer.getName());
            map.put("health", String.valueOf(mc.thePlayer.getHealth()));
            map.put("foodLevel", String.valueOf(mc.thePlayer.getFoodStats().getFoodLevel()));
            map.put("ticksExisted", String.valueOf(mc.thePlayer.ticksExisted));
        }

        map.put("clientName", Flush.NAME);
        map.put("clientVersion", Flush.VERSION);
        map.put("flushUsername", String.valueOf(Flush.currentUser.getUsername()));
        map.put("flushUID", String.valueOf(Flush.currentUser.getUid()));
        map.put("enabledModules", String.valueOf(Flush.getInstance().getModuleManager().getEnabledModules().size()));
        map.put("modulesCount", String.valueOf(Flush.getInstance().getModuleManager().getModules().size()));
        return map;
    }
}
