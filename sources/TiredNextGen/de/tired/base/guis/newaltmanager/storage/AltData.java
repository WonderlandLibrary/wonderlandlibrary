package de.tired.base.guis.newaltmanager.storage;

import de.tired.util.animation.Animation;
import de.tired.util.animation.ColorAnimation;
import de.tired.base.interfaces.IHook;
import lombok.Getter;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;


@Getter
public class AltData implements IHook {

    private final String name, emailAddress, password, uuid;

    public ColorAnimation hoverAnimationColor = new ColorAnimation();
    public Animation hoverAnimationOutlineAlpha = new Animation();

    public AltData(String name, String email, String password, String uuid) {
        this.name = name;
        this.emailAddress = email;
        this.password = password;
        this.uuid = uuid;
    }

    public ResourceLocation head;

    public void loadHead() {
        if (head == null) {
            head = new ResourceLocation("heads/" + getName());

            ThreadDownloadImageData textureHead = new ThreadDownloadImageData(null, "https://crafatar.com/avatars/" + uuid, null, null);
            MC.getTextureManager().loadTexture(head, textureHead);
        }
    }

}
