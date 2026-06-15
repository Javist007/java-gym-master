package ru.yandex.practicum.gym;

import ru.yandex.practicum.gym.enums.Age;

import java.util.Objects;

/**
 * Описывает группу занятий: название, возрастная категория и длительность в минутах.
 */
public final class Group {

    /**
     * Название группы.  Не может быть {@code null} и не должно быть пустой строкой.
     */
    private final String title;

    /**
     * Возрастная категория (взрослый/детский).  Не может быть {@code null}.
     */
    private final Age age;

    /**
     * Длительность занятия в минутах.
     * <p>Положительное целое число – отрицательные значения считаются ошибкой.
     */
    private final int duration;

    /**
     * Создаёт группу с заданными параметрами.
     *
     * @param title    название группы, не {@code null} и непустое
     * @param age      возрастная категория, не {@code null}
     * @param duration длительность в минутах, должно быть >0
     */
    public Group(String title, Age age, int duration) {
        this.title = Objects.requireNonNull(title, "Название не может быть null");
        if (title.isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }

        this.age = Objects.requireNonNull(age, "Возраст не может быть null");

        if (duration <= 0) {
            throw new IllegalArgumentException("Продолжительность должна быть положительная");
        }
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public Age getAge() {
        return age;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group group)) return false;
        return duration == group.duration &&
                title.equals(group.title) &&
                age == group.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, age, duration);
    }

    @Override
    public String toString() {
        return "Group{" +
                "title='" + title + '\'' +
                ", age=" + age +
                ", duration=" + duration + "min" +
                '}';
    }
}
