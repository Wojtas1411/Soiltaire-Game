package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;

class Container{
    private MainFrame tab[];
    private int size;
    Container(int size,MainFrame tab[]){
        this.size = size;
        this.tab = tab;
        for(int i=0;i<this.size;i++){
            this.tab[i].setContainser(this);
        }
    }
    public MainFrame getElement(int i){
        if(i<0 && i>size)i=0;
        return tab[i];
    }
}
abstract class MainFrame{
    protected int height, width;
    protected Scene current_scene;
    protected Stage tmp_stage;
    protected Container tmp_container;
    Group my_group;
    ObservableList my_group_list;
    MainFrame(){
        height = 768;
        width = 1024;
    }
    MainFrame(int h, int w, Stage z){
        height = h;
        width = w;
        tmp_stage = z;
    }
    void setContainser(Container x){
        tmp_container = x;
    }
    void evaluateScene(){

    }
    Scene loadScene(){
        this.evaluateScene();
        return current_scene;
    }
    public int getType(){
        return -1;
    }
    public int getTimeS(){
        return -1;
    }
    public int getSize_of_map(){
        return -1;
    }
    public void setScore(int Score){
        System.out.println("Error");
    }
    public void setTime(String time){
        System.out.println("Error");
    }
    public void setIsplayer(boolean isplayer) {
        System.out.println("Error");
    }
    public void killAI(){
        System.out.println("Error");
    }
}
class Menu extends MainFrame {

    private void loadMenu(){
        Button guzik_game;
        Button guzik_options;
        Button guzik_exit;

        Text title;

        VBox vBox = new VBox();
        vBox.setPrefWidth(100);

        Guziczek menu = new Guziczek("New Game",(int)(width-vBox.getPrefWidth())/2,height/2-100);
        guzik_game = menu.getGuzik();
        guzik_game.setPrefWidth(vBox.getPrefWidth());
        guzik_game.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->{
            current_scene = tmp_container.getElement(2).loadScene();
            tmp_stage.setScene(current_scene);
        });
        vBox.getChildren().add(guzik_game);

        Guziczek opcje = new Guziczek("Options",(int)(width-vBox.getPrefWidth())/2,height/2);
        guzik_options = opcje.getGuzik();
        guzik_options.setPrefWidth(vBox.getPrefWidth());
        guzik_options.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->{
            current_scene = tmp_container.getElement(1).loadScene();
            tmp_stage.setScene(current_scene);
        });
        vBox.getChildren().add(guzik_options);

        Guziczek wyjscie = new Guziczek("Exit",(int)(width-vBox.getPrefWidth())/2,height/2+100);
        guzik_exit = wyjscie.getGuzik();
        guzik_exit.setCancelButton(true);
        guzik_exit.setPrefWidth(vBox.getPrefWidth());
        guzik_exit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            tmp_container.getElement(3).killAI();
            System.exit(0);
        });
        vBox.getChildren().add(guzik_exit);


        Rectangle frame = new Rectangle(400,100);
        frame.setStroke(Color.WHITE);
        frame.setStrokeWidth(4);
        frame.setFill(Color.BLACK);
        frame.setX(width/2-200);
        frame.setY(height/2-265);

        title = new Text();
        title.setFill(Color.WHITE);
        title.setFont(new Font(45));
        title.setText("PEG SOLITAIRE");

        title.setX(width/2-150);
        title.setY(height/2-200);

        my_group = new Group();
        my_group_list = my_group.getChildren();
        my_group_list.add(guzik_game);
        my_group_list.add(guzik_options);
        my_group_list.add(guzik_exit);
        my_group_list.add(frame);
        my_group_list.add(title);

        current_scene = new Scene(my_group,width,height);
        int tmp_type = tmp_container.getElement(1).getType();
        if(tmp_type == 1)current_scene.setFill(Color.MAGENTA);
        else if(tmp_type == 2)current_scene.setFill(Color.YELLOW);
        else if(tmp_type == 3)current_scene.setFill(Color.CYAN);
        else current_scene.setFill(Color.GREY);
    }

    Menu(int h, int w, Stage z){
        super(h,w,z);
        //this.loadMenu();
    }
    @Override
    void evaluateScene(){
        this.loadMenu();
    }
}
class Options extends MainFrame{
    private int size_of_map;
    private double time;
    private int type;

