package su.nightexpress.lootconomy.booster.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.TimeUtil;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class BoosterSchedule implements Writeable {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Map<DayOfWeek, LocalTime> dayTimes;
    private final double                    multiplier;
    private final int duration;

    public BoosterSchedule(@NotNull Map<DayOfWeek, LocalTime> dayTimes, double multiplier, int duration) {
        this.dayTimes = dayTimes;
        this.multiplier = multiplier;
        this.duration = duration;
    }

    @NotNull
    public static BoosterSchedule read(@NotNull FileConfig config, @NotNull String path) {
        Map<DayOfWeek, LocalTime> dayTimes = new HashMap<>();
        for (String dayName : config.getSection(path + ".Start_Times")) {
            DayOfWeek day = StringUtil.getEnum(dayName, DayOfWeek.class).orElse(null);
            if (day == null) continue;

            String raw = config.getString(path + ".Start_Times." + dayName);
            if (raw == null) continue;

            try {
                dayTimes.put(day, LocalTime.parse(raw, TIME_FORMATTER).truncatedTo(ChronoUnit.MINUTES));
            }
            catch (DateTimeParseException ignored) {}
        }

        double multiplier = config.getDouble(path + ".Multiplier");
        int duration = config.getInt(path + ".Duration");

        return new BoosterSchedule(dayTimes, multiplier, duration);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        this.dayTimes.forEach((day, times) -> {
            config.set(path + ".Start_Times." + day.name(), times.format(TIME_FORMATTER));
        });
        config.set(path + ".Multiplier", this.multiplier);
        config.set(path + ".Duration", this.duration);
    }

    @NotNull
    public Booster createBooster(boolean relative) {
        return Booster.create(this.multiplier, relative ? this.calculateDuration() : this.duration);
    }

    public int calculateDuration() {
        DayOfWeek day = TimeUtil.getCurrentDate().getDayOfWeek();
        LocalTime start = this.dayTimes.get(day);
        if (start == null) return 0;

        LocalTime end = start.plusSeconds(this.duration);
        LocalTime current = TimeUtil.getCurrentTime().truncatedTo(ChronoUnit.MINUTES);
        if (current.isBefore(start) || current.isAfter(end)) return 0;

        Duration duration = Duration.between(current, end);
        return (int) duration.toSeconds();
    }

    public boolean isReady() {
        return this.calculateDuration() > 0;
    }

    @NotNull
    public Map<DayOfWeek, LocalTime> getDayTimes() {
        return this.dayTimes;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public int getDuration() {
        return this.duration;
    }
}
