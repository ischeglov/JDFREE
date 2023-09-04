import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = "ВАШ_ТОКЕН";
    private static final String BOT_USERNAME = "ВАШЕ_ИМЯ_БОТА";
    private static final String URL = "https://api.nasa.gov/planetary/apod?api_key=ВАШ_КЛЮЧ_NASA";
    private static long chatId;

    private static final String HELP_TEXT = "Демонстрация моего бота:\n\n" +
            "Вы можете выполнять команды из главного меню слева или набрав команду:\n\n" +
            "Введите /start, чтобы увидеть приветственное сообщение\n\n" +
            "Введите /help, чтобы снова увидеть это сообщение\n\n" +
            "Введите /give, чтобы загрузить изображение Nasa\n\n" +
            "Введите /date, чтобы загрузить изображение Nasa на определенную дату";

    public MyTelegramBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);

        List<BotCommand> listOfCommand = new ArrayList<>();
        listOfCommand.add(new BotCommand ("/start", "Приветственное сообщение"));
        listOfCommand.add(new BotCommand("/help", "Помощь"));
        listOfCommand.add(new BotCommand("/give", "Загрузить изображение Nasa"));
        listOfCommand.add(new BotCommand("/date", "Загрузить изображение Nasa на дату"));
        try {
            this.execute(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();

            String answer = update.getMessage().getText();
            switch (answer) {
                case "/start":
                case "Старт":
                    sendMessage("Привет! Я бот, который присылает картинку от NASA");
                    break;

                case "/help":
                case "Помощь":
                    sendMessage(HELP_TEXT);
                    break;

                case "/give":
                case "Загрузить изображение":
                    try {
                        sendMessage(Utils.getURL(URL));
                        sendMessage(Utils.getName(URL));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case "/date":
                case "Введите дату":
                    sendMessage("Введите дату в формате YYYY-MM-DD:");
                    break;

                default:
                    if (answer.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        String date = answer;
                        try {
                            sendMessage(Utils.getURL(URL + "&date=" + date));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        sendMessage("Моя твоя не понимай");
            }
        }
    }

    private void sendMessage(String messageText) {
        SendMessage message = new SendMessage();

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Старт");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("Помощь");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("Загрузить изображение");

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("Введите дату");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}





