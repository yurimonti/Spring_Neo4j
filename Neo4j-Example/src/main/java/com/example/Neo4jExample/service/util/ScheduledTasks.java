package com.example.Neo4jExample.service.util;

import com.example.Neo4jExample.service.ProvaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final ProvaService provaService;


    @Scheduled(fixedRate = 60000,initialDelay = 15000)
    public void printScemo() {
        this.provaService.updateOpenPois(new Date());
        log.info("pois time open updated!!");
    }
}
