package com.had0uken.sport_bot.bot;

import com.had0uken.sport_bot.config.BotConfig;
import com.had0uken.sport_bot.model.Game;
import com.had0uken.sport_bot.model.League;
import com.had0uken.sport_bot.model.Team;
import com.had0uken.sport_bot.model.User;
import com.had0uken.sport_bot.repository.LeagueRepository;
import com.had0uken.sport_bot.repository.UserRepository;
import com.had0uken.sport_bot.service.LeagueService;
import com.had0uken.sport_bot.service.TeamService;
import com.had0uken.sport_bot.service.UserService;
import com.had0uken.sport_bot.utility.JsonGetter;
import com.had0uken.sport_bot.utility.JsonHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private LeagueService leagueService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
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
        listOfCommands.add(new BotCommand("/test", "test"));

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



    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText())
            handleCommand(update);
        else if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();

            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId((int) messageId);
            editMessageText.setChatId(chatId);

            if(callbackQuery.getData().matches("league_\\d+"))handleLeague(callbackQuery, editMessageText);
            if(callbackQuery.getData().matches("view_team_\\d+"))handleBackButton(callbackQuery,editMessageText);
            if(callbackQuery.getData().matches("team_\\d+"))handleTeam(callbackQuery,editMessageText);
            if(callbackQuery.getData().matches("del_team_\\d+"))deleteTeamFromUserList(callbackQuery,editMessageText);
            switch (callbackQuery.getData()) {
                case BACK_BUTTON ->
                    handleBackButton(callbackQuery,editMessageText);

                case "addTeam" ->
                    addTeam(callbackQuery, editMessageText);

                case "deleteTeam" ->
                    deleteTeam(callbackQuery,editMessageText);

                case "viewTeam" -> {
                    viewTeam(callbackQuery,editMessageText);
                }
                case "checkResults" -> {
                    checkResults(callbackQuery,editMessageText);
                }



                default -> {

                }
            }
        }
    }

    private void checkResults(CallbackQuery callbackQuery, EditMessageText editMessageText) throws IOException, TelegramApiException {
        User user = extractUser(callbackQuery);
        List<Team> teams = user.getTeams();
        if(teams.isEmpty()){
            editMessageText.setText("Yor do not have teams in yor list. Add teams to observe their results");
        }
        else {
            String date = "20231211";
            List<Game> games = JsonHandler.parseResultsFromJson(JsonGetter.getLocalJsonResultsByDate(date));

            StringBuilder sb = new StringBuilder();
            for (Game game : games)
                if (teams.contains(game.getTeam1()) || teams.contains(game.getTeam2()))
                    sb.append(game).append("\n");
            editMessageText.setText(!sb.isEmpty() ? sb.toString() : "Today your teams did not play");
        }
        editMessageText.setReplyMarkup(getBackButtonKit(user,null));
        execute(editMessageText);
    }

    private void viewTeam(CallbackQuery callbackQuery, EditMessageText editMessageText) throws TelegramApiException {
        editMessageText.setText("Your teams:");
        editMessageText.setReplyMarkup(getButtonsUserTeam(extractUser(callbackQuery),"view_team_"));
        execute(editMessageText);
    }

    private void deleteTeamFromUserList(CallbackQuery callbackQuery, EditMessageText editMessageText) {
        User user = extractUser(callbackQuery);
        Long id = Long.valueOf(callbackQuery.getData().substring(9));
        Optional<Team> teamOptional = teamService.getTeamById(id);
        teamOptional.ifPresent(team -> userService.deleteTeam(user, team));
        handleBackButton(callbackQuery,editMessageText);
    }

    private void deleteTeam(CallbackQuery callbackQuery, EditMessageText editMessageText) throws TelegramApiException {
        editMessageText.setText("Select team to delete");
        editMessageText.setReplyMarkup(getButtonsUserTeam(extractUser(callbackQuery),"del_team_"));
        execute(editMessageText);
    }



    private void addTeam(CallbackQuery callbackQuery, EditMessageText editMessageText) throws TelegramApiException {
        editMessageText.setText("Select country:");
        editMessageText.setReplyMarkup(getButtonsLeagues());
        execute(editMessageText);
    }


    private void handleLeague(CallbackQuery callbackQuery, EditMessageText editMessageText) throws TelegramApiException {
        editMessageText.setText("Select a team");
        Long id = Long.valueOf(callbackQuery.getData().substring(7));
        Optional<League> leagueOptional = leagueService.getLeagueById(id);
        if(leagueOptional.isPresent()){
            editMessageText.setReplyMarkup(getButtonsTeams(leagueOptional.get()));
        }
        else {
            handleBackButton(callbackQuery,editMessageText);
        }
        execute(editMessageText);
    }

    private void handleTeam(CallbackQuery callbackQuery, EditMessageText editMessageText) {
        long userID = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(chatId);
        getChatMember.setUserId(userID);
        try {
            ChatMember chatMember = execute(getChatMember);
            Long id = chatMember.getUser().getId();
            Optional<User> userOptional = userService.getUserById(id);
            if(userOptional.isPresent()){
                saveTeam(callbackQuery,userOptional.get(),editMessageText);
            }
        }
        catch (TelegramApiException e) {
        log.error(ERROR_TEXT + e.getMessage());
        }
        handleBackButton(callbackQuery,editMessageText);

    }
    private void saveTeam(CallbackQuery callbackQuery, User user, EditMessageText editMessageText) throws TelegramApiException {
        Optional<Team> teamOptional = teamService.getTeamById(Long.valueOf(callbackQuery.getData().substring(5)));
        teamOptional.ifPresent(team -> userService.addTeam(user, team));
        handleBackButton(callbackQuery,editMessageText);
    }

    private void handleBackButton(CallbackQuery callbackQuery, EditMessageText editMessageText) {
        long userID = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(chatId);
        getChatMember.setUserId(userID);
        try {
            ChatMember chatMember = execute(getChatMember);
            String firstName = chatMember.getUser().getFirstName();
            editMessageText.setReplyMarkup(getStartButtonsKit());
            editMessageText.setText("Hello, " + firstName + "!");
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }


    private void handleCommand(Update update) throws TelegramApiException, IOException {
        String messageText = update.getMessage().getText();
        long chatID = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatID);

        switch (messageText) {
            case "/start" -> {
                registerUser(update);
                message.setReplyMarkup(getStartButtonsKit());
                message.setText("Hello, " + update.getMessage().getChat().getFirstName() + "!");
            }

            case "/help" -> {
                message.setText(HELP_TEXT);
            }

            case "/mydata" -> {
                message.setText(userService.getData(update.getMessage().getChatId()));
            }

            case "/deletedata" -> {
                message.setText(userService.delete(update.getMessage().getChatId()));
            }

            case "/test" ->{
                commitTest();
            }

            default -> {
                message.setText("Sorry, command was not recognized");
            }
        }
        execute(message);
    }

    private void commitTest() throws IOException {
        JsonHandler.parseResultsFromJson(JsonGetter.getLocalJsonResultsByDate("20231211"));
    }

    private void registerUser(Update update) {
        Message message = update.getMessage();
        if(!userService.isExist(message.getChatId())) {
            Chat chat = message.getChat();
            User user = new User(message.getChatId(), chat.getFirstName(), chat.getLastName(),
                    chat.getUserName(), new java.sql.Timestamp(System.currentTimeMillis()),new ArrayList<>());
            userService.save(user);
            log.info("user saved: " + user);
        }
    }


    private InlineKeyboardMarkup getStartButtonsKit(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();

        button1.setText("Add new team");
        button2.setText("Delete a team");
        button3.setText("View my teams");
        button4.setText("Check results");



        button1.setCallbackData("addTeam");
        button2.setCallbackData("deleteTeam");
        button3.setCallbackData("viewTeam");
        button4.setCallbackData("checkResults");

        rowInLine1.add(button1);
        rowInLine1.add(button2);
        rowInLine1.add(button3);
        rowsInline.add(rowInLine1);

        rowInLine2.add(button4);
        rowsInline.add(rowInLine2);

        markup.setKeyboard(rowsInline);
        return markup;
    }

    private InlineKeyboardMarkup getButtonsLeagues(){
        List<League> leagues = leagueService.getAllLeagues();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for(League l: leagues)
        {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(l.getLeague_name());
            button.setCallbackData("league_"+l.getLeague_id());
            rowInLine.add(button);
            rowsInline.add(rowInLine);
        }
        rowsInline.add(getBackButton());
        markup.setKeyboard(rowsInline);
        return markup;
    }

    private InlineKeyboardMarkup getButtonsTeams(League league){
        List<Team> teams = league.getTeams();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for(Team t: teams)
        {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(t.getTEAM_NAME());
            button.setCallbackData("team_"+t.getTEAM_ID());
            rowInLine.add(button);
            rowsInline.add(rowInLine);
        }
        rowsInline.add(getBackButton());
        markup.setKeyboard(rowsInline);
        return markup;
    }

    private InlineKeyboardMarkup getButtonsUserTeam(User user, String prefix) {
        List<Team> teams = user.getTeams();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for(Team t: teams)
        {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(t.getTEAM_NAME());
            button.setCallbackData(prefix+t.getTEAM_ID());
            rowInLine.add(button);
            rowsInline.add(rowInLine);
        }
        rowsInline.add(getBackButton());
        markup.setKeyboard(rowsInline);
        return markup;
    }

    private InlineKeyboardMarkup getBackButtonKit(User user, String prefix) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(getBackButton());
        markup.setKeyboard(rowsInline);
        return markup;
    }



    private List<InlineKeyboardButton> getBackButton() {
        List<InlineKeyboardButton> backButtonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("<< BACK TO MAIN MENU");
        button.setCallbackData(BACK_BUTTON);
        backButtonList.add(button);
        return backButtonList;
    }

    private User extractUser(CallbackQuery callbackQuery){
        ChatMember chatMember = null;
        try {
            long userID = callbackQuery.getFrom().getId();
            long chatId = callbackQuery.getMessage().getChatId();
            GetChatMember getChatMember = new GetChatMember();
            getChatMember.setChatId(chatId);
            getChatMember.setUserId(userID);
            chatMember = execute(getChatMember);

        }
        catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
        Long id = chatMember.getUser().getId();
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.orElse(null);
    }


}


