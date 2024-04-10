package info.sigmaclient.sigma.gui.clickgui.musicplayer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.client.gui.screen.ChatScreen;
import info.sigmaclient.sigma.sigma5.jellomusic.Player;
import info.sigmaclient.sigma.modules.gui.hide.ClickGUI;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.sigma5.jellomusic.player.JelloMusic;

public class MusicWaveRender
    {
        public static MusicWaveRender SELF;
        public static float[] volces;
        public static JelloMusic music;
        public PartialTicksAnim scaleAnim;
        public PartialTicksAnim[] volcesTransAnim;

        public MusicWaveRender() {
            this.scaleAnim = new PartialTicksAnim(0.0f);
            this.volcesTransAnim = new PartialTicksAnim[110];
            for (int i = 0; i < this.volcesTransAnim.length; ++i) {
                this.volcesTransAnim[i] = new PartialTicksAnim(0.0f);
            }
        }

    public void onTick() {
            for (int i = 0; i < 110; ++i) {
                try {
                    final float avgBuffer = Math.abs(MusicWaveRender.volces[i]) * 1.7f;
                    this.volcesTransAnim[i].interpolate(avgBuffer, (this.volcesTransAnim[i].getValue() - avgBuffer < -20.0f) ? 5.0 : 3.0);
                }
                catch (final NullPointerException d) {
                    d.printStackTrace();
                }
            }
        float scale = 0.0f;
            for (int u = 109; u > 60; --u) {
                scale += MusicWaveRender.volces[u];
                scale /= 8.0f;
            }
        scale = (float)Math.min(scale, 2.0);
            scale = -scale;
            this.scaleAnim.interpolate(scale * 20.0f, 1.5);
        }

    public void drawWave() {
            if (MusicPlayer.firstColor == null || MusicPlayer.secondColor == null || ClickGUI.clickGui.musicPlayer.currentFile == null || Player.player == null) {
                return;
            }
        Player.player.drawLines();
            Player.player.drawWave();
            if (!(Module.mc.currentScreen instanceof ChatScreen)) {
                return;
            }
        this.drawTexture2();
        }

    public void drawTexture2() {
            if (Player.player == null) {
                return;
            }
        Player.player.drawTexture();
        }

    public void drawTexture() {
            if (MusicPlayer.firstColor == null || MusicPlayer.secondColor == null || ClickGUI.clickGui.musicPlayer.currentFile == null) {
                return;
            }
        if (Module.mc.currentScreen instanceof ChatScreen) {
            return;
        }
        this.drawTexture2();
        }

    public static Color averageColor(final BufferedImage bi, final int width, final int height, final int pixelStep) {
            final int[] color = new int[3];
            for (int x = 0; x < width; x += pixelStep) {
                for (int y = 0; y < height; y += pixelStep) {
                    final Color pixel = new Color(bi.getRGB(x, y));
                    final int[] array = color;
                    final int n = 0;
                    array[n] += pixel.getRed();
                    final int[] array2 = color;
                    final int n2 = 1;
                    array2[n2] += pixel.getGreen();
                    final int[] array3 = color;
                    final int n3 = 2;
                    array3[n3] += pixel.getBlue();
                }
            }
        final int num = width * height / (pixelStep * pixelStep);
            return new Color(color[0] / num, color[1] / num, color[2] / num);
        }

    static {
            MusicWaveRender.SELF = new MusicWaveRender();
            MusicWaveRender.volces = new float[110];
            MusicWaveRender.music = new JelloMusic();
        }
    }
