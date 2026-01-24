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
