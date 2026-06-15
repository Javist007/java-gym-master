package ru.yandex.practicum.gym;

import ru.yandex.practicum.gym.coaches.Coach;
import ru.yandex.practicum.gym.enums.DayOfWeek;

import java.util.Objects;

/**
 * Одно конкретное занятие: группа + тренер + день недели + время начала.
 */
public final class TrainingSession {

    /**
     * Группа, для которой проводится занятие.  Не может быть {@code null}.
     */
    private final Group group;

    /**
     * Тренер, ведущий занятие.  Не может быть {@code null}.
     */
    private final Coach coach;

    /**
     * День недели, в который проводится занятие.  Не может быть {@code null}.
     */
    private final DayOfWeek dayOfWeek;

    /**
     * Время начала занятия (часы‑минуты).  Не может быть {@code null}.
     */
    private final TimeOfDay timeOfDay;

    /**
     * Создаёт сеанс с заданными параметрами.
     *
     * @param group     группа, не {@code null}
     * @param coach     тренер, не {@code null}
     * @param dayOfWeek день недели, не {@code null}
     * @param timeOfDay время начала, не {@code null}
     */
    public TrainingSession(Group group,
                           Coach coach,
                           DayOfWeek dayOfWeek,
                           TimeOfDay timeOfDay) {
        this.group = Objects.requireNonNull(group, "Группа не может быть null");
        this.coach = Objects.requireNonNull(coach, "Тренер не может быть null");
        this.dayOfWeek = Objects.requireNonNull(dayOfWeek, "День недели не может быть null");
        this.timeOfDay = Objects.requireNonNull(timeOfDay, "Время не может быть null");
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingSession that)) return false;
        return group.equals(that.group)
                && coach.equals(that.coach)
                && dayOfWeek == that.dayOfWeek
                && timeOfDay.equals(that.timeOfDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, coach, dayOfWeek, timeOfDay);
    }

    @Override
    public String toString() {
        return "TrainingSession{"
                + "group=" + group.getTitle()
                + ", coach=" + coach.getSurname() + ' ' + coach.getName()
                + ", day=" + dayOfWeek
                + ", time=" + timeOfDay
                + '}';
    }
}
