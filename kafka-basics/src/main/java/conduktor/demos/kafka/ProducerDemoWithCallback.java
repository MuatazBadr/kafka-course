package conduktor.demos.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer!");


        // create producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092"); // connecting to localhost


        // set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName()); // this means the producer is expecting strings as keys and values
        properties.setProperty("value.serializer",StringSerializer.class.getName());
        properties.setProperty("batch.size", "400"); //we usually keep the default kafka batch sizewhich is 16kbs

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);


        // producing multiple messages

        for (int j=0; j<10; j++) {
            for (int i=0; i<30; i++) {
                // create a producer Record
                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>("demo_java", "hello world i am: " + i);

                // sending data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        // executes every time a record gets successfully sent or an exception is thrown
                        if (e == null) {
                            log.info("Received new meta data \n" +
                                    "Topic: " + metadata.topic() + "\n" +
                                    "Partition: " + metadata.partition() + "\n" +
                                    "Offset: " + metadata.offset() + "\n" +
                                    "Timestampp: " + metadata.timestamp());
                        } else {
                            log.error("Error while producing", e);
                        }
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



        // tell the producer to send all data and block until done -- synchronous
        producer.flush();

        // flush and close the producer
        producer.close(); // this also flushes the producer before closing
    }
}
