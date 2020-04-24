package com.sayo.qa;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@SpringBootTest
public class KafkaTest {
    @Autowired
    private Producer producer;
    @Test
    public void test(){
        producer.send("sayo","hello");
        producer.send("sayo","byebye");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
@Component
class Producer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(String topic,String content){
        kafkaTemplate.send(topic,content);
    }
}
@Component
class Consumer{
    @KafkaListener(topics = "sayo")
    public void handle(ConsumerRecord record){
        System.out.println(record.value());
    }

}