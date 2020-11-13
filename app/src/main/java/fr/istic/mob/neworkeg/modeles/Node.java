package fr.istic.mob.neworkeg.modeles;

import android.graphics.Color;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Collection;
import java.util.List;
import java.util.Random;

@Entity
public class Node {

    @PrimaryKey(autoGenerate = true) private long id ;
    @ColumnInfo(name = "x") private float x;
    @ColumnInfo(name = "y") private float y;
    @ColumnInfo(name = "label") private String label;
    @ColumnInfo(name = "labelsize") private int labelSize;
    @ColumnInfo(name = "color") private int color;
    @ColumnInfo(name = "width") private float width;

    public static int DEFAULT_COLOR = Color.BLUE;
    public static int DEFAULT_RADIUS = 45;
    public static String DEFAULT_LABEL = "";

    public long getId(){ return this.id ;}
    public void setId(long Id){this.id = Id ;}

    @Ignore
    List<Connexion> listConnexion = null;

    public Node(long id, float x, float y, String label, int color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.label = label;
        this.color = color;
        this.labelSize = labelSize ;
        setRadiaus();
    }

    @Ignore
    public Node(float x, float y) {
        this.x = x;
        this.y = y;
        this.color = DEFAULT_COLOR;
        this.label = DEFAULT_LABEL;
        setRadiaus();
    }

    @Ignore
    public Node(float x, float y, int color, String label) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.label = label;
        setRadiaus();
    }

    @Ignore
    public Node(float x, float y, String label) {
        this.x = x;
        this.y = y;
        this.color = DEFAULT_COLOR;
        this.label = label;
        setRadiaus();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float Y) {
        this.y = Y;
    }

    public String getLabel() {
        return this.label;
    }

    public int getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(int size) {
        this.labelSize = size;
    }

    public String getShortLabel() {
        if (this.getLabel().length()<5){
            return this.getLabel();
        }else {
            return this.getLabel().substring(0,4)+"...";
        }
    }

    public void setLabel(String label) {
        this.label = label;
        setRadiaus();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this. width = width;
    }

    /**
     * definir la taille de l'objet en fonction son nom
     */
    public void setRadiaus() {
        this.width = DEFAULT_RADIUS ;

       /*
        this.width = DEFAULT_RADIUS * quelqueChose ;
       */
    }

    /**
     * Met les coodonnées d'un Node à jour
     *
     * @param x
     * @param y
     */
    public void upadte(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Met un Node à jour
     *
     * @param x
     * @param y
     * @param color
     */
    public void upadte(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Met un Node à jour
     *
     * @param x
     * @param y
     * @param label
     * @param color
     */
    public void upadte(int x, int y, String label, int color) {
        this.x = x;
        this.y = y;
        this.label = label;
        this.color = color;
        setRadiaus();
    }


     // Vérifier si un objet(noeud)  est trop proche de l'objet courant

    public boolean overlap(Node n) {
        float margY = Math.abs(y - n.getY());
        float margX = Math.abs(x - n.getX());

        if(x==n.getX() && y == n.getY() ){return true ;}

        return (margX < (this.getWidth()) + (n.getWidth())) && (margY < (this.getWidth()) + (n.getWidth()));
    }


     // Vérifier si deux objets sont trop proches

    public static boolean overlap(Node n1, Node n2) {
        return n1.overlap(n2);
    }


     // Verifier si un objet du graphe est trop proche d'une position

    public boolean isClose(float x, float y) {
        return this.overlap(new Node(x, y));
    }

    /**
     * @param max
     * @return une coordonnée comprise entre 100 et max-100 de manière aléatoire
     */
    public static int getRandomCoord(int max) {
        Random rand = new Random();
        int x = rand.nextInt(max) + 1 - 100;
        return 100 + (x / DEFAULT_RADIUS) * DEFAULT_RADIUS;
    }

    /**
     * transformer l'objet en String
     */
    public String toString() {
        return "Node => { id:"+id+", position:(" + x + "," + y + "), label:" + label + ", color:" + color + "}";
    }

    /**
     * Affiche une collection de nodes
     *
     * @param nodes
     */
    public static void printNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            System.out.println(node);
        }
        System.out.println(nodes.size() + " nodes -----------------");
    }
}