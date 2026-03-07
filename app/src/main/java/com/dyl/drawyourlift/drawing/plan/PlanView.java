package com.dyl.drawyourlift.drawing.plan;

import android.content.Context;
import android.graphics.*;
import android.view.View;

import com.dyl.drawyourlift.data.model.LiftProject;
import com.dyl.drawyourlift.data.repository.ProjectRepository;

public class PlanView extends View {

    private static final float SCALE = 0.2f;

    private static final int LEFT = 140;
    private static final int TOP = 160;
    private static final int RIGHT = 180;
    private static final int BOTTOM = 220;

    private static final int RAIL_WIDTH = 65;
    private static final int CLEAR_GAP = 65;
    private static final int CABIN_WALL = 50;

    private static final int PASSAGE_DEPTH = 60;
    private static final int TRACK_THICKNESS = 40;
    private static final int LANDING_GAP = 30;
    private static final int LANDING_DOOR_THICKNESS = 40;
    private static final int SHAFT_FACE_OFFSET = 10;

    private static final int DBG_THICKNESS = 120;
    private static final int COUNTER_BACK_OFFSET = 70;

    private static final float DBG_VISUAL_MULT = 1.6f;

    private Paint shaftPaint, cabinPaint, innerPaint, counterPaint;
    private Paint doorPaint, dimPaint, textPaint, centerPaint;
    private Paint capacityPaint;



    public PlanView(Context c) {
        super(c);
        init();
    }

