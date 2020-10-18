package fr.istic.mob.neworkeg.modeles;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.neworkeg.MainActivity;
import fr.istic.mob.neworkeg.R;

/**
 * Created by matok on 09/11/2017.
 */

public class OptionDialogConn extends Dialog {

    public MainActivity activity;
    private Button no;
    private Button defautColorBtn ;
    private Button chooseColor;
    private Button removeConnBtn;
    private TextView firstNode  ;
    private TextView secondNode ;


    public OptionDialogConn(MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.activity.optionPopupVisible = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupcon);
        removeConnBtn = (Button) findViewById(R.id.removeConn);
        firstNode = (TextView)findViewById(R.id.firtNodeLabel) ;
        secondNode = (TextView)findViewById(R.id.secondNodeLabel) ;
        firstNode.setText(activity.selectedConn.getDebut().getLabel() );
        secondNode.setText(activity.selectedConn.getFin().getLabel());
        defautColorBtn = (Button) findViewById(R.id.connDefautColorBtn) ;

        List<Connnexion> Connnexions = new ArrayList<Connnexion>();
        Connnexions = activity.getSelectConn();

        chooseColor = (Button) findViewById(R.id.connsetcolor);
        no = (Button) findViewById(R.id.connclose);

        List<String> categories = new ArrayList<String>();
        for (Connnexion conn : Connnexions) {
            categories.add(conn.getLabel());
            //categories.add(" -- > " + conn.getFin().getLabel());
        }

        no.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupVisible = false;
                activity.supportView.invalidate();
                dismiss();
            }
        });


        removeConnBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupVisible = false;
                MainActivity.mGraph.getConns().remove(MainActivity.selectedConn);
                activity.supportView.invalidate();
                dismiss();
            }
        });

        defautColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showConnDefaultColorPopup();
            }
        });


        chooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showColorPopupConn();
            }
        });

    }
}
