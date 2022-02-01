package uz.wishmaker.alertreminder.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wishmaker.alertreminder.checker.StatusChecker;
import uz.wishmaker.alertreminder.entity.Users;
import uz.wishmaker.alertreminder.repository.UserMessageRepository;
import uz.wishmaker.alertreminder.service.AddAndChangeMessage;

@Service
public class ChangeMessageService implements AddAndChangeMessage {

   @Autowired
    UserMessageRepository userMessageRepository;


    @Override
    public StatusChecker updateMessage(Integer id, String message, boolean agree) {
        StatusChecker statusChecker = new StatusChecker();
        if (userMessageRepository.findById(id).isPresent()) {
            Users user = userMessageRepository.getById(id);
            user.setMessage(message);
            user.setAgree(true);
            userMessageRepository.save(user);
            statusChecker.setStatus(true);
            statusChecker.setMessage("successfull");
        }else {
            statusChecker.setStatus(false);
            statusChecker.setMessage("problem");
        }

        return statusChecker;
    }
}
