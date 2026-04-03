package ru.yandex.practicum.gym.coaches;

import java.util.Objects;

/**
 * Объект с неизменяемым значением, который однозначно идентифицирует тренера по его полному имени.
 */
public final class Coach {

    private final String surname;
    private final String name;
    private final String middleName;

    /**
     * @param surname    фамилия тренера (не {@code null})
     * @param name       имя тренера (не {@code null})
     * @param middleName отчество тренера (не {@code null})
     */
    public Coach(String surname, String name, String middleName) {
        this.surname = Objects.requireNonNull(surname, "surname");
        this.name = Objects.requireNonNull(name, "name");
        this.middleName = Objects.requireNonNull(middleName, "middleName");
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coach coach)) return false;
        return surname.equals(coach.surname)
                && name.equals(coach.name)
                && middleName.equals(coach.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, middleName);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                '}';
    }
}
