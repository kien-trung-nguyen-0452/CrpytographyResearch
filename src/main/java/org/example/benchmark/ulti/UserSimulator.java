package org.example.benchmark.ulti;

import lombok.experimental.UtilityClass;
import org.example.benchmark.model.User;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@UtilityClass
public class UserSimulator {

    public Queue<User> getSimulationModels(int userNum) {
        Queue<User> modelList = new ConcurrentLinkedQueue<>();
        for (int num = 0; num < userNum; num++) {
            String userId = UUID.randomUUID().toString();
            String userName = "user" + num;

            User model = User.builder()
                    .userId(userId)
                    .username(userName)
                    .build();

            modelList.add(model);
        }
        return modelList;
    }
}
