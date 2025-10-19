package selection;

import chromosome.Chromosome;
import core.*;
import java.util.*;
import java.util.stream.Collectors;

public class Tournament implements SelectionMethod {
    private final Random random = new Random();
    private final int tournamentSize;

    public Tournament(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public Chromosome select(Population population) {
        List<Chromosome> participants = random
                .ints(tournamentSize, 0, population.getChromosomeList().size())
                .mapToObj(i -> population.getChromosomeList().get(i))
                .toList();

        return participants.stream()
                .max(Comparator.comparingDouble(Chromosome::getFitness))
                .orElse(population.getChromosomeList().get(0));
    }
}
