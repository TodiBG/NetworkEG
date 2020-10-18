package fr.istic.mob.neworkeg;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.neworkeg.modeles.Connnexion;
import fr.istic.mob.neworkeg.modeles.DrawableGraph;
import fr.istic.mob.neworkeg.modeles.Graph;
import fr.istic.mob.neworkeg.modeles.Node;
import fr.istic.mob.neworkeg.modeles.OptionDialogClass;
import fr.istic.mob.neworkeg.modeles.OptionDialogConn;

public class MainActivity extends AppCompatActivity {

    public DrawableGraph myDraw;
    public  ImageView supportView;

    int downx = 0;
    int downy = 0;

    int upx = 0;
    int upy = 0;

    int umpx = 0;
    int umpy = 0;

    final static long LONG_TOUCH_DURATION = 700;
    long touchStartTime = 0;

    public boolean optionPopupVisible = false;
    private Integer selectedColor;

    public Node selectedNode = null;
    public static Connnexion selectedConn = null;


    public static Graph mGraph;

    List<Integer> closestColorsList = new ArrayList<Integer>();

    private void todo(){ }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initReseau() ;
    }

    private void initReseau(){
        mGraph = new Graph(0);
        myDraw = new DrawableGraph(mGraph);
        closestColorsList = myDraw.mGraph.getNodeColors();
        supportView = (ImageView) findViewById(R.id.imageViewPlan);
        supportView.setImageDrawable(myDraw);
        supportView.setOnTouchListener(mModeLecture );
    }



    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    /**
     * Réagir au clic des elements de menu
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_read: {
                supportView.setOnTouchListener(mModeLecture);
                Toast.makeText(this,R.string.reading_mode_info,Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.item_edit : {
                supportView.setOnTouchListener(mModeCreation );
                Toast.makeText(this,R.string.editting_mode_info,Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.item_reset : {
                // Réinitialiser le reseau
                initReseau() ;
                Toast.makeText(this,R.string.resetting_mode_info,Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.item_further : {
                Toast.makeText(this,R.string.further_mode_info,Toast.LENGTH_LONG).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    // Basculer en mode lecture
    private View.OnTouchListener mModeLecture = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downx = (int) event.getX();
                    downy = (int) event.getY();
                    touchStartTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    myDraw.setTempConnNull();
                    upx = (int) event.getX();
                    upy = (int) event.getY();

                    Node n1 = mGraph.getSelectedNode(downx, downy);
                    Node n2 = mGraph.getSelectedNode(upx, upy);
                    if ((n1 != null) && (n2 != null)) {
                        Connnexion a = new Connnexion(n1, n2);
                        mGraph.addConn(a);
                        supportView.invalidate();
                    } else if (n1 != null) {
                        n1.upadte(upx, upy);
                        supportView.invalidate();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    umpx = (int) event.getX();
                    umpy = (int) event.getY();

                    Node startNode = mGraph.getSelectedNode(downx, downy);
                    Node tempNode = new Node(umpx, umpy);
                    Connnexion conn = mGraph.getSelectedConn(umpx, umpy);
                    if ((startNode != null) && (tempNode != null)) {
                        Connnexion tempConn = new Connnexion(startNode, tempNode);
                        myDraw.setTempConn(tempConn);
                        supportView.invalidate();
                    }

                    break ;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        }
    } ;

    // Bacukler en mode création
    private View.OnTouchListener mModeCreation = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            long time = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downx = (int) event.getX();
                    downy = (int) event.getY();
                    touchStartTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    myDraw.setTempConnNull();
                    upx = (int) event.getX();
                    upy = (int) event.getY();

                     time = System.currentTimeMillis() - touchStartTime;

                     boolean newNodeCreated = false ;

                    if ( ((Math.abs(upx - downx) < 10) || (Math.abs(upy - downy) < 10)) && (time>=LONG_TOUCH_DURATION) ) {
                        int number = 1 + myDraw.mGraph.getNoeuds().size();
                        Node node = new Node(upx, upy, number + "");
                        if (mGraph.addNode(node)) { newNodeCreated = true ; }
                        supportView.invalidate();
                    }

                    if ( ((upx == downx) || (upy == downy)) && (time>=LONG_TOUCH_DURATION-800)&&(!newNodeCreated)  ) {

                        Node currentNode = mGraph.getSelectedNode(downx, downy);
                        if ((!optionPopupVisible)&&(currentNode != null)) {
                            optionPopupVisible = true;
                            selectedNode = currentNode;
                            showOptions();
                        }else {
                            Connnexion currentConn = mGraph.getSelectedConn(downx, downy);
                            if ( (currentConn != null)&&(!optionPopupVisible) ){
                                optionPopupVisible = true;
                                selectedConn = currentConn ;
                                showOptionsConn();
                            }
                        }
                    }


                    Node n1 = mGraph.getSelectedNode(downx, downy);
                    Node n2 = mGraph.getSelectedNode(upx, upy);
                    if ((n1 != null) && (n2 != null)) {
                        Connnexion a = new Connnexion(n1, n2);
                        mGraph.addConn(a);
                        supportView.invalidate();
                    } else if (n1 != null) {
                        n1.upadte(upx, upy);
                        supportView.invalidate();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    umpx = (int) event.getX();
                    umpy = (int) event.getY();

                    Node startNode = mGraph.getSelectedNode(downx, downy);
                    Node tempNode = new Node(umpx, umpy);

                    if ((startNode != null) && (tempNode != null)) {
                        Connnexion tempConn = new Connnexion(startNode, tempNode);
                        myDraw.setTempConn(tempConn);
                        supportView.invalidate();
                    }

                    break ;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        }
    };




    /**
     * Couleur des objets
     */

    public void showNodeColorPopup() {
        selectedNode.setColor(Color.RED);
        supportView.invalidate();
    }


    /**
     * Couleur de connexions
     */


    public void showColorPopupConn() {
        selectedConn.setColor(Color.RED);
        supportView.invalidate();
    }


    public void showDefaultColorPopup() {
        Node.DEFAULT_COLOR  = Color.GREEN ;
    }

    public void showOptions() {
        OptionDialogClass optionDialog = new OptionDialogClass(this);
        optionDialog.show();

    }

    public void showOptionsConn() {
        OptionDialogConn optionDialogConn = new OptionDialogConn(this);

        optionDialogConn.show();

    }

    public List<Connnexion> getSelectConn() {
        return mGraph.getConnOutOfMe(selectedNode);
    }

}


