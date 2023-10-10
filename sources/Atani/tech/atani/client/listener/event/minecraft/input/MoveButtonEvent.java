package tech.atani.client.listener.event.minecraft.input;

import tech.atani.client.listener.event.Event;

public class MoveButtonEvent extends Event {
    private Button left;
    private Button right;
    private Button backward;
    private Button forward;
    private boolean sneak;
    private boolean jump;

    public MoveButtonEvent(Button left, Button right, Button backward, Button forward, boolean sneak, boolean jump) {
        this.left = left;
        this.right = right;
        this.backward = backward;
        this.forward = forward;
        this.sneak = sneak;
        this.jump = jump;
    }

    public Button getLeft() {
        return left;
    }

    public Button getRight() {
        return right;
    }

    public Button getBackward() {
        return backward;
    }

    public Button getForward() {
        return forward;
    }

    public boolean isSneak() {
        return sneak;
    }

    public boolean isJump() {
        return jump;
    }

    public void setLeft(Button left) {
        this.left = left;
    }

    public void setRight(Button right) {
        this.right = right;
    }

    public void setBackward(Button backward) {
        this.backward = backward;
    }

    public void setForward(Button forward) {
        this.forward = forward;
    }

    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setForward(final boolean forward) {
        this.getForward().button = forward;
    }

    public void setBackward(final boolean backward) {
        this.getBackward().button = backward;
    }

    public void setLeft(final boolean left) {
        this.getLeft().button = left;
    }

    public void setRight(final boolean right) {
        this.getRight().button = right;
    }
    public static class Button {
        boolean button;
        double offset;

        public Button(boolean button, double offset) {
            this.button = button;
            this.offset = offset;
        }

        public boolean isButton() {
            return button;
        }

        public double getOffset() {
            return offset;
        }

        public void setButton(boolean button) {
            this.button = button;
        }

        public void setOffset(double offset) {
            this.offset = offset;
        }
    }
}
