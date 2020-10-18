package fr.istic.mob.neworkeg.modeles;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.istic.mob.neworkeg.MainActivity;
import fr.istic.mob.neworkeg.R;

/**
 * Created by diarranabe on 09/11/2017.
 */

public class OptionDialogClass extends Dialog {

    public MainActivity activity;
    public Dialog dialog;
    private Button yes;
    private Button no;
    private Button chooseColor;
    private Button deleteNode;
    private Button defaultColorBtn;
    private EditText labelInput;
    private TextView nodeLabel;

    public OptionDialogClass(MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.activity.optionPopupVisible = true;
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

        yes = (Button) findViewById(R.id.savebtn);
        no = (Button) findViewById(R.id.dismiss);
        chooseColor = (Button) findViewById(R.id.choosecolorbtn);
        deleteNode = (Button) findViewById(R.id.deletebtn);
        defaultColorBtn = (Button) findViewById(R.id.defaultcolorbtn);
        labelInput = (EditText) findViewById(R.id.node_label_input);
        nodeLabel = (TextView) findViewById(R.id.nodelabel);

        labelInput.setText(activity.selectedNode.getLabel());
        nodeLabel.setText(activity.selectedNode.getLabel());

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.selectedNode.setLabel(labelInput.getText().toString());
                activity.optionPopupVisible = false;
                activity.selectedNode = null;
                activity.supportView.invalidate();
                dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupVisible = false;
                dismiss();
            }
        });

        chooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showNodeColorPopup();
            }
        });

        defaultColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     activity.showNodeDefaultColorPopup();
                }
        });

        deleteNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    activity.myDraw.mGraph.removeNode(activity.selectedNode);
                    activity.optionPopupVisible = false;
                    activity.supportView.invalidate();
                    dismiss();
            }
        });
    }
}
