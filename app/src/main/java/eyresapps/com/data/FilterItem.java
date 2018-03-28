package eyresapps.com.data;

import android.support.v7.widget.CardView;
import android.widget.TextView;

/**
 * Created by thomaseyre on 28/03/2018.
 */

public class FilterItem {

    private CardView cardView;
    private TextView name;
    private String nameString;
    private Boolean show;

    public FilterItem(CardView cardView, TextView name, String nameString, Boolean filter){
        this.cardView = cardView;
        this.name = name;
        this.nameString = nameString;
        this.show = filter;
    }

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public CardView getCardView() {
        return cardView;
    }

    public Boolean getShow() {
        return show;
    }
}
