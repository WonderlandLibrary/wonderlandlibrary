package wtf.evolution.helpers;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Mouse;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.model.models.HashMapWithDefault;

import java.awt.*;
import java.util.Map;

public class ChangeLog {

    public HashMapWithDefault<String, Type> update = new HashMapWithDefault<>();

    public double scroll = 0;
    public double scrollA = 0;

    public ChangeLog() {
        update.put("KillAura - удары идут лучше", ChangeLog.Type.FIXED);
        update.put("Jesus - работает лучше", ChangeLog.Type.FIXED);
        update.put("Spider - SunRise", ChangeLog.Type.FIXED);
        update.put("AutoLeave - выходит с сервера когда рядом игрок", ChangeLog.Type.NEW);
        update.put("GAppleTimer - показывает кулдаун яблока", ChangeLog.Type.NEW);
        update.put("SuperKnockback - дает больше велосити игроку", ChangeLog.Type.NEW);
        update.put("Freecam - позволяет летать в прогруженных чанках", ChangeLog.Type.NEW);
        update.put("InventoryDroper - выкидывает весь инвентарь", ChangeLog.Type.NEW);
        update.put("ClientSound - звуки при включении модлуя", ChangeLog.Type.NEW);
        update.put("HighJump - высокий прыжок", ChangeLog.Type.NEW);
        update.put("WebLeave - высокий прыжок когда ты в паутине", ChangeLog.Type.NEW);
    }

    public void render() {
        float maxWidth = Fonts.RUB14.getStringWidth(update.keySet().stream().max((s1, s2) -> Fonts.RUB14.getStringWidth(s1) > Fonts.RUB14.getStringWidth(s2) ? 1 : -1).get()) + 25;
        scroll += (Mouse.getDWheel() / 120f) * 10;
        scrollA = MathHelper.interpolate(scroll, scrollA, 0.2);
        scroll = MathHelper.clamp((float) scroll, 0, update.size() * 10);
        if (update.size() * 10 < 300) {
            scroll = 0;
        }
        RenderUtil.bloom(() -> {
            RoundedUtil.drawRoundOutline(5, 50 - 20, maxWidth, 300, 5, 0.01f, new Color(20, 20, 20, 150), Color.WHITE);
        }, 5, 2, Color.BLACK.getRGB());
        RoundedUtil.drawRoundOutline(5, 50 - 20, maxWidth, 300, 5, 0.01f, new Color(10, 10, 10, 150), Color.WHITE);

        double y = 40 + scrollA;
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRoundOutline(12 - 5, 52 - 20, maxWidth - 6, 295, 5, 0.1f, new Color(20, 20, 20, 150), Color.WHITE);
        StencilUtil.readStencilBuffer(1);
        for (Map.Entry<String, Type> entry : update.entrySet()) {
            if (entry.getValue().name().equalsIgnoreCase("NEW")) {
                Fonts.RUB14.drawString(ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + entry.getKey(), 10, y, -1);
            }
            else if (entry.getValue().name().equalsIgnoreCase("FIXED")) {
                Fonts.RUB14.drawString(ChatFormatting.GRAY + "[" + ChatFormatting.YELLOW + "/" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + entry.getKey(), 10, y, -1);
            }
            else if (entry.getValue().name().equalsIgnoreCase("REMOVED")) {
                Fonts.RUB14.drawString(ChatFormatting.GRAY + "[" + ChatFormatting.RED + "-" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + entry.getKey(), 10, y, -1);
            }
            y+=10;
        }
        StencilUtil.uninitStencilBuffer();

    }

    public enum Type {
        NEW,
        FIXED,
        REMOVED,
    }

}
