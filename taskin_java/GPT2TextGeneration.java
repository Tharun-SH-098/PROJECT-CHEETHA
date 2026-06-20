import java.util.Random;
import java.util.Scanner;

public class GPT2TextGeneration {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Prompt: ");
        String prompt = sc.nextLine();

        String[] words = {
            "Artificial", "Intelligence", "Machine", "Learning",
            "Deep", "Neural", "Network", "Data", "Science",
            "Automation", "Technology", "Innovation", "Future",
            "Analysis", "Prediction", "Model", "Training"
        };

        Random random = new Random();

        StringBuilder generatedText = new StringBuilder(prompt);

        for(int i = 0; i < 30; i++) {
            generatedText.append(" ")
                         .append(words[random.nextInt(words.length)]);
        }

        System.out.println("\nGenerated Text:");
        System.out.println(generatedText);

        sc.close();
    }
}