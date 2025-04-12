package fctreddit.clients.contents.Parser;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {

    public static Map<String, String> parseOptionalArgs(String[] args, int start) {
        Map<String, String> map = new HashMap<>();
        for (int i = start; i < args.length - 1; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2);
                String value = args[i + 1];
                map.put(key.toLowerCase(), value);
                i++;
            }
        }
        return map;
    }

}
