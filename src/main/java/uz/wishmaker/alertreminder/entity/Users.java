package uz.wishmaker.alertreminder.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Users {
    @Id
    private Integer id;
    private String phoneNumber;
    private String message;
    private Boolean agree;
    private Integer password;
    private String newMessage;
}
