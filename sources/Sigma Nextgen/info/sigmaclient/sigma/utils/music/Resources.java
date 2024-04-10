package info.sigmaclient.sigma.utils.music;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.net.URL;

import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class Resources {
	public static TextureImage downloadTexture(final String imageURL) {
		if (TextureManager.exists(imageURL)) {
			if (TextureManager.get(imageURL) != null) {
				return TextureManager.get(imageURL);
			}
		}
		TextureImage textureImage = new TextureImage();
		textureImage.location = imageURL;
		final TextureImage ti = textureImage;
		TextureManager.cache.add(ti);
		new Thread(() -> {
			try {
				URL urlfile = new URL(imageURL);
				InputStream inStream = urlfile.openStream();
				ti.pixels = ImageIO.read(inStream);
				inStream.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}).start();
		return ti;
	}
}
