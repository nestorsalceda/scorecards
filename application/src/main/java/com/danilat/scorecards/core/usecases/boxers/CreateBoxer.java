package com.danilat.scorecards.core.usecases.boxers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer.CreateBoxerParams;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.UseCase;

public class CreateBoxer implements UseCase<Boxer, CreateBoxerParams> {

  private final UniqueIdGenerator uniqueIdGenerator;
  private final BoxerRepository boxerRepository;
  private final EventBus eventBus;
  private final Clock clock;

  public CreateBoxer(UniqueIdGenerator uniqueIdGenerator,
      BoxerRepository boxerRepository, EventBus eventBus, Clock clock) {
    this.uniqueIdGenerator = uniqueIdGenerator;
    this.boxerRepository = boxerRepository;
    this.eventBus = eventBus;
    this.clock = clock;
  }

  @Override
  public void execute(PrimaryPort<Boxer> primaryPort, CreateBoxerParams params) {
    BoxerId boxerId = new BoxerId(uniqueIdGenerator.next());
    Boxer boxer = Boxer.create(boxerId, params.getName(), params.getAlias(), params.getBoxrecUrl(), clock.now());
    boxerRepository.save(boxer);
    eventBus.publish(boxer.domainEvents());
    primaryPort.success(boxer);
  }

  public static class CreateBoxerParams {

    private final String name;
    private final String alias;
    private final String boxrecUrl;

    public CreateBoxerParams(String name, String alias, String boxrecUrl) {
      this.name = name;
      this.alias = alias;
      this.boxrecUrl = boxrecUrl;
    }

    public String getName() {
      return name;
    }

    public String getAlias() {
      return alias;
    }

    public String getBoxrecUrl() {
      return boxrecUrl;
    }
  }
}
