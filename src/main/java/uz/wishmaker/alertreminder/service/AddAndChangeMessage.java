package uz.wishmaker.alertreminder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.wishmaker.alertreminder.checker.StatusChecker;
import uz.wishmaker.alertreminder.repository.UserMessageRepository;


public interface AddAndChangeMessage {
     StatusChecker updateMessage(Integer id,String message,boolean agree);
}
