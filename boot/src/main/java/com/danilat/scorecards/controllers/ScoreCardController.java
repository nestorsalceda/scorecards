package com.danilat.scorecards.controllers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.usecases.scores.RetrieveScoreCards;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScoreCardController {

  private Model model;
  @Autowired
  private RetrieveScoreCards retrieveScoreCards;
  private PrimaryPort<Map<ScoreCardId, ScoreCard>> retrieveScoreCardsPrimaryPort = new PrimaryPort<Map<ScoreCardId, ScoreCard>>() {
    @Override
    public void success(Map<ScoreCardId, ScoreCard> response) {
      model.addAttribute("scorecards", response.values());
    }
  };

  @GetMapping("{accountId}/scorecards")
  public String listByAccount(@PathVariable String accountId, Model model) {
    this.model = model;
    this.model.addAttribute("accountId", accountId);
    retrieveScoreCards.execute(retrieveScoreCardsPrimaryPort, new AccountId(accountId));
    return "list-scorecards";
  }
}
