# TutorialView-Android
Code used in [Yazzy (Fake conversations)](https://play.google.com/store/apps/details?id=com.cerminara.yazzy) for the initial tutorial
## How to use
#### 1. Create a new TutorialView:
```TutorialView tutorialView = new TutorialView(this);```
#### 2. Add a series of steps
```java
tutorialView.addStep(
  targetView1, // The view to highlight
  true, // specifies if the position measurement should be related to the window (if true) or to the parent (if false)
  4, // The scale factor of the circle in relation to the view sizes
  "Press this button to save the image" // The text of the tutorial step
);
// ...
tutorialView.addStep(targetViewN, true, 4, "Press this button to edit the image");
```
#### 3. Add an optional OnFinishedListener
```java
tutorialView.setOnFinishedListener(new OverlayView.OnFinishedListener() {
  @Override
  public void onFinished() {
    preferences.edit().putBoolean(FIRST_TIME_OVERLAY, false).apply();
    tutorialView.setVisibility(View.GONE);
  }
});
```
