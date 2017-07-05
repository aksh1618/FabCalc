package com.aksh.fabcalc;

import static com.aksh.fabcalc.utils.Animations.animateActivityIn;
import static com.aksh.fabcalc.utils.Animations.animateButtonsIn;
import static com.aksh.fabcalc.utils.Animations.animateButtonsOut;
import static com.aksh.fabcalc.utils.Animations.animateKeysToState;
import static com.aksh.fabcalc.utils.Animations.revealFromCenter;
import static com.aksh.fabcalc.utils.ClickListeners.onKeyClicked;
import static com.aksh.fabcalc.utils.LabelsLists.applyLabels;
import static com.aksh.fabcalc.utils.LabelsLists.initArrays;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.aksh.fabcalc.databinding.ActivityMainBinding;
import com.aksh.fabcalc.databinding.BasicCalcKeysBinding;
import com.aksh.fabcalc.databinding.CalcDisplayBinding;
import com.aksh.fabcalc.utils.OnSwipeTouchListener;
import com.aksh.fabcalc.utils.States;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding calc;
    BasicCalcKeysBinding keys;
    CalcDisplayBinding display;
    ViewGroup mainLayout;
    States calcState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calc = DataBindingUtil.setContentView(this,R.layout.activity_main);
        keys = calc.keys;
        display = calc.display;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainLayout = calc.mainLinearLayout;
        } else {
            mainLayout = calc.mainConstraintLayout;
        }
        calcState = States.BASIC;

        initArrays(this);
        applyLabels(keys.basicGridLayout, calcState);

        // Animate app (fade in)
        animateActivityIn(mainLayout);

        // TODO: 17-06-2017 Check if this works on SDK 15
        disableKeyboard();
        setListeners();
    }


    private void disableKeyboard() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void setListeners() {
        keys.basicGridLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public boolean onSwipe() {
                toggleState();
                return true;
            }
        });

        keys.myFab4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                display.inputEditText.setText("");
                display.resultTextView.setText(getString(R.string.key_num_zero));
                display.resultTextView.setVisibility(View.GONE);
                // TODO: 27-06-2017 change origin, color for reveal anim
                revealFromCenter(calc.calcDisplayCardView);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        for(int i=0; i<mainLayout.getChildCount(); i++) {
            FrameLayout frameLayout = ((FrameLayout) mainLayout.getChildAt(i));
            final View myView = frameLayout.getChildAt(0);
            myView.post(new Runnable() {
                @Override
                public void run() {
                    revealFromCenter(myView);
                }
            });
        }
        animateButtonsIn(keys.basicGridLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        animateButtonsOut(keys.basicGridLayout);
    }

    public void onKeyPressed(View view){
        onKeyClicked(this, view, calc);
    }

    @Override
    public void onBackPressed() {
        if (calcState == States.BASIC) {
            super.onBackPressed();
        } else {
            switchToState(States.BASIC);
        }
    }

    public void switchToState(States state) {
        animateKeysToState(keys.basicGridLayout, state);
        calcState = state;
    }

    public void toggleState() {
        if (calcState == States.BASIC) {
            switchToState(States.ADVANCED);
        } else {
            switchToState(States.BASIC);
        }
    }

    public void onOptionPressed(View view) {
        if (calcState == States.BASIC) {
            switchToState(States.ADVANCED);
            calc.options.statusTextView.setText("<- BASIC");
        } else {
            switchToState(States.BASIC);
            calc.options.statusTextView.setText("ADVANCED");
        }
    }
}

// TODO: 22-06-2017 Animations Speed is set faster on rn4, so check speeds on some other device.
// TODO: 16-06-2017 BottomBar
// TODO: 16-06-2017 History
// TODO: 20-06-2017 Settings :
// - clear on equate? (pressing equal and then typing something else doesn't do anything)
// - separation commas?
// - custom background for operations card view (like a pic or something)
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