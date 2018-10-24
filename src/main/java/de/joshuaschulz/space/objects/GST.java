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
        this.category = new SimpleStringProperty(category);
    }
}
