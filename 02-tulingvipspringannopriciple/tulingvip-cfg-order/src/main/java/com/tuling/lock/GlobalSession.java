package com.tuling.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by smlz on 2019/12/12.
 */
public class GlobalSession {

    private GlobalSessionLock globalSessionLock = new GlobalSessionLock();

    public GlobalSessionLock getGlobalSessionLock() {
        return globalSessionLock;
    }

    public void setGlobalSessionLock(GlobalSessionLock globalSessionLock) {
        this.globalSessionLock = globalSessionLock;
    }

    private static class GlobalSessionLock {

        private Lock globalSessionLock = new ReentrantLock();

        public Lock getGlobalSessionLock() {
            return globalSessionLock;
        }

        public void setGlobalSessionLock(Lock globalSessionLock) {
            this.globalSessionLock = globalSessionLock;
        }

        public void lock() {
            try {
                if (globalSessionLock.tryLock(100000, TimeUnit.MILLISECONDS)) {
                    return;
                }
            } catch (InterruptedException e) {
                System.out.println("获取锁失败");
            }
            throw new RuntimeException("Lock global session failed");
        }

        public void unlock() {
            globalSessionLock.unlock();
        }
    }

    public void lock() {
        globalSessionLock.lock();
    }

    public void unlock() {
        globalSessionLock.unlock();
    }
}
