package Method.Client.module.render;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static Method.Client.Main.setmgr;

public class BossStack extends Module {

    Setting mode = setmgr.add(new Setting("Stack mode", this, "Stack", "Stack", "Minimize"));
    Setting scale = setmgr.add(new Setting("Scale", this, .5, .5, 4, false));

    private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");

    public BossStack() {
        super("BossStack", Keyboard.KEY_NONE, Category.RENDER, "BossStack");
    }

    @Subscribe
    public void RenderGameOverLayPost(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.BOSSINFO) return;

        Map<UUID, BossInfoClient> bossInfoMap = mc.ingameGUI.getBossOverlay().mapBossInfos;
        if (bossInfoMap == null) return;

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int screenWidth = scaledResolution.getScaledWidth();
        int barPositionY = 12;

        if (mode.getValString().equalsIgnoreCase("Minimize")) {
            for (BossInfoClient bossInfo : bossInfoMap.values()) {
                barPositionY = renderScaledBossBar(event, screenWidth, barPositionY, bossInfo, bossInfo.getName().getFormattedText());
            }
        } else if (mode.getValString().equalsIgnoreCase("Stack")) {
            Map<String, Pair<BossInfoClient, Integer>> stackedBossInfoMap = getStackedBossInfoMap(bossInfoMap);
            for (Map.Entry<String, Pair<BossInfoClient, Integer>> entry : stackedBossInfoMap.entrySet()) {
                String text = entry.getKey() + " x" + entry.getValue().getValue();
                barPositionY = renderScaledBossBar(event, screenWidth, barPositionY, entry.getValue().getKey(), text);
            }
        }

    }


    @Subscribe
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO) {
            event.setCanceled(true);
        }
    }


    private Map<String, Pair<BossInfoClient, Integer>> getStackedBossInfoMap(Map<UUID, BossInfoClient> bossInfoMap) {
        Map<String, Pair<BossInfoClient, Integer>> stackedMap = new HashMap<>();
        for (BossInfoClient bossInfo : bossInfoMap.values()) {
            String bossName = bossInfo.getName().getFormattedText();
            Pair<BossInfoClient, Integer> pair = stackedMap.getOrDefault(bossName, new Pair<>(bossInfo, 0));
            pair.setValue(pair.getValue() + 1);
            stackedMap.put(bossName, pair);
        }
        return stackedMap;
    }

    private int renderScaledBossBar(RenderGameOverlayEvent.Post event, int screenWidth, int posY, BossInfoClient bossInfo, String bossName) {
        int barPositionX = (int) ((screenWidth / scale.getValDouble()) / 2 - 91);
        GlStateManager.scale(scale.getValDouble(), scale.getValDouble(), 1);
        if (!event.isCanceled()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
            mc.ingameGUI.getBossOverlay().render(barPositionX, posY, bossInfo);
            mc.fontRenderer.drawStringWithShadow(bossName, (float) ((screenWidth / scale.getValDouble()) / 2 - mc.fontRenderer.getStringWidth(bossName) / 2), (float) (posY - 9), 16777215);
        }
        GlStateManager.scale(1d / scale.getValDouble(), 1d / scale.getValDouble(), 1);
        return posY + 10 + mc.fontRenderer.FONT_HEIGHT;
    }

}

class Pair<T, S> {
    private T key;
    private S value;

    public Pair(T key, S value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public S getValue() {
        return value;
    }

    public void setValue(S value) {
        this.value = value;
    }
}
