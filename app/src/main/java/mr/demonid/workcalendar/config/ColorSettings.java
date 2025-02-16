package mr.demonid.workcalendar.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mr.demonid.workcalendar.WorkApp;
import mr.demonid.workcalendar.types.DayType;


public class ColorSettings implements Iterable<ColorUnit> {

    private static final String PREFS_NAME = "work_colors_prefs";

    private static final String KEY_TITLE_COLOR = "title-color";
    private static final String KEY_BKGR = "color-background";
    private static final String KEY_FRGR = "color-foreground";
    private static final String KEY_BORDER = "color-border";

    private final SharedPreferences preferences;
    private static ColorSettings instance;

    private int titleColor;
    private final List<ColorUnit> colorsSettings;
    private boolean isDirty;


    private ColorSettings(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        colorsSettings = new ArrayList<>();
    }

    public static synchronized ColorSettings getInstance() {
        if (instance == null) {
            instance = new ColorSettings(WorkApp.getAppContext());
        }
        return instance;
    }

    public void load() {
        titleColor = preferences.getInt(KEY_TITLE_COLOR, 0xFF808080);

        colorsSettings.clear();
        colorsSettings.add(loadColorUnit(DayType.LABEL,                         Color.WHITE, Color.BLUE,   Color.BLACK));
        colorsSettings.add(loadColorUnit(DayType.FREE_DAY,                      Color.BLACK, Color.LTGRAY, Color.BLACK));
        colorsSettings.add(loadColorUnit(DayType.FIRST_HALF_OF_24_HOUR_SHIFT,   Color.BLACK, Color.LTGRAY, Color.RED));
        colorsSettings.add(loadColorUnit(DayType.SECOND_HALF_OF_24_HOUR_SHIF,   Color.BLACK, Color.LTGRAY, Color.YELLOW));
        colorsSettings.add(loadColorUnit(DayType.DAY_SHIFT,                     Color.BLACK, Color.LTGRAY, Color.MAGENTA));
        colorsSettings.add(loadColorUnit(DayType.NIGHT_SHIFT,                   Color.BLACK, Color.LTGRAY, Color.YELLOW));
        colorsSettings.add(loadColorUnit(DayType.EMPTY,                         Color.BLACK, Color.LTGRAY, Color.DKGRAY));
        isDirty = false;
    }

    /*
        Скидывает настройки в файл.
     */
    public void save() {
        if (isDirty) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_TITLE_COLOR, titleColor);
            editor.apply();

            colorsSettings.forEach(this::saveColorUnit);
            isDirty = false;
        }
    }

    /*
    Сброс настроек до дефолтных.
 */
    public void reset() {
        preferences.edit().clear().apply();
        load();
    }


    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        if (this.titleColor != titleColor) {
            this.titleColor = titleColor | 0xFF000000;  // убираем альфа-наложение
            isDirty = true;
        }
    }

    public void setColorUnit(ColorUnit unit) {
        if (colorsSettings.contains(unit)) {
            int index = colorsSettings.indexOf(unit);
            colorsSettings.set(index, unit);
        } else {
            colorsSettings.add(unit);
        }
        isDirty = true;
    }


    /*
    Загружает настройки цветов одного типа
     */
    private ColorUnit loadColorUnit(DayType type, int defFrGr, int defBkGr, int defBorder) {
        int background = preferences.getInt(KEY_BKGR + "-" + type.name(), defBkGr);
        int foreground = preferences.getInt(KEY_FRGR + "-" + type.name(), defFrGr);
        int border = preferences.getInt(KEY_BORDER + "-" + type.name(), defBorder);
        return new ColorUnit(type, foreground, background, border);
    }

    /*
    Сохраняет настройки цветов одного типа
     */
    private void saveColorUnit(ColorUnit unit) {
        if (unit != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_BKGR + "-" + unit.getType().name(), unit.getBackground());
            editor.putInt(KEY_FRGR + "-" + unit.getType().name(), unit.getForeground());
            editor.putInt(KEY_BORDER + "-" + unit.getType().name(), unit.getBorder());
            editor.apply();
        }
    }


    /*
        Итератор для перебора всех цветов
     */
    @NonNull
    @Override
    public Iterator<ColorUnit> iterator()
    {
        return new ImplCommands();
    }

    private class ImplCommands implements Iterator<ColorUnit> {
        int index = 0;

        @Override
        public boolean hasNext()
        {
            return index < colorsSettings.size();
        }

        @Override
        public ColorUnit next()
        {
            if (hasNext()) {
                ColorUnit res = colorsSettings.get(index++);
                return new ColorUnit(res.getType(), res.getForeground(), res.getBackground(), res.getBorder());
            }
            return null;
        }
    }
}
