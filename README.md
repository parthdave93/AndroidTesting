# AndroidTesting

Objective:
Discuss the types of Android testing and test cases that we may have in real world application.

First, There are two types of testing that can be done:
1. Junit or unit tests, where we can test the logic behind the application
2. Integration test, where we test the UI of the application

Test cases that we may have in real world application inside JUnit or unit tests:
1. Test if the function call has been made
2. Test if the parameters are what we are expecting from the method call
3. Count number of times the method has been called
4. Spy method calls

Test cases that we may have in real world application inside Integration test:
We can use espresso and other libs based on the requirements
1. Check if the error or validation has been succeed in any given UI
2. Check if test/element is present, such as on button click or on tap of add to cart, is cart count changed or not
3. Tap on info window of map marker bubble
4. Check if deeplinks opens correct UI or not

All this test cases has been done one by one on different branches.