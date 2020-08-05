# ClockSkin DigitalImageBuilder
 
##### 1. Declare builder in watchface engine
```java
private DigitalImageBuilder digitalImageBuilder = new DigitalImageBuilder();
```

##### 2. Clear builder image set before draw and fill with new images
This example from ARRAY_HOUR_MINUTE type
```java
digitalImageBuilder.clear();
digitalImageBuilder.addSlice(new DigitalImageBuilder.DigitalObject(
        clockEngineLayer.getDrawableArrays().get(TimeUnit.hour / 10),
        DigitalImageBuilder.ItemType.SLICE,
        DigitalImageBuilder.DividerVisibility.VISIBLE
));
digitalImageBuilder.addSlice(new DigitalImageBuilder.DigitalObject(
        clockEngineLayer.getDrawableArrays().get(TimeUnit.hour % 10),
        DigitalImageBuilder.ItemType.SLICE,
        DigitalImageBuilder.DividerVisibility.VISIBLE
));
digitalImageBuilder.addSlice(new DigitalImageBuilder.DigitalObject(
        clockEngineLayer.getDrawableArrays().get(10),
        DigitalImageBuilder.ItemType.DIVIDER,
        (TimeUnit.second % 2 == 0) ? DigitalImageBuilder.DividerVisibility.VISIBLE : DigitalImageBuilder.DividerVisibility.INVISIBLE
));
digitalImageBuilder.addSlice(new DigitalImageBuilder.DigitalObject(
        clockEngineLayer.getDrawableArrays().get(TimeUnit.minute / 10),
        DigitalImageBuilder.ItemType.SLICE,
        DigitalImageBuilder.DividerVisibility.VISIBLE
));
digitalImageBuilder.addSlice(new DigitalImageBuilder.DigitalObject(
        clockEngineLayer.getDrawableArrays().get(TimeUnit.minute % 10),
        DigitalImageBuilder.ItemType.SLICE,
        DigitalImageBuilder.DividerVisibility.VISIBLE
));
digitalImageBuilder.draw(canvas, clockEngineLayer.getPositionX(), clockEngineLayer.getPositionY());                                          
```
