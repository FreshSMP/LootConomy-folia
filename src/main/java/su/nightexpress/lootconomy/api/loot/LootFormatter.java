package su.nightexpress.lootconomy.api.loot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LootFormatter<O> {

    @NotNull String getName(@NotNull O object);

    @NotNull String getLocalized(@NotNull O object);

    @Nullable O parseObject(@NotNull String name);
}
