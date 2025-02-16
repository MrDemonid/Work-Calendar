package mr.demonid.workcalendar.model;

import java.util.UUID;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.types.WorkType;

/**
 * Реализация графика дежурств: неделя в день, неделя в ночь.
 */
public class WeekDaysWorkSchedule extends AbstractWorkSchedule {


    public WeekDaysWorkSchedule(UUID id, String name, int firstDay) {
        super(id, name, WorkType.WEEK_DAY_WEEK_NIGHT, firstDay, 14);
    }


    @Override
    public DayType getDayType(int day) {
        if (day > 0 && day <= numDaysOfMonth) {
            int k = (int) ((totalFirstDay + startDay + day - 1) % cycleDays);
            if (k < 7) {
                return DayType.DAY_SHIFT;
            } else if (k < 14) {
                return DayType.NIGHT_SHIFT;
            }
        }
        return DayType.FREE_DAY;
    }

}
