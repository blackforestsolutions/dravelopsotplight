package org.opentripplanner.routing.core;

import java.util.Currency;

/**
 * A Bean wrapper class for java.util.Currency 
 * @author novalis
 *
 */
public class WrappedCurrency {
    private Currency value;

    public WrappedCurrency(Currency value) {
        this.value = value;
    }

    /**
     * FOR TESTING
     * @param name
     */
    public WrappedCurrency(String name) {
        value = Currency.getInstance(name);
    }

    public int getDefaultFractionDigits() {
        return value.getDefaultFractionDigits();
    }

    public String toString() {
        return value.toString();
    }
    
    public boolean equals(Object o) {
        if (o instanceof WrappedCurrency) {
            WrappedCurrency c = (WrappedCurrency) o;
            return value.equals(c.value);
        }
        return false;
    }
    
    public Currency getCurrency() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
