package mr.demonid.workcalendar.model;

import java.util.UUID;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.types.WorkType;

/**
 * Реализация графика дежурств: три в день, три в ночь и три отдых.
 */
public class AllThreeDaysWorkSchedule extends AbstractWorkSchedule {

    /**
     * Конструктор графика дежурств.
     * @param firstDay Первый день смены (от 0 до 8).
     */
    public AllThreeDaysWorkSchedule(UUID id, int firstDay) {
        super(id, WorkType.THREE_DAY_THREE_NIGHT_THREE_FREE, firstDay, 9);
    }

    @Override
    public DayType getDayType(int day) {
        if (day > 0 && day <= numDaysOfMonth) {
            int k = (int) ((totalFirstDay + startDay + day - 1) % cycleDays);
            switch (k) {
                case 0:
                case 1:
                case 2:
                    return DayType.DAY_SHIFT;
                case 3:
                case 4:
                case 5:
                    return DayType.NIGHT_SHIFT;
            }
        }
        return DayType.FREE_DAY;
    }

}
