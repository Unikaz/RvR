#Robot vs Robot

The goal of this project is to create an environement for battles between robots.
It's inspired by robocode and code combat.

##What's now
Actually, the program is just at its beginning, and many things have to be done before it become usable.
The UI don't permit to make things correctly and should be one of the next step.
Robots are saved in bots/RobotName.java and will be compile during runtime when you add them from the list. So now you can create and modify robots without recompile everything or relaunch the game.
The robots' editor isn't ready yet, it's just a base for work ^^
You can't create wall now, but you can create some OtherEntities like the HealthEntity.

##Actual robots capacities
- Actuators
    - Change speed
    - Change orientation
    - Fire (bullet)
    - Attack
    - Change view field (with fire's precision)

- Sensors
    - Contact with entities
    - Contact with map border
    - View entities in view field


About the dynamically loaded robots :
The bots's code have to be saved in bots/ folder that isn't configurable for now, and the compiled classes gonna be store in a bots_compiled/Robots/, next to the bots/ folder.
If the code can't compile, it will be write in the console. The code have to be a class which extends Robot, and redefine the method public void run();
A documentation will come we mode details and a list of the method inherited from Robot that you can use. For now, look at the code from R1, R2 and R3, it's pretty simple ^^
