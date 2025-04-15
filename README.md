<h1 align="center">🔌 Projet Enerdis – Communication Modbus RTU via RS485 


![Java](https://img.shields.io/badge/Java-FFEB3B?style=for-the-badge&logo=java&logoColor=black)
![Modbus RTU](https://img.shields.io/badge/Modbus%20RTU-Protocol-blue?style=for-the-badge)
![RS485](https://img.shields.io/badge/RS485-Serial-green?style=for-the-badge)

## 🎯 Objectif

Ce projet a pour but de mettre en place une communication série avec un **capteur Enerdis** via le protocole **Modbus RTU** sur une liaison **RS485**.  
L’objectif principal est de pouvoir **interroger le capteur** pour récupérer des données (tension, intensité, puissance), les **décoder** puis les **envoyer vers un cloud**.  
Ces données sont ensuite **affichées sur un site web**, accompagnées de la **position GPS du capteur**, représentée sur une **carte interactive**.

---
---

## ⚙️ Matériel utilisé

-  **Capteur** : EM111,
-  **Ordinateur** : Poste de développement,
-  **Câblage** : ModBus RS485 via USB,
-  **Multiprise** : Simule une installation électrique.
---

## 🛠️ Outils & Langages

-  **Langage** : Java,  
-  **IDE** : IntelliJ IDEA.

---
---

## 📦 Structure du code

## `ModBus.java`
➡ Classe principale qui hérite de `LiaisonSerie` et centralise la **logique métier** :
- Initialise la communication avec le port série
- Envoie les trames formatées automatiquement
- Attend et lit la réponse
- Vérifie et décode la trame reçue
- Affiche la valeur mesurée dans la console

#### Fonctions utiles :
- `connecterEsclave(...)` : établit la connexion série avec les bons paramètres
- `lectureTension()` / `lectureIntensite()` / `lecturePuissance()` / `lectureFrequence()` : envoie une trame prédéfinie pour chaque type de mesure
- `lectureCoils()` : lecture libre via saisie manuelle d’une trame
- `decoderReponse()` : extrait les octets utiles et retourne la valeur en float
- `serialEvent()` : gère les événements de réception sur le port série

---

## `LiaisonSerie.java`
➡ Classe de base qui encapsule toutes les opérations liées à la **communication série via RS485**.  
Elle utilise la bibliothèque `jSSC` pour gérer les ports série.

#### Fonctionnalités :
-  `listerLesPorts()` : récupère la liste des ports série disponibles sur la machine
-  `initCom(...)` : initialise un port série donné
-  `configurerParametres(...)` : configure les paramètres de la liaison (baudrate, bits de données, parité, stop)
-  `ecrire(byte[])` : envoie une trame (tableau de bytes) sur le port
-  `lire()` / `lireTrame(int)` : lit un octet ou une trame complète depuis le buffer de réception
-  `detecteSiReception()` : vérifie si des données ont été reçues
-  `fermerPort()` : ferme proprement la communication

---

## `Main.java`
➡ Point d’entrée du programme. Cette classe permet de lancer la communication avec le capteur via la console.

#### Fonctionnement :
➡ Demande à l’utilisateur :
   - Le numéro de l’esclave (adresse Modbus)
   - Le port COM à utiliser (ex : COM3, ttyUSB0…)

➡ 🔧 Initialise la liaison série avec les paramètres standards (9600 bauds, 8 bits, parité nulle, 1 bit de stop)

➡ Lance une boucle infinie qui :
   - Envoie une trame pour lire la **fréquence (Hz)**
   - Envoie une trame pour lire la **tension (V)**
   - Envoie une trame pour lire la **puissance (kW)**
   - Envoie une trame pour lire l’**intensité (A)**
   - Attend **60 secondes** avant de recommencer (intervalle de mesure)
  
---

## `CRC16.java`
➡ Calcule le **checksum CRC16** pour garantir l’intégrité des trames Modbus.

## `BigEndian.java`
➡ Convertit les **octets Big Endian** en types exploitables (`int`, `float`, etc.).



---
---

## 🔁 Exemple de Trame

[01] [03] [00] [00] [00] [01] [84] [0A]   <-- Trame envoyée

[01] [03] [02] [09] [10] [BF] [D8]  <-- Réponse du capteur
