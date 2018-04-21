# Code Test

## Warm up
They will call you to a coding session, where you will do live coding in https://coderpad.io.

First you will be presented a code snippet ([Warm Up](warm_up/Solution.java)) that in the comments tells you to print a pyramid to the console.
The pyramid shall have a base consisting of n number of asterisks with a space in between.
The important thing here is to explain the formula you want to use to calculate the leading spaces 
and the position of the first asterisk of each row in the pyramid.

To print the pyramid we need to know 2 params base with and leading spaces, the base width is n * 2 -1 
then we can conclude that the number of leading spaces is (base - 1) / 2 or n - 1. 

So we have to loop from 1 to n and print n - i spaces and print n asterisks.

## Code review
You will get some code ([Code Review](code_review/Solution.java)) that you will be asked to review and fix.
It consists of 5 classes Solution, Item, Discount, Cart and Util.

The main method in Solution is calling a junit test. First you shuld check and calculate the correct expected amount, 
its wrong in the test and lets a wrong calculation throw.
The correct amount is 30.60 and is calculated (59-25)*.9.

Now e have to fix the calculation and the discount is never deducted from the item. 
applyDiscount must be called for every item price while adding the total amount.

Next thing is to remove the Util class and move the methods in to the relevant classes.
THe discount class is ugly because it deal with a percentage and a fixed discount with, 
create a discount interface with a applyDiscount method. 
PercentageDiscount and AmountDiscount implements the discount interface and you need to split the code 
from applyDiscount from Util in to the right classes. 
Add a getter for price in Item that subtracts the discount if its not null from the price and return it.

calculateAmountToPayForCart goes in the Cart class and can be renamed to getAmount, 
make sure that getPrice is called instead of prce instance when the total amount is calculated. 
And subtracts the discount if its not null from the total price and return it.

Pitfalls: all instance variables are called direct without getters, I was told it was simpler in coderpad.
All amounts and prices are BIgDecimals and are immutable, when you do calculations don't forget to replace the result.
ex. amount = amount.add(item.getPrice)

The solution might look like this [Solution.java](code_review/solved/Solution.java)


Good luck
