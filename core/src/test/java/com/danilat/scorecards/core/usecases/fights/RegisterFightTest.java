package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundException;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.events.FightCreated;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegisterFightTest {

  private static final BoxerId ALI = new BoxerId("Ali");
  private static final BoxerId FOREMAN = new BoxerId("Foreman");
  private static final BoxerId NON_EXISTING_BOXER = new BoxerId("NON_EXISTING_BOXER");
  private RegisterFight registerFight;
  private LocalDate aDate;
  @Spy
  private FightRepository fightRepository;
  @Mock
  private BoxerRepository boxerRepository;
  @Spy
  private EventBus eventBus;
  @Mock
  private Clock clock;
  @Mock
  private UniqueIdGenerator uniqueIdGenerator;

  private static final String AN_ID = "irrelevant id";
  private String aPlace = "Kinsasa, Zaire";
  private Integer numberOfRounds = 12;
  private LocalDate anHappenedAt;

  @Before
  public void setup() {
    aDate = LocalDate.now();
    anHappenedAt = LocalDate.now();
    when(uniqueIdGenerator.next()).thenReturn(AN_ID);
    when(boxerRepository.get(ALI)).thenReturn(Optional.of(BoxerMother.aBoxerWithId(ALI)));
    when(boxerRepository.get(FOREMAN)).thenReturn(Optional.of(BoxerMother.aBoxerWithId(FOREMAN)));
    when(clock.now()).thenReturn(anHappenedAt);
    when(uniqueIdGenerator.next()).thenReturn(AN_ID);
    registerFight = new RegisterFight(fightRepository, boxerRepository, eventBus, clock,
        uniqueIdGenerator);
  }

  @Test
  public void givenTwoBoxersAndADateThenIsRegistered() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate, aPlace,
        numberOfRounds);

    Fight fight = registerFight.execute(parameters);

    assertEquals(ALI, fight.firstBoxer());
    assertEquals(FOREMAN, fight.secondBoxer());
    assertEquals(AN_ID, fight.id().value());
    assertEquals(aDate, fight.event().happenAt());
    assertEquals(aPlace, fight.event().place());
    assertEquals(numberOfRounds, fight.numberOfRounds());
  }

  @Test
  public void givenTwoBoxersAndADateThenIsPersisted() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate, aPlace,
        numberOfRounds);

    Fight fight = registerFight.execute(parameters);

    verify(fightRepository, times(1)).save(fight);
  }

  @Captor
  ArgumentCaptor<FightCreated> fightCreatedArgumentCaptorCaptor;
  @Test
  public void givenTwoBoxersAndADateThenAnEventIsPublished() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate, aPlace,
        numberOfRounds);

    Fight fight = registerFight.execute(parameters);

    verify(eventBus, times(1)).publish(fightCreatedArgumentCaptorCaptor.capture());
    FightCreated fightCreated = fightCreatedArgumentCaptorCaptor.getValue();
    assertEquals(fight, fightCreated.fight());
    assertEquals(AN_ID, fightCreated.eventId().value());
    assertEquals(anHappenedAt, fightCreated.happenedAt());
  }

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void givenTwoBoxerButNotDateThenIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("happenAt is mandatory");

    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, null, aPlace,
        numberOfRounds);

    registerFight.execute(parameters);
  }

  @Test
  public void givenFirstBoxerIsNotPresentThenIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("firstBoxer is mandatory");

    RegisterFightParameters parameters = new RegisterFightParameters(null, FOREMAN, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(parameters);
  }

  @Test
  public void givenFirstBoxerIsNotExistingThenIsInvalid() {
    expectedEx.expect(BoxerNotFoundException.class);
    expectedEx.expectMessage(NON_EXISTING_BOXER.toString() + " not found");

    RegisterFightParameters parameters = new RegisterFightParameters(NON_EXISTING_BOXER, FOREMAN,
        aDate, aPlace,
        numberOfRounds);

    registerFight.execute(parameters);
  }

  @Test
  public void givenSecondBoxerIsNotPresentThenIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("secondBoxer is mandatory");

    RegisterFightParameters parameters = new RegisterFightParameters(ALI, null, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(parameters);
  }

  @Test
  public void givenSecondBoxerIsNotExistingThenIsInvalid() {
    expectedEx.expect(BoxerNotFoundException.class);
    expectedEx.expectMessage(NON_EXISTING_BOXER.toString() + " not found");

    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        numberOfRounds);

    registerFight.execute(parameters);
  }

  @Test
  public void givenNumberOfRoundsIsNotPresentThenIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("numberOfRounds is mandatory");

    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        null);

    registerFight.execute(parameters);
  }

  @Test
  public void givenLessThanThreeNumberOfRoundsThenIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("numberOfRounds is less than three");

    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        2);

    registerFight.execute(parameters);
  }

  @Test
  public void givenMoreThanTwelveNumberOfRoundsThenIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("numberOfRounds is more than twelve");

    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        13);

    registerFight.execute(parameters);
  }
}
