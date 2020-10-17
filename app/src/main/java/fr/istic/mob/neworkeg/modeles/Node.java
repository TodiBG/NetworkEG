package fr.istic.mob.neworkeg.modeles;

import android.graphics.Color;

import java.util.Collection;
import java.util.Random;

public class Node {
    private int x;
    private int y;
    private String mLabel;
    private int mColor;
    private int width;

    public static int DEFAULT_COLOR = Color.BLUE;
    public static int DEFAULT_RADIUS = 45;
    public static int CHAR_LENGTH = 6;
    public static String DEFAULT_ETIQ = "";

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.mColor = DEFAULT_COLOR;
        this.mLabel = DEFAULT_ETIQ;
        setRadiaus();
    }

    public Node(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.mColor = color;
        this.mLabel = DEFAULT_ETIQ;
        setRadiaus();
    }

    public Node(int x, int y, String label) {
        this.x = x;
        this.y = y;
        this.mColor = DEFAULT_COLOR;
        this.mLabel = label;
        setRadiaus();
    }

    public Node(int x, int y, String label, int color) {
        this.x = x;
        this.y = y;
        this.mLabel = label;
        this.mColor = color;
        setRadiaus();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public String getShortLabel() {
        if (this.getLabel().length()<5){
            return this.getLabel();
        }else {
            return this.getLabel().substring(0,4)+"...";
        }
    }

    public void setLabel(String label) {
        this.mLabel = label;
        setRadiaus();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Le diamètre en fonction de l'etiquete
     */
    public void setRadiaus() {
        int length = this.mLabel.length();
        if (length < 4) {
            this.width = DEFAULT_RADIUS;
        } else if (length <= 10) {
            this.width = DEFAULT_RADIUS + length * length;
        } else if (length <= 25) {
            this.width = DEFAULT_RADIUS + length * length - length * (length / 2) - length;
        } else {
            this.width = DEFAULT_RADIUS + length * length - length * (length / 2) - length * 10;
        }

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
        this.mColor = color;
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
        this.mLabel = label;
        this.mColor = color;
        setRadiaus();
    }


     // Vérifier si un objet(noeud)  est trop proche de l'objet courant

    public boolean overlap(Node n) {
        int margY = Math.abs(y - n.getY());
        int margX = Math.abs(x - n.getX());
        return (margX < (this.getWidth()) + (n.getWidth())) && (margY < (this.getWidth()) + (n.getWidth()));
    }


     // Vérifier si deux objets sont trop proches

    public static boolean overlap(Node n1, Node n2) {
        return n1.overlap(n2);
    }


     // Verifier si un objet du graphe est trop proche d'une position

    public boolean isClose(int x, int y) {
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
        return "Node : {(" + x + "," + y + ")," + mLabel + ", color:" + mColor + "}";
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