package com.dropit.deliveriesmanagment.util;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockService {
    private static Map<String, Lock> idToLockMap = new HashMap<>();

    public static Lock getLock(String id) {
        synchronized (idToLockMap) {
            Lock lock = idToLockMap.get(id);
            if (lock == null) {
                lock = new ReentrantLock();
                idToLockMap.put(id, lock);
            }
            return lock;
        }
    }

    public static void removeLock(String id) {
        synchronized (idToLockMap) {
            idToLockMap.remove(id);

        }
    }
}
