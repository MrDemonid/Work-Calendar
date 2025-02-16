package mr.demonid.workcalendar.model;

import java.util.UUID;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.types.WorkType;


/**
 * Интерфейс графика в календаре.
 */
public interface WorkSchedule {
    UUID getId();
    String getName();                   // возвращает пользовательское имя графика
    WorkType getType();                 // возвращает тип графика
    void refresh();                     // пересчет данных по текущему месяцу
    DayType getDayType(int day);        // возвращает тип ячейки для указанного для
    void setStartOffset(int day);       // задает смещение для расчетов графика
    int getStartOffset();               // возвращает текущее смещение для расчетов графика
}
