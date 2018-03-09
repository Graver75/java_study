package package_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class WordStatIndex {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Input and output file names should be provided by cli args");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];
        LinkedHashMap<String, Word> wordsList = new LinkedHashMap<>();

        try (Scanner text = new Scanner(new File(inputFileName), "UTF-8")) {
            String word;
            for (int i = 0; text.hasNext(); i++) {
                word = text.next().toLowerCase();
                word = Word.validateWord(word);
                if (wordsList.containsKey(word)) {
                    Word tempWord = wordsList.get(word);
                    tempWord.numbers.add(i);
                    tempWord.counter++;
                    wordsList.put(word, tempWord);
                } else {
                    wordsList.put(word, new Word(i));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Input file name provided by cli args is invalid");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new File(outputFileName))) {
            for (String key : wordsList.keySet()) {
                Word tempWord = wordsList.get(key);
                StringBuilder numbersRawString = new StringBuilder();
                for (Integer number : tempWord.numbers) {
                    numbersRawString.append(" ").append(number);
                }
                writer.println(key + " " + tempWord.counter + numbersRawString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Output file name provided by cli args is invalid");
        }
    }

}

class Word {

    int counter = 1;
    ArrayList<Integer> numbers = new ArrayList<>();

    Word(Integer firstNumber) {
        this.numbers.add(firstNumber);
    }

    public static String validateWord(String word) {
        char[] symbols = word.toCharArray();
        int len = word.length();
        if (Character.isDigit(symbols[0])) {
            return null;
        }
        if (!Character.isLetter(symbols[len - 1])) {
            len -= 1;
        }
        for (int i = 0; i < len; i++) {
            if (!(Character.isLetter(symbols[i]) || symbols[i] == '-' || symbols[i] == '\'')) {
                return null;
            }
        }
        StringBuilder wordBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            wordBuilder.append(symbols[i]);
        }
        return wordBuilder.toString();
    }

}