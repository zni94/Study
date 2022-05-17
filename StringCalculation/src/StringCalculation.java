import java.util.ArrayList;
import java.util.List;

public class StringCalculation {

    public static void main(String[] args) {
        String str = "33+4*21/1";

        Operator operator = new Operator();

        List<String> returnList = operator.strSplit(str);
        System.out.println("RETURN LIST :: " + returnList);

        String result = operator.calculate(returnList);
        System.out.println("RESULT :: " + result);
    }

    public static class Operator {
        public List<String> strSplit(String str) {
            String[] inputArr = str.split("");
            List<String> strArr = new ArrayList<String>();

            String num = "";
            int i = 0;
            int exceptionCnt = 0;

            do {
                try {
                    if (Integer.parseInt(inputArr[i]) >= 0) {
                        num += inputArr[i];
                        exceptionCnt = 0; // 숫자가 정상적으로 들어갔다면 예외처리 횟수를 0으로 초기화
                    }
                } catch (Exception e) { // 문자열은 Integer로 변환 불가능하므로 예외로 처리
                    strArr.add(num);
                    num = ""; // 이전까지의 숫자배열을 List에 추가 후 초기화
                    strArr.add(inputArr[i]);
                    exceptionCnt++; // 예외가 발생 할 때 마다 예외처리 횟수를 +1
                }

                // operator가 2번 이상 연속하는지 확인
                if (exceptionCnt == 2) {
                    strArr = new ArrayList<String>();
                    break;
                }

                i++;
            } while (i < inputArr.length);

            if (i == inputArr.length) {
                try {
                    if (Integer.parseInt(num) >= 0) {
                        strArr.add(num);
                    }
                } catch (Exception e) {
                    strArr = new ArrayList<String>();
                }
            }

            return strArr;
        }

        public String calculate(List<String> strArr) {
            String result = "";
            int num = 0;

            int i = 1;
            do {
                try {
                    int num1 = Integer.parseInt(strArr.get(i - 1)), num2 = Integer.parseInt(strArr.get(i + 1));
                    String operator = strArr.get(i);

                    switch (operator) {
                        case "+":
                            num = Math.addExact(num1, num2);
                            break;
                        case "-":
                            num = Math.subtractExact(num1, num2);
                            break;
                        case "*":
                            num = Math.multiplyExact(num1, num2);
                            break;
                        case "/":
                            num = Math.floorDiv(num1, num2);
                            break;
                    }

                    result = String.valueOf(num);
                } catch (Exception e) {
                    if (e instanceof ArithmeticException)
                        result = "0으로 나눌 수 없습니다.";
                    else
                        result = "ERROR";

                    break;
                }

                strArr.set(i - 1, result);
                strArr.remove(i);
                strArr.remove(i);
            } while (i < strArr.size());

            return result;
        }
    }
}