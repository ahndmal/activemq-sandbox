package org.example;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MyMqService {

    public static final String WIRE_LEVEL_ENDPOINT = "tcp://localhost:61616";
    public static final String ACTIVE_MQ_USERNAME = "admin";
    public static final String ACTIVE_MQ_PASSWORD = "admin";

    public static Session init() {
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = createActiveMQConnectionFactory();

            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();

            // boolean transacted, int acknowledgeMode
            return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
        connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
        connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
        return connectionFactory;
    }


    public static void sendMessageToTopic(String topicName, Session session, String text) throws JMSException {

        Topic topic = session.createTopic(topicName);

        MessageProducer producer = session.createProducer(topic);

        TextMessage message = session.createTextMessage(text);

        producer.send(topic, message);

        System.out.printf(">>> Message '%s' sent to topic '%s'%n", message, topic);

        producer.close();

    }

    public static void sendMessage(String queueName, Session session, String text) {
        try {
            Queue queue = session.createQueue(queueName);

            System.out.println(">>> Queue name is: \u001b[32m" + queue.getQueueName() + " \u001b[0m");

            MessageProducer producer = session.createProducer(queue);

            TextMessage message = session.createTextMessage(text);

            producer.send(message);

            System.out.println(">>> Message sent: " + message);

            producer.close();
            session.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public static void receiveMessage(String queueName, Session session) {
        MessageConsumer consumer = null;
        try {
            Queue queue = session.createQueue(queueName);

            System.out.println(">>> Queue name is: \u001b[32m" + queue.getQueueName() + " \u001b[0m");

            consumer = session.createConsumer(queue);

            Message message = consumer.receive(10000);

            TextMessage textMessage = (TextMessage) message;
            System.out.println(">>> Message received:");
            System.out.println(textMessage.getText());

            textMessage.acknowledge();

            consumer.close();
            session.close();

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    private static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        // Create a pooled connection factory.
        final PooledConnectionFactory pooledConnectionFactory =
                new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
        return pooledConnectionFactory;
    }
    */

}
