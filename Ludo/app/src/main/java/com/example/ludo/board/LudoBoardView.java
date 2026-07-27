package com.example.ludo.board;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.ludo.R;

public class LudoBoardView extends View {

    private Paint paintRed, paintGreen, paintYellow, paintBlue;
    private Paint paintWhite, paintBorder;

    private float boardSize;
    private float cellSize;

    private Paint paintTokenStroke;
    private Paint paintTokenShadow;

    // A standard Ludo board is a 15x15 grid
    private static final int GRID_SIZE = 15;
    private static final int BASE_SIZE = 6; // Home bases are 6x6 cells

    public LudoBoardView(Context context) {
        super(context);
        init(context);
    }

    public LudoBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LudoBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Initialize Paints for the 4 primary colors
        paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRed.setColor(ContextCompat.getColor(context, R.color.ludo_red));
        paintRed.setStyle(Paint.Style.FILL);

        paintGreen = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGreen.setColor(ContextCompat.getColor(context, R.color.ludo_green));
        paintGreen.setStyle(Paint.Style.FILL);

        paintYellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintYellow.setColor(ContextCompat.getColor(context, R.color.ludo_yellow));
        paintYellow.setStyle(Paint.Style.FILL);

        paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBlue.setColor(ContextCompat.getColor(context, R.color.ludo_blue));
        paintBlue.setStyle(Paint.Style.FILL);

        // White for the walking paths
        paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintWhite.setColor(Color.WHITE);
        paintWhite.setStyle(Paint.Style.FILL);

        // Dark border for grid lines
        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(Color.parseColor("#D0D9E8")); // Soft grey-blue border
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(2f);

        // 2. Initialize the variables INSIDE the method
        paintTokenStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTokenStroke.setColor(Color.WHITE);
        paintTokenStroke.setStyle(Paint.Style.STROKE);
        paintTokenStroke.setStrokeWidth(4f);

        paintTokenShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTokenShadow.setColor(Color.parseColor("#40000000")); // 25% black shadow
        paintTokenShadow.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // The board is a square. We take the smaller dimension to guarantee it fits.
        boardSize = Math.min(w, h);
        cellSize = boardSize / GRID_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (boardSize == 0) return;

        // Draw the white background for the entire board
        canvas.drawRect(0, 0, boardSize, boardSize, paintWhite);

        // 1. Draw the 4 Home Bases (6x6 cells each)
        drawHomeBase(canvas, 0, 0, paintRed);                                    // Top-Left
        drawHomeBase(canvas, (GRID_SIZE - BASE_SIZE) * cellSize, 0, paintGreen); // Top-Right
        drawHomeBase(canvas, 0, (GRID_SIZE - BASE_SIZE) * cellSize, paintYellow);// Bottom-Left
        drawHomeBase(canvas, (GRID_SIZE - BASE_SIZE) * cellSize, (GRID_SIZE - BASE_SIZE) * cellSize, paintBlue); // Bottom-Right

        // 2. Draw the Grid Lines for the paths
        drawGridLines(canvas);

        // 3. Draw Center Finish Area (Triangles)
        drawCenterHome(canvas);

        drawHomeTokens(canvas); // <--- ADD THIS

