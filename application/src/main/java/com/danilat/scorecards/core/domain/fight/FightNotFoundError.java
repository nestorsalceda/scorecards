package com.danilat.scorecards.core.domain.fight;


import com.danilat.scorecards.shared.domain.errors.Error;

public class FightNotFoundError extends Error {

  public FightNotFoundError(FightId fightId) {
    super("Fight: " + fightId + " not found");
  }
}
