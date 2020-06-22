# bussidn note :

Hello this is my version of the kata?

I've tried a functional approach with :
  - high expressiveness at highest business level
  - logs handled by a monadic approach
  - results (for tests and deployment) coded like sealed types with pattern matching (match method)
  - decoupling of business and logs
  
 Below is the orignial README

# Untangled Conditionals Kata

[![Build Status](https://travis-ci.org/tomphp/untangled-conditionals-kata.svg?branch=master)](https://travis-ci.org/tomphp/untangled-conditionals-kata)

This repository contains a simple coding kata.

Category: **refactoring**<br>
Skill Level: **beginner**<br>
Time: **~30 mins**

This repository contains the starting code.
The logic is contained in a single method, `Pipeline.run()`.
This method contains a number of embedded `if` statements.
Your goal is to refactor the code to a more understandable and desireable state.

Remember, keep the tests passing while you refactor!

## Approach

Below you can find some resources demonstrating the approach that this kata was designed to teach.
However, I recommend you try the kata yourself first, and then review the resources.

* Blog Post: [Refactoring — Untangling Conditionals](https://cloudnative.ly/refactoring-untangling-conditionals-cc5693b8ec3c).
* Video: [![Untangled Conditionals Kata](https://img.youtube.com/vi/NWgY-0Qu4S4/0.jpg)](http://www.youtube.com/watch?v=NWgY-0Qu4S4)
