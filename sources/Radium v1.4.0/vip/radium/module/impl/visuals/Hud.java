package vip.radium.module.impl.visuals;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vip.radium.RadiumClient;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.game.WindowResizeEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.overlay.Render2DEvent;
import vip.radium.gui.font.FontManager;
import vip.radium.gui.font.FontRenderer;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.StringUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.Colors;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.render.Translate;

import java.util.*;
import java.util.function.Supplier;

@ModuleInfo(label = "HUD", category = ModuleCategory.VISUALS)
public final class Hud extends Module {

    private static final float DARK_FACTOR = 0.49F;
    private static final Map<Module, String> displayLabelCache = new HashMap<>();
    private static List<Module> moduleCache;
    private final Property<Boolean> cFontProperty = new Property<>("CFont", true);

    private final Property<Boolean> watermarkProperty = new Property<>(
            "Watermark",
            true);
    public final Property<String> watermarkTextProperty = new Property<>(
            "Watermark Text",
            RadiumClient.NAME.charAt(0) + "\247R\247F" + RadiumClient.NAME.substring(1) + " " + RadiumClient.VERSION,
            watermarkProperty::getValue);
    private final EnumProperty<ColorMode> watermarkColorModeProperty = new EnumProperty<>(
            "W-Color Mode",
            ColorMode.FADE,
            watermarkProperty::getValue);
    private final Property<Integer> watermarkColorProperty = new Property<>(
            "W-Color",
            Colors.PINK,
            watermarkProperty::getValue);
    private final Property<Integer> secondWatermarkColorProperty = new Property<>(
            "W-Color-2",
            Colors.BLUE,
            () -> watermarkProperty.getValue() &&
                    watermarkColorModeProperty.getValue() == ColorMode.BLEND);
    private final DoubleProperty watermarkFadeSpeedProperty = new DoubleProperty(
            "W-Fade Speed", 1.0,
            () -> watermarkProperty.getValue() &&
                    watermarkColorModeProperty.getValue() != ColorMode.RAINBOW &&
                    watermarkColorModeProperty.getValue() != ColorMode.STATIC,
            0.1, 5, 0.1);
    private final Property<Boolean> arrayListProperty = new Property<>("ArrayList", true);
    private final EnumProperty<ArrayListPosition> arrayListPositionProperty = new EnumProperty<>(
            "ArrayList Pos",
            ArrayListPosition.TOP,
            arrayListProperty::getValue);
    @EventLink
    public final Listener<WindowResizeEvent> onWindowResizeEvent =
            event -> updateModulePositions(event.getScaledResolution());
    private final EnumProperty<SortingMode> sortingModeProperty = new EnumProperty<>(
            "A-Sort Mode",
            SortingMode.LENGTH,
            arrayListProperty::getValue);
    private final EnumProperty<ColorMode> arrayListColorModeProperty = new EnumProperty<>(
            "A-Color Mode",
            ColorMode.FADE,
            arrayListProperty::getValue);
    private final DoubleProperty arrayListFadeSpeedProperty = new DoubleProperty(
            "A-Fade Speed", 1.0,
            () -> arrayListProperty.getValue() &&
                    arrayListColorModeProperty.getValue() != ColorMode.RAINBOW &&
                    arrayListColorModeProperty.getValue() != ColorMode.STATIC,
            0.1, 5, 0.1);
    private final Property<Boolean> arrayListBackgroundProperty = new Property<>(
            "Background", true,
            arrayListProperty::getValue);
    private final Property<Boolean> arrayListLineProperty = new Property<>(
            "Line", true,
            arrayListProperty::getValue);
    private final Property<Boolean> arrayListOutlineProperty = new Property<>(
            "Outline", true,
            arrayListProperty::getValue);
    private final Property<Integer> arrayListColorProperty = new Property<>(
            "A-Color",
            Colors.BLUE,
            () -> arrayListProperty.getValue() &&
                    arrayListColorModeProperty.getValue() != ColorMode.RAINBOW);
    private final Property<Integer> secondaryArrayListColorProperty = new Property<>(
            "A-Color-2",
            Colors.BLUE,
            () -> arrayListProperty.getValue() &&
                    arrayListColorModeProperty.getValue() == ColorMode.BLEND);
    private final Property<Boolean> notificationsProperty = new Property<>("Notifications", true);
    private final Property<Boolean> bpsProperty = new Property<>("BPS", true);
    //    private final Property<Boolean> uidProperty = new Property<>("UID", false);
    private final Property<Boolean> coordsProperty = new Property<>("Coords", false);
    private final Property<Boolean> fpsProperty = new Property<>("FPS", true);
    //    private final String uid = String.format("%04d", UserCache.getUid());
    private final Property<Boolean> potionsProperty = new Property<>("Potions", true);
    private double lastDist;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            if (bpsProperty.getValue()) {
                EntityPlayerSP player = Wrapper.getPlayer();

                double xDist = player.posX - player.lastTickPosX;
                double zDist = player.posZ - player.lastTickPosZ;

                lastDist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
            }

