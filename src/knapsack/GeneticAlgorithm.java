package knapsack;
import java.lang.String;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final int NUMBER_OF_SELECTED_INDIVIDUALS = 2;    // must be an even number

    private int noGenerations = 100;
    private double crossoverProbability = 0.1;
    private double mutationProbability = 0.1;
    private int initialPopulationSize = 20;
    private int chromosomeLength = 14;

    public void perform(KnapSack ks){
        // Initialize first generation
        List<String> population = createInitialGeneration();

        String bestSolution;
        double bestWeight;
        double bestFitness;
        // Run the GA until maxGenerations is reached
        for(int k = 0; (k < noGenerations) ; k++){
            bestSolution = getBestSolution(population, ks);
            bestWeight = calcWeight(bestSolution, ks);
            bestFitness = calcFitness(bestSolution, ks);
            System.out.print("Generation: " + (k+1));
            System.out.print(" " + "Best solution:" + bestSolution);
            System.out.print(", " + "Fitness = " + bestFitness);
            System.out.println(", " + "Utilization = " + bestWeight / ks.getKnapsackSize() * 100 + " %");
            List<String> nextGeneration = newGeneration(population, ks);
            population.addAll(nextGeneration);
        }
    }
    
    // This method generates all solution candidates as first generation
    // Takes as parameter the number of candidates in this first gen.
    private List<String> createInitialGeneration(){
        List<String> result = new ArrayList<>();
        // We initialize a new Random object
        Random rand = new Random(System.currentTimeMillis());
        
        String cand;
        
        // For each candidate solution
        for(int i = 0; i < this.initialPopulationSize; i++){
            cand = "";
            
            // Randomly assign 1 or 0 to the item to take in the knapsack
            // 1 => the respective item is taken
            // 0 => we leave the respective item
            for(int j = 0; j < this.chromosomeLength; j++){
                int letter = rand.nextInt(2);
                cand += letter;
            }
            
            // Add the randomly formed candidate solution to the generation
            result.add(cand);
        }
        return result;
    }
    
    // This method takes the 1st and 2nd best candidate solutions
    // and creates a new generation through crossovering them
    // and mutating the new generation
    private List<String> newGeneration(List<String> currentPopulation, KnapSack ks){
        // perform Selection
        List<String> selectedIndividuals = performSelection(currentPopulation, ks);
        // perform Reproduction
        List<String> reproducedIndividuals = performReproduction(selectedIndividuals);
        // perform Mutation
        List<String> newGeneration = performMutation(reproducedIndividuals);
        // return final population set
        return newGeneration;
    }

    private List<String> performSelection(List<String> currentPopulation, KnapSack ks){
        List<String> result = new ArrayList<>();
        List<String> populationToChooseFrom = new ArrayList<>();
        populationToChooseFrom.addAll(currentPopulation);

        for(int i = 0; i < GeneticAlgorithm.NUMBER_OF_SELECTED_INDIVIDUALS; i++){
            // get currently best candidate solution and add it to the result set
            String currentBestSolution = getBestSolution(populationToChooseFrom, ks);
            result.add(currentBestSolution);

            // Remove it from the list of solutions so we can select the 2nd best one
            populationToChooseFrom.remove(currentBestSolution);
        }
        return result;
    }

    private List<String> performReproduction(List<String> currentPopulation){
        List<String> result = new ArrayList<>();
        for(int i = 0; i < currentPopulation.size() - 1; i+= 2) {
            String nonDominantIndividual = currentPopulation.get(i);
            String dominantIndividual = currentPopulation.get(i + 1);
            String crossedOverIndividual = "";  // This will hold the resulting crossed over offspring
            // Iterate over the genes of a chromosome
            for (int j = 0; j < this.chromosomeLength; j++) {
                // Checking randomly whether to add to the resulting offspring
                // a gene from candidate1 or from candidate2
                if (Math.random() >= this.crossoverProbability) {
                    crossedOverIndividual += nonDominantIndividual.charAt(j);
                }else {
                    crossedOverIndividual += dominantIndividual.charAt(j);
                }
            }
            result.add(crossedOverIndividual);
        }
        return result;
    }

    private List<String> performMutation(List<String> currentPopulation){
        List<String> result = new ArrayList<>();
        for(int i = 0; i < currentPopulation.size(); i++) {
            String mutationCandidate = currentPopulation.get(i);
            String mutatedIndividual = "";
            // Iterate over the genes of the candidate
            for (int j = 0; j < this.chromosomeLength; j++) {
                // Checking randomly whether to mutate the current
                // gene of the iteration
                if (Math.random() <= this.mutationProbability) {
                    mutatedIndividual += reverseChromosome(mutationCandidate.charAt(j));
                }else{
                    mutatedIndividual += mutationCandidate.charAt(j);
                }
            }
            result.add(mutatedIndividual);
        }
        return result;
    }

    private char reverseChromosome(char c){
        if(c == 49){
            return '0';
        }else{
            return '1';
        }
    }

    // This method returns the best solution out of the current generation
    private String getBestSolution(List<String> candidateSolutions, KnapSack ks){
        double bestFit = -10000;
        double bestWeight = -1000;
        String bestSol = null;

        // Iterate over all individuals
        for (String currentIndividual : candidateSolutions) {
            double newFit = calcFitness(currentIndividual, ks);
            double newWeight = calcWeight(currentIndividual, ks);
            // If a better fit found
            // update bestSol variable
            // if two solutions are equal, take the one with lower weight
            if((newFit == bestFit && newWeight > bestWeight) || newFit > bestFit){
                bestSol = currentIndividual;
                bestFit = newFit;
                bestWeight = newWeight;

            }
        }
        // Return the best candidate solution
        return bestSol;
    }

    // This method calculates the fitness of a given candidate solution
    private double calcFitness(String solution, KnapSack ks){
        double fit = 0;
        double weight = 0;

        // We iterate over all the candidate solution genes.
        for(int i = 0; i < solution.length(); i++){
            // If gene is 1, respective item is taken into the knapsack
            if(solution.charAt(i) == 49){
                // Increment the weight and the value of taking this
                // item in the sack
                weight += ks.getWeight()[i];
                fit += ks.getValue()[i];
            }
        }

        // If we did not surpass the knapsack size
        // return the fitness for this candidate solution
        // OTHERWISE return -1
        if(weight <= ks.getKnapsackSize()) {
            return fit;
        }else {
            return ks.getKnapsackSize() - weight;
        }
    }

    // This method calculates the fitness of a given candidate solution
    private double calcWeight(String solution, KnapSack ks){
        double weight = 0;

        // We iterate over all the candidate solution genes.
        for(int i = 0; i < solution.length(); i++){
            // If gene is 1, respective item is taken into the knapsack
            if(solution.charAt(i) == 49){
                weight += ks.getWeight()[i];
            }
        }

        return weight;
    }
}
