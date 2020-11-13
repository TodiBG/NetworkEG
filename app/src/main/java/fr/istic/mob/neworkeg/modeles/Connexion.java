package fr.istic.mob.neworkeg.modeles;

import android.graphics.Color;
import android.graphics.Path;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Collection;

import fr.istic.mob.neworkeg.MainActivity;

@Entity
public class Connexion {

    @PrimaryKey(autoGenerate = true) private long id;
    @ColumnInfo(name = "color") private int color;
    @ColumnInfo(name = "label") private String label;

    @ForeignKey(entity = Node.class, parentColumns = "id", childColumns = "finNodeId", onDelete = ForeignKey.CASCADE)
    private long finNodeId ;
    @ForeignKey(entity = Node.class,parentColumns = "id",childColumns = "debutNodeId", onDelete = ForeignKey.CASCADE)
    private long debutNodeId ;

    @Ignore
    private Node debut;
    @Ignore
    private Node fin;

    private static final int SELECT_DISTANCE = 30;

    public static int DEFAULT_COLOR = Color.RED;
    public static float MID_POINT_RADIUS = 9 ;
    public static int CONN_WIDTH= 10;


    public Connexion(){
        this.debut = MainActivity.nodeViewModel.getNodeById(debutNodeId) ;
        this.fin = MainActivity.nodeViewModel.getNodeById(finNodeId) ;
    }


    @Ignore
    public Connexion(long id, int color, String label, long debut, long fin) {
        this.id = id ;
        this.color = color ;
        this.debutNodeId = debut;
        this.finNodeId = fin;
        this.label = label ; // this.debut.getShortLabel()+" --> "+this.fin.getShortLabel();

       // this.debut = MainActivity.nodeViewModel.getNodeById(debut) ;
       // this.fin = MainActivity.nodeViewModel.getNodeById(debut) ;
    }

    @Ignore
    public Connexion(Node debut, Node fin) {
        this.color = DEFAULT_COLOR ;
        this.debut = debut;
        this.fin = fin;
        this.debutNodeId = debut.getId();
        this.finNodeId = fin.getId();
        this.label = this.debut.getShortLabel()+" --> "+this.fin.getShortLabel();
    }


    public long getId() {
        return id;
    }

    public void setId(long Id) {
        this.id = Id;
    }


    public long getFinNodeId() {
        return finNodeId;
    }

    public long getDebutNodeId() {
        return debutNodeId;
    }

    public void setDebutNodeId(long debutNodeId) {
        this.debutNodeId = debutNodeId;
       // this.debut = MainActivity.nodeViewModel.getNodeById(debutNodeId) ;
    }

    public void setFinNodeId(long finNodeId) {
        this.finNodeId = finNodeId;
        //this.fin = MainActivity.nodeViewModel.getNodeById(finNodeId) ;
    }


    public Node getDebut() {
        //if(debut == null){ debut = MainActivity.nodeViewModel.getNodeById(debutNodeId);}
        return debut ;
    }

    public Node getFin() {

        //if(fin == null){ fin = MainActivity.nodeViewModel.getNodeById(finNodeId);}
        return fin ;
    }


    public void setFin(Node fin) {
        this.fin = fin;
        this.finNodeId  = fin.getId() ;
    }


    public void setDebut(Node debut) {
        this.debut = debut;
        this.debutNodeId = debut.getId() ;
    }



    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param node
     * @return true si node est concerné par la connexion
     */


    public boolean contains(Node node) {
        return node instanceof Node && (Node.overlap(debut, node) || Node.overlap(fin, node));
    }


    /**
     * @param node
     * @return true si node est le node de debut de la connexion
     */


    public boolean containsbegin(Node node) {
        return node instanceof Node && (Node.overlap(debut, node));
    }


    /**
     * @param n1
     * @param n2
     * @return true si les Nodes n1 et n2 sont reliés par la connexion
     */

    public boolean concerns(Node n1, Node n2) {
        return contains(n1) && contains(n2);
    }



    public String toString() {
        return debut.toString() + " --->  " + fin.toString();
    }


    public String details(){
        return debut.getShortLabel()+" --> "+fin.getShortLabel()+", label: "+label+", mid:("+getMidPointX()+","+getMidPointY()+")";
    }

    /**
     * Le path de la connexion
     * @return
     */

    public Path getPath(){
       Node node =  this.getDebut();
       final Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, 0);

       if ( node != null ) {
           float x1 = this.getDebut().getX();
           float x2 = this.getFin().getX();
           float y1 = this.getDebut().getY();
           float y2 = this.getFin().getY();
           path.moveTo(x1, y1);
           path.lineTo(x2, y2);
       }

        return path;
    }




    public float getMidPointX (){
        float mil = 0 ;
        Node node =  this.getDebut();

        if(node != null){
        float x1 = this.getDebut().getX();
        float x2 = this.getFin().getX();

        mil = (x1+x2)/2;
        }
        return  mil ;
    }


    public float getMidPointY (){
        float mil = 0 ;
        Node node =  this.getDebut();

        if(node != null){
        float y1 = this.getDebut().getY();
        float y2 = this.getFin().getY();

            mil = (y1+y2)/2 ;
        }
        return  mil ;
    }



    public boolean isSelected(float x,float y){

        float distanceX = Math.abs(this.getMidPointX() - x) ;
        float distanceY = Math.abs( this.getMidPointY() - y ) ;

        return (distanceX < SELECT_DISTANCE) && (distanceY < SELECT_DISTANCE);
    }

    /**
     * Affiche les traces d'une collection de connexions
     *
     * @param conns
     */

    public static void printConns(Collection<Connexion> conns) {
        for (Connexion conn : conns) {
            System.out.println(conn);
        }
    }
}