    private void loadOptions(){
        my_group = new Group();
        GridPane optionsPane = new GridPane();

        //set time
        Text timeLabel = new Text("Set time");
        Slider timeSlider = new Slider(1,5,2.5);
        timeSlider.setBlockIncrement(0.5);
        timeSlider.setShowTickMarks(true);
        timeSlider.setShowTickLabels(true);
        timeSlider.setMajorTickUnit(0.5);
        Text timeView = new Text();
        timeSlider.addEventFilter(MouseEvent.MOUSE_RELEASED,e->{
            for(double i = 1.0;i<=5.0;i+=0.5){
                if(timeSlider.getValue()>=i-0.25 && timeSlider.getValue()<i+0.25){
                    timeSlider.setValue(i);
                    timeView.setText(String.valueOf(timeSlider.getValue()));
                    break;
                }
            }
        });

        timeView.setText(String.valueOf(timeSlider.getValue()));

        //set counter
        Text typeLabel = new Text("Set counter");
        RadioButton type1 = new RadioButton("Magenta");
        RadioButton type2 = new RadioButton("Yellow");
        RadioButton type3 = new RadioButton("Cyan");
        type1.setSelected(true);

        Field f1 = new Field(0,0,80,false,true);
        Field f2 = new Field(0,0,80,false,true);
        Field f3 = new Field(0,0,80,false,true);

        Pionek p1 = new Pionek(f1,1);
        Pionek p2 = new Pionek(f2,2);
        Pionek p3 = new Pionek(f3,3);

        type1.addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
            if(type1.isSelected()){
                type2.setSelected(false);
                type3.setSelected(false);
                //optionsPane.setStyle("-fx-background-color: MAGENTA;");
            }
        });
        type2.addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
            if(type2.isSelected()){
                type1.setSelected(false);
                type3.setSelected(false);
                //optionsPane.setStyle("-fx-background-color: YELLOW;");
            }
        });
        type3.addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
            if(type3.isSelected()){
                type1.setSelected(false);
                type2.setSelected(false);
                //optionsPane.setStyle("-fx-background-color: CYAN;");
            }
        });

        //set size
        Text sizeLabel = new Text("Set size");
        Slider sizeSlider = new Slider(7,15,7);
        sizeSlider.setBlockIncrement(2);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMajorTickUnit(2);
        Text sizeView = new Text();
        sizeSlider.addEventFilter(MouseEvent.MOUSE_RELEASED,e->{
            for(int i=7;i<=15;i+=2){
                if(sizeSlider.getValue()>=i-1 && sizeSlider.getValue()<i+1){
                    sizeSlider.setValue(i);
                    sizeView.setText(String.valueOf((int)sizeSlider.getValue()));
                    break;
                }
            }
        });

        sizeView.setText(String.valueOf((int)sizeSlider.getValue()));

        Button guzik_menu;

        guzik_menu = new Button();
        guzik_menu.setText("Save and exit");
        guzik_menu.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->{
            size_of_map = (int)sizeSlider.getValue();
            time = timeSlider.getValue();
            if(type1.isSelected())type = 1;
            if(type2.isSelected())type = 2;
            if(type3.isSelected())type = 3;
            current_scene = tmp_container.getElement(0).loadScene();
            tmp_stage.setScene(current_scene);
        });


        optionsPane.setMinSize(500,500);
        optionsPane.setPadding(new Insets(10,10,10,10));
        optionsPane.setVgap(5);
        optionsPane.setHgap(5);
        optionsPane.setAlignment(Pos.CENTER);

        optionsPane.add(timeLabel,0,0);
        optionsPane.add(timeView,1,0);
        optionsPane.add(timeSlider,2,0);

        optionsPane.add(typeLabel,0,1);
        optionsPane.add(p1.getMe(),0,2);
        optionsPane.add(p2.getMe(),1,2);
        optionsPane.add(p3.getMe(),2,2);

        optionsPane.add(type1,0,3);
        optionsPane.add(type2,1,3);
        optionsPane.add(type3,2,3);

        optionsPane.add(sizeLabel,0,4);
        optionsPane.add(sizeView,1,4);
        optionsPane.add(sizeSlider,2,4);

        optionsPane.add(guzik_menu,2,5);

        current_scene = new Scene(optionsPane,width,height);
        current_scene.setFill(Color.RED);
    }
    Options(int h,int w,Stage z){
        super(h,w,z);
        type = 1;
        size_of_map = 7;
        time = 2.5;
        this.loadOptions();
    }
    @Override
    void evaluateScene() {
        this.loadOptions();
    }
    @Override
    public int getType() {
        return type;
    }
    @Override
    public int getTimeS() {
        return (int)(time*60);
    }
    @Override
    public int getSize_of_map() {
        return size_of_map;
    }
}

