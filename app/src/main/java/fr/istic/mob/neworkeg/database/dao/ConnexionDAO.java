package fr.istic.mob.neworkeg.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.istic.mob.neworkeg.modeles.Connexion;

@Dao
public interface ConnexionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Connexion Connexion);

    @Query("SELECT * FROM Connexion WHERE debutNodeId = :debut AND finNodeId =:fin")
    Connexion getConnexionByDF(long debut, long fin);

    @Query("SELECT * FROM Connexion")
    List<Connexion> getAllConnexions();

    @Update
    int update(Connexion Connexion);

    @Query("DELETE FROM Connexion WHERE id = :ConnexionId")
    int delete(long ConnexionId);

    @Query("DELETE FROM Connexion")
    int deleteAll();
}