package io.deephaven.engine.rowset.impl.rsp.container;

/**
 * Results comprised of multiple, ordered ranges are provided via this interface.
 */
public interface RangeConsumer {
    /**
     * Deliver a single range. Methods receiving a RangeConsumer should call accept on it for for non-empty, disjoint
     * ranges. Calls should be made in increasing order of values contained in the ranges.
     *
     * @param begin first value of the range to add.
     * @param end one past the last value in the range to add (ie, end is exclusive).
     */
    void accept(int begin, int end);
}
