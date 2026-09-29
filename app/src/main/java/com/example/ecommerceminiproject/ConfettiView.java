package com.example.ecommerceminiproject;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class ConfettiView extends View {

    private static final int PIECE_COUNT = 110;
    private static final int[] COLORS = {
            0xFFF9A825, 0xFF3DB54A, 0xFF2A86B1, 0xFFE4405F, 0xFF9C27B0, 0xFFFF7043
    };

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<Piece> pieces = new ArrayList<>();
    private final Random random = new Random(42); // fixed seed = same pattern every time

    public ConfettiView(Context context) {
        super(context);
    }

    public ConfettiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        generatePieces(w, h);
    }

    private void generatePieces(int width, int height) {
        pieces.clear();
        if (width == 0 || height == 0) return;
        float density = getResources().getDisplayMetrics().density;
        float clearLeft = width * 0.15f, clearRight = width * 0.85f;
        float clearTop = height * 0.30f, clearBottom = height * 0.65f;

        while (pieces.size() < PIECE_COUNT) {
            float x = random.nextFloat() * width;
            float y = random.nextFloat() * height;
            if (x > clearLeft && x < clearRight && y > clearTop && y < clearBottom) continue;

            Piece piece = new Piece();
            piece.x = x;
            piece.y = y;
            piece.width = (3 + random.nextInt(4)) * density;
            piece.height = (8 + random.nextInt(8)) * density;
            piece.rotation = random.nextInt(360);
            piece.color = COLORS[random.nextInt(COLORS.length)];
            piece.circle = random.nextInt(5) == 0;
            pieces.add(piece);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for (Piece piece : pieces) {
            paint.setColor(piece.color);
            canvas.save();
            canvas.translate(piece.x, piece.y);
            canvas.rotate(piece.rotation);
            if (piece.circle) {
                canvas.drawCircle(0, 0, piece.width, paint);
            } else {
                canvas.drawRect(-piece.width / 2, -piece.height / 2, piece.width / 2, piece.height / 2, paint);
            }
            canvas.restore();
        }
    }

    private static class Piece {
        float x, y, width, height, rotation;
        int color;
        boolean circle;
    }
}
