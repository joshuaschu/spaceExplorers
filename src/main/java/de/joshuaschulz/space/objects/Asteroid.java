package de.joshuaschulz.space.objects;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Asteroid extends RecursiveTreeObject<Asteroid> {
    public StringProperty name;
    public StringProperty approach;
    public StringProperty hazardous;

    public Asteroid(String name, String approach, String hazardous){
        this.name = new SimpleStringProperty(name);
        this.approach = new SimpleStringProperty(approach);
        this.hazardous = new SimpleStringProperty(hazardous);
    }
}