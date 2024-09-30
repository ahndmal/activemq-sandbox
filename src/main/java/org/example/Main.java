package org.example;


import javax.jms.Session;

public class Main {

    public static void main(String[] args) {

        Session session = MyMqService.init();

//        MyMqService.sendMessage("TEST-1", session, "lorem...");

        MyMqService.receiveMessage("TEST-1", session);


    }

}
