package fr.istic.mob.neworkeg.database.dao;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import fr.istic.mob.neworkeg.database.NetworkDB;
import fr.istic.mob.neworkeg.modeles.Connexion;
import fr.istic.mob.neworkeg.modeles.Node;

public class NodeViewModel  extends AndroidViewModel {
    private NodeDAO nodeDAO ;
    private NetworkDB dataBase ;
    private ConnexionDAO connexionDAO ;

    public NodeViewModel (Application application){
        super(application);
        dataBase = NetworkDB.getInstance(application);
        nodeDAO = dataBase.NodeDao() ;
        connexionDAO = dataBase.ConnexionDAO() ;
    }

    public long insert(Node node){
        this.nodeDAO.insert(node) ;
        System.out.println("Noeud inseré !! ");
        return this.nodeDAO.getNodeByXY(node.getX(),node.getY()).getId() ;
    }

    public void update(Node node){
        this.nodeDAO.update(node) ;
    }

    public void delete(long Id){
        this.nodeDAO.delete(Id) ;
    }
    public void deleteAll(){
        this.connexionDAO.deleteAll() ;
        this.nodeDAO.deleteAll() ;
    }

    public  Node getNodeById(long Id){
       return nodeDAO.getNodeById(Id) ;
    }

    public List<Connexion> getConnexionCollection(long nodeId){
        return  nodeDAO.getConnexionCollectionOf(nodeId) ;
    }

    public List<Node> getAllNodes(){
        return  nodeDAO.getAllNodes() ;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

}




