package com.example.Neo4jExample.service.util;

import com.example.Neo4jExample.service.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final UtilityService utilityService;


    @Scheduled(fixedRate = 60000,initialDelay = 15000)
    public void printScemo() {
        this.utilityService.updateOpenPois(new Date());
        log.info("pois time open updated!!");
    }
}
