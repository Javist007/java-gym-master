package ru.yandex.practicum.gym;

import java.util.Objects;

/**
 * Представляет время суток (часы 0–23, минуты 0–59).
 */
public final class TimeOfDay implements Comparable<TimeOfDay> {

    /**
     * Часы в диапазоне [0;23].
     */
    private final int hours;

    /**
     * Минуты в диапазоне [0;59].
     */
    private final int minutes;

    /**
     * Создаёт объект времени.
     *
     * @param hours   часы, 0 ≤ hours ≤ 23
     * @param minutes минуты, 0 ≤ minutes ≤ 59
     * @throws IllegalArgumentException если значения вне диапазона
     */
    public TimeOfDay(int hours, int minutes) {
        if (hours < 0 || hours > 23)
            throw new IllegalArgumentException("Часы должны быть в диапазоне от 0 до 23");
        if (minutes < 0 || minutes > 59)
            throw new IllegalArgumentException("Минуты должны быть в диапазоне от 0 до 59");

        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public int compareTo(TimeOfDay other) {
        int cmp = Integer.compare(this.hours, other.hours);
        return (cmp != 0) ? cmp : Integer.compare(this.minutes, other.minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeOfDay that)) return false;
        return hours == that.hours && minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, minutes);
    }
}
