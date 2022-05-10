package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToUnixTmstp {
    private String input;

    public StringToUnixTmstp(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Long convert() throws ParseException {
        SimpleDateFormat formatter
                = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = formatter.parse(this.input);
        return date.getTime();

    }
}
