package fr.istic.mob.neworkeg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.neworkeg.modeles.Connexion;
import fr.istic.mob.neworkeg.modeles.DrawableGraph;
import fr.istic.mob.neworkeg.modeles.Fonction;
import fr.istic.mob.neworkeg.modeles.Graph;
import fr.istic.mob.neworkeg.modeles.Node;
import fr.istic.mob.neworkeg.modeles.Stockeur;
import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {
    private List<Node> nodeList ;
    public DrawableGraph myDraw;
    public static ImageView supportView;
    private TextView networkNameTextView  ;
    private Drawable backgroundImage = null ;

    int downx = 0;
    int downy = 0;
    int upx = 0;
    int upy = 0;
    int umpx = 0;
    int umpy = 0;

    final static long LONG_TOUCH_DURATION = 700;
    long touchStartTime = 0;
    public static boolean  optionPopupNodeVisible = false;
    public static boolean  optionPopupConnVisible = false;
    public static Node mNewNode ;
    public Node selectedNode = null;
    public static Connexion selectedConn = null;
    public static Graph mGraph;
    int mMode = 0 ;

    public static  String NETWORK_NAME = "";
    private static final String FOLDERNAME = "Sauvegardes";
    public static final String TEMP_FOLDER = "temp";
    private static final String TEMP_FILE = "temp_network";
    private static final int IMAGE_PICKER_CODE =1000;
    private static final int IMAGE_PICKER_PERMISSION_CODE =1010;
    private static final int MY_REQUEST_CODE_PERMISSION = 1020;
    private static final int MY_RESULT_CODE_FILECHOOSER = 1030;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkNameTextView = (TextView)findViewById(R.id.networt_name);
        networkNameTextView.setText(NETWORK_NAME);
        supportView = (ImageView) findViewById(R.id.imageViewPlan);

        if (backgroundImage != null){ supportView.setBackground(backgroundImage); }
        else { supportView.setBackgroundResource(R.mipmap.plan_portrait);}

        nodeList = new ArrayList<>() ;
        List<Connexion> connexionsList = new ArrayList<>() ;
        mGraph = new Graph(nodeList);
        myDraw = new DrawableGraph(mGraph);
        supportView.setImageDrawable(myDraw);
        supportView.setOnTouchListener(mModeLecture );
        updateGraph(connexionsList) ;

        if (savedInstanceState != null){

            readFromStorage(TEMP_FOLDER,TEMP_FILE+".json");
            NETWORK_NAME = savedInstanceState.getString("tempNetwork") ;
            networkNameTextView.setText(savedInstanceState.getString("tempNetwork"));

            mMode = savedInstanceState.getInt("Mode") ;
            if( mMode == 1){ supportView.setOnTouchListener(mModeLecture ); }
            else if(mMode == 2){ supportView.setOnTouchListener(mModeCreation ); }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {

        writeOnExternalStorage(TEMP_FOLDER, TEMP_FILE) ;

        savedInstanceState.putInt("Mode",mMode);
        savedInstanceState.putString("tempNetwork", networkNameTextView.getText().toString() );
        super.onSaveInstanceState(savedInstanceState);
    }



    private void initReseau(){

        nodeList = new ArrayList<Node>() ;
        mGraph = new Graph(nodeList);
        myDraw = new DrawableGraph(mGraph);
        supportView.setImageDrawable(myDraw);
        supportView.setOnTouchListener(mModeLecture );
    }

    //Desine les connexions sauvegardées dans la base de donnéeskl
    private void updateGraph(List<Connexion> connexionsList){
        //List<Connexion> connexionsList = MainActivity.connexionViewModel.getAllConnexions() ;
        Node debut ;
        Node fin ;
        Node n1 ;
        Node n2 ;
        for (Connexion c: connexionsList){
            //debut =  MainActivity.nodeViewModel.getNodeById(c.getDebutNodeId()) ;
            //fin =  MainActivity.nodeViewModel.getNodeById(c.getFinNodeId()) ;

            n1 = mGraph.getNodeById(c.getDebutNodeId());
            n2 = mGraph.getNodeById(c.getFinNodeId());
            if ((n1 != null) && (n2 != null)) {
                Connexion a = new Connexion(n1, n2);
                a.setColor(c.getColor()) ;
                mGraph.addConn(a);
                supportView.invalidate();
            }
        }

        myDraw = new DrawableGraph(mGraph);
        supportView.setImageDrawable(myDraw);
        supportView.setOnTouchListener(mModeLecture );
    }



    public void createNetwork(){
        networkNameTextView.setText(NETWORK_NAME);
        myDraw = new DrawableGraph(mGraph);
        supportView.setImageDrawable(myDraw);
        supportView.setOnTouchListener(mModeCreation );
        mMode = 2 ;
    }

    public void  lecture (){
        supportView.setOnTouchListener(mModeLecture);
        networkNameTextView.setText(NETWORK_NAME);
        Toast.makeText(this,R.string.reading_mode_info,Toast.LENGTH_LONG).show();
    }


    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    /**
     * Réagir au clic des elements de menu
     * */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_read: {
                lecture () ;
                break;
            }
            case R.id.item_edit : {
                if( networkNameTextView.getText().toString().equals("")) {
                    CreateNetworkDialog dialog = new CreateNetworkDialog(this);
                    dialog.show();
                }
                else{  NETWORK_NAME = networkNameTextView.getText().toString() ;
                    createNetwork() ;
                    Toast.makeText(this,R.string.editting_mode_info,Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.item_reset : {
                // Réinitialiser le reseau
                initReseau() ;
                Toast.makeText(this,R.string.resetting_mode_info,Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.item_save : {
                 checkPermissionAndWrite();
                break;
            }
            case R.id.item_import : {
                askPermissionToReadIntent() ;
                break;
            }
            case R.id.item_import_plan : {
                askPermissionForImagePicker() ;
                break;
            }
            case R.id.item_screemshot : {
                String dirPath  = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/MyScreeshot" ;
                Uri uriToScreenshotImge = Fonction.takeScreenshot(this, getWindow().getDecorView().getRootView(),dirPath,"screenshot");
                shareScreenshot(uriToScreenshotImge);

                break;
            }
            case R.id.item_help : {
                OptionDialogHelp optionDialogHelp = new OptionDialogHelp(this);
                optionDialogHelp.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private  void creationAndLectureInfo(int message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }


    // Basculer en mode lecture
    private View.OnTouchListener mModeLecture = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mMode = 1 ;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downx = (int) event.getX();
                    downy = (int) event.getY();
                    selectedNode = mGraph.getSelectedNode(downx, downy);
                    touchStartTime = System.currentTimeMillis();

                    break;
                }
                case MotionEvent.ACTION_UP: {
                    upx = (int) event.getX();
                    upy = (int) event.getY();
                    Long time = System.currentTimeMillis() - touchStartTime;
                    if ( ((Math.abs(upx - downx) < 10) || (Math.abs(upy - downy) < 10)) && (time>=LONG_TOUCH_DURATION) ) {
                        creationAndLectureInfo(R.string.passOnCreationMode) ;
                    }

                    break;}
                case MotionEvent.ACTION_MOVE:{
                    umpx = (int) event.getX();
                    umpy = (int) event.getY();
                    selectedNode = mGraph.getSelectedNode(umpx, umpy);
                    if ( selectedNode != null ) {
                        selectedNode.upadte(umpx, umpy);
                    }
                    supportView.invalidate();
                    break ;
                }
                case MotionEvent.ACTION_CANCEL:{break;}

            }
            return true;
        }
    } ;

    // Bacukler en mode création
    private View.OnTouchListener mModeCreation = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            mMode = 2 ;
            long time = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downx = (int) event.getX();
                    downy = (int) event.getY();
                    touchStartTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    myDraw.setTempConnNull();
                    upx = (int) event.getX();
                    upy = (int) event.getY();

                    boolean newNodeCreated = false ;
                    time = System.currentTimeMillis() - touchStartTime;
                    if ( ((Math.abs(upx - downx) < 10) || (Math.abs(upy - downy) < 10)) && (time>=LONG_TOUCH_DURATION) ) {
                        int number = 1 + myDraw.mGraph.getNoeuds().size();
                        mNewNode = new Node(upx, upy, number + "");
                        newNodeCreated = true ;
                        showNewNodeOptions();
                        supportView.invalidate();
                    }

                    if ( ((upx == downx) || (upy == downy)) && (time>=LONG_TOUCH_DURATION-800)&&(!newNodeCreated)  ) {

                        Node currentNode = mGraph.getSelectedNode(downx, downy);
                        if ((!optionPopupConnVisible && !optionPopupNodeVisible)&&(currentNode != null)) {
                            optionPopupNodeVisible = true;
                            selectedNode = currentNode;
                            showOptions();
                        }else {
                            Connexion currentConn = mGraph.getSelectedConn(downx, downy);
                            if ( (currentConn != null)&&(!optionPopupConnVisible && !optionPopupNodeVisible) ){
                                optionPopupConnVisible = true;
                                selectedConn = currentConn ;
                                showOptionsConn();
                            }
                        }
                    }


                    Node n1 = mGraph.getSelectedNode(downx, downy);
                    Node n2 = mGraph.getSelectedNode(upx, upy);
                    if ((n1 != null) && (n2 != null)) {
                        Connexion a = new Connexion(n1, n2);
                        mGraph.addConn(a);
                        supportView.invalidate();
                    }
                    else if (n1 != null) {
                        supportView.invalidate();
                        creationAndLectureInfo(R.string.objetNonDeplacableEnModeCreation) ;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    umpx = (int) event.getX();
                    umpy = (int) event.getY();

                    Node startNode = mGraph.getSelectedNode(downx, downy);
                    Node tempNode = new Node(umpx, umpy);

                    if ((startNode != null) && (tempNode != null)) {
                        Connexion tempConn = new Connexion(startNode, tempNode);
                        myDraw.setTempConn(tempConn);
                        supportView.invalidate();
                    }

                    break ;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        }
    };




    /**
     * Couleur des objets
     */

    public void showNodeColorPopup() {

        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                if( (Integer)color != 0) {
                    selectedNode.setColor(color);
                }
            }

            @Override
            public void onCancel(){    }
        });


        supportView.invalidate();
    }


    /**
     * Couleur de connexions
     */


    public void showColorPopupConn() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                if( (Integer)color != 0) {selectedConn.setColor(color);
                }
            }
            @Override
            public void onCancel(){    }
        });
        supportView.invalidate();
    }

    public void showNewNodeColorPop() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                if( (Integer)color != 0) {mNewNode.setColor(color);
                }
            }
            @Override
            public void onCancel(){    }
        });
        supportView.invalidate();
    }

    public void showNodeDefaultColorPopup() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                if( (Integer)color != 0) {
                    Node.DEFAULT_COLOR = color ;
                }
            }

            @Override
            public void onCancel(){    }
        });
    }

    public void showConnDefaultColorPopup() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                if( (Integer)color != 0) {
                    Connexion.DEFAULT_COLOR = color ;
                }
            }

            @Override
            public void onCancel(){    }
        });
    }
    public void showNewNodeOptions() {
        CreateNodeDialog dialog  = new CreateNodeDialog(this);
        dialog.show();

    }

    public void showOptions() {
        OptionDialogNode optionDialog = new OptionDialogNode(this);
        optionDialog.show();

    }

    public void showOptionsConn() {
        OptionDialogConn optionDialogConn = new OptionDialogConn(this);

        optionDialogConn.show();

    }

    public List<Connexion> getSelectConn() {
        return mGraph.getConnOutOfMe(selectedNode);
    }

    private void askPermissionToReadIntent(){
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // Check if we have Call permission
            int permisson = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_REQUEST_CODE_PERMISSION);
                return;
            }
        }
        this.doBrowseFile();
    }

    private void doBrowseFile()  {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == MY_RESULT_CODE_FILECHOOSER && resultCode == MainActivity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            String uri = "";
            if (resultData != null) {
                uri = resultData.getData().getPath() ;
                String g =  java.io.File.separator ;
                String[]strings = uri.split(g) ;
                int taille = strings.length ;
                String fileName = strings[taille-1] ;

                taille = fileName.length();
                if(taille>5){
                    networkNameTextView.setText(fileName.substring(0,taille-5) );
                }

                readFromStorage(FOLDERNAME,fileName);
            }
        }else if (resultCode== RESULT_OK && requestCode==IMAGE_PICKER_CODE){

            Uri uri = resultData.getData();

            Drawable bg;
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bg = Drawable.createFromStream(inputStream, uri.toString());
                supportView.setBackground(bg);
            } catch (FileNotFoundException e) { e.getMessage(); }

        }
    }


    // 2 - Read from storage
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readFromStorage(String folderName, String fileName)  {

        if (Stockeur.isExternalStorageReadable()){
            String data = Stockeur.getDataFromStorage(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), this, folderName, fileName);
            JSONParser parser = new JSONParser();
            JSONObject  dataJson = null;
            try {    dataJson = (JSONObject) parser.parse(data);         } catch (ParseException e) { e.printStackTrace(); }
            JSONArray  nodesJson = (JSONArray) dataJson.get("nodes");
            JSONArray  connexionsJson = (JSONArray) dataJson.get("connexions");

            List<Node> nodes = new ArrayList<Node>();
            JSONObject o = null;
            long id = 0; float x = 0 ; float y = 0; String label = "" ; int color = 0 ; int labelsize = 0 ;

            for (int i =0;i<nodesJson.size();i++){
                o = (JSONObject) nodesJson.get(i) ;
                id = Long.parseLong(o.get("id").toString());
                x = Float.parseFloat(o.get("x").toString()) ;
                y = Float.parseFloat(o.get("y").toString()) ;
                label =  o.get("label").toString();
                color = Integer.parseInt(o.get("color").toString());
                nodes.add( new Node(id, x,y,label,color) );
            }

            this.nodeList = nodes ;
            mGraph = new Graph(nodeList);

            List<Connexion> connexions = new ArrayList<Connexion>() ;
            id = 0; color = 0 ; label = "" ; long debut = 0; long fin = 0;
            Connexion con = null ;
            for (int i =0;i<connexionsJson.size();i++){
                o = (JSONObject) connexionsJson.get(i) ;
                id = Long.parseLong(o.get("id").toString());
                color = Integer.parseInt(o.get("color").toString());
                label =  o.get("label").toString();
                debut = Long.parseLong(o.get("debutNodeId").toString());
                fin = Long.parseLong(o.get("finNodeId").toString());
                con = new Connexion(id, color,label,debut,fin) ;
                con.setDebut(mGraph.getNodeById(debut));
                con.setFin(mGraph.getNodeById(fin));
                connexions.add(con);
            }

            updateGraph(connexions);

            //Toast.makeText(this,teste.toJSONString(), Toast.LENGTH_LONG).show();
        }
    }

    // 3 - Write on external storage
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeOnExternalStorage(String foldername, String networkName)  {
        if (Stockeur.isExternalStorageWritable()){
            String exportableData = mGraph.export() ;
            Stockeur.setDataInStorage( getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), this, foldername,networkName+".json",  exportableData);
            if( !foldername.equals(TEMP_FOLDER) ){
                String msg =  R.string.network_saved_message+" "+getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+foldername ;
                Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
            }
        } else {
            //Toast.makeText(this, getString(R.string.external_storage_impossible_create_file), Toast.LENGTH_LONG).show();
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkPermissionAndWrite(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            writeOnExternalStorage(FOLDERNAME, NETWORK_NAME) ;
        }else{
            ActivityCompat.requestPermissions( MainActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100 );
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED ) ){
            writeOnExternalStorage(FOLDERNAME,NETWORK_NAME) ;

        }
        else if ( requestCode == MY_REQUEST_CODE_PERMISSION &&  (grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.doBrowseFile();
        }
        else if ( requestCode == IMAGE_PICKER_PERMISSION_CODE &&   (grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED    ){
            pickImageFromGallery();
        }
    }

    // ========================= Importer ses propres plans

    private void  askPermissionForImagePicker(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions ={ Manifest.permission.READ_EXTERNAL_STORAGE };
                requestPermissions(permissions,IMAGE_PICKER_PERMISSION_CODE);
            }
            else{ pickImageFromGallery(); }
        }
        else{ pickImageFromGallery(); }
    }


    private void pickImageFromGallery() {
        //Intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICKER_CODE);
    }


    private  void shareScreenshot(Uri uriToImage){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }













}


