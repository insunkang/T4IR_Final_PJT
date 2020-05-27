package multi.android.infortainmentw.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

class Oil extends View {
    public Oil(Context context) {
        super(context);
    }

    public Oil(Context context, AttributeSet att) {
        super(context, att);
    }

    public Oil(Context context, AttributeSet att, int a) {
        super(context, att, a);
    }

    int cx = 443;
    int cy = 208;
    int dx = cx + 50;
    int dy = cy;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5f);
        canvas.drawLine(cx, cy, dx, dy, paint);
    }
}