package knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class KnapSack {

    private double[] value;
    private double[] weight;
    private double knapsackSize;

    public KnapSack(String fileName){
        try {
            System.out.println(fileName);
            Scanner input = new Scanner(new File(fileName));
            int noItems = input.nextInt();
            double[] values = new double[noItems];
            double[] weights = new double[noItems];
            for(int i = 0; i < noItems; i++)
                values[i] = input.nextDouble();
            for(int i = 0; i < noItems; i++)
                weights[i] = input.nextDouble();
            this.value = values;
            this.weight = weights;
            this.knapsackSize = input.nextInt();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found!");
            System.exit(1);
        }
    }

    public double[] getValue() {
        return value;
    }

    public void setValue(double[] value) {
        this.value = value;
    }

    public double[] getWeight() {
        return weight;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    public double getKnapsackSize() {
        return knapsackSize;
    }

    public void setKnapsackSize(double knapsackSize) {
        this.knapsackSize = knapsackSize;
    }
}
