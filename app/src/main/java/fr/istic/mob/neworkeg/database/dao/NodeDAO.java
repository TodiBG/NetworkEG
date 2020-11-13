package fr.istic.mob.neworkeg.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.istic.mob.neworkeg.modeles.Connexion;
import fr.istic.mob.neworkeg.modeles.Node;

@Dao
public interface NodeDAO {



    @Query("SELECT * FROM Node WHERE id = :nodeId")
    Node getNodeById(long nodeId);

    @Query("SELECT * FROM Node ")
    List<Node> getAllNodes();

    @Query("SELECT * FROM Connexion WHERE debutNodeId = :nodeId OR debutNodeId = :nodeId")
    List<Connexion> getConnexionCollectionOf(long nodeId);

   @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Node Node);

    @Query("SELECT * FROM Node WHERE x = :x AND y =:y")
    Node getNodeByXY(float x, float y);


    @Update
    int update(Node Node);

    @Query("DELETE FROM Node WHERE id = :NodeId")
    int delete(long NodeId);

    @Query("DELETE FROM Node")
    int deleteAll();
}