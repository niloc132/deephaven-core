/* ---------------------------------------------------------------------------------------------------------------------
 * AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY - for any changes edit CharSsmBackedSource and regenerate
 * ------------------------------------------------------------------------------------------------------------------ */
package io.deephaven.engine.v2.by.ssmcountdistinct;

import io.deephaven.engine.tables.dbarrays.FloatVector;
import io.deephaven.engine.v2.sources.AbstractColumnSource;
import io.deephaven.engine.v2.sources.ColumnSourceGetDefaults;
import io.deephaven.engine.v2.sources.MutableColumnSourceGetDefaults;
import io.deephaven.engine.v2.sources.ObjectArraySource;
import io.deephaven.engine.v2.ssms.FloatSegmentedSortedMultiset;
import io.deephaven.engine.v2.utils.RowSet;

/**
 * A {@link SsmBackedColumnSource} for Floats.
 */
public class FloatSsmBackedSource extends AbstractColumnSource<FloatVector>
                                 implements ColumnSourceGetDefaults.ForObject<FloatVector>,
                                            MutableColumnSourceGetDefaults.ForObject<FloatVector>,
                                            SsmBackedColumnSource<FloatSegmentedSortedMultiset, FloatVector> {
    private final ObjectArraySource<FloatSegmentedSortedMultiset> underlying;
    private boolean trackingPrevious = false;

    //region Constructor
    public FloatSsmBackedSource() {
        super(FloatVector.class, float.class);
        underlying = new ObjectArraySource<>(FloatSegmentedSortedMultiset.class, float.class);
    }
    //endregion Constructor

    //region SsmBackedColumnSource
    @Override
    public FloatSegmentedSortedMultiset getOrCreate(long key) {
        FloatSegmentedSortedMultiset ssm = underlying.getUnsafe(key);
        if(ssm == null) {
            //region CreateNew
            underlying.set(key, ssm = new FloatSegmentedSortedMultiset(DistinctOperatorFactory.NODE_SIZE));
            //endregion CreateNew
        }
        ssm.setTrackDeltas(trackingPrevious);
        return ssm;
    }

    @Override
    public FloatSegmentedSortedMultiset getCurrentSsm(long key) {
        return underlying.getUnsafe(key);
    }

    @Override
    public void clear(long key) {
        underlying.set(key, null);
    }

    @Override
    public void ensureCapacity(long capacity) {
        underlying.ensureCapacity(capacity);
    }

    @Override
    public ObjectArraySource<FloatSegmentedSortedMultiset> getUnderlyingSource() {
        return underlying;
    }
    //endregion

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public FloatVector get(long index) {
        return underlying.get(index);
    }

    @Override
    public FloatVector getPrev(long index) {
        final FloatSegmentedSortedMultiset maybePrev = underlying.getPrev(index);
        return maybePrev == null ? null : maybePrev.getPrevValues();
    }

    @Override
    public void startTrackingPrevValues() {
        trackingPrevious = true;
        underlying.startTrackingPrevValues();
    }

    @Override
    public void clearDeltas(RowSet indices) {
        indices.iterator().forEachLong(key -> {
            final FloatSegmentedSortedMultiset ssm = getCurrentSsm(key);
            if(ssm != null) {
                ssm.clearDeltas();
            }
            return true;
        });
    }
}
