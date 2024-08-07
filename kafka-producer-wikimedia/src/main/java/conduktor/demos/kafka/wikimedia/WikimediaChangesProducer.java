package conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {

    public static void main(String[] args) throws InterruptedException {

        String bootstrapServers = "127.0.0.1:9092";

        // create Producer properties
        // create producer properties

        Properties properties = new Properties();
        // set producer properties
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); // connecting to localhost
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // this means the producer is expecting strings as keys and values
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());


        // set safe (idempotent) producer configs (for Kafka versions <= 2.8.0)
        //properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
        //properties.setProperty(ProducerConfig.ACKS_CONFIG,"all"); // same as setting - 1
        //properties.setProperty(ProducerConfig.RETRIES_CONFIG,Integer.toString(Integer.MAX_VALUE));

        // set high throughput producer configs
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG,"20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,Integer.toString(32*1024)); // 32 kb instead of default 16
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");



        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "wikimedia.recentchange";

        BackgroundEventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        EventSource.Builder eventSourceBuilder = new EventSource.Builder(URI.create(url));

        BackgroundEventSource.Builder builder = new BackgroundEventSource.Builder(eventHandler,eventSourceBuilder);
        // builds the event source and send the info from the stream URL to the built eventHandler which will then consume messages to our producer
        BackgroundEventSource eventSource = builder.build();

        // start the producer in another thread
        eventSource.start();

        // we produce for 10 minutes and block the program until then
        TimeUnit.MINUTES.sleep(10);
    }
}

