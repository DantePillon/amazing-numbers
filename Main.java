package numbers;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Amazing Numbers!\n");
        printInstructions();

        while (true) {
            System.out.print("Enter a request: ");
            String input = scan.nextLine();
            System.out.println();
            Scanner scanInput = new Scanner(input);
            long firstArg;
            int secondArg = -1;     // Needs to be initialized to include in conditions.
            String thirdArg = "";   // Same here.
            List<Property> requestedProperties = new ArrayList<>();
            List<Property> requestedSans = new ArrayList<>();
            List<String> inValidProperties = new ArrayList<>(); // List to display error message for invalid properties
            final List<List<String>> mutualExcPairs = List.of(
                    List.of(" EVEN", " ODD"), List.of("-EVEN", "-ODD"),
                    List.of(" SPY", " DUCK"), List.of("-SPY", "-DUCK"),
                    List.of(" SUNNY", " SQUARE"),  List.of("-SUNNY", "-SQUARE"),
                    List.of(" HAPPY", " SAD"), List.of("-HAPPY", "-SAD"));
            boolean isList = true;
            boolean isSearch = true;

            try {
                firstArg = scanInput.nextLong();
            } catch (Exception ignored) {
                printInstructions();
                continue;
            }
            try {
                secondArg = scanInput.nextInt();
            } catch (Exception ignored) {
                isList = false;
            }
            try {
                thirdArg = scanInput.nextLine().toUpperCase();
            } catch (NoSuchElementException e) {
                isSearch = false;
            }
            Scanner scanThird = new Scanner(thirdArg);
            while (scanThird.hasNext()) {
                String curStr = scanThird.next();
                try {
                    if (curStr.charAt(0) == '-') {
                        requestedSans.add(Property.valueOf(curStr.substring(1)));
                    } else {
                        requestedProperties.add(Property.valueOf(curStr));
                    }
                } catch (IllegalArgumentException e) {
                    inValidProperties.add(curStr);
                }
            }

            if (inValidProperties.size() == 1) {
                System.out.println("The property " + inValidProperties + " is wrong.\n" +
                        "Available properties:\n" +
                        "[EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]\n");
                continue;
            }
            if (inValidProperties.size() > 1) {
                System.out.println("The properties " + inValidProperties + " are wrong.\n" +
                        "Available properties:\n" +
                        "[EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]\n");
                continue;
            }

            boolean badRequest = false;
            for (List<String> l : mutualExcPairs) {
                if (thirdArg.contains(l.get(0)) && thirdArg.contains(l.get(1))) {
                    System.out.println("The request contains mutually exclusive properties: " + l + "\n" +
                            "There are no numbers with these properties.\n");
                    badRequest = true;
                }
            }
            for (Property p : requestedProperties) {
                if (requestedSans.contains(p)) {
                    System.out.println("The request contains mutually exclusive properties: [" + p + ", -" + p + "\n" +
                            "There are no numbers with these properties.\n");
                    badRequest = true;
                    break;
                }
            }

            // if (thirdArg.matches("(.* -\\w+ .* )")) {}
            // I attempted to create a regex to match both some "word" and the same "-word" in a string. I failed :(

            if (badRequest) {
                continue;
            }

            if (firstArg < 0) {
                System.out.println("The first parameter should be a natural number or zero.\n");
                continue;
            }
            if (firstArg == 0) {
                System.out.println("Goodbye!");
                break;
            }
            if (isList && secondArg < 1) {
                System.out.println("The second parameter should be a natural number.\n");
                continue;
            }

            if (!isList) {
                printProperties(firstArg);
            } else if (!isSearch) {
                for (int i = 0; i < secondArg; i++) {
                    printListProperties(firstArg + i);
                }
            } else {
                searchForProperties(firstArg, secondArg, requestedProperties, requestedSans);
            }
            System.out.println();
        }
    }

    static void printInstructions() {
        System.out.println("Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.\n");
    }

    static void printProperties(long num) {
        System.out.println("Properties of " + num + "\n" +
                "        buzz: " + isBuzz(num) + "\n" +
                "        duck: " + isDuck(num) + "\n" +
                " palindromic: " + isPalindromic(num) + "\n" +
                "      gapful: " + isGapful(num) + "\n" +
                "         spy: " + isSpy(num) + "\n" +
                "      square: " + isSquare(num) + "\n" +
                "       sunny: " + isSunny(num) + "\n" +
                "     jumping: " + isJumping(num) + "\n" +
                "       happy: " + isHappy(num) + "\n" +
                "         sad: " + !isHappy(num) + "\n" +
                "        even: " + (num % 2 == 0) + "\n" +
                "         odd: " + (num % 2 != 0) + "\n");
    }

    static void printListProperties(long num) {
        StringBuilder message = new StringBuilder();
        message.append(num).append(" is ");
        if (num % 2 == 0) {
            message.append("even, ");
        } else {
            message.append("odd, ");
        }
        if (isHappy(num)) {
            message.append("happy, ");
        } else {
            message.append("sad, ");
        }
        if (isBuzz(num)) message.append("buzz, ");
        if (isDuck(num)) message.append("duck, ");
        if (isPalindromic(num)) message.append("palindromic, ");
        if (isGapful(num)) message.append("gapful, ");
        if (isSpy(num)) message.append("spy, ");
        if (isSquare(num)) message.append("square, ");
        if (isSunny(num)) message.append("sunny, ");
        if (isJumping(num)) message.append("jumping, ");
        message.setLength(message.length() - 2);    // Omitting the ", " at the end.
        System.out.println(message);
    }
    static boolean isBuzz(long num) {
        String numStr = String.valueOf(num);
        return (num % 7 == 0 || numStr.charAt(numStr.length() - 1) == '7');
    }

    static boolean isDuck(long num) {
        return (String.valueOf(num).contains("0"));
    }

    @Deprecated
    static boolean oldIsPalindromic(long number) {
        String str = String.valueOf(number);
        StringBuilder secondHalf = new StringBuilder(str.substring((str.length() + 1) / 2)).reverse();
        return str.substring(0, str.length() / 2).equals(secondHalf.toString());
    }

    static boolean isPalindromic(long number) {
        String str = String.valueOf(number);
        for (int i = 0; i < str.length() / 2; i++) {
            if (str.charAt(i) != str.charAt(str.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    static boolean isGapful(long number) {
        if (-99 <= number && number <= 99) {
            return false;
        }
        String str = String.valueOf(number);
        long modulus = Integer.parseInt(str.charAt(0) + str.substring(str.length() - 1));
        return number % modulus == 0;
    }

    static boolean isSpy(long number) {
        char[] chars = String.valueOf(number).toCharArray();
        int sum = 0;
        for (char c : chars) {
            sum += c - '0';
        }
        int product = 1;
        for (char c : chars) {
            product *= c - '0';
        }
        return sum == product;
    }

    static boolean isSquare(long number) {
        return Math.sqrt(number) == (int) Math.sqrt(number);
    }

    static boolean isSunny(long number) {
        return isSquare(number + 1);
    }

    static boolean isJumping(long number) {
        char[] chars = String.valueOf(number).toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            int diff = chars[i] - chars[i + 1];
            if (diff != 1 && diff != -1) {
                return false;
            }
        }
        return true;
    }

    static boolean isHappy(long number) {
        List<Integer> numbersInCycle = new ArrayList<>();
        // Above: Keeps track of numbers we've encountered in the cycle, so we can determine if it is infinite.
        return isHappyRe(number, numbersInCycle);
    }

    static boolean isHappyRe(long prevSum, List<Integer> pastSums) {
        char[] chars = String.valueOf(prevSum).toCharArray();
        int squareSum = 0;
        for (char c : chars) {
            squareSum += (c - '0') * (c - '0');
        }
        if (squareSum == 1) return true;
        if (pastSums.contains(squareSum)) return false;
        pastSums.add(squareSum);
        return isHappyRe(squareSum, pastSums);
    }

    static void searchForProperties(long start, int amountRequested, List<Property> properties, List<Property> sans) {
        int count = 0;
        for (long i = 0; count < amountRequested; i++) {
            if (checkForProperties(start + i, properties) && (!checkForProperties(start + i, sans) || sans.size() == 0)) {
                printListProperties(start + i);
                count++;
            }
        }
    }
    static boolean checkForProperties(long num, List<Property> properties) {
        boolean qualify = true;
        for (Property p : properties) {
            switch (p) {
                case BUZZ:
                    qualify = qualify && isBuzz(num);   // equivalent to if(isBuzz(num) {qualify = false;}
                    break;
                case DUCK:
                    qualify = qualify && isDuck(num);
                    break;
                case PALINDROMIC:
                    qualify = qualify && isPalindromic(num);
                    break;
                case GAPFUL:
                    qualify = qualify && isGapful(num);
                    break;
                case SPY:
                    qualify = qualify && isSpy(num);
                    break;
                case EVEN:
                    qualify = qualify && num % 2 == 0;
                    break;
                case ODD:
                    qualify = qualify && num % 2 != 0;
                    break;
                case SQUARE:
                    qualify = qualify && isSquare(num);
                    break;
                case SUNNY:
                    qualify = qualify && isSunny(num);
                    break;
                case JUMPING:
                    qualify = qualify && isJumping(num);
                    break;
                case HAPPY:
                    qualify = qualify && isHappy(num);
                    break;
                case SAD:
                    qualify = qualify && !isHappy(num);
                    break;
                default:
                    throw new RuntimeException("Something went wrong with the properties list");
            }
        }
        return qualify;
    }

    @Deprecated
    static void searchBuzz(long start, int numberRequested) {
        int count = 0;
        for (int i = 0; count < numberRequested; i++) {
            if (isBuzz(start + i)) {
                printListProperties(start + i);
                count++;
            }
        }
    }

    @Deprecated
    static void searchDuck(long start, int numberRequested) {
        int count = 0;
        for (int i = 0; count < numberRequested; i++) {
            if (isDuck(start + i)) {
                printListProperties(start + i);
                count++;
            }
        }
    }

    @Deprecated
    static void searchPalindromic(long start, int numberRequested) {
        int count = 0;
        for (int i = 0; count < numberRequested; i++) {
            if (isPalindromic(start + i)) {
                printListProperties(start + i);
                count++;
            }
        }
    }

    @Deprecated
    static void searchGapful(long start, int numberRequested) {
        int count = 0;
        for (int i = 0; count < numberRequested; i++) {
            if (isGapful(start + i)) {
                printListProperties(start + i);
                count++;
            }
        }
    }

    @Deprecated
    static void searchSpy(long start, int numberRequested) {
        int count = 0;
        for (int i = 0; count < numberRequested; i++) {
            if (isSpy(start + i)) {
                printListProperties(start + i);
                count++;
            }
        }
    }

    @Deprecated
    static void searchEven(long start, int numberRequested) {
        int count = 0;
        for (int i = 0; count < numberRequested; i++) {
            if ((start + i) % 2 == 0) {
                printListProperties(start + i);
                count++;
            }
        }
    }

    @Deprecated
    static void searchOdd(long start, int numberRequested) {
        int count = 0;
        for (int i = 0; count < numberRequested; i++) {
            if ((start + i) % 2 != 0) {
                printListProperties(start + i);
                count++;
            }
        }
    }
}

enum Property {
    BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, EVEN, ODD, SQUARE, SUNNY, JUMPING, HAPPY, SAD
    // Message to future self. I wonder, is there a way so that when we add a new constant to our enum, to ensure
    // that it has to be implemented in the rest of the program, otherwise an error occurs; similarly to how
    // an error is caused if an abstract method is not overridden.
}
