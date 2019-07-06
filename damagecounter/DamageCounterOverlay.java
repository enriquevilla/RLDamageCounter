package net.runelite.client.plugins.damagecounter;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class DamageCounterOverlay extends Overlay
{
    private final DamageCounterPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();
    private final Client client;

    @Inject
    private DamageCounterOverlay(Client client, DamageCounterPlugin plugin)
    {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.BOTTOM_RIGHT);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();

        DamageCounterSession session = plugin.getSession();

        if (session == null)
        {
            return null;
        }

        if (plugin.getDamage() > 0)
        {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Damage Counter")
                    .build()
            );
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Damage: ")
                    .right(Integer.toString(plugin.getDamage()))
                    .build());
        }
        return panelComponent.render(graphics);
    }
}
