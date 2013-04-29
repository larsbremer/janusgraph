package com.thinkaurelius.titan.diskstorage.keycolumnvalue.keyvalue;

import com.thinkaurelius.titan.diskstorage.StorageException;
import com.thinkaurelius.titan.diskstorage.keycolumnvalue.RecordIterator;
import com.thinkaurelius.titan.diskstorage.keycolumnvalue.StoreTransaction;

import java.nio.ByteBuffer;

/**
 * Interface for a data store that represents data in the simple key->value data model where each key is uniquely
 * associated with a value. Keys and values are generic ByteBuffers.
 *
 * @author Matthias Br&ouml;cheler (me@matthiasb.com);
 */
public interface KeyValueStore {

    /**
     * Inserts the given key-value pair into the store. If the key already exists, its value is overwritten by the given one.
     *
     * @param key
     * @param value
     * @param txh
     * @throws com.thinkaurelius.titan.diskstorage.StorageException
     */
    public void insert(ByteBuffer key, ByteBuffer value, StoreTransaction txh) throws StorageException;

    /**
     * Deletes the given key from the store.
     *
     * @param key
     * @param txh
     * @throws StorageException
     */
    public void delete(ByteBuffer key, StoreTransaction txh) throws StorageException;

    /**
     * Returns the value associated with the given key.
     *
     * @param key
     * @param txh
     * @return
     * @throws StorageException
     */
    public ByteBuffer get(ByteBuffer key, StoreTransaction txh) throws StorageException;

    /**
     * Returns true iff the store contains the given key, else false
     *
     * @param key
     * @param txh
     * @return
     * @throws StorageException
     */
    public boolean containsKey(ByteBuffer key, StoreTransaction txh) throws StorageException;

    /**
     * Returns an iterator over all keys in this store. The keys may be
     * ordered but not necessarily.
     *
     * @return An iterator over all keys in this store.
     */
    public RecordIterator<ByteBuffer> getKeys(StoreTransaction txh) throws StorageException;

    /**
     * Acquires a lock for the given key and expected value (null, if not value is expected).
     *
     * @param key
     * @param expectedValue
     * @param txh
     * @throws StorageException
     */
    public void acquireLock(ByteBuffer key, ByteBuffer expectedValue, StoreTransaction txh) throws StorageException;

    /**
     * Returns the range of keys that are stored locally.
     *
     * This is an optional operation and only makes sense in the context of distributed stores.
     *
     * @return
     * @throws StorageException
     */
    public ByteBuffer[] getLocalKeyPartition() throws StorageException;

    /**
     * Returns the name of this store
     *
     * @return
     */
    public String getName();

    /**
     * Closes this store and releases its resources.
     *
     * @throws StorageException
     */
    public void close() throws StorageException;

}
