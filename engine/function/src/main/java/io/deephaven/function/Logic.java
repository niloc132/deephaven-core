/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.function;

import io.deephaven.vector.BooleanVector;

/**
 * Logic functions.
 */
public class Logic {

    /**
     * Ands all of the values in the array together.
     *
     * @param values values.
     * @return logical and of all the values in the array. By convention, returns true if the array is empty.
     */
    static public Boolean and(Boolean... values) {
        for (Boolean b : values) {
            if (!b) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ands all of the values in the array together.
     *
     * @param values values.
     * @return logical and of all the values in the array. By convention, returns true if the array is empty.
     */
    static public Boolean and(boolean... values) {
        for (Boolean b : values) {
            if (!b) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ands all of the values in the array together.
     *
     * @param values values.
     * @return logical and of all the values in the array. By convention, returns true if the array is empty.
     */
    static public Boolean and(BooleanVector values) {
        for (int ii = 0; ii < values.size(); ++ii) {
            Boolean b = values.get(ii);
            if (!b) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ands all of the values in the array together.
     *
     * @param values values.
     * @param nullValue value to use in place of null values.
     * @return logical and of all the values in the array. By convention, returns true if the array is empty.
     */
    static public Boolean and(Boolean[] values, Boolean nullValue) {
        for (Boolean b : values) {
            b = b == null ? nullValue : b;

            if (!b) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ands all of the values in the array together.
     *
     * @param values values.
     * @param nullValue value to use in place of null values.
     * @return logical and of all the values in the array. By convention, returns true if the array is empty.
     */
    static public Boolean and(BooleanVector values, Boolean nullValue) {
        for (int ii = 0; ii < values.size(); ++ii) {
            Boolean b = values.get(ii);
            b = b == null ? nullValue : b;

            if (!b) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ors all of the values in the array together.
     *
     * @param values values.
     * @return logical or of all the values in the array. By convention, returns false if the array is empty.
     */
    static public Boolean or(Boolean... values) {
        for (Boolean b : values) {
            if (b) {
                return true;
            }
        }

        return false;
    }

    /**
     * Ors all of the values in the array together.
     *
     * @param values values.
     * @return logical or of all the values in the array. By convention, returns false if the array is empty.
     */
    static public Boolean or(boolean... values) {
        for (Boolean b : values) {
            if (b) {
                return true;
            }
        }

        return false;
    }

    /**
     * Ors all of the values in the array together.
     *
     * @param values values.
     * @param nullValue value to use in place of null values.
     * @return logical or of all the values in the array. By convention, returns false if the array is empty.
     */
    static public Boolean or(Boolean[] values, Boolean nullValue) {
        for (Boolean b : values) {
            b = b == null ? nullValue : b;

            if (b) {
                return true;
            }
        }

        return false;
    }

    /**
     * Not of all values in an array.
     *
     * @param values values.
     * @return logical not of all the values in the array.
     */
    static public Boolean[] not(Boolean... values) {
        if (values == null) {
            return null;
        }

        Boolean[] result = new Boolean[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = values[i] == null ? null : !values[i];
        }

        return result;
    }

    /**
     * Not of all values in an array.
     *
     * @param values values.
     * @return logical not of all the values in the array.
     */
    static public Boolean[] not(boolean... values) {
        if (values == null) {
            return null;
        }

        Boolean[] result = new Boolean[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = !values[i];
        }

        return result;
    }

}
