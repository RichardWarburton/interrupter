Interrupter
===========

Problem
-------

`java.lang.Thread.stop()` is deprecated and unreliable.  It is consequently impossible to guarantee that a thread will stop.

Approach
--------

Implement a bytecode rewriter than reliably interrupts a `Java` program.

Useful For
----------

People who have a some bytecode that is coming from an unreliable source and isn't guaranteed to terminate, but must do so in order for the program to behave correctly.

Caveats
-------

The performance properties of this approach haven't been tested.  Aside from the direct overhead of introducing regular method calls, this makes methods larger.  If a method becomes too large, then the JVM won't inline it.  I have undertaken no empirical study of the performance properties, so you should use at your own risk.

Example
-------

The maven test directory contains an example of how to use the `InterruptingClassLoader`, along with an example non-terminating program that is interrupted when the test is run.

