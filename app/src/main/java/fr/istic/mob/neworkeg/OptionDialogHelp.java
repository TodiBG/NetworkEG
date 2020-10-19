package fr.istic.mob.neworkeg;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class OptionDialogHelp extends Dialog {

    public MainActivity activity;

    public Dialog dialog;
    private Button helpClose ;

    public OptionDialogHelp(MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.activity.optionPopupVisible = true;
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_help);
        helpClose =  (Button)findViewById(R.id.helpClose) ;

        helpClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { dismiss();
                MainActivity.optionPopupVisible = false; }
        });
    }
}
