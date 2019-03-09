package com.example.sharechat.service;

import com.example.sharechat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.*;

@Service
public class LeaderBoardService {

    @Autowired
    private Jedis jedis;

    @Value("${redis.sorted.set.name}")
    private String sortedSetName;

    public Long getRank(String userId) {
        Long totalUsers = jedis.zcard(sortedSetName);
        Double score = jedis.zscore(sortedSetName, userId);
        if (score == null) {
            return null;
        }
        Set<Tuple> set = jedis.zrangeByScoreWithScores(sortedSetName, score, score);
        List<User> users = new ArrayList<>();
        set.forEach(e -> {
            User user = new User();
            user.setScore(e.getScore());
            user.setUserId(e.getElement());
            users.add(user);
        });
        String lastElementUserId = users.get(users.size() - 1).getUserId();
        Long zRankLastElement = jedis.zrank(sortedSetName, lastElementUserId);
        Long rankOfLastElement = totalUsers - zRankLastElement;
        return rankOfLastElement;
    }

    public List<User> getLeaderBoard(Integer offset, Integer limit) {
        Set<Tuple> set = jedis.zrevrangeByScoreWithScores(sortedSetName, Double.MAX_VALUE, -1*Double.MAX_VALUE, offset, limit);
        List<User> users = new ArrayList<>();
        set.forEach(e -> {
            User user = new User();
            user.setScore(e.getScore());
            user.setUserId(e.getElement());
            users.add(user);
        });
        for (int i=0; i<users.size(); i++) {
            if (i==0) {
                users.get(i).setRank(getRank(users.get(i).getUserId()).intValue());
            } else {
                if (users.get(i-1).getScore() == users.get(i).getScore()) {
                    users.get(i).setRank(users.get(i-1).getRank());
                } else {
                    users.get(i).setRank(getRank(users.get(i).getUserId()).intValue());
                }
            }
        }
        return users;
    }

    public Double updateScore(String userId, Double score) {
        Double currentScore = jedis.zscore(sortedSetName, userId);
        if (currentScore != null) {
            jedis.zincrby(sortedSetName, -1*currentScore, userId);
        }
        return jedis.zincrby(sortedSetName, score, userId);
    }

}
