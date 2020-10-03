package com.example.trendyol.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author created by cengizhan on 3.10.2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "test")
@EqualsAndHashCode(of = {"id"})
public class Test implements Serializable {
    @Id
    private String id;
    private String deepLink;
    private String webLink;
    private String processType;

}
