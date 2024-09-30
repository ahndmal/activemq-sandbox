package org.example;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Main {

    public static final String WIRE_LEVEL_ENDPOINT = "tcp://localhost:61616";
    public static final String ACTIVE_MQ_USERNAME = "admin";
    public static final String ACTIVE_MQ_PASSWORD = "admin";

    public static void main(String[] args) {

        try {

            ActiveMQConnectionFactory activeMQConnectionFactory = createActiveMQConnectionFactory();

            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();

            // boolean transacted, int acknowledgeMode
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);


            // ---------------- Actions

//            sendMessageToTopic("TEST-1", session, "Lorem...");

//            sendMessage("TEST-1", session, "Lorem...");

//            receiveMessage("TEST-1", session);


            session.close();
            connection.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private static void sendMessageToTopic(String topicName, Session session, String text) throws JMSException {

        Topic topic = session.createTopic(topicName);

        MessageProducer producer = session.createProducer(topic);

        TextMessage message = session.createTextMessage(text);

        producer.send(topic, message);

        System.out.printf(">>> Message '%s' sent to topic '%s'%n", message, topic);

        producer.close();

    }

    private static void sendMessage(String queueName, Session session, String text) throws JMSException {
        Queue queue = session.createQueue(queueName);

        System.out.println(">>> Queue name is: \u001b[32m" + queue.getQueueName() + " \u001b[0m");

        MessageProducer producer = session.createProducer(queue);

        TextMessage message = session.createTextMessage(text);

        producer.send(message);

        System.out.println(">>> Message sent: " + message);

        producer.close();
        session.close();

    }

    private static void receiveMessage(String queueName, Session session) throws JMSException {
        Queue queue = session.createQueue(queueName);

        System.out.println(">>> Queue name is: \u001b[32m" + queue.getQueueName() + " \u001b[0m");

        MessageConsumer consumer = session.createConsumer(queue);

        Message message = consumer.receive(10000);

        TextMessage textMessage = (TextMessage) message;
        System.out.println(">>> Message received:");
        System.out.println(textMessage.getText());

        textMessage.acknowledge();

        consumer.close();
        session.close();
    }

    private static ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
        connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
        connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
        return connectionFactory;
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