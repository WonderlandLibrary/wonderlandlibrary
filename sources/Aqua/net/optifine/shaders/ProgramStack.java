package net.optifine.shaders;

import java.util.ArrayDeque;
import java.util.Deque;
import net.optifine.shaders.Program;

public class ProgramStack {
    private Deque<Program> stack = new ArrayDeque();

    public void push(Program p) {
        this.stack.addLast((Object)p);
        if (this.stack.size() > 100) {
            throw new RuntimeException("Program stack overflow: " + this.stack.size());
        }
    }

    public Program pop() {
        if (this.stack.isEmpty()) {
            throw new RuntimeException("Program stack empty");
        }
        Program program = (Program)this.stack.pollLast();
        return program;
    }
}
