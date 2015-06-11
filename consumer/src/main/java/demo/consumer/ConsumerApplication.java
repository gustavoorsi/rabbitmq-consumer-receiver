package demo.consumer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableRabbit
public class ConsumerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Autowired
    private Receiver receiver;

    @Override
    public void run(String... args) throws Exception {

    }

}

@Service
class Receiver {
    @RabbitListener(queues = "queue")
    public void receiveMessage(Foo foo) {
        System.out.println("Received <" + foo.name + ">");
    }

    @RabbitListener(queues = "queue")
    public void receiveMessage(Bar bar) {
        System.out.println("Received <" + bar.age + ">");
    }
}

class Foo {
    public String name;
}

class Bar {
    public int age;
}
