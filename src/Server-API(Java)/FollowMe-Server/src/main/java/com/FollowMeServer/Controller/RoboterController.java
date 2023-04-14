package com.FollowMeServer.Controller;

import com.FollowMeServer.Entity.Roboter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoboterController {

    @Autowired
    private RoboterDAO roboterDAO;
    /* public void save(Roboter roboter){
        RoboterDAO.save(roboter);
    }

    // public void delete(Roboter roboter){
        roboterRepository.delete(roboter);
    }
    */
    @GetMapping("/roboter/getAll")
    public List<Roboter> getRobotList(){
        return roboterDAO.getAllRoboter();
    }
}
