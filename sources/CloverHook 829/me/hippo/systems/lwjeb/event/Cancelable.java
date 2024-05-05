/*
 * MIT License
 *
 * Copyright (c) 2018 Hippo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.hippo.systems.lwjeb.event;

/**
 * The {@link Cancelable}, use this for events you would like to cancel.
 *
 * @author Hippo
 * @since 9/29/2018
 */
public class Cancelable {

    /**
     * Weather to cancel the event or not.
     */
    private boolean cancel;

    /**
     * Gets if the event is cancelled.
     *
     * @return  If the event is cancelled.
     */
    public boolean isCancellled() {
        return cancel;
    }

    /**
     * Sets the cancel state to {@code cancel}.
     *
     * @param cancel  Weather to cancel the event.
     */
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }
}
