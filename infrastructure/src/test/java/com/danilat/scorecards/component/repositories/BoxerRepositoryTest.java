package com.danilat.scorecards.component.repositories;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.repositories.InMemoryBoxerRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class BoxerRepositoryTest {

  private BoxerRepository boxerRepository;
  private BoxerId boxerId = new BoxerId("some irrelevant id");

  @Before
  public void setup() {
    boxerRepository = new InMemoryBoxerRepository();
  }

  @Test
  public void saveABoxer() {
    Boxer aBoxer = BoxerMother.aBoxerWithId(boxerId);

    boxerRepository.save(aBoxer);

    Optional retrieved = boxerRepository.get(boxerId);
    assertTrue(retrieved.isPresent());
  }

  @Test
  public void nextIdGeneratesUniques() {
    BoxerId aBoxerId = boxerRepository.nextId();
    BoxerId anotherBoxerId = boxerRepository.nextId();

    assertNotEquals(aBoxerId, anotherBoxerId);
  }
}
