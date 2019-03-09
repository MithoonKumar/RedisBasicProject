package com.example.sharechat.controller;

import com.example.sharechat.model.User;
import com.example.sharechat.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BasicController {

    @Autowired
    private LeaderBoardService leaderBoardService;

    @RequestMapping(method = RequestMethod.GET, value = "/getRank")
    @ResponseBody
    public String getRank(@RequestParam String userId) {
        Long rank = leaderBoardService.getRank(userId);
        if (rank == null) {
            return "No user exists with this id";
        } else {
            return String.valueOf(rank);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getLeaderBoard")
    @ResponseBody
    public List<User> getLeaderBoard(@RequestParam Integer offset, @RequestParam Integer limit) {
        return leaderBoardService.getLeaderBoard(offset, limit);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/updateScore")
    @ResponseBody
    public Double updateScore(@RequestParam String userId, @RequestParam Double score) {
        return leaderBoardService.updateScore(userId, score);
    }
}
