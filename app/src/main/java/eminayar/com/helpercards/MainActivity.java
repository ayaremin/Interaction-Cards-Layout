package eminayar.com.helpercards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import eminayar.com.cardhelper.CardClickListener;
import eminayar.com.cardhelper.CardClickLongListener;
import eminayar.com.cardhelper.HelperCardsLayout;
import eminayar.com.cardhelper.models.CardItem;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements CardClickListener, CardClickLongListener {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    HelperCardsLayout layout = (HelperCardsLayout) findViewById(R.id.cardHelper);

    ArrayList<CardItem> cardItems = new ArrayList<>();

    layout.setOnCardClickListener(this);

    cardItems.add(new CardItem("Short Title With Image",
        "Description this can be some long text, " + "layout" + "will scale itself",
        R.drawable.barchart));

    cardItems.add(new CardItem("Short Title With No Image",
        "Description this can be some long text, layout" + "will scale itself"));

    cardItems.add(new CardItem("Long title example to demonstrate users how can this textview can"
        + " be longer and longer with image",
        "Description this can be some long text, layout" + "will scale itself",
        R.drawable.getmoney));

    cardItems.add(new CardItem("Long title example to demonstrate users how can this textview can"
        + " be longer and longer without image",
        "Description this can be some long text, layout" + "will scale itself"));

    cardItems.add(new CardItem("Very very long description example",
        "Description this can be some long text, layout"
            + "will scale itself"
            + "Description this can be some long text, layout"
            + "will scale itself"
            + "Description this can be some long text, layout"
            + "will scale itself", R.drawable.celebration));

    layout.setItems(cardItems);
    layout.setOnCardClickListener(this);
    layout.setOnCardLongClickListener(this);
  }

  @Override public void onCardClicked(int pos, CardItem cardItem) {
    Toast.makeText(this, "CLİCKED " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
  }

  @Override public void onCardLongClicked(int pos, CardItem cardItem) {
    Toast.makeText(this, "LONG CLİCKED " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
  }
}