class Singleplayer extends MainFrame {
    private int size_of_map;
    private Map mymap;
    private int score;
    private int mytime;
    private boolean timeout;
    private boolean isplayer;
    private MightyAI myAI[];
    private int num_of_AI;
    private void loadSingle(){
        timeout = false;
        size_of_map = tmp_container.getElement(1).getSize_of_map();
        mytime = tmp_container.getElement(1).getTimeS();
        mymap = new Map(size_of_map,tmp_container.getElement(1).getType(),isplayer);
        mymap.playGame();
        my_group = mymap.getMe();
        score = mymap.getScore();

        Text scoreLabel = new Text("Sore:");
        scoreLabel.setX(mymap.getCurrentSize()-100);
        scoreLabel.setY(mymap.getCurrentSize()-80);
        scoreLabel.setFill(Color.WHITE);
        my_group.getChildren().add(scoreLabel);

        Text timeLabel = new Text("Time:");
        timeLabel.setX(mymap.getCurrentSize()-100);
        timeLabel.setY(mymap.getCurrentSize()-100);
        timeLabel.setFill(Color.WHITE);
        Text timeValue = new Text("0:00");
        timeValue.setX(mymap.getCurrentSize()-50);
        timeValue.setY(mymap.getCurrentSize()-100);
        timeValue.setFill(Color.WHITE);

        Guziczek stop = new Guziczek("Exit",mymap.getCurrentSize()-50,mymap.getCurrentSize()-50);
        Button guzik_stop = stop.getGuzik();
        guzik_stop.addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
            myAI[(size_of_map-7)/2].setInGame(false);
            mymap.setDone(true);
            tmp_container.getElement(5).setScore(mymap.getScore());
            tmp_container.getElement(5).setTime(timeValue.getText());
            current_scene = tmp_container.getElement(5).loadScene();
            tmp_stage.setScene(current_scene);
        });

        Task<Void> task = new Task<Void>() {
            {
                updateMessage("0:00");
            }
            @Override
            public Void call() throws Exception {
                int i = 0;
                while (true) {
                    if(i>=mytime){
                        timeout = true;
                    }
                    else{
                        timeout = false;
                    }
                    if(!isplayer && !(mymap.getDone()||timeout)){
                        myAI[(size_of_map-7)/2].setInGame(true);
                        mymap.setSkelet(myAI[(size_of_map-7)/2].getNewSkelet(i));
                        if(!mymap.getDone())mymap.playGame();
                    }
                    if(i%60>=10)updateMessage(String.valueOf(i/60)+":"+String.valueOf(i%60));
                    else updateMessage(String.valueOf(i/60)+":0"+String.valueOf(i%60));
                    if(!timeout&&!mymap.getDone())i++;
                    else{
                        break;
                    }
                    Thread.sleep(1000);
                }
                return null;
            }
        };
        timeValue.textProperty().bind(task.messageProperty());
        Thread th = new Thread(task);
        //th.setDaemon(true);
        th.start();

        my_group.getChildren().add(timeLabel);
        my_group.getChildren().add(timeValue);

        my_group.getChildren().add(guzik_stop);

        my_group.addEventFilter(MouseEvent.MOUSE_MOVED,f->{
            tmp_container.getElement(5).setTime(timeValue.getText());
            this.opened();
        });

        my_group.setLayoutY((height-mymap.getCurrentSize())/2);
        my_group.setLayoutX((width-mymap.getCurrentSize())/2);

        while(!myAI[(size_of_map-7)/2].isReady()){
            System.out.println("waiting");
        }

        current_scene = new Scene(my_group,width,height);
        if(tmp_container.getElement(1).getType()==1)current_scene.setFill(Color.MAGENTA);
        if(tmp_container.getElement(1).getType()==2)current_scene.setFill(Color.YELLOW);
        if(tmp_container.getElement(1).getType()==3)current_scene.setFill(Color.CYAN);
    }

    private void opened(){
        Rectangle tmpBack= new Rectangle(mymap.getCurrentSize()/2-150,mymap.getCurrentSize()/2-150,300,300);
        tmpBack.setVisible(mymap.getDone()||timeout);
        my_group.getChildren().add(tmpBack);
        Text tmpText = new Text("Game Over");
        tmpText.setFill(Color.WHITE);
        tmpText.setFont(new Font(30));
        tmpText.setX(mymap.getCurrentSize()/2-70);
        tmpText.setY(mymap.getCurrentSize()/2-10);
        tmpText.setVisible(mymap.getDone()||timeout);
        my_group.getChildren().add(tmpText);
        Button tmpButton = new Button("Exit");
        tmpButton.setLayoutX(mymap.getCurrentSize()/2-10);
        tmpButton.setLayoutY(mymap.getCurrentSize()/2);
        tmpButton.addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
            myAI[(size_of_map-7)/2].setInGame(false);
            tmp_container.getElement(5).setScore(mymap.getScore());
            current_scene = tmp_container.getElement(5).loadScene();
            tmp_stage.setScene(current_scene);
        });
        tmpButton.setVisible(mymap.getDone()||timeout);
        my_group.getChildren().add(tmpButton);
        if(isplayer)mymap.setCanMove(!mymap.getDone()&&!timeout);
    }

    Singleplayer(int h, int w, Stage z){
        super(h,w,z);
    }
    @Override
    void evaluateScene() {
        this.loadSingle();
    }
    public void setIsplayer(boolean isplayer) {
        this.isplayer = isplayer;
    }

    public void setMyAI(MightyAI[] myAI,int num_of_AI) {
        this.myAI = myAI;
        this.num_of_AI = num_of_AI;
    }

    public void killAI(){
        for(int i=0;i<num_of_AI;i++){
            myAI[i].setEnd(true);
        }
    }
}

