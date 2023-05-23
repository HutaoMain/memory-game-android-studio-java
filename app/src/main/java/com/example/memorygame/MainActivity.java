package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private List<ImageButton> buttons;
    private List<MemoryCard> cards;
    private Integer indexOfSingleSelectedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.cow1);
        images.add(R.drawable.elepant1);
        images.add(R.drawable.pig1);
        images.add(R.drawable.sheep1);

        // Add each image twice so we can create pairs
        images.addAll(images);

        // Randomize the order of the images
        Collections.shuffle(images);

        buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.imageButton10));
        buttons.add(findViewById(R.id.imageButton2));
        buttons.add(findViewById(R.id.imageButton3));
        buttons.add(findViewById(R.id.imageButton4));
        buttons.add(findViewById(R.id.imageButton5));
        buttons.add(findViewById(R.id.imageButton6));
        buttons.add(findViewById(R.id.imageButton7));
        buttons.add(findViewById(R.id.imageButton8));

        cards = new ArrayList<>();
        for (int index = 0; index < buttons.size(); index++) {
            MemoryCard card = new MemoryCard(images.get(index));
            cards.add(card);
        }

        for (int index = 0; index < buttons.size(); index++) {
            final int position = index;
            buttons.get(index).setOnClickListener(view -> {
                Log.i(TAG, "button click!");
                // Update the models
                updateModels(position);
                // Update the UI for the game
                updateViews();
            });
        }

        ImageButton resetButton = findViewById(R.id.imageButton24);
        resetButton.setOnClickListener(view -> resetGame());
    }

    private void updateModels(int position) {
        MemoryCard card = cards.get(position);
        if (card.isFaceUp) {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (indexOfSingleSelectedCard == null) {
            restoreCards();
            indexOfSingleSelectedCard = position;
        } else {
            checkForMatch(indexOfSingleSelectedCard, position);
            indexOfSingleSelectedCard = null;
        }
        card.isFaceUp = !card.isFaceUp;
    }

    private void updateViews() {
        for (int index = 0; index < cards.size(); index++) {
            MemoryCard card = cards.get(index);
            ImageButton button = buttons.get(index);
            button.setImageResource(card.isFaceUp ? card.identifier : R.drawable.question);
        }
    }

    private void checkForMatch(int position1, int position2) {
        if (cards.get(position1).identifier == cards.get(position2).identifier) {
            Toast.makeText(this, "Match found!", Toast.LENGTH_SHORT).show();
            cards.get(position1).isMatched = true;
            cards.get(position2).isMatched = true;
        }
    }

    private void restoreCards() {
        for (MemoryCard card : cards) {
            if (!card.isMatched) {
                card.isFaceUp = false;
            }
        }
    }

    private void resetGame() {
        // Reset card states
        for (MemoryCard card : cards) {
            card.isFaceUp = false;
            card.isMatched = false;
        }

        // Shuffle images
        Collections.shuffle(cards);

        // Reset UI
        updateViews();
    }
}