package fr.istic.mob.neworkeg;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CreateNetworkDialog extends Dialog {

    public MainActivity activity;
    private Button cancel;
    private Button confirme ;

    private EditText netWorkName ;


    public CreateNetworkDialog(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_network);
        cancel = (Button) findViewById(R.id.cancel_network_creation);
        confirme = (Button) findViewById(R.id.confirm_network_creation);
        netWorkName = (EditText)findViewById(R.id.network_name_content) ;

        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.lecture();
                dismiss();
            }
        });

        confirme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = netWorkName.getText().toString() ;
                if(!txt.equals("")) {
                    activity.NETWORK_NAME = netWorkName.getText().toString();
                    activity.createNetwork();
                    dismiss();
                }
            }
        });

    }
}
