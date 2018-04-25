package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
    public ArrayList<memProcess> added_arr;
    public ArrayList<memProcess> mem;
    public ArrayList<memProcess> holes;

    public ArrayList<String> addresses;

    static int count=0;
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
    @FXML private Label warning;


    public ObservableList<memProcess> getProcesses()
    {
        ObservableList<memProcess> memProcesses= FXCollections.observableArrayList();


        return memProcesses;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        mp_arr=new ArrayList<memProcess>();
        added_arr=new ArrayList<>();
        holes=new ArrayList<>();
        mem=new ArrayList<>();
        addresses=new ArrayList<String>();

        idCol.setMinWidth(200);
        startCol.setMinWidth(200);
        sizeCol.setMinWidth(200);


        idCol.setCellValueFactory(new PropertyValueFactory<>("processId"));
        startCol.setCellValueFactory(new PropertyValueFactory<memProcess, Integer>("processStart"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<memProcess, Integer>("processSize"));

        table.setItems(getProcesses());

        //allow the table to be editable
       // table.setEditable(true);

        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        startCol.setCellFactory(TextFieldTableCell.<memProcess,Integer>forTableColumn(new IntegerStringConverter()));
        sizeCol.setCellFactory(TextFieldTableCell.<memProcess,Integer>forTableColumn(new IntegerStringConverter()));

    }

    @FXML
    public void ADD_HOLE()
    {
        memProcess X=new memProcess("EMPTY",Integer.parseInt(P_Start.getText()),Integer.parseInt(P_Size.getText()));


        //clearing the fields
        P_Size.clear();
        P_Start.clear();
        if(X.getProcessStart()+X.getProcessSize()>Integer.parseInt(mem_size.getText()))
        {
            warning.setText("hole goes out of memory size");
            return;
        }
        else warning.setText("");
        //add it to the holes array
        holes.add(X);
        //fix the holes
        memProcess.fix_holes(holes);
        //draw the table again

        draw_mem2();

    }

    @FXML
    public void ALLOCATE_PROCESS()
    {

        memProcess X=new memProcess(P_ID_to_in.getText(),-1,Integer.parseInt(P_Size_to_in.getText()));

        Boolean found=false;
        if(first.isSelected())
        {
            memProcess.sort_start(holes,0);
            for(int i=0;i<holes.size();i++)
            {
                if(X.getProcessSize()<=holes.get(i).getProcessSize())
                {
                    X.setProcessStart(holes.get(i).getProcessStart());
                    holes.get(i).setProcessStart(X.getProcessStart()+X.getProcessSize());
                    holes.get(i).setProcessSize(holes.get(i).getProcessSize()-X.getProcessSize());
                    //fix the holes
                    memProcess.fix_holes(holes);
                    mp_arr.add(X);
                    //add it to the table
                    table.getItems().add(X);
                    found=true;
                    warning.setText("process with id "+X.getProcessId()+" allocated at "+X.getProcessStart());
                    break;
                }
            }
            draw_mem2();
        }
        else if(best.isSelected())
        {
            memProcess.sort_size(holes,0);
            for(int i=0;i<holes.size();i++)
            {
                if(X.getProcessSize()<=holes.get(i).getProcessSize())
                {
                    X.setProcessStart(holes.get(i).getProcessStart());
                    holes.get(i).setProcessStart(X.getProcessStart()+X.getProcessSize());
                    holes.get(i).setProcessSize(holes.get(i).getProcessSize()-X.getProcessSize());
                    //fix the holes
                    memProcess.fix_holes(holes);
                    mp_arr.add(X);
                    //add it to the table
                    table.getItems().add(X);
                    found=true;
                    warning.setText("process with id "+X.getProcessId()+" allocated at "+X.getProcessStart());
                    break;
                }
            }
            draw_mem2();
        }
       else if(worst.isSelected())
        {
            memProcess.sort_size(holes,1);
            for(int i=0;i<holes.size();i++)
            {
                if(X.getProcessSize()<=holes.get(i).getProcessSize())
                {
                    X.setProcessStart(holes.get(i).getProcessStart());
                    holes.get(i).setProcessStart(X.getProcessStart()+X.getProcessSize());
                    holes.get(i).setProcessSize(holes.get(i).getProcessSize()-X.getProcessSize());
                    //fix the holes
                    memProcess.fix_holes(holes);
                    mp_arr.add(X);
                    //add it to the table
                    table.getItems().add(X);
                    found=true;
                    warning.setText("process with id "+X.getProcessId()+" allocated at "+X.getProcessStart());
                    break;
                }
            }
            draw_mem2();
        }
        if(!found)
        {
            warning.setText("process can't be allocated, try free memory to allocate");
        }
        else
        {
            P_ID_to_in.clear();
            P_Size_to_in.clear();
        }

    }
    public void DEALLOCATE_PROCESS()
    {

            ObservableList<memProcess> selected,all;
            all=table.getItems();
            selected=table.getSelectionModel().getSelectedItems();
            int selected_start=selected.get(0).getProcessStart();
            //removing it from the array
            for(int i=0;i<mp_arr.size();i++)
            {
                if(mp_arr.get(i).getProcessStart()==selected_start)
                {
                    memProcess X=new memProcess("EMPTY",mp_arr.get(i).getProcessStart(),mp_arr.get(i).getProcessSize());
                    holes.add(X);
                    memProcess.fix_holes(holes);
                    mp_arr.remove(i);
                    break;
                }
            }
        for(int i=0;i<added_arr.size();i++)
        {
            if(added_arr.get(i).getProcessStart()==selected_start)
            {
                memProcess X=new memProcess("EMPTY",added_arr.get(i).getProcessStart(),added_arr.get(i).getProcessSize());
                holes.add(X);
                memProcess.fix_holes(holes);
                added_arr.remove(i);
                break;
            }
        }
        warning.setText("precess with id: "+selected.get(0).getProcessId()+" is de-allocated");
            selected.forEach(all::remove);

        draw_mem2();

    }

    public void draw_mem2()
    {

        if(mem_size.isEditable())
        {
            //initially the memory is full
            added_arr.add(new memProcess("P",0,Integer.parseInt(mem_size.getText())));
            mem_size.setEditable(false);
        }
        else {
            //clear the initial processes array
            added_arr.clear();
            addresses.clear();
        }
        table.getItems().clear();
        draw_area.getChildren().clear();
        mem.clear();
        HBox H_Box=new HBox(5);
        VBox V_Box1=new VBox();
        VBox V_Box2=new VBox();
        int totalSize=(((int) draw_area.getHeight()))-5;

        int currentSize=0;
        while(currentSize!=Integer.parseInt(mem_size.getText()))
        {
            Boolean found=false;
            for(int i=0;i<mp_arr.size();i++)
            {
                if(mp_arr.get(i).getProcessStart()==currentSize)
                {
                    found=true;
                    memProcess X=new memProcess(mp_arr.get(i).getProcessId(),currentSize,mp_arr.get(i).getProcessSize());
                    mem.add(X);
                    currentSize+=mp_arr.get(i).getProcessSize();
                    break;
                }
            }
            if(!found)
            {
                for(int i=0;i<holes.size();i++)
                {
                    if(holes.get(i).getProcessStart()==currentSize)
                    {
                        found=true;
                        memProcess X=new memProcess(holes.get(i).getProcessId(),currentSize,holes.get(i).getProcessSize());
                        mem.add(X);
                        currentSize+=holes.get(i).getProcessSize();
                        break;
                    }
                }
            }
            if(!found)
            {
                int nextS1=Integer.parseInt(mem_size.getText());
                int nextS2=Integer.parseInt(mem_size.getText());
                for(int i=0;i<mp_arr.size();i++)
                {
                    if(mp_arr.get(i).getProcessStart()>currentSize)
                    {
                        nextS1=mp_arr.get(i).getProcessStart();
                        break;
                    }
                }
                for(int i=0;i<holes.size();i++)
                {
                    if(holes.get(i).getProcessStart()>currentSize)
                    {
                        nextS2=holes.get(i).getProcessStart();
                        break;
                    }
                }

                memProcess X=new memProcess("P",currentSize,Math.min(nextS1,nextS2)-currentSize);
                added_arr.add(X);
                mem.add(X);
                currentSize+=X.getProcessSize();
            }
        }
        for(memProcess x:mem)
        {
            Label memLocation=new Label(x.getProcessId());
            memLocation.setPrefSize(50, ((double) (x.getProcessSize()) / Integer.parseInt(mem_size.getText())) * totalSize);
            if(x.getProcessId()=="P")
            {
                memLocation.setStyle("-fx-border-color: black;-fx-background-color:#000000");
            }
            else
            {
                memLocation.setStyle("-fx-border-color: black");
            }
            V_Box1.getChildren().add(memLocation);
            addresses.add(x.getProcessId());
        }
        for(memProcess x:mem)
        {
            Label memAddress=new Label(Integer.toString(x.getProcessStart()));
            Label memAdjust=new Label();
          //  memAddress.setPrefSize(50, ((double) (x.getProcessSize()) / Integer.parseInt(mem_size.getText())) * totalSize);
          //  memAddress.setStyle("-fx-display:flex;-fx-align-items:start");
            memAdjust.setPrefSize(50,(((double) (x.getProcessSize()) / Integer.parseInt(mem_size.getText())) * totalSize));
            V_Box2.getChildren().addAll(memAddress,memAdjust);
        }
        V_Box2.getChildren().add(new Label(mem_size.getText()));
        H_Box.getChildren().addAll(V_Box1,V_Box2);
        draw_area.getChildren().add(H_Box);
        //drawing the table
        for(memProcess x:mem)
        {
            if(x.getProcessId()=="EMPTY")
                continue;
            table.getItems().add(x);
        }
    }


}
