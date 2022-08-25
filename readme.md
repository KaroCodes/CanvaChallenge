Photo mosaic
------------

The goal of this task is to implement the following flow in an android app.
This task should take around 3-5 hours, there is no hard limit,
   focus on getting the best results in this time.

1. A user selects a local image file.
2. The app must:
   * load that image;
   * divide the image into tiles;
   * find the average color for each tile;
   * fetch a tile from the provided server (see below) for that color;
   * composite the results into a photomosaic of the original image;
3. The composited photomosaic should be displayed according to the following
   constraints:
   * tiles should be rendered a complete row at a time (a user should never
      see a row with some completed tiles and some incomplete)
   * the mosaic should be rendered from the top row to the bottom row.
4. The client app should make effective use of parallelism and asynchrony.

The servers directory contains a simple local mosaic tile server. See the
README file in that directory for more information.

## Constraints

You should, in priority order:

 * do not use third-party libraries that directly solve the task,
   this will not tell us anything about you,
   and will not produce adaptable code;
 * use a tile size of 32x32;
 * make the UI work on different screen sizes;
 * use an API level for which source code is available.

You may:

 * choose a minimum API level you want to support
 * make it work with different tile sizes other than 32x32;
 * be as creative as you like with the submission UI
   however, it is not the focus of the task, a minimal UI is fine;
 * use popular libraries like Guava or RxJava if you are familiar with them
   (be prepared to refactor and extend your code in an interview);

## Marking Criteria

Your code should be clear and easy to understand:

 * Avoids unnecessary complexity / over-engineering
 * Brief comments are added where appropriate
 * Broken into logical chunks

Your code should be performant:

 * Gives feedback to the user as soon as possible (perceived performance)
 * Intelligently coordinates dependent asynchronous tasks
 * UI remains responsive

Your code should be testable (but writing tests isn't necessary), for example:

 * Use dependency injection (the design pattern, not a framework or library)
 * Separate presentation logic from business logic.

Have fun!

