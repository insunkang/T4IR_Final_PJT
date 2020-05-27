package multi.android.infortainmentw.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

class Seat extends View {
    public Seat(Context context) {
        super(context);
    }

    public Seat(Context context, AttributeSet att) {
        super(context, att);
    }

    public Seat(Context context, AttributeSet att, int a) {
        super(context, att, a);
    }

    int cx = 695;
    int cy = 190;
    int dx = cx + (int) (-90 * (Math.cos((-60)* 3.14 / 100d)));
    int dy = cy + (int) (90 * (Math.sin((-60)* 3.14 / 100d)));

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(13,179,229));
        paint.setStrokeWidth(5f);
        canvas.drawLine(cx, cy, dx, dy, paint);
    }
}