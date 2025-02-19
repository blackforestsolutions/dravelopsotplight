package org.opentripplanner.routing.api.request;


import java.io.Serializable;
import java.util.function.DoubleFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a factory for creating functions witch can be used as parameters in the
 * {@link RoutingRequest}. A function is used in the request to specify limits/thresholds. We
 * support linear function for now, but it should be easy to extend with other type of functions if
 * needed. Use the {@link #parse(String)} method to create new functions from a string.
 * <p>
 * The following "kind" of functions are supported:
 * <ul>
 * <li>Linear function - ({@link #createLinearFunction(double, double)})
 * </ul>
 */
public class RequestFunctions {
  private static final String SEP = "\\s*";
  private static final String NUM = "([\\d.,]+)";
  public static final String PLUS = Pattern.quote("+");
  private static final Pattern LINEAR_FUNCTION_PATTERN = Pattern.compile(NUM + SEP + PLUS + SEP + NUM + SEP + "[Xx]");


  /** This is private to prevent this utility class from instantiation. */
  private RequestFunctions() { /* empty */ }

  /**
   * Parse an input string representing a linear function on the format:
   * <p>
   * {@code a + b x}
   * <p>
   * where {@code a} is the constant and {@code b} is the coefficient in:
   *
   * @throws RuntimeException if the input is not parsable.
   */
  public static DoubleFunction<Double> parse(String text) {
    if(text == null || text.isBlank()) { return null; }

    // Try to match to a linear function
    Matcher m = LINEAR_FUNCTION_PATTERN.matcher(text);

    if(m.find()) {
      return createLinearFunction(
          Double.parseDouble(m.group(1)),
          Double.parseDouble(m.group(2))
      );
    }

    // No function matched
    throw new IllegalArgumentException("Unable to parse function: '" + text + "'");
  }


  /**
   * Create a linear function of the form: {@code y = f(x) = a + b * x}. It allows setting both a
   * constant 'a' and a coefficient 'b' and the use those in the computation of a limit. The input
   * value 'x' is normally the min/max value across the sample set.
   */
  public static DoubleFunction<Double> createLinearFunction(double constant, double coefficient) {
    return new LinearFunction(constant, coefficient);
  }

  private static class LinearFunction implements DoubleFunction<Double>, Serializable {
    // This class is package local to be unit testable.

    /** The constant part of the function. */
    private final double a;

    /** The coefficient part of the function. */
    private final double b;

    public LinearFunction(double constant, double coefficient) {
      this.a = constant;
      this.b = coefficient;
    }

    public double calculate(double x) {
      return a + b * x;
    }

    @Override
    public Double apply(double value) {
      return calculate(value);
    }

    @Override
    public String toString() {
      return "f(x) = " + a + " + " + b + " x";
    }
  }
}
