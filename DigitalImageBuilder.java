import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class DigitalImageBuilder {
    /**
     * Static scale variable for calculating width and height of items
     * 400f - its constant initial value for engine size
     */
    private static float SCALE = 400f / Resources.getSystem().getDisplayMetrics().widthPixels;
    /**
     * List of Digital objects for drawing final image
     */
    private List<DigitalObject> digitalObjectList;

    /**
     * Empty constructor
     */
    public DigitalImageBuilder() {
        this.digitalObjectList = new ArrayList<>();
    }

    /**
     * Using this method you can add Digital object to list
     * @param digitalObject Configured object
     */
    public void addSlice(DigitalObject digitalObject) {
        this.digitalObjectList.add(digitalObject);
    }

    /**
     * Call this function when you need draw new digital images set! Warning do not allocate new builder object! Use this method for performance!
     */
    public void clear(){
        this.digitalObjectList.clear();
    }


    /**
     * This method uses provided digital objects to build one configured bitmap
     * @return result of building
     */
    private Bitmap buildImage(){
        //Calculate total width for build image
        int width = calculateSlicesWidth() + calculateDividersWidth();
        //Calculate height for build image (Using second item in list (in 100% his is slice)), height are same for all slices
        int height = getItemHeight(digitalObjectList.get(1).getResource());
        //Create new bitmap using calculated sizes
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //Create canvas for drawing to bitmap
        Canvas canvas = new Canvas(bitmap);
        //This is x position for each slice or divider, starting value is 0. 
        int drawPosX = 0;
        //Loop drawing
        for(DigitalObject digitalObject:digitalObjectList){
            //TODO add instance check for other types images
            //Allocate drawable from digital object
            Drawable drawable = (Drawable) digitalObject.getResource();
            //Calculate width and height for this drawable
            int drawableWidth = getItemWidth(drawable);
            int drawableHeight = getItemHeight(drawable);
            //Check type of object
            switch (digitalObject.getType()){
                //If this is a divider we need know his visibility (some arrays have blinking dividers)
                case DIVIDER:
                    switch (digitalObject.getDividerVisibility()){
                        //If divider invisible just add margin to x position and not draw divider
                        case INVISIBLE:
                            drawPosX += drawableWidth;
                            break;
                        //If divider visible add margin to x position and draw divider
                        case VISIBLE:
                            drawable.setBounds(drawPosX, 0, drawPosX + drawableWidth, drawableHeight);
                            drawable.draw(canvas);
                            drawPosX += drawableWidth;
                            break;
                        //Do nothing if need hide divider (For example for temperature array, draw minus in start when temperature < 0 or not)
                        case GONE:
                            //TODO do not draw and not add margin
                            break;
                    }
                    break;
                //If its slice just draw it
                case SLICE:
                    drawable.setBounds(drawPosX, 0, drawPosX + drawableWidth, drawableHeight);
                    drawable.draw(canvas);
                    drawPosX += drawableWidth;
                    break;
            }
        }
        //Return result of drawing
        return bitmap;
    }

    /**
     * Draw result image in specified position
     * @param canvas Watchface view canvas
     * @param positionX scaled position x
     * @param positionY scaled position y
     */
    public void draw(Canvas canvas, int positionX, int positionY) {
        Bitmap result = buildImage();
        canvas.drawBitmap(result, Math.round(positionX - (result.getWidth()/2f)), Math.round(positionY - (result.getHeight()/2f)), null);
    }

    /**
     * Calculate width only slices
     * @return total width off all slices
     */
    private int calculateSlicesWidth() {
        int slicesWidth = 0;
        for (DigitalObject digitalObject : digitalObjectList) {
            if (digitalObject.getType() == ItemType.SLICE) {
                slicesWidth += getItemWidth(digitalObject.getResource());
            }
        }
        return slicesWidth;
    }
    /**
     * Calculate width only dividers
     * @return total width off all dividers
     */
    private int calculateDividersWidth() {
        int dividersWidth = 0;
        for (DigitalObject digitalObject : digitalObjectList) {
            if (digitalObject.getType() == ItemType.DIVIDER) {
                dividersWidth += getItemWidth(digitalObject.getResource());
            }
        }
        return dividersWidth;
    }


    /**
     * Calculate scaled image width
     * @param item this can be drawable or input stream (not implemented yet)
     * @return scaled width
     */
    private int getItemWidth(Object item) {
        if (item instanceof Drawable) {
            Drawable drawable = (Drawable) item;
            return Math.round(drawable.getIntrinsicWidth() * SCALE);
        } else {
            return 0;
        }
    }
    
    /**
     * Calculate scaled image height
     * @param item this can be drawable or input stream (not implemented yet)
     * @return scaled height
     */
    private int getItemHeight(Object item) {
        if (item instanceof Drawable) {
            Drawable drawable = (Drawable) item;
            return Math.round(drawable.getIntrinsicHeight() * SCALE);
        } else {
            return 0;
        }
    }


    /**
     * This enum can help detect object type
     */
    public enum ItemType {
        SLICE,
        DIVIDER
    }

    /**
     * This enum provides divider visibility for draw
     */
    public enum DividerVisibility {
        VISIBLE,
        INVISIBLE,
        GONE
    }

    /**
     * Digital object uses for building and drawing 
     */
    public static class DigitalObject {
        Object resource;
        ItemType type;
        DividerVisibility dividerVisibility;

        /**
         * Create new digital object
         * @param resource This can be drawable or input stream
         * @param type type of object slice or divider
         * @param dividerVisibility visibility for all provided dividers
         */
        public DigitalObject(Object resource, ItemType type, DividerVisibility dividerVisibility) {
            this.resource = resource;
            this.type = type;
            this.dividerVisibility = dividerVisibility;
        }

        public Object getResource() {
            return resource;
        }

        public ItemType getType() {
            return type;
        }

        public DividerVisibility getDividerVisibility() {
            return dividerVisibility;
        }
    }
}
