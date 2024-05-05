package best.azura.client.impl.ui;

import java.util.ArrayList;
import java.util.Arrays;

public class AnimatedTexture {

    private final ArrayList<Texture> textures = new ArrayList<>();
    private Texture currentTexture;
    private int index;

    public AnimatedTexture(Texture... textures) {
        this.textures.addAll(Arrays.asList(textures));
        this.index = 0;
        this.currentTexture = this.textures.isEmpty() ? null : this.textures.get(0);
    }

    public void updateIndex() {
        this.index++;
        if (this.index > this.textures.size() - 1)
            this.index = 0;
        if (textures.isEmpty()) this.currentTexture = null;
        else this.currentTexture = this.textures.get(this.index);
    }

    public Texture getCurrentTexture() {
        return currentTexture;
    }

    public ArrayList<Texture> getTextures() {
        return textures;
    }
}