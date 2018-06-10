package eyresapps.com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import eyresapps.com.wct.R;

public class BitmapGenerator {

    public Bitmap getMarkerBitmapFromView(int mapColour, View v, Context context) {
        CardView cardView = v.findViewById(R.id.custom_marker_view);
        TextView textView = v.findViewById(R.id.txtCrimeCount);
        textView.setText(Integer.toString(mapColour));

        if (mapColour <= 2) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color1));
        } else if (mapColour > 2 && mapColour <= 4) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color2));
        } else if (mapColour > 4 && mapColour <= 6) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color3));
        } else if (mapColour > 6 && mapColour <= 8) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color4));
        } else if (mapColour > 8 && mapColour <= 10) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color5));
        } else if (mapColour > 12 && mapColour <= 14) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color6));
        } else if (mapColour > 14 && mapColour <= 16) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color7));
        } else if (mapColour > 16 && mapColour <= 18) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color8));
        } else if (mapColour > 18 && mapColour <= 20) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color9));
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color10));
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        v.setLayoutParams(new ActionMenuView.LayoutParams(ActionMenuView.LayoutParams.WRAP_CONTENT, ActionMenuView.LayoutParams.WRAP_CONTENT));
        v.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        v.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        v.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);

        return bitmap;
    }
}
