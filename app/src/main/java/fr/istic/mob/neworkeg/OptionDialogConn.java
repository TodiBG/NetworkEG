package fr.istic.mob.neworkeg;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.neworkeg.modeles.Connexion;

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
        this.activity.optionPopupConnVisible = false;
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

        List<Connexion> connexions = new ArrayList<Connexion>();
        connexions = activity.getSelectConn();

        chooseColor = (Button) findViewById(R.id.connsetcolor);
        no = (Button) findViewById(R.id.connclose);

        List<String> categories = new ArrayList<String>();
        for (Connexion conn : connexions) {
            categories.add(conn.getLabel());
            //categories.add(" -- > " + conn.getFin().getLabel());
        }

        no.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupConnVisible = false;
                activity.supportView.invalidate();
                dismiss();
            }
        });


        removeConnBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupConnVisible = false;
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
