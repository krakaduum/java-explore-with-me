package ru.practicum.stats.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ViewStats {

    String app;

    String uri;

    Long hits;

}
