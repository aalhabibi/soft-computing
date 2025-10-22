package selection;

import chromosome.Chromosome;
import core.*;
import java.util.*;
import java.util.stream.Collectors;

public class Tournament<T> implements SelectionMethod<T> {
    private final Random random = new Random();
    private final int tournamentSize;

    public Tournament(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public Chromosome<T> select(Population population) {
        List<Chromosome<T>> participants = random
                .ints(tournamentSize, 0, population.getChromosomeList().size())
                .mapToObj(i -> (Chromosome<T>) population.getChromosomeList().get(i))
                .toList();

        return participants.stream()
                .max(Comparator.comparingDouble(Chromosome::getFitness))
                .orElse((Chromosome<T>) population.getChromosomeList().get(0));
    }
}
