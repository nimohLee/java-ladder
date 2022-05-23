package ladder.domain;

import ladder.UserGenerator;
import ladder.contoller.InputView;
import ladder.contoller.ResultView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String gameContributors = InputView.findGameContributors();
        LadderGame ladderGame = new LadderGame(UserGenerator.generate(gameContributors));
        List<Line> lines = ladderGame.ready();
        ResultView.printLadder(ladderGame.drawUserList(), lines);

    }
}
