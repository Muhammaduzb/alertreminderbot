package uz.wishmaker.alertreminder.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wishmaker.alertreminder.checker.StatusChecker;
import uz.wishmaker.alertreminder.entity.Users;
import uz.wishmaker.alertreminder.repository.UserMessageRepository;
import uz.wishmaker.alertreminder.service.AddUserFirstTime;

@Service
public class AddUserFirstTimeService implements AddUserFirstTime {

    @Autowired
    UserMessageRepository userMessageRepository;

    @Override
    public StatusChecker updateUser(Integer id, String phoneNumber,Integer password) {
        StatusChecker statusChecker = new StatusChecker();
        if (userMessageRepository.findByPhoneNumber(phoneNumber).isPresent()){
            Users user = userMessageRepository.findByPhoneNumber(phoneNumber).get();
            if (user.getPassword().equals(password)){
            user.setId(id);
            userMessageRepository.save(user);
            statusChecker.setStatus(true);
            statusChecker.setMessage("successfull");
            }
        }else {
            statusChecker.setStatus(false);
            statusChecker.setMessage("error");
        }
        return statusChecker;
    }
}