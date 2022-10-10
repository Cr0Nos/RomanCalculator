import java.util.*;
import java.util.stream.Collectors;

enum RomanNumeral {
    I(1), IV(4), V(5), IX(9), X(10),
    XL(40), L(50), XC(90), C(100),
    CD(400), D(500), CM(900), M(1000);

    final private int value;

    RomanNumeral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<RomanNumeral> getReverseSortedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                .collect(Collectors.toList());
    }
}
public class Main {
    static Scanner console = new Scanner(System.in);
    static boolean normalMode = false;

    public static void main (String[] args) {
        System.out.println("Введите выражение арабскими числами (5+3) или римскими (II + IV) " +
                "и нажмите Enter, если ввести оба типа чисел, " +
                "то выражение посчитано не будет. Для выхода введите 'stop'.");
        while(true) {
            System.out.print("Выражение: ");
            String uInput = console.nextLine();
            if(uInput.contains("stop"))
            {
                System.out.println("Выход из приложения...");
                System.exit(0);
            }
            if(uInput.contains("normal"))
            {
                normalMode = true;
                System.out.println("Ограничения отключены");
                continue;
            }
            else System.out.println(Calc(uInput));
        }
    }

    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) result = -1;

        return result;
    }

    public static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " вне допустимого диапазона (0,4000]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static String Calc(String calcInput) {
        char _oper = '+';
        int result;
        char[] uChar = new char[10];
        for (int i = 0; i < calcInput.length(); i++) {
            if (i >= uChar.length) break;
            uChar[i] = calcInput.charAt(i);
            if (uChar[i] == '+') _oper = '+';
            else if (uChar[i] == '-') _oper = '-';
            else if (uChar[i] == '*') _oper = '*';
            else if (uChar[i] == '/') _oper = '/';
        }
        String uCharString = String.valueOf(uChar).toUpperCase();
        String[] _nums = uCharString.split("[+-/*]");
        if(_nums.length > 2) throw new RuntimeException("Использование более одного оператора не допускается");
        if(_nums.length < 2) throw new RuntimeException("Не найден второй операнд");
        String _tmpNum1 = _nums[0].trim();
        String _tmpNum2 = _nums[1].trim();
        int _romanNum1 = romanToArabic(_tmpNum1);
        int _romanNum2 = romanToArabic(_tmpNum2);
        if((_romanNum1 > 10 || _romanNum2 > 10) && !normalMode) throw new RuntimeException("Доступны операции с числами от 1(I) до 10(X)");
        if(!(_romanNum1 < 0 && _romanNum2 < 0))
        {
            if(_romanNum1 < 0 || _romanNum2 < 0) throw new RuntimeException("Разные системы счисления");
            result = Calculate(_romanNum1, _romanNum2, _oper);
            if(result < 1) throw new RuntimeException("Недопустимый результат в выбранной системе счисления");
            return "Результат: " + arabicToRoman(result);
        }
        int _arabicNum1 = Integer.parseInt(_tmpNum1);
        int _arabicNum2 = Integer.parseInt(_tmpNum2);
        if((_arabicNum1 > 10 || _arabicNum2 > 10) && !normalMode) throw new RuntimeException("Доступны операции с числами от 1 до 10");
        result = Calculate(_arabicNum1, _arabicNum2, _oper);
        return "Результат: " + result;
    }

    private static int Calculate(int _num1, int _num2, char _oper)
    {
        int result;
        switch (_oper) {
            case '+':
                result = _num1 + _num2;
                break;
            case '-':
                result = _num1 - _num2;
                break;
            case '*':
                result = _num1 * _num2;
                break;
            case '/':
                try {
                    result = _num1 / _num2;
                } catch (Exception e) {
                    throw new RuntimeException("Нельзя делить на ноль ("+ e +")");
                }
                break;
            default:
                throw new RuntimeException("Неверная операция");
        }
        return result;
    }
}


