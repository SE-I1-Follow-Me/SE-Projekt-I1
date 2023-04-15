package com.FollowMeServer.Controller;

import com.FollowMeServer.Entity.Roboter;
import com.FollowMeServer.Repository.RoboterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoboterDAO {

    @Autowired
    private RoboterRepository roboterRepository;

    public List<Roboter> getAllRoboter(){
        return roboterRepository.findAll();
    }
}
