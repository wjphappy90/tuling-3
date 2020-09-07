package com.tuling.compent;

import lombok.Data;

import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * Created by smlz on 2019/12/16.
 */
@Data
public class TulingTimeBetweenConfig {

    private LocalTime startTime;

    private LocalTime endTime;

    public static void main(String[] args) {
        System.out.println(ZonedDateTime.now());
    }

}
