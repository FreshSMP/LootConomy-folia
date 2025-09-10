package su.nightexpress.lootconomy.booster.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.lootconomy.LootConomyPlugin;
import su.nightexpress.lootconomy.booster.BoosterManager;
import su.nightexpress.nightcore.manager.AbstractListener;

public class BoosterListener extends AbstractListener<LootConomyPlugin> {

    public BoosterListener(@NotNull LootConomyPlugin plugin, @NotNull BoosterManager boosterManager) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBoosterJoin(PlayerJoinEvent event) {

    }
}
