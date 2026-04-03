package ru.yandex.practicum.gym.coaches;

import java.util.Objects;

/**
 * Неизменяемый объект‑DTO, связывающий конкретного тренера с количеством тренировок,
 * которые он проводит в течение недели.
 *
 * <p>Все поля объявлены {@code final}, и класс не содержит методов‑мутаций,
 * поэтому экземпляр безопасен для использования в многопоточной среде.</p>
 */
public class CounterOfTrainings {

    /** Тренер, к которому относится счётчик. */
    private final Coach coach;

    /** Количество тренировок данного тренера (неотрицательное целое). */
    private final int trainingCounter;

    /**
     * Создаёт новый объект‑счётчик.
     *
     * @param coach          тренер, чьи занятия считаются; {@code null} запрещено
     * @param trainingCount количество тренировок; должно быть ≥ 0
     * @throws NullPointerException     если передан {@code null}
     * @throws IllegalArgumentException если {@code trainingCount} < 0
     */
    public CounterOfTrainings(Coach coach, int trainingCount) {
        this.coach = Objects.requireNonNull(coach,
                "Тренер не должен быть пустым");
        if (trainingCount < 0) {
            throw new IllegalArgumentException(
                    "Количество тренировок не может быть отрицательным");
        }
        this.trainingCounter = trainingCount;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getTrainingCounter() {
        return trainingCounter;
    }

    @Override
    public String toString() {
        return "CounterOfTrainings{" +
                "coach=" + coach.getSurname() + ' ' + coach.getName() +
                ", count=" + trainingCounter +
                '}';
    }
}

