package io.github.nevalackin.client.binding;

import io.github.nevalackin.client.event.game.input.InputType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Objects;

public class Bind {

    private final InputType inputType;
    private final int code;
    private final BindType bindType;

    public Bind(InputType inputType, int code, BindType bindType) {
        this.inputType = inputType;
        this.code = code;
        this.bindType = bindType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public BindType getBindType() {
        return bindType;
    }

    public int getCode() {
        return code;
    }

    public String getCodeName() {
        switch (this.inputType) {
            case KEYBOARD:
                return Keyboard.getKeyName(this.code);
            case MOUSE:
                return Mouse.getButtonName(this.code);
        }

        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.inputType, this.code, this.bindType);
    }
}
