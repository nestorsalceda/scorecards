package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFightWithBoxersRepository implements
    FightWithBoxersRepository {

  private FightRepository fightRepository;
  private BoxerRepository boxerRepository;

  public InMemoryFightWithBoxersRepository(@Autowired FightRepository fightRepository, @Autowired BoxerRepository boxerRepository) {
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
  }

  @Override
  public Optional<FightWithBoxers> get(FightId id) {
    Optional<Fight> optionalFight = fightRepository.get(id);
    if (!optionalFight.isPresent()) {
      return Optional.empty();
    }
    Fight fight = optionalFight.get();
    Boxer firstBoxer = boxerRepository.get(fight.firstBoxer()).get();
    Boxer secondBoxer = boxerRepository.get(fight.secondBoxer()).get();

    FightWithBoxers fightWithBoxers = new FightWithBoxers(fight.id(), firstBoxer.name(),
        secondBoxer.name(), fight.event().place(), fight.event().happenAt(),
        fight.numberOfRounds());
    return Optional.ofNullable(fightWithBoxers);
  }
}
