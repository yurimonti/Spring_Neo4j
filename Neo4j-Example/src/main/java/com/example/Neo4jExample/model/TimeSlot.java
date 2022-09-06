package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a time slot
 */
@Data
@Node
public class TimeSlot {
    @Id @GeneratedValue
    private Long id;

    private Collection<LocalTime> monday;
    private Collection<LocalTime> tuesday;
    private Collection<LocalTime> wednesday;
    private Collection<LocalTime> thursday;
    private Collection<LocalTime> friday;
    private Collection<LocalTime> saturday;
    private Collection<LocalTime> sunday;

    private Boolean isOpen;

    public TimeSlot(){
        this.monday = new ArrayList<>();
        this.tuesday = new ArrayList<>();
        this.wednesday = new ArrayList<>();
        this.thursday = new ArrayList<>();
        this.friday = new ArrayList<>();
        this.saturday = new ArrayList<>();
        this.sunday = new ArrayList<>();
        this.isOpen = false;
    }

    public TimeSlot(
            Collection<LocalTime> monday,Collection<LocalTime> tuesday,Collection<LocalTime> wednesday,
            Collection<LocalTime> thursday,Collection<LocalTime> friday,Collection<LocalTime> saturday,
            Collection<LocalTime> sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.isOpen = false;
    }

    /*private String startDayOfWeek;
    private String endDayOfWeek;
    private String startTime;
    private String endTime;

    private Boolean isOpen;

    public TimeSlot(String startDayOfWeek, String endDayOfWeek, String startTime, String endTime) {
        this.startDayOfWeek = startDayOfWeek;
        this.endDayOfWeek = endDayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }*/
}
