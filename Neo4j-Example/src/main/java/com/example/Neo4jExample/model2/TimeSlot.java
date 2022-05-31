package com.example.Neo4jExample.model2;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@NoArgsConstructor
@Node
public class TimeSlot {
    @Id @GeneratedValue
    private Long id;
    private String startDayOfWeek;
    private String endDayOfWeek;
    private String startTime;
    private String endTime;

    private Boolean isOpen;

    public TimeSlot(String startDayOfWeek, String endDayOfWeek, String startTime, String endTime) {
        this.startDayOfWeek = startDayOfWeek;
        this.endDayOfWeek = endDayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
