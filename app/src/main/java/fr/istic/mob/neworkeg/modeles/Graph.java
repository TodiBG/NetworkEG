package fr.istic.mob.neworkeg.modeles;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class Graph {

    public int mMaxX = 600;
    public int mMaxY = 800;
    private List<Node> mNodes = new ArrayList<Node>();/*Les nodes du graphe*/
    //Les connexions qui reliant les nodes
    private List<Connnexion> mConns = new ArrayList<Connnexion>();
    public static List<Integer> mNodeColors = new ArrayList<>();/*Les couleurs possibles pour les nodes*/

    /**
     * Constructeur avec le nombre de nodes et les dimensions initiales
     *
     * @param n
     * @param screnH
     * @param screnW
     */
    public Graph(int n, int screnH, int screnW) {
        initmNodeColors();
        mMaxX = screnH;
        mMaxY = screnW;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int x = Node.getRandomCoord(mMaxX);
            int y = Node.getRandomCoord(mMaxY);
            Node node = new Node(x, y);
            node = new Node(y, x);
            node.setColor(getRandomColor());
            int num = getNoeuds().size() + 1;
            node.setLabel("" + num);
            boolean add = addNode(node);
            while (!add) {
                x = Node.getRandomCoord(mMaxX);
                y = Node.getRandomCoord(mMaxY);
                node = new Node(x, y);
                node.setColor(getRandomColor());
                num = getNoeuds().size() + 1;
                node.setLabel("" + num);
                add = addNode(node);
            }
        }
    }

    /**
     * Constructeur avec le nombre de nodes de depart
     *
     * @param n
     */
    public Graph(int n) {
        initmNodeColors();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int x = Node.getRandomCoord(mMaxX);
            int y = Node.getRandomCoord(mMaxY);
            Node node = new Node(x, y);

            node.setColor(getRandomColor());
            int num = getNoeuds().size() + 1;
            node.setLabel("" + num);
            boolean add = addNode(node);
            while (!add) {
                x = Node.getRandomCoord(mMaxX);
                y = Node.getRandomCoord(mMaxY);
                node = new Node(x, y);
                node.setColor(getRandomColor());
                num = getNoeuds().size() + 1;
                node.setLabel("" + num);
                add = addNode(node);
            }
        }
    }

    /**
     * Initialise les couleurs disponibles
     */
    public void initmNodeColors() {
        mNodeColors.add(Color.BLUE);
        mNodeColors.add(Color.CYAN);
        mNodeColors.add(Color.DKGRAY);
        mNodeColors.add(Color.RED);
        mNodeColors.add(Color.GRAY);
        mNodeColors.add(Color.GREEN);
        mNodeColors.add(Color.MAGENTA);
        mNodeColors.add(Color.LTGRAY);
        mNodeColors.add(Color.YELLOW);
    }

    /**
     * @return ArrayList de tous les nodes
     */
    public List<Node> getNoeuds() {
        return mNodes;
    }

    /**
     * @return ArrayList de toutes les connexions
     */
    public List<Connnexion> getConns() {
        return mConns;
    }

    /**
     * @return La liste de toutes couleurs disponibles
     */
    public List<Integer> getNodeColors() {
        return mNodeColors;
    }

    /**
     * @return Une couleur de manière aléatoire
     */
    public static int getRandomColor() {
        Random rand = new Random();
        return mNodeColors.get(rand.nextInt(mNodeColors.size()));
    }

    /**
     * Ajoute un nouveau Node au graphe quand c'est possible
     *
     * @param node
     * @return true quand le Node est ajouté
     */
    public boolean addNode(Node node) {
        boolean overlap = false;
        Iterator<Node> i = mNodes.iterator();
        baka:
        while (i.hasNext()) {
            Node n = i.next();
            if (Node.overlap(n, node)) {
                overlap = true;
                break baka;
            }
        }
        if (!overlap) {
            this.mNodes.add(node);
        }
        return !overlap; // prouve que le node à bien de l'espace
    }

    /**
     * Retourne le node selectionné ou null
     *
     * @param x
     * @param y
     * @return
     */
    public Node getSelectedNode(int x, int y) {
        Node node = new Node(x, y);
        Node foundNode = null ;
        Iterator<Node> i = mNodes.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (Node.overlap(n, node)) {
                foundNode = n;
            }
        }
        return foundNode ;
    }

    /**
     * Ajoute un nouveau Node quand c'est possible
     *
     * @param x
     * @param y
     * @return true quand le Node est ajouté
     */
    public boolean addNode(int x, int y) {
        Node node = new Node(x, y);
        boolean overlap = false;
        Iterator<Node> i = mNodes.iterator();
        baka:
        while (i.hasNext()) {
            Node n = i.next();
            if (Node.overlap(n, node)) {
                overlap = true;
                break baka;
            }
        }
        if (!overlap) {
            this.mNodes.add(node);
        }
        return !overlap; // prouve que le node à bien de l'espace
    }

    /**
     * Supprime un Node du graphe en suppriment aussi toutes les connexions qui lui sont reliés
     *
     * @param node
     */
    public void removeNode(Node node) {
        try {
            removeNodeConns(node);
            mNodes.remove(node);
        } catch (ConcurrentModificationException e) {
            removeNode(node); // passe 1 fois sur 4 donc executé plusieurs fois par appel
        }
    }

    /**
     * Supprimes toutes les connexions dans lesquelles un node est impliqué
     *
     * @param node
     */
    public void removeNodeConns(Node node) {
        for (Connnexion conn : mConns) {
            if (conn.contains(node)) {
                mConns.remove(conn);
            }
        }
    }

    /**
     * Supprimer le Node à la position nodeIndex du graphe en supprimant aussi toutes les connexions qui lui sont reliées
     *
     * @param nodeIndex
     */
    public void removeNode(int nodeIndex) {
        if (nodeIndex < mNodes.size()) {
            Node node = mNodes.get(nodeIndex);
            removeNodeConns(node);
            mNodes.remove(node);
        }
    }

    /**
     * Ajoute un connexion
     *
     * @param conn
     */
    public void addConn(Connnexion conn) {
        if (!Node.overlap(conn.getDebut(), conn.getFin()))
            this.mConns.add(conn);
    }

    /**
     * Ajoute une connexion entre les nodes index1 et index2 de la liste de nodes
     *
     * @param index1
     * @param index2
     */
    public void addConn(int index1, int index2) {
        if (index1 != index2) {
            Node n1 = getNoeuds().get(index1);
            Node n2 = getNoeuds().get(index2);
            if (!Node.overlap(n1, n2)) {
                Log.d("XXX", "add conn ");
                this.mConns.add(new Connnexion(n1, n2));
            }
        }

    }

    /**
     * Retourne la connexion selectionnée ou null
     *
     * @param x
     * @param y
     * @return conn
     */
    public Connnexion getSelectedConn(int x, int y) {
        ListIterator<Connnexion> it = mConns.listIterator(mConns.size());
        while (it.hasPrevious()) {
            Connnexion conn = it.previous();
            if (conn.isSelected(x,y)) {
                return conn;
            }
        }
        return null;
    }

    /**
     * Supprime une connexion du graphe
     *
     * @param conn
     */
    public void removeConn(Connnexion conn) {
        mConns.remove(conn);
    }

    /**
     * Supprime la connexion à la position connIndex des connexions du graphe
     *
     * @param connIndex
     */
    public void removeConn(int connIndex) {
        mConns.remove(getConns().get(connIndex));
    }

    /**
     * Retoune l'index d'un Node de manière aléatoire de la liste de nodes
     *
     * @return un index
     */
    public int getRandomNodeIndex() {
        Random rand = new Random();
        int x = rand.nextInt(getNoeuds().size());
        return x;
    }

    /**
     * Ajoute n connexion de manière aléatoire (n<=nombre de nodes)
     */
    public void addRandomConns() {
        for (int i = 0; i < mNodes.size(); i++) {
            addConn(getRandomNodeIndex(), getRandomNodeIndex());
        }
    }

    /**
     * Ajoute n connexions de manière aléatoire (n<=nombre de nodes)
     *
     * @param n si n > nombre de nodes il est ignoré, addRandomConns() est executé
     */
    public void addRandomConns(int n) {
        if (n < mNodes.size()) {
            for (int i = 0; i < n; i++) {
                addConn(getRandomNodeIndex(), getRandomNodeIndex());
            }
        } else {
            addRandomConns();
        }
    }


    /**
     * Retourne toutes les connexions dont le Node en parametre en le debut
     *
     * @param nodeBegin si nodeBegin n'est lie a aucun autre node on retourne une liste vide
     */

    public List<Connnexion> getConnOutOfMe(Node nodeBegin) {
        List<Connnexion> connOutOfMe = new ArrayList<Connnexion>();
        int i = 0;
        int e = 0;
        for (Connnexion conn : mConns) {
            if (conn.containsbegin(nodeBegin))  {
                connOutOfMe.add(conn);
                e++;
            }
            i++;
        }
        return connOutOfMe;
    }


}
