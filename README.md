# Splendor
Splendor is a famous borad game around the world. If you don't know this game, check it out [here](https://en.wikipedia.org/wiki/Splendor_(game)).

This repository is a console version of this board game, and it is implemented by Java. Although it supports multiplayer game, but since there is no GUI, it is not user friendly at all.
The goal for this repository is to train an Splendor AI. Currently it is still under developing. A few APIs will be exposed so that everyone could leverage this repository to train their own AI.

# Design Your Own AI
Everyone is welcomed to implement his/her own AI for this game. To do that, simple create a class extends from Player.java. To extend from this class, you'll need to implement the following methods.

* `public abstract ActionAndResponse askForAction(List<Player> opponents,Map<Color, Integer> remainingTokens,Map<CardsPosition, Card> visibleCards,List<Noble> nobles)`  
  This method will be invoked when its your turn. 
  For the parameters, 
  `opponents` is a list where stores all other players;
  `remainingTokens` stores remaining tokens in the Bank;
  `visibleCards` stores all cards that are visible for players but please noted there could null in the Map which means there is no card at that position;
  `nobles` are all remaining nobles on the table.
 
* `public abstract int pickNoble(Map<Integer, Noble> nobles)`  
  This method will be invoked when you are eligible to pick more than one nobles, you should return an index (the key of the parameter) to choose which one you want to take.
 
* `public abstract Map<Color, Integer> askToReturnTokens()`  
  This method will be invoked when you have more than 10 tokens and you need to return some to keep the total count below 10.
