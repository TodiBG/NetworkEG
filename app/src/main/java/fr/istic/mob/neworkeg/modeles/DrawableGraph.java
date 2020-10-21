package fr.istic.mob.neworkeg.modeles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static fr.istic.mob.neworkeg.modeles.Connnexion.CONN_WIDTH;


public class DrawableGraph extends Drawable {

    private static final int LABEL_TEXT_SIZE = 30;
    private Paint mModePaint;
    private Paint mConnPaint;
    private Paint mWhitePaint;
    private Paint mPaint;
    private Paint mNodeLapelPaint;
    private Canvas mCanvas = new Canvas();

    private Path mLinePath = new Path();
    public Graph mGraph;
    private Connnexion mTempConn = null; // connexion temporaire pour suivre les mouvements

    public Connnexion getTempConn() {
        return mTempConn;
    }

    public void setTempConn(Connnexion tempConn) {
        this.mTempConn = tempConn;
    }

    public void setTempConnNull() {
        this.mTempConn = null;
    }

    public DrawableGraph(Graph graph) {
        this.mGraph = graph;

        mModePaint = new Paint();
        mModePaint.setColor(Color.DKGRAY);

        mConnPaint = new Paint();
        mConnPaint.setAntiAlias(true);
        mConnPaint.setStyle(Paint.Style.STROKE);
        mConnPaint.setStrokeWidth(CONN_WIDTH);
        mConnPaint.setColor(Color.RED);


        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setStyle(Paint.Style.STROKE);
        mWhitePaint.setStrokeWidth(CONN_WIDTH+5);
        mWhitePaint.setColor(Color.WHITE);


        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        mNodeLapelPaint = new Paint();
        mNodeLapelPaint.setColor(Color.WHITE);
        mNodeLapelPaint.setTextSize(LABEL_TEXT_SIZE);
        mNodeLapelPaint.setFakeBoldText(true);

    }

    public void drawNode(Node node) {
        mModePaint.setColor(node.getColor());
        float left = node.getX()-2*node.getWidth() ;
        float right = node.getX()+2*node.getWidth() ;
        float top = node.getY()-node.getWidth() ;
        float bottom = node.getY()+node.getWidth() ;

        mCanvas.drawRect(left, top, right, bottom, mModePaint);

        //Afficher le nom de l'objet
        node.setLabelSize(LABEL_TEXT_SIZE);
        if( node.getLabel().length()>10){
            if( node.getLabel().length()<18){
                node.setLabelSize(20);
            }else if( node.getLabel().length()<22){
                node.setLabelSize(15);
            }else if( node.getLabel().length()<26){
                node.setLabelSize(10);
            }else {
                node.setLabelSize(10);
                String label = node.getLabel() ;
                label = label.substring(0, 23) ;
                label = label+"..." ;
                node.setLabel(label);
            }
        }
        mNodeLapelPaint.setTextSize(node.getLabelSize());
        //Afficher le nom de l'objet
        float xPos =  node.getX() - (int) (mNodeLapelPaint.measureText(node.getLabel()) / 2);
        float yPos =  (int) (node.getY() - ((mNodeLapelPaint.descent() + mNodeLapelPaint.ascent()) / 2));

        mCanvas.drawText(node.getLabel(), xPos, yPos, mNodeLapelPaint);
    }



    @Override
    public void draw(@NonNull Canvas canvas) {
        this.mCanvas = canvas;
        drawConns();
        drawNodes();
    }

    private void drawNodes() {
        for (Node node : mGraph.getNoeuds()) {
            drawNode(node);
        }
    }

    public void drawConns() {
        for (Connnexion conns : mGraph.getConns()) {
            drawConn(conns);
        }
        if (this.mTempConn != null) {
            drawConn(mTempConn);
        }
    }

    //Tracer une ligne de connexion
    public void drawConn(Connnexion conn) {
        mConnPaint.setColor(conn.getColor());
        mCanvas.drawPath(conn.getPath(), mWhitePaint);
        mCanvas.drawPath(conn.getPath(), mConnPaint);

        //Dessine le milieu de la connexion
        mCanvas.drawCircle(conn.getMidPointX(),conn.getMidPointY(), conn.MID_POINT_RADIUS, mConnPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) { }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
