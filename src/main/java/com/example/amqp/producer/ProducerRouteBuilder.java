package com.example.amqp.producer;

import static org.apache.camel.LoggingLevel.INFO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProducerRouteBuilder extends RouteBuilder {

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void configure() throws Exception {

        AtomicInteger counter = new AtomicInteger(0);

        from("timer:timerProducer?period={{period}}")
            .id("amqp-producer")
            .process(exchange -> {
                exchange.getIn().setBody(
                    "messageId="+counter.getAndIncrement()
                    +", time="+LocalDateTime.now().format(timeFormatter));
            })
            .log(INFO, "Sending: ${body}")
            .to("amqp:{{destination}}");
    }
}
