package fr.istic.mob.neworkeg;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CreateNodeDialog extends Dialog {

    public MainActivity activity;
    private Button cancel;
    private Button confirme ;
    private Button color ;

    private EditText nodeName ;


    public CreateNodeDialog(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_node);
        cancel = (Button) findViewById(R.id.cancel_node_creation);
        confirme = (Button) findViewById(R.id.confirm_node_creation);
        nodeName = (EditText)findViewById(R.id.node_name_content) ;
        color = (Button) findViewById(R.id.choose_new_node_colorbtn) ;

        color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.showNewNodeColorPop();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        confirme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = nodeName.getText().toString() ;
                if(!txt.equals("")) {
                    activity.mNewNode.setLabel(nodeName.getText().toString());
                    activity.mGraph.addNode(activity.mNewNode);
                    activity.createNetwork();
                    dismiss();
                }
            }
        });

    }
}
