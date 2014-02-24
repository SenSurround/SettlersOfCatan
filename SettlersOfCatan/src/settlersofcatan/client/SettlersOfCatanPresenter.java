package settlersofcatan.client;

import java.util.List;

import com.google.common.base.Optional;
import settlersofcatan.client.GameApi.Container;
import settlersofcatan.client.GameApi.UpdateUI;

public class SettlersOfCatanPresenter {
    
    interface View {
        /**
         * Sets the presenter. The viewer will call certain methods on the presenter, e.g.,
         * when a card is selected ({@link #cardSelected}),
         * when selection is done ({@link #finishedSelectingCards}), etc.
         *
         * The process of making a claim looks as follows to the viewer:
         * 1) The viewer calls {@link #cardSelected} a couple of times to select the cards to drop to
         * the middle pile
         * 2) The viewer calls {@link #finishedSelectingCards} to finalize his selection
         * 3) The viewer calls {@link #rankSelected} to pass the selected rank, which sends the claim.
         * The process of making a claim looks as follows to the presenter:
         * 1) The presenter calls {@link #chooseNextCard} and passes the current selection.
         * 2) The presenter calls {@link #chooseRankForClaim} and passes the possible ranks.
         */
        void setPresenter(SettlersOfCatanPresenter settlersOfCatanPresenter);

        /** Sets the state for a viewer, i.e., not one of the players. */
        void setViewerState(int numberOfWhiteCards, int numberOfBlackCards,
            int numberOfCardsInMiddlePile,
            CheaterMessage cheaterMessage);

        /**
         * Sets the state for a player (whether the player has the turn or not).
         * The "declare cheater" button should be enabled only for CheaterMessage.IS_OPPONENT_CHEATING.
         */
        void setPlayerState(
                List<String> resourceCards,
                List<String> developmentCards,
                int victoryPoints,
                boolean longestRoad,
                boolean largestArmy);

        /**
         * Asks the player to choose the next card or finish his selection.
         * We pass what cards are selected (those cards will be dropped to the
         * middle pile), and what cards will remain in the player hands.
         * The user can either select a card (by calling {@link #cardSelected),
         * or finish selecting
         * (by calling {@link #finishedSelectingCards}; only allowed if selectedCards.size>1).
         * If the user selects a card from selectedCards, then it moves that card to remainingCards.
         * If the user selects a card from remainingCards, then it moves that card to selectedCards.
         */
        void chooseNextCard(List<Card> selectedCards, List<Card> remainingCards);

        /**
         * After the player finished selecting 1-4 cards, the player needs to choose the rank for his
         * claim.
         * The possible ranks depend on the rank Y in the previous claim (possibleRanks is Y-1,Y,Y+1),
         * or all ranks if there wasn't a previous claim.
         */
        void chooseRankForClaim(List<Rank> possibleRanks);
      }
    
    private final SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    private final View view;
    private final Container container;
    /** A viewer doesn't have a color. */
    private Optional<Color> myColor;
    private CheatState cheatState;
    private List<Card> selectedCards;

    public SettlersOfCatanPresenter(View view, Container container) {
      this.view = view;
      this.container = container;
      view.setPresenter(this);
    }
    
    public void updateUI(UpdateUI updateUI) {
      }

}
