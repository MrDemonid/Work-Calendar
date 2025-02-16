package mr.demonid.workcalendar.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import mr.demonid.workcalendar.WorkApp;
import mr.demonid.workcalendar.types.CellRendererStyle;
import mr.demonid.workcalendar.types.WorkType;

/**
 * Настройки приложения.
 * Поскольку все настройки глобальные, то выполнен в Singletone паттерне.
 */
public class AppSettings implements Iterable<CalendarUnit> {

    private static final String KEY_UUID = "uuid-list";

    private static final String PREFS_NAME = "work_calendar_prefs";
    private static final String KEY_WORK_TYPE = "work-type";
    private static final String KEY_DAY_STYLE = "day-style";
    private static final String KEY_SECOND_ENABLE = "second-enable";

    private static final String KEY_OFFSET = "mode-offset";
    private static final String KEY_WORK = "mode-work";
    private static final String KEY_NAME = "mode-name";

    private final List<CalendarUnit> calendarSettings;  // список настроек графиков

    private UUID currentSchedule;       // текущий график
    private CellRendererStyle cellRendererStyle;          // стиль отображения ячеек календаря
    private boolean isSecondDayEnable;  // учитывать ли в календаре окончание суток (с 0ч до 8ч)
    private boolean isDirty;

    private static AppSettings instance;
    private final SharedPreferences preferences;


    private AppSettings(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        calendarSettings = new ArrayList<>();
        isDirty = false;
    }

    public static synchronized AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings(WorkApp.getAppContext());
        }
        return instance;
    }

    /*
        Загрузка настроек из файла.
     */
    public void load() {

        try {
            currentSchedule = UUID.fromString(preferences.getString(KEY_WORK_TYPE, null));
        } catch (Exception e) {
            currentSchedule = null;
        }
        cellRendererStyle = CellRendererStyle.valueOf(preferences.getString(KEY_DAY_STYLE, CellRendererStyle.CIRCLE.toString()));
        isSecondDayEnable = preferences.getBoolean(KEY_SECOND_ENABLE, false);

        calendarSettings.clear();
        Set<String> uuids = preferences.getStringSet(KEY_UUID, new HashSet<>());
        uuids.forEach(e -> {
            CalendarUnit unit = loadUnit(e);
            calendarSettings.add(unit);
            Log.d("Setup", String.format("Load unit: %s\n", unit));
        });

        isDirty = false;
    }

    /*
        Скидывает настройки в файл.
     */
    public void save() {
        if (isDirty) {
            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean(KEY_SECOND_ENABLE, isSecondDayEnable);
            editor.putString(KEY_DAY_STYLE, cellRendererStyle.name());
            if (currentSchedule != null)
                editor.putString(KEY_WORK_TYPE, currentSchedule.toString());
            else
                editor.remove(KEY_WORK_TYPE);

            // Сохраняем графики, для чего потребуется удалить ненужные
            Set<String> oldId = preferences.getStringSet(KEY_UUID, new HashSet<>());
            Set<String> newId = calendarSettings.stream().map(e -> e.getId().toString()).collect(Collectors.toSet());
            // получаем разницу old - new
            Set<String> removed = oldId.stream().filter(e -> !newId.contains(e)).collect(Collectors.toSet());
            // удаляем ненужные значения
            removed.forEach(this::deleteUnit);
            // и сохраняем новые
            calendarSettings.forEach(this::saveUnit);
            editor.putStringSet(KEY_UUID, newId);
            editor.apply();
            isDirty = false;
        }
    }



    /*
        Сброс настроек до дефолтных.
     */
    public void reset() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DAY_STYLE, CellRendererStyle.CIRCLE.name());
        editor.putBoolean(KEY_SECOND_ENABLE, false);
        editor.apply();
