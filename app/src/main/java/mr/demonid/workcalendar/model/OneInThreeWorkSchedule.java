package mr.demonid.workcalendar.model;

import java.util.UUID;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.types.WorkType;

/**
 * Реализация графика дежурств: сутки через двое (один из трех).
 */
public class OneInThreeWorkSchedule extends AbstractWorkSchedule {

    /**
     * Конструктор графика дежурств.
     * @param firstDay Первый день смены (от 0 до 2).
     */
    public OneInThreeWorkSchedule(UUID id, int firstDay, boolean isShowSecond) {
        super(id, WorkType.DAY_TWO_FREE, firstDay, 3);
        isSecondDayEnable = isShowSecond;
    }

    @Override
    public void setStartOffset(int day) {
        startDay = (int) ((totalFirstDay + day - 1) % cycleDays);
    }

    @Override
    public DayType getDayType(int day)
    {
        int endNight = startDay == (cycleDays-1) ? 0 : startDay+1;
        if (day > 0 && day <= numDaysOfMonth) {
            int k = (int) ((totalFirstDay + day - 1) % cycleDays);
            if (k == startDay)
                return DayType.FIRST_HALF_OF_24_HOUR_SHIFT;
            else if (k == endNight && isSecondDayEnable)
                return DayType.SECOND_HALF_OF_24_HOUR_SHIF;
        }
        return DayType.FREE_DAY;
    }

}
