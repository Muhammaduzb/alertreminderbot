package uz.wishmaker.alertreminder.checker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusChecker {
    Boolean status;
    String message;
}
