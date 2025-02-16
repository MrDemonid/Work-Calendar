package mr.demonid.workcalendar;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import mr.demonid.workcalendar.config.AppSettings;
import mr.demonid.workcalendar.config.ColorSettings;
import mr.demonid.workcalendar.types.CellRendererStyle;
import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.views.PaintStore;
import mr.demonid.workcalendar.views.StylePreviewCell;
import mr.demonid.workcalendar.views.TitlePreviewCell;
import yuku.ambilwarna.AmbilWarnaDialog;


/**
 * Экран настройки цветов календаря
 */
public class SettingsCellColorActivity extends AppCompatActivity {

    private ColorSettings config;
    private AppSettings appSettings;

    private final SparseArray<Runnable> cardMessages = new SparseArray<>();
    private TitlePreviewCell title;
    private StylePreviewCell preview2;
    private StylePreviewCell preview3;
    private StylePreviewCell preview4;
    private StylePreviewCell preview5;
    private StylePreviewCell preview6;
    private StylePreviewCell preview7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_color);

        config = ColorSettings.getInstance();
        config.load();
        appSettings = AppSettings.getInstance();

        /*
            Настраиваем превьюшки
         */
        title = findViewById(R.id.colorPreview1);
        title.setColor(config.getTitleColor());
        preview2 = setPreviewStyle(R.id.colorPreview2, DayType.LABEL, CellRendererStyle.LABEL);
        preview3 = setPreviewStyle(R.id.colorPreview3, DayType.FREE_DAY, appSettings.getDayStyle());
        preview4 = setPreviewStyle(R.id.colorPreview4, DayType.FIRST_HALF_OF_24_HOUR_SHIFT, appSettings.getDayStyle());
        preview5 = setPreviewStyle(R.id.colorPreview5, DayType.SECOND_HALF_OF_24_HOUR_SHIF, appSettings.getDayStyle());
        preview6 = setPreviewStyle(R.id.colorPreview6, DayType.DAY_SHIFT, appSettings.getDayStyle());
        preview7 = setPreviewStyle(R.id.colorPreview7, DayType.NIGHT_SHIFT, appSettings.getDayStyle());

        /*
            Настраиваем кнопки
         */
        Button save = findViewById(R.id.btn_color_save);
        save.setOnClickListener(v -> {
            PaintStore.getInstance().save();
            appSettings.setSecondDayEnable(((SwitchCompat) findViewById(R.id.switch_enable_night)).isChecked());
            appSettings.save();
            finish();
        });

        Button cancel = findViewById(R.id.btn_color_cancel);
        cancel.setOnClickListener(v -> {
            config.load();
            PaintStore.getInstance().load();
            finish();
        });

        /*
            Настраиваем реакцию на выбор менюшек
         */
        setEvent(R.id.colorSettingCardView1, this::changeTitleColor);
        setEvent(R.id.colorSettingCardView2, this::changeLabelDay);
        setEvent(R.id.colorSettingCardView3, this::changeFreeDay);
        setEvent(R.id.colorSettingCardView4, this::changeAfterDay);
        findViewById(R.id.colorSettingLinear5).setOnClickListener(v -> changeAfterSecondDay());
        setEvent(R.id.colorSettingCardView6, this::changeDayShift);
        setEvent(R.id.colorSettingCardView7, this::changeNightShift);

        ((SwitchCompat) findViewById(R.id.switch_enable_night)).setChecked(appSettings.isSecondDayEnable());
    }

    /**
     * Обработчик нажатий на CardView
     */
    View.OnClickListener cardClickListener = view -> {
        Runnable action = cardMessages.get(view.getId());
        Log.d("Style", "View id = " + view.getId());
        if (action != null) {
            action.run(); // Выполняем действие
        }
    };

    /*=========================================================================

        Методы для запуска настроек под каждый пункт меню

      =========================================================================*/

    // Заголовок
    private void changeTitleColor() {
        int current = config.getTitleColor();
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, current, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                config.setTitleColor(color);
                title.setColor(color);
//                config.save();
            }
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
        });
        colorPicker.show();
    }

    // Ячейки разметки календаря
    private void changeLabelDay() {
        showColorSheet(DayType.LABEL);
    }

    // Ячейки свободных (выходных) дней
    private void changeFreeDay() {
        showColorSheet(DayType.FREE_DAY);
    }

    // Суточная смена
    private void changeAfterDay() {
        showColorPickerDialogForPaint(PaintStore.getBorderPaint(DayType.FIRST_HALF_OF_24_HOUR_SHIFT));
    }
    private void changeAfterSecondDay() {
        showColorPickerDialogForPaint(PaintStore.getBorderPaint(DayType.SECOND_HALF_OF_24_HOUR_SHIF));
    }

    // Дневная смена
    private void changeDayShift() {
        showColorPickerDialogForPaint(PaintStore.getBorderPaint(DayType.DAY_SHIFT));
    }

    // Ночная смена
    private void changeNightShift() {
        showColorPickerDialogForPaint(PaintStore.getBorderPaint(DayType.NIGHT_SHIFT));
    }

    /*
     * Вызов SheetDialog, для выбора нескольких цветов.
     */
    private void showColorSheet(DayType type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_color_fg_bg, null);

        Button btnForeground = view.findViewById(R.id.btn_color_foreground);
        Button btnBackground = view.findViewById(R.id.btn_color_background);
        Button btnBorder = view.findViewById(R.id.btn_color_border);

        btnForeground.setOnClickListener(v -> showColorPickerDialogForPaint(PaintStore.getTextPaint(type)));
        btnBackground.setOnClickListener(v -> showColorPickerDialogForPaint(PaintStore.getBackgroundPaint(type)));
        btnBorder.setOnClickListener( v -> showColorPickerDialogForPaint(PaintStore.getBorderPaint(type)));

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
//        bottomSheetDialog.dismiss();      // сам уничтожится при клике мимо него
    }

    /*
     * Вызов диалога выбора цвета.
     */
    private void showColorPickerDialogForPaint(Paint paintColor) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, paintColor.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                paintColor.setColor(color);
                updatePreviews();
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Ничего не делаем
            }
        });
        colorPicker.show();
    }

    /*
     * Перерисовка превьюшек
     */
    private void updatePreviews() {
        preview2.update();
        preview3.update();
        preview4.update();
        preview5.update();
        preview6.update();
        preview7.update();
    }

    /*
     * Привязка обработчика к событию
     */
    private void setEvent(int id, Runnable func) {
        cardMessages.put(id, func);
        findViewById(id).setOnClickListener(cardClickListener);
    }

    /*
     * Настройка стиля и типа превьюшки
     */
    private StylePreviewCell setPreviewStyle(int id, DayType type, CellRendererStyle style) {
        StylePreviewCell cell = findViewById(id);
        if (cell != null) {
            cell.setType(type);
            cell.setRendererStyle(style);
        }
        return cell;
    }


}