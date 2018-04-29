package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FalloutHack {
    private static final int[] wordLengthByDifficulty = new int[]{4, 7, 10, 12, 15};
    private static final int[] wordCountByDifficulty = new int[]{4, 8, 11, 12, 15};

    private static int guessesLeft = 4;
    private static boolean gameWon = false;

    public static void main(String[] args) throws IOException {
        int difficulty = chooseDifficulty();
        List<String> options = getWords(difficulty);
        String password = getPassword(options);

        displayOptions(options);

        while (guessesLeft != 0 && !gameWon) {
            Guess(password);
        }

        if (gameWon) {
            System.out.println("You win!");
        }
        else System.out.println("You lost, unfortunately...");
    }

    private static int chooseDifficulty() {
        System.out.println("Choose difficulty (1-5): ");
        Scanner scanner = new Scanner(System.in);
        int difficulty = scanner.nextInt();
        if (difficulty < 0 || difficulty > 5) {
            System.out.println("Incorrect difficulty, please choose between 1 and 5.");
            chooseDifficulty();
        }
        return difficulty;
    }

    private static void Guess(String password) {
        System.out.println("Guess (" + guessesLeft + " left): ");
        Scanner sc = new Scanner(System.in);
        String guess = sc.next().toLowerCase();
        int passLength = password.length();
        int matches = checkMatches(password, guess);
        System.out.println(matches + "/" + passLength + " correct");
        if (matches == passLength) {
            gameWon = true;
        }
        else {
            guessesLeft--;
        }
    }

    private static int checkMatches(String password, String guess) {
        int matches = 0;
        for (int i=0; i<password.length(); i++) {
            if (password.charAt(i) == guess.charAt(i)) {
                matches++;
            }
        }
        return matches;
    }

    private static void displayOptions(List<String> options) {
        for (String option : options) {
            System.out.println(option);
        }
    }
    private static String getPassword(List<String> words) {
        String password = "";
        Random rn = new Random();
        password = words.get(rn.nextInt(words.size()));
        return password;
    }
    private static List<String> getWords(int diff) throws IOException {
        List<String> strings = readFile();
        List<String> sameLengthWords = getSameLengthWords(diff, strings);
        Set<String> randomWords = getRandomWords(diff, sameLengthWords);
        return new ArrayList<String>(randomWords);
    }

    private static Set<String> getRandomWords(int diff, List<String> words) throws IOException {
        Set<String> randomWords = new HashSet<>();
        Random random = new Random();
        int setSize = wordCountByDifficulty[diff - 1];
        while (randomWords.size() != setSize) {
            randomWords.add(words.get(random.nextInt(words.size())));
        }
        return randomWords;
    }

    private static List<String> readFile() throws IOException {
        File f = new File("src/com/resources/enable1.txt");
        BufferedReader b = new BufferedReader(new FileReader(f));
        List<String> strings = new ArrayList<String>();

        try {
            String line = b.readLine();

            while (line != null) {
                line = b.readLine();
                strings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            b.close();
        }
        return strings;
    }

    private static List<String> getSameLengthWords(int diff, List<String> strings) {
        List<String> words = new ArrayList<>();

        for (String word : strings) {
            if (word != null && word.length() == wordLengthByDifficulty[diff - 1]) {
                words.add(word);
            }
        }
        return words;
    }
}
