package de.joshuaschulz.space.objects;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GST extends RecursiveTreeObject<GST> {
    public StringProperty date;
    public StringProperty time;
    public StringProperty category;

    public GST(String date, String time, String category){
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        switch (category){
            case "5":
                this.category = new SimpleStringProperty("G1");
                break;
            case "6":
                this.category = new SimpleStringProperty("G2");
                break;
            case "7":
                this.category = new SimpleStringProperty("G3");
                break;
            case "8":
                this.category = new SimpleStringProperty("G4");
                break;
            case "9":
                this.category = new SimpleStringProperty("G5");
                break;
                default:
                    this.category = new SimpleStringProperty("unknown");
        }
    }
}
