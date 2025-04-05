package su.nightexpress.lootconomy.booster;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.economybridge.api.Currency;
import su.nightexpress.lootconomy.Placeholders;
import su.nightexpress.lootconomy.booster.impl.BoosterSchedule;
import su.nightexpress.lootconomy.config.Config;
import su.nightexpress.lootconomy.config.Lang;
import su.nightexpress.nightcore.util.NumberUtil;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class BoosterUtils {

    @NotNull
    public static Map<String, BoosterSchedule> getDefaultBoosterSchedules() {
        Map<String, BoosterSchedule> map = new HashMap<>();

        Map<DayOfWeek, LocalTime> dayTimes = new HashMap<>();
        LocalTime time = LocalTime.of(12, 0);
        dayTimes.put(DayOfWeek.SATURDAY, time);
        dayTimes.put(DayOfWeek.SUNDAY, time);

        map.put("weekend", new BoosterSchedule(dayTimes, 1.5, 3600 * 6));

        return map;
    }

    public static boolean isApplicable(@NotNull Currency currency) {
        return !Config.BOOSTER_EXCLUSIVE_CURRENCIES.get().contains(currency.getInternalId());
    }

    public static double getAsPercent(double multiplier) {
        return multiplier * 100D - 100D;
    }

    @NotNull
    public static String formatMultiplier(double multiplier) {
        return formatPercent(getAsPercent(multiplier));
    }

    @NotNull
    public static String formatPercent(double amount) {
        String format = (amount >= 0 ? Lang.BOOSTER_FORMAT_POSITIVE : Lang.BOOSTER_FORMAT_NEGATIVE).getString();
        return format.replace(Placeholders.GENERIC_AMOUNT, NumberUtil.format(amount));
    }
}
