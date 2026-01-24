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

        // -------- PAGE 1: ELEVATION --------
        ElevationView elevationView = new ElevationView(context);
        elevationView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        PdfDocument.Page elevationPage = pdfDocument.startPage(
                new PdfDocument.PageInfo.Builder(
                        elevationView.getMeasuredWidth(),
                        elevationView.getMeasuredHeight(),
                        1
                ).create()
        );

        Canvas elevationCanvas = elevationPage.getCanvas();
        elevationView.draw(elevationCanvas);
        pdfDocument.finishPage(elevationPage);

        // -------- PAGE 2: FRONT VIEW --------
        FrontView frontView = new FrontView(context);
        PdfDocument.Page frontPage = pdfDocument.startPage(
                new PdfDocument.PageInfo.Builder(
                        frontView.getMeasuredWidth(),
                        frontView.getMeasuredHeight(),
                        2
                ).create()
        );

        Canvas frontCanvas = frontPage.getCanvas();
        frontView.draw(frontCanvas);
        pdfDocument.finishPage(frontPage);

        // -------- PAGE 3: PLAN VIEW --------
        PlanView planView = new PlanView(context);
        PdfDocument.Page planPage = pdfDocument.startPage(
                new PdfDocument.PageInfo.Builder(
                        planView.getMeasuredWidth(),
                        planView.getMeasuredHeight(),
                        3
                ).create()
        );

        Canvas planCanvas = planPage.getCanvas();
        planView.draw(planCanvas);
        pdfDocument.finishPage(planPage);

        // -------- SAVE FILE --------
        File downloadsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
        );

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
}
