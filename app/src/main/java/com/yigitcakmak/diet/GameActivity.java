package com.yigitcakmak.diet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    ImageButton replayBtn;
    TextView turnTxt;
    View bgRed, bgBlue;

    int[][] gridArray = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    int totalMoves = 0;
    int turn = 0;

    String player1 = "Player 1", player2 = "CPU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        image1 = findViewById(R.id.img_grid_1);
        image2 = findViewById(R.id.img_grid_2);
        image3 = findViewById(R.id.img_grid_3);
        image4 = findViewById(R.id.img_grid_4);
        image5 = findViewById(R.id.img_grid_5);
        image6 = findViewById(R.id.img_grid_6);
        image7 = findViewById(R.id.img_grid_7);
        image8 = findViewById(R.id.img_grid_8);
        image9 = findViewById(R.id.img_grid_9);
        turnTxt = findViewById(R.id.txt_turn_name);
        replayBtn = findViewById(R.id.imgbtn_replay);
        bgRed = findViewById(R.id.view_bg_red);
        bgBlue = findViewById(R.id.view_bg_blue);

        replayBtn.setVisibility(View.GONE);

        Intent intent = getIntent();
        player1 = intent.getStringExtra("player1");
        player2 = intent.getStringExtra("player2");

        if (player2 == null) {
            player2 = "CPU";
        }
        String turnStr = "Sıra: " + player1;
        turnTxt.setText(turnStr);

        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);
        image7.setOnClickListener(this);
        image8.setOnClickListener(this);
        image9.setOnClickListener(this);

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Log.e("Turn", turn + " " + totalMoves);
        switch (view.getId()) {
            case R.id.img_grid_1:
                processInput(0, 0, image1);
                break;
            case R.id.img_grid_2:
                processInput(0, 1, image2);
                break;
            case R.id.img_grid_3:
                processInput(0, 2, image3);
                break;
            case R.id.img_grid_4:
                processInput(1, 0, image4);
                break;
            case R.id.img_grid_5:
                processInput(1, 1, image5);
                break;
            case R.id.img_grid_6:
                processInput(1, 2, image6);
                break;
            case R.id.img_grid_7:
                processInput(2, 0, image7);
                break;
            case R.id.img_grid_8:
                processInput(2, 1, image8);
                break;
            case R.id.img_grid_9:
                processInput(2, 2, image9);
                break;
        }
        replayBtn.setVisibility(View.VISIBLE);
    }

    private void processInput(int row, int col, ImageView image) {
        if (gridArray[row][col] == 0) {
            if (turn % 2 == 0) {
                image.setImageResource(R.drawable.boyoz);
                gridArray[row][col] = 1;
            } else {
                image.setImageResource(R.drawable.yumurta);
                gridArray[row][col] = 2;
            }
            turn += 1;
            totalMoves += 1;

            if (turn % 2 == 0) {
                crossFadeToRed();
                String turnStr = "Turn: " + player1;
                turnTxt.setText(turnStr);
            } else {
                crossFadeToBlue();
                String turnStr = "Turn: " + player2;
                turnTxt.setText(turnStr);
                if (player2.equals("CPU") && !checkRows() && !checkDiagonals() && !checkColumns()) {
                    disableInput();
                    Snackbar.make(replayBtn.getRootView(), "", 1000).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cpuMove();
                            crossFadeToRed();
                            String turnStr = "" + player1;
                            turnTxt.setText(turnStr);
                            turn += 1;
                            totalMoves += 1;
                            enableInput();
                        }
                    }, 1000);
                }
            }
            checkGrid();
        } else {
            image.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Onayla")
                .setMessage("Tekrar ?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gridArray = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
                        image1.setImageResource(R.drawable.ic_empty_cell);
                        image2.setImageResource(R.drawable.ic_empty_cell);
                        image3.setImageResource(R.drawable.ic_empty_cell);
                        image4.setImageResource(R.drawable.ic_empty_cell);
                        image5.setImageResource(R.drawable.ic_empty_cell);
                        image6.setImageResource(R.drawable.ic_empty_cell);
                        image7.setImageResource(R.drawable.ic_empty_cell);
                        image8.setImageResource(R.drawable.ic_empty_cell);
                        image9.setImageResource(R.drawable.ic_empty_cell);

                        enableInput();

                        turn = 0;
                        String turnStr = "Sıra: " + player1;
                        turnTxt.setText(turnStr);
                        totalMoves = 0;
                    }
                })
                .setNegativeButton("Hayır", null);
        builder.show();
    }

    private boolean checkDiagonals() {
        if (gridArray[0][0] == gridArray[1][1] &&
                gridArray[1][1] == gridArray[2][2] &&
                gridArray[0][0] != 0)
            return true;

        if (gridArray[0][2] == gridArray[1][1] &&
                gridArray[1][1] == gridArray[2][0] &&
                gridArray[0][2] != 0)
            return true;

        return false;
    }

    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (gridArray[i][0] == gridArray[i][1] &&
                    gridArray[i][1] == gridArray[i][2] &&
                    gridArray[i][0] != 0)
                return true;
        }
        return false;
    }

    private boolean checkColumns() {
        for (int i = 0; i < 3; i++) {
            if (gridArray[0][i] == gridArray[1][i] &&
                    gridArray[1][i] == gridArray[2][i] &&
                    gridArray[0][i] != 0)
                return true;
        }
        return false;
    }

    private boolean isBoardFilled() {
        return totalMoves != 9;
    }

    private void checkGrid() {
        if (!isBoardFilled()) {
            Toast.makeText(getApplicationContext(), "Tahtada boş yer kalmadı", Toast.LENGTH_SHORT).show();
            disableInput();
        }
        if (checkColumns() || checkDiagonals() || checkRows()) {
            if ((turn + 1) % 2 == 0) {
                Toast.makeText(getApplicationContext(), player1 + " kazanan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), player2 + " kazanan", Toast.LENGTH_SHORT).show();
            }
            disableInput();
        }
    }

    private void disableInput() {
        image1.setClickable(false);
        image2.setClickable(false);
        image3.setClickable(false);
        image4.setClickable(false);
        image5.setClickable(false);
        image6.setClickable(false);
        image7.setClickable(false);
        image8.setClickable(false);
        image9.setClickable(false);
    }

    private void enableInput() {
        image1.setClickable(true);
        image2.setClickable(true);
        image3.setClickable(true);
        image4.setClickable(true);
        image5.setClickable(true);
        image6.setClickable(true);
        image7.setClickable(true);
        image8.setClickable(true);
        image9.setClickable(true);
    }

    private void crossFadeToBlue() {
        bgBlue.setAlpha(0f);
        bgBlue.setVisibility(View.VISIBLE);

        bgBlue.animate()
                .alpha(1f)
                .setDuration(350)
                .setListener(null);

        bgRed.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bgRed.setVisibility(View.GONE);
                    }
                });
    }

    private void crossFadeToRed() {
        bgRed.setAlpha(0f);
        bgRed.setVisibility(View.VISIBLE);

        bgRed.animate()
                .alpha(1f)
                .setDuration(350)
                .setListener(null);

        bgBlue.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bgBlue.setVisibility(View.GONE);
                    }
                });
    }

    private void cpuMove() {
        Random random = new Random();

        switch (random.nextInt(8)) {
            case 0:
                if (gridArray[0][0] == 0) {
                    gridArray[0][0] = 2;
                    image1.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 1:
                if (gridArray[0][1] == 0) {
                    gridArray[0][1] = 2;
                    image2.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 2:
                if (gridArray[0][2] == 0) {
                    gridArray[0][2] = 2;
                    image3.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 3:
                if (gridArray[1][0] == 0) {
                    gridArray[1][0] = 2;
                    image4.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 4:
                if (gridArray[1][1] == 0) {
                    gridArray[1][1] = 2;
                    image5.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 5:
                if (gridArray[1][2] == 0) {
                    gridArray[1][2] = 2;
                    image6.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 6:
                if (gridArray[2][0] == 0) {
                    gridArray[2][0] = 2;
                    image7.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 7:
                if (gridArray[2][1] == 0) {
                    gridArray[2][1] = 2;
                    image8.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
            case 8:
                if (gridArray[2][2] == 0) {
                    gridArray[2][2] = 2;
                    image9.setImageResource(R.drawable.yumurta);
                } else {
                    cpuMove();
                }
                break;
        }
        if (checkColumns() || checkDiagonals() || checkRows()) {
            disableInput();
            Toast.makeText(getApplicationContext(), player2 + " kazanan", Toast.LENGTH_SHORT).show();
        }
    }
}
