package com.sun.javafx.scene.input;

import javafx.scene.input.InputMethodRequests;

public interface ExtendedInputMethodRequests extends InputMethodRequests {
   int getInsertPositionOffset();

   String getCommittedText(int var1, int var2);

   int getCommittedTextLength();
}
