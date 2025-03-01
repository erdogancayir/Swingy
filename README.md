# Swingy - A Text-Based RPG with Java Swing

![Swingy RPG](https://example.com/banner.png) <!-- Replace with actual banner if available -->

## 📖 Overview

**Swingy** is a text-based RPG built using Java and the Swing framework. It is the second project in the Java series at **42** and focuses on GUI programming while adhering to **best software design principles**.

The game allows players to create and manage heroes, navigate a dynamically generated map, and battle enemies while progressing through levels. Swingy supports **both console and GUI modes**.

## 🚀 Features

- 🏹 **Turn-based RPG Gameplay** with hero progression.
- 🎭 **Multiple Hero Classes** with unique stats.
- 🗺️ **Dynamically Generated Map** based on hero level.
- ⚔️ **Combat System** with fight and run mechanics.
- 💎 **Artifacts System** (Weapons, Armor, Helmets) affecting hero stats.
- 🔀 **Console and GUI Modes** switchable at launch.
- 💾 **Hero Persistence** via a text file.
- ✅ **Annotation-Based Input Validation** with `javax.validation`.

## 🎮 Gameplay

1. **Hero Management**  
   - Create a new hero or select an existing one.
   - View hero attributes such as attack, defense, hit points, experience, and level.

2. **Navigation**  
   - Move in four directions: **North, East, South, and West**.
   - The map size is determined by:  
     `(level-1) * 5 + 10 - (level%2)`

3. **Encounters with Villains**  
   - **Fight**: Engage in battle based on hero and villain stats.
   - **Run**: 50% chance of escaping; failure forces combat.

4. **Leveling Up**  
   - Experience formula: `level * 1000 + (level - 1)² * 450`
   - Example experience requirements:
     - **Level 1:** 1000 XP
     - **Level 2:** 2450 XP
     - **Level 3:** 4800 XP
     - **Level 4:** 8050 XP

5. **Game Objective**  
   - **Win by reaching the map border**.
   - **Lose if the hero is defeated** in combat.

## 📌 Installation & Setup

### Prerequisites
- **Java LTS version**
- **Maven**
- **IDE with Maven support** (IntelliJ IDEA, Eclipse, VS Code)

### Build & Run

Clone the repository:
```sh
git clone https://github.com/yourusername/swingy.git
cd swingy
