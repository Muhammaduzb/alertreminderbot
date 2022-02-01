package uz.wishmaker.alertreminder.service;

import uz.wishmaker.alertreminder.checker.StatusChecker;

public interface AddUserFirstTime {
    StatusChecker updateUser(Integer id,String phoneNumber,Integer password);
}