class Score extends MainFrame{
    private int score;
    private int size;
    private String time;


    private void loadScore(){
        GridPane scorePane = new GridPane();

        size = tmp_container.getElement(1).getSize_of_map();

        Text scoreLabel = new Text("Score:");
        Text scoreText = new Text(String.valueOf(score));

        Text timeLabel = new Text("Time:");
        Text timeText = new Text(time);

        Text sizeLabel = new Text("Size:");
        Text sizeText = new Text(String.valueOf(size));


        Guziczek menu = new Guziczek("Back to Menu",0,0);
        Button guzik_back = menu.getGuzik();
        guzik_back.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            current_scene = tmp_container.getElement(0).loadScene();
            tmp_stage.setScene(current_scene);
        });

        scorePane.setMinSize(500,500);
        scorePane.setPadding(new Insets(10,10,10,10));
        scorePane.setVgap(5);
        scorePane.setHgap(5);
        scorePane.setAlignment(Pos.CENTER);

        scorePane.add(scoreLabel,0,0);
        scorePane.add(scoreText,1,0);

        scorePane.add(timeLabel,0,1);
        scorePane.add(timeText,1,1);

        scorePane.add(sizeLabel,0,2);
        scorePane.add(sizeText,1,2);

        scorePane.add(guzik_back,1,3);

        current_scene = new Scene(scorePane,width,height);
        current_scene.setFill(Color.BLUE);
    }
    Score(int h,int w,Stage s){
        super(h,w,s);
        score = 0;
        time = "";
    }

    @Override
    void evaluateScene() {
        this.loadScore();
    }
    @Override
    public void setScore(int score) {
        this.score = score;
    }
    @Override
    public void setTime(String time) {
        this.time = time;
    }
}

class PreGame extends MainFrame{

    private void loadPreGame(){
        my_group = new Group();
        GridPane pregamePane = new GridPane();

        Guziczek menu = new Guziczek("Back to Menu",0,0);
        Button guzik_back = menu.getGuzik();
        guzik_back.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            current_scene = tmp_container.getElement(0).loadScene();
            tmp_stage.setScene(current_scene);
        });

        Guziczek single = new Guziczek("Singleplayer",0,0);
        Button guzik_single = single.getGuzik();
        guzik_single.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            tmp_container.getElement(3).setIsplayer(true);
            current_scene = tmp_container.getElement(3).loadScene();
            tmp_stage.setScene(current_scene);
        });

        Guziczek multi = new Guziczek("Watch SI play",0,0);
        Button guzik_multi = multi.getGuzik();
        guzik_multi.addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
            tmp_container.getElement(3).setIsplayer(false);
            current_scene = tmp_container.getElement(3).loadScene();
            tmp_stage.setScene(current_scene);
        });


        pregamePane.setMinSize(500,500);
        pregamePane.setPadding(new Insets(10,10,10,10));
        pregamePane.setVgap(5);
        pregamePane.setHgap(5);
        pregamePane.setAlignment(Pos.CENTER);

        pregamePane.add(guzik_single,0,0);
        pregamePane.add(guzik_multi,2,0);
        pregamePane.add(guzik_back,1,3);

        current_scene = new Scene(pregamePane,width,height);
        current_scene.setFill(Color.RED);
    }

    PreGame(int h, int w, Stage z){
        super(h,w,z);
    }

    @Override
    void evaluateScene() {
        this.loadPreGame();
    }
}

class Map{
    private Group me;
    private int maxSize = 750;
    private int type;
    private int currentSize;
    private int skelet[][];
    private int size;
    private int numberOfOccupied;
    private Field tab[][];
    private Pionek tabP[][];
    private Rectangle tabG[][];
    private Circle tabC[][];
    private Boolean done;
    private boolean canMove;
    private Text scoreValue;

    private Boolean isClicked;
    private int ClickedX, ClickedY;

