Yashruko 2: (Subname tbd)

Welcome to Yashrukor, this is an online multiplayer cabapable RTS game developed using only native Java libraries.

Starting the Game
	1. Run the Server.java file located in the network package, like you would a normal Java program.
		- Server should be a java window with the "Start" button. DON'T PRESS THE BUTTON YET.
	2. Take note of the ip address of the computer Server.java is running on.
	3. On every computer that wants to play the game (max of 4 players) run Client.java like any Java program.
		- The Server computer can also run Client.
	4. Running client should launch a Java window that allows you to pick your screen and enter an IP Address.
	   For the IP Address enter the IP of computer Server.java is runnning on. Then hit connect.
	5. After all players have connected, which you can tell on Server, which shows all the connected players, press
	   the "Start" button in the Server window.
	6. The game has started enjoy the game.

Basic Controls:
	- Select units or buildings by drawing a selection box around them, which is done by dragging your mouse.
		- If units and buildings are in the selection box, units are prioritized.
	- Move units by selecting them and right clicking somewhere on the screen or on the minimap (map in bottom right corner).
	- Attack Move units, which means they will move until they see an enemy unit when they will begin attacking the enemy unit,
	  by holding A while cursor is at the location you want to attack move to and the units are selected.
	- You can build buildings by pressing 1, 2, 3, 4, 5, 6, or 7. 
		1. Farm
		2. Quarry
		3. Lumbermill
		4. Tower
		5. Barracks
		6. Range
		7. Hospital

Resources
	- Building a unit or building requires resources. The amount of required resources can be seen in the UI by default for each building,
	  and for units by selecting the buildings that build them.
	- The amount of resources you have are listed at the top left.

	Resource          Building that Gather Resource
	--------          -----------------------------
	Gold			  Base
	Wood              Lumbermill
	Stone			  Quarry
	Food              Farm

Buidlings
	- There are 8 buildings in Yashrukor
		- Base: 
			-The default buidling which every player starts with, located in the players corner.
			- Collects gold
		- Farm
			- Collects food
		- Quarry
			- Collects stone
		- Lumbermill
			- Collects wood
		- Tower
			- Defensive building with high health
		- Barracks
			- Builds Warrior and Knight
		- Range
			- Builds Archer and Crossbowman
		- Hospital 
			- Builds Medic and Shaman

Units
	- Warrior
		- Basic level melee unit
	- Knight
		- Advanced level melee unit, higher health and attack than Warrior
	- Archer
		- Basic level ranged unit
	- Crossbowman
		- Advanced level ranged unit, higher health and attack than Archer
	- Medic
		- Basic level healing unit, heals one friendly unit at a time.
	- Shaman
		- Advanced level healing unit, heals friendly unit in an Area
		  of Effect around them.

Win Condition
	- Win the game by defeating all enemy units.
	- Not all buildings just units.

Thank You for supporting Yashrukor, good luck and have fun. 
