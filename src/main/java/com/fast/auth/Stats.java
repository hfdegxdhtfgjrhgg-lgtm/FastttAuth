package com.fast.auth;

import java.util.concurrent.atomic.AtomicLong;

public final class Stats {

    private final AtomicLong registers = new AtomicLong(0);
    private final AtomicLong logins = new AtomicLong(0);
    private final AtomicLong logout = new AtomicLong(0);
    private final AtomicLong changePassword = new AtomicLong(0);
    private final AtomicLong unregisters = new AtomicLong(0);
    private final AtomicLong failedLogins = new AtomicLong(0);

    public void incRegisters() { registers.incrementAndGet(); }
    public void incLogins() { logins.incrementAndGet(); }
    public void incLogout() { logout.incrementAndGet(); }
    public void incChangePassword() { changePassword.incrementAndGet(); }
    public void incUnregisters() { unregisters.incrementAndGet(); }
    public void incFailedLogins() { failedLogins.incrementAndGet(); }

    public long getRegisters() { return registers.get(); }
    public long getLogins() { return logins.get(); }
    public long getLogout() { return logout.get(); }
    public long getChangePassword() { return changePassword.get(); }
    public long getUnregisters() { return unregisters.get(); }
    public long getFailedLogins() { return failedLogins.get(); }
}
