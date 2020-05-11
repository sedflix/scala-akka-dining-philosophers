# DIC: P3

Siddharth Yadav | 2016268

# Setup

### Option 1: IntelliJ IDEA

I've used IntelliJ IDEA to run all of my code. My code can be simply imported using `build.sbt` present in the content root. I've provided my `.idea` configuration.

### Option 2: sbt

Open `sbt` console in the content root. And you can run the following command for each part in the assignment:

1. `runMain hello_world.HelloWorld`
2. `runMain sorting.SortIntegerArray`
3. `runMain dining.philosopher.Main`

# Questions

## Part 1: Hello World!

### REPL: Screenshot

![images/Untitled.png](images/Untitled.png)

"Hello World!" in Scala REPL 

### Object: Screenshot

The file is`src/main/scala/hello_world/HelloWorld.scala`

Try: `runMain hello_world.HelloWorld`

![images/Untitled%201.png](images/Untitled%201.png)

"Hello World!" in scala object

## Part 2: Quick Sort!

### Usage

The file is `src/main/scala/sorting/SortIntegerArray.scala`

Try: `runMain sorting.SortIntegerArray`

### Code

My Quick Sort Code is quite sort and I've written here for reference.

```scala
  def quickSort(array: Array[Int]): Array[Int] = {
    if (array.length <= 1) {
      array
    } else {
      val pivot = array(array.length / 2)

      quickSort(array.filter(pivot > _)).
        appendedAll(array.filter(pivot == _)).
        appendedAll(quickSort(array.filter(pivot < _)))
   }
```

### Screenshots

![images/Untitled%202.png](images/Untitled%202.png)

Screenshot my my working sort code

## Part 3: Dining Philosopher with Akka

### Usage

The concerned files are present in `src/main/scala/dining/philosopher`

Try: `runMain dining.philosopher.Main.`

If you want more detailed information about how actors are receiving and sending messages, it can be done by changing `src/main/resources/application.conf`. Turn `receive = off` on line 6 to `receive = on`  &  `loglevel = "INFO"` on line 2 to `loglevel = "DEBUG"` in `application.conf.` 

### Description

*The code is very well annotated and documented and description can be found in the code it.*

I've used **akkaVersion = "2.6.5".**

### Screenshots

![images/Untitled%203.png](images/Untitled%203.png)

Example execute of the code

# References

Besides what was mentioned on course project and reference links, I've used the following links for reference

[It's Actors All The Way Down](http://www.dalnefre.com/wp/2010/08/dining-philosophers-in-humus/)

[42ways/akka-dining-philosophers](https://github.com/42ways/akka-dining-philosophers/)