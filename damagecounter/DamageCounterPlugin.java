package net.runelite.client.plugins.damagecounter;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;


@Slf4j
@PluginDescriptor(
        name = "Damage Counter",
        description = "Count damage dealt",
        tags = {"damage", "counter"}
)

public class DamageCounterPlugin extends Plugin
{

    private int xpBefore;
    private int damage = 0;

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private DamageCounterOverlay myOverlay;

    @Inject
    private DamageCounterConfig config;

    @Getter
    private DamageCounterSession session;

    @Provides
    DamageCounterConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(DamageCounterConfig.class);
    }

    @Override
    public void startUp()
    {
        overlayManager.add(myOverlay);
    }

    @Override
    public void shutDown()
    {
        overlayManager.remove(myOverlay);
        session = null;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        Skill skill = Skill.HITPOINTS;
        xpBefore = client.getSkillExperience(skill);
    }

    @Subscribe
    public void onGameTick(GameTick gameTick)
    {
        Skill skill = Skill.HITPOINTS;
        xpBefore = client.getSkillExperience(skill);
        if (session == null || session.getLastHit() == null)
        {
            return;
        }

        Duration statTimeout = Duration.ofMinutes(config.statTimeout());
        Duration sinceHit = Duration.between(session.getLastHit(), Instant.now());

        if (sinceHit.compareTo(statTimeout) >= 0)
        {
            session = null;
            if (!config.saveDamage()) {
                damage = 0;
            }
        }
    }
    @Subscribe
    public void onExperienceChanged(ExperienceChanged event)
    {
        Skill skill = event.getSkill();
        int xpAfter;
        if (skill == Skill.HITPOINTS && client.getRealSkillLevel(skill) > 0)
        {
            xpAfter = client.getSkillExperience(skill);
            int xpDrop = xpAfter - xpBefore;
            damage += Math.round(xpDrop / 1.33);
            if (session == null)
            {
                session = new DamageCounterSession();
            }
            session.setLastHit();
        }
    }

    public int getDamage()
    {
        return damage;
    }
}
