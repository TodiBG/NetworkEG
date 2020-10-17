package fr.istic.mob.neworkeg;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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

    final static long LONG_TOUCH_DURATION = 1000;
    long touchStartTime = 0;

    public boolean optionPopupVisible = false;
    private Integer selectedColor;

    public Node selectedNode = null;


    static {
        mGraph = new Graph(4);
    }

    public static Graph mGraph;

    List<Integer> closestColorsList = new ArrayList<Integer>();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDraw = new DrawableGraph(mGraph);
        closestColorsList = myDraw.mGraph.getNodeColors();
        setContentView(R.layout.activity_main);
        supportView = (ImageView) findViewById(R.id.imageViewPlan);
        supportView.setImageDrawable(myDraw);

        supportView.setOnTouchListener(mModeCreation );

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.layout.menu, menu);
        return true;
    }








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

                    Node n1 = mGraph.selectedNode(downx, downy);
                    Node n2 = mGraph.selectedNode(upx, upy);
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

                    Node startNode = mGraph.selectedNode(downx, downy);
                    Node tempNode = new Node(umpx, umpy);
                    Connnexion conn = mGraph.selectedConn(umpx, umpy);
                    if ((startNode != null) && (tempNode != null)) {
                        Connnexion tempConn = new Connnexion(startNode, tempNode);
                        myDraw.setTempConn(tempConn);
                        supportView.invalidate();
                    }
                    long time = System.currentTimeMillis() - touchStartTime;
                    if (time > LONG_TOUCH_DURATION) {
                        if ((startNode != null)) {
                            if ((tempNode.overlap(startNode))) {// pas encore fais de mouvement ou pas encore loin
                                if (!optionPopupVisible) {
                                    optionPopupVisible = true;
                                    selectedNode = startNode;
                                    showOptions();
                                }
                            }
                            return true;
                        }
                    }
                    break ;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        }
    } ;


    private View.OnTouchListener mModeCreation = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
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
                    if ((Math.abs(upx - downx) < 10) || (Math.abs(upy - downy) < 10)) {
                        int number = 1 + myDraw.mGraph.getNoeuds().size();
                        Node node = new Node(upx, upy, number + "");
                        if (mGraph.addNode(node)) {

                        }
                        supportView.invalidate();
                    }
                    Node n1 = mGraph.selectedNode(downx, downy);
                    Node n2 = mGraph.selectedNode(upx, upy);
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

                    Node startNode = mGraph.selectedNode(downx, downy);
                    Node tempNode = new Node(umpx, umpy);
                    Connnexion conn = mGraph.selectedConn(umpx, umpy);
                    if ((startNode != null) && (tempNode != null)) {
                        Connnexion tempConn = new Connnexion(startNode, tempNode);
                        myDraw.setTempConn(tempConn);
                        supportView.invalidate();
                    }
                    long time = System.currentTimeMillis() - touchStartTime;
                    if (time > LONG_TOUCH_DURATION) {
                        if ((startNode != null)) {
                            if ((tempNode.overlap(startNode))) {// pas encore fais de mouvement ou pas encore loin
                                if (!optionPopupVisible) {
                                    optionPopupVisible = true;
                                    selectedNode = startNode;
                                    showOptions();
                                }
                            }
                            return true;
                        }
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


    public void showColorPopupConn(final Connnexion conn) {

        conn.setColor(Color.BLUE);
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


