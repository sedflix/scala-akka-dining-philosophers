# Dining Philosophers in Akka

## Setup

### Option 1: IntelliJ IDEA

I've used IntelliJ IDEA to run all of my code. My code can be simply imported using `build.sbt` present in the content root. I've provided my `.idea` configuration.

### Option 2: sbt

Open `sbt` console in the content root. And you can run the following command: `runMain dining.philosopher.Main`

## Usage

The concerned files are present in `src/main/scala/dining/philosopher`

Try: `runMain dining.philosopher.Main.`

If you want more detailed information about how actors are receiving and sending messages, it can be done by changing `src/main/resources/application.conf`. Turn `receive = off` on line 6 to `receive = on`  &  `loglevel = "INFO"` on line 2 to `loglevel = "DEBUG"` in `application.conf.` 

## Description

*The code is very well annotated and documented and description can be found in the code it.*

![images/Untitled%203.png](images/Untitled%203.png)

Example execution of the code.

# References

I've used the following links for references:

[It's Actors All The Way Down](http://www.dalnefre.com/wp/2010/08/dining-philosophers-in-humus/)

[42ways/akka-dining-philosophers](https://github.com/42ways/akka-dining-philosophers/)
