/*
 * ---------------------------------------------------------------------------------------------------------------------
 * AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY - for any changes edit ShortChunkedNumericalStats and regenerate
 * ---------------------------------------------------------------------------------------------------------------------
 */
package io.deephaven.server.table.stats;

import io.deephaven.chunk.LongChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.table.ChunkSource;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.Table;
import io.deephaven.engine.util.TableTools;
import io.deephaven.util.QueryConstants;

public class LongChunkedNumericalStats implements ChunkedNumericalStatsKernel<Long> {
    private long count = 0;

    private long sum = 0;
    private boolean useFloatingSum = false;
    private double floatingSum = .0;

    private long absSum = 0;
    private boolean useFloatingAbsSum = false;
    private double floatingAbsSum = .0;

    private long sqrdSum = 0;
    private boolean useFloatingSqrdSum = false;
    private double floatingSqrdSum = .0;

    private long min = QueryConstants.NULL_LONG;
    private long max = QueryConstants.NULL_LONG;
    private long absMin = QueryConstants.NULL_LONG;
    private long absMax = QueryConstants.NULL_LONG;

    @Override
    public Table processChunks(final RowSet index, final ColumnSource<?> columnSource, boolean usePrev) {

        try (final ChunkSource.GetContext getContext = columnSource.makeGetContext(CHUNK_SIZE)) {
            final RowSequence.Iterator okIt = index.getRowSequenceIterator();

            while (okIt.hasMore()) {
                final RowSequence nextKeys = okIt.getNextRowSequenceThrough(CHUNK_SIZE);
                final LongChunk<? extends Values> chunk = (usePrev ? columnSource.getPrevChunk(getContext, nextKeys)
                        : columnSource.getChunk(getContext, nextKeys)).asLongChunk();

                /*
                 * we'll use these to get as big as we can before adding into a potentially MUCH larger "total" in an
                 * attempt to reduce cumulative loss-of-precision error brought on by floating-point math; - but ONLY if
                 * we've overflowed our non-floating-point (long)
                 */
                double chunkedOverflowSum = .0;
                double chunkedOverflowAbsSum = .0;
                double chunkedOverflowSqrdSum = .0;

                final int chunkSize = chunk.size();
                for (int ii = 0; ii < chunkSize; ii++) {
                    final long val = chunk.get(ii);

                    if (val == QueryConstants.NULL_LONG) {
                        continue;
                    }

                    final long absVal = (long) Math.abs(val);

                    if (count == 0) {
                        min = max = val;
                        absMax = absMin = absVal;
                    } else {
                        if (val < min) {
                            min = val;
                        }

                        if (val > max) {
                            max = val;
                        }

                        if (absVal < absMin) {
                            absMin = absVal;
                        }

                        if (absVal > absMax) {
                            absMax = absVal;
                        }
                    }

                    count++;

                    if (!useFloatingSum) {
                        try {
                            sum = Math.addExact(sum, val);
                        } catch (final ArithmeticException ae) {
                            useFloatingSum = true;
                            floatingSum = sum;
                            chunkedOverflowSum = val;
                        }
                    } else {
                        chunkedOverflowSum += val;
                    }

                    if (!useFloatingAbsSum) {
                        try {
                            absSum = Math.addExact(absSum, absVal);
                        } catch (final ArithmeticException ae) {
                            useFloatingAbsSum = true;
                            floatingAbsSum = absSum;
                            chunkedOverflowAbsSum = absVal;
                        }
                    } else {
                        chunkedOverflowAbsSum += absVal;
                    }

                    if (!useFloatingSqrdSum) {
                        try {
                            sqrdSum = Math.addExact(sqrdSum, Math.multiplyExact(val, val));
                        } catch (final ArithmeticException ae) {
                            useFloatingSqrdSum = true;
                            floatingSqrdSum = sqrdSum;
                            chunkedOverflowSqrdSum = Math.pow(val, 2);
                        }

                    } else {
                        chunkedOverflowSqrdSum += Math.pow(val, 2);
                    }
                }

                if (useFloatingSum) {
                    floatingSum += chunkedOverflowSum;
                }

                if (useFloatingAbsSum) {
                    floatingAbsSum += chunkedOverflowAbsSum;
                }

                if (useFloatingSqrdSum) {
                    floatingSqrdSum += chunkedOverflowSqrdSum;
                }
            }
        }

        double avg = ChunkedNumericalStatsKernel.avg(count, sum);
        return TableTools.newTable(
                TableTools.longCol("Count", count),
                TableTools.longCol("Size", index.size()),
                useFloatingSum ? TableTools.doubleCol("Sum", floatingSum) : TableTools.longCol("Sum", sum),
                useFloatingAbsSum ? TableTools.doubleCol("AbsSum", floatingAbsSum)
                        : TableTools.longCol("AbsSum", absSum),
                useFloatingSqrdSum ? TableTools.doubleCol("SqrdSum", floatingSqrdSum)
                        : TableTools.longCol("SqrdSum", sqrdSum),
                TableTools.longCol("Min", min),
                TableTools.longCol("Max", max),
                TableTools.longCol("AbsMin", absMin),
                TableTools.longCol("AbsMax", absMax),
                TableTools.doubleCol("Avg", avg),
                TableTools.doubleCol("AbsAvg", ChunkedNumericalStatsKernel.avg(count, absSum)),
                TableTools.doubleCol("StdDev", ChunkedNumericalStatsKernel.stdDev(count, avg, sqrdSum)));

    }
}
