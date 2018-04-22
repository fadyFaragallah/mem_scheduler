package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ArrayList<memProcess> mp_arr;
    public ArrayList<memProcess> mp_arr_empty;
    public ArrayList<String> addresses;


    @FXML private TableView<memProcess> table;
    @FXML private TableColumn<memProcess,String> idCol;
    @FXML private TableColumn<memProcess, Integer> startCol;
    @FXML private TableColumn<memProcess, Integer> sizeCol;

    @FXML private TextField P_ID;
    @FXML private TextField P_Start;
    @FXML private TextField P_Size;

    @FXML private VBox draw_area;
    @FXML   private TextField mem_size;
    @FXML private RadioButton first;
    @FXML private RadioButton best;
    @FXML private RadioButton worst;
    @FXML private TextField P_ID_to_in;
    @FXML private TextField P_Size_to_in;


    public ObservableList<memProcess> getProcesses()
    {
        ObservableList<memProcess> memProcesses= FXCollections.observableArrayList();


        return memProcesses;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        mp_arr=new ArrayList<memProcess>();
        mp_arr_empty=new ArrayList<memProcess>();
        addresses=new ArrayList<String>();

        idCol.setMinWidth(200);
        startCol.setMinWidth(200);
        sizeCol.setMinWidth(200);


        idCol.setCellValueFactory(new PropertyValueFactory<>("processId"));
        startCol.setCellValueFactory(new PropertyValueFactory<memProcess, Integer>("processStart"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<memProcess, Integer>("processSize"));

        table.setItems(getProcesses());

        //allow the table to be editable
        table.setEditable(true);

        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        startCol.setCellFactory(TextFieldTableCell.<memProcess,Integer>forTableColumn(new IntegerStringConverter()));
        sizeCol.setCellFactory(TextFieldTableCell.<memProcess,Integer>forTableColumn(new IntegerStringConverter()));

    }
    @FXML
    public void ADD_PROCESS()
    {
        memProcess X=new memProcess(P_ID.getText(),Integer.parseInt(P_Start.getText()),Integer.parseInt(P_Size.getText()));


        //clearing the fields
        P_ID.clear();
        P_Size.clear();
        P_Start.clear();

        //add it to the processes array
        mp_arr.add(X);
        //add it to the table
        table.getItems().add(X);
        //draw the table again
        draw_mem();
    }

    @FXML
    public void draw_mem()
    {
        if(mem_size.isEditable())
        {
            mp_arr_empty.add(new memProcess("EMPTY",0,Integer.parseInt(mem_size.getText())));
            mem_size.setEditable(false);
        }
        else {
            //clear the empty array
            mp_arr_empty.clear();
            addresses.clear();
        }
        draw_area.getChildren().clear();

        HBox H_Box=new HBox(5);
        VBox V_Box1=new VBox();
        VBox V_Box2=new VBox();
        int totalSize=(((int) draw_area.getHeight()))-5;
        if(mp_arr.size()==0)
        {
            //draw empty memory
            Label memLocation=new Label();
            memLocation.setPrefSize(50,totalSize);
            memLocation.setStyle("-fx-border-color: black");
            memLocation.setText("EMPTY");
            V_Box1.getChildren().add(memLocation);
            addresses.add("0");
            //Label memStart=new Label("0");
            //Label memAdjust=new Label();
            //memAdjust.setPrefSize(50,totalSize);
            //V_Box2.getChildren().addAll(memStart,memAdjust);
        }
        else if(mp_arr.size()>0)
        {
            int currentSize=0;
            memProcess.sort_start(mp_arr,0);
            for(int i=0;i<mp_arr.size();i++)
            {
                if(mp_arr.get(i).getProcessStart()>currentSize)
                {
                    //there is an empty location

                        addresses.add(Integer.toString(currentSize));
                        //add it to the empty locations
                        memProcess x=new memProcess("EMPTY",currentSize,mp_arr.get(i).getProcessStart()-currentSize);
                        mp_arr_empty.add(x);
                        //draw it
                        Label memLocation=new Label();
                        memLocation.setPrefSize(50,((double) (x.getProcessSize())/Integer.parseInt(mem_size.getText()))*totalSize);
                        memLocation.setStyle("-fx-border-color: black");
                        memLocation.setText("EMPTY");
                        V_Box1.getChildren().add(memLocation);
                        currentSize+=x.getProcessSize();

                    i--;
                }
                else
                {
                    //there is an allocated process
                    addresses.add(Integer.toString(currentSize));
                    //draw it
                    Label memLocation=new Label();
                    memLocation.setPrefSize(50,((double) (mp_arr.get(i).getProcessSize())/Integer.parseInt(mem_size.getText()))*totalSize);
                    memLocation.setStyle("-fx-border-color: black");
                    memLocation.setTextAlignment(TextAlignment.CENTER);
                    memLocation.setText(mp_arr.get(i).getProcessId());
                    V_Box1.getChildren().add(memLocation);
                    currentSize+=mp_arr.get(i).getProcessSize();
                }
            }
            if(currentSize<Integer.parseInt(mem_size.getText()))
            {
                //create process to put at from the last process to the end
                addresses.add(Integer.toString(currentSize));
                //add it to the empty locations
                memProcess x=new memProcess("EMPTY",currentSize,Integer.parseInt(mem_size.getText())-currentSize);
                mp_arr_empty.add(x);
                //draw it
                Label memLocation=new Label();
                memLocation.setPrefSize(50,((double) (x.getProcessSize())/Integer.parseInt(mem_size.getText()))*totalSize);
                memLocation.setStyle("-fx-border-color: black");
                memLocation.setText("EMPTY");
                V_Box1.getChildren().add(memLocation);

            }
        }
        //drawing the addresses
        for(int i=0;i<addresses.size();i++)
        {
            Label memStart=new Label(addresses.get(i));
            Label memAdjust=new Label();
            if(i==(addresses.size()-1))
            {
                memAdjust.setPrefSize(50, ((double)((Integer.parseInt(mem_size.getText()))-Integer.parseInt(addresses.get(i)))*totalSize/(Integer.parseInt(mem_size.getText()))));
            }
            else
            {
                memAdjust.setPrefSize(50,((double)(Integer.parseInt(addresses.get(i+1))-Integer.parseInt(addresses.get(i)))*totalSize/(Integer.parseInt(mem_size.getText()))));

            }
            V_Box2.getChildren().addAll(memStart,memAdjust);
        }
        //adding the last label to the V_Box2
        V_Box2.getChildren().add(new Label(mem_size.getText()));

        H_Box.getChildren().addAll(V_Box1,V_Box2);
        draw_area.getChildren().add(H_Box);

    }
    @FXML
    public void ALLOCATE_PROCESS()
    {
        memProcess X=new memProcess(P_ID_to_in.getText(),-1,Integer.parseInt(P_Size_to_in.getText()));
        P_ID_to_in.clear();
        P_Size_to_in.clear();
        if(first.isSelected())
        {
            memProcess.sort_start(mp_arr_empty,0);
            for(int i=0;i<mp_arr_empty.size();i++)
            {
                if(X.getProcessSize()<=mp_arr_empty.get(i).getProcessSize())
                {
                    X.setProcessStart(mp_arr_empty.get(i).getProcessStart());
                    mp_arr_empty.get(i).setProcessSize(mp_arr_empty.get(i).getProcessSize()-X.getProcessSize());
                    mp_arr.add(X);
                    //add it to the table
                    table.getItems().add(X);
                    break;
                }
            }
            draw_mem();
        }
        else if(best.isSelected())
        {
            memProcess.sort_size(mp_arr_empty,0);
            for(int i=0;i<mp_arr_empty.size();i++)
            {
                if(X.getProcessSize()<=mp_arr_empty.get(i).getProcessSize())
                {
                    X.setProcessStart(mp_arr_empty.get(i).getProcessStart());
                    mp_arr_empty.get(i).setProcessSize(mp_arr_empty.get(i).getProcessSize()-X.getProcessSize());
                    mp_arr.add(X);
                    //add it to the table
                    table.getItems().add(X);
                    break;
                }
            }
            draw_mem();
        }
       else if(worst.isSelected())
        {
            memProcess.sort_size(mp_arr_empty,1);
            for(int i=0;i<mp_arr_empty.size();i++)
            {
                if(X.getProcessSize()<=mp_arr_empty.get(i).getProcessSize())
                {
                    X.setProcessStart(mp_arr_empty.get(i).getProcessStart());
                    mp_arr_empty.get(i).setProcessSize(mp_arr_empty.get(i).getProcessSize()-X.getProcessSize());
                    mp_arr.add(X);
                    //add it to the table
                    table.getItems().add(X);
                    break;
                }
            }
            draw_mem();
        }
    }
    public void DEALLOCATE_PROCESS()
    {

            ObservableList<memProcess> selected,all;
            all=table.getItems();
            selected=table.getSelectionModel().getSelectedItems();
            String selected_id=selected.get(0).getProcessId();
            //removing it from the array
            for(int i=0;i<mp_arr.size();i++)
            {
                if(mp_arr.get(i).getProcessId()==selected_id)
                {
                    mp_arr.remove(i);
                    break;
                }
            }

            selected.forEach(all::remove);
            draw_mem();
    }


}
