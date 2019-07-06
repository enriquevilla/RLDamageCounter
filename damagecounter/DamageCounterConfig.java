package net.runelite.client.plugins.damagecounter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("damagecounter")
public interface DamageCounterConfig extends Config
{
    @ConfigItem(
            position = 1,
            keyName = "saveDamage",
            name = "Save damage",
            description = "Saves damage if overlay hides"
    )

    default boolean saveDamage()
    {
        return true;
    }

    @ConfigItem(
            position = 2,
            keyName = "statTimeout",
            name = "Hides overlay (mins)",
            description = "Configures the time until the overlay is hidden"
    )

    default int statTimeout()
    {
        return 5;
    }


}