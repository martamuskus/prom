# prom

# Usage

Running the app: java -jar /home/…/app.jar /home/…/orders.json /home/…/paymentmethods.json

Running tests: mvn test

Build: make 

# File structure

|-src

   |-main
   
	    |-java
     
	       |- ... [project files]	
	
   |-test
   
	    |-java
     
	       |- ... [tests]

|-target

       |-classes
       
		            |- ... [generated .class files] 
	      
       |-test-classes
       
		            |- ... [generated .class test files]
	      
       -app-1.0.jar
       
       -app-1.0-jar-with-dependencies.jar
				 		
-Makefile

-pom.xml

# Notes

Folder contains both normal .jar [app-1.0.jar] and fat jar with dependencies [app-1.0-jar-with-dependencies.jar]

# Task Overview

The goal is to develop an algorithm that, given a list of orders, available promotions, and the customer’s wallet (containing payment methods, limits, and discounts), selects the optimal payment method for each order to maximize the total discount while meeting all constraints. Payments should be selected so that all orders are fully paid. The algorithm should minimize card payments, preferring to use points if it does not reduce the applicable discount.

# Rules

Each order is assigned a selected subset of applicable promotions related to the payment method.

If the entire order is paid with a card from a bank with which we have a contract, a percentage discount defined for the given payment method is applied.

If the customer pays at least 10% of the order value (before discount) using loyalty points, the store applies an additional 10% discount to the entire order.

If the entire order is paid with loyalty points, the discount defined for the "POINTS" method should be applied instead of the 10% discount for partial payment with points.
