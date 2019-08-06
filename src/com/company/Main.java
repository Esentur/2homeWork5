package com.company;

import java.util.concurrent.CountDownLatch;

public class Main {
    // Количество клиентов у гида
    private static final int CLIENTS_COUNT = 15;
    // Объект синхронизации
    private static CountDownLatch LATCH;

    public static void main(String[] args) throws InterruptedException {
        // Определение объекта синхронизации
        LATCH = new CountDownLatch(CLIENTS_COUNT+1);
        // Создание потоков туристов
        for (int i = 1; i <= CLIENTS_COUNT; i++) {
            Guide guide = new Guide(i);
            guide.start();
            Thread.sleep(200);
        }

        // Проверка наличия всех туристов на старте
        while (LATCH.getCount() > 1) {
            Thread.sleep(100);
        }

        Thread.sleep(1000);
        System.out.println("Все собрались?Так начнем же наш тур!");
        LATCH.countDown();  // Уменьшаем счетчик на 1


        // Счетчик обнулен, и все ожидающие этого события
        // потоки разблокированы
    }
    public static class Guide extends Thread {
        private int hasClients;

        public Guide(int hasClients) {
            this.hasClients = hasClients;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Туристр %d записался на тур \n",
                        hasClients);
                // Уменьшаем счетчик на 1
                LATCH.countDown();

                // Метод await блокирует поток до тех пор, пока
                // счетчик CountDownLatch не обнулится
                LATCH.await();
                // Ожидание, пока турист не пройдет тур
                Thread.sleep(2000);
                System.out.printf("Турист %d прошел тур-программу\n", hasClients);
            } catch (InterruptedException e) {
            }
        }
    }
}
