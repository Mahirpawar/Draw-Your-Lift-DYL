package com.dyl.drawyourlift.drawing.plan;

import android.content.Context;
import android.graphics.*;
import android.view.View;

import com.dyl.drawyourlift.data.model.LiftProject;
import com.dyl.drawyourlift.data.repository.ProjectRepository;

public class PlanView extends View {

    private static final float SCALE = 0.2f;

    private static final int TOP = 120;
    private static final int LEFT = 140;
    private static final int RIGHT = 180;
    private static final int BOTTOM = 180;

    // Engineering constants (mm)
    private static final int WALL_GAP = 40;
    private static final int CABIN_WALL = 50;
    private static final int DOOR_GAP = 40;
    private static final int DOOR_RECESS = 150;

    private Paint shaftPaint, cabinPaint, innerPaint, counterPaint;
    private Paint doorPaint, dimPaint, textPaint, centerPaint;

    public PlanView(Context c) {
        super(c);
        init();
    }

    private void init() {
        shaftPaint = stroke(Color.BLACK, 6);
        cabinPaint = fill(Color.LTGRAY);
        innerPaint = fill(Color.WHITE);
        counterPaint = fill(Color.DKGRAY);
        doorPaint = fill(Color.WHITE);
        dimPaint = stroke(Color.BLACK, 2);
        textPaint = text(22);
        centerPaint = stroke(Color.GRAY, 2);
        centerPaint.setPathEffect(new DashPathEffect(new float[]{10,10},0));
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        LiftProject p = ProjectRepository.getInstance().getProject();

        int shaftW = mm(p.shaftWidth);
        int shaftD = mm(p.shaftDepth);

        int sx = LEFT;
        int sy = TOP;
        int sr = sx + shaftW;
        int sb = sy + shaftD;

        int cx = sx + shaftW / 2;
        int cy = sy + shaftD / 2;

        // SHAFT
        c.drawRect(sx, sy, sr, sb, shaftPaint);

        // CENTERLINES
        c.drawLine(cx, sy, cx, sb, centerPaint);
        c.drawLine(sx, cy, sr, cy, centerPaint);

        // COUNTER
        int counter = mm(p.counterDbgSize);
        int counterX, counterY;

        if ("Left".equalsIgnoreCase(p.counterFrameSide)) {
            counterX = sx + mm(WALL_GAP);
            counterY = cy - counter / 2;
        } else if ("Right".equalsIgnoreCase(p.counterFrameSide)) {
            counterX = sr - mm(WALL_GAP) - counter;
            counterY = cy - counter / 2;
        } else {
            counterX = cx - counter / 2;
            counterY = sy + mm(WALL_GAP);
        }

        c.drawRect(counterX, counterY,
                counterX + counter, counterY + counter, counterPaint);

        // CABIN OUTER
        int cabinOuterW = shaftW - mm(WALL_GAP * 2);
        int cabinOuterD = shaftD - mm(WALL_GAP * 2 + DOOR_GAP + DOOR_RECESS);

        int cabinX = sx + mm(WALL_GAP);
        int cabinY = sy + mm(WALL_GAP);

        c.drawRect(cabinX, cabinY,
                cabinX + cabinOuterW,
                cabinY + cabinOuterD, cabinPaint);

        // CABIN INNER
        c.drawRect(
                cabinX + mm(CABIN_WALL),
                cabinY + mm(CABIN_WALL),
                cabinX + cabinOuterW - mm(CABIN_WALL),
                cabinY + cabinOuterD - mm(CABIN_WALL),
                innerPaint
        );

        // DOOR PASSAGE (VISUAL CONNECTION)
        int doorW = mm(p.clearOpening);
        int doorX = cx - doorW / 2;

        int passageTop = cabinY + cabinOuterD;
        int passageBottom = passageTop + mm(DOOR_RECESS);

        c.drawRect(
                doorX,
                passageTop,
                doorX + doorW,
                passageBottom,
                innerPaint
        );

        // LANDING DOORS (CENTER OPENING)
        int doorLeaf = doorW / 2;
        int doorY = passageBottom + mm(DOOR_GAP);

        c.drawRect(doorX, doorY,
                doorX + doorLeaf, doorY + mm(40), doorPaint);

        c.drawRect(doorX + doorLeaf, doorY,
                doorX + doorW, doorY + mm(40), doorPaint);

        c.drawRect(doorX, doorY,
                doorX + doorW, doorY + mm(40), shaftPaint);

        // TITLE
        c.drawText("PLAN VIEW", sx, sy - 30, textPaint);

        // DIMENSIONS
        int dy = sb + 40;
        drawH(c, doorX, doorX + doorW, dy,
                "Clear Opening : " + p.clearOpening + " mm");
        dy += 30;
        drawH(c, cabinX, cabinX + cabinOuterW, dy, "Cabin Outer");
        dy += 30;
        drawH(c, sx, sr, dy,
                "Shaft Width : " + p.shaftWidth + " mm");

        int dx = sr + 40;
        drawV(c, cabinY, cabinY + cabinOuterD, dx, "Cabin Depth");
        dx += 30;
        drawV(c, sy, sb, dx,
                "Shaft Depth : " + p.shaftDepth + " mm");
    }

    @Override
    protected void onMeasure(int w, int h) {
        LiftProject p = ProjectRepository.getInstance().getProject();
        setMeasuredDimension(
                mm(p.shaftWidth) + LEFT + RIGHT,
                mm(p.shaftDepth) + TOP + BOTTOM
        );
    }

    // ===== helpers =====
    private int mm(int v) { return (int)(v * SCALE); }

    private Paint stroke(int c, int w) {
        Paint p = new Paint();
        p.setColor(c);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(w);
        p.setAntiAlias(true);
        return p;
    }

    private Paint fill(int c) {
        Paint p = new Paint();
        p.setColor(c);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        return p;
    }

    private Paint text(int s) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(s);
        p.setAntiAlias(true);
        return p;
    }

    private void drawH(Canvas c, int x1, int x2, int y, String t) {
        c.drawLine(x1, y, x2, y, dimPaint);
        c.drawLine(x1, y - 8, x1, y + 8, dimPaint);
        c.drawLine(x2, y - 8, x2, y + 8, dimPaint);
        float w = textPaint.measureText(t);
        c.drawText(t, (x1 + x2)/2f - w/2f, y - 6, textPaint);
    }

    private void drawV(Canvas c, int y1, int y2, int x, String t) {
        c.drawLine(x, y1, x, y2, dimPaint);
        c.drawLine(x - 8, y1, x + 8, y1, dimPaint);
        c.drawLine(x - 8, y2, x + 8, y2, dimPaint);
        c.save();
        c.rotate(-90, x + 20, (y1 + y2)/2f);
        float w = textPaint.measureText(t);
        c.drawText(t, x + 20 - w/2f, (y1 + y2)/2f, textPaint);
        c.restore();
    }
}
