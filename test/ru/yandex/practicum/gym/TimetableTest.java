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

        // за понедельник вернулось одно занятие
        Collection<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());
        assertTrue(monday.contains(singleTrainingSession));

        // за вторник не вернулось занятий
        Collection<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesday.isEmpty());
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

        // за понедельник – одно занятие
        Collection<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());
        assertTrue(monday.contains(mondayChildTrainingSession));

        // за четверг – два занятия в правильном порядке: 13:00 → 20:00
        Collection<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursday.size());
        List<TrainingSession> list = new ArrayList<>(thursday);
        assertEquals(new TimeOfDay(13, 0), list.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(20, 0), list.get(1).getTimeOfDay());

        // за вторник – нет занятий
        Collection<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesday.isEmpty());
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

        Collection<TrainingSession> weekDay = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);
        assertEquals(2, weekDay.size());
        assertTrue(weekDay.contains(ts1));
        assertTrue(weekDay.contains(ts2));

        Collection<TrainingSession> slot = timetable.getTrainingSessionsForDayAndTime(
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

        // Получаем занятия за понедельник
        Collection<TrainingSession> daySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertThrows(UnsupportedOperationException.class,
                () -> daySessions.add(new TrainingSession(group, coach,
                        DayOfWeek.MONDAY, new TimeOfDay(10, 0))));

        // Получаем занятия за конкретное время
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

        List<TrainingSession> list =
                new ArrayList<>(timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY));
        assertEquals(3, list.size());
        assertEquals(new TimeOfDay(13, 0), list.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(15, 0), list.get(1).getTimeOfDay());
        assertEquals(new TimeOfDay(20, 0), list.get(2).getTimeOfDay());
    }
}
