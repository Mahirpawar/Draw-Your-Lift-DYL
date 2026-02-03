package com.dyl.drawyourlift.drawing.plan;

import android.content.Context;
import android.graphics.*;
import android.view.View;

import com.dyl.drawyourlift.data.model.LiftProject;
import com.dyl.drawyourlift.data.repository.ProjectRepository;

public class PlanView extends View {

    private static final float SCALE = 0.2f; // 1 mm = 0.2 px

    // Margins
    private static final int LEFT = 140;
    private static final int TOP = 160;
    private static final int RIGHT = 180;
    private static final int BOTTOM = 220;

    // Engineering constants (REAL mm)
    private static final int RAIL_WIDTH = 65;
    private static final int CLEAR_GAP = 65;
    private static final int CABIN_WALL = 50;

    private static final int PASSAGE_DEPTH = 60;          // mm
    private static final int TRACK_THICKNESS = 40;        // mm
    private static final int LANDING_GAP = 30;            // mm
    private static final int LANDING_DOOR_THICKNESS = 40; // mm
    private static final int SHAFT_FACE_OFFSET = 10;      // mm
    // Counterweight visual thickness (short side)
    private static final int DBG_THICKNESS = 120; // mm
    private static final int COUNTER_BACK_OFFSET = 20;



    // VISUAL tuning (does NOT change real dimensions)
    // Visual exaggeration for counter DBG long side
    private static final float DBG_VISUAL_MULT = 1.6f;


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
        doorPaint = fill(Color.DKGRAY);
        dimPaint = stroke(Color.BLACK, 2);
        textPaint = text(22);
        centerPaint = stroke(Color.GRAY, 2);
        centerPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        LiftProject p = ProjectRepository.getInstance().getProject();

        // ================= SHAFT =================
        int shaftW = mm(p.shaftWidth);
        int shaftD = mm(p.shaftDepth);

        int sx = LEFT;
        int sy = TOP;
        int sr = sx + shaftW;
        int sb = sy + shaftD;

        int cx = sx + shaftW / 2;
        int cy = sy + shaftD / 2;

        c.drawRect(sx, sy, sr, sb, shaftPaint);

        // Centerlines
        c.drawLine(cx, sy, cx, sb, centerPaint);
        c.drawLine(sx, cy, sr, cy, centerPaint);

        // ================= WALL GAP (STEP 4) =================
        int wallGap = mm(p.mainBracketDistance + RAIL_WIDTH);

        // ================= COUNTER (RECTANGULAR DBG) =================

// Long side = user DBG size (parallel to door)
        int dbgLength = (int) (mm(p.counterDbgSize) * DBG_VISUAL_MULT);


// Short side = fixed visual thickness
        int dbgThickness = mm(DBG_THICKNESS);

        int counterX, counterY;

        if ("Left".equalsIgnoreCase(p.counterFrameSide)) {

            // Vertical DBG on left wall, long side horizontal
            counterX = sx + wallGap;
            counterY = cy - dbgThickness / 2;

        } else if ("Right".equalsIgnoreCase(p.counterFrameSide)) {

            counterX = sr - wallGap - dbgLength;
            counterY = cy - dbgThickness / 2;

        } else { // Back

            // FIXED anchor line from back wall (red line)
            int backDbgStart = sy + wallGap + mm(COUNTER_BACK_OFFSET);

// DBG always starts from this line
            counterY = backDbgStart;

// DBG grows only in length, position never shifts
            counterX = cx - dbgLength / 2;

        }

// Draw rectangular counterweight
        c.drawRect(
                counterX,
                counterY,
                counterX + dbgLength,
                counterY + dbgThickness,
                counterPaint
        );


        // ================= CABIN =================
        int cabinX, cabinY, cabinW, cabinD;

        if ("Back".equalsIgnoreCase(p.counterFrameSide)) {

            cabinX = sx + wallGap;
            cabinY = counterY + dbgThickness + mm(CLEAR_GAP);

            cabinW = shaftW - wallGap * 2;
            cabinD = sb - cabinY - wallGap - mm(PASSAGE_DEPTH);

        } else {

            if ("Left".equalsIgnoreCase(p.counterFrameSide)) {
                cabinX = counterX + dbgThickness + mm(CLEAR_GAP);
            } else {
                cabinX = sx + wallGap;
            }

            cabinY = sy + wallGap;
            cabinW = sr - wallGap - mm(CLEAR_GAP) - dbgThickness - cabinX;
            cabinD = shaftD - wallGap * 2;
        }


        // Safety clamp
        cabinW = Math.max(cabinW, mm(900));
        cabinD = Math.max(cabinD, mm(900));

