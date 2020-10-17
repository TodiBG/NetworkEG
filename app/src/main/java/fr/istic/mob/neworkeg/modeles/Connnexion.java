package fr.istic.mob.neworkeg.modeles;

import android.graphics.Path;

import java.util.Collection;


public class Connnexion {
    private static final int SELECT_DISTANCE = 20;
    private Node mDebut;
    private Node mFin;
    private int mColor;
    private String mLabel;
    private float [] mMidPoint;

    public static int CONN_WIDTH= 10;


    public Connnexion(Node debut, Node fin) {
        this.mColor = debut.getColor();
        this.mDebut = debut;
        this.mFin = fin;
        this.mColor = debut.getColor();
        this.mLabel = this.mDebut.getShortLabel()+" --> "+this.mFin.getShortLabel();
        mMidPoint = new float[]{0,0};
        setMidPoint();
    }

    public Node getDebut() {
        return mDebut;
    }

    public Node getFin() {
        return mFin;
    }

    public void setFin(Node fin) {
        this.mFin = fin;
    }


    public void setDebut(Node debut) {
        mDebut = debut;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    /**
     * @param node
     * @return true si node est concerné par la connexion
     */

    public boolean contains(Node node) {
        return node instanceof Node && (Node.overlap(mDebut, node) || Node.overlap(mFin, node));
    }


    /**
     * @param node
     * @return true si node est le node de debut de la connexion
     */

    public boolean containsbegin(Node node) {
        return node instanceof Node && (Node.overlap(mDebut, node));
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
        return mDebut.toString() + " --->  " + mFin.toString();
    }

    public String details(){
        return mDebut.getShortLabel()+" --> "+mFin.getShortLabel()+", label: "+mLabel+", mid:("+mMidPoint[0]+","+mMidPoint[1]+")";
    }

    /**
     * Le path de la connexion
     * @return
     */
    public Path getPath(){
        setMidPoint();
        int x1 = this.getDebut().getX();
        int x2 = this.getFin().getX();
        int y1 = this.getDebut().getY();
        int y2 = this.getFin().getY();
        final Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        return path;
    }

    public void setMidPoint (){
        int x1 = this.getDebut().getX();
        int x2 = this.getFin().getX();
        int y1 = this.getDebut().getY();
        int y2 = this.getFin().getY();

        float pointX = (x1+x2)/2;
        float pointY = (y1+y2)/2 ;
        mMidPoint[0] = pointX;
        mMidPoint[1] = pointY;
    }


    public boolean isSelected(int x,int y){
        return (this.mMidPoint[0] - x < SELECT_DISTANCE) && (this.mMidPoint[1] - x < SELECT_DISTANCE);
    }

    /**
     * Affiche les traces d'une collection de connexions
     *
     * @param conns
     */
    public static void printConns(Collection<Connnexion> conns) {
        for (Connnexion conn : conns) {
            System.out.println(conn);
        }
        System.out.println(conns.size() + " items -----------------");
    }
}
