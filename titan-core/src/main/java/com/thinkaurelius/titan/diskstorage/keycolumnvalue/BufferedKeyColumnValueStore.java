package com.thinkaurelius.titan.diskstorage.keycolumnvalue;

import com.google.common.base.Preconditions;
import com.thinkaurelius.titan.diskstorage.StorageException;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Wraps a {@link KeyColumnValueStore} and buffers all mutations in a corresponding {@link BufferTransaction}.
 * The buffered mutations are flushed in batches to increase write performance.
 *
 * (c) Matthias Broecheler (me@matthiasb.com)
 */

public class BufferedKeyColumnValueStore implements KeyColumnValueStore {

    private final KeyColumnValueStore store;
    private final boolean bufferEnabled;

    public BufferedKeyColumnValueStore(KeyColumnValueStore store, boolean bufferEnabled) {
        Preconditions.checkNotNull(store);
        this.store = store;
        this.bufferEnabled = bufferEnabled;
    }

    private final StoreTransaction getTx(StoreTransaction txh) {
        assert txh instanceof BufferTransaction;
        return ((BufferTransaction) txh).getWrappedTransactionHandle();
    }

    @Override
    public boolean containsKey(ByteBuffer key, StoreTransaction txh) throws StorageException {
        return store.containsKey(key, getTx(txh));
    }

    @Override
    public List<Entry> getSlice(KeySliceQuery query, StoreTransaction txh) throws StorageException {
        return store.getSlice(query, getTx(txh));
    }

    @Override
    public void mutate(ByteBuffer key, List<Entry> additions, List<ByteBuffer> deletions, StoreTransaction txh) throws StorageException {
        if (bufferEnabled) {
            assert txh instanceof BufferTransaction;
            ((BufferTransaction) txh).mutate(store.getName(), key, additions, deletions);
        } else {
            store.mutate(key, additions, deletions, getTx(txh));
        }
    }

    @Override
    public void acquireLock(ByteBuffer key, ByteBuffer column, ByteBuffer expectedValue, StoreTransaction txh) throws StorageException {
        store.acquireLock(key, column, expectedValue, getTx(txh));
    }

    @Override
    public RecordIterator<ByteBuffer> getKeys(StoreTransaction txh) throws StorageException {
        return store.getKeys(getTx(txh));
    }

    @Override
    public ByteBuffer[] getLocalKeyPartition() throws StorageException {
        return store.getLocalKeyPartition();
    }


    @Override
    public String getName() {
        return store.getName();
    }

    @Override
    public void close() throws StorageException {
        store.close();
    }
}
