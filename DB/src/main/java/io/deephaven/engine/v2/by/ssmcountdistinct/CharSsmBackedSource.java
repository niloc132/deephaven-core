package io.deephaven.engine.v2.by.ssmcountdistinct;

import io.deephaven.engine.tables.dbarrays.CharVector;
import io.deephaven.engine.v2.sources.AbstractColumnSource;
import io.deephaven.engine.v2.sources.ColumnSourceGetDefaults;
import io.deephaven.engine.v2.sources.MutableColumnSourceGetDefaults;
import io.deephaven.engine.v2.sources.ObjectArraySource;
import io.deephaven.engine.v2.ssms.CharSegmentedSortedMultiset;
import io.deephaven.engine.v2.utils.RowSet;

/**
 * A {@link SsmBackedColumnSource} for Characters.
 */
public class CharSsmBackedSource extends AbstractColumnSource<CharVector>
                                 implements ColumnSourceGetDefaults.ForObject<CharVector>,
                                            MutableColumnSourceGetDefaults.ForObject<CharVector>,
                                            SsmBackedColumnSource<CharSegmentedSortedMultiset, CharVector> {
    private final ObjectArraySource<CharSegmentedSortedMultiset> underlying;
    private boolean trackingPrevious = false;

    //region Constructor
    public CharSsmBackedSource() {
        super(CharVector.class, char.class);
        underlying = new ObjectArraySource<>(CharSegmentedSortedMultiset.class, char.class);
    }
    //endregion Constructor

    //region SsmBackedColumnSource
    @Override
    public CharSegmentedSortedMultiset getOrCreate(long key) {
        CharSegmentedSortedMultiset ssm = underlying.getUnsafe(key);
        if(ssm == null) {
            //region CreateNew
            underlying.set(key, ssm = new CharSegmentedSortedMultiset(DistinctOperatorFactory.NODE_SIZE));
            //endregion CreateNew
        }
        ssm.setTrackDeltas(trackingPrevious);
        return ssm;
    }

    @Override
    public CharSegmentedSortedMultiset getCurrentSsm(long key) {
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
    public ObjectArraySource<CharSegmentedSortedMultiset> getUnderlyingSource() {
        return underlying;
    }
    //endregion

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public CharVector get(long index) {
        return underlying.get(index);
    }

    @Override
    public CharVector getPrev(long index) {
        final CharSegmentedSortedMultiset maybePrev = underlying.getPrev(index);
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
            final CharSegmentedSortedMultiset ssm = getCurrentSsm(key);
            if(ssm != null) {
                ssm.clearDeltas();
            }
            return true;
        });
    }
}
