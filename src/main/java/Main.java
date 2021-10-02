import task1.utils.MailUtils;

import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String string = MailUtils.getTextFromLatestMessage();
        System.out.println(Pattern.compile("https://my\\.kaspersky\\.com/.*Download").matcher(string).find());
        System.out.println(string);
    }
}
