package keyboardcorrector;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author MohammmadJafar MashhadiEbrahim
 */
public class KeyboardCorrectorEngine {

    private boolean standardLayout;
    private static HashMap<Character, Character> engTper = new HashMap<>();
    private static HashMap<Character, Character> perTeng = new HashMap<>();

    static {
        engTper.put('q', 'ض');
        engTper.put('w', 'ص');
        engTper.put('e', 'ث');
        engTper.put('r', 'ق');
        engTper.put('t', 'ف');
        engTper.put('y', 'غ');
        engTper.put('u', 'ع');
        engTper.put('i', 'ه');
        engTper.put('o', 'خ');
        engTper.put('p', 'ح');
        engTper.put('[', 'ج');
        engTper.put(']', 'چ');
        engTper.put('a', 'ش');
        engTper.put('s', 'س');
        engTper.put('d', 'ی');
        engTper.put('f', 'ب');
        engTper.put('g', 'ل');
        engTper.put('h', 'ا');
        engTper.put('j', 'ت');
        engTper.put('k', 'ن');
        engTper.put('l', 'م');
        engTper.put(';', 'ک');
        engTper.put('\'', 'گ');
        engTper.put('z', 'ظ');
        engTper.put('x', 'ط');
        engTper.put('c', 'ز');
        engTper.put('v', 'ر');
        engTper.put('b', 'ذ');
        engTper.put('n', 'د');
        engTper.put('m', 'پ');
        engTper.put(',', 'و');
        
        engTper.put('Q', 'ْ');
        engTper.put('W', 'ٌ');
        engTper.put('E', 'ٍ');
        engTper.put('R', 'ً');
        engTper.put('T', 'ُ');
        engTper.put('Y', 'ِ');
        engTper.put('U', 'َ');
        engTper.put('I', 'ّ');
        engTper.put('O', ']');
        engTper.put('P', '[');
        engTper.put('{', '}');
        engTper.put('}', '{');
        engTper.put('A', 'ؤ');
        engTper.put('S', 'ئ');
        engTper.put('D', 'ي');
        engTper.put('F', 'إ');
        engTper.put('G', 'أ');
        engTper.put('H', 'آ');
        engTper.put('J', 'ة');
        engTper.put('K', '»');
        engTper.put('L', '«');
        engTper.put(':', ':');
        engTper.put('"', '؛');
        engTper.put('Z', 'ك');
        engTper.put('X', 'ٓ');
        engTper.put('C', 'ژ');
        engTper.put('V', 'ٰ');
        //engTper.put('B', '‌‌‌‌‌‌ ');
        engTper.put('N', 'ٔ');
        engTper.put('M', 'ء');
        engTper.put('<', '>');
        engTper.put('>', '<');
        engTper.put('?', '؟');
        
        engTper.put('1', '۱');
        engTper.put('2', '۲');
        engTper.put('3', '۳');
        engTper.put('4', '۴');
        engTper.put('5', '۵');
        engTper.put('6', '۶');
        engTper.put('7', '۷');
        engTper.put('8', '۸');
        engTper.put('9', '۹');
        engTper.put('0', '۰');
        
        engTper.put('!', '!');
        engTper.put('@', '٬');
        engTper.put('#', '٫');
        engTper.put('$', '﷼');
        engTper.put('%', '٪');
        engTper.put('^', '×');
        engTper.put('&', '،');
        engTper.put('*', '*');
        engTper.put('(', ')');
        engTper.put(')', '(');
        
        for (Entry<Character, Character> pair : engTper.entrySet()) {
            perTeng.put(pair.getValue(), pair.getKey());
        }
    }
    
    public boolean isStandardLayout() {
        return standardLayout;
    }

    public void setStandardLayout(boolean standardLayout) {
        this.standardLayout = standardLayout;
    }

    public String convert(String text) {
        StringBuilder sbld = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            sbld.append(convert(c));
        }

        return sbld.toString();
    }

    public char convert(char c) {
        if (engTper.containsKey(c)) {
            return engTper.get(c);
        } else if (perTeng.containsKey(c)) {
            return perTeng.get(c);
        } else {
            return c;
        }
    }
}
