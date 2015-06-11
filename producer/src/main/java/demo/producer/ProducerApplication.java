package demo.producer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Bean
    Queue queue() {
        return new Queue("queue", false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("queue");
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }

    @Autowired
    private Sender sender;

    @Override
    public void run(String... args) throws Exception {
        sender.sendToRabbitmq(new Foo(), new Bar());
    }
}

@Service
class Sender {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    @Autowired
    private MappingJackson2MessageConverter mappingJackson2MessageConverter;

    public void sendToRabbitmq(final Foo foo, final Bar bar) {

        this.rabbitMessagingTemplate.setMessageConverter(this.mappingJackson2MessageConverter);

        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue", foo);
        this.rabbitMessagingTemplate.convertAndSend("exchange", "queue", bar);

    }
}

class Bar {
    public int age = 33;
}

class Foo {
    public String name = "gustavo";
}
