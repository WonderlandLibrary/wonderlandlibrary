package javafx.beans.binding;

import java.util.Locale;
import javafx.beans.value.ObservableNumberValue;

public interface NumberExpression extends ObservableNumberValue {
   NumberBinding negate();

   NumberBinding add(ObservableNumberValue var1);

   NumberBinding add(double var1);

   NumberBinding add(float var1);

   NumberBinding add(long var1);

   NumberBinding add(int var1);

   NumberBinding subtract(ObservableNumberValue var1);

   NumberBinding subtract(double var1);

   NumberBinding subtract(float var1);

   NumberBinding subtract(long var1);

   NumberBinding subtract(int var1);

   NumberBinding multiply(ObservableNumberValue var1);

   NumberBinding multiply(double var1);

   NumberBinding multiply(float var1);

   NumberBinding multiply(long var1);

   NumberBinding multiply(int var1);

   NumberBinding divide(ObservableNumberValue var1);

   NumberBinding divide(double var1);

   NumberBinding divide(float var1);

   NumberBinding divide(long var1);

   NumberBinding divide(int var1);

   BooleanBinding isEqualTo(ObservableNumberValue var1);

   BooleanBinding isEqualTo(ObservableNumberValue var1, double var2);

   BooleanBinding isEqualTo(double var1, double var3);

   BooleanBinding isEqualTo(float var1, double var2);

   BooleanBinding isEqualTo(long var1);

   BooleanBinding isEqualTo(long var1, double var3);

   BooleanBinding isEqualTo(int var1);

   BooleanBinding isEqualTo(int var1, double var2);

   BooleanBinding isNotEqualTo(ObservableNumberValue var1);

   BooleanBinding isNotEqualTo(ObservableNumberValue var1, double var2);

   BooleanBinding isNotEqualTo(double var1, double var3);

   BooleanBinding isNotEqualTo(float var1, double var2);

   BooleanBinding isNotEqualTo(long var1);

   BooleanBinding isNotEqualTo(long var1, double var3);

   BooleanBinding isNotEqualTo(int var1);

   BooleanBinding isNotEqualTo(int var1, double var2);

   BooleanBinding greaterThan(ObservableNumberValue var1);

   BooleanBinding greaterThan(double var1);

   BooleanBinding greaterThan(float var1);

   BooleanBinding greaterThan(long var1);

   BooleanBinding greaterThan(int var1);

   BooleanBinding lessThan(ObservableNumberValue var1);

   BooleanBinding lessThan(double var1);

   BooleanBinding lessThan(float var1);

   BooleanBinding lessThan(long var1);

   BooleanBinding lessThan(int var1);

   BooleanBinding greaterThanOrEqualTo(ObservableNumberValue var1);

   BooleanBinding greaterThanOrEqualTo(double var1);

   BooleanBinding greaterThanOrEqualTo(float var1);

   BooleanBinding greaterThanOrEqualTo(long var1);

   BooleanBinding greaterThanOrEqualTo(int var1);

   BooleanBinding lessThanOrEqualTo(ObservableNumberValue var1);

   BooleanBinding lessThanOrEqualTo(double var1);

   BooleanBinding lessThanOrEqualTo(float var1);

   BooleanBinding lessThanOrEqualTo(long var1);

   BooleanBinding lessThanOrEqualTo(int var1);

   StringBinding asString();

   StringBinding asString(String var1);

   StringBinding asString(Locale var1, String var2);
}
