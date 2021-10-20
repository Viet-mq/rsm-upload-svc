package com.edso.resume.file.domain.entities;

import com.rabbitmq.client.AMQP;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CV extends AMQP.BasicProperties {
    private String id;
    private String url;
    private String fileName;
}
