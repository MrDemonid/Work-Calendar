package mr.demonid.workcalendar.views;

import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;

import mr.demonid.workcalendar.config.ColorSettings;
import mr.demonid.workcalendar.config.ColorUnit;
import mr.demonid.workcalendar.types.DayType;

/**
 * Singleton-хранилище стилей контролов.
 */
public class PaintStore {

    private final ColorSettings colorSettings;

    private final static Map<DayType, Paint> backgrounds = new HashMap<>();
    private final static Map<DayType, Paint> borders = new HashMap<>();
    private final static Map<DayType, Paint> texts = new HashMap<>();

    private static PaintStore instance;


    private PaintStore() {
        colorSettings = ColorSettings.getInstance();
        load();
    }

    public static synchronized PaintStore getInstance() {
        if (instance == null) {
            instance = new PaintStore();
        }
        return instance;
    }


    /*
        Загружаем настройки из файла конфигурации
         */
    public void load() {
        backgrounds.clear();
        borders.clear();
        texts.clear();
        colorSettings.forEach(e -> {
            backgrounds.put(e.getType(), makePaint(e.getBackground(), Paint.Style.FILL_AND_STROKE, 2));
            texts.put(e.getType(), makePaint(e.getForeground(), Paint.Style.FILL, 1));
            borders.put(e.getType(), makePaint(e.getBorder(), Paint.Style.STROKE, 3));
        });
    }

    public void save() {
        for (DayType type : DayType.values()) {
            Paint text = texts.get(type);
            Paint bkgr = backgrounds.get(type);
            Paint border = borders.get(type);
            ColorUnit unit = new ColorUnit(type, text == null ? 0 : text.getColor(), bkgr == null ? 0xFF808080 : bkgr.getColor(), border == null ? 0 : border.getColor());
            colorSettings.setColorUnit(unit);
        }
        colorSettings.save();
    }


    public static Paint getBackgroundPaint(DayType type) {
        return backgrounds.get(type);
    }

    public static Paint getBorderPaint(DayType type) {
        return borders.get(type);
    }

    public static Paint getTextPaint(DayType type) {
        return texts.get(type);
    }


    private static Paint makePaint(int color, Paint.Style style, int strokeWidth) {
        Paint res = new Paint();
        res.setColor(color);
        res.setStyle(style);
        res.setStrokeWidth(strokeWidth);
        res.setAntiAlias(true);
        return res;
    }
}
