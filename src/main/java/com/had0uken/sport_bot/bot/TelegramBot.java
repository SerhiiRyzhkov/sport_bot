package com.had0uken.sport_bot.bot;

import com.had0uken.sport_bot.config.BotConfig;
import com.had0uken.sport_bot.model.User;
import com.had0uken.sport_bot.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserService userService;
    private final BotConfig config;
    private static final String ERROR_TEXT = "Error occurred: ";

    private static final String BACK_BUTTON = "Back button";
    private static final String HELP_TEXT = """
            Hi there! 

            Type /start to see a welcome message

            Type /help to see this message again
                        
            Type /deletedata to delete all your personal data that we keep
                        
            Type /mydata to view the information that we keep about you

            """;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private void startCommandReceived(long chatID, String name, EditMessageText editMessageText) {

    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        /*if(update.hasMessage() && update.getMessage().hasText())
            handleCommand(update);*/
    }




    private void handleCommand(Update update) throws TelegramApiException {
        String messageText = update.getMessage().getText();
        long chatID = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatID);

        switch (messageText) {
            case "/start" -> {
                registerUser(update);
                message.setText("Hello, " + update.getMessage().getChat().getFirstName() + "!");
            }

            case "/help" -> {
                message.setText(HELP_TEXT);
            }

            case "/mydata" -> {
                message.setText(userService.getData(update.getMessage().getChatId()));
            }

            case "/deletedata" -> {
                if(userService.isExist(update.getMessage().getChatId())) {
                    userService.delete(update.getMessage().getChatId());
                    message.setText("We deleted all your data");
                }
                else
                    message.setText("We do not keep any of your data");
            }

            default -> {
                message.setText("Sorry, command was not recognized");
            }
        }
        execute(message);
    }

    private void registerUser(Update update) {
        Message message = update.getMessage();
        if(!userService.isExist(message.getChatId())) {
            Chat chat = message.getChat();
            User user = new User(message.getChatId(), chat.getFirstName(), chat.getLastName(),
                    chat.getUserName(), new java.sql.Timestamp(System.currentTimeMillis()),new HashSet<>());
            userService.save(user);
            log.info("user saved: " + user);
        }
    }


}