        // Draw the dynamic list of tokens!
        drawTokens(canvas);
    }

    private void drawHomeBase(Canvas canvas, float startX, float startY, Paint colorPaint) {
        float size = BASE_SIZE * cellSize;

        // Draw main colored square
        canvas.drawRect(startX, startY, startX + size, startY + size, colorPaint);
        canvas.drawRect(startX, startY, startX + size, startY + size, paintBorder);

        // Draw inner white square (where tokens sit)
        float padding = cellSize;
        canvas.drawRect(
                startX + padding,
                startY + padding,
                startX + size - padding,
                startY + size - padding,
                paintWhite
        );

        // (Token placeholder circles will be drawn inside this white area in the next step)
    }

    private void drawGridLines(Canvas canvas) {
        // We draw the vertical and horizontal grid lines for the cross-shaped path
        for (int i = 0; i <= GRID_SIZE; i++) {
            float pos = i * cellSize;

            // Draw lines for the vertical path column (x between 6 and 9)
            if (i >= BASE_SIZE && i <= GRID_SIZE - BASE_SIZE) {
                canvas.drawLine(pos, 0, pos, boardSize, paintBorder); // Vertical lines
                canvas.drawLine(0, pos, boardSize, pos, paintBorder); // Horizontal lines
            }
        }

        // Horizontal lines through the vertical path
        for(int i = 0; i <= GRID_SIZE; i++){
            float pos = i * cellSize;
            canvas.drawLine(BASE_SIZE * cellSize, pos, (GRID_SIZE - BASE_SIZE) * cellSize, pos, paintBorder);
            canvas.drawLine(pos, BASE_SIZE * cellSize, pos, (GRID_SIZE - BASE_SIZE) * cellSize, paintBorder);
        }
    }

    private void drawCenterHome(Canvas canvas) {
        float centerStartX = BASE_SIZE * cellSize;
        float centerStartY = BASE_SIZE * cellSize;
        float centerSize = 3 * cellSize;

        Path path = new Path();

        // Top Triangle (Green)
        path.moveTo(centerStartX, centerStartY);
        path.lineTo(centerStartX + centerSize, centerStartY);
        path.lineTo(centerStartX + (centerSize / 2), centerStartY + (centerSize / 2));
        path.close();
        canvas.drawPath(path, paintGreen);
        canvas.drawPath(path, paintBorder);

        // Left Triangle (Red)
        path.reset();
        path.moveTo(centerStartX, centerStartY);
        path.lineTo(centerStartX, centerStartY + centerSize);
        path.lineTo(centerStartX + (centerSize / 2), centerStartY + (centerSize / 2));
        path.close();
        canvas.drawPath(path, paintRed);
        canvas.drawPath(path, paintBorder);

        // Right Triangle (Blue)
        path.reset();
        path.moveTo(centerStartX + centerSize, centerStartY);
        path.lineTo(centerStartX + centerSize, centerStartY + centerSize);
        path.lineTo(centerStartX + (centerSize / 2), centerStartY + (centerSize / 2));
        path.close();
        canvas.drawPath(path, paintBlue);
        canvas.drawPath(path, paintBorder);

        // Bottom Triangle (Yellow)
        path.reset();
        path.moveTo(centerStartX, centerStartY + centerSize);
        path.lineTo(centerStartX + centerSize, centerStartY + centerSize);
        path.lineTo(centerStartX + (centerSize / 2), centerStartY + (centerSize / 2));
        path.close();
        canvas.drawPath(path, paintYellow);
        canvas.drawPath(path, paintBorder);
    }




    // Add this new method to draw tokens sitting inside their home bases
    private void drawHomeTokens(Canvas canvas) {
        if (boardSize == 0) return;

        // We will pass the exact positions later, but to test the UI,
        // let's draw a placeholder token in the top-left Red base.
        float startX = 0;
        float startY = 0;

        // Center of the 4 quadrants inside the home base
        float innerBoxStart = cellSize;
        float innerBoxSize = 4 * cellSize;
        float quadCenterOffset = innerBoxSize / 4f;

        // Top Left Token inside Top Left Base
        float tokenX = startX + innerBoxStart + quadCenterOffset;
        float tokenY = startY + innerBoxStart + quadCenterOffset;
        float tokenRadius = cellSize * 0.45f; // slightly smaller than a cell

        // Draw shadow
        canvas.drawCircle(tokenX + 4f, tokenY + 6f, tokenRadius, paintTokenShadow);
        // Draw token color
        canvas.drawCircle(tokenX, tokenY, tokenRadius, paintRed);
        // Draw token white rim (makes it look like a physical piece)
        canvas.drawCircle(tokenX, tokenY, tokenRadius, paintTokenStroke);

        // Note: In the Game Logic module, we will create a loop that takes a
        // List<Token> and places all 16 tokens correctly based on their state!
    }

    // Call drawHomeTokens(canvas) at the very bottom of your onDraw() method:

    // 1. Add this list to the top of LudoBoardView.java
    private java.util.List<com.example.ludo.models.Token> activeTokens = new java.util.ArrayList<>();

    // 2. Add this public method so GameActivity can update the board
    public void updateTokens(java.util.List<com.example.ludo.models.Token> tokens) {
        this.activeTokens = tokens;
        invalidate(); // Forces the view to redraw itself immediately
    }

    // 3. Replace the old "drawHomeTokens" placeholder method with this complete logic:
    private void drawTokens(Canvas canvas) {
        if (boardSize == 0 || activeTokens == null) return;

        float tokenRadius = cellSize * 0.35f;

        for (com.example.ludo.models.Token token : activeTokens) {
            float cx, cy;

            if (token.isAtHome()) {
                // Draw token in its respective home base quadrant
                cx = getHomeX(token);
                cy = getHomeY(token);
            } else {
                // Token is on the path! Translate grid (row/col) to screen pixels
                com.example.ludo.models.GridPoint gp =
                        com.example.ludo.utils.BoardMapper.getGridPosition(token.getPlayerIndex(), token.getPosition());

                if (gp != null) {
                    cx = (gp.col * cellSize) + (cellSize / 2f);
                    cy = (gp.row * cellSize) + (cellSize / 2f);
                } else {
                    continue; // Skip if invalid
                }
            }

            // Figure out the paint color for the token
            Paint currentTokenPaint;
            switch (token.getPlayerIndex()) {
                case 0: currentTokenPaint = paintRed; break;
                case 1: currentTokenPaint = paintGreen; break;
                case 2: currentTokenPaint = paintBlue; break;
                default: currentTokenPaint = paintYellow; break;
            }

            // Draw shadow, body, and stroke
            canvas.drawCircle(cx + 4f, cy + 6f, tokenRadius, paintTokenShadow);
            canvas.drawCircle(cx, cy, tokenRadius, currentTokenPaint);
            canvas.drawCircle(cx, cy, tokenRadius, paintTokenStroke);

            // Nice Extra: Draw a subtle inner ring to make it look like a physical plastic token
            canvas.drawCircle(cx, cy, tokenRadius * 0.6f, paintTokenStroke);
        }
    }

    // Helper to calculate exact pixel coordinates for tokens inside their home bases
    private float getHomeX(com.example.ludo.models.Token token) {
        int p = token.getPlayerIndex();
        int id = token.getId(); // 0 to 3 for the four tokens

        float baseStartX = (p == 1 || p == 2) ? (9 * cellSize) : 0; // Green & Blue are on the right
        float innerOffset = 1.5f * cellSize;

        return baseStartX + innerOffset + ((id % 2 == 1) ? (3 * cellSize) : 0);
    }

    private float getHomeY(com.example.ludo.models.Token token) {
        int p = token.getPlayerIndex();
        int id = token.getId();

        float baseStartY = (p == 2 || p == 3) ? (9 * cellSize) : 0; // Blue & Yellow are on the bottom
        float innerOffset = 1.5f * cellSize;

        return baseStartY + innerOffset + ((id >= 2) ? (3 * cellSize) : 0);
    }



}