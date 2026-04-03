package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.gym.coaches.Coach;
import ru.yandex.practicum.gym.coaches.CounterOfTrainings;
import ru.yandex.practicum.gym.enums.Age;
import ru.yandex.practicum.gym.enums.DayOfWeek;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит‑тесты для {@link Timetable}.
 */
class TimetableTest {

    /**
     * Проверяем, добавление сеанса в указанный день через getTrainingSessionsForDayAndTime.
     */
    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // за понедельник – одна запись
        NavigableMap<TimeOfDay, List<TrainingSession>> mondayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondayMap.size());
        TimeOfDay key = new TimeOfDay(13, 0);
        assertTrue(mondayMap.containsKey(key));
        assertEquals(singleTrainingSession,
                mondayMap.get(key).getFirst());

        // за вторник – пустая карта
        NavigableMap<TimeOfDay, List<TrainingSession>> tuesdayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdayMap.isEmpty());
    }

    /**
     * Проверяем, добавление сеанса в указанное время через getTrainingSessionsForDayAndTime.
     */
    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 15));

        timetable.addNewTrainingSession(singleTrainingSession);

        // за понедельник в 13:00 – одно занятие
        Collection<TrainingSession> at1315 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                        new TimeOfDay(13, 15));
        assertEquals(1, at1315.size());
        assertTrue(at1315.contains(singleTrainingSession));

        // за понедельник в 14:00 – нет занятий
        Collection<TrainingSession> at1415 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                        new TimeOfDay(14, 0));
        assertTrue(at1415.isEmpty());
    }

    /**
     * Комплексная проверка на добавление занятий в разные дни и в один день несколько.
     * Проверяется сортировка занятий по времени.
     */
    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // понедельник
        NavigableMap<TimeOfDay, List<TrainingSession>> mondayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondayMap.size());
        assertTrue(mondayMap.containsKey(new TimeOfDay(13, 0)));
        assertEquals(mondayChildTrainingSession,
                mondayMap.get(new TimeOfDay(13, 0)).getFirst());

        // четверг – два ключа
        NavigableMap<TimeOfDay, List<TrainingSession>> thursdayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdayMap.size());
        assertTrue(thursdayMap.containsKey(new TimeOfDay(13, 0)));
        assertTrue(thursdayMap.containsKey(new TimeOfDay(20, 0)));

        // проверяем порядок ключей (сортировка по времени)
        Iterator<TimeOfDay> it = thursdayMap.keySet().iterator();
        assertEquals(new TimeOfDay(13, 0), it.next());
        assertEquals(new TimeOfDay(20, 0), it.next());

        // вторник – пустая карта
        NavigableMap<TimeOfDay, List<TrainingSession>> tuesdayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdayMap.isEmpty());
    }

    /**
     * Проверяем, что при добавлении нескольких сеансов в один и тот же слот
     * они оба попадают в коллекцию и доступны через getTrainingSessionsForDayAndTime.
     */
    @Test
    void testGetTrainingSessionsForDayMultipleSameTime() {
        Timetable timetable = new Timetable();

        Group group1 = new Group("Группа 1", Age.CHILD, 60);
        Group group2 = new Group("Группа 2", Age.ADULT, 90);
        Coach coach = new Coach("Иванов", "Петр", "Петрович");
        TimeOfDay time = new TimeOfDay(10, 0);

        TrainingSession ts1 = new TrainingSession(group1, coach,
                DayOfWeek.WEDNESDAY, time);
        TrainingSession ts2 = new TrainingSession(group2, coach,
                DayOfWeek.WEDNESDAY, time);

        timetable.addNewTrainingSession(ts1);
        timetable.addNewTrainingSession(ts2);

        /* --- проверяем карту за день (два ключа → два списка) --- */
        NavigableMap<TimeOfDay, List<TrainingSession>> weekDayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);
        assertEquals(1, weekDayMap.size());          // только один ключ – 10:00
        List<TrainingSession> list = weekDayMap.get(time);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertTrue(list.contains(ts1));
        assertTrue(list.contains(ts2));

        /* --- проверяем отдельный слот (как раньше) --- */
        Collection<TrainingSession> slot =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.WEDNESDAY, time);
        assertEquals(2, slot.size());
    }

    /**
     * Проверяем, что addNewTrainingSession(null) бросает NullPointerException.
     */
    @Test
    void testAddNullSessionThrowsException() {
        Timetable timetable = new Timetable();
        assertThrows(NullPointerException.class,
                () -> timetable.addNewTrainingSession(null));
    }

    /**
     * Проверяем, что getCountByCoaches возвращает список в порядке убывания.
     */
    @Test
    void testGetCountByCoachesSorting() {
        Timetable timetable = new Timetable();

        Coach coachA = new Coach("А", "Борис", "");
        Coach coachB = new Coach("В", "Геннадий", "");

        Group group = new Group("Тренировка", Age.ADULT, 60);

        // coachA: 3 занятия
        timetable.addNewTrainingSession(new TrainingSession(group, coachA,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA,
                DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0)));

        // coachB: 1 занятие
        timetable.addNewTrainingSession(new TrainingSession(group, coachB,
                DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        assertEquals(2, counts.size());
        assertEquals(coachA, counts.get(0).getCoach());
        assertEquals(3, counts.get(0).getTrainingCounter());
        assertEquals(coachB, counts.get(1).getCoach());
        assertEquals(1, counts.get(1).getTrainingCounter());
    }

    /**
     * Проверяем, что возвращаемые коллекции неизменяемы.
     */
    @Test
    void testReturnedCollectionsAreUnmodifiable() {
        Timetable timetable = new Timetable();
        Group group = new Group("Группа", Age.CHILD, 60);
        Coach coach = new Coach("К", "Л", "");
        TrainingSession ts = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        timetable.addNewTrainingSession(ts);

        /* --- проверяем неизменяемость карты за день --- */
        NavigableMap<TimeOfDay, List<TrainingSession>> daySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertThrows(UnsupportedOperationException.class,
                () -> daySessions.put(new TimeOfDay(10, 0), new ArrayList<>()));

        /* --- проверяем неизменяемость списка‑слота (как было до изменений) --- */
        Collection<TrainingSession> slotSessions =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                        new TimeOfDay(9, 0));
        assertThrows(UnsupportedOperationException.class,
                () -> slotSessions.remove(ts));
    }

    /**
     * Проверяем, что занятия добавленные «в произвольном порядке» выводятся
     * отсортированными по времени.
     */
    @Test
    void testGetTrainingSessionsForDaySorting() {
        Timetable timetable = new Timetable();

        Group group = new Group("Группа", Age.CHILD, 60);
        Coach coach = new Coach("К", "Л", "");

        // добавляем в неупорядоченном порядке
        TrainingSession ts1 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(15, 0));
        TrainingSession ts2 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(13, 0));
        TrainingSession ts3 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(ts1);
        timetable.addNewTrainingSession(ts2);
        timetable.addNewTrainingSession(ts3);

        NavigableMap<TimeOfDay, List<TrainingSession>> fridayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);
        assertEquals(3, fridayMap.size());

        Iterator<TimeOfDay> it = fridayMap.keySet().iterator();
        assertEquals(new TimeOfDay(13, 0), it.next());
        assertEquals(new TimeOfDay(15, 0), it.next());
        assertEquals(new TimeOfDay(20, 0), it.next());

        // проверяем корректность списка по каждому ключу
        assertEquals(ts2, fridayMap.get(new TimeOfDay(13, 0)).getFirst());
        assertEquals(ts1, fridayMap.get(new TimeOfDay(15, 0)).getFirst());
        assertEquals(ts3, fridayMap.get(new TimeOfDay(20, 0)).getFirst());
    }
}
