package com.example.Neo4jExample.service.util;

import com.example.Neo4jExample.service.ProvaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ProvaService provaService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


    @Scheduled(fixedRate = 60000,initialDelay = 15000)
    public void printScemo() {
        provaService.updateOpenPois(new Date());
        log.info("pois time open updated!!");
    }
}
