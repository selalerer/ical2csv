package com.selalerer.ical2csv.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RepeatRule {
    private String frequency;
    private String weekStart;
    private LocalDateTime until;
    private int interval = 1;
}
