package fr.istic.mob.neworkeg.database.dao;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import fr.istic.mob.neworkeg.database.NetworkDB;
import fr.istic.mob.neworkeg.modeles.Connexion;

public class ConnexionViewModel extends AndroidViewModel {
    private ConnexionDAO ConnexionDAO ;
    private NetworkDB dataBase ;

    public ConnexionViewModel(Application application){
        super(application);
        dataBase = NetworkDB.getInstance(application);
        ConnexionDAO = dataBase.ConnexionDAO() ;
    }

    public long insertOrUpdate(Connexion Connexion){
        if(Connexion.getId() == 0) {
            this.ConnexionDAO.insert(Connexion);
        }else {
            this.ConnexionDAO.update(Connexion);
        }
        return this.ConnexionDAO.getConnexionByDF(Connexion.getDebutNodeId(),Connexion.getFinNodeId()).getId() ;
    }

    public void delete(long Id){
        this.ConnexionDAO.delete(Id) ;
    }
    public void deleteAll(){
        this.ConnexionDAO.deleteAll() ;
    }

    public void update(Connexion Connexion){
        this.ConnexionDAO.update(Connexion) ;
    }

   /* public  Connexion getConnexionById(long Id){
       return ConnexionDAO.getConnexionById(Id) ;
    }*/

    public List<Connexion> getAllConnexions(){
        return  ConnexionDAO.getAllConnexions() ;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

}




