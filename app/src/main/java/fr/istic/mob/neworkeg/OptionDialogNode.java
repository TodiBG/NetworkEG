package fr.istic.mob.neworkeg;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.istic.mob.neworkeg.database.NetworkDB;

public class OptionDialogNode extends Dialog {
    private NetworkDB dataBase ;
    public MainActivity activity;
    public Dialog dialog;
    private Button yes;
    private Button no;
    private Button chooseColor;
    private Button deleteNode;
    private Button defaultColorBtn;
    private EditText labelInput;
    private TextView nodeLabel;

    public OptionDialogNode(MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.activity.optionPopupNodeVisible = true;
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase = NetworkDB.getInstance(activity) ;


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_node);

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
                activity.optionPopupNodeVisible = false;
                activity.selectedNode = null;
                activity.supportView.invalidate();
                dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                activity.optionPopupNodeVisible = false;
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
                activity.optionPopupNodeVisible = false;
                activity.supportView.invalidate();
                dismiss();
            }
        });
    }
}
