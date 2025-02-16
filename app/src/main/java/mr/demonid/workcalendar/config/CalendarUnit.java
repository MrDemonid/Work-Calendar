package mr.demonid.workcalendar.config;

import androidx.annotation.NonNull;

import java.util.UUID;

import mr.demonid.workcalendar.types.WorkType;

/**
 * Настроки для отдельного графика работы.
 */
public class CalendarUnit {
    private UUID id;
    private WorkType type;
    private int offsetDay;              // смещение для точки отсчета графика
    private String name;                // пользовательское имя графика


    public CalendarUnit(WorkType type, int offsetDay, String name) {
        this.type = type;
        this.offsetDay = offsetDay;
        this.name = name;
    }

    public CalendarUnit(UUID id, WorkType type, int offsetDay, String name) {
        this.id = id;
        this.type = type;
        this.offsetDay = offsetDay;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WorkType getType() {
        return type;
    }

    public void setType(WorkType type) {
        this.type = type;
    }

    public int getOffsetDay() {
        return offsetDay;
    }

    public void setOffsetDay(int offsetDay) {
        this.offsetDay = offsetDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @NonNull
    @Override
    public String toString() {
        return "CalendarUnit{" +
                "id=" + id +
                ", type=" + type +
                ", offsetDay=" + offsetDay +
                ", name='" + name + '\'' +
                '}';
    }
}
