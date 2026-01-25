package com.dyl.drawyourlift.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;

import com.dyl.drawyourlift.drawing.elevation.ElevationView;
import com.dyl.drawyourlift.drawing.front.FrontView;
import com.dyl.drawyourlift.drawing.plan.PlanView;

import java.io.File;
import java.io.FileOutputStream;

public class PdfGenerator {

    public static File generateMultiPagePdf(Context context, String projectName) throws Exception {

        PdfDocument pdfDocument = new PdfDocument();

        // Page 1: Elevation
        createPage(pdfDocument, new ElevationView(context), 1);

        // Page 2: Front View
        createPage(pdfDocument, new FrontView(context), 2);

        // Page 3: Plan View
        createPage(pdfDocument, new PlanView(context), 3);

        // Save file
        File downloadsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
        );

        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        File file = new File(
                downloadsDir,
                projectName + "_Lift_Drawing.pdf"
        );

        FileOutputStream fos = new FileOutputStream(file);
        pdfDocument.writeTo(fos);

        pdfDocument.close();
        fos.close();

        return file;
    }
    private static int drawHeader(
            Canvas canvas,
            String projectName,
            String liftType
    ) {
        Paint titlePaint = new Paint();
        titlePaint.setTextSize(40);
        titlePaint.setFakeBoldText(true);

        Paint infoPaint = new Paint();
        infoPaint.setTextSize(24);

        int startX = 40;
        int startY = 60;

        canvas.drawText("DRAW YOUR LIFT (DYL)", startX, startY, titlePaint);

        startY += 40;
        canvas.drawText("Project: " + projectName, startX, startY, infoPaint);

        canvas.drawText(
                "Date: " + java.time.LocalDate.now(),
                startX + 500,
                startY,
                infoPaint
        );

        startY += 30;
        canvas.drawText("Lift Type: " + liftType, startX, startY, infoPaint);

        // Bottom line
        startY += 20;
        canvas.drawLine(startX, startY, canvas.getWidth() - 40, startY, infoPaint);

        return startY + 40; // return Y where drawings should start
    }
    public static File generateSinglePagePdf(
            Context context,
            String projectName,
            String liftType
    ) throws Exception {

        PdfDocument pdfDocument = new PdfDocument();

        // Prepare views
        ElevationView elevationView = new ElevationView(context);
        FrontView frontView = new FrontView(context);
        PlanView planView = new PlanView(context);

        // Measure all views
        measureView(elevationView);
        measureView(frontView);
        measureView(planView);

        int pageWidth = Math.max(
                Math.max(elevationView.getMeasuredWidth(), frontView.getMeasuredWidth()),
                planView.getMeasuredWidth()
        ) + 80;

        int pageHeight =
                elevationView.getMeasuredHeight()
                        + frontView.getMeasuredHeight()
                        + planView.getMeasuredHeight()
                        + 300; // header + spacing

        PdfDocument.Page page = pdfDocument.startPage(
                new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        );

        Canvas canvas = page.getCanvas();

        // Header
        int currentY = drawHeader(canvas, projectName, liftType);

        // Elevation
        canvas.translate(40, currentY);
        elevationView.draw(canvas);
        canvas.translate(-40, -currentY);

        currentY += elevationView.getMeasuredHeight() + 40;

        // Front View
        canvas.translate(40, currentY);
        frontView.draw(canvas);
        canvas.translate(-40, -currentY);

        currentY += frontView.getMeasuredHeight() + 40;

        // Plan View
        canvas.translate(40, currentY);
        planView.draw(canvas);
        canvas.translate(-40, -currentY);

        pdfDocument.finishPage(page);



        // Save
        File downloadsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
        );

        File file = new File(
                downloadsDir,
                projectName + "_Single_Page_Lift_Drawing.pdf"
        );

        FileOutputStream fos = new FileOutputStream(file);
        pdfDocument.writeTo(fos);

        pdfDocument.close();
        fos.close();

        return file;
    }
    private static void measureView(View view) {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }




    // 🔹 CORE METHOD — DO NOT MODIFY
    private static void createPage(
            PdfDocument pdfDocument,
            View view,
            int pageNumber
    ) {

        // FORCE MEASURE
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        // FORCE LAYOUT
        view.layout(
                0,
                0,
                view.getMeasuredWidth(),
                view.getMeasuredHeight()
        );

        PdfDocument.Page page = pdfDocument.startPage(
                new PdfDocument.PageInfo.Builder(
                        view.getMeasuredWidth(),
                        view.getMeasuredHeight(),
                        pageNumber
                ).create()
        );

        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        pdfDocument.finishPage(page);
    }
}
