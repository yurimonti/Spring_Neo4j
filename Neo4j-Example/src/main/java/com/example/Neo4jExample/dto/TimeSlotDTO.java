package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.TimeSlot;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a data transfer object for the class TimeSlot
 */
@Data
public class TimeSlotDTO {
    private Collection<String> monday;
    private Collection<String> tuesday;
    private Collection<String> wednesday;
    private Collection<String> thursday;
    private Collection<String> friday;
    private Collection<String> saturday;
    private Collection<String> sunday;
    private boolean isOpen;

    public TimeSlotDTO(){
        this.monday = new ArrayList<>();
        this.tuesday = new ArrayList<>();
        this.wednesday = new ArrayList<>();
        this.thursday = new ArrayList<>();
        this.friday = new ArrayList<>();
        this.saturday = new ArrayList<>();
        this.sunday = new ArrayList<>();
        this.isOpen = false;
    }

    public TimeSlotDTO(TimeSlot timeSlot){
        this();
        timeSlot.getMonday().forEach(localTime -> this.monday.add(localTime.toString()));
        timeSlot.getTuesday().forEach(localTime -> this.tuesday.add(localTime.toString()));
        timeSlot.getWednesday().forEach(localTime -> this.wednesday.add(localTime.toString()));
        timeSlot.getThursday().forEach(localTime -> this.thursday.add(localTime.toString()));
        timeSlot.getFriday().forEach(localTime -> this.friday.add(localTime.toString()));
        timeSlot.getSaturday().forEach(localTime -> this.saturday.add(localTime.toString()));
        timeSlot.getSunday().forEach(localTime -> this.sunday.add(localTime.toString()));
        this.isOpen = timeSlot.getIsOpen();
    }
}
