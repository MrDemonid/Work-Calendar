package mr.demonid.workcalendar.model;

import androidx.annotation.NonNull;

import java.util.UUID;

import mr.demonid.workcalendar.types.WorkType;


/**
 * Абстрактная реализация графика работы.
 * Реализует базовые методы, общие для всех.
 */
public abstract class AbstractWorkSchedule implements WorkSchedule {

    protected final UUID id;
    protected final WorkType type;
    protected final String name;
    protected long totalFirstDay;           // первый день месяца, исчисляя от 1 января 1970
    protected long numDaysOfMonth;          // кол-во дней в месяце
    protected int startDay;                 // первый день смены - с 8 утра до 24 ночи (0 - 8)
    protected int cycleDays;
    protected boolean isSecondDayEnable;    // отображать ли второй день смены (с 0 до 8)

    protected final CalendarUtil calendar;


    public AbstractWorkSchedule(UUID id, String name, WorkType type, int firstDay, int cycleDays) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cycleDays = cycleDays;
        calendar = CalendarUtil.getInstance();
        startDay = firstDay % cycleDays;
        isSecondDayEnable = false;
        refresh();
    }

    @Override
    public UUID getId() {
        return id;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public WorkType getType() {
        return type;
    }

    @Override
    public void refresh() {
        totalFirstDay = calendar.getDayFromEpoch();
        numDaysOfMonth = calendar.getDaysInCurrentMonth();
    }


    @Override
    public void setStartOffset(int day) {
        startDay = cycleDays - (int) ((totalFirstDay + day - 1) % cycleDays);
    }

    @Override
    public int getStartOffset() {
        return startDay;
    }


    @NonNull
    @Override
    public String toString() {
        return "ThemeAllTwoDays{" +
                "type=" + type +
                ", startDay=" + startDay +
                '}';
    }

}
