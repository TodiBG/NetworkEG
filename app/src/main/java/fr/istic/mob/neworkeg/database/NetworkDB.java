package fr.istic.mob.neworkeg.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.istic.mob.neworkeg.database.dao.ConnexionDAO;
import fr.istic.mob.neworkeg.database.dao.NodeDAO;
import fr.istic.mob.neworkeg.modeles.Connexion;
import fr.istic.mob.neworkeg.modeles.Node;

@Database(entities = {Node.class, Connexion.class}, version = 1, exportSchema = false)
public abstract class NetworkDB extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile NetworkDB INSTANCE;
    private static final  String DBname = "DB.db";


    // --- DAO ---
    public abstract NodeDAO NodeDao();
    public abstract ConnexionDAO ConnexionDAO();

    // --- INSTANCE ---
    public static NetworkDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NetworkDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NetworkDB.class, DBname)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
