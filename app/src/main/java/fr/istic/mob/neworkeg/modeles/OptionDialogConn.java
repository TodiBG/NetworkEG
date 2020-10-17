package fr.istic.mob.neworkeg.modeles;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    private Button chooseColor;
    private Spinner listspinner;
    private EditText labelEdit;
    private Button removeConnBtn;

    public OptionDialogConn(MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.activity.optionPopupVisible = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupcon);
        listspinner = (Spinner) findViewById(R.id.spinner);
        labelEdit = (EditText) findViewById(R.id.connLabelEditText);
        removeConnBtn = (Button) findViewById(R.id.removeArc);

        List<Connnexion> Connnexions = new ArrayList<Connnexion>();
        Connnexions = activity.getSelectConn();

        chooseColor = (Button) findViewById(R.id.connsetcolor);
        no = (Button) findViewById(R.id.connclose);

        List<String> categories = new ArrayList<String>();
        for (Connnexion conn : Connnexions) {
            categories.add(conn.getLabel());
            //categories.add(" -- > " + conn.getFin().getLabel());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, categories);


        // attaching data adapter to spinner
        listspinner.setAdapter(dataAdapter);

        no.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupVisible = false;
                dismiss();
            }
        });

        final List<Connnexion> finalArcs = Connnexions;

        removeConnBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupVisible = false;
                MainActivity.mGraph.getConns().remove(finalArcs.get((int) listspinner.getSelectedItemId()));
                dismiss();
            }
        });

        labelEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                finalArcs.get((int) listspinner.getSelectedItemId()).setLabel(labelEdit.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


        chooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showColorPopupConn((Connnexion) finalArcs.get((int) listspinner.getSelectedItemId()));
            }
        });

    }
}
