package task3;

import java.util.*;

public class MarkovChainTextGenerator {

    private Map<String, List<String>> markovChain = new HashMap<>();
    private Random random = new Random();

    // Train the Markov Chain
    public void train(String text) {
        String[] words = text.split("\\s+");

        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i];
            String nextWord = words[i + 1];

            markovChain.putIfAbsent(currentWord, new ArrayList<>());
            markovChain.get(currentWord).add(nextWord);
        }
    }

    // Generate text
    public String generateText(int length) {
        if (markovChain.isEmpty()) {
            return "Model is not trained!";
        }

        List<String> keys = new ArrayList<>(markovChain.keySet());
        String currentWord = keys.get(random.nextInt(keys.size()));

        StringBuilder generatedText = new StringBuilder(currentWord);

        for (int i = 1; i < length; i++) {
            List<String> nextWords = markovChain.get(currentWord);

            if (nextWords == null || nextWords.isEmpty()) {
                currentWord = keys.get(random.nextInt(keys.size()));
            } else {
                currentWord = nextWords.get(random.nextInt(nextWords.size()));
            }

            generatedText.append(" ").append(currentWord);
        }

        return generatedText.toString();
    }

    public static void main(String[] args) {

        String trainingText =
                "Artificial intelligence is transforming the world. " +
                "Artificial intelligence helps in automation. " +
                "Machine learning is a part of artificial intelligence. " +
                "Artificial intelligence is used in many applications.";

        MarkovChainTextGenerator generator = new MarkovChainTextGenerator();

        generator.train(trainingText);

        System.out.println("Generated Text:");
        System.out.println(generator.generateText(20));
    }
}