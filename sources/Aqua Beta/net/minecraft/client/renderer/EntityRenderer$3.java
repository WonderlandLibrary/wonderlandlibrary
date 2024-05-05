package net.minecraft.client.renderer;

import java.util.concurrent.Callable;
import org.lwjgl.input.Mouse;

class EntityRenderer.3
implements Callable<String> {
    final /* synthetic */ int val$k1;
    final /* synthetic */ int val$l1;

    EntityRenderer.3(int n, int n2) {
        this.val$k1 = n;
        this.val$l1 = n2;
    }

    public String call() throws Exception {
        return String.format((String)"Scaled: (%d, %d). Absolute: (%d, %d)", (Object[])new Object[]{this.val$k1, this.val$l1, Mouse.getX(), Mouse.getY()});
    }
}
