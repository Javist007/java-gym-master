package ru.yandex.practicum.gym;

import ru.yandex.practicum.gym.coaches.Coach;
import ru.yandex.practicum.gym.coaches.CounterOfTrainings;
import ru.yandex.practicum.gym.enums.DayOfWeek;

import java.util.*;

/**
 * Хранилище всех тренировочных сеансов.
 *
 * <p>Внутри используется {@code EnumMap<DayOfWeek,…>} для быстрого поиска по дню недели и
 * {@link NavigableMap} (реализация TreeMap) для каждого дня, чтобы занятия были отсортированы
 * по времени начала.
 */
public class Timetable {

    /** Карта «день недели → (время начала → список сеансов)» – хранит занятия по дням и времени. */
    private final Map<DayOfWeek, NavigableMap<TimeOfDay, List<TrainingSession>>> timetable =
            new EnumMap<>(DayOfWeek.class);

    /** Счётчик количества сессий для каждого тренера за неделю. */
    private final Map<Coach, Integer> coachesCounter = new HashMap<>();

    /**
     * Добавляет новую тренировочную сессию в расписание.
     *
     * @param trainingSession сеанс, который необходимо добавить; не может быть {@code null}
     * @throws NullPointerException если {@code trainingSession} равен {@code null}
     */
    public void addNewTrainingSession(TrainingSession trainingSession) {
        Objects.requireNonNull(trainingSession, "Тренировочная сессия не может быть null");

        timetable.computeIfAbsent(
                        trainingSession.getDayOfWeek(),
                        k -> new TreeMap<>())
                .computeIfAbsent(
                        trainingSession.getTimeOfDay(),
                        k -> new ArrayList<>())
                .add(trainingSession);

        Coach coach = trainingSession.getCoach();
        coachesCounter.merge(coach, 1, Integer::sum);
    }

    /**
     * Возвращает все занятия, запланированные на указанный день недели,
     * отсортированные по времени начала.
     *
     * @param day день недели; не может быть {@code null}
     * @return неизменяемый набор занятий для указанного дня;
     */
    public Collection<TrainingSession> getTrainingSessionsForDay(DayOfWeek day) {
        NavigableMap<TimeOfDay, List<TrainingSession>> slotMap = timetable.get(day);
        if (slotMap == null) return Collections.emptyList();

        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> list : slotMap.values()) {
            result.addAll(list);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Возвращает все занятия, которые начинаются в указанный день недели и время.
     *
     * @param day  день недели; не может быть {@code null}
     * @param time точное время начала; не может быть {@code null}
     */
    public Collection<TrainingSession> getTrainingSessionsForDayAndTime(
            DayOfWeek day, TimeOfDay time) {

        NavigableMap<TimeOfDay, List<TrainingSession>> slotMap = timetable.get(day);
        if (slotMap == null) return Collections.emptyList();

        List<TrainingSession> list = slotMap.get(time);
        return list != null ? Collections.unmodifiableList(list)
                : Collections.emptyList();
    }

    /**
     * Подсчитывает, сколько тренировок каждую неделю проводит каждый тренер,
     * и возвращает список объектов {@link CounterOfTrainings},
     * отсортированный по убыванию количества занятий.
     *
     * @return неизменяемый список «тренер – количество»;
     *         никогда не {@code null}
     */
    public List<CounterOfTrainings> getCountByCoaches() {
        return coachesCounter.entrySet().stream()
                .map(e -> new CounterOfTrainings(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(CounterOfTrainings::getTrainingCounter)
                        .reversed())
                .toList();
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "timetable=" + timetable +
                '}';
    }
}