        // Cabin outer
        c.drawRect(cabinX, cabinY,
                cabinX + cabinW, cabinY + cabinD, cabinPaint);
        c.drawRect(cabinX, cabinY,
                cabinX + cabinW, cabinY + cabinD, shaftPaint);

        // Cabin inner
        c.drawRect(
                cabinX + mm(CABIN_WALL),
                cabinY + mm(CABIN_WALL),
                cabinX + cabinW - mm(CABIN_WALL),
                cabinY + cabinD - mm(CABIN_WALL),
                innerPaint
        );

        // ================= DOOR SYSTEM (CORRECT STACK) =================

        int clearOpening = mm(p.clearOpening); // e.g. 700
        int halfDoor = clearOpening / 2;

        int doorLeft = cx - clearOpening / 2;
        int doorRight = cx + clearOpening / 2;

// REAL (non-compressed) vertical sizes
        int passagePx = mm(PASSAGE_DEPTH);              // 60
        int trackPx = mm(TRACK_THICKNESS);              // 40
        int gapPx = mm(LANDING_GAP);                    // 30
        int landingDoorPx = mm(LANDING_DOOR_THICKNESS); // 40

// Vertical stacking (TOP → BOTTOM)
        int passageTop = cabinY + cabinD;
        int passageBottom = passageTop + passagePx;

        int cabinTrackTop = passageBottom;
        int cabinTrackBottom = cabinTrackTop + trackPx;

        int landingGapTop = cabinTrackBottom;
        int landingGapBottom = landingGapTop + gapPx;

        int landingDoorTop = landingGapBottom;
        int landingDoorBottom = landingDoorTop + landingDoorPx;

// Clamp landing door inside shaft face (10 mm offset)
        int shaftFaceY = sb - mm(SHAFT_FACE_OFFSET);
        if (landingDoorBottom > shaftFaceY) {
            int shift = landingDoorBottom - shaftFaceY;
            landingDoorTop -= shift;
            landingDoorBottom -= shift;
            landingGapTop -= shift;
            landingGapBottom -= shift;
            cabinTrackTop -= shift;
            cabinTrackBottom -= shift;
            passageTop -= shift;
            passageBottom -= shift;
        }

// ---- PASSAGE ----
        c.drawRect(doorLeft, passageTop, doorRight, passageBottom, innerPaint);
        c.drawRect(doorLeft, passageTop, doorRight, passageBottom, shaftPaint);

// ---- CABIN DOOR TRACK ----
        int trackLeft = doorLeft - halfDoor;
        int trackRight = doorRight + halfDoor;

        c.drawRect(trackLeft, cabinTrackTop, trackRight, cabinTrackBottom, shaftPaint);

// ---- VISUAL GAP (30 mm) ----
        c.drawRect(
                doorLeft,
                landingGapTop,
                doorRight,
                landingGapBottom,
                innerPaint
        );

// ---- LANDING DOOR (CENTER OPEN, 40 mm) ----
        c.drawRect(
                doorLeft - halfDoor,
                landingDoorTop,
                doorLeft,
                landingDoorBottom,
                doorPaint
        );

        c.drawRect(
                doorRight,
                landingDoorTop,
                doorRight + halfDoor,
                landingDoorBottom,
                doorPaint
        );

        c.drawRect(
                doorLeft,
                landingDoorTop,
                doorRight,
                landingDoorBottom,
                shaftPaint
        );


        // ================= TEXT =================
        c.drawText("PLAN VIEW", sx, sy - 40, textPaint);

        // ================= DIMENSIONS =================
        int dy = sb + 40;
        drawH(c, doorLeft, doorRight, dy,
                "Clear Opening : " + p.clearOpening + " mm");

        dy += 30;
        drawH(c, cabinX, cabinX + cabinW, dy, "Cabin Outer");

        dy += 30;
        drawH(c, sx, sr, dy,
                "Shaft Width : " + p.shaftWidth + " mm");

        int dx = sr + 40;
        drawV(c, cabinY, cabinY + cabinD, dx, "Cabin Depth");

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

    // ================= HELPERS =================
    private int mm(int v) { return (int) (v * SCALE); }

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
        c.drawText(t, (x1 + x2) / 2f - w / 2f, y - 6, textPaint);
    }

    private void drawV(Canvas c, int y1, int y2, int x, String t) {
        c.drawLine(x, y1, x, y2, dimPaint);
        c.drawLine(x - 8, y1, x + 8, y1, dimPaint);
        c.drawLine(x - 8, y2, x + 8, y2, dimPaint);
        c.save();
        c.rotate(-90, x + 20, (y1 + y2) / 2f);
        float w = textPaint.measureText(t);
        c.drawText(t, x + 20 - w / 2f, (y1 + y2) / 2f, textPaint);
        c.restore();
    }
}