    Map(int n,int type,boolean single){
        canMove = single;
        this.type = type;
        size = n;
        me = new Group();
        skelet = new int[n][n];
        tab = new Field[n][n];
        tabP = new Pionek[n][n];
        tabG = new Rectangle[n][n];
        tabC = new Circle[n][n];
        numberOfOccupied = 0;
        done = false;

        int num = 0;
        if(n%3 == 0)num = n/3;
        else if(n%3 == 1)num = n/3+1;
        else if(n%3 == 2)num = n/3;

        int fieldSize = (maxSize-n-1)/n;
        currentSize = n+1+n*fieldSize;
        Rectangle back= new Rectangle(0,0,currentSize,currentSize);
        me.getChildren().add(back);

        //skelet init
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(j == n/2 && i == n/2){
                    skelet[i][j]=1;
                }
                else if((i<(n-num)/2 && j<(n-num)/2)||(i<(n-num)/2 && j>(n-num)/2+num-1)||(j<(n-num)/2 && i>(n-num)/2+num-1)||(i>(n-num)/2+num-1 && j>(n-num)/2+num-1)){
                    skelet[i][j] = -1;
                }
                else{
                    skelet[i][j] = 0;
                    numberOfOccupied++;
                }
            }
        }
        isClicked = false;

        scoreValue = new Text(String.valueOf(numberOfOccupied));
        scoreValue.setX(this.getCurrentSize()-50);
        scoreValue.setY(this.getCurrentSize()-80);
        scoreValue.setFill(Color.WHITE);

        me.getChildren().add(scoreValue);


        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(skelet[i][j]!=-1){
                    tab[i][j] = new Field(1+i+i*fieldSize,1+j+j*fieldSize,fieldSize,false,true);
                    if(skelet[i][j]==1)tab[i][j].setEmpty(true);
                    Rectangle fieldNew = tab[i][j].getMe();
                    if(j == n/2 && i == n/2) fieldNew.setFill(Color.DARKGREY);
                    me.getChildren().add(fieldNew);

                    tabP[i][j] = new Pionek(tab[i][j],type);
                    tabC[i][j] = tabP[i][j].getMe();
                    tabC[i][j].addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
                        if(!isClicked && canMove){
                            isClicked = true;
                            ClickedX = (int)(fieldNew.getX()-1)/(fieldSize+1);
                            ClickedY = (int)(fieldNew.getY()-1)/(fieldSize+1);
                            if(ClickedX-2>=0&&skelet[ClickedX-2][ClickedY]==1&&skelet[ClickedX-1][ClickedY]==0){
                                if(skelet[ClickedX-2][ClickedY]!=-1)tabG[ClickedX-2][ClickedY].setVisible(true);
                            }
                            if(ClickedX+2<size&&skelet[ClickedX+2][ClickedY]==1&&skelet[ClickedX+1][ClickedY]==0){
                                if(skelet[ClickedX+2][ClickedY]!=-1)tabG[ClickedX+2][ClickedY].setVisible(true);
                            }
                            if(ClickedY-2>=0&&skelet[ClickedX][ClickedY-2]==1&&skelet[ClickedX][ClickedY-1]==0){
                                if(skelet[ClickedX][ClickedY-2]!=-1)tabG[ClickedX][ClickedY-2].setVisible(true);
                            }
                            if(ClickedY+2<size&&skelet[ClickedX][ClickedY+2]==1&&skelet[ClickedX][ClickedY+1]==0){
                                if(skelet[ClickedX][ClickedY+2]!=-1)tabG[ClickedX][ClickedY+2].setVisible(true);
                            }
                        }
                        else if(canMove && isClicked &&  ClickedX == (int)(fieldNew.getX()-1)/(fieldSize+1) && ClickedY == (int)(fieldNew.getY()-1)/(fieldSize+1)){
                            isClicked = false;
                            for(int k=0;k<size;k++){
                                for(int l=0;l<size;l++){
                                    if(skelet[k][l] != -1)tabG[k][l].setVisible(false);
                                }
                            }
                        }
                        else if (canMove){
                            for(int k=0;k<size;k++){
                                for(int l=0;l<size;l++){
                                    if(skelet[k][l] != -1)tabG[k][l].setVisible(false);
                                }
                            }
                            ClickedX = (int)(fieldNew.getX()-1)/(fieldSize+1);
                            ClickedY = (int)(fieldNew.getY()-1)/(fieldSize+1);
                            //System.out.println(ClickedX+" "+ClickedY);
                            if(ClickedX-2>=0&&skelet[ClickedX-2][ClickedY]==1&&skelet[ClickedX-1][ClickedY]==0){
                                if(skelet[ClickedX-2][ClickedY]!=-1)tabG[ClickedX-2][ClickedY].setVisible(true);
                            }
                            if(ClickedX+2<size&&skelet[ClickedX+2][ClickedY]==1&&skelet[ClickedX+1][ClickedY]==0){
                                if(skelet[ClickedX+2][ClickedY]!=-1)tabG[ClickedX+2][ClickedY].setVisible(true);
                            }
                            if(ClickedY-2>=0&&skelet[ClickedX][ClickedY-2]==1&&skelet[ClickedX][ClickedY-1]==0){
                                if(skelet[ClickedX][ClickedY-2]!=-1)tabG[ClickedX][ClickedY-2].setVisible(true);
                            }
                            if(ClickedY+2<size&&skelet[ClickedX][ClickedY+2]==1&&skelet[ClickedX][ClickedY+1]==0){
                                if(skelet[ClickedX][ClickedY+2]!=-1)tabG[ClickedX][ClickedY+2].setVisible(true);
                            }
                        }
                    });

                    me.getChildren().add(tabC[i][j]);

                    tabG[i][j] = new Rectangle(1+i+i*fieldSize,1+j+j*fieldSize,fieldSize,fieldSize);
                    tabG[i][j].setFill(Color.GREEN);
                    tabG[i][j].setOpacity(0.5);
                    tabG[i][j].setVisible(false);
                    tabG[i][j].addEventFilter(MouseEvent.MOUSE_CLICKED,e->{
                        if(isClicked && canMove){
                            int tmpX = (int)(fieldNew.getX()-1)/(fieldSize+1);
                            int tmpY = (int)(fieldNew.getY()-1)/(fieldSize+1);
                            skelet[tmpX][tmpY] = 0;
                            skelet[ClickedX][ClickedY] = 1;
                            skelet[(ClickedX+tmpX)/2][(ClickedY+tmpY)/2] = 1;
                            this.playGame();
                            numberOfOccupied--;
                            for(int k=0;k<size;k++){
                                for(int l=0;l<size;l++){
                                    if(skelet[k][l] != -1)tabG[k][l].setVisible(false);
                                }
                            }
                            isClicked = false;
                            done = this.isOver();
                        }
                    });
                    me.getChildren().add(tabG[i][j]);

                }
            }
        }
    }
    public void playGame(){
        int k=0;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(skelet[i][j] != -1){
                    tab[i][j].status(skelet);
                    tabP[i][j].setOnMap(tab[i][j].isOccupied());
                    tabC[i][j].setVisible(tabP[i][j].isOnMap());
                    if(skelet[i][j]==0)k++;
                }
            }
        }
        numberOfOccupied = k;
        scoreValue.setText(String.valueOf(numberOfOccupied));
        done = this.isOver();
    }
    private Boolean isOver(){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(skelet[i][j]==0){
                    if(i-1>=0 && i+1<size && skelet[i-1][j] == 0 && skelet[i+1][j]==1) return false;
                    if(i-1>=0 && i+1<size && skelet[i+1][j] == 0 && skelet[i-1][j]==1) return false;
                    if(j-1>=0 && j+1<size && skelet[i][j-1] == 0 && skelet[i][j+1]==1) return false;
                    if(j-1>=0 && j+1<size && skelet[i][j+1] == 0 && skelet[i][j-1]==1) return false;
                }
            }
        }
        return true;
    }

    public Group getMe(){
        return me;
    }
    public int getCurrentSize(){

        return currentSize;
    }
    public int getScore(){
        return numberOfOccupied;
    }
    public Boolean getDone() {
        return done;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setSkelet(int[][] skelet) {
        this.skelet = skelet;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
class Field{
    private Rectangle me;
    private Boolean empty;
    private Boolean active;
    private int XPos, Ypos, myS;
    private int myMapX, myMapY;

    Field(int Xpos, int Ypos, int myS, Boolean empty, Boolean active){
        me = new Rectangle(Xpos,Ypos,myS,myS);
        me.setFill(Color.GREY);
        this.empty = empty;
        this.active = active;
        this.XPos = Xpos;
        this.Ypos = Ypos;
        this.myS = myS;
        myMapX = (this.getXPos()-1)/(this.getMyS()+1);
        myMapY = (this.getYpos()-1)/(this.getMyS()+1);

    }
    public Rectangle getMe() {
        return me;
    }
    public void setEmpty(Boolean empty){
        this.empty = empty;
    }
    public int getXPos(){
        return XPos;
    }
    public int getYpos() {
        return Ypos;
    }
    public int getMyS() {
        return myS;
    }
    public Boolean isOccupied(){
        return !empty;
    }
    public Boolean isActive(){
        return active;
    }
    public void status(int skelet[][]){
        if(skelet[myMapX][myMapY]==-1)active = false;
        else if(skelet[myMapX][myMapY]==0)empty = false;
        else if(skelet[myMapX][myMapY]==1)empty = true;
    }


}
class Pionek{
    private int type;
    private Circle me;
    private Boolean onMap;
    private int myMapX, myMapY;
    Pionek(Field x, int type){
        this.type = type;
        onMap = (x.isOccupied() && x.isActive());

        me = new Circle();
        me.setRadius(x.getMyS()/2-4);
        me.setCenterY(x.getYpos()+4+me.getRadius());
        me.setCenterX(x.getXPos()+4+me.getRadius());

        if(type == 1)me.setFill(Color.MAGENTA);
        else if(type == 2)me.setFill(Color.YELLOW);
        else if(type == 3)me.setFill(Color.CYAN);

        myMapX = (x.getXPos()-1)/(x.getMyS()+1);
        myMapY = (x.getYpos()-1)/(x.getMyS()+1);

        me.setVisible(onMap);
    }
    public Boolean isOnMap(){
        return onMap;
    }
    public Circle getMe() {
        return me;
    }
    public void setOnMap(Boolean onMap) {
        this.onMap = onMap;
    }
}
class Guziczek{
    private Button guzik;
    Guziczek(String name, int x, int y){
        guzik = new Button(name);
        int minH = (int)guzik.getHeight();
        int minW = (int)guzik.getWidth();
        guzik.setMinHeight(minH);
        guzik.setMinWidth(minW);
        guzik.setLayoutY(y);
        guzik.setLayoutX(x-(minW/2));
    }
    Button getGuzik(){
        return this.guzik;
    }

}

class MightyAI implements Runnable{
    private int best_score;
    private int start_score;
    private int size_of_map;
    private int start_skelet[][];
    private int best_set_skelets[][][];
    private int current_set_skelets[][][];
    private int current_score;
    private int possible_moves;
    private int list_of_moves[][];
    private boolean isInGame;
    private boolean isReady;
    private boolean isEnd;

    private void makeStartSkelet(){
        start_skelet = new int[size_of_map][size_of_map];
        start_score=0;
        int num = 0;
        if(size_of_map%3 == 0)num = size_of_map/3;
        else if(size_of_map%3 == 1)num = size_of_map/3+1;
        else if(size_of_map%3 == 2)num = size_of_map/3;
        for(int i=0;i<size_of_map;i++){
            for(int j=0;j<size_of_map;j++){
                if(j == size_of_map/2 && i == size_of_map/2){
                    start_skelet[i][j]=1;
                }
                else if((i<(size_of_map-num)/2 && j<(size_of_map-num)/2)||(i<(size_of_map-num)/2 && j>(size_of_map-num)/2+num-1)||(j<(size_of_map-num)/2 && i>(size_of_map-num)/2+num-1)||(i>(size_of_map-num)/2+num-1 && j>(size_of_map-num)/2+num-1)){
                    start_skelet[i][j] = -1;
                }
                else{
                    start_skelet[i][j] = 0;
                    start_score++;
                }
            }
        }
        best_score = start_score;
    }

    private void new_try(){
        current_set_skelets = new int[start_score][size_of_map][size_of_map];
        current_set_skelets[0] = start_skelet;
        current_score = start_score;

        Random random = new Random();

        possible_moves = 1;
        while(possible_moves>0){
            possible_moves=0;
            //counting moves
            for(int i=0;i<size_of_map;i++){
                for(int j=0;j<size_of_map;j++){
                    if(current_set_skelets[start_score-current_score][i][j]==0){
                        if(i-1>=0 && i+1<size_of_map && current_set_skelets[start_score-current_score][i-1][j] == 0 && current_set_skelets[start_score-current_score][i+1][j]==1) possible_moves++;
                        if(i-1>=0 && i+1<size_of_map && current_set_skelets[start_score-current_score][i+1][j] == 0 && current_set_skelets[start_score-current_score][i-1][j]==1) possible_moves++;
                        if(j-1>=0 && j+1<size_of_map && current_set_skelets[start_score-current_score][i][j-1] == 0 && current_set_skelets[start_score-current_score][i][j+1]==1) possible_moves++;
                        if(j-1>=0 && j+1<size_of_map && current_set_skelets[start_score-current_score][i][j+1] == 0 && current_set_skelets[start_score-current_score][i][j-1]==1) possible_moves++;
                    }
                }
            }
            list_of_moves = new int[possible_moves][4];
            //making list
            int k=0;
            for(int i=0;i<size_of_map;i++){
                for(int j=0;j<size_of_map;j++){
                    if(current_set_skelets[start_score-current_score][i][j]==0){
                        if(i-1>=0 && i+1<size_of_map && current_set_skelets[start_score-current_score][i-1][j] == 0 && current_set_skelets[start_score-current_score][i+1][j]==1){
                            list_of_moves[k][0] = i-1;
                            list_of_moves[k][1] = j;
                            list_of_moves[k][2] = i+1;
                            list_of_moves[k][3] = j;
                            k++;
                        }
                        if(i-1>=0 && i+1<size_of_map && current_set_skelets[start_score-current_score][i+1][j] == 0 && current_set_skelets[start_score-current_score][i-1][j]==1) {
                            list_of_moves[k][0] = i+1;
                            list_of_moves[k][1] = j;
                            list_of_moves[k][2] = i-1;
                            list_of_moves[k][3] = j;
                            k++;
                        }
                        if(j-1>=0 && j+1<size_of_map && current_set_skelets[start_score-current_score][i][j-1] == 0 && current_set_skelets[start_score-current_score][i][j+1]==1) {
                            list_of_moves[k][0] = i;
                            list_of_moves[k][1] = j-1;
                            list_of_moves[k][2] = i;
                            list_of_moves[k][3] = j+1;
                            k++;
                        }
                        if(j-1>=0 && j+1<size_of_map && current_set_skelets[start_score-current_score][i][j+1] == 0 && current_set_skelets[start_score-current_score][i][j-1]==1) {
                            list_of_moves[k][0] = i;
                            list_of_moves[k][1] = j+1;
                            list_of_moves[k][2] = i;
                            list_of_moves[k][3] = j-1;
                            k++;
                        }
                    }
                }
            }
            //Randing move;
            if(possible_moves>0){
                int c = random.nextInt(possible_moves);
                current_score--;
                for(int i=0;i<size_of_map;i++){
                    current_set_skelets[start_score-current_score][i] = current_set_skelets[start_score-current_score-1][i].clone();
                }
                //moving counter
                current_set_skelets[start_score-current_score][list_of_moves[c][0]][list_of_moves[c][1]] = 1;
                current_set_skelets[start_score-current_score][list_of_moves[c][2]][list_of_moves[c][3]] = 0;
                current_set_skelets[start_score-current_score][(list_of_moves[c][0]+list_of_moves[c][2])/2][(list_of_moves[c][1]+list_of_moves[c][3])/2] = 1;
            }
        }
        if(!isInGame && current_score<best_score){
            best_score = current_score;
            best_set_skelets = new int[start_score][size_of_map][size_of_map];
            for(int i=0;i<(start_score-current_score+1);i++){
                for(int j=0;j<size_of_map;j++){
                    for(int k=0;k<size_of_map;k++){
                        best_set_skelets[i][j][k] = current_set_skelets[i][j][k];
                    }
                }
            }
        }
    }

    public void run(){
        this.new_try();
        isReady = true;
        while(true){
            if(isEnd)break;
            this.new_try();
        }
    }

    MightyAI(int size_of_map){
        this.size_of_map = size_of_map;
        this.isInGame = false;
        this.isReady = false;
        this.isEnd = false;
        this.makeStartSkelet();
    }

    public int[][] getNewSkelet(int i){
        //System.out.println(i);
        //this.showSkelet(best_set_skelets[i]);
        return best_set_skelets[i];
    }

    public boolean isReady(){
        return isReady;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    private void showSkelet(int[][]tmpskelet){
        for(int i=0;i<size_of_map;i++){
            for(int j=0;j<size_of_map;j++){
                if(tmpskelet[j][i]!=-1)System.out.print(" ");
                System.out.print(tmpskelet[j][i]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
}

public class Main extends Application {
    private int width = 1024;
    private int height = 768;

    private Scene current;

    @Override
    public void start(Stage primaryStage) throws Exception{

        MightyAI test[] = new MightyAI[5];
        test[0] = new MightyAI(7);
        (new Thread(test[0])).start();
        test[1] = new MightyAI(9);
        (new Thread(test[1])).start();
        test[2] = new MightyAI(11);
        (new Thread(test[2])).start();
        test[3] = new MightyAI(13);
        (new Thread(test[3])).start();
        test[4] = new MightyAI(15);
        (new Thread(test[4])).start();

        Singleplayer tmp = new Singleplayer(height,width,primaryStage);
        tmp.setMyAI(test,5);

        MainFrame tmpTab[];
        tmpTab = new MainFrame[6];
        tmpTab[0] = new Menu(height,width,primaryStage);
        tmpTab[1] = new Options(height,width,primaryStage);
        tmpTab[2] = new PreGame(height,width,primaryStage);
        //tmpTab[3] = new Singleplayer(height,width,primaryStage);
        tmpTab[4] = tmp;
        tmpTab[3] = tmp;
        tmpTab[5] = new Score(height,width,primaryStage);

        Container tmpContainer = new Container(6,tmpTab);

        current = tmpContainer.getElement(0).loadScene();

        primaryStage.setTitle("Peg Solitaire");
        primaryStage.setScene(current);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception{
        launch(args);
    }
}
