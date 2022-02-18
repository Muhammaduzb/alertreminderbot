package uz.wishmaker.alertreminder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.wishmaker.alertreminder.entity.Users;
import uz.wishmaker.alertreminder.repository.UserMessageRepository;
import java.util.ArrayList;
import java.util.List;

@Component
@RestController
public class TelegramController extends TelegramLongPollingBot {
    @Autowired
    UserMessageRepository userMessageRepository;

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
//        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        Message message = null;
        try {
            message = update.getMessage();
            sendMessage.setChatId(message.getChatId().toString());
        } catch (NullPointerException e) {
            System.out.print("Caught NullPointerException");
        }
//        sendMessage.setChatId(message.getChatId().toString());
        System.out.println(message);
        System.out.println("teksti : " + message.getText());
        System.out.println("userni idisi : " + message.getChatId());
        if (userMessageRepository.findById(Integer.valueOf(message.getChatId().toString())).isPresent()) {
            Users user = userMessageRepository.findById(Integer.valueOf(message.getChatId().toString())).get();
            if (message.hasText()) {
                if (message.getText().equals("/start")) {
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("You can change your alert message. If you want press the button!");
                    setChangeMessageButton(sendMessage);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else   if (message.getText().equals("173941")) {
                        user.setAgree(false);
                        userMessageRepository.save(user);
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Congratulation, you can change your alert message. If you want press the button!");
                        setChangeMessageButton(sendMessage);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else if (message.getText().equals("Change my alert message!") ) {
                        user.setAgree(false);
                        userMessageRepository.save(user);
                        sendMessage.setText("Send your new alert text!");
                        sendMessage.setChatId(message.getChatId().toString());
                        setAlertTextPlace(sendMessage);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else if (!user.getAgree() && !message.getText().equals("\uD83D\uDEAB Cancel")) {
                                user.setNewMessage(message.getText());
                                user.setAgree(true);
                                userMessageRepository.save(user);
                                sendMessage.setText("Are you sure?");
                                sendMessage.setChatId(message.getChatId().toString());
                                setAgreement(sendMessage);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else if (message.getText().equals("Yes ,i'm sure")){
                                user.setMessage(user.getNewMessage());
                                user.setAgree(true);
                                userMessageRepository.save(user);
                                sendMessage.setChatId(message.getChatId().toString());
                                sendMessage.setText("You have changed your alert message!");
                                setChangeMessageButton(sendMessage);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else if (message.getText().equals("\uD83D\uDEAB Cancel")){
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("You can change your alert message. If you want, press the button!");
                    user.setAgree(true);
                    userMessageRepository.save(user);
                    setChangeMessageButton(sendMessage);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                } else if (message.hasContact()) {
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("You can change your alert message. If you want press the button!");
                    setChangeMessageButton(sendMessage);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (message.hasText()) {
                    if (message.getText().equals("/start")) {
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Share your phone number,please!");
                        setButtons(sendMessage);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (message.hasContact()) {
                    if (userMessageRepository.findByPhoneNumber(message.getContact().getPhoneNumber().substring(1)).isPresent()) {
                        Users user = userMessageRepository.findByPhoneNumber(message.getContact().getPhoneNumber().substring(1)).get();
                        user.setPhoneNumber(user.getPhoneNumber());
                        user.setMessage(user.getMessage());
                        userMessageRepository.deleteById(user.getId());
                        user.setId(Math.toIntExact(message.getChatId()));
                        userMessageRepository.save(user);
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Please send your password!");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Please apply to administrator!");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        }



    ReplyKeyboardMarkup replyKeyboardMarkup;
    KeyboardRow keyboardFirstRow;
    KeyboardButton keyboardButton;


    public synchronized void setButtons(SendMessage sendMessage) {

        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        keyboardFirstRow = new KeyboardRow();

        keyboardButton = new KeyboardButton();
        keyboardButton.setText("Share my number ->");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);

        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public synchronized void setChangeMessageButton(SendMessage sendMessage) {

        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        keyboardFirstRow = new KeyboardRow();

        keyboardButton = new KeyboardButton();
        keyboardButton.setText("Change my alert message!");
        keyboardFirstRow.add(keyboardButton);

        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public synchronized void setAlertTextPlace(SendMessage sendMessage) {

        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();

//        KeyboardButton keyboardButton = new KeyboardButton();
//        keyboardButton.setText("Yes ,i'm sure");
        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText("\uD83D\uDEAB Cancel");
//        keyboardFirstRow.add(keyboardButton);
        keyboardFirstRow.add(keyboardButton1);

        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public synchronized void setAgreement(SendMessage sendMessage) {

        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();

        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Yes ,i'm sure");
        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText("\uD83D\uDEAB Cancel");
        keyboardFirstRow.add(keyboardButton);
        keyboardFirstRow.add(keyboardButton1);

        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    public void sendMessagenotification() {
        SendMessage sendMessage = new SendMessage();
        Users user = userMessageRepository.findAll().get(0);

        sendMessage.setChatId("-752599555");
        sendMessage.setText(user.getMessage());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}


