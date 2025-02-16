package mr.demonid.workcalendar.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mr.demonid.workcalendar.config.AppSettings;
import mr.demonid.workcalendar.types.WorkType;


/**
 * менеджер всех доступных графиков работы.
 * Реализовано по паттерну Factory Method.
 * Дополнительно реализован функционал циклического перебора тем по порядку,
 * в любом направлении.
 */
public class WorkSchedulesManager implements Iterable<WorkSchedule> {

    private final Map<UUID, WorkSchedule> workSchedules = new HashMap<>();
    private final List<UUID> keys = new ArrayList<>();

    private int currentSchedule;


    public WorkSchedulesManager() {
        currentSchedule = 0;
        registerThemes();
        AppSettings config = AppSettings.getInstance();
        setCurrentSchedule(config.getCurrentSchedule());
    }

    /**
     * Регистрация всех доступных графиков работы.
     */
    private void registerThemes() {
        AppSettings config = AppSettings.getInstance();
        config.forEach(e -> {
            switch (e.getType()) {
                case DAY_TWO_FREE:
                    register(e.getId(), new OneInThreeWorkSchedule(e.getId(), config.getOffsetFirstDay(WorkType.DAY_TWO_FREE), config.isSecondDayEnable()));
                    break;
                case DAY_THREE_FREE:
                    register(e.getId(), new OneInFourWorkSchedule(e.getId(), config.getOffsetFirstDay(WorkType.DAY_THREE_FREE), config.isSecondDayEnable()));
                    break;
                case WEEK_DAY_WEEK_NIGHT:
                    register(e.getId(), new WeekDaysWorkSchedule(e.getId(), config.getOffsetFirstDay(WorkType.WEEK_DAY_WEEK_NIGHT)));
                    break;
                case TWO_DAY_TWO_NIGHT_TWO_FREE:
                    register(e.getId(), new AllTwoDaysWorkSchedule(e.getId(), config.getOffsetFirstDay(WorkType.TWO_DAY_TWO_NIGHT_TWO_FREE)));
                    break;
                case THREE_DAY_THREE_NIGHT_THREE_FREE:
                    register(e.getId(), new AllThreeDaysWorkSchedule(e.getId(), config.getOffsetFirstDay(WorkType.THREE_DAY_THREE_NIGHT_THREE_FREE)));
            }
        });
    }

    /*
     * Регистрация графика.
     */
    private void register(UUID uuid, WorkSchedule theme) {
        workSchedules.put(uuid, theme);
        keys.add(uuid);
    }

    /**
     * Установка текущего графика работы.
     */
    public void setCurrentSchedule(UUID uuid) {
        if (uuid != null) {
            int index = keys.indexOf(uuid);
            if (index >= 0) {
                currentSchedule = index;
            }
        }
    }

    /**
     * Возврат текущей график работы.
     * @return Тема, или null, если их нет.
     */
    public WorkSchedule getCurrentWorkSchedule() {
        try {
            return workSchedules.get(keys.get(currentSchedule));
        } catch (IndexOutOfBoundsException ignored) {}
        return null;
    }

    /**
     * Переходит к следующему графику работы и возвращает на него ссылку.
     * @return График, или null, если их нет.
     */
    public WorkSchedule next() {
        if (!keys.isEmpty()) {
            currentSchedule = currentSchedule == keys.size() - 1 ? 0 : currentSchedule +1;
            try {
                return workSchedules.get(keys.get(currentSchedule));
            } catch (IndexOutOfBoundsException ignored) {}
        }
        return null;
    }

    /**
     * Переходит к предыдущему графику работы и возвращает на него ссылку.
     * @return График, или null, если их нет.
     */
    public WorkSchedule prev() {
        if (!keys.isEmpty()) {
            currentSchedule = currentSchedule == 0 ? keys.size()-1 : currentSchedule -1;
            try {
                return workSchedules.get(keys.get(currentSchedule));
            } catch (IndexOutOfBoundsException ignored) {}
        }
        return null;
    }


    /*
     * Реализация перебора всех зарегистрированных графиков работы.
     */
    @NonNull
    @Override
    public Iterator<WorkSchedule> iterator()
    {
        return new ImplCommands();
    }

    private class ImplCommands implements Iterator<WorkSchedule> {
        int index = 0;

        @Override
        public boolean hasNext()
        {
            return index < keys.size();
        }

        @Override
        public WorkSchedule next()
        {
            if (hasNext()) {
                return workSchedules.get(keys.get(index++));
            }
            return null;
        }
    }


}
