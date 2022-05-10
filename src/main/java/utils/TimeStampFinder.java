package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeStampFinder {
    private String input;
    static Pattern p;
    static Matcher m;
    public TimeStampFinder(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String finder(){
        m = p.compile("[0-9]{14}").matcher(this.input);
        if (!m.find())
            System.out.println("not found");

        String tmstp = m.group();
        return tmstp;
    }
}
