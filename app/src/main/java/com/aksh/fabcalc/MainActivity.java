package com.aksh.fabcalc;

import static com.aksh.fabcalc.utils.AnimationUtils.animateActivityIn;
import static com.aksh.fabcalc.utils.AnimationUtils.animateButtonsIn;
import static com.aksh.fabcalc.utils.AnimationUtils.animateButtonsOut;
import static com.aksh.fabcalc.utils.AnimationUtils.animateKeysToState;
import static com.aksh.fabcalc.utils.AnimationUtils.revealFromCenter;
import static com.aksh.fabcalc.utils.AnimationUtils.revealFromXY;
import static com.aksh.fabcalc.utils.ClickListeners.onKeyClicked;
import static com.aksh.fabcalc.utils.LabelsUtils.applyLabels;
import static com.aksh.fabcalc.utils.LabelsUtils.initArrays;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.aksh.fabcalc.databinding.ActivityMainBinding;
import com.aksh.fabcalc.databinding.BasicCalcKeysBinding;
import com.aksh.fabcalc.databinding.CalcDisplayBinding;
import com.aksh.fabcalc.databinding.CalcOptionsBinding;
import com.aksh.fabcalc.history.HistoryItem;
import com.aksh.fabcalc.utils.CalculationUtils;
import com.aksh.fabcalc.utils.ColorUtils;
import com.aksh.fabcalc.utils.DisplayUtils;
import com.aksh.fabcalc.utils.OnSwipeTouchListener;
import com.aksh.fabcalc.utils.State;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding calc;
    BasicCalcKeysBinding keys;
    CalcDisplayBinding display;
    CalcOptionsBinding options;
    ViewGroup mainLayout;
    State calcState;
    FastItemAdapter<HistoryItem> mFastItemAdapter;

//    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calc = DataBindingUtil.setContentView(this,R.layout.activity_main);
        keys = calc.keys;
        display = calc.display;
        options = calc.options;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainLayout = calc.mainLinearLayout;
        } else {
            mainLayout = calc.mainConstraintLayout;
        }
        calcState = State.BASIC;
        initArrays(this);

        // Animate app (fade in)
        animateActivityIn(mainLayout);

        // TODO: 17-06-2017 Check if this works on SDK 15
        disableKeyboard();
        setListeners();
        CalculationUtils.setAngleUnit(this);

        calc.historyRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        MainActivity.this,
                        LinearLayoutManager.VERTICAL,
                        false));
        mFastItemAdapter = new FastItemAdapter<>();
        calc.historyRecyclerView.setAdapter(mFastItemAdapter);
    }

    private void disableKeyboard() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    Rect outRect = new Rect();
    int[] location = new int[2];

    private boolean isViewInBounds(View view, int x, int y){
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

    private void setListeners() {
        calc.keysOverlayCardView.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public boolean onSwipe() {
                toggleState();
                return true;
            }
        });

        int childCount = keys.basicGridLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = keys.basicGridLayout.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            child.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (isViewInBounds(view, (int) motionEvent.getRawX(),
                            (int) motionEvent.getRawY())) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            onKeyClicked(MainActivity.this, view, calc);
                        }
                    } else {
                        // FIXME: 18-07-2017 This gives NPE until first swipe not starting from fab
                        calc.keysOverlayCardView.dispatchTouchEvent(motionEvent);
                    }
                    return false;
                }
            });
        }

        keys.myFab4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DisplayUtils.resetDisplay(MainActivity.this, calc, view);
                return true;
            }
        });

        calc.options.bottombar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_toggle:
                                toggleState();
                                break;
                            case R.id.action_settings:
                                Intent settingsIntent = new Intent(
                                        MainActivity.this,
                                        SettingsActivity.class);
                                startActivity(settingsIntent);
                                break;
                            case R.id.action_history:
                                mFastItemAdapter.clear();
                                mFastItemAdapter.add(
                                        HistoryItem.getHistoryList(MainActivity.this));
                                calc.historyRecyclerView.setVisibility(View.VISIBLE);
                                keys.basicGridLayout.setVisibility(View.GONE);
                                animateButtonsOut(keys.basicGridLayout);
                                View view = options.bottombar;
                                int coords[] = new int[]{
                                        (view.getLeft() + view.getRight())/2,
                                        (view.getTop() + view.getBottom())/2
                                };
                                view.getLocationOnScreen(coords);
                                revealFromXY(calc.operationsCardView, (view.getLeft() + view.getRight())/2, coords[1]);
                                MenuItem menuItem = calc.options.bottombar.getMenu().getItem(0);
                                menuItem.setIcon(R.drawable.ic_fabcalc_concise);
                                menuItem.setTitle(getString(R.string.action_calc));
                                calcState = State.OTHER;
                                break;