            if (moduleCache != null) {
                for (Module module : moduleCache)
                    displayLabelCache.put(module, getDisplayLabel(module));

                moduleCache.sort(sortingModeProperty.getValue().getSorter());
            }
        }
    };

    @EventLink(EventBusPriorities.HIGHEST)
    public final Listener<Render2DEvent> onRender2DEvent = e -> {
        final boolean topArrayList = arrayListPositionProperty.getValue() == ArrayListPosition.TOP;
        final int textHeight = 12;
        final int textOffset = textHeight - 2;
        final int offset = topArrayList ? textHeight : -textHeight;
        int color = 0x780D0D0D;
        LockedResolution lockedResolution = e.getResolution();
        int screenX = lockedResolution.getWidth();
        int screenY = lockedResolution.getHeight();

        int notificationYOffset = 2;

        FontRenderer fontRenderer = getFontRenderer();

        int potionY = topArrayList ? screenY - offset : 1;

//        if (uidProperty.getValue()) {
//            // TODO: Add when release
//
//            String text = UserCache.getUsername() + " \2477- " + uid;
////
////            String text = "neva lack + \2477- 2";
//
//            fontRenderer.drawStringWithShadow(text,
//                    screenX - fontRenderer.getWidth(text) - 2,
//                    potionY, -1);
//
//            potionY -= 11;
//        }

        if (coordsProperty.getValue()) {
            final String coords = String.format("X: \2477%.0f \247FY: \2477%.1f \247FZ: \2477%.0f",
                    Wrapper.getPlayer().posX,
                    Wrapper.getPlayer().posY,
                    Wrapper.getPlayer().posZ);


            fontRenderer.drawStringWithShadow(coords, screenX - 2 - fontRenderer.getWidth(coords), potionY, -1);

            if (topArrayList)
                notificationYOffset += textHeight;

            potionY -= offset;
        }

        if (fpsProperty.getValue()) {
            final String fps = "FPS: " + Minecraft.getDebugFPS();

            fontRenderer.drawStringWithShadow(fps, screenX - 2 - fontRenderer.getWidth(fps), potionY, -1);

            if (topArrayList)
                notificationYOffset += textHeight;

            potionY -= offset;
        }

        if (potionsProperty.getValue()) {
            for (PotionEffect effect : Wrapper.getPlayer().getActivePotionEffects()) {
                Potion potion = Potion.potionTypes[effect.getPotionID()];
                String effectName = I18n.format(
                        potion.getName()) + " " +
                        (effect.getAmplifier() + 1) +
                        " \2477" +
                        Potion.getDurationString(effect);
                fontRenderer.drawStringWithShadow(effectName,
                        screenX - 2 - fontRenderer.getWidth(effectName),
                        potionY,
                        potion.getLiquidColor());
                float potionNameHeight = fontRenderer.getHeight(effectName);

                if (!topArrayList)
                    potionNameHeight = -potionNameHeight;
                else
                    notificationYOffset += potionNameHeight;

                potionY -= potionNameHeight;
            }
        }

        boolean watermark = watermarkProperty.getValue();
        boolean arraylist = arrayListProperty.getValue();

        if (bpsProperty.getValue())
            fontRenderer.drawStringWithShadow(
                    String.format("%.2f blocks/s", lastDist * 20 * Wrapper.getTimer().timerSpeed),
                    2,
                    screenY - (Wrapper.getCurrentScreen() instanceof GuiChat ? 24 : 11),
                    -1);

        long currentMillis = -1;

        if (watermark) {
            long ms = (long) (watermarkFadeSpeedProperty.getValue().floatValue() * 1000L);

            currentMillis = System.currentTimeMillis();

            final int watermarkColor = watermarkColorProperty.getValue();
            int wColor;
            switch (watermarkColorModeProperty.getValue()) {
                case FADE:
                    wColor = RenderingUtils.fadeBetween(
                            watermarkColor,
                            RenderingUtils.darker(watermarkColor, DARK_FACTOR),
                            currentMillis % ms / (ms / 2.0F));
                    break;
                case BLEND:
                    wColor = RenderingUtils.fadeBetween(
                            watermarkColor,
                            secondWatermarkColorProperty.getValue(),
                            currentMillis % ms / (ms / 2.0F));
                    break;
                case RAINBOW:
                    wColor = RenderingUtils.getRainbow(currentMillis, 2000, 0);
                    break;
                default:
                    wColor = watermarkColor;
            }

            String watermarkText = watermarkTextProperty.getValue();
            fontRenderer.drawStringWithShadow(watermarkText, 2, 2, wColor);
        }

        if (arraylist) {
            long ms = (long) (arrayListFadeSpeedProperty.getValue().floatValue() * 1000L);

            if (currentMillis == -1)
                currentMillis = System.currentTimeMillis();

            int arrayListColor = arrayListColorProperty.getValue();
            int sArrayListColor = secondaryArrayListColorProperty.getValue();

            if (moduleCache == null)
                updateModulePositions(RenderingUtils.getScaledResolution());

            int y = topArrayList ? 2 : screenY - textOffset;

            boolean cFont = cFontProperty.getValue();
            boolean background = arrayListBackgroundProperty.getValue();
            boolean line = arrayListLineProperty.getValue();
            boolean outline = arrayListOutlineProperty.getValue();

            float previousModuleWidth = -1;

            final int moduleCacheSize = moduleCache.size();
            int lastVisibleModuleIndex = moduleCacheSize - 1;

            for (; lastVisibleModuleIndex > 0; lastVisibleModuleIndex--) {
                if (moduleCache.get(lastVisibleModuleIndex).isVisible())
                    break;
            }

            int firstVisibleModuleIndex = -1;

            int visibleModuleIndex = 0;

            for (int i = 0; i < moduleCacheSize; i++) {
                final Module module = moduleCache.get(i);
                final Translate translate = module.getTranslate();
                final String name = displayLabelCache.get(module);
                final float moduleWidth = fontRenderer.getWidth(name);
                final boolean visible = module.isVisible();
                if (visible) {
                    if (firstVisibleModuleIndex == -1)
                        firstVisibleModuleIndex = i;
                    translate.animate(screenX - moduleWidth - (line ? 2 : 1), y);
                    if (!topArrayList)
                        notificationYOffset += textHeight;
                    y += offset;
                } else {
                    translate.animate(screenX, y);
                }

                double translateX = translate.getX();
                double translateY = translate.getY();

                if (visible || translateX < screenX) {
                    int aColor;
                    final float aOffset = (currentMillis + (visibleModuleIndex * 100L)) % ms / (ms / 2.0F);
                    switch (arrayListColorModeProperty.getValue()) {
                        case FADE:
                            aColor = RenderingUtils.fadeBetween(
                                    arrayListColor,
                                    RenderingUtils.darker(arrayListColor, DARK_FACTOR),
                                    aOffset);
                            break;
                        case BLEND:
                            aColor = RenderingUtils.fadeBetween(
                                    arrayListColor,
                                    sArrayListColor,
                                    aOffset);
                            break;
                        case RAINBOW:
                            aColor = RenderingUtils.getRainbow(currentMillis, 2000, visibleModuleIndex);
                            break;
                        default:
                            aColor = arrayListColor;
                    }
                    double top = translateY - 2;
                    if (background) {
                        Gui.drawRect(translateX - 1,
                                top,
                                screenX,
                                translateY + textOffset,
                                color);
                    }
                    fontRenderer.drawStringWithShadow(
                            name,
                            (float) translateX,
                            (float) translateY - (cFont ? 1 : 0),
                            aColor);
                    if (outline) {
                        Gui.drawRect(translateX - 2,
                                translateY - 2,
                                translateX - 1,
                                translateY + textOffset,
                                aColor);

                        double outlineTop = topArrayList ? top - 1 : translateY + textOffset;
                        double outlineBottom = topArrayList ? translateY + textOffset : top - 1;

                        if (i != firstVisibleModuleIndex && moduleWidth - previousModuleWidth > 0) {
                            Gui.drawRect(translateX - 2,
                                    outlineTop,
                                    screenX - previousModuleWidth - 3,
                                    outlineTop + 1,
                                    aColor);
                        }

                        if (i != lastVisibleModuleIndex) {
                            Module nextModule = null;
                            int indexOffset = 1;

                            while (i + indexOffset <= lastVisibleModuleIndex) {
                                nextModule = moduleCache.get(i + indexOffset);
                                if (nextModule.isVisible())
                                    break;
                                nextModule = null;
                                indexOffset++;
                            }

                            if (nextModule != null) {
                                String nextModuleName = displayLabelCache.get(nextModule);
                                float nextModuleWidth = fontRenderer.getWidth(nextModuleName);

                                if (moduleWidth - nextModuleWidth > 0.5)
                                    Gui.drawRect(translateX - 2,
                                            outlineBottom,
                                            screenX - nextModuleWidth - 3,
                                            outlineBottom + 1,
                                            aColor);
                            }
                        } else {
                            Gui.drawRect(translateX - 2,
                                    outlineBottom,
                                    screenX,
                                    outlineBottom + 1,
                                    aColor);
                        }
                    }
                    if (line) {
                        Gui.drawRect(screenX - 1,
                                translateY - 2,
                                screenX,
                                translateY + (textHeight - 2),
                                aColor);
                    }

                    visibleModuleIndex++;
                    previousModuleWidth = moduleWidth;
                }
            }
        }

        if (notificationsProperty.getValue())
            RadiumClient.getInstance().getNotificationManager()
                    .render(null,
                            lockedResolution,
                            true,
                            notificationYOffset);

    };

    public Hud() {
        toggle();

        cFontProperty.addValueChangeListener((oldValue, value) -> {
            for (SortingMode mode : SortingMode.values()) {
                mode.getSorter().setFontRenderer(getFontRenderer());
            }
        });

        watermarkTextProperty.addValueChangeListener((oldValue, value) -> {
            if (value.contains("&") || value.contains("<3"))
                watermarkTextProperty.setValue(StringUtils.replaceUserSymbols(value.trim()));
        });
    }

    private static String getDisplayLabel(Module m) {
        String label = m.getLabel();
        Supplier<String> suffix = m.getSuffix();
        String updatedSuffix = m.getUpdatedSuffix();
        if (suffix != null || updatedSuffix != null) {
            return label + " \2477" + (updatedSuffix != null ? updatedSuffix : suffix.get());
        } else
            return label;
    }

    private void updateModulePositions(ScaledResolution scaledResolution) {
        if (moduleCache == null)
            moduleCache = new ArrayList<>(RadiumClient.getInstance().getModuleManager().getModules());

        int y = 1;
        for (Module module : moduleCache) {
            if (module.isEnabled()) {
                module.getTranslate().setX(scaledResolution.getScaledWidth() -
                        this.getFontRenderer().getWidth(getDisplayLabel(module)) - 2);
            } else
                module.getTranslate().setX(scaledResolution.getScaledWidth());
            module.getTranslate().setY(y);
            if (module.isEnabled())
                y += arrayListPositionProperty.getValue() == ArrayListPosition.TOP ? 12 : -12;
        }
    }

    private FontRenderer getFontRenderer() {
        return cFontProperty.getValue() ?
                FontManager.FR :
                Wrapper.getMinecraftFontRenderer();
    }

    private enum ColorMode {
        FADE, BLEND, RAINBOW, STATIC
    }

    private enum ArrayListPosition {
        TOP, BOTTOM
    }

    private enum SortingMode {
        LENGTH(new LengthComparator()),
        ALPHABETICAL(new AlphabeticalComparator());

        private final ModuleComparator sorter;

        SortingMode(ModuleComparator sorter) {
            this.sorter = sorter;
        }

        public ModuleComparator getSorter() {
            return sorter;
        }
    }

    private abstract static class ModuleComparator implements Comparator<Module> {
        protected FontRenderer fontRenderer;

        @Override
        public abstract int compare(Module o1, Module o2);

        public FontRenderer getFontRenderer() {
            return fontRenderer;
        }

        public void setFontRenderer(FontRenderer fontRenderer) {
            this.fontRenderer = fontRenderer;
        }
    }

    private static class LengthComparator extends ModuleComparator {
        @Override
        public int compare(Module o1, Module o2) {
            return Float.compare(
                    fontRenderer.getWidth(displayLabelCache.get(o2)),
                    fontRenderer.getWidth(displayLabelCache.get(o1)));
        }
    }

    private static class AlphabeticalComparator extends ModuleComparator {
        @Override
        public int compare(Module o1, Module o2) {
            String n = displayLabelCache.get(o1);
            String n1 = displayLabelCache.get(o2);
            char char0 = n.charAt(0);
            char char01 = n1.charAt(0);
            if (char0 == char01)
                return n.charAt(1) - n1.charAt(1);
            return char0 - char01;
        }
    }
}
