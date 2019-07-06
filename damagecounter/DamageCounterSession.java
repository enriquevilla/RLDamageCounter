package net.runelite.client.plugins.damagecounter;

import java.time.Instant;

public class DamageCounterSession
{
    private Instant lastHit;

    public void setLastHit()
    {
        lastHit = Instant.now();
    }

    public Instant getLastHit()
    {
        return lastHit;
    }
}