//                            case R.id.action_test:
//                                getContentResolver().delete(
//                                        HistoryProvider.History.CONTENT_URI,
//                                        null,
//                                        null);
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyColors();
        applyLabels(keys.basicGridLayout, calcState);
        calc.displayCardView.post(new Runnable() {
            @Override
            public void run() {
                revealFromCenter(calc.displayCardView);
            }
        });
        calc.centralFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                revealFromCenter(calc.centralFrameLayout);
            }
        });
        calc.navbarCardView.post(new Runnable() {
            @Override
            public void run() {
                revealFromCenter(calc.navbarCardView);
            }
        });
        animateButtonsIn(keys.basicGridLayout);
    }

    // TODO: 15-07-2017 This should be called only if preference has been changed?
    private void applyColors() {
        calc.setColors(ColorUtils.currentColors);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorUtils.currentColors.getPrimaryColorDark());
            getWindow().setNavigationBarColor(ColorUtils.currentColors.getPrimaryColorDark());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        animateButtonsOut(keys.basicGridLayout);
    }

    @Override
    public void onBackPressed() {
        if (calcState != State.BASIC) {
            toggleState();
        } else {
            super.onBackPressed();
        }
    }

    public void switchToState(State state) {
        if (calcState == State.OTHER) {
            calc.historyRecyclerView.setVisibility(View.GONE);
            keys.basicGridLayout.setVisibility(View.VISIBLE);
            View view = options.bottombar;
            int coords[] = new int[]{
                    (view.getLeft() + view.getRight())/2,
                    (view.getTop() + view.getBottom())/2
            };
            view.getLocationOnScreen(coords);
            revealFromXY(calc.operationsCardView, (view.getLeft() + view.getRight())/3, coords[1]);
            animateButtonsIn(keys.basicGridLayout);
            MenuItem item = calc.options.bottombar.getMenu().getItem(0);
            item.setIcon(R.drawable.ic_flask_outline);
            item.setTitle(getString(R.string.action_advanced));
            calcState = state;
            return;
        }
        animateKeysToState(keys.basicGridLayout, state);
        calcState = state;
    }

    public void toggleState() {
        MenuItem item = calc.options.bottombar.getMenu().getItem(0);
        switch (calcState) {
            case BASIC:
                switchToState(State.ADVANCED);
                item.setIcon(R.drawable.ic_child_friendly);
                item.setTitle(getString(R.string.action_basic));
                break;
            case ADVANCED:
                switchToState(State.BASIC);
                item.setIcon(R.drawable.ic_flask_outline);
                item.setTitle(getString(R.string.action_advanced));
                break;
            default:
                switchToState(State.BASIC);
        }
    }

    // TODO: 08-08-2017 move this somewhere appropriate?
    @BindingAdapter("menu")
    public static void setMenu(BottomNavigationView navigationView, int id) {
        navigationView.inflateMenu(id);
    }
}

// FIXME: 19-07-2017 Multiple decimal points possible in a single number
// FIXME: 15-07-2017 Pasting into display edit text doesn't update result preview text view, maybe use changeListener
// FIXME: 15-07-2017 Trigonometric functions at extremes (points with values 0 and INF) -> https://github.com/fasseg/exp4j/issues/76
// FIXME: 22-06-2017 AnimationUtils Speed is set faster on rn4, so check speeds on some other device.
// TODO: 15-07-2017 Implement displaying of infinity symbol
// TODO: 15-07-2017 Check if ripple works on lower sdks
// TODO: 16-07-2017 Add a quick calc option for >N long click, shows basic calc dialog with
// result having two options, re and done (sync and tick)
// TODO: 16-06-2017 BottomBar : Can be hidden from settings, available on swipe up
// TODO: 16-06-2017 History : Accessible from bottom bar ?
// TODO: 20-06-2017 Settings :
// - separation commas?
// - default/launch calc (calc to show on app launch)
// - default angle unit -> radian/degree/<that third one>/custom (get relation to radian)
// TODO: 22-06-2017 Themes :
// - make equal button etc different colored, maybe dark accent, or something.
// - divide theme setting into two parts (Spinner/List Preference?)
//   (Customize option takes to the corresponding screen):
//   color (primary and secondary) and background ();
// TODO: 22-06-2017 Calcs :
// triangle calc: triangle with edit texts as sides and angles, basic input keys; animation maybe
//                upper white expands and triangle appears and keyboard shrinks
// imaginary operations:
// equations:
// matrix
// graphs