package com.company;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Message message = new Message();

        (new Thread(new Writer(message))).start();
        (new Thread(new Reader(message))).start();
        //по този начин стартираме анонимни инстанции!!!
    }
}

class Message {

    private String message;
    private boolean empty = true;

    public synchronized String read() {

        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
        empty = true;
        notifyAll();
        return message;
    }

    public synchronized void write(String message) {

        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
        empty = false;
        notifyAll();
        this.message = message;
    }
}

class Writer implements Runnable {

    private Message message;

    public Writer(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        String[] messages = {
                "Message one",
                "Message two",
                "Message three",
                "Message four",
                "Message five"};


        Random random = new Random();

        for (String s : messages) {
            message.write(s);
        }
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {

        }
        message.write("Finished");
    }
}

class Reader implements Runnable {

    private Message message;

    public Reader(Message message) {
        this.message = message;
    }

    @Override
    public void run() {

        Random random = new Random();

        for (String latestMessage = message.read(); !latestMessage.equals("Finished"); latestMessage = message.read()) {
            System.out.println(latestMessage);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {

            }
        }
    }
}