//        preferences.edit().clear().apply();
        load();
    }

    //region Геттеры и Сеттера

    public UUID getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(UUID currentSchedule) {
        if (this.currentSchedule != currentSchedule) {
            this.currentSchedule = currentSchedule;
            isDirty = true;
        }
    }

    public CellRendererStyle getDayStyle() {
        return cellRendererStyle;
    }

    public void setDayStyle(CellRendererStyle cellRendererStyle) {
        if (this.cellRendererStyle != cellRendererStyle) {
            this.cellRendererStyle = cellRendererStyle;
            isDirty = true;
        }
    }

    public boolean isSecondDayEnable() {
        return isSecondDayEnable;
    }

    public void setSecondDayEnable(boolean secondDayEnable) {
        if (isSecondDayEnable != secondDayEnable) {
            isSecondDayEnable = secondDayEnable;
            isDirty = true;
        }
    }

    /**
     * Удаление CalendarUnit.
     * Удаление только из списка, а чтобы удалить с диска, необходимо
     * вызвать save().
     */
    public void removeSettingUnit(UUID uuid) {
        calendarSettings.stream().filter(e -> e.getId() == uuid).findFirst().ifPresent(calendarSettings::remove);
        isDirty = true;
    }

    /**
     * Меняет настройки текущего, или добавляет новый график работы
     * @param unit График работы
     */
    public void setSettingUnit(CalendarUnit unit) {
        CalendarUnit res = calendarSettings.stream().filter(e -> e.getId().equals(unit.getId())).findFirst().orElse(null);
        if (res != null) {
            int index = calendarSettings.indexOf(res);
            calendarSettings.set(index, unit);
        } else {
            calendarSettings.add(unit);
        }
        isDirty = true;
    }

    /**
     * Возвращает значение смещения графика для календаря.
     *
     * @param type Тип графика, для которого получаем значение.
     */
    public int getOffsetFirstDay(WorkType type) {
        return calendarSettings.stream().filter(e -> e.getType() == type).findFirst().map(CalendarUnit::getOffsetDay).orElse(0);
    }

    /**
     * Устанавливает значение смещения графика для календаря.
     *
     * @param type      Тип графика, для которого устанавливаем значение.
     * @param offsetDay Смещение.
     */
    public void setOffsetFirstDay(WorkType type, int offsetDay) {
        Optional<CalendarUnit> unit = calendarSettings.stream().filter(e -> e.getType() == type).findFirst();
        if (unit.isPresent()) {
            unit.get().setOffsetDay(offsetDay);
            isDirty = true;
        }
    }

    //endregion


    /*
    Загружает настройки для одного графика.
     */
    private CalendarUnit loadUnit(String uuid) {
        WorkType work = WorkType.valueOf(preferences.getString(KEY_WORK + "-" + uuid, WorkType.DAY_TWO_FREE.name()));
        int offset = preferences.getInt(KEY_OFFSET + "-" + uuid, 0);
        String name = preferences.getString(KEY_NAME + "-" + uuid, work.getDescription());
        return new CalendarUnit(UUID.fromString(uuid), work, offset, name);
    }

    /*
    Сохраняет натсройки одного графика.
     */
    private void saveUnit(CalendarUnit unit) {
        if (unit != null) {
            SharedPreferences.Editor editor = preferences.edit();
            String uuid = unit.getId().toString();
            editor.putString(KEY_WORK + "-" + uuid, unit.getType().name());
            editor.putInt(KEY_OFFSET + "-" + uuid, unit.getOffsetDay());
            editor.putString(KEY_NAME + "-" + uuid, unit.getName());
            editor.apply();
        }
    }

    /*
    Удаляет настройки одного графика.
     */
    private void deleteUnit(String uuid) {
        SharedPreferences.Editor editor = preferences.edit();;
        editor.remove(KEY_WORK + "-" + uuid);
        editor.remove(KEY_OFFSET + "-" + uuid);
        editor.remove(KEY_NAME + "-" + uuid);
        editor.apply();
    }


    /*
        Итератор для перебора всех настроек каледнарей
     */
    @NonNull
    @Override
    public Iterator<CalendarUnit> iterator() {
        return new ImplCommands();
    }

    private class ImplCommands implements Iterator<CalendarUnit> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < calendarSettings.size();
        }

        @Override
        public CalendarUnit next() {
            if (hasNext()) {
                CalendarUnit res = calendarSettings.get(index++);
                return new CalendarUnit(res.getId(), res.getType(), res.getOffsetDay(), res.getName());
            }
            return null;
        }
    }

}
