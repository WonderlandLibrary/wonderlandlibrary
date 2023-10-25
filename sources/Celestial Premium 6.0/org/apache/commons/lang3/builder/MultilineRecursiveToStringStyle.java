/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class MultilineRecursiveToStringStyle
extends RecursiveToStringStyle {
    private static final long serialVersionUID = 1L;
    private int indent = 2;
    private int spaces = 2;

    public MultilineRecursiveToStringStyle() {
        this.resetIndent();
    }

    private void resetIndent() {
        this.setArrayStart("{" + SystemUtils.LINE_SEPARATOR + this.spacer(this.spaces));
        this.setArraySeparator("," + SystemUtils.LINE_SEPARATOR + this.spacer(this.spaces));
        this.setArrayEnd(SystemUtils.LINE_SEPARATOR + this.spacer(this.spaces - this.indent) + "}");
        this.setContentStart("[" + SystemUtils.LINE_SEPARATOR + this.spacer(this.spaces));
        this.setFieldSeparator("," + SystemUtils.LINE_SEPARATOR + this.spacer(this.spaces));
        this.setContentEnd(SystemUtils.LINE_SEPARATOR + this.spacer(this.spaces - this.indent) + "]");
    }

    private StringBuilder spacer(int spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; ++i) {
            sb.append(" ");
        }
        return sb;
    }

    @Override
    public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (!ClassUtils.isPrimitiveWrapper(value.getClass()) && !String.class.equals(value.getClass()) && this.accept(value.getClass())) {
            this.spaces += this.indent;
            this.resetIndent();
            buffer.append(ReflectionToStringBuilder.toString(value, this));
            this.spaces -= this.indent;
            this.resetIndent();
        } else {
            super.appendDetail(buffer, fieldName, value);
        }
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void reflectionAppendArrayDetail(StringBuffer buffer, String fieldName, Object array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, long[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, int[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, short[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, char[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, double[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, float[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, boolean[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
}

