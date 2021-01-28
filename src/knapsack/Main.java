package knapsack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    private static final String PARAMETERS_FILE_PATH = "./src/knapsack/init.txt";
    
    public static void main(String[] args){
        KnapSack ks = new KnapSack(Main.PARAMETERS_FILE_PATH);
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.perform(ks);
    }
}
