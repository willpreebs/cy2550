import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class PasswordGenerator {


    private final static String fileName = "words.txt";
    private static File file;
    private static int length;


    public static void main(String[] args) throws IOException, URISyntaxException {

        ArrayList<String> arr = new ArrayList<String> (Arrays.asList(args));

        //default values
        int words = 4;
        int caps = 0;
        int numbers = 0;
        int symbols = 0;

        File root = new File
                (Thread.currentThread().getContextClassLoader().getResource("").toURI());


        file = new File(root, fileName);

        //System.out.println(file.getAbsolutePath());
/*
        Scanner scan = null;

        scan = new Scanner(file);

        while(scan.hasNext()) {
            length++;
            System.out.println(length);
            scan.next();
        }

        scan.close();
        */

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        }catch(FileNotFoundException e) {
            throw e;
        }
        length = 0;
        while(reader.readLine() !=  null) {
            length++;
        }
        reader.close();


        boolean printPass = true;


        if(arr.contains("-h") || arr.contains("--help")) {
            System.out.println("usage: xkcdpwgen [-h] [-w WORDS] [-c CAPS] [-n NUMBERS] [-s SYMBOLS]\n" +
                    "                \n" +
                    "Generate a secure, memorable password using the XKCD method\n" +
                    "                \n" +
                    "optional arguments:\n" +
                    "    -h, --help            show this help message and exit\n" +
                    "    -w WORDS, --words WORDS\n" +
                    "                          include WORDS words in the password (default=4)\n" +
                    "    -c CAPS, --caps CAPS  capitalize the first letter of CAPS random words\n" +
                    "                          (default=0)\n" +
                    "    -n NUMBERS, --numbers NUMBERS\n" +
                    "                          insert NUMBERS random numbers in the password\n" +
                    "                          (default=0)\n" +
                    "    -s SYMBOLS, --symbols SYMBOLS\n" +
                    "                          insert SYMBOLS random symbols in the password\n" +
                    "                          (default=0)");

            printPass = false;
        }


        //word argument handling
        if(arr.contains("-w") || arr.contains("--words")) {
            int index = arr.indexOf("-w");
            if(arr.indexOf("--words") > index) {
                index = arr.indexOf("--words");
            }
            words = Integer.parseInt(arr.get(index + 1));
            printPass = true;
        }

        //caps argument handling
        if(arr.contains("-c") || arr.contains("--caps")) {
            int index = arr.indexOf("-c");
            if(arr.indexOf("--caps") > index) {
                index = arr.indexOf("--caps");
            }
            caps = Integer.parseInt(arr.get(index + 1));
            printPass = true;
        }

        //numbers argument handling
        if(arr.contains("-n") || arr.contains("--numbers")) {
            int index = arr.indexOf("-n");
            if(arr.indexOf("--numbers") > index) {
                index = arr.indexOf("--numbers");
            }
            numbers = Integer.parseInt(arr.get(index + 1));
            printPass = true;
        }

        //symbols argument handling
        if(arr.contains("-s") || arr.contains("--symbols")) {
            int index = arr.indexOf("-s");
            if(arr.indexOf("--symbols") > index) {
                index = arr.indexOf("--words");
            }
            symbols = Integer.parseInt(arr.get(index + 1));
            printPass = true;
        }

        if(printPass) {
            System.out.println(generatePassword(words, caps, numbers, symbols));
        }

    }

    //currently only works with -w and --words flags
    public static String generatePassword(int words, int caps, int numbers, int symbols) {

        ArrayList<String>base = new ArrayList<>(words);

        Random random = new Random();


        for(int i = 0; i < words; i++) {
            Scanner scan = null;
            try {
                scan = new Scanner(file);
            } catch (IOException e) {

            }
            int rand = random.nextInt(length);
            String toAdd = "";
            for(int j = 0; j < rand; j++) {
                toAdd = scan.next();
            }
            base.add(toAdd);
        }
        String toReturn = "";



        while(notAllUppercase(base) && caps > 0) {

            int rand = random.nextInt(base.size());
            char toCaps = base.get(rand).charAt(0);
            if(Character.isLowerCase(toCaps)) {
                toCaps = Character.toUpperCase(toCaps);
                base.set(rand, toCaps + base.get(rand).substring(1));
                caps--;
            }
        }

        addNumbers(base, numbers);

        addSymbols(base, symbols);





        //need to add more loops to handle each argument(caps, numbers, symbols)

        for (String s: base) {
            toReturn+=s;
        }
        return toReturn;
    }

    //returns true if there is a word that doesn't have an upper-cased first letter
    public static boolean notAllUppercase(ArrayList<String> base) {
        for(String word: base) {
            if(Character.isLowerCase(word.charAt(0))) {
                return true;
            }
        }
        return false;
    }

    public static void addNumbers(ArrayList<String> base, int num) {

        Random rand = new Random();

        for (int i = 0; i < num; i++) {
            int randIndex = rand.nextInt(base.size());
            base.add(randIndex, Integer.toString(rand.nextInt(10)));
        }
    }

    public static void addSymbols(ArrayList<String> base, int num) {

        // ~!@#$%^&*.:;
        ArrayList<String> symbols = new ArrayList<>(
                Arrays.asList("~", "!", "@", "#", "$", "%", "^", "&", "*", ".", ":", ";"));

        Random rand = new Random();

        for (int i = 0; i < num; i++) {
            int randIndex = rand.nextInt(base.size());

            String symbol = symbols.get(rand.nextInt(symbols.size()));

            base.add(randIndex, symbol);
        }
    }

}

