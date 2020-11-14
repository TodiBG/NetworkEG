package fr.istic.mob.neworkeg.modeles;

import android.graphics.Color;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

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
    private List<Connexion> mConns = new ArrayList<Connexion>();
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
     * Constructeur avec la liste de noeud de depart
     *
     * @param nodeList provenant de la base données
     */
    public Graph(List<Node> nodeList ) {
        this.mNodes = nodeList ;
        System.out.println(mNodes);

       /* List<Connexion> connexionsList = MainActivity.connexionViewModel.getAllConnexions() ;
        Connexion nc = null ;
        for (Connexion c: connexionsList){
            Node n1 =  MainActivity.nodeViewModel.getNodeById(c.getDebutNodeId()) ;
            Node n2 =  MainActivity.nodeViewModel.getNodeById(c.getFinNodeId()) ;
            nc = new Connexion(n1,n2) ;
            nc.setColor(c.getColor());
            addConn(nc) ;
        }

        */
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
    public List<Connexion> getConns() {
        return mConns;
    }
    public void setConns(List<Connexion> connexions) {
         mConns = connexions;
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

            //long id= MainActivity.nodeViewModel.insert(node);
            int lastId = mNodes.size() ;
            if(lastId == 0){node.setId(1);}
            else{
                node.setId(mNodes.get(lastId-1).getId()+1);
            }

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
    public Node getSelectedNode(float x, float y) {
        Node node = new Node(x, y);
        Node foundNode = null ;
        Iterator<Node> i = mNodes.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (Node.overlap(n, node)) {
                foundNode = n;
            }

            System.out.println("X: "+x+"---> "+n.getX());
            System.out.println("Y: "+y+"---> "+n.getY());
        }


        return foundNode ;
    }

    public Node getNodeById(long Id) {
        Node foundNode = null;
        Iterator<Node> i = mNodes.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (n.getId() == Id) {
                foundNode = n;
            }

            //System.out.println("X: "+x+"---> "+n.getX());
            //System.out.println("Y: "+y+"---> "+n.getY());
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



        System.out.println("Node Id ==   "+node.getId());




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
        for (Connexion conn : mConns) {
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
    public void addConn(Connexion conn) {
        if (!Node.overlap(conn.getDebut(), conn.getFin())){
            //long id = MainActivity.connexionViewModel.insertOrUpdate(conn);
            int lastId = mConns.size() ;
            if(lastId == 0){
                conn.setId(1);
            }else {
                conn.setId( mConns.get(lastId-1).getId()+1 );
            }
            this.mConns.add(conn);
        }
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
                this.mConns.add(new Connexion(n1, n2));
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
    public Connexion getSelectedConn(float x, float y) {
        ListIterator<Connexion> it = mConns.listIterator(mConns.size());
        while (it.hasPrevious()) {
            Connexion conn = it.previous();
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
    public void removeConn(Connexion conn) {
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
     * Retourne toutes les connexions dont le Node en parametre est le debut
     *
     * @param nodeBegin si nodeBegin n'est lie a aucun autre node on retourne une liste vide
     */

    public List<Connexion> getConnOutOfMe(Node nodeBegin) {
        List<Connexion> connOutOfMe = new ArrayList<Connexion>();
        int i = 0;
        int e = 0;
        for (Connexion conn : mConns) {
            if (conn.containsbegin(nodeBegin))  {
                connOutOfMe.add(conn);
                e++;
            }
            i++;
        }
        return connOutOfMe;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String export()  {

        ArrayMap data  = new  ArrayMap() ;
        data.put("nodes",mNodes) ;
        data.put("connexions",mConns) ;
        return new Gson().toJson(data) ;
    }

}
