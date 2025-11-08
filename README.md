# CSC508Pacman
# Names: Parshana Sekhon, Mio Nakagawa, Sadie Fisher

## MQTT-Based Multi-User Pac-Man
Creates a Java Desktop Application that allows users to move a colored square using their keyboard and displays other players' squares. 
Each player's square location gets published to a Mosquitto broker using the topic format:
csc509/multiverse/id/. The user subscribes to receive all messages (square location of other players) with the topic prefix:
csc509/multiverse/.

## Running the Application
### 1. Clone
Clone the repository:

```bash
git clone https://github.com/Parshana007/CSC508Pacman.git
```
### 2. Run
Each instance represents a player/square.

There are two methods to run the program:
1. If you start the program without an argument/with the below command, it will create a unique default ID by appending the current time in milliseconds (Ex default1390494)
```bash
mvn exec:java -Dexec.mainClass="Main"
mvn exec:java -Dexec.mainClass="Main"
...
```

2. If you start the program with an argument/with the below command, it will use that as the player/square’s ID,

```bash
mvn exec:java -Dexec.mainClass="Main" -Dexec.args="player1"
mvn exec:java -Dexec.mainClass="Main" -Dexec.args="player2"
...
```

## Controls

| Key | Action |
|-----|---------|
| Up Arrow | Move Up |
| Down Arrow | Move Down |
| Left Arrow | Move Left |
| Right Arrow | Move Right |

## Structure

| Class                 | Responsibility                                                                                                                         |
|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| `Main.java`           | Entry point and main application to run the square PacMan game                                                                         |
| `Blackboard.java`     | Blackboard is a singleton that holds the states of all squares in the game                                                             |
| `WorldPanel.java`     | WorldPanel handles the drawing of squares, updating the square states and manages key events (up, down, left, right) of a given square |
| `Square.java`         | A square in a grid with a unique id, location, and color                                                                               |
| `MQTTPublisher.java`  | MQTT publisher that sends messages to a TOPIC. The broker is test.mosquitto.org and the TOPIC is csc509/multiverse/                    |
| `MQTTSubscriber.java` | MQTT subscriber that listens to a TOPIC. The BROKER is test.mosquitto.org and the TOPIC is csc509/multiverse/                          |

## GUI 
<img width="1506" height="889" alt="Screenshot 2025-11-07 at 4 55 51 PM" src="https://github.com/user-attachments/assets/7f7253f7-1313-46fc-ab3f-ea461b0cc50e" />