    private void init() {

        shaftPaint = stroke(Color.BLACK,6);
        cabinPaint = fill(Color.LTGRAY);
        innerPaint = fill(Color.WHITE);
        counterPaint = fill(Color.DKGRAY);
        doorPaint = fill(Color.DKGRAY);

        dimPaint = stroke(Color.BLACK,2);
        textPaint = text(22);

        centerPaint = stroke(Color.GRAY,2);
        centerPaint.setPathEffect(new DashPathEffect(new float[]{10,10},0));

        capacityPaint = new Paint();
        capacityPaint.setColor(Color.RED); // gold color
        capacityPaint.setTextSize(32);
        capacityPaint.setTextAlign(Paint.Align.CENTER);
        capacityPaint.setFakeBoldText(true);
        capacityPaint.setAntiAlias(true);
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

        int cx = sx + shaftW/2;
        int cy = sy + shaftD/2;

        c.drawRect(sx,sy,sr,sb,shaftPaint);

        c.drawLine(cx,sy,cx,sb,centerPaint);
        c.drawLine(sx,cy,sr,cy,centerPaint);

        // ================= GAPS =================

        int railGap = mm(p.mainBracketDistance + RAIL_WIDTH);
        int counterGap = mm(p.counterBracketDistance + RAIL_WIDTH);

        int backGap = mm(p.cabinWallGap > 0 ? p.cabinWallGap : RAIL_WIDTH);

        // ================= COUNTER =================

        int dbgLength = (int)(mm(p.counterDbgSize) * DBG_VISUAL_MULT);
        int dbgThickness = mm(DBG_THICKNESS);

        int counterX, counterY;
        int counterW, counterH;

        if("Left".equalsIgnoreCase(p.counterFrameSide)){

            counterW = dbgThickness;
            counterH = dbgLength;

            counterX = sx + counterGap;
            counterY = cy - counterH/2;

        }
        else if("Right".equalsIgnoreCase(p.counterFrameSide)){

            counterW = dbgThickness;
            counterH = dbgLength;

            counterX = sr - counterGap - counterW;
            counterY = cy - counterH/2;

        }
        else{

            int counterWallGapMm =
                    p.counterBracketDistance + COUNTER_BACK_OFFSET;

            int backDbgStart = sy + mm(counterWallGapMm);

            counterX = cx - dbgLength/2;
            counterY = backDbgStart;

            counterW = dbgLength;
            counterH = dbgThickness;
        }

        c.drawRect(counterX,counterY,
                counterX+counterW,counterY+counterH,counterPaint);

        // ================= CABIN =================

        int cabinX, cabinY, cabinW, cabinD;

        int baseGap = mm(50); // default clearance from wall

        int doorStack =
                mm(PASSAGE_DEPTH)
                        + mm(TRACK_THICKNESS)
                        + mm(LANDING_GAP)
                        + mm(LANDING_DOOR_THICKNESS);

        int cabinFront = sb - doorStack;

// wall gaps increase with main bracket distance


        int leftGap  = baseGap + mm(effectiveWallGapMM);
        int rightGap = baseGap + mm(effectiveWallGapMM);

// back gap (user input)
        int backGapLocal = baseGap + mm(p.cabinWallGap);

        if ("Back".equalsIgnoreCase(p.counterFrameSide)) {

            cabinX = sx + leftGap;
            cabinW = shaftW - leftGap - rightGap;

            cabinY = counterY + dbgThickness + mm(CLEAR_GAP);
            cabinD = cabinFront - cabinY;
        }
        else if ("Left".equalsIgnoreCase(p.counterFrameSide)) {

            // cabin must be to the right of counter
            int counterClear = counterX + dbgThickness + mm(CLEAR_GAP);

            // left boundary is max of wall clearance or counter clearance
            cabinX = Math.max(sx + leftGap, counterClear);

            cabinW = sr - rightGap - cabinX;

            cabinD = shaftD - backGapLocal - doorStack;
            cabinY = cabinFront - cabinD;
        }
        else { // RIGHT COUNTER

            int counterClear = counterX - mm(CLEAR_GAP);

            int rightLimit = Math.min(sr - rightGap, counterClear);

            cabinX = sx + leftGap;
            cabinW = rightLimit - cabinX;

            cabinD = shaftD - backGapLocal - doorStack;
            cabinY = cabinFront - cabinD;
        }

// ================= SAFETY =================

        cabinW = Math.max(mm(900), cabinW);
        cabinD = Math.max(mm(900), cabinD);


        if (cabinX + cabinW > sr)
            cabinW = sr - cabinX;

        if (cabinY + cabinD > sb)
            cabinD = sb - cabinY;

        // ================= FINAL CABIN SIZE (MM) =================

        int cabinWidthMM = pxToMm(cabinW);
        int cabinDepthMM = pxToMm(cabinD);
        int cabinArea = cabinWidthMM * cabinDepthMM;
        float persons = cabinArea / 210000f;

// round to 1 decimal
        persons = Math.round(persons * 10f) / 10f;


// ================= DRAW CABIN =================

        c.drawRect(cabinX, cabinY,
                cabinX + cabinW, cabinY + cabinD,
                cabinPaint);

        c.drawRect(cabinX, cabinY,
                cabinX + cabinW, cabinY + cabinD,
                shaftPaint);

// inner cabin
        c.drawRect(
                cabinX + mm(CABIN_WALL),
                cabinY + mm(CABIN_WALL),
                cabinX + cabinW - mm(CABIN_WALL),
                cabinY + cabinD - mm(CABIN_WALL),
                innerPaint
        );
        // ================= PERSON CAPACITY =================

        String capacityText = "≈ " + persons + " Persons";

        float centerX = cabinX + cabinW / 2f;
        float centerY = cabinY + cabinD / 2f + capacityPaint.getTextSize()/3;

        c.drawText(capacityText, centerX, centerY, capacityPaint);

        // ================= DOOR SYSTEM =================

        int clearOpening = mm(p.clearOpening);

        int halfDoor = clearOpening/2;

        int doorLeft = cx - clearOpening/2;
        int doorRight = cx + clearOpening/2;

        int passagePx = mm(PASSAGE_DEPTH);
        int trackPx = mm(TRACK_THICKNESS);
        int gapPx = mm(LANDING_GAP);
        int landingDoorPx = mm(LANDING_DOOR_THICKNESS);

        int passageTop = cabinY + cabinD;
        int passageBottom = passageTop + passagePx;

        int cabinTrackTop = passageBottom;
        int cabinTrackBottom = cabinTrackTop + trackPx;

        int landingGapTop = cabinTrackBottom;
        int landingGapBottom = landingGapTop + gapPx;

        int landingDoorTop = landingGapBottom;
        int landingDoorBottom = landingDoorTop + landingDoorPx;

        int shaftFaceY = sb - mm(SHAFT_FACE_OFFSET);

        if(landingDoorBottom > shaftFaceY){

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

        c.drawRect(doorLeft,passageTop,doorRight,passageBottom,innerPaint);
        c.drawRect(doorLeft,passageTop,doorRight,passageBottom,shaftPaint);

        int trackLeft = doorLeft - halfDoor;
        int trackRight = doorRight + halfDoor;

        c.drawRect(trackLeft,cabinTrackTop,trackRight,cabinTrackBottom,shaftPaint);

        c.drawRect(doorLeft,landingGapTop,doorRight,landingGapBottom,innerPaint);

        c.drawRect(
                doorLeft-halfDoor,
                landingDoorTop,
                doorLeft,
                landingDoorBottom,
                doorPaint
        );

        c.drawRect(
                doorRight,
                landingDoorTop,
                doorRight+halfDoor,
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

        c.drawText("PLAN VIEW",sx,sy-40,textPaint);

        int dy = sb + 40;

        drawH(c,doorLeft,doorRight,dy,
                "Clear Opening : "+p.clearOpening+" mm");

        dy += 30;

        drawH(c,cabinX,cabinX+cabinW,dy,"Cabin Width : " + cabinWidthMM + " mm");

        dy += 30;

        drawH(c,sx,sr,dy,
                "Shaft Width : "+p.shaftWidth+" mm");


        int dx = sr + 40;

        drawV(c,cabinY,cabinY+cabinD,dx,"Cabin Depth : " + cabinDepthMM + " mm");

        dx += 30;

        drawV(c,sy,sb,dx,
                "Shaft Depth : "+p.shaftDepth+" mm");

    }

    @Override
    protected void onMeasure(int w,int h){

        LiftProject p = ProjectRepository.getInstance().getProject();

        setMeasuredDimension(
                mm(p.shaftWidth) + LEFT + RIGHT,
                mm(p.shaftDepth) + TOP + BOTTOM
        );
    }

    private int mm(int v){
        return (int)(v*SCALE);
    }
    private int pxToMm(int px){
        return (int)(px / SCALE);
    }

    private Paint stroke(int c,int w){
        Paint p = new Paint();
        p.setColor(c);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(w);
        p.setAntiAlias(true);
        return p;
    }

    private Paint fill(int c){
        Paint p = new Paint();
        p.setColor(c);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        return p;
    }

    private Paint text(int s){
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(s);
        p.setAntiAlias(true);
        return p;
    }

    private void drawH(Canvas c,int x1,int x2,int y,String t){

        c.drawLine(x1,y,x2,y,dimPaint);

        c.drawLine(x1,y-8,x1,y+8,dimPaint);
        c.drawLine(x2,y-8,x2,y+8,dimPaint);

        float w = textPaint.measureText(t);

        c.drawText(t,(x1+x2)/2f - w/2f,y-6,textPaint);
    }

    private void drawV(Canvas c,int y1,int y2,int x,String t){

        c.drawLine(x,y1,x,y2,dimPaint);

        c.drawLine(x-8,y1,x+8,y1,dimPaint);
        c.drawLine(x-8,y2,x+8,y2,dimPaint);

        c.save();

        c.rotate(-90,x+20,(y1+y2)/2f);

        float w = textPaint.measureText(t);

        c.drawText(t,x+20 - w/2f,(y1+y2)/2f,textPaint);

        c.restore();
    }
}