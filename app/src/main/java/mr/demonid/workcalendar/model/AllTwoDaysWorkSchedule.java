package mr.demonid.workcalendar.model;

import java.util.UUID;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.types.WorkType;

/**
 * Реализация графика дежурств: два в день, два в ночь, два - отдых.
 */
public class AllTwoDaysWorkSchedule extends AbstractWorkSchedule {

    /**
     * Конструктор графика дежурств.
     * @param firstDay Первый день смены (от 0 до 8).
     */
    public AllTwoDaysWorkSchedule(UUID id, int firstDay) {
        super(id, WorkType.TWO_DAY_TWO_NIGHT_TWO_FREE, firstDay, 6);
    }

    @Override
    public DayType getDayType(int day) {
        if (day > 0 && day <= numDaysOfMonth) {
            int k = (int) ((totalFirstDay + startDay + day - 1) % cycleDays);
            switch (k) {
                case 0:
                case 1:
                    return DayType.DAY_SHIFT;
                case 2:
                case 3:
                    return DayType.NIGHT_SHIFT;
            }
        }
        return DayType.FREE_DAY;
    }

}
