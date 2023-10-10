package vestige.util.animation;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import vestige.util.misc.TimerUtil;
import vestige.util.misc.VoidFunction;

@Getter
public class Animation {

    private boolean rendered;

    @Setter
    private AnimationType animType;

    @Setter
    private long animDuration = 250;

    private AnimationState state = AnimationState.IN;

    private final TimerUtil timer = new TimerUtil();

    private boolean animDone;

    private boolean lastEnabled;

    private float timeElapsed;

    public void updateState(boolean rendered) {
        this.rendered = rendered;

        if (timer.getTimeElapsed() >= animDuration) {
            animDone = true;
        }

        if (animDone) {
            timer.reset();
        }

        if (rendered && !lastEnabled) {
            state = AnimationState.IN;

            if (!animDone) {
                timer.setTimeElapsed(animDuration - timer.getTimeElapsed());
            }

            animDone = false;
        } else if (!rendered && lastEnabled) {
            state = AnimationState.OUT;

            if (!animDone) {
                timer.setTimeElapsed(animDuration - timer.getTimeElapsed());
            }

            animDone = false;
        }

        timeElapsed = Math.max(timer.getTimeElapsed(), 1);

        this.lastEnabled = rendered;
    }

    public void render(VoidFunction renderInstructions, float startX, float startY, float endX, float endY) {
        GL11.glPushMatrix();

        if (!animDone) {
            if (state == AnimationState.IN)
                startAnimationIn(startX, startY, endX, endY);
            else
                startAnimationOut(startX, startY, endX, endY);
        }

        renderInstructions.execute();

        if (!animDone) {
            if (state == AnimationState.IN)
                stopAnimationIn(startX, startY, endX, endY);
            else
                stopAnimationOut(startX, startY, endX, endY);
        }

        GL11.glPopMatrix();
    }

    public float getYMult() {
        timeElapsed = Math.max(timer.getTimeElapsed(), 1);

        if (!animDone) {
            if (animType == AnimationType.POP || animType == AnimationType.SLIDE) {
                if (rendered) {
                    return timeElapsed / animDuration;
                } else {
                    return 1.0F - timeElapsed / animDuration;
                }
            } else if (animType == AnimationType.POP2) {
                float anim1 = animDuration * 0.9F;
                float anim2 = animDuration * 0.1F;

                return rendered ? anim1 >= timeElapsed ?
                        1.1F / anim1 * timeElapsed : 1.1F - (0.1F / anim2 * (timeElapsed - anim1)) :
                        timeElapsed >= anim2 ? 1.1F - 1.1F / anim1 * (timeElapsed - anim2) : 1F + 0.1F / anim2 * timeElapsed;
            }
        }

        return 1F;
    }

    private void startAnimationIn(float startX, float startY, float endX, float endY) {
        switch (animType) {
            case POP:
            case POP2:
                startScaling(getYMult(), startX, startY, endX, endY);
                break;
            case SLIDE:
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

                int screenWidth = sr.getScaledWidth() + 3;

                float x;

                if(startX > sr.getScaledWidth() / 2) {
                    x = (screenWidth - startX) * (1 - getYMult());
                } else {
                    x = -endX * (1 - getYMult());
                }

                GL11.glTranslatef(x, 0, 0);
                break;
        }
    }

    private void stopAnimationIn(float startX, float startY, float endX, float endY) {
        switch (animType) {
            case POP:
            case POP2:
                stopScaling(getYMult(), startX, startY, endX, endY);
                break;
            case SLIDE:
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

                int screenWidth = sr.getScaledWidth() + 3;

                float x;

                if(startX > sr.getScaledWidth() / 2) {
                    x = (screenWidth - startX) * (1 - getYMult());
                } else {
                    x = -endX * (1 - getYMult());
                }

                GL11.glTranslatef(-x, 0, 0);
                break;
        }
    }

    private void startAnimationOut(float startX, float startY, float endX, float endY) {
        switch (animType) {
            case POP:
            case POP2:
                startScaling(getYMult(), startX, startY, endX, endY);
                break;
            case SLIDE:
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

                int screenWidth = sr.getScaledWidth() + 3;

                float x;

                if(startX > sr.getScaledWidth() / 2) {
                    x = (screenWidth - startX) * (1 - getYMult());
                } else {
                    x = -endX * (1 - getYMult());
                }

                GL11.glTranslatef(x, 0, 0);
                break;
        }
    }

    private void stopAnimationOut(float startX, float startY, float endX, float endY) {
        switch (animType) {
            case POP:
            case POP2:
                stopScaling(getYMult(), startX, startY, endX, endY);
                break;
            case SLIDE:
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

                int screenWidth = sr.getScaledWidth() + 3;

                float x;

                if(startX > sr.getScaledWidth() / 2) {
                    x = (screenWidth - startX) * (1 - getYMult());
                } else {
                    x = -endX * (1 - getYMult());
                }

                GL11.glTranslatef(-x, 0, 0);
                break;
        }
    }

    private void startScaling(float mult, float startX, float startY, float endX, float endY) {
        float middleX = startX + (endX - startX) * 0.5F;
        float middleY = startY + (endY - startY) * 0.5F;

        float translateX = middleX - middleX * mult;
        float translateY = middleY - middleY * mult;

        GL11.glTranslatef(translateX, translateY, 1);
        GL11.glScalef(mult, mult, 1);
    }

    private void stopScaling(float mult, float startX, float startY, float endX, float endY) {
        float middleX = startX + (endX - startX) * 0.5F;
        float middleY = startY + (endY - startY) * 0.5F;

        float translateX = middleX - middleX * mult;
        float translateY = middleY - middleY * mult;

        GL11.glScalef(1.0F / mult, 1.0F / mult, 1);
        GL11.glTranslatef(-translateX, -translateY, 1);
    }

}
