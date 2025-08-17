package org.example.benchmark.ulti;

import lombok.experimental.UtilityClass;
import org.example.benchmark.model.User;

import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@UtilityClass
public class UserSimulator {
    private static final Random RANDOM = new Random();

    public Queue<User> getSimulationModels(int userNum) {
        Queue<User> modelList = new ConcurrentLinkedQueue<>();
        for (int num = 0; num < userNum; num++) {
            String userId = UUID.randomUUID().toString();
            String userName = "user" + num;

            double cpuUsage = 10 + (90 * RANDOM.nextDouble()); // từ 10% đến 100%
            long ramUsage = 50_000_000L + RANDOM.nextInt(500_000_000); // 50MB - 500MB

            User model = User.builder()
                    .userId(userId)
                    .username(userName)
                    .cpuUsage(cpuUsage)
                    .ramUsage(ramUsage)
                    .build();

            modelList.add(model);
        }
        return modelList;
    }
}
